package com.fuhao.esb.core.component;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBPlatformConstrants {
    // ---------------------JVM系统环境变量区键常量-------------------------------------------------------------------------------

    /**
     * 平台JMX管理端口
     */
    public static final String ESB_SYSTEM_PROPERTY_JMX_PORT = "ESB-JmxPort";

    // ---------------------平台内部变常量-------------------------------------------------------------------------------
    /**
     * ESB平台包前缀
     */
    public static final String ESB_PACKAGE_HEAD;

    /**
     * 本地服务调用处理器
     */
    public static final String ESB_LOCAL_INVOKE_SENDER = "ESBLocalInvokeSender";

    /**
     * 执行过程中未抛出的异常
     */
    public static final String ESB_NOT_THROW_EXCEPTION = "ESBNotThrowException";

    /**
     * 失败即停的配置项名称
     */
    public static final String FAIL_STOP = "failStop";

    static {
        String str = ESBPlatformConstrants.class.getPackage().getName();
        ESB_PACKAGE_HEAD = str.substring(0, str.indexOf('.', str.indexOf('.', str.indexOf('.') + 1) + 1));
    }
}
