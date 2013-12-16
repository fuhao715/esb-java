package com.fuhao.esb.core.exception;
import com.fuhao.esb.core.component.utils.ESBDateUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * package name is  com.fuhao.esb.core.exception
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class ExceptionInfos {


    private static final LinkedList<String> exceptions = new LinkedList<String>();

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    private static int maxExceptionCount = 50;

    static synchronized void setMaxExceptionCount(int maxExceptionCount) {
        ExceptionInfos.maxExceptionCount = maxExceptionCount;
    }

    static synchronized int getMaxExceptionCount() {
        return ExceptionInfos.maxExceptionCount;
    }

    public static boolean addException(Throwable ex) {
        if (maxExceptionCount <= 0 || ex == null) {
            return false;
        }

        try {
            lock.writeLock().lock();

            while (exceptions.size() >= maxExceptionCount) {
                exceptions.removeFirst();
            }

            StringWriter writer = new StringWriter();
            writer.append(new StringBuilder("[").append(ESBDateUtils.toDateStrByFormatIndex(ESBDateUtils.getSystemCurrentTime(), 0))
                    .append("] "));
            ex.printStackTrace(new PrintWriter(writer));
            exceptions.add(writer.getBuffer().toString());
        } finally {
            lock.writeLock().unlock();
        }
        return true;
    }

    public static String queryLastExceptions() {
        if (maxExceptionCount <= 0) {
            return null;
        }

        try {
            lock.readLock().lock();

            StringBuilder exceptions = new StringBuilder();
            for (String ex : ExceptionInfos.exceptions) {
                exceptions.append(ex);
            }
            return exceptions.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void clearLastExceptions() {
        if (maxExceptionCount <= 0) {
            return;
        }

        try {
            lock.writeLock().lock();
            exceptions.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
