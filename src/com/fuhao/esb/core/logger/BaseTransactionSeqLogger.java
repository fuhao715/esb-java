package com.fuhao.esb.core.logger;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.common.vo.Constants.SEQLOG_TYPE;
import com.fuhao.esb.common.vo.ESBNodeInfo;
import com.fuhao.esb.common.xml.IXmlPackageTransformer;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.AsyTaskExcutor;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.component.utils.ESBSequenceUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.inbound.aip.AipXmlTransformer;
import com.fuhao.esb.core.route.RouteCache;
import com.fuhao.esb.core.route.RoutePolicyInfo;
import com.fuhao.esb.core.route.RouteProtocalInfo;
import com.fuhao.esb.core.session.ESBSessionUtils;
import com.fuhao.esb.core.vo.EsbTransactionChain;
import com.fuhao.esb.core.vo.TransSeqVO;

import java.util.Date;

/**
 * package name is  com.fuhao.esb.core.logger
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class BaseTransactionSeqLogger  implements IESBSeqLogger {
    ESBLogUtils logger = ESBLogUtils.getLogger(this.getClass());
    private IXmlPackageTransformer transformer;

    public BaseTransactionSeqLogger(IXmlPackageTransformer  transformer){
        this.transformer = transformer;
    }

    public BaseTransactionSeqLogger(){
        this.transformer = new AipXmlTransformer();
    }

    /**
     *
     *@name    根据SEQLOG_TYPE返回0,1,2,3
     *@Description 根据SEQLOG_TYPE返回0,1,2,3
     *@Time    创建时间:2011-12-22下午05:04:45
     *@param type
     *@return
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String convertJLDLX2DataBaseFormat(SEQLOG_TYPE type){
        switch(type){
            case TRANS_IN:	return "TRANS_IN";
            case SERVICE_REQ:	return "SERVICE_REQ";
            case SERVICE_RES:	return "SERVICE_RES";
            case TRANS_OUT:	return "TRANS_OUT";
        }
        return "TRANS_UNKNOW";
    }

    /**
     *
     *@name    根据SEQLOG_TYPE是TRANS_IN/SERVICE_REQ还是其他
     *@Description 如果是TRANS_IN/SERVICE_REQ则返回true，否则返回false
     *@Time    创建时间:2011-12-22下午04:58:13
     *@param type
     *@return true/false
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private boolean getJYZTMBySeqType(SEQLOG_TYPE type){
        if(SEQLOG_TYPE.TRANS_IN.equals(type)|| SEQLOG_TYPE.SERVICE_REQ.equals(type)){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     *@name    记录交易信息
     *@Description 相关说明
     *@Time    创建时间:2011-12-13下午04:31:55
     *@param type
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private TransSeqVO getTransSeqVOFromMessageVO(SEQLOG_TYPE type,
                                              IESBAccessMessage message) throws ESBBaseCheckedException {
        TransSeqVO po = new TransSeqVO();
        try {
            po.setSeqUUID(ESBSequenceUtils.generateRandomString());
            po.setSeqNum(message.getTranSeq());
            //将JLDLX根据SEQLOG_TYPE转换为要存到数据库里的0,1,2,3
            po.setLogType(convertJLDLX2DataBaseFormat(type));
            //判断是保存0还是保存returnCode
            if(message instanceof IESBReturnMessage){
                IESBReturnMessage resmessage = (IESBReturnMessage)message;
//				logger.debug("返回码:"+resmessage.getReturnCode());
                po.setTranStatus(getJYZTMBySeqType(type) ? "0" : resmessage.getReturnCode());
            }else{
                po.setTranStatus("0");
            }
            //记录业务数据标识
			/*if (message.getBusinessDataID()!=null)
				po.setBizNum(message.getBusinessDataID());
            po.setTranID(message.getTranID()); */
			/*新增记录节点信息,渠道编码,服务器名 */
            po.setServerName(ESBServerContext.getServerName());
            po.setNodeName(ESBNodeInfo.getInstance().getCurrentNodeBM());
            po.setSystemID(ESBNodeInfo.getInstance().getCurrentSystemID());
            po.setChannelID(message.getChannelID());
            //新增调用序号，20130528
            po.setCallSeqNum(EsbTransactionChain.getCurrentNodeExecutionSequence());
            //录入人员和录入时间
            String loginUser=ESBSessionUtils.getUserID()==null?"admin": ESBSessionUtils.getUserID();

            po.setLoginUser(loginUser);
            po.setLoginTime(new Date());//日期时间以主流程发生时间为准，由于后面是队列
            String threadName = Thread.currentThread().getName();
            if (threadName.length()>100)
                threadName = threadName.substring(0,100);
            po.setThreadID(threadName);
            Object webReqID = ESBSessionUtils.getTempDataIntoSystemContext("web-requestID");//web请求ID
            po.setWebReqID(webReqID==null?null:webReqID.toString());

            //记录下一个交易节点
            if(SEQLOG_TYPE.SERVICE_REQ.equals(type)){
                RouteProtocalInfo routeProtocalInfo = RouteCache.getInstance().getRouteInfor(message);

                if(routeProtocalInfo!=null){//取下一个节点的节点编码
                    String poNextNode = routeProtocalInfo.getNextNode();
                    po.setTargetID(poNextNode);
                }
            }

            boolean isPersistenceMesage  = (Boolean)RouteCache.getInstance().isLogXml(message);
            if(isPersistenceMesage ){// 记录报文
                try {
                    String xml = this.transformer.bean2XML(message);
                    po.setXml(xml);
                } catch (Exception e) {
                    throw new ESBBaseCheckedException("ESB02RZ003003", e); //
                }
            }

        } catch (ESBBaseCheckedException ex) {
            throw new ESBBaseCheckedException("ESB02RZ003002", ex);
        }
        return po;
    }

    /**
     *
     *@name    记录交易流水
     *@Description 将交易流水记录到数据库
     *@Time    创建时间:2011-12-22下午05:02:12
     *@param po
     * @throws ESBBaseCheckedException
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private void logTranSeq(SEQLOG_TYPE type,IESBAccessMessage message,TransSeqVO po) throws ESBBaseCheckedException{
        LogSeqTask lst = new LogSeqTask(po);
        AsyTaskExcutor.getInstance().getThreadPoolExecutor().execute(lst);
    }

    /**
     *
     *@name   记录交易流水和记录报文
     *@Description 负责交易流水的记录，报文暂时不记录
     *@Time    创建时间:2011-12-22下午05:00:40
     *@param type
     *@throws ESBBaseCheckedException
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void logTransaction(SEQLOG_TYPE type,IESBAccessMessage message) {
        try {
            if (!ESBNodeInfo.getInstance().isLogTransactionSeqence() || !RouteCache.getInstance().isLogXml(message)){
                logger.info(message.getTranID()+"记录交易流水开关关闭,不记录流水");
                return;
            }
            BaseMessageLogger logger = new BaseMessageLogger(message,this.getClass());
            logger.debug("开始记录交易流水,类型为"+type);
            TransSeqVO po = getTransSeqVOFromMessageVO(type, message);
            //根据记录类型记录时间
            if(SEQLOG_TYPE.TRANS_IN.equals(type)){
                message.setTradeInStartTime(new Date().getTime());
                // po.setCostTimes(null);
            }else if(SEQLOG_TYPE.SERVICE_REQ.equals(type)){
                message.setTradeReqStartTime(new Date().getTime());
                // po.setCostTimes(null);
            }else if(SEQLOG_TYPE.TRANS_OUT.equals(type)){
                double costTimes = (new Date().getTime()-message.getTradeInStartTime())/1000d;
                po.setCostTimes(costTimes);
                logger.debug("in-out耗时："+costTimes);
            }else if(SEQLOG_TYPE.SERVICE_RES.equals(type)){
                double costTimes = (new Date().getTime()-message.getTradeReqStartTime())/1000d;
                po.setCostTimes(costTimes);
                logger.debug("req-res耗时："+costTimes);
            }
            //设置组织id和sessionid
            po.setOrgID(ESBSessionUtils.getOrgID());
            po.setSessionID(ESBSessionUtils.getSessionID());

            logTranSeq(type, message,po);
//			logger.info("结束记录交易流水");
        }catch(Exception e) {
            //TODO 捕获所有异常，保证记录流水异常不影响业务正常进行
            logger.error("记录交易流水失败",e);
        }

    }
}
