package com.fuhao.esb.core.component.classScanner.scanner;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.classScanner.scanner
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public interface IESBClassScannerEndListener {
    /**
     * 类扫描结束事件
     */
    public void scanClassEnd() throws ESBBaseCheckedException;
}
