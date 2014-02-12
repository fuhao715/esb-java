package com.fuhao.esb.core.route.protocal;

import com.fuhao.esb.common.request.IProtocolConf;

/**
 * package name is  com.fuhao.esb.core.route.protocal
 * Created by fuhao on 14-2-12.
 * Project Name esb-java
 */
public class DynamicProtocalInfo implements IProtocalInfo{
    private String protocalID;//协议ID
    private String clientClass;// 外挂服务名
    private String nodeName;
    private String memo;

    public void setProtocalID(String protocalID) {
        this.protocalID = protocalID;
    }

    public String getClientClass() {
        return clientClass;
    }

    public void setClientClass(String clientClass) {
        this.clientClass = clientClass;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String getProtocalID() {
        return protocalID;
    }

    @Override
    public IProtocolConf.ProtocolType getProtocalType() {
        return IProtocolConf.ProtocolType.DYNAMIC;
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public String getMemo() {
        return memo;
    }
}
