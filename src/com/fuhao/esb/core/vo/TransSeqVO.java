package com.fuhao.esb.core.vo;

import java.util.Date;

/**
 * package name is  com.fuhao.esb.core.vo
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class TransSeqVO {
    private String seqUUID;
    private String seqNum;
    private String tranID;
    private String logType;
    private String tranStatus;
    private String bizNum;
    private String loginUser;
    private String modifiedUser;
    private Date loginTime;
    private Date modifiedTime;
    private String nodeName;
    private String channelID;
    private String serverName;
    private String targetID;
    private double costTimes;
    private String sessionID;
    private String orgID;
    private String threadID;
    private String webReqID;
    private String systemID;
    private int callSeqNum;// 调用路径顺序号
    private String xml;
    public int getCallSeqNum() {
        return callSeqNum;
    }
    public void setCallSeqNum(int callSeqNum) {
        this.callSeqNum = callSeqNum;
    }
    public String getSeqUUID() {
        return seqUUID;
    }

    public void setSeqUUID(String seqUUID) {
        this.seqUUID = seqUUID;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }

    public String getBizNum() {
        return bizNum;
    }

    public void setBizNum(String bizNum) {
        this.bizNum = bizNum;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getTargetID() {
        return targetID;
    }

    public void setTargetID(String targetID) {
        this.targetID = targetID;
    }

    public double getCostTimes() {
        return costTimes;
    }

    public void setCostTimes(double costTimes) {
        this.costTimes = costTimes;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getWebReqID() {
        return webReqID;
    }

    public void setWebReqID(String webReqID) {
        this.webReqID = webReqID;
    }

    public String getThreadID() {
        return threadID;
    }

    public void setThreadID(String threadID) {
        this.threadID = threadID;
    }

    public String getSystemID() {
        return systemID;
    }

    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
