package com.fuhao.esb.core.route;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.route.protocal.IProtocalInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    /*
	 * 路由版本缓存
	 */
    private Long routeVersion = 0L;

    // 路由规则处理器缓存
    private Map<String,RoutePolicyInfo> mapRouteRule = new ConcurrentHashMap<String,RoutePolicyInfo>();

    /**
     * 协议配置缓存
     */
    private Map<String,IProtocalInfo> mapProtocalConf = new ConcurrentHashMap<String,IProtocalInfo>();

    /**
     * 路由配置缓存
     */
    private Map<String,RouteProtocalInfo> mapRouteConf = new ConcurrentHashMap<String,RouteProtocalInfo>();

    public void clean(){
        mapRouteRule.clear();
        mapRouteConf.clear();
        mapProtocalConf.clear();
    }


    // TODO 缓存路由信息
    public void cacheRouteRule(){}
    public void cacheRouteConf(){}
    public void cacheProtocalConf(){}









    public boolean isLogXml(IESBAccessMessage message)throws ESBBaseCheckedException{
        return false; //TODO
    }

    public RouteProtocalInfo getRouteInfor(IESBAccessMessage message) throws ESBBaseCheckedException{
        RouteProtocalInfo routeProtocalInfo = new RouteProtocalInfo(); //TODO 路由查找
        return routeProtocalInfo;
    }


    public Long getRouteVersion() {
        return routeVersion;
    }

    public void setRouteVersion(Long routeVersion) {
        this.routeVersion = routeVersion;
    }
    public Map<String, RoutePolicyInfo> getMapRouteRule() {
        return mapRouteRule;
    }

    public void setMapRouteRule(Map<String, RoutePolicyInfo> mapRouteRule) {
        this.mapRouteRule = mapRouteRule;
    }

    public Map<String, IProtocalInfo> getMapProtocalConf() {
        return mapProtocalConf;
    }

    public void setMapProtocalConf(Map<String, IProtocalInfo> mapProtocalConf) {
        this.mapProtocalConf = mapProtocalConf;
    }

    public Map<String, RouteProtocalInfo> getMapRouteConf() {
        return mapRouteConf;
    }

    public void setMapRouteConf(Map<String, RouteProtocalInfo> mapRouteConf) {
        this.mapRouteConf = mapRouteConf;
    }
}
