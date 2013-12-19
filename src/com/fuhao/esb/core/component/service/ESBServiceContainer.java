package com.fuhao.esb.core.component.service;

import com.fuhao.esb.core.component.ESBServiceProxyClassGenerator;
import com.fuhao.esb.core.component.classScanner.ESBServiceInfo;
import com.fuhao.esb.core.component.classScanner.IESBServiceProxy;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.service
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 * 服务生命周期
 */
public class ESBServiceContainer {

    public static class CacheValue {
        public Object value;

        public CacheValue(Object value) {
            this.value = value;
        }
    }

    /**
     * 服务状态
     *
     */
    public static class LifeCycle {
        /**
         * 服务正在启动
         */
        public static final byte STARTING = 1;

        /**
         * 服务正在运行
         */
        public static final byte RUNNING = 2;

        /**
         * 服务已经挂超
         */
        public static final byte SLEEP = 3;

        /**
         * 服务已停止
         */
        public static final byte STOP = 4;

        /**
         * 服务异常
         */
        public static final byte ERROR = 5;
    }

    /**
     * 类加载器名称
     */
    public final String classLoaderName;

    /**
     * 服务信息：为生成服务代理类提供信息
     */
    public final ESBServiceInfo serviceInfo;

    /**
     * 服务对象
     */
    public final Object service;

    /**
     * 服务代理实例
     */
    private IESBServiceProxy serviceProxy;

    /**
     * 服务方法位置索引
     */
    private final byte methodIndex;

    /**
     * 服务状态
     */
    public byte serviceState;



    public ESBServiceContainer(String classLoaderName, ESBServiceInfo serviceInfo, Object service, byte serviceState, int valueCacheSize) {
        this.classLoaderName = classLoaderName;
        this.serviceInfo = serviceInfo;
        this.service = service;
        this.serviceState = serviceState;
        this.methodIndex = serviceInfo.methodIndex;
    }

    public IESBServiceProxy getServiceProxy() throws ESBBaseCheckedException {
        if (this.serviceProxy != null) {
            return this.serviceProxy;
        }

        synchronized (this) {
            if (this.serviceProxy == null) {
                try {
                    this.serviceProxy = ESBServiceProxyClassGenerator.generateCode(this.service.getClass().getClassLoader(),
                            this.serviceInfo);
                    this.serviceProxy.setService(this.service);
                } catch (ESBBaseCheckedException ex) {
                    // 更新并清理服务容器类
                    this.serviceState = LifeCycle.ERROR;
                    throw ex;
                } finally {
                    // this.serviceInfo.clear();
                }
            }
        }

        return this.serviceProxy;
    }

    public byte getMethodIndex() {
        return this.methodIndex;
    }

    public Byte getSecurityLevel() {
        return this.serviceInfo.securityLevel;
    }
}
