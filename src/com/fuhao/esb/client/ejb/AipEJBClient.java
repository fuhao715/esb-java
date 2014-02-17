package com.fuhao.esb.client.ejb;

import com.fuhao.esb.client.IBaseESBClientMessageSender;
import com.fuhao.esb.client.IBaseESBMessageReceiver;
import com.fuhao.esb.client.IObjectESBMessageReceiver;
import com.fuhao.esb.client.IXmlMessageReceiver;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;

/**
 * package name is  com.fuhao.esb.client.ejb
 * Created by fuhao on 14-1-3.
 * Project Name esb-java
 */
public class AipEJBClient extends BaseEJBClient implements IBaseESBClientMessageSender {
    public AipEJBClient() {
        super();
        setEjbName("AipEJBAdapter#"+IBaseESBMessageReceiver.class.getName());
    }

    @Override
    public IESBReturnMessage send(IESBAccessMessage message) throws Exception {
        IBaseESBMessageReceiver sender = (IBaseESBMessageReceiver)getReceiver();
        return sender.receiveBean(message);
    }

    @Override
    public String sendXML(String xml) throws Exception {
        IXmlMessageReceiver sender = getReceiver();
        return sender.receiveXML(xml);
    }

    @Override
    public Object sendObject(String tranID, Object message) throws Exception {
        IObjectESBMessageReceiver sender = (IObjectESBMessageReceiver)getReceiver();
        return sender.sendObject(tranID, message);
    }

    @Override
    public IXmlMessageReceiver getReceiver() throws Exception {
        return (IXmlMessageReceiver)getEJB();
    }

}
