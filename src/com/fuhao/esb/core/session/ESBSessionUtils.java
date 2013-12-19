package com.fuhao.esb.core.session;

import com.fuhao.esb.core.component.ESBServerContext;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * package name is  com.fuhao.esb.core.session
 * Created by fuhao on 13-12-9.
 * Project Name esb-java
 */
public class ESBSessionUtils {
    /**
     * 设置SessionID
     */
    public static void setSessionID(String sessionId) {
        ESBServerContext.getSession().setSessionId(sessionId);
    }

    /**
     * 获取SessionID
     */
    public static String getSessionID() {
        return ESBServerContext.getSession().getSessionId();
    }

    /**
     * 获取父SessionID
     */
    public static String getParentSessionID() {
        return ESBServerContext.getSession().getParentSessionId();
    }

    /**
     * 设置操作人员ID
     */
    public static void setUserID(String userID) {
        ESBServerContext.getSession().setUserID(userID);
    }

    /**
     * 获取操作人员人员ID
     */
    public static String getUserID() {
        return ESBServerContext.getSession().getUserID();
    }

    /**
     * 设置组织机构代码
     */
    public static void setOrgID(String orgID) {
        ESBServerContext.getSession().setOrgID(orgID);
    }

    /**
     * 获取组织机构代码
     */
    public static String getOrgID() {
        return ESBServerContext.getSession().getOrgID();
    }

    /**
     * 服务嵌套调用层数
     */
    public static int getServiceCallLevel() {
        return ESBServerContext.getSession().getServiceCallLevel();
    }

    /**
     * 向Session的平台系统临时数据区中放入临时数据
     *
     * @Description 将指定的临时数据放入Session中
     * @param key
     *            需要放入的数据的key
     * @param value
     *            需要放入的数据的value
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object putTempDataIntoSystemContext(Object key, Object value) {
        BaseSession session = ESBServerContext.getSession();
        ConcurrentHashMap<Object, Object> context = session.getSystemContext();
        if (context == null) {
            synchronized (session) {
                context = session.getSystemContext();
                if (context == null) {
                    context = new ConcurrentHashMap<Object, Object>(5);
                    session.setSystemContext(context);
                }
            }
        }
        return context.put(key, value);
    }

    /**
     * 向Session的平台系统临时数据区中放入临时数据
     *
     * @Description 向Session的平台系统临时数据区中放入临时数据
     * @param data
     *            需要放置的数据
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void putTempDataIntoSystemContext(Map<Object, Object> data) {
        if (data == null) {
            return;
        }

        BaseSession session = ESBServerContext.getSession();
        ConcurrentHashMap<Object, Object> context = session.getSystemContext();
        if (context == null) {
            synchronized (session) {
                context = session.getSystemContext();
                if (context == null) {
                    context = new ConcurrentHashMap<Object, Object>(5);
                    session.setSystemContext(context);
                }
            }
        }
        context.putAll(data);
    }

    /**
     * 从Session的平台系统临时数据区中获取放入的临时数据
     *
     * @Description 根据Key获取之前放入Session中的临时数据
     * @param key
     *            要获取的数据对应的key
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object getTempDataIntoSystemContext(Object key) {
        ConcurrentHashMap<Object, Object> conext = ESBServerContext.getSession().getSystemContext();
        if (conext == null) {
            return null;
        } else {
            return conext.get(key);
        }
    }

    /**
     * 从Session的平台系统临时数据区中删除放入的临时数据
     *
     * @Description 从Session的平台系统临时数据区中删除放入的临时数据
     * @param key
     *            需要删除的数据的key
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object removeTempDataFromSystemContext(Object key) {
        ConcurrentHashMap<Object, Object> conext = ESBServerContext.getSession().getSystemContext();
        if (conext == null) {
            return null;
        } else {
            return conext.remove(key);
        }
    }

    /**
     * 向Session的应用系统临时数据区中放入临时数据
     *
     * @Description 将指定的临时数据放入Session中
     * @param key
     *            需要放入的数据的key
     * @param value
     *            需要放入的数据的value
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object putTempDataIntoApplicationContext(Object key, Object value) {
        BaseSession session = ESBServerContext.getSession();
        ConcurrentHashMap<Object, Object> context = session.getApplicationContext();
        if (context == null) {
            synchronized (session) {
                context = session.getApplicationContext();
                if (context == null) {
                    context = new ConcurrentHashMap<Object, Object>(5);
                    session.setApplicationContext(context);
                }
            }
        }
        return context.put(key, value);
    }

    /**
     * 向Session的应用系统临时数据区中放入临时数据
     *
     * @Description 将指定的临时数据放入Session中
     * @param data
     *            需向放入的数据
     */
    public static void putTempDataIntoApplicationContext(Map<Object, Object> data) {
        if (data == null) {
            return;
        }

        BaseSession session = ESBServerContext.getSession();
        ConcurrentHashMap<Object, Object> context = session.getApplicationContext();
        if (context == null) {
            synchronized (session) {
                context = session.getApplicationContext();
                if (context == null) {
                    context = new ConcurrentHashMap<Object, Object>(5);
                    session.setApplicationContext(context);
                }
            }
        }
        context.putAll(data);
    }

    /**
     * 从Session的应用系统临时数据区中获取放入的临时数据
     *
     * @Description 根据Key获取之前放入Session中的临时数据
     * @param key
     *            需要获取的数据对应的key
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object getTempDataIntoApplicationContext(Object key) {
        ConcurrentHashMap<Object, Object> conext = ESBServerContext.getSession().getApplicationContext();
        if (conext == null) {
            return null;
        } else {
            return conext.get(key);
        }
    }

    /**
     * 从Session的应用系统临时数据区中删除放入的临时数据
     *
     * @Description 根据Key删除之前放入Session中的临时数据
     * @param key
     *            需要删除的数据对应的key
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object removeTempDataFromApplicationContext(Object key) {
        ConcurrentHashMap<Object, Object> conext = ESBServerContext.getSession().getApplicationContext();
        if (conext == null) {
            return null;
        } else {
            return conext.remove(key);
        }
    }

    /**
     * 向Session中放入临时数据
     *
     * @Description 将指定的临时数据放入Session中
     * @param key
     *            需要放入的数据对应的key
     * @param value
     *            需要放入的数据对应的value
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object putTempDataInSession(Object key, Object value) {
        BaseSession session = ESBServerContext.getSession();
        ConcurrentHashMap<Object, Object> tempData = session.getTmpData();
        if (tempData == null) {
            synchronized (session) {
                tempData = session.getTmpData();
                if (tempData == null) {
                    tempData = new ConcurrentHashMap<Object, Object>(3);
                    session.setTmpData(tempData);
                }
            }
        }
        return tempData.put(key, value);
    }

    /**
     * 向Session中放入临时数据
     *
     * @Description 将指定的临时数据放入Session中
     * @param data
     *            需要放入的数据
     */
    public static void putTempDataInSession(Map<Object, Object> data) {
        if (data == null) {
            return;
        }

        BaseSession session = ESBServerContext.getSession();
        ConcurrentHashMap<Object, Object> tempData = session.getTmpData();
        if (tempData == null) {
            synchronized (session) {
                tempData = session.getTmpData();
                if (tempData == null) {
                    tempData = new ConcurrentHashMap<Object, Object>(3);
                    session.setTmpData(tempData);
                }
            }
        }
        tempData.putAll(data);
    }

    /**
     * 从Session中获取放入的临时数据
     *
     * @Description 根据Key获取之前放入Session中的临时数据
     * @param key
     *            需要获取的数据对应的key
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object getTempDataFromSession(Object key) {
        ConcurrentHashMap<Object, Object> tempData = ESBServerContext.getSession().getTmpData();
        if (tempData == null) {
            return null;
        }
        return tempData.get(key);
    }

    /**
     * 从Session中获删除入的临时数据
     *
     * @Description 根据Key删除之前放入Session中的临时数据
     * @param key
     *            需要删除的数据对应的key
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object removeTempDataFromSession(Object key) {
        ConcurrentHashMap<Object, Object> tempData = ESBServerContext.getSession().getTmpData();
        if (tempData == null) {
            return null;
        }
        return tempData.remove(key);
    }

    /**
     * 向Session中放入本地临时数据. 不进行跨域传输.
     *
     * @Description 将指定的临时数据放入Session中
     * @param key
     *            需要向Session中放入的数据的Key
     * @param value
     *            需要向Session中放入的数据的value
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object putLocalTempDataInSession(Object key, Object value) {
        BaseSession session = ESBServerContext.getSession();
        ConcurrentHashMap<Object, Object> tempData = session.getLocalTempData();
        if (tempData == null) {
            synchronized (session) {
                tempData = session.getLocalTempData();
                if (tempData == null) {
                    tempData = new ConcurrentHashMap<Object, Object>(3);
                    session.setLocalTempData(tempData);
                }
            }
        }
        return tempData.put(key, value);
    }

    /**
     * 向Session中放入本地临时数据. 不进行跨域传输.
     *
     * @Description 将指定的临时数据放入Session中
     * @param data
     *            需要向Session中放入的数据
     */
    public static void putLocalTempDataInSession(Map<Object, Object> data) {
        if (data == null) {
            return;
        }

        BaseSession session = ESBServerContext.getSession();
        ConcurrentHashMap<Object, Object> tempData = session.getLocalTempData();
        if (tempData == null) {
            synchronized (session) {
                tempData = session.getLocalTempData();
                if (tempData == null) {
                    tempData = new ConcurrentHashMap<Object, Object>(3);
                    session.setLocalTempData(tempData);
                }
            }
        }
        tempData.putAll(data);
    }

    /**
     * 从Session中获取放入的本地临时数据
     *
     * @Description 根据Key获取之前放入Session中的本地临时数据
     * @param key
     *            需要获取的数据对应的Key
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object getLocalTempDataFromSession(Object key) {
        ConcurrentHashMap<Object, Object> tempData = ESBServerContext.getSession().getLocalTempData();
        if (tempData == null) {
            return null;
        }
        return tempData.get(key);
    }

    /**
     * 从Session中删除存入的本地临时数据
     *
     * @Description 根据Key删除之前放入Session中的本地临时数据
     * @param key
     *            需要删除的数据对应的Key
     * @return Object 返回指定key对应的数据，如果指定的key没有对应的数据，则为null值
     */
    public static Object removeLocalTempDataFromSession(Object key) {
        ConcurrentHashMap<Object, Object> tempData = ESBServerContext.getSession().getLocalTempData();
        if (tempData == null) {
            return null;
        }
        return tempData.remove(key);
    }

    /**
     * 获取请求经过的服务器列表
     *
     * @Description 获取请求经过的服务器列表
     * @return String[] 返回请求经过的服务器列表数组
     */
    public static String[] getServers() {
        return ESBServerContext.getSession().getServers();
    }

    /**
     * 拷贝当前Session数据生成新的Sessoin
     *
     * @Description 根据当前Session的相关业务数据生成新的Session对象
     * @return BaseSession 返回值为BaseSession
     */
    public static BaseSession copySessionData() {
        return new BaseSession(ESBServerContext.getSession(), true);
    }

    /**
     * 获取服务调用堆栈
     */
    public static Stack<String> getServiceCallStack() {
        return ESBServerContext.getSession().getServiceCallStack();
    }

    /**
     * 获取父服务名
     *
     * @Description 获取父服务名
     * @return String 返回值为父服务名
     */
    public static String getParentServiceName() {
        Stack<String> stack = ESBServerContext.getSession().getServiceCallStack();
        if (stack.isEmpty()) {
            return null;
        } else {
            return stack.peek();
        }
    }

    /**
     * 获取当前交易的启动时间
     */
    public static long getStartTime() {
        return ESBServerContext.getSession().getStartTime();
    }
}
