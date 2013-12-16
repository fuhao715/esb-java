package com.fuhao.esb.core.component.classScanner.scanner;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component.classScanner.scanner
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBClassScannerListener {
    /**
     * Class类型监听器注册方法-返回值为要处理的Annotation

     */
    public Class<? extends Annotation> registerAnnotation(Map<String, String> parameter) throws ESBBaseCheckedException;

    /**
     * 处理方法
     */
    public void process(Class<?> clazz) throws ESBBaseCheckedException;
}
