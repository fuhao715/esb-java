package com.fuhao.esb.core.route;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-30.
 * Project Name esb-java
 */
public class RouteManager {
    private ESBLogUtils logger = ESBLogUtils.getLogger(RouteFileUtils.class);
    /*
    获取路由
     */
    public RouteProtocalInfo getRoute(IESBAccessMessage message) throws ESBBaseCheckedException{
        RouteProtocalInfo routeProtocalInfo = RouteCache.getInstance().getRouteInfor(message);
        if(null == routeProtocalInfo ){
            logger.error("路由信息未找到");
             throw  new ESBBaseCheckedException("路由信息未找到");
        }
        return  routeProtocalInfo;
    }
}
