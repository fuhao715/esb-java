package com.fuhao.esb.core.route.protocal;

import com.fuhao.esb.common.request.IProtocolConf;

/**
 * package name is  com.fuhao.esb.core.route.protocal
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 */
public class SocketProtocalInfo  implements IProtocalInfo {
    private String protocalID;
    private String providerUrls;
    private String nodeName;
    private String callTimeOut;  //调用超时

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

    public String getCallTimeOut() {
        return callTimeOut;
    }

    public void setCallTimeOut(String callTimeOut) {
        this.callTimeOut = callTimeOut;
    }

    @Override
    public String getProtocalID() {
        return null;
    }

    @Override
    public IProtocolConf.ProtocolType getProtocalType() {
        return null;
    }

    @Override
    public String getNodeName() {
        return null;
    }
}
