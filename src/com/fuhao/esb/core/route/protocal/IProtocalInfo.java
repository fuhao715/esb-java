package com.fuhao.esb.core.route.protocal;

import com.fuhao.esb.common.request.IProtocolConf.ProtocolType;

/**
 * package name is  com.fuhao.esb.core.route.protocal
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public interface IProtocalInfo {
    /**
     * 获得协议ID
     * @return
     */
    public String getProtocalID();
    /**
     * 协议类型
     */
    public ProtocolType getProtocalType();
    /**
     * 获取协议提供者节点信息
     */
    public String getNodeName();
    public String getMemo() ;
}
