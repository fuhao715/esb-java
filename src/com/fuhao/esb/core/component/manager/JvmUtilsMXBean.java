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
     *
     * @Time 创建时间:2011-9-26下午2:47:23
     * @param generateLogFile
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String generateThreadDumpInfo(boolean generateLogFile) throws ESBBaseCheckedException;

    /**
     * 生成当前JVM实例的Heap Dump日志文件
     *
     * @Time 创建时间:2011-9-26下午3:21:08
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String generateHeapDumpFile() throws ESBBaseCheckedException;

    /**
     * 生成当前JVM实例堆中的对象统计信息
     *
     * @Time 创建时间:2011-9-29下午10:02:45
     * @param generateLogFile
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String generateHeapInfo(boolean generateLogFile) throws ESBBaseCheckedException;

    /**
     * 获取当前JVM Statistics信息
     *
     * @Description 主要包含内存的相关信息
     * @Time 创建时间:2011-9-26下午5:14:49
     * @param parameter
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String getJVMStatistics(String parameter) throws ESBBaseCheckedException;

    /**
     * 终止线程运行
     *
     * @Description 相关说明
     * @param threadName
     * @throws Exception
     * @Time 创建时间:2013年10月30日上午10:00:05
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void interruptThread(String threadName) throws Exception;

    /**
     * 立即停止服务器
     *
     * @Description 立即停止正在运行的服务器，强行退出JVM
     * @Time 创建时间:2012-2-11上午11:48:34
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void shutdownServerNow();
}
