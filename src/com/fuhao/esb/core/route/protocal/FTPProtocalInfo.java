package com.fuhao.esb.core.route.protocal;

import com.fuhao.esb.common.request.IProtocolConf;

/**
 * package name is  com.fuhao.esb.core.route.protocal
 * Created by fuhao on 13-12-26.
 * Project Name esb-java
 */
public class FTPProtocalInfo implements IProtocalInfo {
    //TODO
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
