package com.fuhao.esb.core.exception;

import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.beans.IntrospectionException;
import java.io.IOError;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.BufferOverflowException;
import java.sql.SQLException;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;

/**
 * package name is  com.fuhao.esb.core.component.exception
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBExceptionParser {

    /**
     * 处理捕获到的非ESBBaseCheckException异常对象
     */
    public static ESBBaseCheckedException processException(Throwable e) {
        ESBBaseCheckedException ex = null;
        Throwable subEx = e.getCause();

        if (e instanceof ESBBaseCheckedException) {
            return (ESBBaseCheckedException) e;
        }

        if (e instanceof Exception) {
            if (e instanceof NullPointerException) {
                ex = new ESBBaseCheckedException("SM-01014:系统执行过程中发生空指针异常");
            } else if (e instanceof IndexOutOfBoundsException) {
                ex = new ESBBaseCheckedException("SM-01007:系统执行过程中发生索引超出范围异常:" + e.getMessage());
            } else if (e instanceof NumberFormatException) {
                ex = new ESBBaseCheckedException("SM-01009:系统执行过程中发生数字转换异常，该字符串不能转换为正确的数字:" + e.getMessage());
            } else if (e instanceof NoSuchElementException) {
                ex = new ESBBaseCheckedException("SM-01010:系统执行过程中发生Collection对象遍历时无元素异常:" + e.getMessage());
            } else if (e instanceof EmptyStackException) {
                ex = new ESBBaseCheckedException("SM-01011:系统执行过程在空栈上进行操作时发生异常:" + e.getMessage());
            } else if (e instanceof BufferOverflowException) {
                ex = new ESBBaseCheckedException("SM-01012:系统执行过程中发生缓冲区溢出异常:" + e.getMessage());
            } else if (e instanceof ClassCastException) {
                ex = new ESBBaseCheckedException("SM-01010:系统执行过程中发生类型转换异常:" + e.getMessage());
            } else if (e instanceof RuntimeException) {
                ex = new ESBBaseCheckedException("SM-01016:系统执行过程中抛出未处理的运行时异常:" + e.getMessage());
            } else if (e instanceof IOException) {
                ex = new ESBBaseCheckedException("SM-01016:系统运行过程中发生I/O读写错误:" + e.getMessage());
            } else if (e instanceof SQLException) {
                ex = new ESBBaseCheckedException("SM-01017:系统执行过程中执行SQL调用时发生错误:" + e.getMessage());
            } else if (e instanceof IllegalArgumentException) {
                ex = new ESBBaseCheckedException("SM-01008:系统执行过程中发生参数传递异常，参数不合法或不正确:" + e.getMessage());
            } else if (e instanceof ClassNotFoundException || e instanceof NoClassDefFoundError) {
                ex = new ESBBaseCheckedException("SM-01018:系统执行过程中未找要调用的类，请检查环境部署的程序是否正确:" + e.getMessage());
            } else if (e instanceof NoSuchMethodException) {
                ex = new ESBBaseCheckedException("SM-01005:被调用方法未找到，请检查环境部署的程序是否正确:" + e.getMessage());
            } else if (e instanceof NoSuchFieldException) {
                ex = new ESBBaseCheckedException("SM-01006:被访问字段未找到，请检查环境部署的程序是否正确:" + e.getMessage());
            } else if (e instanceof IllegalClassFormatException) {
                ex = new ESBBaseCheckedException("SM-01019:初始类文件字节无效，请检查调用的类Class文件是否已损坏:" + e.getMessage());
            } else if (e instanceof IntrospectionException) {
                ex = new ESBBaseCheckedException("SM-01020:无法将字符串类名称映射到Class对象，该Class在当前环境没有部署:" + e.getMessage());
            } else if (e instanceof CloneNotSupportedException) {
                ex = new ESBBaseCheckedException("SM-01020:对象不支持克隆操作:" + e.getMessage());
            } else {
                ex = new ESBBaseCheckedException("SM-01002:系统执行过程中抛出非标准异常:" + e.getMessage(), subEx);
            }
        } else if (e instanceof Error) {
            if (e instanceof OutOfMemoryError) {
                ex = new ESBBaseCheckedException("SM-01004:系统运行过程中发生内存溢出错误");
            } else if (e instanceof StackOverflowError) {
                ex = new ESBBaseCheckedException("SM-01013:系统运行过程中发生线程栈溢出错误，请检查程序是否存在死循环问题");
                ex.setStackTrace(e.getStackTrace());
            } else if (e instanceof NoSuchMethodError) {
                ex = new ESBBaseCheckedException("SM-01005:被调用方法未找到，请检查环境部署的程序是否正确:" + e.getMessage());
                ex.setStackTrace(e.getStackTrace());
            } else if (e instanceof NoSuchFieldError) {
                ex = new ESBBaseCheckedException("SM-01006:被访问字段未找到，请检查环境部署的程序是否正确:" + e.getMessage());
            } else if (e instanceof UnknownError) {
                ex = new ESBBaseCheckedException("SM-01014:系统运行过程中Java虚拟机发生未知但严重的错误:" + e.getMessage());
            } else if (e instanceof InternalError) {
                ex = new ESBBaseCheckedException("SM-01015:系统运行过程中Java虚拟机发生内部错误:" + e.getMessage());
            } else if (e instanceof IOError) {
                ex = new ESBBaseCheckedException("SM-01016:系统运行过程中发生I/O读写错误:" + e.getMessage());
            }
        }

        if (ex == null) {
            ex = new ESBBaseCheckedException("SM-01001:系统运行过程中发生严重错误:" + e.getMessage(), subEx);
        }

        ex.setStackTrace(e.getStackTrace());

        return ex;
    }
}
