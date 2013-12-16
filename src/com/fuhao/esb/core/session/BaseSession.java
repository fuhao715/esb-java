package com.fuhao.esb.core.session;

import com.fuhao.esb.common.request.IProtocolConf.ProtocolType;
import com.fuhao.esb.core.component.ESBServerContext;

import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * package name is  com.fuhao.esb.core.session
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 */
public class BaseSession {


    /**
     * 交易开始时间
     */
    private long startTime;

    /**
     * 父会话ID
     */
    private String parentSessionId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 用户标识
     */
    private String userID;

    /**
     * 机构标识
     */
    private String orgID;

    /**
     * 交易处理协议类型(默认:null)
     */
    private transient ProtocolType protocolType = null;

    /**
     * 服务嵌套调用层数
     */
    private transient final AtomicInteger serviceCallLevel = new AtomicInteger(0);

    /**
     * 服务调用栈
     */
    private transient final Stack<String> serviceCallStack = new Stack<String>();

    /**
     * 平台系统临时数据区，只存放平台系统的相关上下文数据
     */
    private volatile ConcurrentHashMap<Object, Object> systemContext = null;
    /**
     * 应用系统临时数据区，只存放应用系统的相关上下文数据
     */
    private volatile ConcurrentHashMap<Object, Object> applicationContext = null;

    /**
     * 临时数据区，只存放使用频率很低的数据，使用频繁很高的数据使用属性存属
     */
    private volatile ConcurrentHashMap<Object, Object> tmpData = null;

    /**
     * 本地系统临时数据区，只存放应用系统在本地的相关上下文数据. 不进行跨域传输.
     */
    private transient volatile ConcurrentHashMap<Object, Object> localTempData = null;

    /**
     * 调用经过的服务器路径
     */
    private volatile String[] servers = null;

    /**
     * 交易请求信息索引
     */
    private int transactionInfoIndex = -1;

    public BaseSession() {
        this.startTime = System.currentTimeMillis();
    }

    public BaseSession(BaseSession session, boolean mergeFullSessionInfo) {
        this();

        if (session == null) {
            return;
        }
        this.sessionId = null;
        this.parentSessionId = session.sessionId;
        this.userID = session.userID;
        this.orgID = session.orgID;
        this.serviceCallLevel.set(session.serviceCallLevel.get());
        this.serviceCallStack.addAll(session.getServiceCallStack());
        this.servers = session.servers == null ? null : session.servers.clone();

        if (mergeFullSessionInfo) {
            this.sessionId = session.sessionId;
            this.parentSessionId = session.parentSessionId;
            this.systemContext = session.systemContext;
            this.applicationContext = session.applicationContext;
            this.tmpData = session.tmpData;
            this.localTempData = session.localTempData;
        }
    }

    /**
     * 创建时间:2011-8-26下午3:51:59 get方法
     *
     * @return long
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 获取用户标识
     *
     * @return
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户标识
     *
     * @param userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 获取机构标识
     *
     * @return
     */
    public String getOrgID() {
        return orgID;
    }

    /**
     * 设置机构标识
     *
     * @param orgID
     */
    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    /**
     * 获取父SessionID
     *
     * @Description 相关说明
     * @return
     * @Time 创建时间:2013-1-25下午1:58:48
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public String getParentSessionId() {
        return parentSessionId;
    }

    /**
     * @return 获取 sessionId
     */
    public String getSessionId() {
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        return sessionId;
    }

    /**
     * @param sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 创建时间:2011-12-16上午10:02:04 get方法
     *
     * @return the systemContext
     */
    public ConcurrentHashMap<Object, Object> getSystemContext() {
        return systemContext;
    }

    /**
     * 创建时间:2011-12-16上午10:02:04 set方法
     *
     * @param systemContext
     *            the systemContext to set
     */
    public void setSystemContext(ConcurrentHashMap<Object, Object> systemContext) {
        this.systemContext = systemContext;
    }

    /**
     * 创建时间:2011-12-16上午10:02:04 get方法
     *
     * @return the applicationContext
     */
    public ConcurrentHashMap<Object, Object> getApplicationContext() {
        return applicationContext;
    }

    /**
     * 创建时间:2011-12-16上午10:02:04 set方法
     *
     * @param applicationContext
     *            the applicationContext to set
     */
    public void setApplicationContext(ConcurrentHashMap<Object, Object> applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 创建时间:2011-10-19上午9:21:32 get方法
     *
     * @return the tmpData
     */
    public ConcurrentHashMap<Object, Object> getTmpData() {
        return tmpData;
    }

    /**
     * 创建时间:2011-10-19上午9:21:32 set方法
     *
     * @param tmpData
     *            the tmpData to set
     */
    public void setTmpData(ConcurrentHashMap<Object, Object> tmpData) {
        this.tmpData = tmpData;
    }

    /**
     * 创建时间:2012-10-10上午11:50:00 get方法
     *
     * @return the localTempData
     */
    public ConcurrentHashMap<Object, Object> getLocalTempData() {
        return localTempData;
    }

    /**
     * 创建时间:2012-10-10上午11:50:00 get方法
     *
     * @param localTempData
     *            the localTempData to set
     */
    public void setLocalTempData(ConcurrentHashMap<Object, Object> localTempData) {
        this.localTempData = localTempData;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }


    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }
    /**
     *
     * 获取当前会话的服务调用层次
     *
     * @return
     */
    public int getServiceCallLevel() {
        return this.serviceCallLevel.get();
    }

    /**
     * 设置当前会话的服务调用层次
     *
     * @param count
     * @return
     */
    public int changeServiceCallLevel(int count) {
        return this.serviceCallLevel.addAndGet(count);
    }

    public String[] getServers() {
        if (servers == null || servers.length == 0) {
            synchronized (this) {
                this.addThisServer();
            }
        }
        return servers;
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }

    public int getTransactionInfoIndex() {
        return transactionInfoIndex;
    }

    public void setTransactionInfoIndex(int transactionInfoIndex) {
        this.transactionInfoIndex = transactionInfoIndex;
    }

    public synchronized String[] addThisServer() {
        if (this.servers == null) {
            this.servers = new String[] { ESBServerContext.getServerName() };
        } else if (!this.servers[0].equals(ESBServerContext.getServerName())) {
            String[] servers = new String[this.servers.length + 1];
            System.arraycopy(this.servers, 0, servers, 1, this.servers.length);
            servers[0] = ESBServerContext.getServerName();
            this.servers = servers;
        }

        return this.servers;
    }

    public Stack<String> getServiceCallStack() {
        return this.serviceCallStack;
    }

    /**
     * 清空Session
     *
     * @Time 创建时间:2011-11-8上午9:01:41
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public BaseSession clear() {
        this.startTime = System.currentTimeMillis();
        this.parentSessionId = null;
        this.sessionId = null;
        this.userID = null;
        this.orgID = null;
        this.serviceCallLevel.set(0);
        this.serviceCallStack.clear();
        this.systemContext = null;
        this.applicationContext = null;
        this.tmpData = null;
        this.localTempData = null;
        this.servers = null;
        this.addThisServer();
        return this;
    }

}
