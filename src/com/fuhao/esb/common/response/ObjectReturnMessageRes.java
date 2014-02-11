package com.fuhao.esb.common.response;

import java.io.Serializable;

/**
 * package name is  com.fuhao.esb.common.response
 * Created by fuhao on 14-2-11.
 * Project Name esb-java
 */
public class ObjectReturnMessageRes implements IESBReturnMessage, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -644293832626270030L;
    private Object bizObj ;
    private String transferMode;
    private boolean hasAttachment;
    private String esbMessageType;
    private String tranID;
    private String tranSeq;
    private String channelID;
    private String destinationID;
    private String securityPublicKey;
    /**
     * 返回码
     */
    private String returnCode = null;

    public String getEsbMessageType() {
        return esbMessageType;
    }

    public void setEsbMessageType(String esbMessageType) {
        this.esbMessageType = esbMessageType;
    }

    private long tradeReqStartTime;
    private long tradeInStartTime;

    public String getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    @Override
    public String getTranID() {
        return tranID;

    }

    @Override
    public String getTranSeq() {
        return tranSeq;
    }

    @Override
    public String getChannelID() {
        return channelID;
    }

    @Override
    public String getDestinationID() {
        return destinationID;
    }

    @Override
    public String getSecurityPublicKey() {
        return securityPublicKey;
    }

    @Override
    public Object getBizObj() {
        return bizObj;
    }

    @Override
    public void setBizObj(Object bizObj) {
        this.bizObj = bizObj;
    }

    @Override
    public String getReturnCode() {
        return returnCode;
    }

    @Override
    public String getIP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setIP(String ip) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNodeCode(String nodeCode) {
        // TODO Auto-generated method stub
    }

    @Override
    public String getNodeCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
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

    public long getTradeReqStartTime() {
        return tradeReqStartTime;
    }

    public void setTradeReqStartTime(long tradeReqStartTime) {
        this.tradeReqStartTime = tradeReqStartTime;
    }

    public long getTradeInStartTime() {
        return tradeInStartTime;
    }

    public void setTradeInStartTime(long tradeInStartTime) {
        this.tradeInStartTime = tradeInStartTime;
    }



/*	@Override
	public String getBusinessDataID() {
		return service.getHead().getExpandValue(HeadType.YWSJBS);
	}*/
}
