package com.fuhao.esb.adapter.hessian;

import com.fuhao.esb.client.IBaseESBMessageReceiver;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.adapter.hessian
 * Created by fuhao on 13-12-19.
 * Project Name esb-java
 */
public class BaseHessianServer implements IBaseESBMessageReceiver{
    @Override
    public IESBReturnMessage receiveBean(IESBAccessMessage message)
            throws ESBBaseCheckedException,Exception {
        //获得客户端ip并放入request中
        //TODO IP添加
//		message.setIP(ServiceContext.getContextRequest().getRemoteAddr());
        // AbstractAccessHandler manager = AccessHandlerManager.getAccessHandler();
        return null;//manager.processAccessBean(message);
    }

    @Override
    public String receiveXML(String xml) throws ESBBaseCheckedException,Exception {
        // AbstractAccessHandler manager = AccessHandlerManager.getAccessHandler();
        // ServiceContext.getContextRequest().getRemoteAddr() TODO IP添加
        return null;//manager.processAccessXML(xml);
    }
}
