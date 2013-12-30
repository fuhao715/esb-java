package com.fuhao.esb.test;

import com.fuhao.esb.core.component.ESBPlatformManager;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.route.RouteFileUtils;

/**
 * package name is  com.fuhao.esb.test
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
public class Main {
    public static void main(String []args) throws ESBBaseCheckedException {
        // new ESBPlatformManager().startPlatform(); // 测试平台启动
        new RouteFileUtils().loadRoutConfig();
    }
}
