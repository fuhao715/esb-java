package com.fuhao.esb.core.component.service;

/**
 * package name is  com.fuhao.esb.core.component.service
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
import java.io.Serializable;

public interface IMediationMessage extends Serializable{
    /**
     * 获取会话ID
     * @return
     */
    String getSessionID();
}
