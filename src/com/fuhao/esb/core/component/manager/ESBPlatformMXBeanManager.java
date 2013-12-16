package com.fuhao.esb.core.component.manager;
import com.fuhao.esb.core.component.ESBPlatformConstrants;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.ESBFileUtils;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.component.utils.SystemUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;



import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
/**
 * package name is  com.fuhao.esb.core.component.manager
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBPlatformMXBeanManager {

    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBPlatformMXBeanManager.class);

    private static final Map<ObjectName, Object> mbeanIndex = new HashMap<ObjectName, Object>(5);
    public static final String MXBEAN_NAME_HEAD = "SwordPlatformManager";
    private static JMXConnectorServer connectorServer;

    public static synchronized void startJMXServer(int port) throws ESBBaseCheckedException {
        if (connectorServer != null || System.getProperty(ESBPlatformConstrants.ESB_SYSTEM_PROPERTY_JMX_PORT) != null) {
            return;
        }

        log.info("开始启动JMX管理服务器");

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        JMXServiceURL jmxURL;
        String protocol = "rmi";

        try {
            jmxURL = new JMXServiceURL("service:jmx:" + protocol + ":///jndi/" + protocol + "://" + SystemUtils.getHostname() + ":" + port
                    + "/" + MXBEAN_NAME_HEAD);
        } catch (MalformedURLException ex) {
            throw new ESBBaseCheckedException("JM-00002:生成JMX服务器的URL路径时发生错误", ex);
        }

        try {
            // 注册RMI服务端口
            LocateRegistry.createRegistry(port);
            connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxURL, getEnv(), server);
            connectorServer.start();
        } catch (IOException ex) {
            connectorServer = null;
            throw new ESBBaseCheckedException("JM-00003:启动JMX服务器时发生错误", ex);
        }

        // 将JMX管理端口参数放入环境变量中
        System.setProperty(ESBPlatformConstrants.ESB_SYSTEM_PROPERTY_JMX_PORT, "" + port);

        log.info("JMX服务器启动成功:" + jmxURL);
    }

    private static Map<String, ?> getEnv() throws ESBBaseCheckedException {
        final Map<String, Object> env = new HashMap<String, Object>();
        File jmxUsers = new File(ESBFileUtils.getESBRootPath() + File.separator + "jmx-users");

        if (jmxUsers.exists() && jmxUsers.isFile()) {
            env.put("jmx.remote.x.password.file", jmxUsers.getPath());
        }

        return env;
    }

    public static void stopJMXServer() throws ESBBaseCheckedException {
        for (ObjectName bname : mbeanIndex.keySet()) {
            try {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(bname);
                log.info("成功注册MXBean组件", bname);
            } catch (Exception ex) {
                log.error(new ESBBaseCheckedException("JM-00007:注册MBean对象" + bname + "时发生错误", ex));
            }
        }
        mbeanIndex.clear();

        try {
            if (connectorServer != null) {
                connectorServer.stop();
            }
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("JM-00004:关闭JMX服务器时发生错误", ex);
        } finally {
            connectorServer = null;
        }
    }

    /**
     * 注册MBean对象
     *
     * @Description 相关说明
     * @Time 创建时间:2012-2-10下午2:56:30
     * @param mbean
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String registerMBean(String mbeanType, String mbeanName, Object mbean) throws ESBBaseCheckedException {
        if (mbeanName == null || "".equals(mbeanName)) {
            throw new ESBBaseCheckedException("JM-00005:MBean的名称不能为空");
        }

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName bname = null;
        String name = null;

        if (mbeanType == null || mbeanType.isEmpty()) {
            mbeanType = ESBServerContext.getServerName();
        }

        name = MXBEAN_NAME_HEAD + ":" + "type=" + mbeanType + "," + "name=" + mbeanName;

        try {
            bname = new ObjectName(name);
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("JM-00006:生成MBean对象名" + name + "时发生错误", ex);
        }

        try {
            server.registerMBean(mbean, bname);
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("JM-00007:注册MBean对象" + name + "时发生错误", ex);
        }

        // 索引MBean对象实例
        mbeanIndex.put(bname, mbean);

        log.info("成功注册MBean对象" + name);

        return name;
    }

    /**
     * 注消MBean对象
     *
     * @Description 相关说明
     * @param mbeanType
     * @param mbeanName
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2013-7-12下午4:22:59
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void unRegisterMBean(String mbeanType, String mbeanName) throws ESBBaseCheckedException {
        if (mbeanName == null || "".equals(mbeanName)) {
            throw new ESBBaseCheckedException("JM-00005:MBean的名称不能为空");
        }

        ObjectName bname = null;
        String name = null;

        if (mbeanType == null || mbeanType.isEmpty()) {
            mbeanType = ESBServerContext.getServerName();
        }

        name = MXBEAN_NAME_HEAD + ":" + "type=" + mbeanType + "," + "name=" + mbeanName;

        try {
            bname = new ObjectName(name);
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("JM-00006:生成MBean对象名" + name + "时发生错误", ex);
        }

        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(bname);
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("JM-00007:注册MBean对象" + name + "时发生错误", ex);
        }

        // 索引MBean对象实例
        mbeanIndex.remove(bname);

        log.info("成功注消MBean对象" + name);
    }
}
