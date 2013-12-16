package com.fuhao.esb.core.component.service;

/**
 * package name is  com.fuhao.esb.core.component.service
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
import java.util.concurrent.ConcurrentHashMap;

public interface IMediationResponse extends IMediationMessage {
    public Object getResponseValue();

    public ConcurrentHashMap<Object, Object> getSystemContext();

    //同步swordsession
    public ConcurrentHashMap<Object, Object> getTmpData();

    public ConcurrentHashMap<Object, Object> getApplicationContext();

}
