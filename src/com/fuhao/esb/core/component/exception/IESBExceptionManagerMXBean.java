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
     *
     * @Description 相关说明
     * @return
     * @Time 创建时间:2013-5-9下午2:59:32
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public long getTime();
}
