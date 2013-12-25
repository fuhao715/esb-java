package com.fuhao.esb.core.component.sequence;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.List;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component.sequence
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public interface IBillCodeContext {

    /**
     * 得到此种单据的当前序号
     *
     * @param ydm
     *            保证序号连续的域标示
     * @return String
     * @throws ESBBaseCheckedException
     */
    public abstract String getOneBillXh(String ydm) throws ESBBaseCheckedException;

    /**
     * 得到此种单据的完整流水号
     *
     * @param ydm
     *            保证序号连续的域标示
     * @param data
     *            业务可提供所需的单据流水相关参数
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public abstract BillData getOneBillData(String ydm, Map<String, Object> data) throws ESBBaseCheckedException;

    /**
     * 用获取的数据填充变量代表的序号和日期得到此种单据的完整流水号
     *
     * @param ydm
     *            保证序号连续的域标示
     * @param template
     *            单据流水模板支持
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public abstract BillData getOneBillData(String ydm, String template) throws ESBBaseCheckedException;

    /**
     * 用获取的数据填充变量代表的序号和日期得到此种单据的完整流水号，模板串来自配置数据库
     *
     * @param ydm
     *            保证序号连续的域标示
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public abstract BillData getOneBillData(String ydm) throws ESBBaseCheckedException;

    /**
     * 用获取的数据填充变量代表的序号和日期得到此种单据的完整流水号，模板串来自配置数据库
     *
     * @param ydm
     *            保证序号连续的域标示
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public abstract List<BillData> getBatchBillData(String ydm, Map<String, Object> data, Integer batchSize)
            throws ESBBaseCheckedException;

    /**
     * 用获取的数据填充变量代表的序号和日期得到此种单据的完整流水号，模板串来自配置数据库
     *
     * @param ydm
     *            保证序号连续的域标示
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public abstract List<BillData> getBatchBillData(String ydm, Integer batchSize) throws ESBBaseCheckedException;

    /**
     * 业务根据据流水号记录的主键自动回退单据序列号
     *
     * @return
     */
    public abstract boolean rollbackBillXh(String ydm, String xh) throws ESBBaseCheckedException;

}
