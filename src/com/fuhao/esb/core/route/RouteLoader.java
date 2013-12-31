package com.fuhao.esb.core.route;

import com.fuhao.esb.common.vo.Constants.ROUTE_POLICY;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-26.
 * Project Name esb-java
 */
public class RouteLoader {

    /*
    加载路由
     */
    public void loadRoute() throws ESBBaseCheckedException{
        RouteFileUtils.loadRoutConfig(); // 读取配置文件并缓存
        handleFullNameMatch();
        // cache  全匹配路由算法，以及访问次数缓存算法。
        /**
         * 路由匹配策略兼容RESTFULL路由方式，即http请求在servlet拦截获取url即为交易码。
         * 匹配策略优先级为：
         * 1、全匹配
         * 2、startwith、endwith，Regex
         * 3、用户自定义匹配，User_defined
         * 4、动态匹配---因为递归算法效率相对低，故而优先级降低。
         *    动态匹配要求：报文中tranID为交易目标对象的节点渠道码（channel_id）
         *    根据当前节点的下一个交易节点（必须为esb节点），递归探索下一个节点的下一个节点
         *    直到equal报文中的tranID,则返回路由信息。----有向图算法

         * 5、Any匹配
         */

    }

    public void handleFullNameMatch(){
        Map<String,String> tempMapRouteRule = new ConcurrentHashMap<String,String>();

        Map<String,RoutePolicyInfo> mapRouteRule = RouteCache.getInstance().getMapRouteRule();
        for (String key :mapRouteRule.keySet()) {
            RoutePolicyInfo routePolicyInfo = mapRouteRule.get(key);
            ROUTE_POLICY route_policy = routePolicyInfo.getRoutePolicy();
            if(route_policy == ROUTE_POLICY.FullName){
                tempMapRouteRule.put(routePolicyInfo.getProtocalEL(),routePolicyInfo.getRouteProtocalInfoID());
            }
        }
        RouteCache.getInstance().setMapRouteCacheConf(tempMapRouteRule);
    }

}
