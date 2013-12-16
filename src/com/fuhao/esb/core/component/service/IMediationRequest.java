package com.fuhao.esb.core.component.service;

/**
 * package name is  com.fuhao.esb.core.component.service
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
import java.util.concurrent.ConcurrentHashMap;

public interface IMediationRequest extends IMediationMessage {
    /**
     * 获取服务名
     *
     * @return
     */
    String getServiceName();

    /**
     * 获取服务参数
     *
     * @return
     */
    Object[] getServiceParam();

    /**
     * 获取用户标识
     *
     * @return
     */
    String getUserID();

    /**
     * 获取机构标识
     *
     * @return
     */
    String getOrgID();

    /**
     * 获取需要自动同步的平台系统临时数据区数据
     *
     * @return
     */
    ConcurrentHashMap<Object, Object> getSystemContext();

    /**
     * 获取需要自动同步的应用系统临时数据区数据
     *
     * @return
     */
    ConcurrentHashMap<Object, Object> getApplicationContext();

    /**
     * 获取需要自动同步的swordSession数据
     *
     * @return
     */
    ConcurrentHashMap<Object, Object> getTmpData();

    /**
     *@name 获取经过的服务器列表
     */
    public String[] getServers();
}
