package com.fuhao.esb.core.inbound;

import com.fuhao.esb.common.apimessage.Expand;
import com.fuhao.esb.common.request.ESBAipAccessMessageReq;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.common.vo.Constants.SEQLOG_TYPE;
import com.fuhao.esb.common.vo.Constants;
import com.fuhao.esb.common.vo.ESBNodeInfo;
import com.fuhao.esb.common.xml.IXmlPackageTransformer;
import com.fuhao.esb.common.xml.errorxmlreturn.Attachments;
import com.fuhao.esb.common.xml.errorxmlreturn.ESBAipReturnAttachmentMessageRes;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.exception.ESBBizCheckedException;
import com.fuhao.esb.core.flowcontrol.FlowControlUtils;
import com.fuhao.esb.core.logger.BaseMessageLogger;
import com.fuhao.esb.core.logger.BaseTransactionSeqLogger;
import com.fuhao.esb.core.security.BaseAuthentication;
import com.fuhao.esb.core.session.ESBSessionUtils;
import com.fuhao.esb.core.token.TokenConstants;
import com.fuhao.esb.core.transaction.BaseTransManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * package name is  com.fuhao.esb.core.inbound
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public abstract class AbstractAccessHandler {


    /**
     *
     *@name    获取XML解释转换器接口
     */
    protected abstract IXmlPackageTransformer getXmlPackageTransformer();

    /**
     *
     *@name    获取系统状态
     *@Description 相关说明
     *@Time    创建时间:2013-5-20上午12:16:45
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    protected abstract IESBReturnMessage getSystemStateBean();

    /**
     *
     *@name    处理异常
     *@Description 相关说明
     *@Time    创建时间:2013-5-20上午12:41:11
     *@param message
     *@param e
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    protected abstract IESBReturnMessage processException(IESBAccessMessage message,Throwable e);

    /**
     *
     *@name    XML报文接入处理
     *@Description 相关说明
     *@Time    创建时间:2013-5-20上午01:39:10
     *@param xmlMessage
     *@return
     *@throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String processAccessXML(String xmlMessage)throws ESBBaseCheckedException {
//		/* 判断系统是否启动成功，否则不允许接入,返回系统维护中的报文信息*/
//		IESBReturnMessage respEvent = getSystemStateBean();
//		if( respEvent != null)
//			try {
//				return getXmlPackageTransformer().bean2XML(respEvent);
//			} catch (Exception e) {
//				throw new ESBBaseCheckedException("报文xml2Bean格式转换错误！", e);
//			}
        IESBAccessMessage beanMessage = null;
        try {
            beanMessage = getXmlPackageTransformer().xml2Bean(xmlMessage,true);
        } catch (Exception e) {/*报文格式转换错误 */
            ESBLogUtils.getLogger(getClass()).error(e);
            IESBReturnMessage returnBean = processException(null, e);
            try {
                return getXmlPackageTransformer().bean2XML(returnBean);
            } catch (Exception e1) {
                throw new ESBBaseCheckedException("报文bean2XML格式转换错误！", e);
            }
        }
        IESBReturnMessage respEvent = processAccessBean(beanMessage);
        try {
            return getXmlPackageTransformer().bean2XML(respEvent);
        } catch (Exception e) {
            throw new ESBBaseCheckedException("报文bean2XML格式转换错误！", e);
        }//返回转XML
    }

    public String processAccessXML(String xmlMessage,String esbMessageType)throws ESBBaseCheckedException{
//		/* 判断系统是否启动成功，否则不允许接入,返回系统维护中的报文信息*/
//		IESBReturnMessage respEvent = getSystemStateBean();
//		if( respEvent != null)
//			try {
//				return getXmlPackageTransformer().bean2XML(respEvent);
//			} catch (Exception e) {
//				throw new ESBBaseCheckedException("报文xml2Bean格式转换错误！", e);
//			}
        IESBAccessMessage beanMessage = null;
        try {
            beanMessage = getXmlPackageTransformer().xml2Bean(xmlMessage,true);
            beanMessage.setEsbMessageType(esbMessageType);
        } catch (Exception e) {/*报文格式转换错误 */
            ESBLogUtils.getLogger(getClass()).error(e);
            IESBReturnMessage returnBean = processException(null, e);
            try {
                return getXmlPackageTransformer().bean2XML(returnBean);
            } catch (Exception e1) {
                throw new ESBBaseCheckedException("报文bean2XML格式转换错误！", e);
            }
        }
        IESBReturnMessage respEvent = processAccessBean(beanMessage);
        try {
            return getXmlPackageTransformer().bean2XML(respEvent);
        } catch (Exception e) {
            throw new ESBBaseCheckedException("报文bean2XML格式转换错误！", e);
        }//返回转XML
    }

    /**
     *
     *@name    刷新session上下文信息
     *@Description 相关说明
     *@Time    创建时间:2013-7-26上午12:09:33
     *@param message
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private void freshSession(IESBAccessMessage message){
        // 外部系统接入，会转为内部系统调用，需要衔接交易流水号，即在session中set报文中的交易流水号即可为会话ID
        ESBServerContext.getSession().clear();
        ESBServerContext.getSession().setSessionId(message.getTranSeq());
        if (message instanceof ESBAipAccessMessageReq){//TODO:需要数据交换平台共同修改接口后再做此处理
            //设置机构、人员session信息
            ESBAipAccessMessageReq request = (ESBAipAccessMessageReq)message;
            //初始化服务参数放入session
            List<Expand> expands=request.getService().getHead().getExpand();
            if(expands!=null&&expands.size()>0) {
                Iterator<Expand> it = expands.iterator();
                while (it.hasNext()) {
                    Expand expand=it.next();
                    if("sjry".equalsIgnoreCase(expand.getName()))
                        ESBSessionUtils.setUserID(expand.getValue());
                    else if("sjjg".equalsIgnoreCase(expand.getName()))
                        ESBSessionUtils.setOrgID(expand.getValue());
                }
            }
        }
    }

    /**
     *
     *@name    BEAN对象报文接入处理
     *@Description 相关说明
     *@Time    创建时间:2013-5-20上午01:39:36
     *@param message
     *@return
     *@throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    @SuppressWarnings("unchecked")
    public IESBReturnMessage processAccessBean(IESBAccessMessage message) throws ESBBaseCheckedException{
        freshSession(message);
		/* 判断系统是否启动成功，否则不允许接入,返回系统维护中的报文对象*/
        IESBReturnMessage respEvent = getSystemStateBean();
        if( respEvent != null)
            return respEvent;
        long lBegin = System.currentTimeMillis();
        BaseMessageLogger logger = new BaseMessageLogger(message,this.getClass());
//		logger.debug("开始进行统一接入处理");
        try {
            //把ESB服务调用的全局事务串联起来，方便获取调用顺序等调用链路信息（目前只保存系统调用链路）
            String currentNodeBM= ESBNodeInfo.getInstance().getCurrentNodeBM();
            if(currentNodeBM!=null) {
                ArrayList<String> systemTransactionStatusList = (ArrayList<String>)ESBSessionUtils.getTempDataFromSession(Constants.ESB_TRANSACTION_CHAIN);
                if(systemTransactionStatusList == null) {
                    systemTransactionStatusList = new ArrayList<String>();
                    ESBSessionUtils.putTempDataInSession(Constants.ESB_TRANSACTION_CHAIN,systemTransactionStatusList);
                }
                systemTransactionStatusList.add(currentNodeBM);
            }
            //把服务起点原始发送方id放入session
            if(ESBSessionUtils.getTempDataFromSession(Constants.ESB_ORIGINATOR_SYSTEM_ID)==null)
                ESBSessionUtils.putTempDataInSession(Constants.ESB_ORIGINATOR_SYSTEM_ID,message.getChannelID());

            //0.记录交易流水 日志信息在调用接口里面调用
            new BaseTransactionSeqLogger(getXmlPackageTransformer()).logTransaction(SEQLOG_TYPE.TRANS_IN,message);
            //1.权限验证(包括报文合法性检查)
//			logger.info("进行安全认证");
            boolean isPermissionPass = new BaseAuthentication().authentication(message);
            if(isPermissionPass){
//				logger.debug("系统通过权限认证");
                //2.流量控制(内部通过令牌控制)
                String sourceAppID = message.getChannelID();
//				logger.info("系统"+sourceAppID+"请求令牌申请");
                if(FlowControlUtils.applyToken(TokenConstants.FLOW_CONTROL_POOL_TYPE.System, sourceAppID)){
//					logger.debug("系统通过流量控制");
                    try{//3.交易管理(路由\服务\协议适配\消息转换)
                        respEvent =  dealTranction(message);
                    }finally{
//						logger.info("系统"+sourceAppID+"请求令牌释放");
                        FlowControlUtils.releaseToken(TokenConstants.FLOW_CONTROL_POOL_TYPE.System, sourceAppID);
                    }
                }else{
                    ESBBaseCheckedException ex = logger.handleException("当前系统忙,请稍后再试,系统的流量有限,无法获得令牌",null);//当前系统忙,请稍后再试,系统的流量有限,无法获得令牌
                    throw ex;
                }
            }else{
                ESBBaseCheckedException ex = logger.handleException("未通过授权验证",null);//未通过授权验证
                throw ex;
            }
			/*若返回成功设置报文returnCode为0*/
//			logger.debug("----------交易成功-------------");
            if(respEvent.getReturnCode()==null){
                respEvent.setReturnCode("0000");
            }
            dealReturnCode(respEvent);
        } catch (ESBBizCheckedException ex ) {
            logger.error("----异常信息为："+ex.getMessage()+"------",ex);
            respEvent = processException(message, ex);
        }catch (ESBBaseCheckedException e ) {
            logger.error("----异常信息为："+e.getMessage()+"------",e);
            respEvent = processException(message, e);
        }catch (Throwable thr) {
            logger.error("----异常信息为："+thr.getMessage()+"------",thr);
            respEvent = processException(message, thr);
        } finally {//不管有无异常，都要记录交易流水
            logger.debug("整个交易消耗时间为ms:"+ (System.currentTimeMillis() - lBegin));
            respEvent.setTradeInStartTime(message.getTradeInStartTime());
            respEvent.setTradeReqStartTime(message.getTradeReqStartTime());
            new BaseTransactionSeqLogger(getXmlPackageTransformer()).logTransaction(SEQLOG_TYPE.TRANS_OUT,respEvent);
        }
        return respEvent;
    }

    /**
     *
     *@name    设置返回报文
     *@Description 相关说明
     *@Time    创建时间:2013-5-20上午02:05:35
     *@param respEvent
     *@throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    protected void dealReturnCode(IESBReturnMessage respEvent)throws ESBBaseCheckedException{
    }

    /**
     *
     *@name    交易前处理
     *@Description 相关说明
     *@Time    创建时间:2013-5-20上午01:44:23
     *@param message
     *@return
     *@throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    protected abstract IESBReturnMessage beforeDealTranction(IESBAccessMessage message)
            throws ESBBaseCheckedException;

    /**
     *
     *@name    交易处理
     *@Description 相关说明
     *@Time    创建时间:2013-5-20上午01:47:45
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    protected IESBReturnMessage dealTranction(IESBAccessMessage message)
            throws ESBBaseCheckedException {
        IESBReturnMessage beforeMessage = beforeDealTranction(message);
        if (beforeMessage !=null)//如果交易前处理有返回结果，则表示无需进行下面交易处理，直接返回
            return beforeMessage;
        else {
            BaseTransManager transManager = getTransManager();
            transManager.setXmlPackageTransformer(getXmlPackageTransformer());
            return transManager.dealTransaction(message);
        }
    }

    protected BaseTransManager getTransManager(){
        return new BaseTransManager();
    }

    /**
     *
     *@name    XML报文接入处理
     *@Description 相关说明
     *@Time    创建时间:2013-5-20上午01:39:10
     *@param xmlMessage
     *@return
     *@throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public ESBAipReturnAttachmentMessageRes processAccessXMLAttachment(String xmlMessage)throws ESBBaseCheckedException{
		/* 判断系统是否启动成功，否则不允许接入,返回系统维护中的报文信息*/
        IESBReturnMessage respEvent = getSystemStateBean();
        IESBAccessMessage beanMessage = null;
        try {
            beanMessage = getXmlPackageTransformer().xml2Bean(xmlMessage,true);
        } catch (Exception e) {/*报文格式转换错误 */
            ESBLogUtils.getLogger(getClass()).error(e);
            IESBReturnMessage returnBean = processException(null, e);
            try {
                ESBAipReturnAttachmentMessageRes earamr = new ESBAipReturnAttachmentMessageRes();
                Attachments attachments = new Attachments();
                attachments.setResult(getXmlPackageTransformer().bean2XML(returnBean));
                earamr.setAttachments(attachments);
                return earamr;
            } catch (Exception e1) {
                throw new ESBBaseCheckedException("报文bean2XML格式转换错误！", e);
            }
        }
        respEvent = processAccessBean(beanMessage);
        ESBAipReturnAttachmentMessageRes earamr = (ESBAipReturnAttachmentMessageRes)respEvent;
        return earamr;

    }
}
