package com.fuhao.esb.client;

/**
 * package name is  com.fuhao.esb.client
 * Created by fuhao on 14-2-12.
 * Project Name esb-java
 */
public interface IObjectESBMessageReceiver extends IBaseESBMessageReceiver{
    public Object sendObject(String tranID, Object message)throws Exception;
}
