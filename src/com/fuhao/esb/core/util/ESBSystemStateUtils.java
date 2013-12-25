package com.fuhao.esb.core.util;

import com.fuhao.esb.common.apimessage.HeadType;
import com.fuhao.esb.common.apimessage.RtnMeg;
import com.fuhao.esb.common.apimessage.Service;
import com.fuhao.esb.common.response.ESBAipReturnMessageRes;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.common.vo.ESBNodeInfo;
import com.fuhao.esb.core.component.AbsESBComponentManager;
import com.fuhao.esb.core.component.ESBComponentRef;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.returncode.ReturnCodeConstant;
import com.fuhao.esb.core.session.ESBSessionUtils;

import java.text.SimpleDateFormat;

/**
 * package name is  com.fuhao.esb.core.util
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ESBSystemStateUtils {

    private static final ESBLogUtils logger = ESBLogUtils.getLogger(ESBSystemStateUtils.class);
    public static void internalAccessState()throws ESBBaseCheckedException{
        checkSystemState();
    }

    public static IESBReturnMessage aipAccessBeanState(){
        ESBAipReturnMessageRes returnBean = null;
        try {
            checkSystemState();
        } catch (ESBBaseCheckedException e) {
            returnBean  = new ESBAipReturnMessageRes();
            Service service = new Service();
            HeadType head = new HeadType();
            head.setChannelId(ESBNodeInfo.getInstance().getCurrentNodeBM());
            head.setTranId("C00.LT.YYJC.XTZT");//通用回复报文报文交易码，系统状态未启动
            head.setTranSeq(ESBSessionUtils.getSessionID());
            java.util.Date date = new java.util.Date();
            SimpleDateFormat tran_date = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat tran_time = new SimpleDateFormat("hhmmssSSS");
            head.setTranDate(tran_date.format(date));
            head.setTranTime(tran_time.format(date));
            service.setHead(head);

            RtnMeg rtnMeg = new RtnMeg();
            rtnMeg.setCode(ReturnCodeConstant.RTN_SUBCODE_SYSTEM_EXCEPTION);
            rtnMeg.setMessage("系统维护升级中");
            rtnMeg.setReason(e.getMessage());
            service.getHead().setRtnCode(ReturnCodeConstant.RTN_CODE_SYSTEM_EXCEPTION);
            service.getHead().setRtnMeg(rtnMeg);
            returnBean.setService(service);
            //logger.error(e);//在checkSystemState中主动抛的异常没有必要再log
        }
        return returnBean ;
    }

    public static void checkSystemState()throws ESBBaseCheckedException{
        if(ESBServerContext.getServerState() != AbsESBComponentManager.ComponentState.RUNNING
                //ESB也要启动
                || ((AbsESBComponentManager)ESBComponentRef.esbManager).getComponentState()!= AbsESBComponentManager.ComponentState.RUNNING){
            String serverName = ESBServerContext.getServerName();
            if(serverName == null){
                if ((serverName = System.getProperty("weblogic.Name")) == null) {
                    // 当为web服务器时，当前域如果不是主域（/），则使用子目录名作为服务器名
                    serverName = System.getProperty("ESB.server.name");
                }
            }
            String msg = "节点"+ESBServerContext.getNodeName()+"的Server名:"+serverName+"的应用系统ESB没有成功启动";
            logger.warn(msg);
            throw new ESBBaseCheckedException(msg);
        }
    }

    public static void isErrorSystemState()throws ESBBaseCheckedException {
        if(ESBServerContext.getServerState() == AbsESBComponentManager.ComponentState.ERROR
                || ((AbsESBComponentManager) ESBComponentRef.esbManager).getComponentState()== AbsESBComponentManager.ComponentState.ERROR){
            String serverName = ESBServerContext.getServerName();
            if(serverName == null){
                if ((serverName = System.getProperty("weblogic.Name")) == null) {
                    // 当为web服务器时，当前域如果不是主域（/），则使用子目录名作为服务器名
                    serverName = System.getProperty("ESB.server.name");
                }
            }
            String msg = "节点"+ESBServerContext.getNodeName()+"的Server名:"+serverName+"的应用系统ESB启动失败";
            logger.warn(msg);
            throw new ESBBaseCheckedException(msg);
        }
    }
}
