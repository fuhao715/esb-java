package com.fuhao.esb.core.component;

import com.fuhao.esb.core.component.classScanner.ESBServiceInfo;
import com.fuhao.esb.core.component.classScanner.IESBServiceProxy;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBServiceProxyClassGenerator {
    /**
     * 服务类实例生成方法
     */
    public static IESBServiceProxy generateCode(final ESBServiceInfo info) throws ESBBaseCheckedException {
        // 选择服务代理类的类加载器
        try {

            // 加载并实例化服务代理类
            Class<?> proxyClazz = Class.forName(info.serviceClassName) ;
            return (IESBServiceProxy) proxyClazz.newInstance();
        } catch (Exception ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("生成服务代理对象时发生错误:" + ex.getMessage());
            e.setStackTrace(ex.getStackTrace());
            throw e;
        }
    }
}
