package com.fuhao.esb.common.xml.errorxmlreturn;

import com.fuhao.esb.common.response.ESBAipReturnMessageRes;

/**
 * package name is  com.fuhao.esb.common.xml.errorxmlreturn
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ESBAipReturnAttachmentMessageRes extends ESBAipReturnMessageRes {

    private static final long serialVersionUID = 1L;

    private Attachments attachments;

    public Attachments getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachments attachments) {
        this.attachments = attachments;
    }
}
