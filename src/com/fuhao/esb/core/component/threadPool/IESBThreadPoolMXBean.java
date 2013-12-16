package com.fuhao.esb.core.component.threadPool;

/**
 * package name is  com.fuhao.esb.core.component.threadPool
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBThreadPoolMXBean {
    /**
     * 获取已经注册的线程池列表
     */
    public String getAllThreadPoolName();

    /**
     * 获取创建线程池对象的相关信息
    */
    public String getThreadPoolInfo(String poolName);
}
