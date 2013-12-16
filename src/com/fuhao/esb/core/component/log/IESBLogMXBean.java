package com.fuhao.esb.core.component.log;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.List;

/**
 * package name is  com.fuhao.esb.core.component.log
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public interface IESBLogMXBean {
    /**
     * 获取所有日志输出器的名称
     *
     * @Description 相关说明
     * @Time 创建时间:2012-9-10上午10:34:27
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public List<String> getLoggerNames();

    /**
     * 设置日志输出级别
     *
     * @Description 相关说明
     * @Time 创建时间:2012-9-10上午10:32:56
     * @param name
     * @param level
     * @throws Exception
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void setLogLevel(String name, String level) throws Exception;

    /**
     * 获取日志输出级别
     *
     * @Description 相关说明
     * @Time 创建时间:2012-9-10上午10:33:06
     * @param name
     * @return
     * @throws Exception
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String getLogLevel(String name) throws Exception;
}
