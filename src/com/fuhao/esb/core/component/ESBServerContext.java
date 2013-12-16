package com.fuhao.esb.core.component;

import com.fuhao.esb.core.component.classScanner.filter.ESBServiceCallSecurityInfo;
import com.fuhao.esb.core.session.BaseSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 */
public final class ESBServerContext {


    /**
     * 服务器启动时间
     */
    private static String serverStartTime = null;

    /**
     * 服务器运行状态
     */
    private static byte serverState = AbsESBComponentManager.ComponentState.STOP;

    /**
     * 主机名
     */
    private static String hostname = null;

    /**
     * 当前JVM进程编号
     */
    private static int pid = 0;

    /**
     * 服务器名
     */
    private static String serverName = null;

    /**
     * 服务器类型
     */
    private static String serverType = null;

    /**
     * 结点名
     */
    private static String nodeName = null;

    /**
     * 机器说明
     */
    private static String memo = null;

    /**
     * 机架ID
     */
    private static String rackID = null;

    /**
     * 集群名
     */
    private static String clusterName = null;

    /**
     * 集群成员列表
     */
    private static volatile Map<String, ?> clusterMembers;

    /**
     * 服务器运行模式
     */
    private static byte serverRunningMode;

    /**
     * Session管理器
     */
    private static final ThreadLocal<BaseSession> sessionManager = new ThreadLocal<BaseSession>();
    /**
     * 服务调用检查信息
     */
    private static final List<ESBServiceCallSecurityInfo> serviceCallCheckInfo = new ArrayList<ESBServiceCallSecurityInfo>();

    /**
     * 检查服务调用的参数和返回值是否可以进行序列化
     */
    private static boolean checkSerializable = false;

    /**
     * 检查服务调用过程中生成异常对象但未抛出的情况
     */
    private static boolean checkNotThrowException = false;

    /**
     * 是否启用EPoll模式
     */
    private static boolean useEPoll = false;

    /**
     * 失败即停模式
     */
    private static boolean failStop = false;

    /**
     * 获取服务器启动时间
     */
    public static String getServerStartTime() {
        return serverStartTime;
    }

    /**
     * 设置服务器启动时间
     */
    static void setServerStartTime(String serverStartTime) {
        ESBServerContext.serverStartTime = serverStartTime;
    }

    /**
     * 获取服务器运行状态
     */
    public static byte getServerState() {
        return serverState;
    }

    /**
     * 设置服务器运行状态
     */
    static void setServerState(byte serverState) {
        ESBServerContext.serverState = serverState;
    }

    /**
     * 获取主机名
     */
    public static String getHostname() {
        return hostname;
    }

    /**
     * 保存操作系统名
     */
    static void setHostname(String hostname) {
        ESBServerContext.hostname = hostname;
    }

    /**
     * 获取当前JVM进程编号
     */
    public static int getPid() {
        return pid;
    }

    /**
     * 保存操作系统名
     */
    static void setPid(int pid) {
        ESBServerContext.pid = pid;
    }

    /**
     * 获取服务器名
     *
     * @return
     */
    public static String getServerName() {
        return serverName;
    }

    /**
     * 设置服务器名
     *
     * @param serverName
     */
    public static void setServerName(String serverName) {
        ESBServerContext.serverName = serverName;
    }

    /**
     * 获取服务器类型
     *
     * @return
     */
    public static String getServerType() {
        return serverType;
    }

    /**
     * 设置服务器类型
     *
     * @param serverType
     */
    static void setServerType(String serverType) {
        ESBServerContext.serverType = serverType;
    }

    /**
     * 获取结点名
     *
     * @return
     */
    public static String getNodeName() {
        return nodeName;
    }

    /**
     * 设置结点名
     *
     * @param nodeName
     */
    static void setNodeName(String nodeName) {
        ESBServerContext.nodeName = nodeName;
    }

    /**
     * 获取服务器说明
     */
    public static String getServerMemo() {
        return memo;
    }

    /**
     * 设置服务器说明
     *
     * @param memo
     */
    public static void setServerMemo(String memo) {
        ESBServerContext.memo = memo;
    }

    /**
     * 获取机架ID
     *
     * @return
     */
    public static String getRackID() {
        return rackID;
    }

    /**
     * 设置机架ID
     *
     * @param rackID
     */
    static void setRackID(String rackID) {
        ESBServerContext.rackID = rackID;
    }

    /**
     * 获取集群名
     *
     * @return
     */
    public static String getClusterName() {
        return clusterName;
    }

    /**
     * 设置集群名
     *
     * @param clusterName
     */
    public static void setClusterName(String clusterName) {
        ESBServerContext.clusterName = clusterName;
    }

    /**
     * 获取集群成员列表
     *
     * @return
     */
    public static Map<String, ?> getClusterMembers() {
        return clusterMembers;
    }

    /**
     * 更新集群成员列表
     *
     * @param clusterMembers
     */
    public static void updateClusterMembers(Map<String, ?> clusterMembers) {
        ESBServerContext.clusterMembers = clusterMembers;
    }

    /**
     * 获取服务器运行模式
     *
     * @return
     */
    public static byte getServerRunningMode() {
        return serverRunningMode;
    }

    /**
     * 设置服务器运行状态
     *
     * @param serverRunningModel
     */
    public static void setServerRunningMode(byte serverRunningModel) {
        ESBServerContext.serverRunningMode = serverRunningModel;
    }

    /**
     * 是否开发模式
     *
     * @return
     */
    public static boolean isDevelopMode() {
        return ServerRunningMode.DEVELOP_MODE == serverRunningMode;
    }

    /**
     * 是否生产模式
     *
     * @return
     */
    public static boolean isProductMode() {
        return ServerRunningMode.PRODUCT_MODE == serverRunningMode;
    }

    /**
     * 是否测试模式
     *
     * @return
     */
    public static boolean isTestMode() {
        return ServerRunningMode.TEST_MODE == serverRunningMode;
    }
    /**
     * 索引一个指定的会话对象
     */
    public static BaseSession indexSession(BaseSession session) {
        if (session == null) {
            session = new BaseSession();
        } else {
            session = new BaseSession(session, true);
        }
        sessionManager.set(session);
        return session;
    }

    /**
     * 获取会话对象
     *
     * @return
     */
    public static BaseSession getSession() {
        BaseSession session = sessionManager.get();
        if (session == null) {
            session = indexSession(null);
        }
        return session;
    }
    /**
     * 创建时间:2011-8-6上午9:43:55 get方法
     *
     * @return the servicecallcheckinfo
     */
    public static List<ESBServiceCallSecurityInfo> getServicecallcheckinfo() {
        return serviceCallCheckInfo;
    }
    public static boolean isCheckSerializable() {
        return checkSerializable;
    }

    public static void setCheckSerializable(boolean checkSerializable) {
        ESBServerContext.checkSerializable = checkSerializable;
    }

    public static boolean isCheckNotThrowException() {
        return checkNotThrowException;
    }

    public static void setCheckNotThrowException(boolean checkNotThrowException) {
        ESBServerContext.checkNotThrowException = checkNotThrowException;
    }

    public static boolean isUseEPoll() {
        return useEPoll;
    }

    static void enableUseEPoll() {
        ESBServerContext.useEPoll = true;
    }

    public static boolean isFailStop() {
        return failStop;
    }

    static void setFailStop(boolean failStop) {
        ESBServerContext.failStop = failStop;
    }


}
