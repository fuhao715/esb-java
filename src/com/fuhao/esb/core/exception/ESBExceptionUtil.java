package com.fuhao.esb.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * package name is  com.fuhao.esb.core.exception
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ESBExceptionUtil {
    public static String getStackTraceMessage( Throwable e){
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.getBuffer().toString();
    }

    /**
     *记录异常信息j
     * @param tranId
     * @param jylsh
     * @param e
     */
    public static void logException(String tranId, String jylsh, Throwable e){
           // TODO 记录异常入库分析
    }

}
