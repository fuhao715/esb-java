package com.fuhao.esb.core.component.sequence;

import java.io.IOException;

/**
 * package name is  com.fuhao.esb.core.component.sequence
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public interface ISequencePersistence {
    /**
     * 从文件中加载缓存的所有本地非连续单据号信息
     *
     * @Description 相关说明
     * @param syncWriteStorage
     * @Time 创建时间:2013-1-6下午1:12:27
     * @throws IOException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public int loadAllFromFile(final boolean syncWriteStorage) throws IOException;

    /**
     * 将缓存的所有本地非连续单据号信息保存到文件中
     *
     * @Description 相关说明
     * @Time 创建时间:2013-1-6下午1:12:52
     * @throws IOException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public int saveAllToFile() throws IOException;

    /**
     * 保存单个单据号信息到文件
     *
     * @Description 相关说明
     * @Time 创建时间:2013-1-6下午1:13:51
     * @param info
     * @param nextValue
     * @param writeMinMax
     * @throws IOException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void saveToFile(IUnforceBillCodeInfo info, long nextValue, boolean writeMinMax) throws IOException;
}
