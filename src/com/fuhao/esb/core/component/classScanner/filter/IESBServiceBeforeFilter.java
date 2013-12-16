package com.fuhao.esb.core.component.classScanner.filter;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.classScanner.filter
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBServiceBeforeFilter {
    /**
     * 前项拦截器
     * 在服务调用前执行拦截操作
     */
    public void before(Class<?> serviceClass, String serviceName, Object[] args) throws ESBBaseCheckedException;
}
