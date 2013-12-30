package com.fuhao.esb.common.request;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.common.request
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public interface IProtocolConf {
    /**
     * 交易的通讯协议类型
     */
    public static enum ProtocolType { // 支持协议类型
        LOCAL, EJB, JMS, EJB_BEAN, EJB_CONTAINER, JMS_BEAN, JMS_CONTAINER ,WEBSERVICE,HESSIAN,ACTIVEMQ,IBMMQ,RABBITMQ,SOCKET,FTP,LocalXML
    }

    String getProtocolID();

    ProtocolType getProtocolType();

    Map<String, String> getOtherConf();
}
