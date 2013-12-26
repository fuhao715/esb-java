package com.fuhao.esb.core.route;

import com.fuhao.esb.common.vo.Constants;
import com.fuhao.esb.common.vo.Constants.ROUTE_POLICY;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class RoutePolicyInfo {
    private String routeRuleID;
    /**
     * 协议路由处理表达式
     */
    private String protocalEL;
    /**
     * 协议处理器类型
     */
    private ROUTE_POLICY routePolicy;

    /**
     * 路由信息id
     */
    private String routeProtocalInfoID;
    private String memo;

    public String getRouteRuleID() {
        return routeRuleID;
    }

    public void setRouteRuleID(String routeRuleID) {
        this.routeRuleID = routeRuleID;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getProtocalEL() {
        return protocalEL;
    }

    public void setProtocalEL(String protocalEL) {
        this.protocalEL = protocalEL;
    }

    public ROUTE_POLICY getRoutePolicy() {
        return routePolicy;
    }

    public void setRoutePolicy(ROUTE_POLICY routePolicy) {
        this.routePolicy = routePolicy;
    }

    public String getRouteProtocalInfoID() {
        return routeProtocalInfoID;
    }

    public void setRouteProtocalInfoID(String routeProtocalInfoID) {
        this.routeProtocalInfoID = routeProtocalInfoID;
    }
}
