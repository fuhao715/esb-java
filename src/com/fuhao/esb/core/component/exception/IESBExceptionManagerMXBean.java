package com.fuhao.esb.core.component.exception;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.List;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component.exception
 * Created by fuhao on 13-12-11.
 * Project Name esb-java
 */
public interface IESBExceptionManagerMXBean {
    public enum Type {
        SYSTEM, APPLICATION, OTHERS
    }

    /**
     * 获取抛出异常的机器名
     *
     * @return
     */
    public String getServerNames();
    /**
     * 当前服务器处理该请求消耗时间
     */
    public long getTime();
}
