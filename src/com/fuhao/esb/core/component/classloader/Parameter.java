package com.fuhao.esb.core.component.classloader;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * package name is  com.fuhao.esb.core.component.classloader
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {

    String defaultValue();

}
