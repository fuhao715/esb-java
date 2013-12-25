package com.fuhao.esb.core.component.utils;

/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 */
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyTaskExcutor {
    private static AsyTaskExcutor asyTaskExcutor = new AsyTaskExcutor();

    private  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10, 50, 30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadPoolExecutor.AbortPolicy());  //TODO 默认执行线程池大小，需改为可配置可修改

    public  ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    private AsyTaskExcutor() {

    }

    public static AsyTaskExcutor getInstance() {
        return asyTaskExcutor;
    }
}
