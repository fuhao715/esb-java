package com.fuhao.esb.core.route;


import com.fuhao.esb.core.route.protocal.IProtocalInfo;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class RouteProtocalInfo {
    private String routeProtocalInfoID;
    /**
     * 具体协议信息
     */
    private IProtocalInfo protocalInfo;
    private String protocalID;
    /**
     * 协议提供者节点编码
     * （下一个交易节点）
     */
    private String nextNode = protocalInfo.getNodeName();

    /**
     * 协议提供者节点是否esb节点
     * （下一个交易节点是否esb)
     */
    private boolean nextNodeisESbNode;
    /**
     * 交易前预处理服务
     */
    private String beforeTransPreprocessServerName ;
    /**
     * 交易后预处理服务
     */
    private String afterTransPreprocessServerName ;
    private String memo;

    public String getProtocalID() {
        return protocalID;
    }

    public void setProtocalID(String protocalID) {
        this.protocalID = protocalID;
        protocalInfo =RouteCache.getInstance().getMapProtocalConf().get(protocalID);
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRouteProtocalInfoID() {
        return routeProtocalInfoID;
    }

    public void setRouteProtocalInfoID(String routeProtocalInfoID) {
        this.routeProtocalInfoID = routeProtocalInfoID;
    }

    public IProtocalInfo getProtocalInfo() {
        return protocalInfo;
    }

    public void setProtocalInfo(IProtocalInfo protocalInfo) {
        this.protocalInfo = protocalInfo;
    }

    public String getNextNode() {
        return nextNode;
    }

    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    public boolean isNextNodeisESbNode() {
        return nextNodeisESbNode;
    }

    public void setNextNodeisESbNode(boolean nextNodeisESbNode) {
        this.nextNodeisESbNode = nextNodeisESbNode;
    }

    public String getBeforeTransPreprocessServerName() {
        return beforeTransPreprocessServerName;
    }

    public void setBeforeTransPreprocessServerName(String beforeTransPreprocessServerName) {
        this.beforeTransPreprocessServerName = beforeTransPreprocessServerName;
    }

    public String getAfterTransPreprocessServerName() {
        return afterTransPreprocessServerName;
    }

    public void setAfterTransPreprocessServerName(String afterTransPreprocessServerName) {
        this.afterTransPreprocessServerName = afterTransPreprocessServerName;
    }
}
