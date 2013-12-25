package com.fuhao.esb.core.vo;

import com.fuhao.esb.common.vo.Constants;
import com.fuhao.esb.common.vo.ESBNodeInfo;
import com.fuhao.esb.core.session.ESBSessionUtils;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * package name is  com.fuhao.esb.core.vo
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class EsbTransactionChain implements Serializable{
    private static final long serialVersionUID = 1L;

    public ArrayList<String> getSystemTransactionStatusList() {
        return systemTransactionStatusList;
    }
    public void setSystemTransactionStatusList(ArrayList<String> systemTransactionStatusList) {
        this.systemTransactionStatusList = systemTransactionStatusList;
    }
    /**
     * 保存系统链路状态（目前只保存系统调用）
     */
    private ArrayList<String> systemTransactionStatusList=new ArrayList<String>();

    /**
     * 获取当前节点的调用顺序
     */
    @SuppressWarnings("unchecked")
    public static int getCurrentNodeExecutionSequence()
    {
        String currentNodeBM = ESBNodeInfo.getInstance().getCurrentNodeBM();
        ArrayList<String> esbTransactionChain=((ArrayList<String>) ESBSessionUtils.getTempDataFromSession(Constants.ESB_TRANSACTION_CHAIN));
        if(esbTransactionChain==null)
            return 1;
        return esbTransactionChain.indexOf(currentNodeBM)+1;
    }

}
