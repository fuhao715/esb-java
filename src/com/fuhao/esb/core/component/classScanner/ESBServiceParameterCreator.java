package com.fuhao.esb.core.component.classScanner;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.classScanner
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class ESBServiceParameterCreator {
    private final String serviceName;
    private int index;
    private final String clazz;

    public ESBServiceParameterCreator(String serviceName, int index, String clazz) {
        this.serviceName = serviceName;
        this.index = index;
        this.clazz = clazz;
    }

    public Object newInstanceof() throws ESBBaseCheckedException {
        try {
            return Class.forName(this.clazz).newInstance();
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("SM-00029:创建服务" + this.serviceName + "的第" + index + "个参数的默认值时发生错误:创建类" + clazz
                    + "的对象实例时发生错误", ex);
        }
    }
}
