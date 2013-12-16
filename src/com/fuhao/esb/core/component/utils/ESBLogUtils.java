package com.fuhao.esb.core.component.utils;

import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.ESBServiceProxyClassGenerator;
import com.fuhao.esb.core.component.log.ESBLogger;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.exception.ExceptionInfos;
import org.apache.log4j.Level;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBLogUtils {

    private ESBLogger log = null;

    protected ESBLogUtils(Class<?> clazz) {
        if (!ESBServerContext.isProductMode()) {
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            String className = ste[3].getClassName();
            Class<?> callClazz = null;

            try {
                callClazz = Class.forName(className);
            } catch (ClassNotFoundException ex) {
            }

            if (ClassUtils.isAbstract(callClazz) || callClazz.isInterface()) {
                // 跳过抽象类和接口的检查
            } else if (callClazz != clazz && callClazz.isAssignableFrom(clazz)) {
                // 跳过Abstract类的日志对象检查
            } else if (!className.equals(clazz.getName())) {
                StringBuilder msg = new StringBuilder(100);
                msg.append("UT-14001:类").append(className).append("第").append(ste[3].getLineNumber()).append("行")
                        .append("创建日志输出对象的参数不是当前类");
                throw new IllegalArgumentException(msg.toString());
            }
        }
        this.log = new ESBLogger(clazz);
    }

    public static ESBLogUtils getLogger(Class<?> clazz) {
        return new ESBLogUtils(clazz);
    }

    public void trace(Object message) {
        if (check(Level.TRACE, null, message)) {
            String msg = processMessage(message);
            this.log.trace(msg);
        }
    }

    public void trace(Object... message) {
        if (check(Level.TRACE, null, message)) {
            String msg = processMessage(message);
            this.log.trace(msg);
        }
    }

    public void debug(Object message) {
        if (check(Level.DEBUG, null, message)) {
            String msg = processMessage(message);
            this.log.debug(msg);
        }
    }

    public void debug(Object... message) {
        if (check(Level.DEBUG, null, message)) {
            String msg = processMessage(message);
            this.log.debug(msg);
        }
    }

    public void debug(Object message, Throwable ex) {
        if (check(Level.DEBUG, null, message)) {
            String msg = processMessage(message);
            this.log.debug(msg, ex);
        }
    }

    public void info(Object message) {
        if (check(Level.INFO, null, message)) {
            String msg = processMessage(message);
            this.log.info(msg);
        }
    }

    public void info(Object... message) {
        if (check(Level.INFO, null, message)) {
            String msg = processMessage(message);
            this.log.info(msg);
        }
    }

    public void info(Object message, Throwable ex) {
        if (check(Level.INFO, ex, message)) {
            String msg = processMessage(message);
            this.log.info(msg, ex);
        }
    }

    public void warn(Object message) {
        if (check(Level.WARN, null, message)) {
            String msg = processMessage(message);
            this.log.warn(msg);
        }
    }

    public void warn(Object... message) {
        if (check(Level.WARN, null, message)) {
            String msg = processMessage(message);
            this.log.warn(msg);
        }
    }

    public void warn(Object message, Throwable ex) {
        if (check(Level.WARN, ex, message)) {
            String msg = processMessage(message);
            this.log.warn(msg, ex);
        }
    }

    public void warn(ESBBaseCheckedException ex) {
        if (check(Level.WARN, ex)) {
            log.warn("", ex);
        }
    }

    public void error(Object message) {
        if (check(Level.ERROR, null, message)) {
            String msg = processMessage(message);
            this.log.error(msg);
        }
    }

    public void error(Object... message) {
        if (check(Level.ERROR, null, message)) {
            String msg = processMessage(message);
            this.log.error(msg);
        }
    }

    public void error(Object message, Throwable ex) {
        if (check(Level.ERROR, ex, message)) {
            String msg = processMessage(message);
            this.log.error(msg, ex);
            ExceptionInfos.addException(ex);
        }
    }

    public void error(ESBBaseCheckedException ex) {
        if (check(Level.ERROR, ex)) {
            this.log.error("", ex);
            ExceptionInfos.addException(ex);
        }
    }

    public void fatal(Object message) {
        if (check(Level.FATAL, null, message)) {
            this.log.fatal(processMessage(message));
        }
    }

    public void fatal(Object... message) {
        if (check(Level.FATAL, null, message)) {
            String msg = processMessage(message);
            this.log.fatal(msg);
        }
    }

    public void fatal(Object message, Throwable ex) {
        if (check(Level.FATAL, ex, message)) {
            String msg = processMessage(message);
            this.log.fatal(msg, ex);
            ExceptionInfos.addException(ex);
        }
    }

    public void fatal(ESBBaseCheckedException ex) {
        if (check(Level.FATAL, ex)) {
            log.fatal("", ex);
            ExceptionInfos.addException(ex);
        }
    }

    private boolean check(Level level, Throwable ex, Object... message) {
        if (this.log.isGreaterOrEqual(level)) {
            return false;
        }

        if (ESBServerContext.isProductMode()) {
            return true;
        }

        if (message != null && message.length > 0) {
            for (int i = 0; i < message.length; i++) {
                if (message[i] == null) {
                    continue;
                } else if (message[i] instanceof Map && ((Map<?, ?>) message[i]).size() > 1000) {
                    throw new IllegalArgumentException("UT-14002:异常信息对象" + i + "(Map)的键值数大于1000");
                } else if (message[i] instanceof Collection && ((Collection<?>) message[i]).size() > 1000) {
                    throw new IllegalArgumentException("UT-14003:异常信息对象" + i + "(Collection)的元素个数大于1000");
                } else if (message[i].getClass().isArray() && Array.getLength(message[i]) > 1000) {
                    throw new IllegalArgumentException("UT-14004:异常信息对象" + i + "数组元素个数大于1000");
                }
            }
        }

        return true;
    }

    private String processMessage(Object... message) {
        if (message == null) {
            return null;
        }

        StringBuilder msg = new StringBuilder(38 + 50);

        for (Object obj : message) {
            msg.append(obj);
        }

        return msg.toString();
    }
}
