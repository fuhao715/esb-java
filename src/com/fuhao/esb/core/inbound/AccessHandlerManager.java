package com.fuhao.esb.core.inbound;

import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.inbound.aip.ESBAipInboundManager;

/**
 * package name is  com.fuhao.esb.core.inbound
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class AccessHandlerManager {

    /**
     * 接入处理组件
     */
    private static AbstractAccessHandler accessHandler = null;

    /**
     * 日志
     */
    private static ESBLogUtils logger = ESBLogUtils.getLogger(AccessHandlerManager.class);

    /**
     *
     *@name    获取接入处理组件
     */
    public static AbstractAccessHandler getAccessHandler(){
        if (accessHandler == null)
            accessHandler = new ESBAipInboundManager();
        return accessHandler;
    }

    /**
     *
     *@name    设置接入处理组件
     */
    public static void setAccessHandler(String className) throws ESBBaseCheckedException {
        if (className == null || className.isEmpty())
            return;
        try {
            AbstractAccessHandler handler = (AbstractAccessHandler)Class.forName(className).newInstance();
            accessHandler = handler;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new ESBBaseCheckedException(e.getMessage(),e);
        }
    }
}
