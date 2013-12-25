package com.fuhao.esb.core.component.utils;
import com.fuhao.esb.core.component.ESBComponentRef;
import com.fuhao.esb.core.component.sequence.BillData;
import com.fuhao.esb.core.component.sequence.IBillCodeContext;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ESBSequenceUtils {
    /**
     * 生成随机字符串
     *
     * @Description 生成一个32位长度的随机字符串
     * @Time 创建时间:2011-9-16上午9:48:18
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String generateRandomString() throws ESBBaseCheckedException {
        if (ESBComponentRef.sequenceManager != null && ESBComponentRef.sequenceManager.isGenerateRandomByGUID()) {
            return ESBSecurityUtils.randomGUID();
        } else {
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    /**
     * 获取指定的序列值，目前生成的是19位的
     *
     * @Description 根据序列名获取对应的序列值，首6位是固定的YYYYMM
     * @Time 创建时间:2011-9-16上午9:56:47
     * @param sequenceName
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Long generateSequence(String sequenceName) {
        Calendar c = ESBDateUtils.getSystemCurrentTime();
        StringBuilder sequence = new StringBuilder(20);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        sequence.append(year).append(month < 10 ? "0" + month : month);

        // 还需要增加自定义格式及从缓存中获取序列值
        sequence.append(c.getTimeInMillis());

        return new Long(sequence.toString());
    }

    /**
     * 得到此种单据的当前序号
     *
     * @param djbs
     * @param ydm
     *            保证序号连续的域标示
     * @return String
     * @throws ESBBaseCheckedException
     */
    public static String getOneBillXh(String djbs, String ydm, String xgry) throws ESBBaseCheckedException {
        checkBillCodeComponen();
        return createContext(djbs, xgry).getOneBillXh(ydm);
    }

    /**
     * 得到此种单据的完整流水号
     *
     * @param djbs
     *            单据号
     * @param ydm
     *            保证序号连续的域标示
     * @param data
     *            业务可提供所需的单据流水相关参数
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public static BillData getOneBillData(String djbs, String ydm, Map<String, Object> data, String xgry) throws ESBBaseCheckedException {
        checkBillCodeComponen();
        return createContext(djbs, xgry).getOneBillData(ydm, data);
    }

    /**
     * 得到此种单据的完整流水号
     *
     * @param djbs
     *            单据号
     * @param ydm
     *            保证序号连续的域标示
     * @param template
     *            业务可提供所需的单据流水相关参数
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public static BillData getOneBillData(String djbs, String ydm, String template, String xgry) throws ESBBaseCheckedException {
        checkBillCodeComponen();
        return createContext(djbs, xgry).getOneBillData(ydm, template);
    }

    /**
     * 得到此种单据的完整流水号
     *
     * @param djbs
     *            单据号
     * @param ydm
     *            保证序号连续的域标示
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public static BillData getOneBillData(String djbs, String ydm, String xgry) throws ESBBaseCheckedException {
        checkBillCodeComponen();
        return createContext(djbs, xgry).getOneBillData(ydm);
    }

    /**
     * 得到此种单据的批量完整流水号-仅支持非连续的取号
     *
     * @param djbs
     *            单据号
     * @param ydm
     *            保证序号连续的域标示
     * @param data
     *            填补数据
     * @batchSize 批量数目
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public static List<BillData> getBatchBillData(String djbs, String ydm, Map<String, Object> data, Integer batchSize, String xgry)
            throws ESBBaseCheckedException {
        checkBillCodeComponen();
        return createContext(djbs, xgry).getBatchBillData(ydm, data, batchSize);
    }

    /**
     * 得到此种单据的批量完整流水号-仅支持非连续的取号
     *
     * @param djbs
     *            单据号
     * @param ydm
     *            保证序号连续的域标示
     * @return BillData(含有当前序号和完整流水号)
     * @throws ESBBaseCheckedException
     */
    public static List<BillData> getBatchBillData(String djbs, String ydm, Integer batchSize, String xgry) throws ESBBaseCheckedException {
        checkBillCodeComponen();
        return createContext(djbs, xgry).getBatchBillData(ydm, batchSize);
    }

    /**
     * 业务根据据流水号记录的主键自动回退单据序列号
     *
     * @param djbs
     *            单据号
     * @param ydm
     *            保证序号连续的域标示
     * @param xh
     *            要回退的序号
     * @return
     * @throws ESBBaseCheckedException
     */
    public static boolean rollbackBillXh(String djbs, String ydm, String xh, String xgry) throws ESBBaseCheckedException {
        checkBillCodeComponen();
        return createContext(djbs, xgry).rollbackBillXh(ydm, xh);
    }

    private static void checkBillCodeComponen() throws ESBBaseCheckedException {
        if (ESBComponentRef.sequenceManager == null) {
            throw new ESBBaseCheckedException("SE-00005:单据号组件未启动，请确认配置");
        }
    }

    private static IBillCodeContext createContext(String djbs, String xgry) throws ESBBaseCheckedException {
        SystemUtils.checkThreadIsInterrupted();
        return ESBComponentRef.sequenceManager.createContext(djbs, xgry);
    }
}
