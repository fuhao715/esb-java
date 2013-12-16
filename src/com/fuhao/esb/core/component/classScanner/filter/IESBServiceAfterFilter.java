package com.fuhao.esb.core.component.classScanner.filter;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.classScanner.filter
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBServiceAfterFilter {
    /**
     * 后项拦截器
     * 在服务调用无异常返回后执行拦截操作
     */
    public void after(Class<?> serviceClass, String serviceName, Object[] args, Object value) throws ESBBaseCheckedException;
}
