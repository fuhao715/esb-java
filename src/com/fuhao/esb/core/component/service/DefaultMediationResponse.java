package com.fuhao.esb.core.component.service;

/**
 * package name is  com.fuhao.esb.core.component.service
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
import java.util.concurrent.ConcurrentHashMap;

public class DefaultMediationResponse implements IMediationResponse {

    private ConcurrentHashMap<Object, Object> systemContext;
    private Object responseValue;
    private String sessionID;

    private static final long serialVersionUID = -8958002173669272191L;

    //同步swordsession
    private ConcurrentHashMap<Object, Object> tmpData = null;
    private ConcurrentHashMap<Object, Object> applicationContext = null;


    public ConcurrentHashMap<Object, Object> getTmpData() {
        return tmpData;
    }

    public void setTmpData(ConcurrentHashMap<Object, Object> tmpData) {
        this.tmpData = tmpData;
    }

    public ConcurrentHashMap<Object, Object> getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ConcurrentHashMap<Object, Object> applicationContext) {
        this.applicationContext = applicationContext;
    }



    @Override
    public Object getResponseValue() {
        return this.responseValue;
    }

    public void setResponseValue(Object o) {
        this.responseValue = o;
    }

    @Override
    public String getSessionID() {
        return this.sessionID;
    }

    /**
     * 设置会话ID
     */
    public void setSessionID(String newSessionID) {
        this.sessionID = newSessionID;
    }

    /**
     * @name 获取返回的系统上下文
     * @Description 相关说明
     * @Time 创建时间:2012-2-13下午8:49:22
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public ConcurrentHashMap<Object, Object> getSystemContext() {
        return systemContext;
    }

    /**
     * @name 设置需要返回的系统上下文
     * @Description 相关说明
     * @Time 创建时间:2012-2-13下午8:49:42
     * @param systemContext
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void setSystemContext(ConcurrentHashMap<Object, Object> systemContext) {
        this.systemContext = systemContext;
    }

}
