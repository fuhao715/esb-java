package com.fuhao.esb.core.component.classScanner.scanner;

import java.util.List;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component.classScanner.scanner
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public interface IESBClassScannerMXBean {
    public Map<String, String> getClassAndJar();

    public String findClass(String className);

    public List<String> getScanClassAndJar();
}
