package com.fuhao.esb.core.component.classScanner;
import com.fuhao.esb.core.component.classScanner.filter.IESBServiceFilterManager;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.component.service.ESBServiceContainer;

import java.util.Collection;
import java.util.List;
/**
 * package name is  com.fuhao.esb.core.component.classScanner
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBServiceManager extends IESBServiceManagerMXBean{

    /**
     * 根据服务容器名查找服务容器
     *
     * @param serviceContainerName
     * @return
     * @throws ESBBaseCheckedException
     */
    public List<ESBServiceContainer> findServiceContainersByContainerName(String serviceContainerName) throws ESBBaseCheckedException;

    /**
     * 根据服务名查找服务
     *
     * @param serviceName
     * @return
     * @throws ESBBaseCheckedException
     */
    public ESBServiceContainer findServiceContainerByServieName(String serviceName) throws ESBBaseCheckedException;

    /**
     * 获取服务拦截器管理器
     *
     * @return
     */
    public IESBServiceFilterManager getServiceFilterManager();

    /**
     * 获取所有的服务名
     *
     * @return
     */
    @Deprecated
    public Collection<ESBServiceContainer> getAllService();

    /**
     * 获取所有本地服务的注册信息
     *
     * @Description 相关说明
     * @return
     * @Time 创建时间:2013-7-4上午11:36:08
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public List<ESBServiceInfo> getAllServiceInfo();

}
