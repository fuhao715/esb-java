package com.fuhao.esb.core.component.utils;

import com.fuhao.esb.core.component.classloader.ESBClassLoaderManager;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * package name is  com.fuhao.esb.core.component.utils
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public class ObjectUtils {
    /**
     * 基于MD5的一致性Hash算法
     */
    public static long compatHashByMD5(Object key) throws ESBBaseCheckedException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ESBBaseCheckedException("UT-09008:不支持MD5算法");
        }
        md5.reset();
        if (key instanceof String) {
            md5.update(((String) key).getBytes());
        } else {
            md5.update(objectToByteArray(key));
        }
        byte[] bKey = md5.digest();
        long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8)
                | (long) (bKey[0] & 0xFF);

        return res;
    }

    /**
     * Java对象序列化到byte数组
     */
    public static byte[] objectToByteArray(Object obj) throws ESBBaseCheckedException {
        if (obj == null) {
            return null;
        }

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1536);
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-06011:序列化对象到byte数组时发生错误", ex);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                    oos = null;
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-06011:序列化对象到byte数组时发生错误", ex);
                }
            }
        }
        return baos.toByteArray();
    }

    /**
     * byte数组反序列化到Java对象
     */
    public static Object byteArrayToObject(byte[] bytes) throws ESBBaseCheckedException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        ObjectInputStream ois = null;
        Object object = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            object = ois.readObject();
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("UT-06012:反序列化byte数组为对象时发生错误", ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                    ois = null;
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-06012:反序列化byte数组为对象时发生错误", ex);
                }
            }
        }
        return object;
    }

    /**
     * Java对象序列化到byte数组
     */
    public static byte[] objectToByteArrayByGZIP(Object obj) throws ESBBaseCheckedException {
        if (obj == null) {
            return null;
        }

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1536);
        try {
            GZIPOutputStream gos = new GZIPOutputStream(baos);
            oos = new ObjectOutputStream(gos);
            oos.writeObject(obj);
            oos.flush();
            gos.finish();
        } catch (IOException ex) {
            throw new ESBBaseCheckedException("UT-06011:序列化对象到byte数组时发生错误", ex);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                    oos = null;
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-06011:序列化对象到byte数组时发生错误", ex);
                }
            }
        }
        return baos.toByteArray();
    }

    /**
     * 从压缩数据组反序列号对象
     */
    public static Object byteArrayToObjectByGZIP(byte[] array) throws ESBBaseCheckedException {
        if (array == null || array.length == 0) {
            return null;
        }

        ObjectInputStream ois = null;
        ByteArrayInputStream baos = new ByteArrayInputStream(array);
        Object object = null;
        try {
            ois = new ObjectInputStream(new GZIPInputStream(baos));
            object = ois.readObject();
        } catch (Exception ex) {
            throw new ESBBaseCheckedException("UT-06014:从压缩的byte数组反序列化对象时发生错误", ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                    ois = null;
                } catch (IOException ex) {
                    throw new ESBBaseCheckedException("UT-06014:从压缩的byte数组反序列化对象时发生错误", ex);
                }
            }
        }
        return object;
    }

    /**
     * 获取结点的属性信息
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getParamter(Element element) {
        if (element == null) {
            return new HashMap<String, String>();
        }

        Map<String, String> parameter = new HashMap<String, String>();
        Iterator<Attribute> itr = element.attributeIterator();
        while (itr.hasNext()) {
            Attribute attribute = itr.next();
            parameter.put(attribute.getName(), attribute.getValue());
        }

        return parameter;
    }

    /**
     * 对象拷贝
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T copy(T source) throws ESBBaseCheckedException {
        T desc = null;
        if (desc instanceof List) {
            desc = (T) copyList((List) source);
        } else if (desc instanceof Set) {
            desc = (T) copySet((Set) source);
        } else if (desc instanceof Map) {
            desc = (T) copyMap((Map) source);
        } else {
            desc = (T) copyObject(source);
        }
        return desc;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List copyList(List source) throws ESBBaseCheckedException {
        List desc = null;

        if (source instanceof ArrayList) {
            desc = new ArrayList(source.size());
        } else {
            desc = new LinkedList();
        }

        for (Object obj : source) {
            desc.add(copy(obj));
        }

        return desc;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Set copySet(Set source) throws ESBBaseCheckedException {
        Set desc = null;

        if (source instanceof HashSet) {
            desc = new HashSet(source.size());
        } else {
            desc = new LinkedHashSet(source.size());
        }

        for (Object obj : source) {
            desc.add(copy(obj));
        }

        return desc;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Map copyMap(Map source) throws ESBBaseCheckedException {
        Map desc = null;

        if (source instanceof LinkedHashMap) {
            desc = new LinkedHashMap(source.size());
        } else {
            desc = new HashMap(source.size());
        }

        for (Object entry : source.entrySet()) {
            Object key = ((Map.Entry) entry).getKey();
            Object value = ((Map.Entry) entry).getValue();
            if (value != null) {
                value = copyObject(value);
            }
            desc.put(key, value);
        }
        return desc;
    }

    @SuppressWarnings("rawtypes")
    private static Object copyObject(Object value) throws ESBBaseCheckedException {
        if (value instanceof Map) {
            value = copyMap((Map) value);
        } else if (value instanceof Calendar) {
            value = ((Calendar) value).clone();
        }else if (value instanceof java.util.Date) {
            value = ((java.util.Date) value).clone();
        } else if (value instanceof List) {
            value = copyList((List) value);
        }else {
            throw new ESBBaseCheckedException("UT-15001:不支持的类型" + value.getClass() + "的对象拷贝");
        }
        return value;
    }

    /**
     * Java对象转JSON串
     */
    public static void toJSONString(Object object, StringBuilder json, boolean isNullConvertToEmptyString, int dateFormat,
                                    boolean allToString, String... lowerCaseColumns) throws ESBBaseCheckedException {
        Set<String> set = new HashSet<String>(lowerCaseColumns == null ? 0 : lowerCaseColumns.length);
        if(lowerCaseColumns != null) {
            for(String column:lowerCaseColumns) {
                set.add(column);
            }
        }
        toJSONString(object, json, isNullConvertToEmptyString, dateFormat, allToString, set);
    }
    public static void toJSONString(Object object, StringBuilder json, boolean isNullConvertToEmptyString, int dateFormat,
                                    boolean allToString, Set<String> lowerCaseColumns) throws ESBBaseCheckedException {
        if (object == null) {
            if (isNullConvertToEmptyString) {
                json.append("\"\"");
            } else {
                if (allToString) {
                    json.append("");
                } else {
                    json.append("null");
                }
            }
            return;
        }

        if (object instanceof String || object instanceof java.lang.Character) {
            String str = "" + object;
            StringBuilder subStr = new StringBuilder(str.length() + 5);
            subStr.append("\"");
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '\\') {
                    subStr.append("\\\\");
                } else if (c == '\n') {
                    subStr.append("\\n");
                } else if (c == '\r') {
                    subStr.append("\\r");
                } else if (c == '\"') {
                    subStr.append("\\\"");
                } else {
                    subStr.append(c);
                }
            }
            subStr.append("\"");
            json.append(subStr);
        } else if (object instanceof Number || object.getClass().isPrimitive() || object instanceof Boolean) {
            if (allToString) {
                json.append("\"").append(object).append("\"");
            } else {
                json.append(object);
            }
        } else if (object instanceof java.util.Date) {
            json.append("\"").append(ESBDateUtils.toDateStrByFormatIndex((java.util.Date) object, dateFormat)).append("\"");
        } else if (object instanceof java.util.Calendar) {
            json.append("\"").append(ESBDateUtils.toDateStrByFormatIndex((java.util.Calendar) object, dateFormat)).append("\"");
        } else if (object instanceof Collection<?>) {
            boolean hasOne = false;
            json.append("[");
            for (Object sub : (Collection<?>) object) {
                hasOne = true;
                toJSONString(sub, json, isNullConvertToEmptyString, dateFormat, allToString, lowerCaseColumns);
                json.append(",");
            }
            if (hasOne) {
                json.setLength(json.length() - 1);
            }
            json.append("]");
        } else if (object instanceof Map<?, ?>) {
            boolean hasOne = false;
            json.append("{");
            for (Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                hasOne = true;
                if(entry.getKey() instanceof String && lowerCaseColumns.contains(entry.getKey())) {
                    toJSONString(entry.getKey().toString().toLowerCase(), json, isNullConvertToEmptyString, dateFormat, allToString, lowerCaseColumns);
                } else {
                    toJSONString(entry.getKey(), json, isNullConvertToEmptyString, dateFormat, allToString, lowerCaseColumns);
                }
                json.append(":");
                toJSONString(entry.getValue(), json, isNullConvertToEmptyString, dateFormat, allToString, lowerCaseColumns);
                json.append(",");
            }
            if (hasOne) {
                json.setLength(json.length() - 1);
            }
            json.append("}");
        } else if (object.getClass().isArray()) {
            boolean hasOne = false;
            int length = Array.getLength(object);
            json.append("[");
            for (int idx = 0; idx < length; idx++) {
                hasOne = true;
                toJSONString(Array.get(object, idx), json, isNullConvertToEmptyString, dateFormat, allToString, lowerCaseColumns);
                json.append(",");
            }
            if (hasOne) {
                json.setLength(json.length() - 1);
            }
            json.append("]");
        } else {
            toJSONString(object.toString(), json, isNullConvertToEmptyString, dateFormat, allToString, lowerCaseColumns);
        }
    }

    /**
     * 异常信息转为字符串
     */
    public static String exceptionToString(Throwable t) {
        StringWriter writer = new StringWriter(512);
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    /**
     * 方法反射调用
     */
    public static Object invokeMethod(Object obj, String methodName, Object... param) throws ESBBaseCheckedException {
        Class<?> clazz = obj instanceof Class ? (Class<?>) obj : obj.getClass();
        Method method = null;
        Class<?>[] parameterTypes = null;

        try {
            if (param != null && param.length > 0) {
                parameterTypes = new Class[param.length];
                for (int i = 0; i < param.length; i++) {
                    parameterTypes[i] = param[i].getClass();
                }
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
            } else {
                method = clazz.getDeclaredMethod(methodName);
            }
        } catch (SecurityException ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("调用方法" + methodName + "时发生安全权限问题:" + ex.getMessage());
            e.setStackTrace(ex.getStackTrace());
            throw e;
        } catch (NoSuchMethodException ex) {
        }

        try {
            Object value = method.invoke(obj instanceof Class ? null : obj, param);
            return value;
        } catch (IllegalArgumentException ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("调用方法" + methodName + "时发生安全权限问题:" + ex.getMessage());
            e.setStackTrace(ex.getStackTrace());
            throw e;
        } catch (IllegalAccessException ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("调用方法" + methodName + "时发生安全权限问题:" + ex.getMessage());
            e.setStackTrace(ex.getStackTrace());
            throw e;
        } catch (InvocationTargetException ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("调用方法" + methodName + "时发生安全权限问题:" + ex.getCause().getMessage());
            e.setStackTrace(ex.getCause().getStackTrace());
            throw e;
        }
    }

    /**
     * 动态调用类的方法
     *
     * @Description 相关说明
     * @param b
     * @param className
     * @param methodName
     * @param args
     * @return
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2013-7-19下午9:45:04
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Object callMethod(byte[] b, String className, String methodName, Map<String, Object> args)
            throws ESBBaseCheckedException {
        Class<?> clazz = null;

        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex1) {
            if (b == null || b.length == 0) {
                throw new ESBBaseCheckedException("类字节码不允许为空");
            }

            try {
                clazz = ESBClassLoaderManager.getRootClassLoader().findClass(className, b);
            } catch (Throwable e) {
                throw new ESBBaseCheckedException("生成" + className + "的Class对象时发生错误", e);
            }
        }

        Object obj;
        try {
            obj = clazz.newInstance();
        } catch (Throwable e) {
            throw new ESBBaseCheckedException("生成" + className + "的对象实例 时发生错误", e);
        }

        Method method;
        try {
            method = clazz.getMethod(methodName, Map.class);
        } catch (SecurityException ex) {
            throw new ESBBaseCheckedException("获取类" + className + "的方法" + methodName + "对象 时违反安全检查规则");
        } catch (NoSuchMethodException ex) {
            throw new ESBBaseCheckedException("类" + className + "中不存在" + methodName + "(Map)方法");
        }

        try {
            return method.invoke(obj, args);
        } catch (IllegalArgumentException ex) {
            throw new ESBBaseCheckedException("获取类" + className + "的方法" + methodName + "方法参数不是Map<String,Object");
        } catch (IllegalAccessException ex) {
            throw new ESBBaseCheckedException("获取类" + className + "的方法" + methodName + "不是public");
        } catch (InvocationTargetException ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("类" + className + "的方法" + methodName + "方法执行时抛出异常:"
                    + ex.getCause().getMessage());
            e.setStackTrace(ex.getStackTrace());
            throw e;
        }
    }
}
