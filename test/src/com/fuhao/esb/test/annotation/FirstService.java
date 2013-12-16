package com.fuhao.esb.test.annotation;

import com.fuhao.esb.core.annotation.Service;
import com.fuhao.esb.core.annotation.ServiceContainer;

/**
 * package name is  com.fuhao.esb.test.annotation
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 */
@ServiceContainer
public class FirstService {
    @Service(serviceName = "esb_insertPO")
    public String insertPO(String str){
        System.out.println("--------"+str);
        return "insertPO";
    }
}
