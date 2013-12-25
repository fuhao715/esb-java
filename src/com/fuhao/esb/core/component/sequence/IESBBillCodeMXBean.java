package com.fuhao.esb.core.component.sequence;

/**
 * package name is  com.fuhao.esb.core.component.sequence
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public interface IESBBillCodeMXBean {


    /**
     * 获取指定单据号的配置信息
     *
     * @Description 相关说明
     * @Time 创建时间:2013-1-6上午10:46:12
     * @param djbs
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String findBillCodeForMXBean(String djbs);

    /**
     * 获取指定的、非连续单据号的在线运行时信息
     *
     * @Description 相关说明
     * @Time 创建时间:2013-1-6上午10:46:12
     * @param djbs
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String findUnforceBillCodeInfoForMXBean(String djbs, String ydm);

    /**
     * 获取系统中配置的单据号数量
     *
     * @Description 相关说明
     * @Time 创建时间:2013-1-6上午10:16:26
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public int getBillCodeCount();

    /**
     * 获取非强制连续单据号本地缓存数据文件位置
     *
     * @Description 相关说明
     * @Time 创建时间:2013-1-7上午11:26:35
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String getLocalCacheFile();

    /**
     * 是否同步写入到存储设备
     *
     * @Description 相关说明
     * @return
     * @Time 创建时间:2013-6-28上午11:27:27
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public boolean isSyncWriteStorage();
}
