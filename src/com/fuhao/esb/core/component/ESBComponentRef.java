package com.fuhao.esb.core.component;

import com.fuhao.esb.core.component.classScanner.IESBServiceManager;
import com.fuhao.esb.core.component.exception.IESBExceptionManagerMXBean;
import com.fuhao.esb.core.component.log.ESBBaseLogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 */
public class ESBComponentRef {
    /**
     * 所有组件名的有序索引，停止系统时使用
     */
    public static final List<String> allComponentName = new ArrayList<String>();

    /**
     * 已成功加载的ESB组件管理器
     */
    public static final Map<String, AbsESBComponentManager> componentManagerIndexer = new HashMap<String, AbsESBComponentManager>();

    /**
     * 日志管理器
     */
    public static ESBBaseLogManager logManager;
    /**
     * 异常管理器
     */
    public static IESBExceptionManagerMXBean exceptionManager;

    /**
     * ESB服务管理器
     */
    public static IESBServiceManager serviceManager;
    /**
     * 事务处理监控器
     */
    // public static IESBTPMonitor tpMonitor;
    /**
     * 持久层管理器
     */
   //  public static AbsESBPersistenceComponentManager persistenceManager;
    /**
     * 中介服务管理器
     */
   // public static IESBComponentManager esbManager;

    /**
     * 性能监控组件管理器
     */
    // public static AbsPerformanceComponentManager performanceManager;

    /**
     * 其它功能组件管理器
     */
    public static final Map<String, AbsESBComponentManager> othersComponentManager = new HashMap<String, AbsESBComponentManager>();
    /**
     * 动态脚本引擎组件管理器
     */
    // public static IESBScriptEngineComponentManager scriptManager;

    /**
     * NoSQL数据库管理组件
     */
    // public static IESBNoSQLComponentManager noSqlManager;
}
