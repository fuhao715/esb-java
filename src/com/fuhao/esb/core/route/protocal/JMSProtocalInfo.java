package com.fuhao.esb.core.route.protocal;

import com.fuhao.esb.common.request.IProtocolConf;

/**
 * package name is  com.fuhao.esb.core.route.protocal
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 */
public class JMSProtocalInfo  implements IProtocalInfo {
    private String protocalID;//协议ID
    private String nodeName;
    private String providerUrls;
    private String contextFactory;
    private String connetFactory;
    private String jndi;
    private String userName;
    private String passWord;
    private String rmiRequestTimeout; // 连接超时
    private String jndiRequestTimeout; // 连接超时
    private String callTimeOutOut;  //调用超时
    private String isSyn;
    private boolean isQueue;  // "true/false"
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

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getProviderUrls() {
        return providerUrls;
    }

    public void setProviderUrls(String providerUrls) {
        this.providerUrls = providerUrls;
    }

    public String getContextFactory() {
        return contextFactory;
    }

    public void setContextFactory(String contextFactory) {
        this.contextFactory = contextFactory;
    }

    public String getConnetFactory() {
        return connetFactory;
    }

    public void setConnetFactory(String connetFactory) {
        this.connetFactory = connetFactory;
    }

    public String getJndi() {
        return jndi;
    }

    public void setJndi(String jndi) {
        this.jndi = jndi;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRmiRequestTimeout() {
        return rmiRequestTimeout;
    }

    public void setRmiRequestTimeout(String rmiRequestTimeout) {
        this.rmiRequestTimeout = rmiRequestTimeout;
    }

    public String getJndiRequestTimeout() {
        return jndiRequestTimeout;
    }

    public void setJndiRequestTimeout(String jndiRequestTimeout) {
        this.jndiRequestTimeout = jndiRequestTimeout;
    }

    public String getCallTimeOutOut() {
        return callTimeOutOut;
    }

    public void setCallTimeOutOut(String callTimeOutOut) {
        this.callTimeOutOut = callTimeOutOut;
    }

    public String getIsSyn() {
        return isSyn;
    }

    public void setIsSyn(String isSyn) {
        this.isSyn = isSyn;
    }

    public boolean isQueue() {
        return isQueue;
    }

    public void setQueue(boolean isQueue) {
        this.isQueue = isQueue;
    }

    @Override
    public String getProtocalID() {
        return this.protocalID;
    }

    @Override
    public IProtocolConf.ProtocolType getProtocalType() {
        return IProtocolConf.ProtocolType.JMS;
    }

    @Override
    public String getNodeName() {
        return this.nodeName;
    }
}
