package com.fuhao.esb.core.component.utils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;
import com.fuhao.esb.core.component.ESBPlatformConstrants;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.ESBServiceProxyClassGenerator;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public class CheckUtils extends ESBPlatformConstrants {
    private static ESBLogUtils log = ESBLogUtils.getLogger(CheckUtils.class);

    /**
     * 平台类检查方法
     *
     * 用于检查类是否为ESB平台的类
     */
    public static boolean checkESBPlatformClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.getName().startsWith(ESB_PACKAGE_HEAD);
    }

    /**
     * 调用者检查
     *
     * @param ste
     *            调用堆栈
     * @param isSecurityCheck
     *            是否安全检查：安全检查时检查的是调用者类型，非安全检查是检查的是执行者类型
     * @return
     */
    public static boolean checkESBPlatformClass(StackTraceElement[] ste, boolean isSecurityCheck) {
        if (ste == null) {
            return false;
        }

        final String className = ste[isSecurityCheck ? 3 : 0].getClassName();
        return className.startsWith(ESB_PACKAGE_HEAD);
    }

    /**
     * 执行调用安全性检查
     */
    public static void callSecurityCheck() throws ESBBaseCheckedException {
        StackTraceElement[] caller = Thread.currentThread().getStackTrace();
        if (!checkESBPlatformClass(caller, true)) {
            throw new ESBBaseCheckedException("UT-00001:不允许调用" + caller[3].getClassName() + "." + caller[3].getMethodName());
        }
    }

    /**
     * 检查类是否包含非静态属性
     *
     * @param clazz
     */
    public static void checkClassHasNonStaticFields(Class<?> clazz) {
        if (!ESBServerContext.isDevelopMode()) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            String fieldDefineInfo = field.toGenericString();
            if (fieldDefineInfo.indexOf("static") == -1) {
                log.warn("警告:" + clazz.getName() + "." + field.getName() + "不是静态属性，单例模式下有可能会发生值混乱的问题");
            }
        }
    }

    public static int hasMultiResource(String name) throws IOException {
        int count = 0;
        Enumeration<URL> itr = ESBFileUtils.getResources(name);
        while (itr.hasMoreElements()) {
            log.info("发现" + itr.nextElement());
            count++;
        }
        return count;
    }

    /**
     * 方法调用者信息查询
     */
    public static String[] whoCallMe() {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
        ste = Thread.currentThread().getStackTrace()[5];
        String[] info = new String[] { ste.getClassName(), ste.getMethodName(), "" + ste.getLineNumber() };
        return info;
    }
}
