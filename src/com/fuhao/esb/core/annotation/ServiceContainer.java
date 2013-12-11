package com.fuhao.esb.core.annotation;

import java.lang.annotation.*;

/**
 * package name is  com.fuhao.esb.core.annotation
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceContainer {

    /**
     * 服务容器中的服务已经不再被支持
     *
     */
    String deprecated() default "";

}

