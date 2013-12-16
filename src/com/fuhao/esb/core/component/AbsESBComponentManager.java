package com.fuhao.esb.core.component;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public abstract class AbsESBComponentManager {

    /**
     * 组件状态
     *
     * @author fuhao
     */
    public static class ComponentState {
        public static final byte STARTING = 1;
        public static final byte RUNNING = 2;
        public static final byte STOP = 3;
        public static final byte ERROR = 4;
    }

    private String name;

    /**
     * 组件的当前状态
     */
    protected volatile byte state = ComponentState.STOP;

    /**
     * 组件说明
     */
    private String memo;

    /**
     * 加载模块
     */
    public void start(Map<String, String> parameter) throws ESBBaseCheckedException {
        this.state = ComponentState.RUNNING;
    }

    /**
     * 平台已成功启动
     */
    public void platformStarted() throws ESBBaseCheckedException {
    }

    /**
     * 停止模块
     *
     * @throws ESBBaseCheckedException
     */
    public void stop() throws ESBBaseCheckedException {
        this.state = ComponentState.STOP;
    }

    /**
     * 重新装载模块
     *
     * @throws ESBBaseCheckedException
     */
    public void reload() throws ESBBaseCheckedException {
    }

    /**
     * 组件是否正在运行
     */
    public boolean isRunning() {
        return this.state == ComponentState.RUNNING;
    }

    /**
     * 获取组件的当前状态
     *
     * @return
     */
    public byte getComponentState() {
        return this.state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置组件说明信息
     * 组件说明信息为架构组件的配置项的memo属性
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 获取组件说明信息
     */
    public String getMemo() {
        return this.memo;
    }
}
