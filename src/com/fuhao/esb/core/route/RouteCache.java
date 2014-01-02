package com.fuhao.esb.core.route;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.vo.Constants;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.route.protocal.IProtocalInfo;
import com.fuhao.esb.core.route.routepolicy.IRoutePolicyHandler;
import com.fuhao.esb.core.route.routepolicy.RoutePolicyFactory;

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
    private volatile  Long routeVersion = 0L;

    // 路由规则处理器缓存
    private volatile Map<String,RoutePolicyInfo> mapRouteRule = new ConcurrentHashMap<String,RoutePolicyInfo>();

    /**
     * 协议配置缓存
     */
    private volatile Map<String,IProtocalInfo> mapProtocalConf = new ConcurrentHashMap<String,IProtocalInfo>();

    /**
     * 路由配置缓存
     */
    private volatile Map<String,RouteProtocalInfo> mapRouteConf = new ConcurrentHashMap<String,RouteProtocalInfo>();
    /**
     * key 为tranID，value为RouteProtocalInfo对象,
     * 其中这个对象为交易路由缓存map结构，初始化只缓存FULLNAME匹配的
     * 后续根据交易状态路由查找策略，缓存查找的路由信息，不再通过路由规则处理器重新查找。
     * TODO（注意事项，如果处理器规则过期（delete、update）；则需重新刷新此map结构中数据）
     */
    private volatile Map<String,String> mapRouteCacheConf = new ConcurrentHashMap<String,String>();


    public void clean(){
        mapRouteRule.clear();
        mapRouteConf.clear();
        mapProtocalConf.clear();
        mapRouteCacheConf.clear();
    }


    // TODO 缓存路由信息
    public void cacheMapProtocalConf(Map<String, IProtocalInfo> protocalConfigParameters){
       this.mapProtocalConf = protocalConfigParameters;
    }
    public void cacheMapRouteConf(Map<String, RouteProtocalInfo> routConfigParameters){
        this.mapRouteConf = routConfigParameters;
    }
    public void cacheMapRouteRule(Map<String, RoutePolicyInfo> ruleConfigParameters){
        this.mapRouteRule = ruleConfigParameters;
    }



    public boolean isLogXml(IESBAccessMessage message)throws ESBBaseCheckedException{
        return false; //TODO

    }

    public RouteProtocalInfo getRouteInfor(IESBAccessMessage message) throws ESBBaseCheckedException{
        RouteProtocalInfo routeProtocalInfo = null; //TODO 路由查找
        String tranID = message.getTranID();
        // 首先从mapRouteCacheConf中查找
        String protocalInfoID = mapRouteCacheConf.get(tranID);
        if(null != protocalInfoID){
            routeProtocalInfo= mapRouteConf.get(protocalInfoID);
            return routeProtocalInfo;
        }
        //TODO
        /**
         * 1、按照相应匹配策略匹配
         * 2、cache匹配到的这个路由信息。
         */
        Map<String,RoutePolicyInfo> mapRouteRule = RouteCache.getInstance().getMapRouteRule();
        for (String key :mapRouteRule.keySet()) {
            RoutePolicyInfo routePolicyInfo = mapRouteRule.get(key);
            Constants.ROUTE_POLICY route_policy = routePolicyInfo.getRoutePolicy();
            IRoutePolicyHandler routePolicyHandler = RoutePolicyFactory.getRoutePolicyHandler(route_policy.name());
            if(null == routePolicyHandler){
               throw new ESBBaseCheckedException("路由规则处理器未定义");
            }
            boolean isMatch = routePolicyHandler.selectRoute(message,routePolicyInfo);
            if(isMatch){
                protocalInfoID = routePolicyInfo.getRouteProtocalInfoID();
                mapRouteCacheConf.put(tranID,protocalInfoID);
                routeProtocalInfo= mapRouteConf.get(protocalInfoID);
                return routeProtocalInfo;
            }

        }
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

    public Map<String, String> getMapRouteCacheConf() {
        return mapRouteCacheConf;
    }

    public void setMapRouteCacheConf(Map<String, String> mapRouteCacheConf) {
        this.mapRouteCacheConf = mapRouteCacheConf;
    }
}
