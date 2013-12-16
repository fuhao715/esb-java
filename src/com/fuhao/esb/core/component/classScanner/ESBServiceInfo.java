package com.fuhao.esb.core.component.classScanner;
import com.fuhao.esb.core.annotation.Service;
import com.fuhao.esb.core.component.classloader.Parameter;
import com.fuhao.esb.core.component.utils.ClassUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/**
 * package name is  com.fuhao.esb.core.component.classScanner
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class ESBServiceInfo {
    // -----------------------------------------------服务代理类生成完成后保留的信息-----------------------------------------
    public final Byte securityLevel;
    public final String memo;
    public final String deprecatedInfo;
    public final String serviceClassName;
    public final String methodName;
    public final String serviceName;
    public final String serviceShortName;
    public final byte methodIndex;
    public final String[] methodParameters;
    private Object[] methodParameterDefaultValues;
    private int[] noPrimitiveParamDefaultValueIndex;
    public final String returnType;
    public final String version;
    public final boolean autoShare;
    public final boolean unitTest;
    public final int methodParameterCount;
    public final boolean isStatic;

    public ESBServiceInfo(final String serviceName, final Object service, final Method method, final Service methodAnnotation,
                            byte methodIndex, final boolean forcedCacheResult) throws ESBBaseCheckedException {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final String returnValueType = method.getReturnType().getName();
        final boolean hasReturnValue = !"void".equals(returnValueType);
        this.serviceClassName = service.getClass().getName();
        this.methodIndex = methodIndex;
        this.methodName = method.getName();
        this.version = methodAnnotation.version();
        this.serviceShortName = serviceName;
        if (this.version == null || "".equals(this.version)) {
            this.serviceName = this.serviceShortName;
        } else {
            this.serviceName = this.serviceShortName + "$" + this.version;
        }
        this.methodParameterCount = parameterTypes.length;
        this.methodParameters = new String[this.methodParameterCount];
        this.memo = methodAnnotation.memo();
        this.deprecatedInfo = methodAnnotation.deprecated();
        this.autoShare = methodAnnotation.autoShare();
        if (hasReturnValue) {
            this.returnType = returnValueType;
        } else {
            this.returnType = null;
        }
        this.securityLevel = methodAnnotation.securityLevel();
        this.unitTest = methodAnnotation.unitTest();
        this.isStatic = ClassUtils.isStatic(method);

        // 生成服务调用参数造型代码
        for (int i = 0; i < parameterTypes.length; i++) {
            this.methodParameters[i] = parameterTypes[i].getName();
        }

        // 服务类调用参数默认值
        this.parseParameterDefaultValue(method.getParameterAnnotations());
    }

    private void parseParameterDefaultValue(final Annotation[][] annotations) throws ESBBaseCheckedException {
        List<Object> defaultValues = new ArrayList<Object>(this.methodParameterCount);
        List<Integer> noPrimitiveParamDefaultValueIndex = new ArrayList<Integer>();

        for (int i = 0; i < this.methodParameterCount; i++) {
            Object value = null;
            String defaultValue = null;
            boolean hasDefaultValue = false;

            if (annotations[i] != null && annotations[i].length > 0) {
                for (int j = 0; j < annotations[i].length; j++) {
                    if (annotations[i][j].annotationType() == Parameter.class) {
                        defaultValue = ((Parameter) annotations[i][j]).defaultValue();
                        hasDefaultValue = true;
                        break;
                    }
                }
            }

            if (!hasDefaultValue) {
                if (!defaultValues.isEmpty()) {
                    defaultValues.add(null);
                }
                continue;
            }

            // 将参数数组指定位置的元素对象造成成特定类型
            final String type = this.methodParameters[i];
            if ("java.lang.String".equals(type) || "java.lang.Object".equals(type)) {
                value = defaultValue;
            } else if ("int".equals(type) || "java.lang.Integer".equals(type)) {
                if (defaultValue == null || "".equals(defaultValue)) {
                    value = new Integer(0);
                } else {
                    value = Integer.valueOf(defaultValue);
                }
            } else if ("double".equals(type) || "java.lang.Double".equals(type)) {
                if (defaultValue == null || "".equals(defaultValue)) {
                    value = new Double(0.0);
                } else {
                    value = Double.valueOf(defaultValue);
                }
            } else if ("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
                if (defaultValue == null || "".equals(defaultValue)) {
                    value = Boolean.FALSE;
                } else {
                    value = Boolean.valueOf(defaultValue);
                }
            } else if ("long".equals(type) || "java.lang.Long".equals(type)) {
                if (defaultValue == null || "".equals(defaultValue)) {
                    value = new Long(0L);
                } else {
                    value = Long.valueOf(defaultValue);
                }
            } else if ("char".equals(type) || "java.lang.Character".equals(type)) {
                if (defaultValue == null || "".equals(defaultValue)) {
                    value = new Character((char) 0);
                } else {
                    value = defaultValue.charAt(0);
                }
            } else if ("byte".equals(type) || "java.lang.Byte".equals(type)) {
                if (defaultValue == null || "".equals(defaultValue)) {
                    value = new Byte((byte) 0);
                } else {
                    value = Byte.valueOf(defaultValue);
                }
            } else if ("short".equals(type) || "java.lang.Short".equals(type)) {
                if (defaultValue == null || "".equals(defaultValue)) {
                    value = new Short((short) 0);
                } else {
                    value = Short.valueOf(defaultValue);
                }
            } else if ("float".equals(type) || "java.lang.Float".equals(type)) {
                if (defaultValue == null || "".equals(defaultValue)) {
                    value = new Float(0.0f);
                } else {
                    value = Float.valueOf(defaultValue);
                }
            } else {
                if ("".equals(defaultValue)) {
                    value = null;
                } else {
                    value = new ESBServiceParameterCreator(serviceName, (i + 1), defaultValue);
                    noPrimitiveParamDefaultValueIndex.add(i);
                }
            }

            defaultValues.add(value);
        }

        if (defaultValues.isEmpty()) {
            this.methodParameterDefaultValues = new Object[0];
        } else {
            this.methodParameterDefaultValues = defaultValues.toArray();
        }

        if (!noPrimitiveParamDefaultValueIndex.isEmpty()) {
            this.noPrimitiveParamDefaultValueIndex = new int[noPrimitiveParamDefaultValueIndex.size()];
            for (int i = 0; i < noPrimitiveParamDefaultValueIndex.size(); i++) {
                this.noPrimitiveParamDefaultValueIndex[i] = noPrimitiveParamDefaultValueIndex.get(i);
            }
        }
    }

    public final Object[] processServiceCallParameter(Object[] args) throws ESBBaseCheckedException {
        final Object[] newArgs = new Object[methodParameterCount];

        if (args.length == 0 && methodParameterDefaultValues.length == 0) {
            return newArgs;
        }

        // 复制传入的参数
        System.arraycopy(args, 0, newArgs, 0, args.length);

        if (methodParameterDefaultValues.length == 0) {
            return newArgs;
        }

        // 计算默认参数复制位置
        int right = methodParameterCount - args.length;
        if (right > methodParameterDefaultValues.length) {
            right = methodParameterDefaultValues.length;
        }

        // 复制默认参数
        System.arraycopy(methodParameterDefaultValues, methodParameterDefaultValues.length - right, newArgs, methodParameterCount - right,
                right);

        // 创建非基本数据类型参数的默认值对象实例
        if (noPrimitiveParamDefaultValueIndex != null) {
            for (int i : noPrimitiveParamDefaultValueIndex) {
                newArgs[i] = ((ESBServiceParameterCreator) newArgs[i]).newInstanceof();
            }
        }

        return newArgs;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ESBServiceInfo)) {
            return false;
        }
        ESBServiceInfo info = (ESBServiceInfo) obj;
        return info.serviceName.equals(this.serviceName) && info.serviceClassName.equals(this.serviceClassName)
                && info.methodIndex == this.methodIndex;
    }
}
