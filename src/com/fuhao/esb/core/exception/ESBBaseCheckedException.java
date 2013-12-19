package com.fuhao.esb.core.exception;

import com.fuhao.esb.core.component.ESBPlatformConstrants;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.TemplateUtils;
import com.fuhao.esb.core.session.ESBSessionUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.exception
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 */
public class ESBBaseCheckedException extends Exception implements IESBBaseException{

    private static final long serialVersionUID = 4540849704711249641L;

    private String innerCode;

    private String message ;

    private List<String> serverNames = new ArrayList<String>(3);

    private List<ESBBaseCheckedException> exceptions;

    private transient Map<String, Object> infoParams;

    private Map<String, Object> parameters;

    private String stackInfo;

    private final Map<String, String> logStorage = new HashMap<String, String>(5);

    private final long time = System.currentTimeMillis();

    public ESBBaseCheckedException(String message) {
        this.message = message;
        this.addServerName();
        processMessage();
        indexThis();
    }
    public ESBBaseCheckedException(Throwable ex) {
        initCause(ex);
        this.addServerName();
        processMessage();
        indexThis();
    }

    public ESBBaseCheckedException(String message, Throwable ex) {
        this.message = message;
        this.addServerName();
        initCause(ex);
        processMessage();
        indexThis();
    }

    public ESBBaseCheckedException( Map<String, Object> infoParams) {
        this.infoParams = infoParams;
        this.addServerName();
        processMessage();
        indexThis();
    }

    public ESBBaseCheckedException(String message, Map<String, Object> infoParams) {
        this.message = message;
        this.infoParams = infoParams;
        this.addServerName();
        processMessage();
        indexThis();
    }

    public ESBBaseCheckedException(Map<String, Object> infoParams, Throwable ex) {
        this.infoParams = infoParams;
        this.addServerName();
        initCause(ex);
        processMessage();
        indexThis();
    }

    public ESBBaseCheckedException(String message, Map<String, Object> infoParams, Throwable ex) {
        this.message = message;
        this.infoParams = infoParams;
        this.addServerName();
        initCause(ex);
        processMessage();
        indexThis();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        if (cause != null) {
            Exception ex = new Exception(cause.getMessage() + "(" + cause.getClass().getName() + ")", cause.getCause());
            ex.setStackTrace(cause.getStackTrace());
            cause = ex;
        }

        super.initCause(cause);

        return super.getCause();
    }
    private void processMessage() {
        StringBuilder str = new StringBuilder();
        String exceptionMsg = null;

        if (this.message == null) {
            this.message = this.innerCode;
            return;
        }
        str.append(this.message);

        if (exceptionMsg != null) {
            str.append(":");
            // 处理参数化的异常提示信息
            if (infoParams != null && !infoParams.isEmpty()) {
                exceptionMsg = TemplateUtils.generateString(exceptionMsg, infoParams);
            }
            str.append(exceptionMsg);
        }
        this.message = str.toString();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
        processMessage();
        this.stackInfo = null;
    }

    public String getExceptionStackInfo() {
        if (stackInfo == null) {
            stackInfo = filterStackTrace();
        }
        // TODO:提示信息加密
        return stackInfo;
    }

    // ---------------------------------------------异常堆栈信息过滤----------------------------------------------------------

    public void printStackTrace(PrintWriter writer) {
        if (stackInfo == null) {
            stackInfo = filterStackTrace();
        }
        synchronized (writer) {
            writer.println(stackInfo);
        }
    }

    public void printStackTrace(PrintStream stream) {
        if (stackInfo == null) {
            stackInfo = filterStackTrace();
        }
        synchronized (stream) {
            stream.println(stackInfo);
        }
    }

    /**
     * 异常堆栈过滤规则
     *
     * @return
     */
    String getFilter() {
        return null;
    }

    /**
     * 过滤异常堆栈信息
     *
     * @return
     */
    public String filterStackTrace() {
        if (stackInfo != null) {
            return stackInfo;
        }

        StringBuffer exMsg = new StringBuffer(this.getClass().getName()).append(": ").append(this.message).append("\r\n")
                .append(this.innerCode);
        // 增加会话ID信息
        exMsg.append("\r\n本次请求会话ID:").append(ESBServerContext.getSession().getSessionId());

        if (this.exceptions == null || this.exceptions.isEmpty()) {
            getExceptionStackMessage(exMsg);
        } else {
            for (ESBBaseCheckedException ex : exceptions) {
                exMsg.append("\r\n 子异常:").append(ex.filterStackTrace());
            }
        }

        return exMsg.toString();
    }

    /**
     * 获取异常堆栈信息
     */
    private void getExceptionStackMessage(StringBuffer exMsg) {
        boolean doFilter = false;
        String filter = getFilter();

        // 增加处理服务器信息
        if (!serverNames.isEmpty()) {
            exMsg.append("\r\n当前应用服务器:").append(this.serverNames.get(0));
            exMsg.append("\r\n本次请求经过的应用服务器:");
            for (String serverName : this.serverNames) {
                exMsg.append("\r\n\t").append(serverName);
                String localLogFile = this.logStorage.get(serverName);
                if (localLogFile != null) {
                    exMsg.append("[ ").append(localLogFile).append(" ]");
                }
            }
        }

        exMsg.append("\r\n当前服务器执行耗时(ms):").append(this.time - ESBSessionUtils.getStartTime());

        exMsg.append("\r\n异常堆栈信息：");

        StackTraceElement[] stes = this.getStackTrace();

        // 判断抛出异常的类是否为在要进行异常过滤的包中
        if (filter != null && stes[0].getClassName().startsWith(filter)) {
            doFilter = true;
        }

        for (StackTraceElement ste : stes) {
            String clazz = ste.getClassName();
            if (doFilter && !clazz.startsWith(filter) && !clazz.startsWith(ESBPlatformConstrants.ESB_PACKAGE_HEAD)) {
                continue;
            }
            exMsg.append("\r\n\tat ").append(clazz).append(".").append(ste.getMethodName()).append("(").append(ste.getFileName())
                    .append(" : ").append(ste.getLineNumber()).append(")");
        }

        // 处理嵌套异常
        Throwable cause = this.getCause();
        while (cause != null) {
            if (cause instanceof ESBBaseCheckedException) {
                exMsg.append("\r\nCaused by: ").append(((ESBBaseCheckedException) cause).filterStackTrace());
            } else {
                exMsg.append("\r\nCaused by: ").append(cause.getClass().getName()).append(" ").append(cause.getMessage());
                for (StackTraceElement ste : cause.getStackTrace()) {
                    exMsg.append("\r\n\tat ").append(ste.getClassName()).append(".").append(ste.getMethodName()).append("(")
                            .append(ste.getFileName()).append(" : ").append(ste.getLineNumber()).append(")");
                }
            }
            cause = cause.getCause();
        }
    }

    public List<String> getServerNames() {
        return serverNames;
    }

    private void addServerName() {
        for (String server : ESBServerContext.getSession().getServers()) {
            this.serverNames.add(server);
        }
    }

    public synchronized void addException(ESBBaseCheckedException ex) {
        if (this.exceptions == null) {
            this.exceptions = new LinkedList<ESBBaseCheckedException>();
        }

        this.exceptions.add(ex);
    }

    public List<ESBBaseCheckedException> getAllExceptions() {
        return this.exceptions;
    }

    public final void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public final Map<String, Object> getParameters() {
        return this.parameters;
    }

    public void reverseInfo() {
        Throwable cause = this.getCause();
        if (cause != null && cause instanceof ESBBaseCheckedException) {
            ESBBaseCheckedException e = (ESBBaseCheckedException) cause;

            // 翻转提示信息
            e.setMessage(this.getMessage());

            // 翻转服务器列表
            List<String> serverNames = this.serverNames;
            this.serverNames = ((ESBBaseCheckedException) cause).serverNames;
            ((ESBBaseCheckedException) cause).serverNames = serverNames;
        }
    }

    public void setLogStorage(String logStorage) {
        this.logStorage.put(ESBServerContext.getServerName(), logStorage);
    }

    public Type getType() {
        return Type.SYSTEM;
    }

    public long getTime() {
        return this.time;
    }

    protected void indexThis() {
        if (ESBServerContext.isDevelopMode() && ESBServerContext.isCheckNotThrowException()) {
            ESBSessionUtils.putTempDataIntoApplicationContext(ESBPlatformConstrants.ESB_NOT_THROW_EXCEPTION, this);
        }

    }

    @Override
    @Deprecated
    public void addThisServerName() {
    }


    public static ESBBaseCheckedException generate(String head, Throwable e) {
        ESBBaseCheckedException ex = new ESBBaseCheckedException(head + e.getMessage());
        ex.setStackTrace(e.getStackTrace());
        return ex;
    }
}
