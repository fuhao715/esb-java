package com.fuhao.esb.core.security;

/**
 * package name is  com.fuhao.esb.core.security
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class SystemSignConstants {
    /**
     * 签到操作
     * checkIn 签到
     * checkOut 签退
     * forceCheckOut 强制退出
     */
    public static final String OPER_LOGIN = "checkIn";
    public static final String OPER_LOGOUT = "checkOut";
    public static final String OPER_FORCELOGOUT = "forceCheckOut";
    /**
     * 签到状态
     * I 签到
     * O 签退
     * F 强制签退
     */
    public static final String STATUS_LOGIN = "I";
    public static final String STATUS_LOGOUT = "O";
    public static final String STATUS_FORCELOGOUT = "F";
    /**
     * 签到、签退、交易状态查询对外服务
     * C00.QX.YYJC.LOGON 签到
     * C00.QX.YYJC.LOGOUT 签退
     * C00.CX.JYCLZT.JYCX 交易状态查询
     */
    public static final String EXTERNAL_SERVICE_SIGN_IN = "C00.QX.YYJC.LOGON";
    public static final String EXTERNAL_SERVICE_SIGN_OUT = "C00.QX.YYJC.LOGOUT";
    public static final String EXTERNAL_SERVICE_CONNECT_TEST = "C00.LT.YYJC.LTCS";
    public static final String EXTERNAL_SERVICE_TRANSACTION_QUERY = "C00.CX.JYCLZT.JYCX";
    /**
     * 签到签退报文扩展字段key值,公钥
     */
    public static final String SECURITY_PUBLIC_KEY = "securityPublicKey";
}
