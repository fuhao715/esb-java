package com.fuhao.esb.core.component.manager;

/**
 * package name is  com.fuhao.esb.core.component.manager
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public interface ServerContextVisitorMXBean {


    /**
     * 获取服务器启动时间
     */
    public String getServerStartTime();

    /**
     * 获取主机名
     */
    public String getHostname();

    /**
     * 获取当前JVM进程编号
     */
    public int getPid();

    /**
     * 获取服务器名
     *
     * @return
     */
    public String getServerName();

    /**
     * 获取服务器结点名
     *
     * @return
     */
    public String getNoeName();

    /**
     * 获取服务器类型
     *
     * @return
     */
    public String getServerType();

    /**
     * 获取机架ID
     *
     * @return
     */
    public String getRackID();

    /**
     * 获取集群名
     *
     * @return
     */
    public String getClusterName();

    /**
     * 获取服务器运行模式
     *
     * @return
     */
    public String getServerRunningMode();
}
