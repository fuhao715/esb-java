package com.fuhao.esb.core.component.classScanner.filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import org.dom4j.Element;
/**
 * package name is  com.fuhao.esb.core.component.classScanner.filter
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public class ESBServiceFilterManager implements IESBServiceFilterManager{
    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBServiceFilterManager.class);

    private volatile List<IESBServiceBeforeFilter> beforeFilters = null;
    private volatile List<IESBServiceAfterFilter> afterFilters = null;
    private volatile List<IESBServiceExceptionFilter> exceptionFilters = null;

    /**
     * @param serviceFilterConfig
     * @throws ESBBaseCheckedException
     */
    public ESBServiceFilterManager(List<Element> serviceFilterConfig) throws ESBBaseCheckedException {
        if (serviceFilterConfig == null || serviceFilterConfig.isEmpty()) {
            return;
        }

        for (Element element : serviceFilterConfig) {
                this.generateServiceFielter(element);
        }
    }

    /**
     * @name 生成服务级拦截器
     * @Description 相关说明
     * @Time 创建时间:2012-2-23上午10:36:25
     * @param filterConfig
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private void generateServiceFielter(Element filterConfig) throws ESBBaseCheckedException {
        String filerName = filterConfig.attributeValue("name");
        String beforeFilterClass = null;
        Object beforeFilter = null;
        String afterFilterClass = null;
        Object afterFilter = null;
        String exceptionFilterClass = null;
        Object exceptionFilter = null;

        // 注册服务前项拦截器
        beforeFilterClass = filterConfig.attributeValue("beforeFilter");
        if (beforeFilterClass != null && !"".equals(beforeFilterClass)) {
            try {
                beforeFilter = Class.forName(beforeFilterClass).newInstance();
                this.registerBeforeFilter((IESBServiceBeforeFilter) beforeFilter, -1);
            } catch (Exception ex) {
                throw new ESBBaseCheckedException("注册服务前项拦截器" + filerName + "时发生错误", ex);
            }
        }

        // 注册服务后项拦截器
        afterFilterClass = filterConfig.attributeValue("afterFilter");
        if (afterFilterClass != null && !"".equals(afterFilterClass)) {
            if (afterFilterClass.equals(beforeFilterClass)) {
                afterFilter = beforeFilter;
            } else {
                try {
                    afterFilter = Class.forName(afterFilterClass).newInstance();
                } catch (Exception ex) {
                    throw new ESBBaseCheckedException("注册服务后项拦截器" + filerName + "时发生错误", ex);
                }
            }

            this.registerAfterFilter((IESBServiceAfterFilter) afterFilter, -1);
        }

        // 注册服务异常拦截器
        exceptionFilterClass = filterConfig.attributeValue("exceptionFilter");
        if (exceptionFilterClass != null && !"".equals(exceptionFilterClass)) {
            if (exceptionFilterClass.equals(beforeFilterClass)) {
                exceptionFilter = beforeFilter;
            }
            if (exceptionFilterClass.equals(afterFilterClass)) {
                exceptionFilter = afterFilter;
            } else {
                try {
                    exceptionFilter = Class.forName(exceptionFilterClass).newInstance();
                } catch (Exception ex) {
                    throw new ESBBaseCheckedException("注册服务异常拦截器" + filerName + "时发生错误", ex);
                }
            }

            this.registerExceptionFilter((IESBServiceExceptionFilter) exceptionFilter, -1);
        }
    }



    @Override
    public List<IESBServiceBeforeFilter> getAllBeforeFilter() {
        return this.beforeFilters;
    }

    @Override
    public synchronized void registerBeforeFilter(IESBServiceBeforeFilter filter, int index) {
        if (beforeFilters == null) {
            beforeFilters = new ArrayList<IESBServiceBeforeFilter>();
        }
        if (index < 0) {
            beforeFilters.add(filter);
        } else {
            beforeFilters.add(index, filter);
        }
    }

    @Override
    public synchronized void unRegisterBeforeFilter(IESBServiceBeforeFilter filter) {
        if (beforeFilters == null) {
            return;
        }

        beforeFilters.remove(filter);
    }

    @Override
    public List<IESBServiceAfterFilter> getAllAfterFilter() {
        return this.afterFilters;
    }

    @Override
    public synchronized void registerAfterFilter(IESBServiceAfterFilter filter, int index) {
        if (afterFilters == null) {
            afterFilters = new ArrayList<IESBServiceAfterFilter>();
        }
        if (index < 0) {
            afterFilters.add(filter);
        } else {
            afterFilters.add(index, filter);
        }
    }

    @Override
    public synchronized void unRegisterAfterFilter(IESBServiceAfterFilter filter) {
        if (afterFilters == null) {
            return;
        }

        afterFilters.remove(filter);
    }

    @Override
    public List<IESBServiceExceptionFilter> getAllExceptionFilter() {
        return this.exceptionFilters;
    }

    @Override
    public synchronized void registerExceptionFilter(IESBServiceExceptionFilter filter, int index) {
        if (exceptionFilters == null) {
            exceptionFilters = new ArrayList<IESBServiceExceptionFilter>();
        }
        if (index < 0) {
            exceptionFilters.add(filter);
        } else {
            exceptionFilters.add(index, filter);
        }
    }

    @Override
    public synchronized void unRegisterExceptionFilter(IESBServiceExceptionFilter filter) {
        if (exceptionFilters == null) {
            return;
        }

        exceptionFilters.remove(filter);
    }
}
