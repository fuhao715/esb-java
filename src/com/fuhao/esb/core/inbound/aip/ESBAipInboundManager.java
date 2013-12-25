package com.fuhao.esb.core.inbound.aip;

import com.fuhao.esb.common.apimessage.HeadType;
import com.fuhao.esb.common.apimessage.RtnMeg;
import com.fuhao.esb.common.apimessage.Service;
import com.fuhao.esb.common.request.ESBAipAccessMessageReq;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.ESBAipReturnMessageRes;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.common.xml.IXmlPackageTransformer;
import com.fuhao.esb.common.xml.IntegratedPlatformUtil;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.service.ESBServiceUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.exception.ESBBizCheckedException;
import com.fuhao.esb.core.exception.ESBExceptionUtil;
import com.fuhao.esb.core.inbound.AbstractAccessHandler;
import com.fuhao.esb.core.returncode.ReturnCodeConstant;
import com.fuhao.esb.core.returncode.ReturnCodeUtil;
import com.fuhao.esb.core.security.SystemSignConstants;
import com.fuhao.esb.core.util.ESBSystemStateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.inbound.aip
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ESBAipInboundManager extends AbstractAccessHandler {

    private IXmlPackageTransformer xmlTransformer = null;
    @Override
    public IXmlPackageTransformer getXmlPackageTransformer() {
        if (xmlTransformer == null)
            xmlTransformer = new AipXmlTransformer();
        return xmlTransformer;
    }

    @Override
    public IESBReturnMessage getSystemStateBean(){
        return ESBSystemStateUtils.aipAccessBeanState();
    }

    @Override
    protected IESBReturnMessage processException(IESBAccessMessage message,Throwable thr) {
        IESBReturnMessage response = null;
        ESBAipAccessMessageReq req = (ESBAipAccessMessageReq)message;
        if (thr instanceof ESBBizCheckedException) {
            ESBBizCheckedException ex = (ESBBizCheckedException) thr;
            Service service = null;
            if (req ==null){
                service = new Service();
                service.setHead(new HeadType());
            } else
                service = req.getService();
            // 根据异常码获得返回码信息
            Map<String, Object> errorMap = ex.getReasonMap();
            // String exceptionCode = ex.getCode();
            RtnMeg rtnMeg = ReturnCodeUtil.getReturnCode(ReturnCodeConstant.RTN_SUBCODE_BIZ_EXCEPTION,errorMap, ex.getMessage(),ESBExceptionUtil.getStackTraceMessage(ex));
            String errorReason=rtnMeg.getReason()+"，异常所在server名"+ ESBServerContext.getServerName()
                    +"，错误原因："+ (ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            rtnMeg.setReason(errorReason);
            String entireCode =ReturnCodeConstant.RTN_CODE_BIZ_EXCEPTION;   // TODO 预处理服务异常
            service.getHead().setRtnCode(rtnMeg.getDLCodeByCode(entireCode));
            service.getHead().setRtnMeg(rtnMeg);
            response = new ESBAipReturnMessageRes();
            ((ESBAipReturnMessageRes)response).setService(service);
            if (message!=null)
                ESBExceptionUtil.logException(message.getTranID(), message.getTranSeq(), ex.getCause() != null ? ex.getCause() : ex);
        } else if(thr instanceof ESBBaseCheckedException){
            ESBBaseCheckedException ex = (ESBBaseCheckedException) thr;
            Service service = req.getService();
            // 根据异常码获得返回码信息
            Map<String, Object> errorMap;
            errorMap = ex.getParameters();
            errorMap.put("errorMsg","异常所在server名"+ESBServerContext.getServerName()
                        +"，错误原因："+ ex.getMessage());
            //生成返回信息
            RtnMeg rtnMeg = ReturnCodeUtil.getReturnCode(ReturnCodeConstant.RTN_SUBCODE_SYSTEM_EXCEPTION,errorMap,ex.getMessage(),ESBExceptionUtil.getStackTraceMessage(ex));
            String entireCode =ReturnCodeConstant.RTN_CODE_SYSTEM_EXCEPTION;
            //组装返回报文
            service.getHead().setRtnCode(rtnMeg.getDLCodeByCode(entireCode));
            service.getHead().setRtnMeg(rtnMeg);
            response = new ESBAipReturnMessageRes();
            ((ESBAipReturnMessageRes)response).setService(service);
            ESBExceptionUtil.logException(message.getTranID(),message.getTranSeq(),ex);//异常入库
        } else {
            Service service = req.getService();
            RtnMeg rtnMeg = ReturnCodeUtil.getSystemReturnMessage(thr.getMessage(),ESBExceptionUtil.getStackTraceMessage(thr));
            String errorReason=rtnMeg.getReason()+"，异常所在server名"+ESBServerContext.getServerName()
                    +",错误原因："+thr.getMessage();
            rtnMeg.setReason(errorReason);
            service.getHead().setRtnCode(ReturnCodeConstant.RTN_CODE_SYSTEM_DEFAULT_EXCEPTION);
            service.getHead().setRtnMeg(rtnMeg);
            response = new ESBAipReturnMessageRes();
            ((ESBAipReturnMessageRes)response).setService(service);
            ESBExceptionUtil.logException(message.getTranID(),message.getTranSeq(),thr);
        }
        return response;
    }

    @Override
    protected IESBReturnMessage beforeDealTranction(IESBAccessMessage message)
            throws ESBBaseCheckedException {
        ESBAipAccessMessageReq req = (ESBAipAccessMessageReq) message;
        String tranId = message.getTranID();
		/* 跨系统签到修改 ,若上一个交易节点的节点编码为null，则需要签到验证，否则不需要*/
        String nodeBM = message.getNodeCode();
        if (null == nodeBM) {
            // 签到
            if (tranId.equals(SystemSignConstants.EXTERNAL_SERVICE_SIGN_IN)) {
                String systemID = req.getService().getHead().getChannelId();
                String securityPublicKey = req.getService().getHead()
                        .getExpandValue(SystemSignConstants.SECURITY_PUBLIC_KEY);

                // 调用签到服务
                ESBServiceUtils.callService("C00.QX.YYJC.LOGON", systemID,
                        securityPublicKey);// TODO 签到公钥验证

                ESBAipReturnMessageRes respone = new ESBAipReturnMessageRes();
                respone.setService(req.getService());
                respone.setReturnCode("0000");
                return respone;
            } else if (tranId.equals(SystemSignConstants.EXTERNAL_SERVICE_SIGN_OUT)) {// 签退
                String systemID = req.getService().getHead().getChannelId();
                String securityPublicKey = req.getService().getHead()
                        .getExpandValue(SystemSignConstants.SECURITY_PUBLIC_KEY);
                // 调用签退服务
                ESBServiceUtils.callService("C00.QX.YYJC.LOGOUT", systemID,
                        securityPublicKey);    // TODO 签到公钥验证
                ESBAipReturnMessageRes respone = new ESBAipReturnMessageRes();
                respone.setService(req.getService());
                respone.setReturnCode("0000");
                return respone;
            } else if (tranId.equals(SystemSignConstants.EXTERNAL_SERVICE_CONNECT_TEST)) {
                String returnCode = (String) ESBServiceUtils.callService(
                        "C00.LT.YYJC.LTCS", new Object[] {});
                ESBAipReturnMessageRes respone = new ESBAipReturnMessageRes();
                respone.setService(req.getService());
                respone.setReturnCode(returnCode);
                return respone;
            }else if (tranId.equals(SystemSignConstants.EXTERNAL_SERVICE_TRANSACTION_QUERY)){
                String body = req.getService().getBody();
                String ywbw=body.substring(IntegratedPlatformUtil.PREFIX_CDATA.length(), body.length()-IntegratedPlatformUtil.SUFFIX_CDATA.length());
                String responeywbm = (String) ESBServiceUtils.callService(
                        "C00.CX.JYCLZT.JYCX",ywbw);
                ESBAipReturnMessageRes respone = new ESBAipReturnMessageRes();
                Service s = req.getService();
                s.setBody(IntegratedPlatformUtil.PREFIX_CDATA +responeywbm + IntegratedPlatformUtil.SUFFIX_CDATA );
                respone.setService(s);
                respone.setReturnCode("0000");
                return respone;
            }
        }
        return null;
    }
}
