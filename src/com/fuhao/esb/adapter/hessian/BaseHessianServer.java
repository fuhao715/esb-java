package com.fuhao.esb.adapter.hessian;

import com.fuhao.esb.client.IBaseESBMessageReceiver;
import com.fuhao.esb.client.IObjectESBMessageReceiver;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.inbound.AbstractAccessHandler;
import com.fuhao.esb.core.inbound.AccessHandlerManager;

/**
 * package name is  com.fuhao.esb.adapter.hessian
 * Created by fuhao on 13-12-19.
 * Project Name esb-java
 */
public class BaseHessianServer implements IObjectESBMessageReceiver {
    @Override
    public IESBReturnMessage receiveBean(IESBAccessMessage message)
            throws ESBBaseCheckedException,Exception {
        AbstractAccessHandler manager = AccessHandlerManager.getAccessHandler();
        return manager.processAccessBean(message);
    }

    @Override
    public String receiveXML(String xml) throws ESBBaseCheckedException,Exception {
        AbstractAccessHandler manager = AccessHandlerManager.getAccessHandler();
        return manager.processAccessXML(xml);
    }

    @Override
    public Object sendObject(String tranID, Object message) throws Exception {
        AbstractAccessHandler manager = AccessHandlerManager.getAccessHandler();
        return manager.processAccessObject(tranID, message);
    }
}
