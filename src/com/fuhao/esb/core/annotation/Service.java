package com.fuhao.esb.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package name is  com.fuhao.esb.core.annotation
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Service {
    /**
     * 服务名
     */
    String serviceName() default "";

    /**
     * 是否允许自动化单元测试框架收集此服务的调用信息
     */
    boolean unitTest() default false;

    /**
     * 服务功能描述
     */
    String memo() default "";

    /**
     * 服务版本
     */
    String version() default "";

    /**
     * 服务已经不再被支持的相关描述信息
     */
    String deprecated() default "";

    /**
     * 自动路由共享
     */
    boolean autoShare() default true;

    /**
     * 服务仅允许直接同步调用
     */
    boolean syncCall() default false;

    /**
     * 缓存服务调用结果
     */
    boolean cacheValue() default false;

    /**
     * 安全等级
     */
    byte securityLevel() default 0;
}
