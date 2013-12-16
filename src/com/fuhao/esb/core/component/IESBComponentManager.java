package com.fuhao.esb.core.component;


import com.fuhao.esb.core.component.service.IMessageSender;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public interface IESBComponentManager {
    IMessageSender getMessageSender();
}
