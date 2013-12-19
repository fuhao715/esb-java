package com.fuhao.esb.core.component.log;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.List;

/**
 * package name is  com.fuhao.esb.core.component.log
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public interface IESBBaseLogMXBean {
    /**
     * 获取所有日志输出器的名称
     */
    public List<String> getLoggerNames();

    /**
     * 设置日志输出级别
     */
    public void setLogLevel(String name, String level) throws Exception;

    /**
     * 获取日志输出级别
     */
    public String getLogLevel(String name) throws Exception;
}
