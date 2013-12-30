package com.fuhao.esb.core.route;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-26.
 * Project Name esb-java
 */
public class RouteLoader {


    public void loadRoute() throws ESBBaseCheckedException{
        RouteFileUtils.loadRoutConfig(); // 读取配置文件并缓存
        // cache 全匹配路由算法，以及访问次数缓存算法。

    }

}
