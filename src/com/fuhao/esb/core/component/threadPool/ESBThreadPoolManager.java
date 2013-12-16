package com.fuhao.esb.core.component.threadPool;
import com.fuhao.esb.core.component.AbsESBComponentManager;
import com.fuhao.esb.core.component.manager.ESBPlatformMXBeanManager;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * package name is  com.fuhao.esb.core.component.threadPool
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class ESBThreadPoolManager extends AbsESBComponentManager {

    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBThreadPoolManager.class);

    static final Map<String, ThreadPoolInfo> threadPoolIndex = new ConcurrentHashMap<String, ThreadPoolInfo>(5);

    private static void checkExists(String name) throws ESBBaseCheckedException {
        if (threadPoolIndex.containsKey(name)) {
            throw new ESBBaseCheckedException("PC-00010:已存在名为" + name + "的线程池");
        }
    }

    public static ExecutorService newFixedThreadPool(String componentName, String name, int nThreads, ThreadFactory threadFactory)
            throws ESBBaseCheckedException {
        checkExists(name);
        ExecutorService pool = Executors.newFixedThreadPool(nThreads, threadFactory == null ? new DefaultThreadFactory(name)
                : threadFactory);
        return registerPool(componentName, name, pool, nThreads, nThreads);
    }

    public static ExecutorService newSingleThreadExecutor(String componentName, String name, ThreadFactory threadFactory)
            throws ESBBaseCheckedException {
        checkExists(name);
        ExecutorService pool = Executors.newSingleThreadExecutor(threadFactory == null ? new DefaultThreadFactory(name) : threadFactory);
        return registerPool(componentName, name, pool, 1, 1);
    }

    public static ExecutorService newCachedThreadPool(String componentName, String name, ThreadFactory threadFactory)
            throws ESBBaseCheckedException {
        return newCachedThreadPool(componentName, name, 0, threadFactory);
    }

    public static ExecutorService newCachedThreadPool(String componentName, String name, int nThread, ThreadFactory threadFactory)
            throws ESBBaseCheckedException {
        checkExists(name);
        ExecutorService pool = Executors.newCachedThreadPool(threadFactory == null ? new DefaultThreadFactory(name) : threadFactory);
        return registerPool(componentName, name, pool, nThread, Integer.MAX_VALUE);
    }

    public static ExecutorService newScheduledThreadPool(String componentName, String name, int corePoolSize, ThreadFactory threadFactory)
            throws ESBBaseCheckedException {
        checkExists(name);
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(corePoolSize, threadFactory == null ? new DefaultThreadFactory(
                name) : threadFactory);
        return registerPool(componentName, name, pool, corePoolSize, Integer.MAX_VALUE);
    }

    public static ExecutorService registerPool(String componentName, String name, ExecutorService pool, int corePoolSize,
                                               int maximumPoolSize) throws ESBBaseCheckedException {
        checkExists(name);
        threadPoolIndex.put(name, new ThreadPoolInfo(name, componentName, pool, corePoolSize, maximumPoolSize));
        return pool;
    }

    public static ExecutorService findThreadPool(String name) {
        ThreadPoolInfo info = threadPoolIndex.get(name);
        return info == null ? null : info.pool;
    }

    public static void shutdown(String name) {
        ExecutorService pool = findThreadPool(name);
        if (pool == null) {
            return;
        }
        pool.shutdown();
        threadPoolIndex.remove(name);
    }

    public static void shutdownNow(String name) {
        ExecutorService pool = findThreadPool(name);
        if (pool == null) {
            return;
        }
        pool.shutdownNow();
        threadPoolIndex.remove(name);
    }

    public static boolean isShutdown(String name) throws ESBBaseCheckedException {
        ExecutorService pool = findThreadPool(name);
        if (pool == null) {
            throw new ESBBaseCheckedException("PC-00011:不存在名为" + name + "的线程池");
        }
        return pool.isShutdown();
    }

    public static boolean isTerminated(String name) throws ESBBaseCheckedException {
        ExecutorService pool = findThreadPool(name);
        if (pool == null) {
            throw new ESBBaseCheckedException("PC-00011:不存在名为" + name + "的线程池");
        }
        return pool.isTerminated();
    }

    public static void execute(String name, Runnable command) throws ESBBaseCheckedException {
        ExecutorService pool = findThreadPool(name);
        if (pool == null) {
            throw new ESBBaseCheckedException("PC-00011:不存在名为" + name + "的线程池");
        }
        pool.execute(command);
    }

    public static void submit(String name, Runnable command) throws ESBBaseCheckedException {
        ExecutorService pool = findThreadPool(name);
        if (pool == null) {
            throw new ESBBaseCheckedException("PC-00011:不存在名为" + name + "的线程池");
        }
        pool.submit(command);
    }

    public static void submit(String name, Callable<?> command) throws ESBBaseCheckedException {
        ExecutorService pool = findThreadPool(name);
        if (pool == null) {
            throw new ESBBaseCheckedException("PC-00011:不存在名为" + name + "的线程池，无法执行提交的任务");
        }
        pool.submit(command);
    }

    // ----------------------组件管理方法----------------------------------------------------------------------------------------

    @Override
    public void start(Map<String, String> parameter) throws ESBBaseCheckedException {
        if (this.state == ComponentState.RUNNING) {
            return;
        }
        ESBPlatformMXBeanManager.registerMBean(null, "ThreadPoolManager", new ESBThreadPoolMXBean());
        this.state = ComponentState.RUNNING;

    }

    @Override
    public synchronized void stop() throws ESBBaseCheckedException {
        if (!threadPoolIndex.isEmpty()) {
            for (ThreadPoolInfo info : threadPoolIndex.values()) {
                log.info("开始关闭" + info.componentName + "组件创建的线程池" + info.name);
                info.pool.shutdown();
            }
        }
        this.state = ComponentState.STOP;
    }

    public String getMemo() {
        return "本地线程池管理器，用于管理本地线程池的创建、销毁和监控等功能";
    }

}

class DefaultThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    DefaultThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix + "-thread-";
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}

class ThreadPoolInfo {
    public final String name;
    public final String componentName;
    public final ExecutorService pool;
    public final int corePoolSize;
    public final int maximumPoolSize;

    public ThreadPoolInfo(String name, String componentName, ExecutorService pool, int corePoolSize, int maximumPoolSize) {
        this.name = name;
        this.componentName = componentName;
        this.pool = pool;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(50);
        str.append(name).append(", ").append(componentName).append(", ").append(corePoolSize).append(", ").append(maximumPoolSize);
        return str.toString();
    }
}
