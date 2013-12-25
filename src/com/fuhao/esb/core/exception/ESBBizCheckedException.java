package com.fuhao.esb.core.exception;

import com.fuhao.esb.core.component.ESBServerContext;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.exception
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class ESBBizCheckedException extends ESBBaseCheckedException {

    private static final long serialVersionUID = -7926728004903781662L;

    /** *
     * 返回原因
     */

    public ESBBizCheckedException(String message) {
        super(message, (Throwable) null);
    }

    public ESBBizCheckedException(Throwable ex) {
        super(ex);
    }

    public ESBBizCheckedException( Map<String, Object> parameters) {
        super(parameters);
    }

    public ESBBizCheckedException(Map<String, Object> parameters, Throwable ex) {
        super(parameters, ex);
    }

    @Override
    public Type getType() {
        return Type.APPLICATION;
    }
}
