package com.fuhao.esb.core.component;

import com.fuhao.esb.core.component.classScanner.IESBServiceProxy;
import com.fuhao.esb.core.component.utils.CheckUtils;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
public abstract class AbsESBServiceProxy implements IESBServiceProxy {
    protected ESBLogUtils log;

    protected Object service;

    @Override
    public void setService(Object service) throws ESBBaseCheckedException {
        if (ESBServerContext.isDevelopMode()) {
            CheckUtils.callSecurityCheck();
        }
        this.service = service;
        this.log = ESBLogUtils.getLogger(this.service.getClass());
    }

    @Override
    public Object getService() {
        return this.service;
    }
}
