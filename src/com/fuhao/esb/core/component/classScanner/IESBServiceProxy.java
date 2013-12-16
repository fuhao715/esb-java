package com.fuhao.esb.core.component.classScanner;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.component.service.ESBServiceContainer;

/**
 * package name is  com.fuhao.esb.core.component.classScanner
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBServiceProxy {
    /**
     * 设置服务对象
     */
    public void setService(Object service) throws ESBBaseCheckedException;

    /**
     * 返回服务对象
     */
    public Object getService();

    /**
     * 调用服务
     */
    public Object callService(String serviceName, byte methodIndex, Object[] args, ESBServiceContainer container)
            throws Exception;

}
