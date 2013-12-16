package com.fuhao.esb.core.component.manager;

import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.ServerRunningMode;

/**
 * package name is  com.fuhao.esb.core.component.manager
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ServerContextVisitor implements ServerContextVisitorMXBean {

    @Override
    public String getServerStartTime() {
        return ESBServerContext.getServerStartTime();
    }

    @Override
    public String getHostname() {
        return ESBServerContext.getHostname();
    }

    @Override
    public int getPid() {
        return ESBServerContext.getPid();
    }

    @Override
    public String getServerName() {
        return ESBServerContext.getServerName();
    }

    @Override
    public String getNoeName() {
        return ESBServerContext.getNodeName();
    }

    @Override
    public String getServerType() {
        return ESBServerContext.getServerType();
    }

    @Override
    public String getRackID() {
        return ESBServerContext.getRackID();
    }

    @Override
    public String getClusterName() {
        return ESBServerContext.getClusterName();
    }

    @Override
    public String getServerRunningMode() {
        switch (ESBServerContext.getServerRunningMode()) {
            case ServerRunningMode.PRODUCT_MODE:
                return "Product Mode";
            case ServerRunningMode.DEVELOP_MODE:
                return "Develop Mode";
            case ServerRunningMode.TEST_MODE:
                return "Test Mode";
            default:
                return "Other Mode";
        }
    }
}
