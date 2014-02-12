package com.fuhao.esb.core.exception;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.exception
 * Created by fuhao on 14-2-12.
 * Project Name esb-java
 */
public class JMSMarkPutBackMessageException extends ESBBaseCheckedException{

    private static final long serialVersionUID = -6387168790725033307L;

    public JMSMarkPutBackMessageException(Throwable e){
        super("JMS redeal mark exception", e);
    }
    public JMSMarkPutBackMessageException(Exception e){
        super("JMS redeal mark exception", e);
    }
    public JMSMarkPutBackMessageException(String message){
        super(message);
    }
    public JMSMarkPutBackMessageException(String message ,Map<String,Object> map){
        super(message, map);
    }
    public JMSMarkPutBackMessageException(String msg, Exception e){
        super(msg, e);
    }
    public JMSMarkPutBackMessageException(String msg ,Map<String,Object> map,Exception e){
        super(msg, map, e);
    }
}
