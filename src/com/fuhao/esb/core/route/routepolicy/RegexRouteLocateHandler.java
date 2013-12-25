package com.fuhao.esb.core.route.routepolicy;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.route.RoutePolicyInfo;

import java.util.regex.Pattern;

/**
 * package name is  com.fuhao.esb.core.route.routepolicy
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class RegexRouteLocateHandler implements IRoutePolicyHandler {
    @Override
    public boolean selectRoute(IESBAccessMessage message, RoutePolicyInfo routePolicyInfo) throws ESBBaseCheckedException {
        String tranID = message.getTranID();
        if(Pattern.matches(routePolicyInfo.getProtocalEL(), tranID))
            return true;
        return false;
    }
}
