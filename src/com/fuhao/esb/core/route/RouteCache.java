package com.fuhao.esb.core.route;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 */
public class RouteCache {
    /**
     * 单例
     */
    private static RouteCache singleton = new RouteCache();
    /**
     * 构造子，私有
     */
    private RouteCache(){
    }

    public static RouteCache getInstance(){
        return singleton;
    }

    public boolean isLogXml(IESBAccessMessage message)throws ESBBaseCheckedException{
        return false; //TODO
    }

    public RouteProtocalInfo getRouteInfor(IESBAccessMessage message) throws ESBBaseCheckedException{
        RouteProtocalInfo routeProtocalInfo = new RouteProtocalInfo(); //TODO 路由查找
        return routeProtocalInfo;
    }

}
