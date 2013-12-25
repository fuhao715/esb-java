package com.fuhao.esb.core.returncode;

/**
 * package name is  com.fuhao.esb.core.returncode
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public interface ReturnCodeConstant {
    public final String RTN_CODE_SYSTEM_EXCEPTION="9";
    public final String RTN_SUBCODE_SYSTEM_EXCEPTION="000";

    public final String RTN_CODE_SYSTEM_DEFAULT_EXCEPTION="9";
    public final String RTN_SUBCODE_SYSTEM_DEFAULT_EXCEPTION="999";

    public final String RTN_CODE_BIZ_EXCEPTION="3";
    public final String RTN_SUBCODE_BIZ_EXCEPTION="000";
    public final String RTN_MESSAGE_SYSTEM_EXCEPTION="系统内部异常";
    public final String RTN_REASON_SYSTEM_EXCEPTION="系统参数错误";
}
