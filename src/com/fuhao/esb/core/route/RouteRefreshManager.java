package com.fuhao.esb.core.route;

import com.fuhao.esb.core.component.ESBComponentManager;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-26.
 * Project Name esb-java
 */
public class RouteRefreshManager {

    private ESBLogUtils logger = ESBLogUtils.getLogger(RouteRefreshManager.class);

    //构造公平的可重入锁
    private ReentrantLock lock = new ReentrantLock(true);

    /**
     * 路由刷新定时器
     * @throws ESBBaseCheckedException
     */
    public void start() throws ESBBaseCheckedException {

		/* 启动时设置初始版本号，获得表的版本号set到内存中 */
        Long version = RouteFileUtils.getRouteConfVersion();
        if(null != version){
            RouteCache.getInstance().setRouteVersion(version);
        }else{
            RouteCache.getInstance().setRouteVersion(0L);
        }
        // 从qz_dmb中查询刷新频率时间
        Long refreshCapacity = RouteFileUtils.getRefreshCapacity();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
				/*锁住此刷新线程，否则容易出现重复刷新，即第一次刷新还没有操作完，第二次刷新又开始了。造成资源同步问题 */
                lock.lock();
                try {
                    // 调用刷新逻辑方法
                    refreshRoute();
                } catch (ESBBaseCheckedException ex) {
                    logger.error("----刷新路由缓存的异常信息为：----"+ex.getMessage()+"------",ex);
                }finally{
                    lock.unlock();
                }
            }
        }, 0, refreshCapacity);
    }

    private void refreshRoute() throws ESBBaseCheckedException{
        Long currentVersion = RouteFileUtils.getRouteConfVersion();
        if(null == currentVersion)
            currentVersion = 0L; // 若库中未配置路由版本号，默认为0L
        Long memoryVersion = RouteCache.getInstance().getRouteVersion();
        if(currentVersion>memoryVersion){
            Map<String, String> mapParameter = ESBComponentManager.getInitMapParameter();
            //缓存路由
            new RouteLoader().loadRoute();//采用服务方式调用，以免连接不释放
            RouteCache.getInstance().setRouteVersion(currentVersion);
        }
    }
}