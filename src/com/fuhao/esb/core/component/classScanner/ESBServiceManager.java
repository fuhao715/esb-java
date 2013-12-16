package com.fuhao.esb.core.component.classScanner;
import com.fuhao.esb.core.annotation.Service;
import com.fuhao.esb.core.annotation.ServiceContainer;
import com.fuhao.esb.core.component.AbsESBComponentManager;
import com.fuhao.esb.core.component.ESBComponentRef;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.classScanner.filter.ESBServiceCallLevelChecker;
import com.fuhao.esb.core.component.classScanner.filter.ESBServiceCallSecurityInfo;
import com.fuhao.esb.core.component.classScanner.filter.ESBServiceFilterManager;
import com.fuhao.esb.core.component.classScanner.filter.IESBServiceFilterManager;
import com.fuhao.esb.core.component.classScanner.scanner.IESBClassScannerEndListener;
import com.fuhao.esb.core.component.classScanner.scanner.IESBClassScannerListener;
import com.fuhao.esb.core.component.classloader.ESBClassLoader;
import com.fuhao.esb.core.component.classloader.ESBClassLoaderManager;
import com.fuhao.esb.core.component.manager.ESBPlatformMXBeanManager;
import com.fuhao.esb.core.component.utils.ClassUtils;
import com.fuhao.esb.core.component.utils.ESBFileUtils;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.component.service.ESBServiceContainer;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * package name is  com.fuhao.esb.core.component.classScanner
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class ESBServiceManager extends AbsESBComponentManager implements IESBServiceManager, IESBClassScannerListener,
        IESBClassScannerEndListener {


    private final ESBLogUtils log = ESBLogUtils.getLogger(ESBServiceManager.class);

    /**
     * 本地服务池访问控制锁
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 本地服务总版本
     */
    private volatile long version = 0L;

    /**
     * 服务池默认初始化大小
     */
    private final int DEFAULT_INITIAL_CAPACITY = 3000;

    /**
     * 临时服务池，用于暂存服务，待服务扫描完成后将其一次性加入到服务池中
     */
    private volatile Map<String, ESBServiceContainer> tempServicePool = null;

    /**
     * 服务池
     */
    private final Map<String, ESBServiceContainer> servicePool = new HashMap<String, ESBServiceContainer>(DEFAULT_INITIAL_CAPACITY,
            0.2f);

    /**
     * 临时服务生命周期管理器
     */
    private volatile Map<String, List<ESBServiceContainer>> tempServiceContainerManager = null;

    /**
     * 服务生命周期管理器
     */
    private final Map<String, List<ESBServiceContainer>> serviceContainerManager = new HashMap<String, List<ESBServiceContainer>>(
            DEFAULT_INITIAL_CAPACITY, 0.2f);

    /**
     * 服务拦截器管理器
     */
    private IESBServiceFilterManager filterManager;

    /**
     * 服务拦截器配置信息，只在平台启动的时候加载，平台启动完成后会清空此信息
     *
     */
    private List<Element> serviceFilterConfig;

    /**
     * 是否在启动的时候生成服务代码类
     */
    private final boolean startupGenerateServiceProxyClass;

    /**
     * 强制缓存服务执行结果
     */
    private boolean forcedCacheResult;

    /**
     * 服务调用缓存数量
     */
    private int valueCacheSize;

    /**
     * 服务调用最大嵌套调用层数
     */
    private int maxServiceCallLevel = 0;
    /**
     * 服务调用最大嵌套服务器调用层数
     */
    private int maxServerCallLevel = 20;

    /**
     * 单个交易最大异步服务并行度
     */
    private int maxASyncService = 5;

    @SuppressWarnings("unchecked")
    public ESBServiceManager(Element rootElement) throws ESBBaseCheckedException {
        // 服务配置参数
        Element element = rootElement.element("service");

        if (element == null) {
            throw new ESBBaseCheckedException("SM-00001:ESB.xml配置文件中没有service相关配置项，请检查配置文件");
        }

        // 服务代理器生成编译配置
        if (element != null && "startup".equals(element.attributeValue("generate-type"))) {
            log.info("服务代理器生成编译模式:启动时");
            this.startupGenerateServiceProxyClass = true;
        } else {
            log.info("服务代理器生成编译模式:首次调用时");
            this.startupGenerateServiceProxyClass = false;
        }

        // 服务调用参数序列化检查
        ESBServerContext.setCheckSerializable(Boolean.parseBoolean(element.attributeValue("checkSerializable")));
        if (ESBServerContext.isCheckSerializable()) {
            log.info("启动因远服务程路由不存在而转为本地调用的服务参数及返回值的序列化检查");
        }

        // 服务调用过程中生成异常对象但未抛出的检查
        ESBServerContext.setCheckNotThrowException(Boolean.parseBoolean(element.attributeValue("checkNotThrowException")));
        if (ESBServerContext.isCheckNotThrowException()) {
            log.info("启动服务调用过程中生成异常对象但未抛出的检查");
        }

        // 服务最大调用深度检查配置
        String maxServiceCallLevel = element.attributeValue("maxServiceCallLevel");
        if (maxServiceCallLevel != null && !"".equals(maxServiceCallLevel)) {
            this.maxServiceCallLevel = Integer.parseInt(maxServiceCallLevel);
        }

        // 服务器最大调用深度检查配置
        String maxServerCallLevel = element.attributeValue("maxServerCallLevel");
        if (maxServerCallLevel != null && !"".equals(maxServerCallLevel)) {
            this.maxServerCallLevel = Integer.parseInt(maxServerCallLevel);
        }
        // 获取服务拦截器的配置信息
        serviceFilterConfig = element.elements("filter");
    }

    @Override
    public Class<ServiceContainer> registerAnnotation(Map<String, String> parameter) {
        return ServiceContainer.class;
    }

    @Override
    public void process(Class<?> serviceClass) throws ESBBaseCheckedException {
        if (ClassUtils.isAbstract(serviceClass)) {
            return;
        }

        // 初始化临时服务池
        if (this.tempServicePool == null || this.tempServiceContainerManager == null) {
            synchronized (this) {
                if (this.tempServicePool == null || this.tempServiceContainerManager == null) {
                    this.tempServicePool = new ConcurrentHashMap<String, ESBServiceContainer>();
                    this.tempServiceContainerManager = new ConcurrentHashMap<String, List<ESBServiceContainer>>();
                }
            }
        }

        // 扫描并注册服务容器中的服务
        List<ESBServiceContainer> containers = scanServiceClass(serviceClass);

        if (this.startupGenerateServiceProxyClass) {
            try {
                for (ESBServiceContainer container : containers) {
                    // 处理 在启动的时候获取服务代理类对象，触发生成服务代理类
                    container.getServiceProxy();
                }
            } catch (ESBBaseCheckedException ex) {
                updateServiceState(containers, ESBServiceContainer.LifeCycle.ERROR);
                throw ex;
            }
        }
    }

    /**
     * 扫描服务容器中的服务
     *
     * @Description 扫描服务容器中的服务，并注册服务
     * @Time 创建时间:2011-8-31下午3:32:18
     * @param serviceClass
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private List<ESBServiceContainer> scanServiceClass(final Class<?> serviceClass) throws ESBBaseCheckedException {
        List<ESBServiceContainer> containers = new ArrayList<ESBServiceContainer>();
        Object service = null;
        try {
            service = serviceClass.newInstance();
        } catch (Throwable ex) {
            throw new ESBBaseCheckedException("SM-00004:生成服务类" + serviceClass.getName() + "的实例时发生错误", ex);
        }

        // 检查服务容器类是否已经不再被支持
        ServiceContainer classAnnotation = serviceClass.getAnnotation(ServiceContainer.class);
        if (!"".equals(classAnnotation.deprecated())) {
            log.warn("警告：", classAnnotation.deprecated());
        }

        log.debug("开始扫描服务容器类", serviceClass.getName());

        // 方法位置索引
        int methodIndex = 0;

        // 找出所有含有Service注解的方法，并生成callServer方法的代码
        Method[] methods = null;
        try {
            if (ClassUtils.isAbstract(serviceClass.getSuperclass())) {
                methods = serviceClass.getMethods();
            } else {
                methods = serviceClass.getDeclaredMethods();
            }
        } catch (Throwable ex) {
            throw new ESBBaseCheckedException("SM-00005:获取服务类" + serviceClass.getName() + "的方法列表时发生错误:" + ex.getMessage(), ex);
        }

        for (Method method : methods) {
            Service methodAnnotation = (Service) method.getAnnotation(Service.class);

            // 跳过非服务方法
            if (methodAnnotation == null) {
                continue;
            }

            if (!ClassUtils.isPublic(method.getModifiers())) {
                log.warn("SM-00026:类", method.getDeclaringClass().getName(), "的", method.getName(), "服务不是public方法");
                continue;
            }

            // 扫描服务方法信息
            final List<ESBServiceContainer> container = scanServiceClassMethod(service, method, methodAnnotation, methodIndex);
            // 忽略已经扫描过的相同的类型　
            if (container != null && !container.isEmpty()) {
                methodIndex += container.size();
                if (methodIndex >= 256) {
                    throw new ESBBaseCheckedException("SM-00006:服务类" + serviceClass.getName() + "中的服务个数超过256个!");
                }
                containers.addAll(container);
            }
        }

        // 处理声名了服务容器类，但是类中却没有服务方法的情况
        if (!containers.isEmpty()) {
            // 注册服务容器
            tempServiceContainerManager.put(serviceClass.getName(), containers);
        }

        return containers;
    }

    /**
     * 扫描服务方法信息
     *
     * @Description 扫描服务方法信息，并收集相关以便生成服务代理类
     * @Time 创建时间:2011-11-18下午4:59:36
     * @param service
     * @param method
     * @param methodAnnotation
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private List<ESBServiceContainer> scanServiceClassMethod(final Object service, final Method method, final Service methodAnnotation,
                                                               int methodIndex) throws ESBBaseCheckedException {
        final Class<?> serviceClass = service.getClass();
        final String classLoaderName = (serviceClass.getClassLoader() instanceof ESBClassLoader) ? ((ESBClassLoader) serviceClass
                .getClassLoader()).getName() : null;

        // 获取服务名列表
        final List<ESBServiceContainer> containers = new ArrayList<ESBServiceContainer>();
        String shortServiceNames = method.getName();
        if (!"".equals(methodAnnotation.serviceName().trim())) {
            // 如果在Annotation上配置过服务名则使用配置的名字
            shortServiceNames = methodAnnotation.serviceName();
        }

        // 处理服务信息
        for (String shortServiceName : shortServiceNames.split(",")) {
            final ESBServiceInfo serviceInfo = new ESBServiceInfo(shortServiceName, service, method, methodAnnotation,
                    (byte) methodIndex++, forcedCacheResult);
            final ESBServiceContainer container = new ESBServiceContainer(classLoaderName, serviceInfo, service, ESBServiceContainer.LifeCycle.STARTING,
                    valueCacheSize);

            // 检查服务名是否已经注册
            ESBServiceContainer serviceContainer = tempServicePool.get(serviceInfo.serviceName);
            if (serviceContainer != null && !serviceContainer.serviceInfo.equals(serviceInfo)) {
                log.warn("警告：类", serviceInfo.serviceClassName, "内的服务", serviceInfo.serviceName, "被", serviceContainer.service.getClass()
                        .getName(), "类的", serviceInfo.serviceName, "重新注册");
            } else {
                log.info("在", serviceInfo.serviceClassName, "类中发现", serviceInfo.serviceName, "服务，参数个数:",
                        serviceInfo.methodParameters.length, "，", (serviceInfo.autoShare ? "可自动共享" : "不可自动共享"));
            }

            // 仅记录新增加的服务的版本
            if (!tempServicePool.containsKey(serviceInfo.serviceName)) {
                synchronized (this) {
                    // 更新服务版本
                    this.version += System.identityHashCode(serviceInfo.serviceName);
                }
            }

            // 注册服务
            tempServicePool.put(serviceInfo.serviceName, container);

            // 注册默认版本服务
            if (!"".equals(serviceInfo.version)) {
                ESBServiceContainer otherContainer = tempServicePool.get(serviceInfo.serviceShortName);
                if (otherContainer != null && otherContainer.serviceInfo.serviceName.equals(otherContainer.serviceInfo.serviceShortName)) {
                    // 已经索引过短服务名，则已注册的服务的长名和短相同，则不再进行处理
                } else if (otherContainer == null) {
                    tempServicePool.put(serviceInfo.serviceShortName, container);
                } else if (serviceInfo.version.compareToIgnoreCase(otherContainer.serviceInfo.version) > 0) {
                    tempServicePool.put(serviceInfo.serviceShortName, container);
                }
            }

            // 修改服务容器状态为运行
            container.serviceState = ESBServiceContainer.LifeCycle.RUNNING;

            containers.add(container);
        }

        return containers;
    }

    // -----------------------------------------------以下为服务生命周期管理方法-----------------------------------------------------------
    @Override
    public synchronized void start(Map<String, String> parameter) throws ESBBaseCheckedException {
        this.state = ComponentState.STARTING;

        ESBComponentRef.serviceManager = this;

        // 开发模式下加载服务调用安全性检查信息
        if (ESBServerContext.isDevelopMode()) {
            loadServiceCallCheckInfo();
        }

        ESBPlatformMXBeanManager.registerMBean(null, "ServiceManager", this);

        this.state = ComponentState.RUNNING;
    }

    /**
     * 加载服务调用检查信息
     */
    private void loadServiceCallCheckInfo() throws ESBBaseCheckedException {
        SAXReader reader = new SAXReader();
        Document doc = null;
        Element rootElement = null;

        try {
            InputStream is = ESBFileUtils.getInputStream("/ESB.xml", null);
            if (is == null) {
                return;
            }
            doc = reader.read(is);
            rootElement = doc.getRootElement();
        } catch (DocumentException ex) {
            throw new ESBBaseCheckedException("SM-00008:读取ESB.xml时发生错误", ex);
        }

        @SuppressWarnings("unchecked")
        List<Element> checkInfo = rootElement.elements();
        for (Element info : checkInfo) {
            String sourceClassAndName = info.attributeValue("source");
            String destinationClassAndServiceName = info.attributeValue("perimit-destination");
            ESBServerContext.getServicecallcheckinfo().add(
                    new ESBServiceCallSecurityInfo(Pattern.compile(sourceClassAndName), Pattern.compile(destinationClassAndServiceName)));
        }
    }

    @Override
    public void scanClassEnd() throws ESBBaseCheckedException {
        if (tempServicePool == null || tempServicePool.isEmpty()) {
            return;
        }

        try {
            lock.writeLock().lock();
            servicePool.putAll(tempServicePool);
            serviceContainerManager.putAll(tempServiceContainerManager);
        } finally {
            tempServicePool = null;
            tempServiceContainerManager = null;
            unRegisterService();
            lock.writeLock().unlock();
        }
    }

    @Override
    public void platformStarted() throws ESBBaseCheckedException {
        filterManager = new ESBServiceFilterManager(serviceFilterConfig);
        this.serviceFilterConfig.clear();
        this.serviceFilterConfig = null;

        // 注册服务调用次数拦截器，用于发现服务调用过程中的死循环
        if (maxServiceCallLevel > 0) {
            log.info("启动服务调用最大深度检查器");
            ESBServiceCallLevelChecker callCountFilter = new ESBServiceCallLevelChecker();
            this.getServiceFilterManager().registerBeforeFilter(callCountFilter, 0);
            this.getServiceFilterManager().registerAfterFilter(callCountFilter, 0);
            this.getServiceFilterManager().registerExceptionFilter(callCountFilter, 0);
        }
    }

    @Override
    public void stop() throws ESBBaseCheckedException {
        this.state = ComponentState.STOP;
    }

    @Override
    public void reload() throws ESBBaseCheckedException {
    }

    @Override
    public void startService(String serviceName) throws ESBBaseCheckedException {
        ESBServiceContainer serviceContainer;
        serviceContainer = findServiceContainerByServieName(serviceName);

        if (serviceContainer == null) {
            throw new ESBBaseCheckedException("SM-00009:没有注册" + serviceName + "服务");
        }

        // TODO:可能涉及到热部署，待完善

        serviceContainer.serviceState = ESBServiceContainer.LifeCycle.RUNNING;
    }

    @Override
    public void stopService(String serviceName) throws ESBBaseCheckedException {
        ESBServiceContainer serviceContainer = findServiceContainerByServieName(serviceName);

        if (serviceContainer == null) {
            throw new ESBBaseCheckedException("SM-00009:没有注册" + serviceName + "服务");
        }

        // TODO:可能涉及到热部署，待完善

        serviceContainer.serviceState = ESBServiceContainer.LifeCycle.STOP;
    }

    @Override
    public void sleepService(String serviceName) throws ESBBaseCheckedException {
        ESBServiceContainer serviceContainer = findServiceContainerByServieName(serviceName);

        if (serviceContainer == null) {
            throw new ESBBaseCheckedException("SM-00009:没有注册" + serviceName + "服务");
        }

        // TODO:可能涉及到热部署，待完善

        serviceContainer.serviceState = ESBServiceContainer.LifeCycle.SLEEP;
    }

    public void weakupService(String serviceName) throws ESBBaseCheckedException {
        ESBServiceContainer serviceContainer = findServiceContainerByServieName(serviceName);

        if (serviceContainer == null) {
            throw new ESBBaseCheckedException("SM-00009:没有注册" + serviceName + "服务");
        }

        // TODO:可能涉及到热部署，待完善

        serviceContainer.serviceState = ESBServiceContainer.LifeCycle.RUNNING;
    }

    // -----------------------------------------------以下为服务方法管理方法-----------------------------------------------------------

    private void updateServiceState(List<ESBServiceContainer> containers, byte state) {
        // 更新并清理服务容器类
        for (ESBServiceContainer container : containers) {
            // 修改服务容器状态为运行
            container.serviceState = state;
        }
    }

    @Override
    public ESBServiceContainer findServiceContainerByServieName(String serviceName) throws ESBBaseCheckedException {
        ESBServiceContainer serviceContainer = null;

        try {
            lock.readLock().lock();
            serviceContainer = servicePool.get(serviceName);
        } finally {
            lock.readLock().unlock();
        }

        if (serviceContainer == null) {
            return null;
        }

        if (!this.isRunning()) {
            throw new ESBBaseCheckedException("SM-00010:服务已全部停止，无法调用");
        }

        if (ESBServiceContainer.LifeCycle.RUNNING == serviceContainer.serviceState) {
            // 服务处于运行状态，跳过后面的状态检查
        } else if (ESBServiceContainer.LifeCycle.STOP == serviceContainer.serviceState) {
            throw new ESBBaseCheckedException("SM-00011:服务" + serviceName + "已停止，无法调用");
        } else if (ESBServiceContainer.LifeCycle.ERROR == serviceContainer.serviceState) {
            throw new ESBBaseCheckedException("SM-00012:服务" + serviceName + "状态异常，无法调用，请确认服务已正常启动");
        } else if (ESBServiceContainer.LifeCycle.STARTING == serviceContainer.serviceState) {
            throw new ESBBaseCheckedException("SM-00013:服务" + serviceName + "正在启动，请稍后重试");
        }

        return serviceContainer;
    }

    @Override
    public List<ESBServiceContainer> findServiceContainersByContainerName(String serviceContainerName) throws ESBBaseCheckedException {
        List<ESBServiceContainer> serviceContainers = serviceContainerManager.get(serviceContainerName);

        if (serviceContainers == null || serviceContainers.isEmpty()) {
            throw new ESBBaseCheckedException("SM-00014:没有ID为" + serviceContainerName + "的服务容器");
        }

        return serviceContainers;
    }

    @Override
    public String findServiceClass(String serviceName) throws ESBBaseCheckedException {
        ESBServiceContainer serviceContainer = findServiceContainerByServieName(serviceName);
        if (serviceContainer != null) {
            return serviceContainer.service.getClass().getName();
        }
        return null;
    }


    @Override
    @Deprecated
    public Collection<ESBServiceContainer> getAllService() {
        try {
            lock.readLock().lock();
            List<ESBServiceContainer> allService = new ArrayList<ESBServiceContainer>(servicePool.size());
            allService.addAll(servicePool.values());
            return allService;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<ESBServiceInfo> getAllServiceInfo() {
        try {
            lock.readLock().lock();
            List<ESBServiceInfo> allService = new ArrayList<ESBServiceInfo>(servicePool.size());
            for (ESBServiceContainer container : servicePool.values()) {
                allService.add(container.serviceInfo);
            }
            return allService;
        } finally {
            lock.readLock().unlock();
        }
    }


    public int getMaxASyncService() {
        return maxASyncService;
    }

    public void setMaxASyncService(int maxASyncService) {
        this.maxASyncService = maxASyncService;
    }

    @Override
    public int getServiceCount() {
        return servicePool.size();
    }

    @Override
    public int getMaxServiceCallLevel() {
        return this.maxServiceCallLevel;
    }

    @Override
    public int getMaxServerCallLevel() {
        return this.maxServerCallLevel;
    }

    @Override
    public void setMaxServiceCallLevel(int maxServiceCallLevel) {
        this.maxServiceCallLevel = maxServiceCallLevel;
    }

    @Override
    public IESBServiceFilterManager getServiceFilterManager() {
        return this.filterManager;
    }

    @Override
    public long getVersion() {
        return version;
    }

    /**
     * 注销服务
     */
    private void unRegisterService() {
        List<String> serviceNames = new LinkedList<String>();
        for (ESBServiceContainer container : servicePool.values()) {
            if (ESBClassLoaderManager.isUnRegisterClassLoader(container.service.getClass().getClassLoader())) {
                serviceNames.add(container.serviceInfo.serviceName);
            }
        }
        if (!serviceNames.isEmpty()) {
            for (String serviceName : serviceNames) {
                servicePool.remove(serviceName);
                log.warn("服务" + serviceName + "被成功注销");
            }
        }
    }
}
