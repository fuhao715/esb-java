package com.fuhao.esb.core.component.classloader;

import com.fuhao.esb.core.component.utils.ClassUtils;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;


import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

/**
 * package name is  com.fuhao.esb.core.component.classloader
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBClassLoader extends URLClassLoader {
    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBClassLoader.class);

    /**
     * 类加载器名
     */
    private final String name;
    /**
     * 类加载器资源列表
     */
    private final List<ResourceInfo> urlResourceInfo = new LinkedList<ResourceInfo>();
    /**
     * 是否为平台根类加载器，平台根类加载器有一些特殊限制，如不能被重新生成，不执行资源变化检查；<br>
     * 根类型加载器主要用途之一是加载系统公用(全域访问)的动态代码
     */
    private final boolean IsRootClassLoader;

    public ESBClassLoader(String name, URL[] urls, ClassLoader parent) {
        super(new URL[0], parent);

        if (ESBClassLoaderManager.PLATFORM_ROOT_CLASSLOADER.equals(name) && ESBClassLoaderManager.findClassLoader(name) != null) {
            throw new IllegalArgumentException("不允许重新生成平台根类加载器实例");
        }

        this.name = name;

        if (ESBClassLoaderManager.PLATFORM_ROOT_CLASSLOADER.equals(name)) {
            IsRootClassLoader = true;
        } else {
            IsRootClassLoader = false;
        }

        for (URL url : urls) {
            this.addURL(url);
        }
    }

    public ESBClassLoader(String name, ClassLoader parent) {
        this(name, new URL[0], parent);
    }

    public ESBClassLoader(ESBClassLoader loader) {
        this(loader.name, loader.getURLs(), loader.getParent());
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);

        if ("jar".equals(url.getProtocol()) || "file".equals(url.getProtocol())) {
            try {
                urlResourceInfo.add(new ResourceInfo(url));
            } catch (Exception ex) {
                throw new IllegalArgumentException("向类加载器" + this.getName() + "增加URL时发生错误", ex);
            }
        }
    }

    /**
     * 向Classloader增加本地资源文件
     *
     * @Description 相关说明
     * @param file
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2013年10月9日下午10:52:47
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void addFile(File file) throws ESBBaseCheckedException {
        this.addURL(ClassUtils.createURLFromLocalFile(file));
    }

    /**
     * 向Classloader增加指定目录下的文件
     *
     * @Description 不增加指定目录中子目录及子目录内的文件
     * @param path
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2013年10月9日下午9:53:18
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void addFilesByPath(final File path) throws ESBBaseCheckedException {
        addFilesByPath(path, null);
    }

    /**
     * 向Classloader增加指定目录下的文件，支持过滤器自定义规则过滤
     *
     * @Description 不增加指定目录中子目录及子目录内的文件
     * @param path
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2013年10月9日下午9:53:18
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void addFilesByPath(final File path, final FileFilter filter) throws ESBBaseCheckedException {
        if (!path.isDirectory()) {
            throw new ESBBaseCheckedException("CL-00002:" + path.getName() + "不是目录");
        }

        for (File f : path.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (filter != null) {
                    return filter.accept(file);
                } else {
                    return true;
                }
            }
        })) {
            this.addFile(f);
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            return this.getParent().loadClass(name);
        } catch (Exception ex) {
            // 父加载器加载失败时由子类加载器进行加载
        }

        Class<?> clazz = super.findLoadedClass(name);

        if (clazz != null) {
            return clazz;
        }

        clazz = super.findClass(name);

        return clazz;
    }

    /**
     * 检查当前类加载器加载的资源是否发生变化
     *
     * @Description 相关说明
     * @Time 创建时间:2012-1-21下午11:31:22
     * @return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public boolean checkResourceChange() {
        // 跳过对平台根类加载器的资源变化检查
        if (IsRootClassLoader) {
            return false;
        }

        for (ResourceInfo info : urlResourceInfo) {
            // 跳过已经删除或时间未发生变化的文件
            if (!info.resourceFile.exists()) {
                log.warn(info.resourceFile + "文件已被删除，跳过变化检查");
                continue;
            }

            try {
                // 重新计算最后编辑时间发生变化的文件的MD5
                if (info.resourceIsChange()) {
                    log.info("文件" + info.resourceFile + "发生变化");
                    return true;
                }
            } catch (Exception ex) {
                log.error(new ESBBaseCheckedException("检查文件" + info.resourceFile + "的变化情况是发生错误", ex));
                return false;
            }

        }

        return false;
    }

    public Class<?> findClass(String className, byte[] bytes) {
        return super.defineClass(className, bytes, 0, bytes.length);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return super.getResourceAsStream(name);
    }

    public String getName() {
        return this.name;
    }

    public List<ResourceInfo> getUrlResourceInfo() {
        return urlResourceInfo;
    }

    public void destroy() {
        ESBClassLoaderManager.removeClassLoader(this.name);
    }
}
