package com.fuhao.esb.core.route.routepolicy;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.route.RoutePolicyInfo;

/**
 * package name is  com.fuhao.esb.core.route.routepolicy
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class DynamicRouteLocateHandler implements IRoutePolicyHandler {
    @Override
    public boolean selectRoute(IESBAccessMessage message, RoutePolicyInfo routePolicyInfo) throws ESBBaseCheckedException {
        //TODO 动态路由算法，根据message中的目标点id进行递归运算出对应的路由信息（前提其中需要规范交易节点编码的规范）

        return false;
    }
}
