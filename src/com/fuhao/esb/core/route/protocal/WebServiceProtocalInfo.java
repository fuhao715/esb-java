package com.fuhao.esb.core.route.protocal;

import com.fuhao.esb.common.request.IProtocolConf.ProtocolType;

/**
 * package name is  com.fuhao.esb.core.route.protocal
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 */
public class WebServiceProtocalInfo implements IProtocalInfo {
    private String protocalID;//协议ID
    private String nodeName;
    private String wsdl; // wsdl地址
    private String methodName; // 方法名
    private String connetTimeOut; // 连接超时
    private String callTimeOut;  //调用超时
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getWsdl() {
        return wsdl;
    }

    public void setWsdl(String wsdl) {
        this.wsdl = wsdl;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setProtocalID(String protocalID) {
        this.protocalID = protocalID;
    }

    public String getConnetTimeOut() {
        return connetTimeOut;
    }

    public void setConnetTimeOut(String connetTimeOut) {
        this.connetTimeOut = connetTimeOut;
    }

    public String getCallTimeOut() {
        return callTimeOut;
    }

    public void setCallTimeOut(String callTimeOut) {
        this.callTimeOut = callTimeOut;
    }

    @Override
    public String getProtocalID() {
        return this.protocalID;
    }

    @Override
    public ProtocolType getProtocalType() {
        return ProtocolType.WEBSERVICE;
    }

    @Override
    public String getNodeName() {
        return this.nodeName;
    }
}
