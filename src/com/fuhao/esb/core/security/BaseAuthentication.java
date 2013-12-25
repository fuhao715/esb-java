package com.fuhao.esb.core.security;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.security
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class BaseAuthentication {
    public boolean authentication(IESBAccessMessage req) throws ESBBaseCheckedException{
          return true; // TODO
    }
}
