package com.fuhao.esb.core.component.classloader;
import com.fuhao.esb.core.component.utils.ClassUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
/**
 * package name is  com.fuhao.esb.core.component.classloader
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ResourceInfo {
    /**
     * 资源加载协议
     */
    public final String protocol;

    /**
     * 资源文件
     */
    public final File resourceFile;

    /**
     * 最后修改时间
     */
    public final long lastModified;

    /**
     * 版本
     */
    public final String version;

    public ResourceInfo(URL url) throws ESBBaseCheckedException {
        this.protocol = url.getProtocol();
        String file = url.getFile();
        if (file.startsWith("jar:")) {
            file = file.substring(4);
        }
        if (file.startsWith("file:")) {
            file = file.substring(5);
        }
        if (file.endsWith("!/")) {
            file = file.substring(0, file.length() - 2);
        }
        this.resourceFile = new File(file);
        this.lastModified = this.resourceFile.lastModified();
        this.version = getJarFileVersion();

    }

    /**
     * 文件是否发生变化
     *
     * @Description 相关说明
     * @Time 创建时间:2012-1-22上午12:17:33
     * @return
     * @throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    boolean resourceIsChange() throws ESBBaseCheckedException {
        boolean isChange = lastModified != resourceFile.lastModified();
        if (this.version != null) {
            isChange = isChange && !version.equals(getJarFileVersion());
        }
        return isChange;
    }

    /**
     * 生成Jar包的MD5码
     */
    private String getJarFileVersion() throws ESBBaseCheckedException {
        if (!"jar".equals(this.protocol)) {
            return null;
        }

        JarFile f = ClassUtils.getJarFile(ClassUtils.createURLFromLocalFile(resourceFile));
        String md5 = null;
        Manifest manifest = null;
        try {
            manifest = f.getManifest();
            if (manifest != null) {
                md5 = manifest.getMainAttributes().getValue("MD5");
            }
            if ("".equals(md5)) {
                md5 = null;
            }
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("读取" + resourceFile + "的MANIFAST.MF文件时发生错误", ex);
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("关闭Jar文件" + f + "时发生错误", ex);
                }
            }
        }

        return md5;
    }
}
