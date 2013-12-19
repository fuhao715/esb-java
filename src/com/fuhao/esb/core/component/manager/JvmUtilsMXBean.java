package com.fuhao.esb.core.component.manager;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.manager
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public interface JvmUtilsMXBean {
    /**
     * 获取当前JVM实例的Thread Dump信息
     */
    public String generateThreadDumpInfo(boolean generateLogFile) throws ESBBaseCheckedException;

    /**
     * 生成当前JVM实例的Heap Dump日志文件
     */
    public String generateHeapDumpFile() throws ESBBaseCheckedException;

    /**
     * 生成当前JVM实例堆中的对象统计信息
     */
    public String generateHeapInfo(boolean generateLogFile) throws ESBBaseCheckedException;

    /**
     * 获取当前JVM Statistics信息
     */
    public String getJVMStatistics(String parameter) throws ESBBaseCheckedException;

    /**
     * 终止线程运行
     */
    public void interruptThread(String threadName) throws Exception;

    /**
     * 立即停止服务器
     * 立即停止正在运行的服务器，强行退出JVM

     */
    public void shutdownServerNow();
}
