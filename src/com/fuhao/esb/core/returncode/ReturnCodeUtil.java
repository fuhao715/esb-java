package com.fuhao.esb.core.returncode;

import com.fuhao.esb.common.apimessage.RtnMeg;
import com.fuhao.esb.core.component.utils.TemplateUtils;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.returncode
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ReturnCodeUtil {
    public static RtnMeg getReturnCode(String subCode,Map errorMap,String originalErrorMsg,String stackTraceMessage){
        RtnMeg rtnMeg = new RtnMeg();
        rtnMeg.setCode(subCode);
        rtnMeg.setMessage(originalErrorMsg);
        rtnMeg.setReason("原始错误信息："+stackTraceMessage);

        RtnMeg rtnMegClone=(RtnMeg)rtnMeg.clone();
        if(null != errorMap && errorMap.size()>0){
            rtnMegClone.setReason(TemplateUtils.generateString(rtnMegClone.getReason(), errorMap));
        }

        return rtnMegClone;
    }

    public static RtnMeg getSystemReturnMessage(String errorMessage,String stackTraceMessage){
        if(errorMessage==null)
        {
            errorMessage="未知错误！";
        }
        RtnMeg rtnMeg = new RtnMeg();
        rtnMeg.setCode(ReturnCodeConstant.RTN_SUBCODE_SYSTEM_DEFAULT_EXCEPTION);
        rtnMeg.setMessage(errorMessage);
        rtnMeg.setReason("原始错误信息："+stackTraceMessage);
        return rtnMeg;
    }
}
