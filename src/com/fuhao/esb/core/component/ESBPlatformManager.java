package com.fuhao.esb.core.component;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.fuhao.esb.core.component.classScanner.ESBServiceManager;
import com.fuhao.esb.core.component.classScanner.scanner.ESBClassScanner;
import com.fuhao.esb.core.component.classScanner.scanner.IESBClassScannerListener;
import com.fuhao.esb.core.component.classloader.ESBClassLoaderManager;
import com.fuhao.esb.core.component.globalvariables.ESBGlobalVariables;
import com.fuhao.esb.core.component.threadPool.ESBThreadPoolManager;
import com.fuhao.esb.core.component.utils.*;
import com.fuhao.esb.core.component.service.ESBServiceUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fuhao.esb.core.component.log.ESBLogManager;
import com.fuhao.esb.core.component.manager.ESBPlatformMXBeanManager;
import com.fuhao.esb.core.component.manager.ServerContextVisitor;
import com.fuhao.esb.core.component.AbsESBComponentManager.ComponentState;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class ESBPlatformManager {

    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBPlatformManager.class);

    /**
     * 启动ESB平台
     *
     */
    public static void startPlatform() {
        long time = System.currentTimeMillis();

        // 防止平台重复启动
        if (ESBServerContext.getServerState() != ComponentState.STOP) {
            return;
        }

        ESBServerContext.getSession().clear();
        ESBServerContext.setServerState(ComponentState.STARTING);

        SAXReader reader = new SAXReader();
        Document doc = null;
        Element rootElement = null;

        try {
            doc = reader.read(new File(ESBFileUtils.getESBRootPath() + "/ESB.xml"));
            rootElement = doc.getRootElement();
        } catch (Exception ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("系统启动失败", ex);
            log.error(e);
            ESBServerContext.setServerState(ComponentState.ERROR);
            return;
        }

        log.info("开始获取系统相关信息");

        try {
            // 获取服务器基础信息
            loadServerInfo(rootElement);

            // 初始化系统的基础设置
            initBaseConfig(rootElement);

            // 检查环境信息，查看环境配置是否符合要求
            checkEnvironment();

            // 初始化基础组件：目前包含数据元管理器和服务管理器
            initBaseComponent(rootElement);

            // 开始进行类扫描
            scanClasses(rootElement);

            // 加载所有组件
            loadComponents(rootElement);

            // 增加停止监听
            addShutdownHook();
        } catch (ESBBaseCheckedException ex) {
            log.error("系统启动失败..................");
            log.error(ex);
            ESBServerContext.setServerState(ComponentState.ERROR);
            return;
        }

        // JVM和Thread事件处理句柄
        setEventHandler();

        ESBServerContext.setServerState(ComponentState.RUNNING);

        String msg = "系统启动已完成，共耗时" + ((System.currentTimeMillis() - time) / 1000.0) + "秒，共加载服务" + ESBServiceUtils.getServiceCount() + "个";
        log.info(msg);
    }

    /**
     * 获取服务器的基础配置信息
     *
     * @param element
     */
    private static void loadServerInfo(Element element) throws ESBBaseCheckedException {
        String serverName;
        String serverType;
        String nodeName;
        String memo;

        // 启用CPU测量和线程争用监视
        ManagementFactory.getThreadMXBean().setThreadCpuTimeEnabled(true);
        ManagementFactory.getThreadMXBean().setThreadContentionMonitoringEnabled(true);

        // 获取当前JVM进程编号和主机名
        String pid = SystemUtils.getPid();

        if (pid != null) {
            ESBServerContext.setPid(Integer.parseInt(pid));
        }
        ESBServerContext.setHostname(SystemUtils.getHostname());

        // Weblogic环境上使用config.xml中配置的应用服务器名做为服务器名
        if ((serverName = System.getProperty("weblogic.Name")) == null) {
            // 当为web服务器时，当前域如果不是主域（/），则使用子目录名作为服务器名
            if ((serverName = System.getProperty("ESB.server.name")) == null) {
                // 使用ESB.xml中配置的服务器名
                serverName = element.element("server-info").element("server-name").attributeValue("name");
            }
        }
        ESBServerContext.setServerName(serverName);
        ESBServerContext.getSession().setServers(new String[] { serverName });

        serverType = element.element("server-info").element("server-name").attributeValue("type");
        ESBServerContext.setServerType(serverType);

        nodeName = element.element("server-info").element("server-name").attributeValue("nodeName");
        ESBServerContext.setNodeName(nodeName);

        memo = element.element("server-info").element("server-name").attributeValue("memo");
        ESBServerContext.setServerMemo(memo);

        // 获取机架ID
        Element te = element.element("server-info").element("rack-id");
        if (te != null) {
            ESBServerContext.setRackID(te.attributeValue("id"));
        } else {
            ESBServerContext.setRackID(SystemUtils.getHostname());
        }

        // 获取服务器运行模式
        String model = element.element("server-info").element("running-model").attributeValue("model");
        if ("product".equalsIgnoreCase(model)) {
            ESBServerContext.setServerRunningMode(ServerRunningMode.PRODUCT_MODE);
        } else if ("develop".equalsIgnoreCase(model)) {
            ESBServerContext.setServerRunningMode(ServerRunningMode.DEVELOP_MODE);
        } else if ("test".equalsIgnoreCase(model)) {
            ESBServerContext.setServerRunningMode(ServerRunningMode.TEST_MODE);
        } else {
            throw new ESBBaseCheckedException("PF-00002:ESB.xml中running-model结点的配置信息错误");
        }

        // 是否开启EPoll模式
        final SelectorProvider provider = SelectorProvider.provider();
        if (provider.getClass().getName().equals("sun.nio.ch.EPollSelectorProvider")) {
            ESBServerContext.enableUseEPoll();
        }

        // 启动过程中发生错误是是否立即停止
        final Element fs = element.element("server-info").element("failStop");
        if (fs != null) {
            ESBServerContext.setFailStop(Boolean.parseBoolean(fs.getText()));
        }
    }

    /**
     * 初始化系统的基础设置
     *
     * @param element
     * @throws ESBBaseCheckedException
     */
    private static void initBaseConfig(Element element) throws ESBBaseCheckedException {
        // 获取当前系统时间，记录服务器启动时间
        ESBServerContext.setServerStartTime(ESBDateUtils.toDateTimeStr(0, Calendar.getInstance()));

        // 根据配置重置系统的默认字符集
        if (element.element("server-info").element("charset") != null) {
            String defaultCharset = element.element("server-info").element("charset").attributeValue("defaultCharset");
            if (defaultCharset != null && !"".equals(defaultCharset)) {
                if (!Charset.isSupported(defaultCharset)) {
                    throw new ESBBaseCheckedException("不支持字符集" + defaultCharset);
                }

                try {
                    Field f = Charset.class.getDeclaredField("defaultCharset");
                    f.setAccessible(true);
                    f.set(null, Charset.forName(defaultCharset));
                    f.setAccessible(false);
                    System.setProperty("file.encoding", defaultCharset);
                } catch (Exception ex) {
                    log.error("PF-00013:重置系统默认字符集时发生错误", ex);
                }
            }
        }

        // 设置ZIP压缩文件名默认字符集
        if (element.element("server-info").element("zipAltEncoding") != null) {
            String pName = element.element("server-info").element("zipAltEncoding").attributeValue("name");
            String value = element.element("server-info").element("zipAltEncoding").attributeValue("charset");
            System.setProperty(pName, value);
        } else {
            System.setProperty("sun.zip.altEncoding", "GBK");
        }
    }

    /**
     * 初始化基础组件：只包含数据元管理组件和服务管理组件
     *
     * @throws ESBBaseCheckedException
     */
    private static void initBaseComponent(Element element) throws ESBBaseCheckedException {
        // 注册平台根类加载器
        ESBClassLoaderManager.createClassLoader(ESBClassLoaderManager.PLATFORM_ROOT_CLASSLOADER,
                ESBPlatformManager.class.getClassLoader());

        // 启动JMX管理服务器
        Element te = element.element("server-info").element("jmx-server");
        if (te != null) {
            try {
                ESBPlatformMXBeanManager.startJMXServer(Integer.parseInt(te.attributeValue("port")));
            } catch (NumberFormatException ex) {
                log.error("PF-00003:启动JMX服务器失败", new ESBBaseCheckedException("JM-00001:JMX服务配置项的port属性不是数字", ex));
                failStop(null);
            } catch (ESBBaseCheckedException ex) {
                log.error("PF-00003:启动JMX服务器失败", ex);
                failStop(null);
            }
        }

        // 注册服务器全局变量MXBean
        try {
            ESBPlatformMXBeanManager.registerMBean(null, "GlobalVariables", ESBGlobalVariables.inst());
        } catch (ESBBaseCheckedException ex) {
            log.error("PF-000016:注册服务器全局变量MBean时发生错误", ex);
            failStop(null);
        }

        // 注册服务器上下文访问MXBean
        try {
            ESBPlatformMXBeanManager.registerMBean(null, "ServerContext", new ServerContextVisitor());
        } catch (ESBBaseCheckedException ex) {
            log.error("PF-00004:注册服务器上下文访问MBean时发生错误", ex);
            failStop(null);
        }

        log.info("开始初始化日志组件");
        {
            ESBLogManager manager = new ESBLogManager();
            manager.start();
            ESBComponentRef.logManager = manager;
        }

        log.info("开始启动本地线程池管理器");
        {
            registerComponentManager("ESBThreadPoolManager", new ESBThreadPoolManager()).start(null);
        }

        // 注册服务管理器
        try {
            log.info("开始初始化本地服务管理组件");
            ESBServiceManager manager = new ESBServiceManager(element);
            registerComponentManager("ServiceManagerComponent", manager);
            ESBClassScanner.registerListener(manager, null);
        } catch (ESBBaseCheckedException ex) {
            log.error("PF-00006:注册服务管理组件时发生错误", ex);
            failStop("true");
        }
    }

    /**
     * 类扫描
     *
     * @param element
     * @throws ESBBaseCheckedException
     */
    @SuppressWarnings("unchecked")
    private static void scanClasses(Element element) throws ESBBaseCheckedException {
        log.info("开始启动类扫描器组件");

        element = (Element) element.selectSingleNode("platform-event/class-scanner");

        final String extensionsLibs = element.attributeValue("extensionsLibs");
        final String inclusionJars = element.attributeValue("inclusionJars");
        final String inclusionClasses = element.attributeValue("inclusionClasses");

        // 注册JMX管理器
        ESBPlatformMXBeanManager.registerMBean(null, "ClassManager", new ESBClassScanner());

        List<Element> scanners = element.elements();
        for (Element scanner : scanners) {
            String scannerClass = scanner.attributeValue("scanner");
            IESBClassScannerListener listener = null;
            try {
                listener = (IESBClassScannerListener) Class.forName(scannerClass).newInstance();
            } catch (Exception ex) {
                throw new ESBBaseCheckedException("PF-00007:生成类扫描事件监听器" + scannerClass + "时发生错误", ex);
            }

            // 注册类扫描监听器
            ESBClassScanner.registerListener(listener, ObjectUtils.getParamter(scanner));
        }

        // 扫描相关Jar包
        ESBClassScanner.scan(extensionsLibs, inclusionJars, inclusionClasses);

        // 类扫描处理结束
        ESBClassScanner.scanEnd();
    }

    /**
     * 加载所有组件
     *
     * @param element
     */
    @SuppressWarnings("unchecked")
    private static void loadComponents(Element element) throws ESBBaseCheckedException {
        Map<String, Map<String, String>> componentConfigParameters = new HashMap<String, Map<String, String>>();
        List<Element> components = null;

        // 组件配置信息
        components = element.element("components").elements();

        log.info("开始初始化已经注册的" + components.size() + "个组件");

        // 从配置文件加载组件
        for (Element component : components) {
            String name = component.attributeValue("name");
            String managerClass = component.attributeValue("component");
            AbsESBComponentManager manager = null;

            // 读取结点的属性信息
            Map<String, String> parameter = ObjectUtils.getParamter(component);

            // 生成配置信息对象
            componentConfigParameters.put(name, parameter);

            // 创建组件管理器实例
            try {
                manager = (AbsESBComponentManager) Class.forName(managerClass).newInstance();
            } catch (Exception ex) {
                log.error(new ESBBaseCheckedException("PF-00008:创建组件" + name + "的管理器" + managerClass + "实例时发生错误", ex));
                failStop(component.attributeValue(ESBPlatformConstrants.FAIL_STOP));
                continue;
            }

            // 将组件管理器加入组件管理器索引中
            registerComponentManager(name, manager);

            // 设置组件说明信息
            manager.setMemo(parameter.get("memo"));
        }

        // 调用已经注册的组件管理器的start方法启动组件
        for (String name : ESBComponentRef.allComponentName) {
            log.info("开始初始化" + name + "组件");

            // 启动组件
            try {
                ESBComponentRef.componentManagerIndexer.get(name).start(componentConfigParameters.get(name));
            } catch (Exception ex) {
                try {
                    if (!(ex instanceof ESBBaseCheckedException)) {
                        ESBBaseCheckedException e = new ESBBaseCheckedException(ex.getMessage(), ex.getCause());
                        e.setStackTrace(ex.getStackTrace());
                        ex = e;
                    }
                    log.error("PF-00014:组件" + name + "启动时发生错误:", ex);
                    failStop(componentConfigParameters.get(name).get(ESBPlatformConstrants.FAIL_STOP));
                } finally {
                    ESBComponentRef.componentManagerIndexer.remove(name);
                }
            }
        }
    }



    /**
     * 将组件管理器加入组件管理器索引中
     *
     * @Description 相关说明
     * @Time 创建时间:2011-8-26下午3:33:32
     * @param name
     * @param manager
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static AbsESBComponentManager registerComponentManager(String name, AbsESBComponentManager manager) {
        manager.setName(name);
        ESBComponentRef.allComponentName.add(name);
        ESBComponentRef.componentManagerIndexer.put(name, manager);
        return manager;
    }

    /**
     * JVM停止处理
     */
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                stopPlatform();
            }
        }));
    }

    /**
     * 停止ESB平台
     */
    public static void stopPlatform() {
        if (ESBServerContext.getServerState() == ComponentState.STOP) {
            return;
        }

        SAXReader reader = new SAXReader();
        Document doc = null;
        Element rootElement = null;

        ESBServerContext.setServerState(ComponentState.STOP);

        try {
            doc = reader.read(ESBFileUtils.getInputStream("/ESB.xml", null));
            rootElement = doc.getRootElement();
        } catch (Exception ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("PF-00001:读取ESB.xml时发生错误, 停止平台时发生错误", ex);
            log.error(e);
            return;
        }

        log.info("开始关闭系统(" + ESBDateUtils.toDateStrByFormatIndex(Calendar.getInstance(), 0) + ")");

        try {
            log.info("开始关闭JMX服务器");
            ESBPlatformMXBeanManager.stopJMXServer();
        } catch (ESBBaseCheckedException ex) {
            log.error(ex);
        }


        log.info("开始停止ESB组件");
        for (int i = ESBComponentRef.allComponentName.size() - 1; i >= 0; i--) {
            String name = ESBComponentRef.allComponentName.get(i);
            try {
                final AbsESBComponentManager manager = ESBComponentRef.componentManagerIndexer.get(name);
                if (manager == null) {
                    continue;
                }
                log.info("开始停止" + name + "组件");
                manager.stop();
            } catch (ESBBaseCheckedException ex) {
                log.error("PF-00011:停止" + name + "组件时发生错误", ex);
            }
        }
        log.info("系统已经停止，可以关闭服务器");
    }


    /**
     * JVM和Thread事件处理句柄
     *
     * @Description 相关说明
     * @Time 创建时间:2011-11-30上午9:29:31
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static void setEventHandler() {
        // 监听线程异常退出事件
        Thread[] threads = new Thread[1024];
        for (int i = Thread.enumerate(threads) - 1; i >= 0; i--) {
            if (threads[i].getUncaughtExceptionHandler() == null) {
                threads[i].setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        log.error("PF-00012:线程" + t.getName() + "异常退出", e);
                    }
                });
            }
        }
    }

    /**
     * 检查系统环境
     */
    private static void checkEnvironment() throws ESBBaseCheckedException {
        StringBuffer serverInfo = new StringBuffer("开始启动ESB平台(").append(ESBDateUtils.toDateStrByFormatIndex(Calendar.getInstance(), 0))
                .append(")，服务器信息:\r\n\r\n");

        serverInfo.append("\t服务器-服务实例名: ").append(ESBServerContext.getServerName()).append("\r\n");
        serverInfo.append("\t服务器-服务结点名: ").append(ESBServerContext.getNodeName()).append("\r\n");
        serverInfo.append("\t服务器-机架ID: " + ESBServerContext.getRackID()).append("\r\n");
        serverInfo.append("\t服务器-主机名/用户名/IP: " + ESBServerContext.getHostname()).append(" / ").append(SystemUtils.getUserName())
                .append(" / ").append(SystemUtils.getLocalIP()).append("\r\n");
        serverInfo.append("\t服务器-JVM进程编号: " + ESBServerContext.getPid()).append("\r\n");

        serverInfo.append("\t服务器-运行模式: ");
        switch (ESBServerContext.getServerRunningMode()) {
            case ServerRunningMode.PRODUCT_MODE:
                serverInfo.append("Product Mode");
                break;
            case ServerRunningMode.DEVELOP_MODE:
                serverInfo.append("Develop Mode");
                break;
            case ServerRunningMode.TEST_MODE:
                serverInfo.append("Test Mode");
                break;
            default:
                serverInfo.append("Other Mode");
        }
        serverInfo.append("\r\n");

        serverInfo.append("\tJava虚拟机-操作系统: ").append(SystemUtils.getOSName()).append(" ").append(SystemUtils.getOSVersion()).append("(")
                .append(SystemUtils.getOSPatch()).append("),").append(SystemUtils.getOSArch()).append("\r\n");
        serverInfo.append("\tJava虚拟机-可用处理器数目: ").append(SystemUtils.getAvailableProcessors()).append("\r\n");
        serverInfo.append("\tJava虚拟机-可使用的最大内存容量(M): ").append((SystemUtils.getMaxMemory() / 1024 / 1024)).append("\r\n");
        serverInfo.append("\tJava虚拟机-可使用的最大DirectMemory容量(M): ").append((SystemUtils.getMaxDirectMemory() / 1024 / 1024)).append("\r\n");
        serverInfo.append("\tJava虚拟机-是否启用EPoll模式：").append(ESBServerContext.isUseEPoll()).append("\r\n");
        serverInfo.append("\tJava虚拟机-位置: ").append(SystemUtils.getJavaHome()).append("\r\n");
        serverInfo.append("\tJava虚拟机-VM启动参数: ").append(SystemUtils.getInputArguments()).append("\r\n");
        serverInfo.append("\tJava虚拟机-信息: ").append(SystemUtils.getJVMVendor()).append(",");
        serverInfo.append(SystemUtils.getJavaRuntimeVersion()).append(",");
        serverInfo.append(SystemUtils.getSunArchDataModel()).append("bit,");
        serverInfo.append(SystemUtils.getCompilerInfo()).append(",");
        serverInfo.append(SystemUtils.getJVMMode()).append("\r\n");
        if (ManagementFactory.getRuntimeMXBean().isBootClassPathSupported()) {
            serverInfo.append("\tJava虚拟机-BootClassPath: ").append(SystemUtils.getClassPathInfo()).append("\r\n");
        }
        serverInfo.append("\tJava虚拟机-ClassPath: ").append(SystemUtils.getClassPathInfo()).append("\r\n");
        serverInfo.append("\tJava虚拟机-动态链接库搜索路径: ").append(SystemUtils.getLibraryScanPath()).append("\r\n");
        serverInfo.append("\tJava虚拟机-默认临时文件路径: ").append(ESBFileUtils.getTempFileDir()).append("\r\n");
        serverInfo.append("\tJava虚拟机-默认字符集: ").append(SystemUtils.getDefaultCharsetName()).append("\r\n");
        final TimeZone defaultTimeZone = TimeZone.getDefault();
        serverInfo.append("\tJava虚拟机-默认时区：").append(defaultTimeZone.getDisplayName()).append("(").append(defaultTimeZone.getID())
                .append(")").append("\r\n");
        serverInfo.append("\tJava虚拟机-默认地区：").append(Locale.getDefault()).append("\r\n");
        serverInfo.append("\tJava虚拟机-Zip Alt Encoding: ").append(SystemUtils.getZipAltEncoding()).append("\r\n");
        serverInfo.append("\tJava虚拟机-默认显示语言: ").append(SystemUtils.getDefaultLanguage()).append("\r\n");
        serverInfo.append("\tJava虚拟机-用户当前目录: ").append(ESBFileUtils.getUserDir()).append("\r\n");
        serverInfo.append("\tJava虚拟机-磁盘空闲空间情况(MB): ").append(SystemUtils.getFreeSpace('m')).append("\r\n");

        log.info(serverInfo);

        // 环境健康检查
        if (ESBServerContext.isDevelopMode()) {
            // 进行JDK版本检查
            String javaVersion = SystemUtils.getSpecificationVersion();
            if (Float.valueOf(javaVersion) < 1.6) {
                throw new ESBBaseCheckedException("当前JDK版本是" + javaVersion + "，低于1.6，请更换为1.6或更高版本的JDK");
            }
        }
    }

    /**
     * 是否失败即停
     */
    private static void failStop(String failStop) {
        if (failStop != null) {
            if (!Boolean.parseBoolean(failStop)) {
                return;
            }
        } else if (!ESBServerContext.isFailStop()) {
            return;
        }
        log.error("服务器启动过程中发生错误，强制停止系统！");
        System.exit(-1);
    }



    public static void main(String[] args) {
        if (args != null && args.length >= 1) {
            try {
                ESBFileUtils.setESBRootPath(args[0]);
            } catch (ESBBaseCheckedException ex) {
                log.error(ex);
                return;
            }
        }

        // 启动平台
        startPlatform();
    }
}
