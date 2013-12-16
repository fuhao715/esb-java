package com.fuhao.esb.core.component.classScanner.filter;

import com.fuhao.esb.core.component.ESBComponentRef;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.classScanner.filter
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public class ESBServiceCallLevelChecker implements IESBServiceBeforeFilter,IESBServiceAfterFilter,IESBServiceExceptionFilter {


    @Override
    public void before(Class<?> serviceClass, String serviceName, Object[] args) throws ESBBaseCheckedException {
        int maxServerCallLevel = ESBComponentRef.serviceManager.getMaxServerCallLevel();
        int currServerCallLevel = ESBServerContext.getSession().getServers().length;
        if (maxServerCallLevel > 0 && currServerCallLevel > maxServerCallLevel) {
            throw new ESBBaseCheckedException("服务调用请求经过的服务器数量超过允许的最大深度" + maxServerCallLevel + "，请确认算法是否存在问题");
        }

        int maxServiceCallLevel = ESBComponentRef.serviceManager.getMaxServiceCallLevel();
        int currServiceCallLevel = ESBServerContext.getSession().changeServiceCallLevel(1);
        if (maxServerCallLevel > 0 && currServiceCallLevel > maxServiceCallLevel) {
            throw new ESBBaseCheckedException("服务嵌套调用层次超过允许的最大深度" + maxServiceCallLevel + "，请确认算法是否存在问题");
        }
    }

    @Override
    public void after(Class<?> serviceClass, String serviceName, Object[] args, Object value) throws ESBBaseCheckedException {
        ESBServerContext.getSession().changeServiceCallLevel(-1);
    }

    @Override
    public void exception(Class<?> serviceClass, String serviceName, Object[] args, ESBBaseCheckedException exception)
            throws ESBBaseCheckedException {
        ESBServerContext.getSession().changeServiceCallLevel(-1);
    }
}
