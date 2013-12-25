package com.fuhao.esb.common.response;

import com.fuhao.esb.common.apimessage.HeadType;
import com.fuhao.esb.common.apimessage.RtnMeg;
import com.fuhao.esb.common.apimessage.Service;

import java.io.Serializable;

/**
 * Created by fuhao on 13-12-6.
 */
public class ESBAipReturnMessageRes implements IESBReturnMessage, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -644293832626270030L;
    Service service ;

    private String transferMode;
    private boolean hasAttachment;
    private String esbMessageType;

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
    public String getReturnCode() {
        if ("0".equals(service.getHead().getRtnCode()))//可能没有rtn_msg节点
            return "0000";
        else{
            if(service.getHead().getRtnMeg() == null){
                return service.getHead().getRtnCode();
            }
            else{
                return service.getHead().getRtnCode()+service.getHead().getRtnMeg().getCode();
            }
        }
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
        service.getHead().setRtnCode(returnCode.substring(0,1));
        RtnMeg rtnMeg = service.getHead().getRtnMeg();
        if(rtnMeg==null){
            rtnMeg = new RtnMeg();
            service.getHead().setRtnMeg(rtnMeg);
        }
        rtnMeg.setCode(returnCode.substring(1,4));
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
