package com.fuhao.esb.core.flowcontrol;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.token.TokenConstants;

/**
 * package name is  com.fuhao.esb.core.flowcontrol
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class FlowControlUtils {
    public static boolean applyToken(TokenConstants.FLOW_CONTROL_POOL_TYPE type,String requestName) throws ESBBaseCheckedException{
        return true;
    }

    public static void releaseToken(TokenConstants.FLOW_CONTROL_POOL_TYPE type,String requestName) throws ESBBaseCheckedException{

    }
}
