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
