package com.fuhao.esb.core.component.globalvariables;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * package name is  com.fuhao.esb.core.component.globalvariables
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public final class ESBGlobalVariables implements IESBGlobalVariablesMXBean {
    private static Map<String, String> var = new ConcurrentHashMap<String, String>();
    private static final ESBGlobalVariables inst = new ESBGlobalVariables();

    private ESBGlobalVariables() {
    }

    public Set<String> keySet() {
        return var.keySet();
    }

    public Collection<String> values() {
        return var.values();
    }

    public Set<Entry<String, String>> entrySet() {
        return var.entrySet();
    }

    public String put(String key, String value) {
        return var.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends String> m) {
        var.putAll(m);
    }

    public String get(String key) {
        return var.get(key);
    }

    public String remove(String key) {
        return var.remove(key);
    }

    public void clear() {
        var.clear();
    }

    public boolean containsKey(String key) {
        return var.containsKey(key);
    }

    public boolean containsValue(String value) {
        return var.containsValue(value);
    }

    public boolean isEmpty() {
        return var.isEmpty();
    }

    public int size() {
        return var.size();
    }

    public static ESBGlobalVariables inst() {
        return inst;
    }
}
