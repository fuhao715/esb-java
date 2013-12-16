package com.fuhao.esb.core.component.classloader;

import com.fuhao.esb.core.component.utils.ClassUtils;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * package name is  com.fuhao.esb.core.component.classloader
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBClassLoaderManager {


    /**
     * 类加载器管理器
     */
    private static final Map<String, ESBClassLoader> classLoaderIndex = new ConcurrentHashMap<String, ESBClassLoader>(20);

    /**
     * 根据Jar包索引ClassLoader
     */
    private static final Map<URL, ESBClassLoader> classLoaderIndexByURL = new ConcurrentHashMap<URL, ESBClassLoader>(20);

    /**
     * 平台根类加载器
     */
    public static final String PLATFORM_ROOT_CLASSLOADER = "PlatformRootClassLoader";

    public static final ESBClassLoader createClassLoader(String name, ClassLoader parent) {
        ESBClassLoader loader = null;

        // 如果父加载器为null则认为父加载器为平台根类加载器
        if (parent == null) {
            parent = ESBClassLoaderManager.getRootClassLoader();
        }

        if (parent instanceof ESBClassLoader && (((ESBClassLoader) parent).getName().equals(name))) {
            loader = (ESBClassLoader) parent;
        } else {
            loader = new ESBClassLoader(name, parent);
        }

        // loader = new ESBClassLoader(name, parent);

        classLoaderIndex.put(name, loader);

        return loader;
    }

    public static ESBClassLoader createClassLoaderByCopy(String name) {
        ESBClassLoader loader = classLoaderIndex.get(name);

        if (loader == null) {
            throw new IllegalArgumentException("CL-00001:不存在名为" + name + "的类加载器");
        }

        loader = new ESBClassLoader(loader);
        classLoaderIndex.put(name, loader);

        return loader;
    }

    public static final ESBClassLoader findClassLoader(String name) {
        return classLoaderIndex.get(name);
    }

    public static final ESBClassLoader findClassLoader(URL jarName) {
        return classLoaderIndexByURL.get(jarName);
    }

    public static final ESBClassLoader removeClassLoader(String name) {
        ESBClassLoader loader = classLoaderIndex.remove(name);
        for (URL url : loader.getURLs()) {
            classLoaderIndexByURL.remove(url);
        }
        return loader;
    }

    public static final boolean isUnRegisterClassLoader(ClassLoader loader) {
        if (!(loader instanceof ESBClassLoader)) {
            return false;
        }

        if (!classLoaderIndex.containsKey(((ESBClassLoader) loader).getName())) {
            return true;
        }

        for (ClassLoader oldLoader : classLoaderIndex.values()) {
            if (oldLoader == loader) {
                return false;
            }
        }

        return true;
    }

    public static final Collection<ESBClassLoader> getAllClassLoader() {
        return classLoaderIndex.values();
    }

    public static final ESBClassLoader getRootClassLoader() {
        return classLoaderIndex.get(PLATFORM_ROOT_CLASSLOADER);
    }

    public static ESBClassLoader addURL(String name, URL url) {
        ESBClassLoader loader = classLoaderIndex.get(name);
        if (loader == null) {
            throw new IllegalArgumentException("CL-00001:不存在名为" + name + "的类加载器");
        }
        loader.addURL(url);
        classLoaderIndexByURL.put(url, loader);
        return loader;
    }

    public static String findClass(String className) {
        return ClassUtils.findClass(className);
    }
}
