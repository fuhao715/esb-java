package com.fuhao.esb.common.request;

import com.fuhao.esb.common.utils.CollectionUtils;

import java.util.Arrays;
import java.util.List;
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
        LOCAL, EJB, JMS, EJB_BEAN, EJB_CONTAINER, JMS_BEAN, JMS_CONTAINER ,WEBSERVICE,HESSIAN,ACTIVEMQ,IBMMQ,RABBITMQ,SOCKET,FTP,LocalXML,DYNAMIC,EXTHANG
    }

    public static enum synProtocolType{ // 异步协议类型
        JMS, JMS_BEAN, JMS_CONTAINER , ACTIVEMQ, IBMMQ, RABBITMQ,DYNAMIC,EXTHANG;
        public static List<String> getEnumNames() {
            return CollectionUtils.getValuesNames(Arrays.asList(values()));
        }
    }

    String getProtocolID();

    ProtocolType getProtocolType();

    Map<String, String> getOtherConf();
}
