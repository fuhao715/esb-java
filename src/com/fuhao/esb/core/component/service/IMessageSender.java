package com.fuhao.esb.core.component.service;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.service
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
public interface IMessageSender {
    public static final String USER_TRANSACTION = "sword_user_transaction";

    IMediationResponse send(IMediationRequest req) throws ESBBaseCheckedException;


    IMediationResponse send(String protocolID, IMediationRequest req) throws ESBBaseCheckedException;
}

