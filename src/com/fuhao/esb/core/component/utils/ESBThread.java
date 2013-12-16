package com.fuhao.esb.core.component.utils;

import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.session.BaseSession;

/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBThread <V> extends Thread {
    private static int threadInitNumber = 0;

    private final Runnable target;
    private BaseSession session;
    protected volatile V value;
    protected volatile ESBBaseCheckedException ex;
    private final boolean isPCThread;
    private volatile Object description;

    public ESBThread() {
        this(null, null, null, 0);
    }

    public ESBThread(String name) {
        this(null, null, name, 0);
    }

    public ESBThread(Runnable target) {
        this(null, target, null, 0);
    }

    public ESBThread(Runnable target, String name) {
        this(null, target, name, 0);
    }

    public ESBThread(ThreadGroup group, String name) {
        this(group, null, name, 0);
    }

    public ESBThread(ThreadGroup group, Runnable target) {
        this(group, target, null, 0);
    }

    public ESBThread(ThreadGroup group, Runnable target, String name) {
        this(group, target, name, 0);
    }

    public ESBThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        this(group, target, name, stackSize, false);
    }

    public ESBThread(boolean isPCThread) {
        this(null, null, null, 0, isPCThread);
    }

    public ESBThread(String name, boolean isPCThread) {
        this(null, null, name, 0, isPCThread);
    }

    public ESBThread(Runnable target, boolean isPCThread) {
        this(null, target, null, 0, isPCThread);
    }

    public ESBThread(Runnable target, String name, boolean isPCThread) {
        this(null, target, name, 0, isPCThread);
    }

    public ESBThread(ThreadGroup group, String name, boolean isPCThread) {
        this(group, null, name, 0, isPCThread);
    }

    public ESBThread(ThreadGroup group, Runnable target, boolean isPCThread) {
        this(group, target, null, 0, isPCThread);
    }

    public ESBThread(ThreadGroup group, Runnable target, String name, boolean isPCThread) {
        this(group, target, name, 0, isPCThread);
    }

    public ESBThread(ThreadGroup group, Runnable target, String name, long stackSize, boolean isPCThread) {
        super(group, target, name == null ? "ESBThread-" + threadInitNumber++ : name, stackSize);
        this.target = target;
        this.isPCThread = isPCThread;
    }

    public final BaseSession mergeCallThreadSession(BaseSession session, boolean mergeFullSessionInfo) {
        if (session == null) {
            session = ESBServerContext.getSession();
        }

        if (mergeFullSessionInfo) {
            this.session = session;
        } else {
            this.session = new BaseSession(session, false);
        }

        return this.session;
    }

    @Override
    public final void run() {
        if (this.session != null) {
            this.session = ESBServerContext.indexSession(this.session);
        }

        if (this.getUncaughtExceptionHandler() == null) {
            this.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable ex) {
                    cachedException(ex);
                }
            });
        }

        try {
            if (this.target != null) {
                this.target.run();
            } else {
                this.value = this.doWork();
            }
        } catch (Throwable ex) {
            this.cachedException(ex);
        }
    }

    public V doWork() throws ESBBaseCheckedException {
        return this.value;
    }

    protected final void cachedException(Throwable ex) {
        if (ex instanceof ESBBaseCheckedException) {
            this.ex = (ESBBaseCheckedException) ex;
            processException(this.ex);
        } else if (ex instanceof Exception) {
            this.ex = new ESBBaseCheckedException("PC-00004:计算线程执行过程中发生未知异常", ex);
            processException(this.ex);
        } else if (ex instanceof Error) {
            this.ex = new ESBBaseCheckedException("PC-00003:计算线程执行过程中发生未知错误", ex);
            processException(this.ex);
        }
    }

    public void processException(ESBBaseCheckedException ex) {
    }

    public V getValue() throws ESBBaseCheckedException {
        if (ex != null) {
            throw this.ex;
        }
        return this.value;
    }

    public final boolean isPCThread() {
        return isPCThread;
    }

    public final Object getDescription() {
        return description;
    }

    public final void setDescription(Object description) {
        this.description = description;
    }
}
