package com.fuhao.esb.core.component.sequence;

/**
 * package name is  com.fuhao.esb.core.component.sequence
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public interface IUnforceBillCodeInfo {

    public String getName();

    public void updateMinAndMax(long min, long max, long value);

    public long getCurrentMinRange();

    public long getCurrentMaxRange();

    public long getCurrentValue();

    public long getNextValue();

    public void updateValue(long value);

    public long getCacheValue();

    public void setCacheValue(long cacheValue);

}
