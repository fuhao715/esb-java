package com.fuhao.esb.core.transaction;

import com.fuhao.esb.client.IBaseESBClientMessageSender;
import com.fuhao.esb.common.apimessage.HeadType;
import com.fuhao.esb.common.apimessage.Service;
import com.fuhao.esb.common.request.IProtocolConf;
import com.fuhao.esb.common.request.IProtocolConf.ProtocolType;
import com.fuhao.esb.common.request.ObjectAccessMessage;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.ESBAipReturnMessageRes;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.common.response.ObjectReturnMessageRes;
import com.fuhao.esb.common.vo.Constants.SEQLOG_TYPE;
import com.fuhao.esb.common.vo.ESBNodeInfo;
import com.fuhao.esb.common.xml.IXmlPackageTransformer;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.service.DefaultMediationRequest;
import com.fuhao.esb.core.component.service.ESBServiceUtils;
import com.fuhao.esb.core.component.service.IMediationResponse;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.exception.JMSMarkPutBackMessageException;
import com.fuhao.esb.core.logger.BaseMessageLogger;
import com.fuhao.esb.core.logger.BaseTransactionSeqLogger;
import com.fuhao.esb.core.route.BaseCommunicationProtocalFactory;
import com.fuhao.esb.core.route.RouteCache;
import com.fuhao.esb.core.route.RouteManager;
import com.fuhao.esb.core.route.RouteProtocalInfo;
import com.fuhao.esb.core.route.protocal.ExtHangProtocalInfo;
import com.fuhao.esb.core.session.ESBSessionUtils;
import com.fuhao.esb.core.util.IPUtils;

import java.io.EOFException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.CommunicationException;
import javax.naming.ServiceUnavailableException;

/**
 * package name is  com.fuhao.esb.core.transaction
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class BaseTransManager {
    /**
     * XML解释转换器
     */
    private IXmlPackageTransformer xmlTransformer = null;

    public IXmlPackageTransformer getXmlPackageTransformer() {
        return xmlTransformer;
    }

    public void setXmlPackageTransformer(IXmlPackageTransformer xmlTransformer) {
        this.xmlTransformer = xmlTransformer;
    }

    /**
     *
     *@name    执行ESB内部的交易服务
     *@Description 相关说明
     *@Time    创建时间:2012-2-29上午08:50:48
     *@param req
     *@param logger
     *@return
     *@throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private IESBReturnMessage executeObjectTransaction(ObjectAccessMessage req,RouteProtocalInfo routeProtocalInfo,BaseMessageLogger logger) throws ESBBaseCheckedException {
		logger.debug("---------记录交易前的流水------");
		new BaseTransactionSeqLogger().logTransaction(SEQLOG_TYPE.SERVICE_REQ, req);
        Object bizObj = req.getBizObj();// 获取业务对象
        // 判断协议类型
        ProtocolType protocalType= routeProtocalInfo.getProtocalInfo().getProtocalType();
        if(protocalType != ProtocolType.EXTHANG){
            ESBBaseCheckedException ex = logger.handleExceptionReason("协议类型不匹配",null);
            throw ex;
        }

        ExtHangProtocalInfo extHangProtocalInfo = (ExtHangProtocalInfo)routeProtocalInfo.getProtocalInfo();
        String serviceName = extHangProtocalInfo.getServiceName();
        Object response =   ESBServiceUtils.callService(serviceName,bizObj);

        ObjectReturnMessageRes resMessage = new ObjectReturnMessageRes();
        resMessage.setReturnCode("0");
        resMessage.setTranSeq(req.getTranSeq());
        resMessage.setTranID(req.getTranID());
        resMessage.setChannelID(req.getChannelID());
        resMessage.setBizObj(response);
		logger.debug("---------记录交易后的流水------");
		new BaseTransactionSeqLogger().logTransaction(SEQLOG_TYPE.SERVICE_RES,resMessage);
        return resMessage;
    }

    /**
     *
     *@name    处理外部系统的交易服务
     *@throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private IESBReturnMessage dealExternalTransaction(IESBAccessMessage message,RouteProtocalInfo routeProtocalInfo,BaseMessageLogger logger,
                                                      IBaseESBClientMessageSender sender) throws ESBBaseCheckedException {
//		logger.debug("调用外部服务send xml出去开始：");
        new BaseTransactionSeqLogger(getXmlPackageTransformer()).logTransaction(SEQLOG_TYPE.SERVICE_REQ, message);
        String xml=null;
        try {
            xml = getXmlPackageTransformer().bean2XML(message);
        } catch (Exception e) {
            throw new ESBBaseCheckedException("报文bean2XML格式转换错误！", e);
        }
        String resXml=null;
        try {
			/*sender根据wsdl地址进行访问 */
//			logger.debug("调用外部服务开始，发送报文为："+xml);
            resXml = sender.sendXML(xml);
        }catch (ESBBaseCheckedException ex) {
            dealExcetionByRequestType(message, ex);
            throw ex;
        }catch (Throwable e) {
            dealExcetionByRequestType(message, e);
            ESBBaseCheckedException ex = logger.handleException("调用外部服务，发送报文方式错误",e);
            throw ex;
        }
//		logger.debug("调用外部服务结束，返回报文为："+resXml);
        // XmlValidatorUtil.validateXMLByXSD(com.css.ESB.esb.comm.request.Constants.MESSAGE_YYJC,resXml);
        if(null == resXml || "".equals(resXml)){
            logger.info("返回报文为空，则把请求报文返回");
            resXml = xml;
        }
//        logger.debug("发送报文信息完成");
        /*根据返回报文转换为对象 */
        IESBReturnMessage response=null;
        try {
            response = (IESBReturnMessage)getXmlPackageTransformer().xml2Bean(resXml,false);
        } catch (Exception e) {
            throw new ESBBaseCheckedException("返回报文xml2Bean格式转换错误！", e);
        }
        response.setTradeReqStartTime(message.getTradeReqStartTime());//
        new BaseTransactionSeqLogger(getXmlPackageTransformer()).logTransaction(SEQLOG_TYPE.SERVICE_RES,response);
//		logger.debug("------------调用外部服务send出去结束：----------------");
        return response;
    }

    public IESBReturnMessage dealTransaction(IESBAccessMessage message) throws ESBBaseCheckedException {
        BaseMessageLogger logger = new BaseMessageLogger(message,this.getClass());
//		logger.info("进入交易处理环节");
        //路由查找匹配
        RouteProtocalInfo routeProtocalInfo = new RouteManager().getRoute(message);
        if (routeProtocalInfo == null){//路由信息没有找到
            Map<String,Object> mapReason = new HashMap<String,Object>();
            mapReason.put("tranid", message.getTranID());
            ESBBaseCheckedException ex = logger.handleExceptionReason("路由信息未找到", mapReason);
            throw ex;
        }
        IESBReturnMessage response = null;
        if (message instanceof ObjectAccessMessage){//表示ESB内部调用callservcie
            response = executeObjectTransaction((ObjectAccessMessage) message, routeProtocalInfo, logger);
            return response;
        }
        // 先获取路由，如果调用时在当前节点在判断对象是否是InternalAccessMessage

        //服务调用
        boolean isNextNodeEsb = routeProtocalInfo.isNextNodeisESbNode();
        if (!isNextNodeEsb){ //下一个节点若为非esb节点，则调用外部客户端消息钻发出去
            logger.debug("在当前节点调用服务，服务所属交易节点为"+routeProtocalInfo.getNextNode());
            /*if (serviceProtocalInfo.getProtocalType() == COMMUN_PROTOCOL_TYPE.Local){
                //本地服务调用，使用Service对象方式
//				logger.debug("外部系统交易"+message.getTranID()+"将内部调用本地服务");
                response = executeLocalTransaction(message,routeProtocalInfo,logger);
            }else if(serviceProtocalInfo.getProtocalType() == COMMUN_PROTOCOL_TYPE.LocalXML){
                //本地服务调用，使用XML方式
                response = executeLocalTransactionInXML(message,routeProtocalInfo,logger);
            }else{*/
                IBaseESBClientMessageSender sender = getAdapter(message, routeProtocalInfo,logger);
				logger.debug("服务挂在目标节点，直接调用目标端的服务");
                beforeDealSend(message, routeProtocalInfo, logger);
                response = dealExternalTransaction(message,routeProtocalInfo,logger,sender);
                afterDealSend(routeProtocalInfo,response, logger);
            // }
        }else{
            //服务不在当前ESB节点，则继续进行内部路由，通过内部Javabean消息格式
            logger.debug("服务继续路由到下一个ESB交易节点"+routeProtocalInfo.getNextNode());
            try {
                String ipLocal = IPUtils.getLocalIP();
                message.setIP(ipLocal);
            } catch (Throwable e) {
                logger.handleException("路由到下一个节点前获取本级IP错误", e);//路由到下一个节点前获取本级IP错误
            }

            try {
				logger.debug("------------继续路由到下一个节点send出去开始：----------------");
  				/* set SourceAppID 获得当前节点编码 */
                String currentNodeBM = ESBNodeInfo.getInstance().getCurrentNodeBM();
                message.setNodeCode(currentNodeBM);
                beforeDealSend(message, routeProtocalInfo, logger);
                new BaseTransactionSeqLogger(getXmlPackageTransformer()).logTransaction(SEQLOG_TYPE.SERVICE_REQ, message);
                IBaseESBClientMessageSender sender = getAdapter(message, routeProtocalInfo,logger);
                response = sender.send(message);

                //todo当异步时返回为null，需要判断，返回一个报文可客户端，内容待定；
                dealCommonReply(response,routeProtocalInfo,message);

                afterDealSend(routeProtocalInfo, response, logger);
                response.setTradeReqStartTime(message.getTradeReqStartTime());
                new BaseTransactionSeqLogger(getXmlPackageTransformer()).logTransaction(SEQLOG_TYPE.SERVICE_RES, response);
// 				logger.debug("------------继续路由到下一个节点send出去结束：----------------");
            }catch (Throwable e) {
                new BaseTransactionSeqLogger(getXmlPackageTransformer()).logTransaction(SEQLOG_TYPE.SERVICE_RES, message);
                dealExcetionByRequestType(message, e);
                ESBBaseCheckedException ex = logger.handleException("ESB内部调用出错", e);//调用内部服务发生异常
                throw ex;
            }
            logger.info("服务路由到下一个交易节点结束" + routeProtocalInfo.getNextNode());
        }
        return  response;
    }

    public void dealExcetionByRequestType(IESBAccessMessage message,
                                          Throwable e) throws JMSMarkPutBackMessageException {
        String type = message.getEsbMessageType();
        if ("jms".equals(type)) {
            while(e!=null){
                if(checkException(e)){
                    throw new JMSMarkPutBackMessageException(e);
                }
                e = e.getCause();
            }
        }
    }

    private boolean checkException(Throwable e){
        if(e instanceof CommunicationException||
                e instanceof EOFException ||
                e instanceof NoRouteToHostException ||
                e instanceof PortUnreachableException ||
                e instanceof ServiceUnavailableException||
                e instanceof java.rmi.UnknownHostException ||
                e instanceof java.net.UnknownHostException ||
                e instanceof java.rmi.ConnectException||
                e instanceof java.net.ConnectException||
                e instanceof NoSuchMethodException||
                e instanceof JMSMarkPutBackMessageException){
            return true;
        }
        return false;
    }

    private void dealCommonReply(IESBReturnMessage response,RouteProtocalInfo routeProtocalInfo,IESBAccessMessage req){
        /**TODO
         * 异步方式后返回报文为通用回复报文
         */
        if(null == response){
            ProtocolType protocalType= routeProtocalInfo.getProtocalInfo().getProtocalType();
            boolean isSyn =IProtocolConf.synProtocolType.getEnumNames().contains(protocalType.name());
            if(isSyn){
                ESBAipReturnMessageRes resMessage = new ESBAipReturnMessageRes();
                resMessage.setReturnCode("0");
                Service service = new Service();
                HeadType head = new HeadType();
                head.setTranSeq(req.getTranSeq());
                head.setTranId(req.getTranID());
                head.setChannelId(req.getChannelID());
                service.setHead(head);
                resMessage.setService(service);
                response = resMessage;
            }
        }

    }

   /* private boolean isServiceInCurrentEsb(RouteProtocalInfo routeProtocalInfo) throws ESBBaseCheckedException {
        boolean isServiceInCurrentEsb = ESBNodeInfo.getInstance().getCurrentNodeID().equals(routeProtocalInfo.getNextNode());
        if (!isServiceInCurrentEsb){//继续看下一个交易节点是否为ESB节点

            isServiceInCurrentEsb = routeProtocalInfo.isNextNodeisESbNode();//如果不是ESB节点,则表示还是在当前节点调用服务
        }
        return isServiceInCurrentEsb;
    }*/

    protected IBaseESBClientMessageSender getAdapter(IESBAccessMessage message, RouteProtocalInfo routeProtocalInfo,BaseMessageLogger logger)
            throws ESBBaseCheckedException{
        //协议适配
//		logger.info("进行协议适配");
        IBaseESBClientMessageSender sender = BaseCommunicationProtocalFactory.getAdapter(routeProtocalInfo, message);
        if(sender == null){//协议适配错误,无法获得协议适配器
            ESBBaseCheckedException ex = logger.handleException("协议适配错误,无法获得协议适配器",null);
            throw ex;
        }
        return sender;
    }

    protected void beforeDealSend(IESBAccessMessage message, RouteProtocalInfo routeProtocalInfo, BaseMessageLogger logger)
            throws ESBBaseCheckedException{
        //获取转换器服务类
        String serviceName   = routeProtocalInfo.getBeforeTransPreprocessServerName();
        logger.debug("本地交易调用节点为："+routeProtocalInfo.getNextNode()+"的服务名："+serviceName);
        Object ret = ESBServiceUtils.callService(serviceName, message.getBizObj());//需要消息转换器的参数改为IESBAccessMessage和IESBReturnMessage
        if (ret == null)
            //外部系统调用本地服务返回对象不符合要求，必须为IESBReturnMessage而且不能为null
            throw new ESBBaseCheckedException("交易前预处理本地服务返回对象参数不符合要求，必须为IESBReturnMessage而且不能为null");//本地服务返回对象参数不符合要求，必须为IESBReturnMessage而且不能为null
        else
            message.setBizObj(ret);
    }

    protected void afterDealSend(RouteProtocalInfo routeProtocalInfo, IESBReturnMessage response, BaseMessageLogger logger)
            throws ESBBaseCheckedException{
        //获取交易后转换器服务类
        String serviceName   = routeProtocalInfo.getAfterTransPreprocessServerName();
        logger.debug("本地交易调用节点为："+routeProtocalInfo.getNextNode()+"的服务名："+serviceName);
        Object ret = ESBServiceUtils.callService(serviceName, response.getBizObj());//需要消息转换器的参数改为IESBAccessMessage和IESBReturnMessage
        if (ret == null)
            //外部系统调用本地服务返回对象不符合要求，必须为IESBReturnMessage而且不能为null
            throw new ESBBaseCheckedException("交易后预处理本地服务返回对象参数不符合要求，必须为IESBReturnMessage而且不能为null");//本地服务返回对象参数不符合要求，必须为IESBReturnMessage而且不能为null
        else
            response.setBizObj(ret);
    }

}
