package com.fuhao.esb.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * package name is  com.fuhao.esb.common.utils
 * Created by fuhao on 14-2-11.
 * Project Name esb-java
 */
public class CollectionUtils {
    public static List<String> getValuesNames(List source)
    {
        Iterator it = source.iterator();
        List clone = new ArrayList();
        while (it.hasNext())
        {
            String name = it.next().toString();
            clone.add(name);
        }
        return clone;
    }
}
