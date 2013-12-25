package com.fuhao.esb.core.token;

/**
 * package name is  com.fuhao.esb.core.token
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class TokenConstants {
    /**
     * 请求类型
     */
    public enum FLOW_CONTROL_REQUEST_TYPE{
        Apply,Release,Refresh,Trash
    }
    /**
     * 刷新时令牌池的处理方式
     */
    public enum FLOW_CONTROL_POOL_HANDLE_TYPE{
        Add,Delete,Refresh
    }
    /**
     * 令牌池类型
     */
    public enum FLOW_CONTROL_POOL_TYPE{
        System,Service
    }
}
