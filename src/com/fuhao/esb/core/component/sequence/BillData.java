package com.fuhao.esb.core.component.sequence;

/**
 * package name is  com.fuhao.esb.core.component.sequence
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
import java.io.Serializable;

public class BillData implements Serializable {
    private static final long serialVersionUID = -5278917369825958279L;

    private final Object key;
    private String djh;// 单据号
    private String ydm;// 域代码
    private String xh;// 序号

    public BillData(String key) {
        this.key = key.intern();
    }

    public BillData(String key, String xh) {
        this(key);
        this.xh = xh;
    }

    public BillData(String key, String xh, String djh, String ydm) {
        this(key, xh);
        this.djh = djh;
        this.ydm = ydm;
    }

    public Object getKey() {
        return key;
    }

    public String getDjh() {
        return djh;
    }

    public void setDjh(String djh) {
        this.djh = djh;
    }

    public String getDomain() {
        return ydm;
    }

    public void setDomain(String ydm) {
        this.ydm = ydm;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

}

