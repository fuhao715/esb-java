package com.fuhao.esb.core.component.classScanner.filter;

import java.util.regex.Pattern;

/**
 * package name is  com.fuhao.esb.core.component.classScanner.filter
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public class ESBServiceCallSecurityInfo {
    /**
     * 调用信息
     */
    public final Pattern source;

    /**
     * 允许调用的目标
     */
    public final Pattern perimitDestination;

    public ESBServiceCallSecurityInfo(Pattern source, Pattern perimitDestination) {
        this.source = source;
        this.perimitDestination = perimitDestination;
    }
}
