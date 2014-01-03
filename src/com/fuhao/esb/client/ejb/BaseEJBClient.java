package com.fuhao.esb.client.ejb;

/**
 * package name is  com.fuhao.esb.client.ejb
 * Created by fuhao on 14-1-2.
 * Project Name esb-java
 */
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.log4j.Logger;
public class BaseEJBClient {
    private static Logger log = Logger.getLogger(BaseEJBClient.class);
    private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
    protected String providerUrl;
    protected String contextFactory = "weblogic.jndi.WLInitialContextFactory";
    protected String ejbName ;//内部EJB消息接收JNDI
    protected String userName ;
    protected String password;
    protected long requestTimeout=10;//默认10秒连接超时 ;
    protected long clientTimeout=30;//默认30秒调用超时;
    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
    public void setClientTimeout(long clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    public void setContextFactory(String contextFactory) {
        this.contextFactory = contextFactory;
    }

    public void setEjbName(String ejbName) {
        this.ejbName = ejbName;
    }

    private String getKey() {
        return (providerUrl + ":" + ejbName).intern();
    }

    public Object getEJB()
            throws Exception {
        if (cache.containsKey(getKey())) {
            return cache.get(getKey());
        }
        synchronized (getKey()) {
            if (!cache.containsKey(getKey())) {
                Object o = initializeEjb();
                cache.put(getKey(), o);
            }
        }
        return cache.get(getKey());
    }

    public void clearContext(){
        synchronized (getKey()) {
            if (cache.containsKey(getKey())) {
                cache.remove(getKey());
            }
        }
    }

    protected Object initializeEjb() throws Exception {
        Hashtable<String, Object> props = new Hashtable<String, Object>();
        props.put(Context.PROVIDER_URL, providerUrl);
        props.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        props.put("weblogic.jndi.requestTimeout", new Long(requestTimeout*1000));//默认10秒连接超时
        props.put("weblogic.rmi.clientTimeout", new Long(clientTimeout*1000));//默认30秒调用超时

        if(null!= userName){
            props.put(Context.SECURITY_PRINCIPAL, userName);
            props.put(Context.SECURITY_CREDENTIALS, (password==null)?"":password);
        }

        long lStart = System.currentTimeMillis();
        InitialContext ctx = null;
        try {
            ctx = new InitialContext(props);
            Object o = ctx.lookup(ejbName);
            return o;
        } finally {
            log.info(providerUrl+"初始化EJB上下文开销为（ms）："+(System.currentTimeMillis()-lStart));
            if (ctx != null)
                ctx.close();
        }
    }
}
