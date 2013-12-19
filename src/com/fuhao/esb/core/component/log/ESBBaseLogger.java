package com.fuhao.esb.core.component.log;


import java.util.Enumeration;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.session.ESBSessionUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * package name is  com.fuhao.esb.core.component.log
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBBaseLogger {


    private final Logger log;

    public ESBBaseLogger(Class<?> clazz) {
        this.log = org.apache.log4j.Logger.getLogger(clazz);
    }

    public void trace(Object message) {
        log.trace(processMessage(message, null));
    }

    public void trace(Object message, Throwable t) {
        log.trace(processMessage(message, t), t);
    }

    public void debug(Object message, Throwable t) {
        log.debug(processMessage(message, t), t);
    }

    public void debug(Object message) {
        log.debug(processMessage(message, null));
    }

    public void info(Object message) {
        log.info(processMessage(message, null));
    }

    public void info(Object message, Throwable t) {
        log.info(processMessage(message, t), t);
    }

    public void warn(Object message) {
        log.warn(processMessage(message, null));
    }

    public void warn(Object message, Throwable t) {
        log.warn(processMessage(message, t), t);
    }

    public void error(Object message) {
        log.error(processMessage(message, null));
    }

    public void error(Object message, Throwable t) {
        log.error(processMessage(message, t), t);
    }

    public void fatal(Object message) {
        log.fatal(processMessage(message, null));
    }

    public void fatal(Object message, Throwable t) {
        log.fatal(processMessage(message, t), t);
    }

    public final String getName() {
        return log.getName();
    }

    public void setLevel(Level level) {
        log.setLevel(level);
    }

    public final Level getLevel() {
        return log.getLevel();
    }

    public Level getEffectiveLevel() {
        return log.getEffectiveLevel();
    }

    public boolean isGreaterOrEqual(Level level) {
        return !level.isGreaterOrEqual(this.log.getEffectiveLevel());
    }

    public boolean equals(Object obj) {
        return log.equals(obj);
    }

    public int hashCode() {
        return log.hashCode();
    }

    public String toString() {
        return log.toString();
    }

    private Object processMessage(Object obj, Throwable t) {
            StringBuilder str = new StringBuilder();
            obj = str.append("会话ID:").append(ESBSessionUtils.getSessionID()).append(":").append(ESBSessionUtils.getServers().length)
                    .append(":").append(obj);

        if (t != null && t instanceof ESBBaseCheckedException) {
            Category log = this.log;
            boolean find = false;
            while (true) {
                Object appender;
                for (Enumeration<?> allAppenders = log.getAllAppenders(); allAppenders.hasMoreElements();) {
                    appender = allAppenders.nextElement();
                    if (appender instanceof ESBRollingFileAppender) {
                        String logFileName = ((ESBRollingFileAppender) appender).getNextBackupFileName();
                        ((ESBBaseCheckedException) t).setLogStorage(logFileName);
                        find = true;
                        break;
                    }
                }

                if (find || (log = log.getParent()) == null) {
                    break;
                }
            }
        }

        return obj;
    }
}
