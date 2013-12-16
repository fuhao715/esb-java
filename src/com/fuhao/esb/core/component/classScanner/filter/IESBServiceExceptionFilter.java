package com.fuhao.esb.core.component.classScanner.filter;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.classScanner.filter
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBServiceExceptionFilter {
    /**
     * @name 异常拦截器
     */
    public void exception(Class<?> serviceClass, String serviceName, Object[] args, ESBBaseCheckedException exception)
            throws ESBBaseCheckedException;
}
