package com.fuhao.esb.core.route.protocal;

import com.fuhao.esb.common.request.IProtocolConf;

/**
 * package name is  com.fuhao.esb.core.route.protocal
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 */
public class HessianProtocalInfo  implements IProtocalInfo {
    private String protocalID;
    private String providerUrls;
    private String nodeName;
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setProtocalID(String protocalID) {
        this.protocalID = protocalID;
    }

    public String getProviderUrls() {
        return providerUrls;
    }

    public void setProviderUrls(String providerUrls) {
        this.providerUrls = providerUrls;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String getProtocalID() {
        return this.protocalID;
    }

    @Override
    public IProtocolConf.ProtocolType getProtocalType() {
        return IProtocolConf.ProtocolType.HESSIAN;
    }

    @Override
    public String getNodeName() {
        return this.nodeName;
    }
}
