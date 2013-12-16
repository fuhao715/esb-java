package com.fuhao.esb.core.component.classScanner.scanner;
import com.fuhao.esb.core.component.ESBComponentRef;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.classloader.ESBClassLoader;
import com.fuhao.esb.core.component.classloader.ESBClassLoaderManager;
import com.fuhao.esb.core.component.classloader.ResourceInfo;
import com.fuhao.esb.core.component.utils.ClassUtils;
import com.fuhao.esb.core.component.utils.ESBFileUtils;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.component.utils.SystemUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
/**
 * package name is  com.fuhao.esb.core.component.classScanner.scanner
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public class ESBClassScanner implements IESBClassScannerMXBean {

    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBClassScanner.class);

    /**
     * 类扫描处理监听器管理器
     */
    private static final Map<Class<? extends Annotation>, IESBClassScannerListener> listeners = new HashMap<Class<? extends Annotation>, IESBClassScannerListener>();

    /**
     * 类扫描处理结束监听器管理器
     */
    private static final List<IESBClassScannerEndListener> endListeners = new LinkedList<IESBClassScannerEndListener>();

    /**
     * 类扫描信息，用于记录扫描信息及相关过滤条件
     */
    public static final ClassScanInfo scanInfo = new ClassScanInfo();

    /**
     * 类路由和Jar包列表
     */
    private static final Map<String, String> classPaths = new HashMap<String, String>(5);

    /**
     * 扫描列表
     */
    private static List<String> scanClassAndJar = new LinkedList<String>();

    /**
     * 注册类加载监听器，一种类型的注解只能被一个监听器处理
     *
     * @param listener
     * @param parameter
     * @throws ESBBaseCheckedException
     */
    public static void registerListener(Object listener, Map<String, String> parameter) throws ESBBaseCheckedException {
        if (!(listener instanceof IESBClassScannerListener || listener instanceof IESBClassScannerEndListener)) {
            throw new ESBBaseCheckedException("CS-00001:类" + listener.getClass().getName()
                    + "未实现IESBClassScannerListener或IESBClassScannerEndListener接口");
        }

        if (listener instanceof IESBClassScannerListener) {
            Class<? extends Annotation> annotation = ((IESBClassScannerListener) listener).registerAnnotation(parameter);

            if (annotation == null) {
                return;
            }

            IESBClassScannerListener csl = listeners.get(annotation);
            if (csl != null) {
                throw new ESBBaseCheckedException("CS-00002:注解" + annotation.getName() + "处理器已被" + csl.getClass().getName() + "注册，不能再被"
                        + listener.getClass().getName() + "注册");
            }

            listeners.put(annotation, (IESBClassScannerListener) listener);
        }

        if (listener instanceof IESBClassScannerEndListener) {
            endListeners.add((IESBClassScannerEndListener) listener);
        }

    }

    /**
     * 获取扫描类路径和Jar文件
     *
     * 搜索顺序:<br>
     *              1.ESB.xml所在目录<br>
     *              2.环境变量classpath的相关目录或Jar包<br>
     *              3.服务器的lib目录<br>
     *              4.热部署目录：环境变量ESB.hotswap.lib或lib/hslib目录
     */
    private static LinkedList<String> getClassFolderAndJars(final String extensionsLibs)
            throws ESBBaseCheckedException {
        Map<String, String> map = new HashMap<String, String>();
        LinkedList<String> classAndJar = new LinkedList<String>();
        final String pathSeparator = System.getProperty("path.separator");
        String[] classpath = getClasspath(pathSeparator);
        String appServerClassesDir = ESBFileUtils.getESBRootPath();
        String appServerLibDir = appServerClassesDir.substring(0, appServerClassesDir.lastIndexOf(File.separator) + 1) + "lib";

        // 记录环境变量的相关信息
        classPaths.clear();
        classPaths.put("classpath", Arrays.toString(classpath));
        classPaths.put("appServerClassesDir", appServerClassesDir);
        classPaths.put("appServerLibDir", appServerLibDir);
        classPaths.put("ESBLibs", extensionsLibs);

        // 处理ESB.xml文件所在的目录
        findFiles(new File(ESBFileUtils.getESBRootPath()), false, map);

        // 处理ClassPath路径
        for (String cp : classpath) {
            findFiles(new File(cp), false, map);
        }

        // 处理服务器的lib目录中的Jar文件
        findFiles(new File(appServerLibDir), true, map);

        // 处理ESB.class.path变量中的目录或文件
        if (extensionsLibs != null && !extensionsLibs.trim().isEmpty()) {
            classpath = extensionsLibs.split(pathSeparator);
            for (String cp : classpath) {
                findFiles(new File(cp), false, map);
            }
        }
        // 去掉对当前目录和父目录扫描
        map.remove(".");
        map.remove("..");

        // 将classpath中的目录放入列表中
        for (String fileName : map.values()) {
            classAndJar.addLast(fileName);
        }

        return classAndJar;
    }

    /**
     * 从系统上下文和类扫描器获取classpath
     *
     * @Description 相关说明
     * @param pathSeparator
     * @return
     * @Time 创建时间:2013-2-18上午11:53:56
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static String[] getClasspath(final String pathSeparator) {
        String[] classpath = SystemUtils.getClassPathInfo().split(pathSeparator);

        if (ESBClassScanner.class.getClassLoader() == null || !(ESBClassScanner.class.getClassLoader() instanceof URLClassLoader)) {
            return classpath;
        }

        Set<String> tmp = new HashSet<String>();
        for (String file : SystemUtils.getClassPathInfo().split(pathSeparator)) {
            tmp.add(new File(file).getPath());
        }

        for (URL url : ((URLClassLoader) ESBClassScanner.class.getClassLoader()).getURLs()) {
            tmp.add(new File(url.getFile()).getPath());
        }

        String[] paths = new String[tmp.size()];
        tmp.toArray(paths);

        return paths;
    }

    /**
     * 查找class目录或目录中的jar包
     *
     * @Description 相关说明
     * @Time 创建时间:2011-11-8下午7:36:25
     * @param jarOrDir
     * @param listFile
     * @param files
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static void findFiles(File jarOrDir, boolean listFile, final Map<String, String> files) {
        if (!jarOrDir.exists()) {
            return;
        }

        if (jarOrDir.getPath().contains("org.eclipse")) {
            return;
        }

        if (jarOrDir.isFile()) {
            String fileName = jarOrDir.getName();
            if (fileName.endsWith(".jar") && (scanInfo.jarPattern == null || scanInfo.jarPattern.matcher(fileName).find())) {
                files.put(fileName, jarOrDir.getPath());
            }
        } else if (jarOrDir.isDirectory()) {
            if (listFile) {
                jarOrDir.listFiles(new FileFilter() {
                    public boolean accept(File name) {
                        String fileName = name.getName();
                        if (name.isFile() && fileName.endsWith(".jar")
                                && (scanInfo.jarPattern == null || scanInfo.jarPattern.matcher(fileName).find())) {
                            files.put(fileName, name.getPath());
                            log.debug("发现文件", fileName, "，扫描此文件");
                        } else {
                            log.debug("发现文件", fileName, "，忽略此文件");
                        }
                        return false;
                    }
                });
            } else {
                files.put(jarOrDir.getPath(), jarOrDir.getPath());
            }
        }
    }

    /**
     * 类扫描方法
     *
     * 用于完成代码的扫描工具，处理过程如下：
     *              <p>
     *              1.加载包名和类名的正则表达式，控制包和类的扫描范围<br>
     *              2.获取扫描列表：class、classpath、服务器lib目录中的Jar、热部署lib目录中的Jar<br>
     *              3.为所有Jar包注册类加载器并绑定资源文件<br>
     *              4.生成并行扫描引擎实例，并行扫描所有Jar文件，并调用注解处理器对Class进行处理
     *              </p>
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2012-2-4上午8:42:52
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void scan(final String extensionsLibs, String inclusionJars, String inclusionClasses)
            throws ESBBaseCheckedException {
        // 设置Jar文件过滤规则
        if (inclusionJars != null) {
            scanInfo.jarPattern = Pattern.compile(inclusionJars);
        }

        // 设置Class类文件过滤规则
        if (inclusionClasses != null) {
            scanInfo.classPattern = Pattern.compile(inclusionClasses);
        }

        // 获取扫描路径
        LinkedList<String> scanClassAndJar = getClassFolderAndJars(extensionsLibs);
        ESBClassScanner.scanClassAndJar.addAll(scanClassAndJar);

        // 注册Jar包的类加载器并绑定资源
        for (String file : scanClassAndJar) {
            createClassLoaderAndBindResource(new File(file));
        }
        // appendJavassistClasspath();

        // 类扫描监视器为空时不再进行类扫描操作
        if (listeners.isEmpty()) {
            return;
        }

        if (!scanByClassListFile()) {
            scanByLibs(scanClassAndJar);
        }
    }

    /**
     * 从配置文件classListFile中获取要扫描的类列表
     *
     * @Description 此种扫描方式为主要用于Android这样的非标准J2SE环境。，
     *              此种扫描方式为单线程扫描，且不支持热部署和非默认加载器加载的类，扫描这些类时有可能会抛出ClassNotFound异常
     * @return
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2013年11月25日上午8:57:34
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static boolean scanByClassListFile() throws ESBBaseCheckedException {
        final File classListFile = new File(ESBFileUtils.getESBRootPath() + "/classListFile");
        if (!classListFile.exists() || !classListFile.isFile()) {
            return false;
        }

        final BufferedReader reader = new BufferedReader(ESBFileUtils.getInputStreamReader(classListFile.getPath(), null, "UTF-8"));
        String className = null;
        try {
            // 记录类扫描开始时间
            scanInfo.startTime = System.nanoTime();

            log.info("开始使用" + classListFile + "文件中的类信息进行扫描");
            while ((className = reader.readLine()) != null) {
                scanClass(className, null, null);
            }

            // 记录类扫描结束时间
            scanInfo.endTime = System.nanoTime();
            log.info("类扫描结束。", "，扫描类总数:", scanInfo.classCount, "，符合条件类总数:", scanInfo.matchCount, "，被监听器处理的类总数:", scanInfo.processCount,
                    "，总耗时:", ((scanInfo.endTime - scanInfo.startTime) / 1000000000.0), "秒");

        } catch (IOException e) {
            throw new ESBBaseCheckedException("读取类列表文件" + classListFile + "时发生错误", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new ESBBaseCheckedException("关闭类列表文件" + classListFile + "读取读取器时发生错误", e);
            }
        }

        return true;
    }

    /**
     * 从ClassPath等上下文信息中获取要扫描的Jar文件列表
     */
    private static void scanByLibs(LinkedList<String> scanClassAndJar) throws ESBBaseCheckedException {
        // 记录类扫描开始时间
        scanInfo.startTime = System.nanoTime();

        try {
            // 类扫描
            for(String folderOrJar : scanClassAndJar){
                try {
                    if (!folderOrJar.endsWith(".jar")) {
                        scanFolader(folderOrJar);
                    } else {
                        // 2. 再扫描lib目录下的jar和zip文件
                        scanJar(folderOrJar);
                    }
                } catch (ESBBaseCheckedException ex) {
                    throw ex;
                } catch (Throwable ex) {
                    throw new ESBBaseCheckedException("CS-00003:扫描" + folderOrJar + "中类时发生错误", ex);
                }
            }
            // 记录类扫描结束时间
            scanInfo.endTime = System.nanoTime();
            log.info("类扫描结束。符合条件的Jar文件数量:", scanInfo.matchJarFileCount, "，扫描类总数:", scanInfo.classCount, "，符合条件类总数:", scanInfo.matchCount,
                    "，被监听器处理的类总数:", scanInfo.processCount, "，总耗时:", ((scanInfo.endTime - scanInfo.startTime) / 1000000000.0), "秒");
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("CS-00004:扫描Class过程中发生错误", ex);
        }
    }

    /**
     * 扫描类文件目录
     *
     * @Description 相关说明
     * @Time 创建时间:2011-8-25下午5:05:37
     * @param appPath
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void scanFolader(String appPath) throws ESBBaseCheckedException {
        log.info("线程", Thread.currentThread().getName(), "开始扫描", appPath, "目录");

        try {
            // 解决路径中含空格、中文问题
            appPath = java.net.URLDecoder.decode(appPath, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new ESBBaseCheckedException("CS-00005:解析类文件目录" + appPath + "时发生错误", ex);
        }
        File classDir = new File(appPath);
        File[] classDirFiles = classDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (new File(dir.getPath() + "/" + name).isDirectory()) {
                    return true;
                } else if (name.endsWith(".class"))
                    return true;
                else {
                    return false;
                }
            }
        });
        if (classDirFiles == null) {
            if (appPath.startsWith("/")) {
                appPath = appPath.substring(1);
                classDir = new File(appPath);
                classDirFiles = classDir.listFiles();
            }
        }
        if (classDirFiles != null) {
            for (File file : classDirFiles) {
                if (file.isDirectory()) {
                    scanClassInDirectory(file, appPath);
                } else {
                    processPathChar(file, appPath);
                }
            }
        }
    }

    /**
     * 扫描Jar文件
     */
    public static void scanJar(String jarF) throws ESBBaseCheckedException {
        File jar = new File(jarF);

        log.info("线程", Thread.currentThread().getName(), "开始扫描", jar.getName(), "文件");

        // 记录符合扫描规划的Jar文件的数量
        scanInfo.matchJarFileCount.addAndGet(1);

        JarFile jarFile = ClassUtils.getJarFile(ClassUtils.createURLFromLocalFile(jar));
        ClassLoader classloader = findClassLoader(jarFile);

        try {
            for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
                JarEntry entry = (JarEntry) entries.nextElement();
                String entryPath = entry.getName();

                if (!entryPath.endsWith(".class")) {
                    continue;
                }

                // 不扫描内部类
                if (entryPath.indexOf("$") == -1) {
                    String className = entryPath.substring(0, entryPath.lastIndexOf('.')).replace('/', '.');
                    try {
                        scanClass(className, jarF.replace('\\', '/') + "!" + entryPath, classloader);
                    } catch (ESBBaseCheckedException ex) {
                        log.error(ex);
                    }
                }
            }
        } finally {
            try {
                jarFile.close();
            } catch (IOException ex) {
                throw new ESBBaseCheckedException("CS-00006:关闭扫描的" + jarF + "的Jar文件对象时发生错误", ex);
            }
        }
    }

    /**
     * 递归扫描目录及其中的class文件
     *
     * @Description 相关说明
     * @Time 创建时间:2011-9-22上午9:12:25
     * @param subDir
     * @param appPath
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static void scanClassInDirectory(File subDir, String appPath) throws ESBBaseCheckedException {
        File[] files = subDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (new File(dir.getPath() + "/" + name).isDirectory()) {
                    return true;
                } else if (name.endsWith(".class"))
                    return true;
                else {
                    return false;
                }
            }
        });
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanClassInDirectory(file, appPath);
                } else {
                    processPathChar(file, appPath);
                }
            }
        }
    }

    /**
     * 处理文件目录分隔符
     *
     * @Description 相关说明
     * @Time 创建时间:2011-9-22上午9:13:07
     * @param classFile
     * @param appPath
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static void processPathChar(File classFile, String appPath) throws ESBBaseCheckedException {
        String tempPath = classFile.getAbsolutePath();
        String fullPath = tempPath.replace('\\', '/');
        // 兼容linux系统下文件名带有/的情况
        if (fullPath.charAt(0) != appPath.charAt(0) && appPath.startsWith("/")) {
            appPath = appPath.substring(1).replace("./", "");
        }
        String className = fullPath.substring((appPath).length() + (appPath.endsWith("/") ? 0 : 1), fullPath.length() - 6)
                .replace('/', '.');

        scanClass(className, fullPath, null);
    }

    /**
     * 扫描类文件
     */
    private static void scanClass(String className, String fullPath, ClassLoader loader) throws ESBBaseCheckedException {
        // 扫描到的类的数量
        scanInfo.classCount.addAndGet(1);

        // 检查类名是否符合扫描规则
        if (scanInfo.classPattern != null && !scanInfo.classPattern.matcher(className).find()) {
            return;
        }

        // 记录符合扫描要求的类的数量
        scanInfo.matchCount.addAndGet(1);

        Class<?> clazz;
        try {
            if (loader == null) {
                // loader = ESBClassScanner.class.getClassLoader();
                loader = ESBClassLoaderManager.getRootClassLoader();
            }

            // 加载类
            clazz = Class.forName(className, false, loader);

            // 过滤掉不需要扫描的类
            if (clazz.isAnnotation() || clazz.isInterface() || clazz.isEnum()) {
                return;
            }
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("CS-00007:线程" + Thread.currentThread().getName() + "加载类" + className + "时发生错误，文件位置:"
                    + fullPath, ex);
        }

        // 调用这个类的所有注解的监听器的processClass方法处理类定义信息
        // 注：要强调的是监听器的processClass方法里不允许生成类定义信息，一旦类定义生成则类定义会被冻结，后面的监听器就会执行出错
        for (Class<? extends Annotation> key : listeners.keySet()) {
            IESBClassScannerListener listener = null;
            try {
                // 处理所有类的类扫描器，全系统只能有一个
                if (key != null && clazz.getAnnotation(key) == null) {
                    continue;
                }

                // 查找对应的监听器完成处理
                listener = listeners.get(key);
                if (listener != null) {
                    // 使用监听器处理相关的类
                    listener.process(clazz);

                    // 记录被监听器处理的类的数量
                    scanInfo.processCount.addAndGet(1);
                }
            } catch (ESBBaseCheckedException ex) {
                log.error(ex);
            } catch (Throwable ex) {
                log.error(new ESBBaseCheckedException("CS-00008:" + (listener != null ? listener.getClass().getName() : "") + "处理"
                        + clazz.getName() + "类的" + (key == null ? "全局" : key) + "注解时发生错误", ex));
            }
        }
    }

    // ==================================Jar包资源处理============================================================================================

    /**
     * 注册Classloader实例并绑定资源
     *
     * @Description 为每个扫描到的Jar分配相应的类加载器，将绑定资源
     * @Time 创建时间:2012-2-3下午7:08:52
     * @param file
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static void createClassLoaderAndBindResource(File file) throws ESBBaseCheckedException {
        if (!file.exists() || !file.isFile()) {
            return;
        }

        URL jarURL = ClassUtils.createURLFromLocalFile(file);
        JarFile jarFile = ClassUtils.getJarFile(jarURL);
        ClassLoader classloader = ESBClassLoaderManager.getRootClassLoader();
        String classLoaderName = null;

        Manifest mf = null;
        try {
            mf = jarFile.getManifest();
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("CS-00009:读取" + jarFile.getName() + "中的MANIFEST.MF文件内容时发生错误", ex);
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("CS-00010:关闭Jar文件" + file + "时发生错误", ex);
                }
            }
        }

        if (mf == null) {
            classLoaderName = ESBClassLoaderManager.PLATFORM_ROOT_CLASSLOADER;
        } else {
            // 检查Jar文件的部署正确
            String deploymentRange = mf.getMainAttributes().getValue("Deployment-Range");
            String serverType = ESBServerContext.getServerType();

            if (deploymentRange != null && !deploymentRange.equals("") && serverType != null && !serverType.equals("")) {
                String[] deploymentRanges = deploymentRange.split(",");
                String[] serverTypes = serverType.split(",");
                boolean canLoad = false;

                for (String dr : deploymentRanges) {
                    dr = dr.trim();
                    for (String st : serverTypes) {
                        st = st.trim();
                        if (dr.equalsIgnoreCase(st)) {
                            canLoad = true;
                            break;
                        }
                    }
                    if (canLoad) {
                        break;
                    }
                }

                if (!canLoad) {
                    throw new ESBBaseCheckedException("CS-00011:Jar文件" + file + "不允许在当前服务器上加载，请检查此Jar文件的部署正确性!");
                }
            }

            if (!Boolean.parseBoolean(mf.getMainAttributes().getValue("HotSwap"))) {
                classLoaderName = ESBClassLoaderManager.PLATFORM_ROOT_CLASSLOADER;
            } else {
                String appServerHotSwapLibDir = classPaths.get("appServerHotSwapLibDir");
                if (appServerHotSwapLibDir != null && file.getParentFile().equals(new File(appServerHotSwapLibDir))) {
                    classLoaderName = mf.getMainAttributes().getValue("Domain-Name");
                } else {
                    classLoaderName = ESBClassLoaderManager.PLATFORM_ROOT_CLASSLOADER;
                }
            }
        }

        // 获取类加载器
        if (classLoaderName == null || "".equals(classLoaderName)) {
            classLoaderName = ESBClassLoaderManager.PLATFORM_ROOT_CLASSLOADER;
        }
        classloader = ESBClassLoaderManager.findClassLoader(classLoaderName);

        if (classloader == null) {
            ESBClassLoaderManager.createClassLoader(classLoaderName, null);
        }
        classloader = ESBClassLoaderManager.addURL(classLoaderName, jarURL);
    }

    /**
     * 向Javassist增加ClassLoader
     *
     * @Description 相关说明
     * @Time 创建时间:2012-2-14上午11:52:54
     * @param file
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    // private static void appendJavassistClasspath() throws
    // ESBBaseCheckedException {
    // for (ClassLoader loader : ESBClassLoaderManager.getAllClassLoader()) {
    // ClassPool.getDefault().appendClassPath(new LoaderClassPath(loader));
    // }
    // }

    /**
     * 根据Jar文件查找相应的类加载器
     *
     * @Description 相关说明
     * @Time 创建时间:2012-2-3下午7:25:21
     * @param jarFile
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static ClassLoader findClassLoader(JarFile jarFile) throws ESBBaseCheckedException {
        ClassLoader classloader = ESBClassLoaderManager.getRootClassLoader();
        Manifest mf = null;

        try {
            mf = jarFile.getManifest();
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("CS-00009:读取" + jarFile.getName() + "中的MANIFEST.MF文件内容时发生错误", ex);
        }

        if (mf == null || !Boolean.parseBoolean(mf.getMainAttributes().getValue("HotSwap"))) {
            return classloader;
        }

        String classLoaderName = mf.getMainAttributes().getValue("Domain-Name");

        if (classLoaderName == null || "".equals(classLoaderName)) {
            classLoaderName = ESBClassLoaderManager.PLATFORM_ROOT_CLASSLOADER;
        }

        // 获取类加载器
        classloader = ESBClassLoaderManager.findClassLoader(classLoaderName);

        return classloader;
    }

    /**
     * 类扫描处理结束
     *
     * @Description 相关说明
     * @Time 创建时间:2012-5-18下午5:00:14
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void scanEnd() throws ESBBaseCheckedException {
        for (IESBClassScannerEndListener lisener : endListeners) {
            lisener.scanClassEnd();
        }
    }

    /**
     * 获取类路径和Jar包列表
     *
     * @Description 相关说明
     * @Time 创建时间:2012-3-13上午10:02:07
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public Map<String, String> getClassAndJar() {
        return classPaths;
    }

    @Override
    public List<String> getScanClassAndJar() {
        return scanClassAndJar;
    }

    @Override
    public String findClass(String className) {
        return ESBClassLoaderManager.findClass(className);
    }

}

/**
 * ESB基础技术平台项目 - 核心框架
 * <p>
 * com.css.ESB.kernel.platform
 * <p>
 * File: ESBClassScanner.java 创建时间:2011-8-31上午10:21:13
 * </p>
 * <p>
 * Title: 类扫描信息
 * </p>
 * <p>
 * Description: 类扫描信息，用于记录平台启动过程中来中类扫描的信息
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 中国软件与技术服务股份有限公司
 * </p>
 * <p>
 * Company: 中国软件与技术服务股份有限公司
 * </p>
 * <p>
 * 模块: 平台架构
 * </p>
 *
 * @author 张久旭
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
final class ClassScanInfo {

    /**
     * Jar文件过滤规则
     */
    public Pattern jarPattern;

    /**
     * Class类文件过滤规则
     */
    public Pattern classPattern;

    /**
     * 扫描开始时间
     */
    public long startTime;

    /**
     * 扫描完成时间
     */
    public long endTime;

    /**
     * 符合扫描规划的Jar文件数量
     */
    public AtomicInteger matchJarFileCount = new AtomicInteger(0);

    /**
     * 发现的类的数量
     */
    public AtomicLong classCount = new AtomicLong(0);

    /**
     * 符合扫描规则的类的数量
     */
    public AtomicLong matchCount = new AtomicLong(0);

    /**
     * 被扫描监听器处理的类的数量
     */
    public AtomicLong processCount = new AtomicLong(0);
}
