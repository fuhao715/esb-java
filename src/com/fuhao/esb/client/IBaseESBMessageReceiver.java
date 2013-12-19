package com.fuhao.esb.client;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;

/**
 * package name is  com.fuhao.esb.client
 * Created by fuhao on 13-12-19.
 * Project Name esb-java
 */
public interface IBaseESBMessageReceiver extends IXmlMessageReceiver {
    public IESBReturnMessage receiveBean(IESBAccessMessage message)throws Exception;
}
