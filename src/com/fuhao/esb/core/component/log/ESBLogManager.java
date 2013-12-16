package com.fuhao.esb.core.component.log;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.fuhao.esb.core.component.manager.ESBPlatformMXBeanManager;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 * package name is  com.fuhao.esb.core.component.log
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBLogManager implements IESBLogMXBean{

    public void start() throws ESBBaseCheckedException {
        ESBPlatformMXBeanManager.registerMBean(null, "LogManager", this);
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getLoggerNames() {
        List<String> names = new ArrayList<String>(5);
        Enumeration<Logger> loggers = LogManager.getLoggerRepository().getCurrentLoggers();
        while (loggers.hasMoreElements()) {
            names.add(loggers.nextElement().getName());
        }
        return names;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setLogLevel(String name, String level) throws Exception {
        Logger logger = LogManager.getLogger(name);
        if (logger.getLevel() == null) {
            logger.setLevel(Level.toLevel(level));
        } else {
            Enumeration<Logger> loggers = LogManager.getLoggerRepository().getCurrentLoggers();
            while (loggers.hasMoreElements()) {
                logger = loggers.nextElement();
                if (logger.getName().startsWith(name)) {
                    logger.setLevel(Level.toLevel(level));
                }
            }
        }
    }

    @Override
    public String getLogLevel(String name) throws Exception {
        Logger logger = Logger.getLogger(name);
        Level level = logger.getLevel();
        if (level == null) {
            level = Logger.getRootLogger().getLevel();
        }
        return level.toString();
    }
}
