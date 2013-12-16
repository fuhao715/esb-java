package com.fuhao.esb.core.component.globalvariables;

/**
 * package name is  com.fuhao.esb.core.component.globalvariables
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBGlobalVariablesMXBean {
    public String put(String key, String value);

    public String get(String key);

    public String remove(String key);
}
