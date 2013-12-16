package com.fuhao.esb.core.component.service;

/**
 * package name is  com.fuhao.esb.core.component.service
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */

import java.util.concurrent.ConcurrentHashMap;

public class DefaultMediationRequest implements IMediationRequest {
    private static final long serialVersionUID = -4426483686659064371L;
    private String sessionID;
    private String serviceName;
    private Object[] serviceParam;
    private transient boolean localService;
    private ConcurrentHashMap<Object, Object> systemContext;
    private ConcurrentHashMap<Object, Object> applicationContext;
    private ConcurrentHashMap<Object, Object> tmpData;
    private String[] servers;

    /**
     * 用户标识
     */
    private String userID;

    /**
     * 机构标识
     */
    private String orgID;

    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public Object[] getServiceParam() {
        return this.serviceParam;
    }

    @Override
    public String getSessionID() {
        return this.sessionID;
    }

    /**
     * 设置服务名
     *
     * @param serviceName
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * 设置服务参数
     *
     * @param serviceParam
     */
    public void setServiceParam(Object[] serviceParam) {
        this.serviceParam = serviceParam;
    }

    /**
     * 设置会话ID
     */
    public void setSessionID(String newSessionID) {
        this.sessionID = newSessionID;
    }

    /**
     * 获取用户标识
     *
     * @return
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户标识
     *
     * @param userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 获取机构标识
     *
     * @return
     */
    public String getOrgID() {
        return orgID;
    }

    /**
     * 设置机构标识
     *
     * @param orgID
     */
    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    /**
     * 创建时间:2011-12-16上午10:32:07 get方法
     *
     * @return the systemContext
     */
    public ConcurrentHashMap<Object, Object> getSystemContext() {
        return systemContext;
    }

    /**
     * 创建时间:2011-12-16上午10:32:07 set方法
     *
     * @param systemContext
     *            the systemContext to set
     */
    public void setSystemContext(ConcurrentHashMap<Object, Object> systemContext) {
        this.systemContext = systemContext;
    }

    /**
     * 创建时间:2011-12-16上午10:32:07 get方法
     *
     * @return the applicationContext
     */
    public ConcurrentHashMap<Object, Object> getApplicationContext() {
        return applicationContext;
    }

    /**
     * 创建时间:2011-12-16上午10:32:07 set方法
     *
     * @param applicationContext
     *            the applicationContext to set
     */
    public void setApplicationContext(ConcurrentHashMap<Object, Object> applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 创建时间:2011-12-16上午10:32:07 get方法
     *
     * @return the tmpData
     */
    public ConcurrentHashMap<Object, Object> getTmpData() {
        return tmpData;
    }

    /**
     * 创建时间:2011-12-16上午10:32:07 set方法
     *
     * @param tmpData
     *            the tmpData to set
     */
    public void setTmpData(ConcurrentHashMap<Object, Object> tmpData) {
        this.tmpData = tmpData;
    }

    public String[] getServers() {
        return servers;
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }

    public boolean isLocalService() {
        return localService;
    }

    public void setLocalService(boolean localService) {
        this.localService = localService;
    }

}
