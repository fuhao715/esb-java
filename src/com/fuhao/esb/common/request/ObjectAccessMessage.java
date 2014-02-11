package com.fuhao.esb.common.request;

import com.fuhao.esb.common.apimessage.Service;

/**
 * package name is  com.fuhao.esb.common.request
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 * 可扩展接入类，把非通用报文构造成通用报文对象进入主流程，在trans环境判断对象类型，进行适配服务处理。
 */
public class ObjectAccessMessage implements IESBAccessMessage {
    private Object bizObj ;
    private String tranID;
    private String tranSeq;
    private String channelID;
    private String destinationID;
    private String securityPublicKey;

    public Object getBizObj() {
        return bizObj;
    }

    private String nodeCode;

    public void setBizObj(Object bizObj) {
        this.bizObj = bizObj;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public void setDestinationID(String destinationID) {
        this.destinationID = destinationID;
    }

    public void setSecurityPublicKey(String securityPublicKey) {
        this.securityPublicKey = securityPublicKey;
    }

    private String transferMode;
    private boolean hasAttachment;
    private long tradeReqStartTime;
    private long tradeInStartTime;
    @Override
    public String getTranID() {
        return null;
    }

    @Override
    public String getTranSeq() {
        return null;
    }

    @Override
    public String getChannelID() {
        return null;
    }

    @Override
    public String getDestinationID() {
        return null;
    }

    @Override
    public String getSecurityPublicKey() {
        return null;
    }

    @Override
    public String getIP() {
        return null;
    }

    @Override
    public void setIP(String ip) {

    }

    @Override
    public void setNodeCode(String nodeCode) {

    }

    @Override
    public String getNodeCode() {
        return null;
    }

    @Override
    public void setTransferMode(String transferMode) {

    }

    @Override
    public String getTransferMode() {
        return null;
    }

    @Override
    public void setHasAttachment(boolean has) {

    }

    @Override
    public boolean isHasAttachment() {
        return false;
    }

    @Override
    public void setTradeReqStartTime(long t) {

    }

    @Override
    public long getTradeReqStartTime() {
        return 0;
    }

    @Override
    public void setTradeInStartTime(long t) {

    }

    @Override
    public long getTradeInStartTime() {
        return 0;
    }

    @Override
    public String getEsbMessageType() {
        return null;
    }

    @Override
    public void setEsbMessageType(String esbMessageType) {

    }
}
