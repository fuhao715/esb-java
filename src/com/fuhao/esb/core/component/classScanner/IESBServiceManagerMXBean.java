package com.fuhao.esb.core.component.classScanner;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.classScanner
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBServiceManagerMXBean {


    /**
     * 启动服务
     */
    public void startService(String serviceName) throws ESBBaseCheckedException;

    /**
     * 停止服务
     */
    public void stopService(String serviceName) throws ESBBaseCheckedException;

    /**
     * 挂起服务
     */
    public void sleepService(String serviceName) throws ESBBaseCheckedException;

    /**
     * 唤醒服务
     */
    public void weakupService(String serviceName) throws ESBBaseCheckedException;

    /**
     * 查找服务对象类
     */
    public String findServiceClass(String serviceName) throws ESBBaseCheckedException;

    /**
     * 获取已注册的服务数量
     */
    public int getServiceCount();

    /**
     * 获取允许服务调用最大嵌套层数
     *
     * @return
     */
    public int getMaxServiceCallLevel();

    /**
     * 获取允许服务调用经过的最大服务器嵌套层数
     */
    public int getMaxServerCallLevel();

    /**
     * 设置允许服务调用最大嵌套层数
     */
    public void setMaxServiceCallLevel(int maxServiceCallLevel);

    /**
     * 获取每个请求允许的最大异步任务数
     *
     * @return
     */
    public int getMaxASyncService();

    /**
     * 设置每个请求允许的最大异步任务数
     */
    public void setMaxASyncService(int maxASyncService);


    /**
     * 获取本地服务的版本信息
     */
    public long getVersion();
}
