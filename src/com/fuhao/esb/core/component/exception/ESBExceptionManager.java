package com.fuhao.esb.core.component.exception;

import com.fuhao.esb.core.component.AbsESBComponentManager;
import com.fuhao.esb.core.component.manager.ESBPlatformMXBeanManager;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component.exception
 * Created by fuhao on 13-12-11.
 * Project Name esb-java
 */
public class ESBExceptionManager  extends AbsESBComponentManager implements IESBExceptionManagerMXBean  {
    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBExceptionManager.class);
    @Override
    synchronized public void start(Map<String, String> parameter) throws ESBBaseCheckedException {
        if (this.state != ComponentState.STOP) {
            return;
        }
        this.state = ComponentState.STARTING;
        // 注册JMX管理器
        ESBPlatformMXBeanManager.registerMBean(null, "ExceptionManager", this);

        this.setMemo("异常组件管理器：用于管理系统异常信息");
        this.state = ComponentState.RUNNING;
    }

    @Override
    synchronized public void stop() throws ESBBaseCheckedException {
        this.state = ComponentState.STOP;
    }

    @Override
    synchronized public void reload() throws ESBBaseCheckedException {
        if (this.state != ComponentState.STOP) {
            return;
        }
        this.state = ComponentState.STARTING;
        // 注册JMX管理器
        ESBPlatformMXBeanManager.registerMBean(null, "ExceptionManager", this);

        this.setMemo("异常组件管理器：用于管理系统异常信息");
        this.state = ComponentState.RUNNING;
    }


    @Override
    public String getServerNames() {
        return null;
    }

    @Override
    public long getTime() {
        return 0;
    }
}
