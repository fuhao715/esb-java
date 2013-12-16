package com.fuhao.esb.core.component.utils;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.classloader.ESBClassLoader;
import com.fuhao.esb.core.component.classloader.ESBClassLoaderManager;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.jar.JarFile;
/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ClassUtils {
    private static final ESBLogUtils log = ESBLogUtils.getLogger(ClassUtils.class);

    public static final String PRIMITIVE_DATA_TYPE_BOOLEAN = Boolean.TYPE.getName();
    public static final String PRIMITIVE_DATA_TYPE_CHAR = Character.TYPE.getName();
    public static final String PRIMITIVE_DATA_TYPE_BYTE = Byte.TYPE.getName();
    public static final String PRIMITIVE_DATA_TYPE_SHORT = Short.TYPE.getName();
    public static final String PRIMITIVE_DATA_TYPE_INT = Integer.TYPE.getName();
    public static final String PRIMITIVE_DATA_TYPE_LONG = Long.TYPE.getName();
    public static final String PRIMITIVE_DATA_TYPE_FLOAT = Float.TYPE.getName();
    public static final String PRIMITIVE_DATA_TYPE_DOUBLE = Double.TYPE.getName();

    // =========================================JDK代码编译器==========开始============================================================//
    private static class ByteArrayJavaSource extends SimpleJavaFileObject {
        private ByteArrayOutputStream baos = null;

        ByteArrayJavaSource(String name) {
            super(URI.create("bytes:///" + name), JavaFileObject.Kind.CLASS);
            this.baos = new ByteArrayOutputStream();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return this.baos;
        }

        public byte[] getBytes() {
            return this.baos.toByteArray();
        }
    }

    private static class StringJavaSource extends SimpleJavaFileObject {
        private String code;

        protected StringJavaSource(String className, String code) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return this.code;
        }
    }

    /**
     * 动态生成Class：未完成
     *
     * @Description 动态生成Class，为组件提供动态生成类功能
     *              <P>
     *              使用JDK的编译器进行源代码编译 <br>
     *              注：classloader为null时使用平台根类加载器
     *              </p>
     * @Time 创建时间:2011-8-30下午2:55:59
     * @param classloader
     * @param className
     * @param code
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Class<?> generateDynamicClassByJDK(ClassLoader classloader, String className, String code)
            throws ESBBaseCheckedException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        final List<ByteArrayJavaSource> source = new ArrayList<ByteArrayJavaSource>(1);
        JavaFileManager fileManager = new ForwardingJavaFileManager<JavaFileManager>(compiler.getStandardFileManager(diagnostics, null,
                null)) {
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
                    throws IOException {
                ByteArrayJavaSource javaSournce = new ByteArrayJavaSource(className);
                source.add(javaSournce);
                return javaSournce;
            }
        };

        // 编译配置
        List<String> options = new ArrayList<String>();
        options.add("-g");
        options.add("-encoding");
        options.add("UTF-8");
        options.add("-nowarn");

        // 生成编译任务
        CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null,
                Arrays.asList(new StringJavaSource(className, code)));

        // 编译源代码
        boolean result = task.call();

        // 处理编译信息
        StringBuilder errorMsg = new StringBuilder("\n");
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            errorMsg.append("编译错误:").append(diagnostic.getLineNumber()).append(",").append(diagnostic.getColumnNumber()).append(" -> ")
                    .append(diagnostic.getMessage(null)).append("\n");
        }

        // 非生产模式下输出要编译的源代码
        if (errorMsg.length() > 1 && !ESBServerContext.isProductMode()) {
            errorMsg.append(code);
        }

        if (errorMsg.length() > 1) {
            log.error(errorMsg);
        }

        try {
            fileManager.close();
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-01004:关闭编译器的文件管理器时发生错误", ex);
        }

        // 处理编译结果
        if (!result) {
            throw new ESBBaseCheckedException("UT-01005:编译" + className + "类失败");
        }

        // 处理类加载器
        ESBClassLoader loader = null;

        if (classloader == null) {
            // classloader为null时使用平台根类加载器
            loader = ESBClassLoaderManager.getRootClassLoader();
        } else if (classloader instanceof ESBClassLoader) {
            loader = (ESBClassLoader) classloader;
        } else {
            loader = new ESBClassLoader(className + "Classloader", classloader);
        }

        // 生成类
        return loader.findClass(className, source.get(0).getBytes());
    }

    /**
     * 动态生成Class：未完成
     *
     * @Description 动态生成Class，为组件提供动态生成类功能
     *              <P>
     *              使用JDK的编译器进行源代码编译
     *              </p>
     * @Time 创建时间:2012-1-27下午9:50:43
     * @param classloader
     * @param className
     * @param methodCodes
     * @param superClass
     * @param interfaces
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Class<?> generateDynamicClassByJDK(ClassLoader classloader, String className, String[] methodCodes, String superClass,
                                                     String[] interfaces) throws ESBBaseCheckedException {
        StringBuilder code = new StringBuilder();
        int index = -1;

        if ((index = className.lastIndexOf(".")) != -1) {
            String packageName = className.substring(0, index);
            code.append("package ").append(packageName).append(";\n");
        }

        if (index != -1) {
            code.append("public class ").append(className.substring(index + 1));
        } else {
            code.append("public class ").append(className);
        }

        if (superClass != null) {
            code.append(" extends ").append(superClass);
        }

        if (interfaces != null) {
            code.append(" implements ").append(interfaces[0]);
            for (int i = 1; i < interfaces.length; i++) {
                code.append(",").append(interfaces[i]);
            }
        }

        code.append(" {");

        // 生成方法代码
        for (String methodCode : methodCodes) {
            code.append("\n").append(methodCode);
        }

        code.append("\n}");

        return generateDynamicClassByJDK(classloader, className, code.toString());
    }

    // =========================================JDK代码编译器==========结束============================================================//

    /**
     * 获取类的包名
     *
     * @Description 相关说明
     * @Time 创建时间:2011-8-30下午12:30:28
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String getClassPackage(Class<?> clazz) {
        String className = clazz.getName();
        return className.substring(0, className.length() - clazz.getSimpleName().length() - 1);
    }

    /**
     * 实例化Class
     *
     * @Description 根据类名实例化类
     * @Time 创建时间:2011-11-22下午9:41:31
     * @param type
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Class<?> getClass(String type) throws ESBBaseCheckedException {
        Class<?> clazz = null;

        if (PRIMITIVE_DATA_TYPE_CHAR.equals(type)) {
            clazz = Boolean.TYPE;
        } else if (PRIMITIVE_DATA_TYPE_CHAR.equals(type)) {
            clazz = Character.TYPE;
        } else if (PRIMITIVE_DATA_TYPE_BYTE.equals(type)) {
            clazz = Byte.TYPE;
        } else if (PRIMITIVE_DATA_TYPE_SHORT.equals(type)) {
            clazz = Short.TYPE;
        } else if (PRIMITIVE_DATA_TYPE_INT.equals(type)) {
            clazz = Integer.TYPE;
        } else if (PRIMITIVE_DATA_TYPE_LONG.equals(type)) {
            clazz = Long.TYPE;
        } else if (PRIMITIVE_DATA_TYPE_FLOAT.equals(type)) {
            clazz = Float.TYPE;
        } else if (PRIMITIVE_DATA_TYPE_DOUBLE.equals(type)) {
            clazz = Double.TYPE;
        } else {
            try {
                clazz = Class.forName(type);
            } catch (ClassNotFoundException ex) {
                throw new ESBBaseCheckedException("UT-01006:类" + type + "不存在", ex);
            }
        }

        return clazz;
    }

    /**
     * 获取类名
     *
     * @Description 获取字符串描述的类全限定名
     * @Time 创建时间:2011-11-9上午9:25:15
     * @param c
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String getClassName(Class<?> c) {
        String type = null;

        if (c.isArray()) {
            type = c.getComponentType().getName();
        } else {
            if (c.isAnonymousClass()) {
                c = c.getSuperclass();
            }
            type = c.getName();
        }

        return type;
    }

    /**
     * 获取对象的类名
     *
     * @Description 获取字符串描述的对象的类全限定名
     * @Time 创建时间:2011-11-9上午9:26:03
     * @param obj
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String getClassName(Object obj) {
        Class<?> c = obj.getClass();
        return getClassName(c);
    }

    /**
     * 生成类实例
     *
     * @Description 相关说明
     * @Time 创建时间:2012-2-7下午8:18:34
     * @param name
     * @param initialize
     * @param classloader
     * @return
     * @throws ClassNotFoundException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Class<?> forName(String name, boolean initialize, ClassLoader classloader) throws ClassNotFoundException {
        if (classloader == null) {
            classloader = ESBClassLoaderManager.getRootClassLoader();
        }
        return Class.forName(name, initialize, classloader);
    }

    /**
     * 获取数组对象维度
     *
     *  获取数组对象维护度以及第一维的实际长度<br>
     *  例如：getArrayDimension(new Object[3][4][5])返回结果为：int[]{3,0,0}
     * @Time 创建时间:2011-11-9上午9:27:40
     * @param array
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static int[] getArrayDimension(Object array) {
        int length = Array.getLength(array);
        int count = array.getClass().getName().lastIndexOf('[') + 1;
        int[] dimension = new int[count];

        dimension[0] = length;

        for (int i = 1; i < count; i++) {
            dimension[i] = 0;
        }

        return dimension;
    }

    /**
     * 对象序列化检查
     *
     * @Description 用来检查对象是否可以进行远程序列化
     * @Time 创建时间:2012-1-12下午2:47:04
     * @param obj
     * @param debugMsg
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void checkSerializable(Object obj, String debugMsg) throws ESBBaseCheckedException {
        if (obj == null) {
            return;
        }

        if (!ESBServerContext.isDevelopMode() || !ESBServerContext.isCheckSerializable()) {
            return;
        }

        if (debugMsg != null) {
            log.debug(debugMsg);
        }

        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return;
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new ByteArrayOutputStream());
            oos.writeObject(obj);
        } catch (IOException ex) {
            checkSerializable(obj, new Stack<Object>());
            throw new ESBBaseCheckedException("UT-01007:对象序列化时发生错误", ex);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-01008:关闭对象序列化流时发生错误", ex);
                }
            }
        }
    }

    private static void checkSerializable(Object data, Stack<Object> stack) throws ESBBaseCheckedException {
        if (data == null) {
            return;
        }

        if (!(data instanceof Serializable)) {
            StringBuilder errorMsg = new StringBuilder("对象序列化失败\n\t失败对象位置：");
            for (Object obj : stack) {
                errorMsg.append(obj).append("->");
            }
            errorMsg.setLength(errorMsg.length() - 2);
            errorMsg.append("\n\t失败对象类型：").append(data.getClass().getName());
            throw new ESBBaseCheckedException("UT-01009:对象序列化失败", new NotSerializableException(errorMsg.toString()));
        } else if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            for (Entry<?, ?> entry : map.entrySet()) {
                stack.push(data.getClass().getSimpleName() + ":" + entry.getKey());
                checkSerializable(entry.getValue(), stack);
                stack.pop();
            }
        } else if (data instanceof Collection) {
            Collection<?> c = (Collection<?>) data;
            int i = 0;
            for (Object obj : c) {
                stack.push(data.getClass().getSimpleName() + ":" + i++);
                checkSerializable(obj, stack);
                stack.pop();
            }
        } else if (data.getClass().isArray()) {
            int length = Array.getLength(data);
            for (int i = 0; i < length; i++) {
                stack.push(data.getClass().getSimpleName() + ":" + i);
                checkSerializable(Array.get(data, i), stack);
                stack.pop();
            }
        }
    }

    /**
     * 生成本地文件的URL
     *
     * @Description 相关说明
     * @Time 创建时间:2012-1-21上午12:02:59
     * @param file
     * @return
     * @throws IOException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static URL createURLFromLocalFile(File file) throws ESBBaseCheckedException {
        URL url = null;
        try {
            final String name = file.getCanonicalPath().replace('\\', '/');
            if (file.isDirectory()) {
                url = new URL("file:" + name + "/");
            } else {
                if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
                    url = new URL("jar:file:" + name + "!/");
                } else {
                    url = new URL("file:" + name);
                }
            }
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("UT-01010:生成" + file + "的URL对象时发生错误", ex);
        }
        return url;
    }

    /**
     * 从URL指定的位置获取Jar文件对象
     *
     * @Description 相关说明
     * @Time 创建时间:2012-2-3下午4:00:19
     * @param jarURL
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static JarFile getJarFile(URL jarURL) throws ESBBaseCheckedException {
        URLConnection con = null;
        try {
            con = jarURL.openConnection();
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-01011:读取Jar文件" + jarURL + "失败，请检查文件或网络是否正常", ex);
        }

        JarFile jarFile = null;

        if (con instanceof JarURLConnection) {
            JarURLConnection jarCon = (JarURLConnection) con;
            jarCon.setUseCaches(true);
            try {
                jarFile = jarCon.getJarFile();
            } catch (IOException ex) {
                throw new ESBBaseCheckedException("UT-01012:获取Jar文件" + jarURL + "的文件对象失败，请检查文件是否损坏", ex);
            }
        }
        return jarFile;
    }

    /**
     * 查找指定类在文件系统上的位置
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:51:10
     * @param className
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String findClass(String className) {
        className = className.replace('.', '/') + ".class";
        URL addr = Thread.currentThread().getContextClassLoader().getResource(className);

        if (addr != null) {
            return addr.toString();
        }

        for (ESBClassLoader loader : ESBClassLoaderManager.getAllClassLoader()) {
            addr = loader.getResource(className);
            if (addr != null) {
                return addr.toString();
            }
        }

        addr = ClassLoader.getSystemResource(className);
        if (addr != null) {
            return addr.toString();
        }

        try {
            final Enumeration<URL> systemResources = ClassLoader.getSystemResources(className);
            while (systemResources != null && systemResources.hasMoreElements()) {
                addr = systemResources.nextElement();
                if (addr != null) {
                    return addr.toString();
                }
            }
        } catch (IOException ex) {
        }

        return null;
    }

    /**
     * 查找指定类在文件系统上的位置
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:51:10
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String findClass(Class<?> clazz) {
        return findClass(clazz.getName());
    }

    /**
     * 是否是接口
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:52:04
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isInterface(Class<?> clazz) {
        return isInterface(clazz.getModifiers());
    }

    /**
     * 是否是接口
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:52:14
     * @param mod
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isInterface(int mod) {
        return Modifier.isInterface(mod);
    }

    /**
     * 是否是枚举类
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:52:30
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isEnum(Class<?> clazz) {
        return clazz.isEnum();

    }

    /**
     * 是否是枚举对象
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:56:14
     * @param field
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isEnum(Field field) {
        return field.isEnumConstant();
    }

    /**
     * 是否是抽象类
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:52:45
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isAbstract(Class<?> clazz) {
        return isAbstract(clazz.getModifiers());
    }

    /**
     * 是否是抽象方法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:53:54
     * @param method
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isAbstract(Method method) {
        return isAbstract(method.getModifiers());
    }

    /**
     * 是否是抽象类或抽象方法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:52:45
     * @param mod
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isAbstract(int mod) {
        return Modifier.isAbstract(mod);
    }

    /**
     * 是否为Public类
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isPublic(Class<?> clazz) {
        return isPublic(clazz.getModifiers());
    }

    /**
     * 是否为Public方法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param method
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isPublic(Method method) {
        return isPublic(method.getModifiers());
    }

    /**
     * 是否为Public字段
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param field
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isPublic(Field field) {
        return isPublic(field.getModifiers());
    }

    /**
     * 是否为Public
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param mod
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isPublic(int mod) {
        return Modifier.isPublic(mod);
    }

    /**
     * 是否为Private类
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isPrivate(Class<?> clazz) {
        return isPrivate(clazz.getModifiers());
    }

    /**
     * 是否为Private方法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param method
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isPrivate(Method method) {
        return isPrivate(method.getModifiers());
    }

    /**
     * 是否为Private字段
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param field
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isPrivate(Field field) {
        return isPrivate(field.getModifiers());
    }

    /**
     * 是否是Private
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午10:03:34
     * @param mod
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isPrivate(int mod) {
        return Modifier.isPrivate(mod);
    }

    /**
     * 是否为Protected类
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isProtected(Class<?> clazz) {
        return isPrivate(clazz.getModifiers());
    }

    /**
     * 是否为Protected方法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param method
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isProtected(Method method) {
        return isProtected(method.getModifiers());
    }

    /**
     * 是否为Protected字段
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午9:59:35
     * @param field
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isProtected(Field field) {
        return isProtected(field.getModifiers());
    }

    /**
     * 是否为Private字段
     */
    public static boolean isProtected(int mod) {
        return Modifier.isProtected(mod);
    }

    /**
     * 是否是线程同步方法
     */
    public static boolean isSynchronized(Method method) {
        return isSynchronized(method.getModifiers());
    }

    /**
     * 是否是线程同步
     */
    public static boolean isSynchronized(int mod) {
        return Modifier.isSynchronized(mod);
    }

    /**
     * 是否为volatile字段
     *
     */
    public static boolean isVolatile(Field field) {
        return isVolatile(field.getModifiers());
    }

    /**
     * 是否为volatile
     */
    public static boolean isVolatile(int mod) {
        return Modifier.isVolatile(mod);
    }

    /**
     * 是否为transient字段
     */
    public static boolean isTransient(Field field) {
        return isTransient(field.getModifiers());
    }

    /**
     * 是否为transient
     */
    public static boolean isTransient(int mod) {
        return Modifier.isTransient(mod);
    }

    /**
     * 是否为final类
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午10:39:03
     * @param clazz
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isFinal(Class<?> clazz) {
        return isFinal(clazz.getModifiers());
    }

    /**
     * 是否为final方法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午10:39:50
     * @param method
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isFinal(Method method) {
        return isFinal(method.getModifiers());
    }

    /**
     * 是否为final字段
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午10:39:59
     * @param field
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isFinal(Field field) {
        return isFinal(field.getModifiers());
    }

    /**
     * 是否为final
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午10:40:09
     * @param mod
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isFinal(int mod) {
        return Modifier.isFinal(mod);
    }

    /**
     * 是否为静态方法
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午10:45:01
     * @param method
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isStatic(Method method) {
        return isStatic(method.getModifiers());
    }

    /**
     * 是否为静态字段
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午10:45:20
     * @param field
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * 是否为静态
     *
     * @Description 相关说明
     * @Time 创建时间:2012-12-20上午10:45:29
     * @param mod
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean isStatic(int mod) {
        return Modifier.isStatic(mod);
    }

    /**
     * 是否为本地方法
     */
    public static boolean isNative(Method method) {
        return isNative(method.getModifiers());
    }

    /**
     * 是否为本地对象
     */
    public static boolean isNative(int mod) {
        return Modifier.isNative(mod);
    }
}
