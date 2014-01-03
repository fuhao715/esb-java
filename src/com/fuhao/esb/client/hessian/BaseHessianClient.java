package com.fuhao.esb.client.hessian;

import com.caucho.hessian.client.HessianProxyFactory;
import com.fuhao.esb.client.IBaseESBClientMessageSender;
import com.fuhao.esb.client.IBaseESBMessageReceiver;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;

/**
 * package name is  com.fuhao.esb.client.hessian
 * Created by fuhao on 14-1-3.
 * Project Name esb-java
 */

public class BaseHessianClient implements IBaseESBClientMessageSender {

    //服务端URL
    private String url;
    public BaseHessianClient(String url){

        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public IESBReturnMessage send(IESBAccessMessage message) throws Exception {
        try {
            return getReceiver().receiveBean(message);
        } catch (Exception ex) {
            throw new Exception("ESB01KH005001",ex);
        }
    }

    @Override
    public String sendXML(String xml) throws Exception {
        try {
            return getReceiver().receiveXML(xml);
        } catch (Exception ex) {
            throw new Exception("ESB01KH005001",ex);
        }
    }

    @Override
    public IBaseESBMessageReceiver getReceiver() throws Exception {
        HessianProxyFactory factory = new HessianProxyFactory();
        return (IBaseESBMessageReceiver)factory.create(url);
    }

}

