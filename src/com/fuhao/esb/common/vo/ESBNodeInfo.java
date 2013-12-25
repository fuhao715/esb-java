package com.fuhao.esb.common.vo;

/**
 * package name is  com.fuhao.esb.common.vo
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ESBNodeInfo {
    /**
     * 单例
     */
    private static ESBNodeInfo esbConf = new ESBNodeInfo();

    /**
     * 当前ESB节点ID
     */
    private String currentNodeID = null;
    /**
     * 交易节点编码
     */
    private String currentNodeBM = null;

    public String getCurrentSystemID() {
        return currentSystemID;
    }

    public void setCurrentSystemID(String currentSystemID) {
        this.currentSystemID = currentSystemID;
    }

    private String currentSystemID = null;

    /**
     * 机构ID
     */
    private String orgID = null;

    /**
     * 是否记录流水
     */
    private boolean isLogTransactionSeqence = false;

    private boolean isLogException = false;

    public boolean isLogException() {
        return isLogException;
    }

    public void setLogException(boolean isLogException) {
        this.isLogException = isLogException;
    }

    public boolean isLogTransactionSeqence() {
        return isLogTransactionSeqence;
    }

    public void setLogTransactionSeqence(boolean isLogTransactionSeqence) {
        this.isLogTransactionSeqence = isLogTransactionSeqence;
    }

    public String getCurrentNodeBM() {
        return currentNodeBM;
    }

    public void setCurrentNodeBM(String currentNodeBM) {
        this.currentNodeBM = currentNodeBM;
    }

    /**
     * 当前ESB节点名称
     */
    private String currentNodeName = null;

    /**
     * 总流量数
     */
    private int totalFlowNumber = 0;

    /**
     * 私有构造子
     */
    private ESBNodeInfo(){
    }

    /**
     *
     *@name    获取ESB配置实例
     *@Description 相关说明
     *@Time    创建时间:2011-12-19下午01:55:01
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static ESBNodeInfo getInstance(){
        return esbConf;
    }

    public String getCurrentNodeID() {
        return currentNodeID;
    }

    public void setCurrentNodeID(String currentNodeID) {
        this.currentNodeID = currentNodeID;
    }

    public int getTotalFlowNumber() {
        return totalFlowNumber;
    }

    public void setTotalFlowNumber(int totalFlowNumber) {
        this.totalFlowNumber = totalFlowNumber;
    }

    public String getCurrentNodeName() {
        return currentNodeName;
    }

    public void setCurrentNodeName(String currentNodeName) {
        this.currentNodeName = currentNodeName;
    }
    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }
}
