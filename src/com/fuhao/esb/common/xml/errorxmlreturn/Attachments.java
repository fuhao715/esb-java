package com.fuhao.esb.common.xml.errorxmlreturn;

/**
 * package name is  com.fuhao.esb.common.xml.errorxmlreturn
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Attachments implements Serializable{

    private static final long serialVersionUID = 1L;

    private String result;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private List<AttachmentFileInfo> attachments = new ArrayList<AttachmentFileInfo>();
    public List<AttachmentFileInfo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentFileInfo> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(AttachmentFileInfo attachment){
        attachments.add(attachment);
    }

    public boolean removeAttachment(AttachmentFileInfo attachment){
        return attachments.remove(attachment);
    }
}
