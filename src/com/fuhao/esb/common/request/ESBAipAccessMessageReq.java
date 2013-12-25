package com.fuhao.esb.common.request;
import com.fuhao.esb.common.apimessage.HeadType;
import com.fuhao.esb.common.apimessage.Service;

import java.io.Serializable;

/**
 * Created by fuhao on 13-12-6.
 */
public class ESBAipAccessMessageReq implements IESBAccessMessage,Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -3634734229986788337L;
    private Service service ;
    private String IP;
    private String nodeCode;
    private String transferMode;
    private boolean hasAttachment;
    private long tradeReqStartTime;
    private long tradeInStartTime;

    private String esbMessageType;

    public String getEsbMessageType() {
        return esbMessageType;
    }

    public void setEsbMessageType(String esbMessageType) {
        this.esbMessageType = esbMessageType;
    }

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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String getTranID() {
        return service.getHead().getTranId();

    }

    @Override
    public String getTranSeq() {
        return service.getHead().getTranSeq();
    }

    @Override
    public String getChannelID() {
        return service.getHead().getChannelId();
    }

    @Override
    public String getDestinationID() {
        return service.getHead().getExpandValue(HeadType.DESTINATION_ID);
    }

    @Override
    public String getSecurityPublicKey() {
        return service.getHead().getExpandValue(HeadType.SECURITY_PUBLIC_KEY);
    }

    @Override
    public String getIP() {
        return this.IP;
    }
    @Override
    public void setIP(String ip){
        this.IP = ip;
    }

    @Override
    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;

    }

    @Override
    public String getNodeCode() {
        return this.nodeCode;
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
