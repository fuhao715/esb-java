package com.fuhao.esb.test.path;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * package name is  com.fuhao.esb.test.path
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class MapTest {
    private static Map<String, Test> mapRouteHandler = new ConcurrentHashMap<String, Test>();

    public static Test getTest(String name){
        Test t = mapRouteHandler.get(name);
        if(null != t){
           return t;
        }
        t = new Test();
        mapRouteHandler.put(name,t);
        return t;
    }

}
