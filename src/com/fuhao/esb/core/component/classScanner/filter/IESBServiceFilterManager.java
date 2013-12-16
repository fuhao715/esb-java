package com.fuhao.esb.core.component.classScanner.filter;
import java.util.List;
/**
 * package name is  com.fuhao.esb.core.component.classScanner.filter
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBServiceFilterManager {


    /**
     * @name 获取注册的所有服务前项拦截器
     * @return
     */
    public List<IESBServiceBeforeFilter> getAllBeforeFilter();

    /**
     * @name 注册服务前项拦截器
     * @param filter
     * @param index
     */
    public void registerBeforeFilter(IESBServiceBeforeFilter filter, int index);

    /**
     * @name 注销服务前项拦截器
     * @param filter
     */
    public void unRegisterBeforeFilter(IESBServiceBeforeFilter filter);

    /**
     * @name 获取注册的所有服务后项拦截器
     * @return
     */
    public List<IESBServiceAfterFilter> getAllAfterFilter();

    /**
     * @name 注册服务后项拦截器
     * @param filter
     * @param index
     */
    public void registerAfterFilter(IESBServiceAfterFilter filter, int index);

    /**
     * @name 注销服务后项拦截器
     * @param filter
     */
    public void unRegisterAfterFilter(IESBServiceAfterFilter filter);

    /**
     * @name 获取注册的所有服务异常拦截器
     * @return
     */
    public List<IESBServiceExceptionFilter> getAllExceptionFilter();

    /**
     * @name 注册服务异常拦截器
     * @param filter
     * @param index
     */
    public void registerExceptionFilter(IESBServiceExceptionFilter filter, int index);

    /**
     * @name 注销服务异常拦截器
     * @param filter
     */
    public void unRegisterExceptionFilter(IESBServiceExceptionFilter filter);
}
