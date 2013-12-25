package com.fuhao.esb.core.route.routepolicy;

import com.fuhao.esb.common.vo.Constants.ROUTE_POLICY;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.route.RoutePolicyInfo;

/**
 * package name is  com.fuhao.esb.core.route.routepolicy
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public interface IRoutePolicyHandler {
    boolean selectRoute(IESBAccessMessage message,RoutePolicyInfo routePolicyInfo)throws ESBBaseCheckedException;
}
