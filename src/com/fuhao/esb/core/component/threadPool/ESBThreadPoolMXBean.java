package com.fuhao.esb.core.component.threadPool;

/**
 * package name is  com.fuhao.esb.core.component.threadPool
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class ESBThreadPoolMXBean implements IESBThreadPoolMXBean {
    @Override
    public String getAllThreadPoolName() {
        return ESBThreadPoolManager.threadPoolIndex.keySet().toString();
    }

    @Override
    public String getThreadPoolInfo(String poolName) {
        final ThreadPoolInfo info = ESBThreadPoolManager.threadPoolIndex.get(poolName);
        return info == null ? null : info.toString();
    }
}
