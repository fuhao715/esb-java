package com.fuhao.esb.core.component.service;
import com.fuhao.esb.core.component.ESBComponentRef;
import com.fuhao.esb.core.component.ESBPlatformConstrants;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.classScanner.ESBServiceInfo;
import com.fuhao.esb.core.component.classScanner.IESBServiceManager;
import com.fuhao.esb.core.component.classScanner.IESBServiceProxy;
import com.fuhao.esb.core.component.classScanner.filter.*;
import com.fuhao.esb.core.component.utils.CheckUtils;
import com.fuhao.esb.core.component.utils.ClassUtils;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.component.utils.SystemUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.exception.ESBExceptionParser;
import com.fuhao.esb.core.session.BaseSession;
import com.fuhao.esb.core.session.ESBSessionUtils;

import java.util.List;
import java.util.Stack;
/**
 * package name is  com.fuhao.esb.core.service
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public class ESBServiceUtils {

    private static final ESBLogUtils log = ESBLogUtils.getLogger(ESBServiceUtils.class);

    /**
     * 明确协议的服务调用
     *
     * @Description 此种服务调用方式不会跳过服务路由表查找，直接使用指定的协议进行服务调用<br>
     *              注：此方法为平台内部使用，业务系统不使用此方法调用服务 <br>
     *              示例：
     *
     *              <pre>
     * &#064;ServiceContainer
     * public class XXXX {
     * 	&#064;service(serviceName=&quot;myservice&quot;)
     * 	public Object service(args1,args2,...){
     * 	.......
     * 	}
     * }
     *
     * Object value = ESBServiceUtils.callServiceByProtocolID(&quot;协议ID&quot;,&quot;myservice&quot;,args1,args2......);
     * </pre>
     * @param protocolID
     *            协议ID，此参数值应与远程服务配置的目标协议ID为准
     * @param serviceName
     *            需要调用的服务名，服务名一般为服务方法名，如果服务注解上特殊定义了服务名，则以注解参数做为服务名，如
     *            <code>@Service(serviceName="Service1")</code> 明确标识服务名为Service1
     * @param args
     *            服务调用参数，参数为变长数组对象，如果服务没有任何形参，则无须给出此参数
     * @return Object 服务执行结果，如果服务方法定义为void无返回值，则调用此类服务器返回值永远为null
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2012-1-14下午1:27:59
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Object callServiceByProtocolID(String protocolID, String serviceName, Object... args) throws ESBBaseCheckedException {
        LocalInvocationSender sender = new LocalInvocationSender();
        boolean isLocalCall = true;

        BaseSession session = ESBServerContext.getSession();
        IMediationRequest req = createServiceRequest(isLocalCall, serviceName, args);
        IMediationResponse res;

        // 本地服务调用时再执行拦截器等操作，交由本地服务调用接口执行相关操作
        if (isLocalCall) {
            res = sender.send(protocolID, req);
            return res == null ? null : res.getResponseValue();
        }

        try {
            // 进行执行线程状态检查
            SystemUtils.checkThreadIsInterrupted();

            // 调用服务前项拦截器
            runAllBeforeFilter(null, serviceName, args);

            // 开始调用远程服务
            res = sender.send(protocolID, req);

            // 合并Session信息,本地服务调用不支持Session合并操作
            mergeSessionContext(session, res);

            // 调用服务前项拦截器
            runAllAfterFilter(null, serviceName, args, res == null ? null : res.getResponseValue());
        } catch (ESBBaseCheckedException ex) {
            // 调用服务异常拦截器
            runAllExceptionFilter(null, serviceName, args, ex);
            throw ex;
        } catch (Exception e) {
            ESBBaseCheckedException ex = null;
            if (CheckUtils.checkESBPlatformClass(e.getStackTrace(), false)) {
                ex = new ESBBaseCheckedException("SM-00024:服务总线组件执行过程中抛出异常:" + e.getMessage(), e);
            } else {
                ex = ESBExceptionParser.processException(e);
            }
            // 调用服务异常拦截器
            runAllExceptionFilter(null, serviceName, args, ex);
            throw ex;
        } finally {
            // 进行执行线程状态检查
            SystemUtils.checkThreadIsInterrupted();
        }

        return res == null ? null : res.getResponseValue();
    }

    /**
     * 合并Session上下文信息
     *
     * @Description 相关说明
     * @Time 创建时间:2012-4-16上午8:48:35
     * @param session
     * @param res
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private static void mergeSessionContext(BaseSession session, IMediationResponse res) {
        ESBSessionUtils.putTempDataIntoSystemContext(res.getSystemContext());
        ESBSessionUtils.putTempDataIntoApplicationContext(res.getApplicationContext());
        ESBSessionUtils.putTempDataInSession(res.getTmpData());
    }

    /**
     * 本地服务调用
     *
     * @Description 此种调用方式不会进行交易路由，即只会查找本地的服务注册表，并调用服务<br>
     *              示例：
     *
     *              <pre>
     * &#064;ServiceContainer
     * public class XXXX {
     * 	&#064;service(serviceName=&quot;myservice&quot;)
     * 	public Object service(args1,args2,...){
     * 	.......
     * 	}
     * }
     *
     * Object value = ESBServiceUtils.callLocalService(&quot;myservice&quot;,args1,args2......);
     * </pre>
     *
     * @param serviceName
     *            需要调用的服务名，服务名一般为服务方法名，如果服务注解上特殊定义了服务名，则以注解参数做为服务名，如
     *            <code>@Service(serviceName="Service1")</code> 明确标识服务名为Service1
     * @param args
     *            服务调用参数，参数为变长数组对象，如果服务没有任何形参，则无须给出此参数
     * @return Object 服务执行结果，如果服务方法定义为void无返回值，则调用此类服务器返回值永远为null
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2012-1-14下午1:35:31
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Object callService(String serviceName, Object... args) throws ESBBaseCheckedException {
        BaseSession session = ESBServerContext.getSession();

        if (ESBComponentRef.serviceManager == null) {
            throw new ESBBaseCheckedException("SM-00016:服务管理器未注册，不能执行本地服务调用，请确认平台是否成功启动");
        }

        if (session.getProtocolType() == null) {
            return callServiceByProtocolID(ESBPlatformConstrants.ESB_LOCAL_INVOKE_SENDER, serviceName, args);
        }

        ESBServiceContainer serviceContainer = ESBComponentRef.serviceManager.findServiceContainerByServieName(serviceName);

        if (serviceContainer == null) {
            throw new ESBBaseCheckedException("SM-00017:没有找到服务" + serviceName);
        }

        return callLocalServiceByServiceContainer(serviceContainer, serviceName, args);
    }

    /**
     * 指定服务容器的本地服务调用
     *
     * @Description 此种调用方式不会进行交易路由，直接调用服务容器对象中的服务对象实例执行服务<br>
     *              注：此方法为平台内部使用，业务系统不使用此方法调用服务<br>
     *              示例：
     *
     *              <pre>
     * &#064;ServiceContainer
     * public class XXXX {
     * 	&#064;service(serviceName=&quot;myservice&quot;)
     * 	public Object service(args1,args2,...){
     * 	.......
     * 	}
     * }
     *
     * Object value = ESBServiceUtils.callLocalServiceByServiceContainer(服务容器对象,&quot;myservice&quot;,args1,args2......);
     * </pre>
     * @param serviceContainer
     *            服务容器对象
     * @param serviceName
     *            需要调用的服务名，服务名一般为服务方法名，如果服务注解上特殊定义了服务名，则以注解参数做为服务名，如
     *            <code>@Service(serviceName="Service1")</code> 明确标识服务名为Service1
     * @param args
     *            服务调用参数，参数为变长数组对象，如果服务没有任何形参，则无须给出此参数
     * @return Object 服务执行结果，如果服务方法定义为void无返回值，则调用此类服务器返回值永远为null
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2012-1-14下午1:37:04
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static Object callLocalServiceByServiceContainer(ESBServiceContainer serviceContainer, final String serviceName,
                                                            Object... args) throws ESBBaseCheckedException {
        final IESBServiceManager serviceManager = ESBComponentRef.serviceManager;
        final boolean checkNotThrowException = ESBServerContext.isDevelopMode() && ESBServerContext.isCheckNotThrowException();
        boolean notThrowException = false;
        Object value;

        // 进行执行线程状态检查
        SystemUtils.checkThreadIsInterrupted();

        if (serviceContainer == null) {
            serviceContainer = serviceManager.findServiceContainerByServieName(serviceName);
            if (serviceContainer == null) {
                    throw new ESBBaseCheckedException("SM-00017:没有找到服务" + serviceName);
            }
        }

        // 清除之前放入的上下文的异常索引
        if (checkNotThrowException) {
            ESBSessionUtils.removeTempDataFromApplicationContext(ESBPlatformConstrants.ESB_NOT_THROW_EXCEPTION);
        }

        final IESBServiceProxy serviceProxy = serviceContainer.getServiceProxy();
        Class<?> clazz = serviceProxy.getClass();

        // 开发模式下启动调用安全性检查
        if (ESBServerContext.isDevelopMode()) {
            // 检查服务调用合法性
            doServiceCallSecurityCheck(serviceProxy, serviceName);
        }

        if (args == null) {
            args = new Object[] { null };
        }

        // 处理服务调用参数默认值
        if (args.length < serviceContainer.serviceInfo.methodParameterCount) {
            args = serviceContainer.serviceInfo.processServiceCallParameter(args);
        }

        try {

            // 调用服务前项拦截器
            runAllBeforeFilter(clazz, serviceName, args);

            // 服务调用入栈
            final Stack<String> serviceCallStack = ESBSessionUtils.getServiceCallStack();
            try {
                // 服务调用入栈
                serviceCallStack.push(serviceName);

                 // 调用本地服务方法
                    value = serviceProxy.callService(serviceName, serviceContainer.getMethodIndex(), args, serviceContainer);

            } finally {
                // 服务调用出栈
                if (serviceCallStack != null && !serviceCallStack.isEmpty()) {
                    serviceCallStack.pop();
                }
            }

            // 标记未抛出异常退出
            notThrowException = true;

            // 调用服务后项拦截器
            runAllAfterFilter(clazz, serviceName, args, value);
        } catch (Throwable e) {
            ESBBaseCheckedException ex = ESBExceptionParser.processException(e);
            // 调用服务异常拦截器
            runAllExceptionFilter(clazz, serviceName, args, ex);
            throw ex;
        } finally {
                // 检查服务执行过程中是否存在生成了异常对象但不抛出的情况
                if (checkNotThrowException) {
                    Throwable e = (Throwable) ESBSessionUtils
                            .removeTempDataFromApplicationContext(ESBPlatformConstrants.ESB_NOT_THROW_EXCEPTION);
                    if (notThrowException && e != null) {
                        log.warn("SM-00025:服务" + serviceName + "执行过程中创建了异常对象，但并未抛出，资源管理器将按照无异常情况进行处理", e);
                    }
                }
        }

        // 进行执行线程状态检查
        SystemUtils.checkThreadIsInterrupted();

        return value;
    }
    /**
     * 构造服务请求对象
     *
     * @Description 此方法用于构造调用请求的缺省Request<br>
     *              注：此方法为平台内部使用，业务系统不使用此方法调用服务
     * @param isCallLocalService
     *            是否使用本地服务调用接口执行服务：<br>
     *            true : 当服务与调用者一定部署并运行在同一个JVM内时，此值设置为true，可以提升服务调用性能 <br>
     *            false : 当服务可能与调用者运行在不同的JVM实例上时，此值设置为false，系统会自动查找服务路由，并启动相关序列化检查
     * @param serviceName
     *            需要调用的服务名，服务名一般为服务方法名，如果服务注解上特殊定义了服务名，则以注解参数做为服务名，如
     *            <code>@Service(serviceName="Service1")</code> 明确标识服务名为Service1
     * @param args
     *            服务调用参数，参数为变长数组对象，如果服务没有任何形参，则无须给出此参数
     * @return DefaultMediationRequest 缺省调用请求对象
     * @Time 创建时间:2013-1-14下午1:45:42
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static DefaultMediationRequest createServiceRequest(boolean isCallLocalService, String serviceName, Object... args) {
        BaseSession session = ESBServerContext.getSession();
        DefaultMediationRequest req;
        req = new DefaultMediationRequest();
        req.setServiceName(serviceName);
        req.setServiceParam(args);
        req.setSessionID(session.getSessionId());
        req.setUserID(session.getUserID());
        req.setOrgID(session.getOrgID());
        req.setSystemContext(session.getSystemContext());
        req.setApplicationContext(session.getApplicationContext());
        req.setTmpData(session.getTmpData());
        req.setServers(session.getServers());
        req.setLocalService(isCallLocalService);
        return req;
    }

    /**
     * 执行服务调用安全性检查
     */
    private static void doServiceCallSecurityCheck(IESBServiceProxy serviceProxy, String serviceName) throws ESBBaseCheckedException {
        List<ESBServiceCallSecurityInfo> securityInfos = ESBServerContext.getServicecallcheckinfo();
        if (securityInfos == null || securityInfos.isEmpty()) {
            return;
        }

        final StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        final String sourceClassAndMethodName = caller.getClassName() + "." + caller.getMethodName();
        final String destinationClassAndServiceName = serviceProxy.getService().getClass().getName() + "." + serviceName;
        boolean hasSecurityInfo = false;

        // 查找调用类的安全信息
        for (ESBServiceCallSecurityInfo securityInfo : ESBServerContext.getServicecallcheckinfo()) {
            hasSecurityInfo = securityInfo.source.matcher(sourceClassAndMethodName).matches();
            if (hasSecurityInfo) {
                if (!securityInfo.perimitDestination.matcher(destinationClassAndServiceName).matches()) {
                    throw new ESBBaseCheckedException("SM-00022:服务调用安全检查不允许" + sourceClassAndMethodName + "不调用" + serviceName + "服务");
                }
            }
        }

        if (!hasSecurityInfo) {
            throw new ESBBaseCheckedException("SM-00023:没有找到" + sourceClassAndMethodName + "相关的调用安全性检查信息，无法进行服务调用");
        }
    }

    /**
     * 查找服务容器类实例
     *
     * @Description 注：此方法为平台内部使用，业务系统不使用此方法调用服务
     * @param serviceName
     *            需要调用的服务名，服务名一般为服务方法名，如果服务注解上特殊定义了服务名，则以注解参数做为服务名，如
     *            <code>@Service(serviceName="Service1")</code> 明确标识服务名为Service1
     * @return IESBServiceProxy 服务代理对象
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2012-1-14下午1:54:10
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static IESBServiceProxy findServiceProxy(String serviceName) throws ESBBaseCheckedException {
        ESBServiceContainer serviceContainer = ESBComponentRef.serviceManager.findServiceContainerByServieName(serviceName);
        if (serviceContainer == null) {
            return null;
        }
        return serviceContainer.getServiceProxy();
    }

    /**
     * 获取服务类文件的物理路径
     *
     * @Description 此方法用于获取服务方法所在的类文件在物理存储的绝对路径，方法服务的查找
     * @param serviceName
     *            需要调用的服务名，服务名一般为服务方法名，如果服务注解上特殊定义了服务名，则以注解参数做为服务名，如
     *            <code>@Service(serviceName="Service1")</code> 明确标识服务名为Service1
     * @return String 服务方法所在类在物理存储的绝对路径
     * @throws ESBBaseCheckedException
     * @Time 创建时间:2012-2-11上午9:38:20
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String findServiceClassAddress(String serviceName) throws ESBBaseCheckedException {
        IESBServiceProxy proxy = findServiceProxy(serviceName);
        if (proxy == null) {
            return null;
        }
        return ClassUtils.findClass(proxy.getService().getClass());
    }

    /**
     * 获取已注册的本地服务数量
     *
     * @Description 获取已注册的本地服务数量
     * @return int 获取已注册的本地服务数量
     * @Time 创建时间:2012-1-14下午2:16:53
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static int getServiceCount() {
        return ESBComponentRef.serviceManager.getServiceCount();
    }

    // ===================服务拦截器管理方法====================================================================

    /**
     * 注册服务执行前拦截器
     *
     * @Description 此方法用于注册一个服务调用前拦截器，以使得服务在开始前可以被拦截器捕获
     * @param filter
     *            拦截器对象，当服务执行前系统会自动调用拦截器的相关方法
     * @param index
     *            拦截器位置，用于自定义拦截器的在服务调用前拦截器链上的位置，<0时表示放入拦截器链尾部
     * @Time 创建时间:2012-1-14下午2:18:03
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void registerBeforeFilter(IESBServiceBeforeFilter filter, int index) {
        ESBComponentRef.serviceManager.getServiceFilterManager().registerBeforeFilter(filter, index);
    }

    /**
     * 注销服务执行前拦截器
     *
     * @Description 此方法用于注销已注册的服务调用前拦截器
     * @param filter
     *            拦截器对象
     * @Time 创建时间:2012-1-14下午2:18:03
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void unRegisterBeforeFilter(IESBServiceBeforeFilter filter) {
        ESBComponentRef.serviceManager.getServiceFilterManager().unRegisterBeforeFilter(filter);
    }

    /**
     * 注册服务执行后拦截器
     *
     * @Description 此方法用于注册一个服务调用后拦截器，以使得服务在结束后可以被拦截器捕获
     * @param filter
     *            拦截器对象，当服务执行执行完成后系统会自动调用拦截器的相关方法
     * @param index
     *            拦截器位置，用于自定义拦截器的在服务执行完成后拦截器链上的位置，<0时表示放入拦截器链尾部
     * @Time 创建时间:2012-1-14下午2:18:03
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void registerAfterFilter(IESBServiceAfterFilter filter, int index) {
        ESBComponentRef.serviceManager.getServiceFilterManager().registerAfterFilter(filter, index);
    }

    /**
     * 注销服务执行后拦截器
     *
     * @Description 此方法用于注销已注册的服务调用前拦截器
     * @param filter
     *            拦截器对象
     * @Time 创建时间:2012-1-14下午2:18:03
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void unRegisterAfterFilter(IESBServiceAfterFilter filter) {
        ESBComponentRef.serviceManager.getServiceFilterManager().unRegisterAfterFilter(filter);
    }

    /**
     * 注册服务执行异常拦截器
     *
     * @Description 此方法用于注册一个服务执行抛出异常的的异常拦截器，以使得服务执行过程中抛出异常后可以被拦截器捕获
     * @param filter
     *            拦截器对象，当服务执行执行过程中抛出异常系统会自动调用拦截器的相关方法
     * @param index
     *            拦截器位置，用于自定义拦截器的在服务执行过程中抛出异常后拦截器链上的位置，<0时表示放入拦截器链尾部
     * @Time 创建时间:2012-1-14下午2:18:03
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void registerExceptionFilter(IESBServiceExceptionFilter filter, int index) {
        ESBComponentRef.serviceManager.getServiceFilterManager().registerExceptionFilter(filter, index);
    }

    /**
     * 注销服务执行异常拦截器
     *
     * @Description 注销已注册的服务执行异常拦截器
     * @param filter
     *            拦截器对象
     * @Time 创建时间:2012-1-14下午2:18:03
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static void unRegisterExceptionFilter(IESBServiceExceptionFilter filter) {
        ESBComponentRef.serviceManager.getServiceFilterManager().unRegisterExceptionFilter(filter);
    }

    /**
     * 执行所有注册的服务执行前拦截器
     *
     * @param serviceClass
     * @param serviceName
     * @param args
     * @throws ESBBaseCheckedException
     */
    private static void runAllBeforeFilter(Class<?> serviceClass, String serviceName, Object[] args) throws ESBBaseCheckedException {
        IESBServiceFilterManager manager = ESBComponentRef.serviceManager.getServiceFilterManager();
        if (manager == null) {
            return;
        }
        List<IESBServiceBeforeFilter> filters = manager.getAllBeforeFilter();
        if (filters == null) {
            return;
        }
        for (IESBServiceBeforeFilter filter : filters) {
            filter.before(serviceClass, serviceName, args);
        }
    }

    /**
     * 执行所有注册的服务执行后拦截器
     */
    private static void runAllAfterFilter(Class<?> serviceClass, String serviceName, Object[] args, Object value)
            throws ESBBaseCheckedException {
        IESBServiceFilterManager manager = ESBComponentRef.serviceManager.getServiceFilterManager();
        if (manager == null) {
            return;
        }
        List<IESBServiceAfterFilter> filters = manager.getAllAfterFilter();
        if (filters == null) {
            return;
        }
        for (IESBServiceAfterFilter filter : filters) {
            filter.after(serviceClass, serviceName, args, value);
        }
    }

    /**
     * 执行所有注册的服务执行异常拦截器
     *
     * @param serviceClass
     * @param serviceName
     * @param args
     * @param exception
     * @throws ESBBaseCheckedException
     */
    private static void runAllExceptionFilter(Class<?> serviceClass, String serviceName, Object[] args, ESBBaseCheckedException exception)
            throws ESBBaseCheckedException {
        IESBServiceFilterManager manager = ESBComponentRef.serviceManager.getServiceFilterManager();
        if (manager == null) {
            return;
        }
        List<IESBServiceExceptionFilter> filters = manager.getAllExceptionFilter();
        if (filters == null) {
            return;
        }
        for (IESBServiceExceptionFilter filter : filters) {
            filter.exception(serviceClass, serviceName, args, exception);
        }
    }

    /**
     * 获取所有本地服务的注册信息
     *
     * @Description 相关说明
     * @return
     * @Time 创建时间:2013-7-4上午9:54:39
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static List<ESBServiceInfo> getAllServiceInfo() {
        return ESBComponentRef.serviceManager.getAllServiceInfo();
    }
}
