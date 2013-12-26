package com.fuhao.esb.core.route.protocal;

import com.fuhao.esb.common.request.IProtocolConf;

/**
 * package name is  com.fuhao.esb.core.route.protocal
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 */
public class EJBProtocalInfo  implements IProtocalInfo {
    private String protocalID;//协议ID
    private String nodeName;
    private String providerUrls;
    private String contextFactory;
    private String jndi;
    private String userName;
    private String passWord;
    private String connetTimeOut; // 连接超时
    private String callTimeOut;  //调用超时
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
    public IProtocolConf.ProtocolType getProtocalType() {
        return IProtocolConf.ProtocolType.EJB;
    }

    @Override
    public String getNodeName() {
        return this.nodeName;
    }
}
