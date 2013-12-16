package com.fuhao.esb.core.component;

import com.fuhao.esb.core.component.classScanner.ESBServiceInfo;
import com.fuhao.esb.core.component.classScanner.IESBServiceProxy;
import com.fuhao.esb.core.component.classloader.ESBClassLoader;
import com.fuhao.esb.core.component.classloader.ESBClassLoaderManager;
import com.fuhao.esb.core.component.log.ESBLogManager;
import com.fuhao.esb.core.component.service.ESBServiceContainer;
import com.fuhao.esb.core.component.utils.CheckUtils;
import com.fuhao.esb.core.component.utils.org.objectweb.asm.ClassWriter;
import com.fuhao.esb.core.component.utils.org.objectweb.asm.Label;
import com.fuhao.esb.core.component.utils.org.objectweb.asm.MethodVisitor;
import com.fuhao.esb.core.component.utils.org.objectweb.asm.Opcodes;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-10.
 * Project Name esb-java
 */
public class ESBServiceProxyClassGenerator implements Opcodes {


    /**
     * 服务代理类名分隔符
     */
    public static final String SERVICE_PROXY = "$$";
    private static final String SERVICE_CONTAINER_CLASS = ESBServiceContainer.class.getName().replace('.', '/');
    private static final String SERVICE_PROXY_SUPER_CLASS = AbsESBServiceProxy.class.getName().replace('.', '/');

    private static final String SERVICE_INFO_CLASS = "com/fuhao/esb/core/component/classScanner/ESBServiceInfo";
    private static final String SERVICE_INFO = "L" + SERVICE_INFO_CLASS + ";";

    private static final String LOG_UTILS_CLASS = "com/fuhao/esb/core/component/utils/ESBLogUtils";
    private static final String LOG_UTILS = "L" + LOG_UTILS_CLASS + ";";

    /**
     * 服务代理类生成方法
     *
     * @Description 相关说明
     * @param loader
     * @param info
     * @return
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2013-2-15下午10:18:48
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static IESBServiceProxy generateCode(ClassLoader loader, final ESBServiceInfo info) throws ESBBaseCheckedException {
        if (ESBServerContext.isDevelopMode()) {
            CheckUtils.callSecurityCheck();
        }

        // 选择服务代理类的类加载器
        if (loader == null || !(loader instanceof ESBClassLoader)) {
            loader = ESBClassLoaderManager.getRootClassLoader();
        }

        Thread.currentThread().setContextClassLoader(loader);

        final String serviceProxyClass = new StringBuilder(info.serviceClassName).append(SERVICE_PROXY).append(info.methodIndex)
                .append(System.nanoTime()).toString();

        try {
            // 生成服务代理类Class的Bytecode
            byte[] bytes = generateCode(info.serviceClassName.replace('.', '/'), serviceProxyClass.replace('.', '/'), info);

            // 加载并实例化服务代理类
            Class<?> proxyClazz = ((ESBClassLoader) loader).findClass(serviceProxyClass, bytes);
            return (IESBServiceProxy) proxyClazz.newInstance();
        } catch (Exception ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("生成服务代理对象时发生错误:" + ex.getMessage());
            e.setStackTrace(ex.getStackTrace());
            throw e;
        }
    }

    /**
     * 服务代理类ASM生成器
     *
     * @Description 相关说明
     * @param serviceClass
     * @param serviceProxyClass
     * @param info
     * @return
     * @throws Exception
     * @Time 创建时间:2013-2-15下午9:22:28
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static byte[] generateCode(final String serviceClass, final String serviceProxyClass, final ESBServiceInfo info)
            throws Exception {
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv;

        // 类定义
        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, serviceProxyClass, null, SERVICE_PROXY_SUPER_CLASS, null);

        // 生成构造方法代码
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, SERVICE_PROXY_SUPER_CLASS, "<init>", "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        // 生成服务方法代码
        {
            // 方法头定义
            // public Object callService(String serviceName, byte methodIndex, Object[] args,
            // ESBServiceContainer container) throws Exception {
            mv = cw.visitMethod(ACC_PUBLIC, "callService", "(Ljava/lang/String;B[Ljava/lang/Object;L" + SERVICE_CONTAINER_CLASS
                    + ";)Ljava/lang/Object;", null, new String[] { "java/lang/Exception" });

            // 方法开始
            mv.visitCode();

            // 服务类对象在本地变量表中的位置
            final int THIS = 0;
            // 方法参数1在本地变量表中的位置
            final int PARAM_SERVICE_NAME = THIS + 1;
            // 方法参数2在本地变量表中的位置
            final int PARAM_METHOD_INDEX = PARAM_SERVICE_NAME + 1;
            // 方法参数3在本地变量表中的位置
            final int PARAM_ARGS = PARAM_METHOD_INDEX + 1;
            // 方法参数4在本地变量表中的位置
            final int PARAM_CONTAINER = PARAM_ARGS + 1;

            // 第1个临时变量在本地变量表中的位置
            final int VAR_1 = PARAM_CONTAINER + 1;
            // 第2个临时变量在本地变量表中的位置
            // final int VAR_2 = VAR_1 + 1;

            // ----------------方法代码开始-----------------------------------------------------------------------------------------------------------

            // 声名第1个临时变量，并赋值为null
            // 例: Object value = null;
            addLineLabel(mv, 1, "Object value = null;");
            mv.visitInsn(ACONST_NULL);
            mv.visitVarInsn(ASTORE, VAR_1);

            // 输出服务调用信息
            if (ESBLogManager.isShowServiceCall()) {
                // 输出被调用服务的位置信息
                addLineLabel(mv, 2, "服务对象.log.debug(......);");
                mv.visitVarInsn(ALOAD, THIS);
                mv.visitFieldInsn(GETFIELD, serviceProxyClass, "log", LOG_UTILS);
                StringBuilder msg = new StringBuilder();
                msg.append("开始调用服务容器").append(info.serviceClassName).append("的").append(info.serviceName).append("服务");
                mv.visitLdcInsn(msg.toString());
                mv.visitMethodInsn(INVOKEVIRTUAL, LOG_UTILS_CLASS, "debug", "(Ljava/lang/Object;)V");

                if (info.deprecatedInfo != null && !"".equals(info.deprecatedInfo.trim())) {
                    // 输出被调用服务的警告信息
                    addLineLabel(mv, 4, "服务对象.log..warn(调用了不在支持的服务);");
                    mv.visitVarInsn(ALOAD, THIS);
                    mv.visitFieldInsn(GETFIELD, serviceProxyClass, "log", LOG_UTILS);
                    mv.visitVarInsn(ALOAD, PARAM_CONTAINER);
                    mv.visitFieldInsn(GETFIELD, SERVICE_CONTAINER_CLASS, "serviceInfo", SERVICE_INFO);
                    mv.visitFieldInsn(GETFIELD, SERVICE_INFO_CLASS, "deprecatedInfo", "Ljava/lang/String;");
                    mv.visitMethodInsn(INVOKEVIRTUAL, LOG_UTILS_CLASS, "warn", "(Ljava/lang/Object;)V");
                }
            }

            // 调用服务类的静态方法或对象方法
            if (info.isStatic) {
                // 调用服务类的静态服务方法
                // 例: Service1.howOldIs(...);
                addLineLabel(mv, 5, "服务类.服务方法(......)");
                mv.visitMethodInsn(INVOKESTATIC, serviceClass, info.methodName,
                        processParam(mv, info.methodParameters, PARAM_ARGS, info.returnType));
            } else {
                // 获取服务代理类的成员变量service，并将其强制造型为对应的服务类，然后将其赋值为第2个临时变量
                // 例:Service1 s = (Service1) this.service;
                addLineLabel(mv, 6, "(服务类)引用对象实例");
                mv.visitVarInsn(ALOAD, THIS);
                mv.visitFieldInsn(GETFIELD, serviceProxyClass, "service", "Ljava/lang/Object;");
                mv.visitTypeInsn(CHECKCAST, serviceClass);

                /**
                 * 优化强制类型转换后到调用对象方法的对象出入栈动作<br>
                 * mv.visitVarInsn(ASTORE, VAR_2); <br>
                 * mv.visitVarInsn(ALOAD, VAR_2);
                 */

                // 调用服务对象的服务方法
                // 例: s.howOldIs(...);
                addLineLabel(mv, 7, "服务对象.服务方法(......);");
                mv.visitMethodInsn(INVOKEVIRTUAL, serviceClass, info.methodName,
                        processParam(mv, info.methodParameters, PARAM_ARGS, info.returnType));
            }

            // 处理服务方法调用返回值
            // value=...
            processReturn(mv, info.returnType, VAR_1, true, 8);

            // 将第1个临时变量的值做为方法返回值
            mv.visitVarInsn(ALOAD, VAR_1);
            mv.visitInsn(ARETURN);

            // ----------------方法代码结束-----------------------------------------------------------------------------------------------------------

            // 自动计算方法的栈长度和本地变量表长度
            mv.visitMaxs(1, 1);

            // 方法结束
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    /**
     * 生成服务方法调用前的参数处理代码，并生成服务调用的参数定义字符串
     */
    private static String processParam(final MethodVisitor mv, final String[] methodParameters, final int PARAM_ARGS,
                                       final String returnType) {
        final StringBuilder methodParam = new StringBuilder("(");

        for (int i = 0; i < methodParameters.length; i++) {
            String type = methodParameters[i];

            // 生成每个参数的调用入栈代码
            mv.visitVarInsn(ALOAD, PARAM_ARGS);
            switch (i) {
                case 0:
                    mv.visitInsn(ICONST_0);
                    break;
                case 1:
                    mv.visitInsn(ICONST_1);
                    break;
                case 2:
                    mv.visitInsn(ICONST_2);
                    break;
                case 3:
                    mv.visitInsn(ICONST_3);
                    break;
                case 4:
                    mv.visitInsn(ICONST_4);
                    break;
                case 5:
                    mv.visitInsn(ICONST_5);
                    break;
                default:
                    mv.visitIntInsn(SIPUSH, i);
                    break;
            }
            mv.visitInsn(AALOAD);

            // 将参数数组指定位置的元素对象造成成特定类型
            if ("int".equals(type)) {
                final char c = 'I';
                mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()" + c);
                methodParam.append(c);
            } else if ("double".equals(type)) {
                final char c = 'D';
                mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()" + c);
                methodParam.append(c);
            } else if ("boolean".equals(type)) {
                final char c = 'Z';
                mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()" + c);
                methodParam.append(c);
            } else if ("long".equals(type)) {
                final char c = 'J';
                mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()" + c);
                methodParam.append(c);
            } else if ("char".equals(type)) {
                final char c = 'C';
                mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()" + c);
                methodParam.append(c);
            } else if ("byte".equals(type)) {
                final char c = 'B';
                mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()" + c);
                methodParam.append(c);
            } else if ("short".equals(type)) {
                final char c = 'S';
                mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()" + c);
                methodParam.append(c);
            } else if ("float".equals(type)) {
                final char c = 'F';
                mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()" + c);
                methodParam.append(c);
            } else {
                type = type.replace('.', '/');
                mv.visitTypeInsn(CHECKCAST, type);
                if (!type.startsWith("L") && !type.startsWith("[")) {
                    type = "L" + type + ";";
                }
                methodParam.append(type);
            }
        }

        methodParam.append(")");

        // 生成服务调用返回值类型字符串
        if (returnType != null && !returnType.equals("void")) {
            if ("int".equals(returnType)) {
                methodParam.append('I');
            } else if ("double".equals(returnType)) {
                methodParam.append('D');
            } else if ("boolean".equals(returnType)) {
                methodParam.append('Z');
            } else if ("long".equals(returnType)) {
                methodParam.append('J');
            } else if ("char".equals(returnType)) {
                methodParam.append('C');
            } else if ("byte".equals(returnType)) {
                methodParam.append('B');
            } else if ("short".equals(returnType)) {
                methodParam.append('S');
            } else if ("float".equals(returnType)) {
                methodParam.append('F');
            } else if (returnType.startsWith("[") || returnType.startsWith("L")) {
                methodParam.append(returnType.replace('.', '/'));
            } else {
                methodParam.append("L").append(returnType.replace('.', '/')).append(";");
            }
        } else {
            methodParam.append("V");
        }

        return methodParam.toString();
    }

    /**
     * 生成服务调用返回值处理代码
     */
    private static void processReturn(final MethodVisitor mv, final String returnType, final int VAR, final boolean doBox, final int line) {
        if (returnType == null) {
            return;
        }

        if (doBox) {
            if (returnType.equals("int")) {
                addLineLabel(mv, 8, "Object value = Integer.valueOf(服务返回结果);");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            } else if (returnType.equals("double")) {
                addLineLabel(mv, 9, "Object value = Double.valueOf(服务返回结果);");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            } else if (returnType.equals("boolean")) {
                addLineLabel(mv, 10, "Object value = Boolean.valueOf(服务返回结果);");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
            } else if (returnType.equals("long")) {
                addLineLabel(mv, 11, "Object value = Long.valueOf(服务返回结果);");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
            } else if (returnType.equals("char")) {
                addLineLabel(mv, 12, "Object value = Character.valueOf(服务返回结果);");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
            } else if (returnType.equals("byte")) {
                addLineLabel(mv, 13, "Object value = Byte.valueOf(服务返回结果);");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
            } else if (returnType.equals("short")) {
                addLineLabel(mv, 14, "Object value = Short.valueOf(服务返回结果);");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
            } else if (returnType.equals("float")) {
                addLineLabel(mv, 15, "Object value = Float.valueOf(服务返回结果);");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
            }
        } else {
            addLineLabel(mv, 16, "Object value = 服务返回结果;");
        }

        mv.visitVarInsn(ASTORE, VAR);
    }

    /**
     * 为服务代理方法中的语句增加行号，方便定位问题
     */
    private static void addLineLabel(final MethodVisitor mv, final int line, final String des) {
        final Label label = new Label() {
            public String toString() {
                return des;
            }
        };
        mv.visitLabel(label);
        mv.visitLineNumber(line, label);
    }
}
