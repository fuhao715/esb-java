package com.fuhao.esb.common.xml.errorxmlreturn;

/**
 * package name is  com.fuhao.esb.common.xml.errorxmlreturn
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
import java.io.Serializable;

public class AttachmentFileInfo implements Serializable{

    //   private String clientFile;
    private static final long serialVersionUID = 1L;
    private String serverFilePathInfo;
    //
    private long position;

//    private String transferUUID;
//
//    public String getTransferUUID() {
//		return transferUUID;
//	}
//
//	public void setTransferUUID(String transferUUID) {
//		this.transferUUID = transferUUID;
//	}

    private byte[] bytes;

//    public String getClientFile() {
//        return clientFile;
//    }
//
//    public void setClientFile(String clientFile) {
//        this.clientFile = clientFile;
//    }

    public String getServerFile() {
        return serverFilePathInfo;
    }

    public void setServerFile(String serverFile) {
        this.serverFilePathInfo = serverFile;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
