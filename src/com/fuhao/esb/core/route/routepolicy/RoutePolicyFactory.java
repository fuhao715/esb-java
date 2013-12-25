package com.fuhao.esb.core.route.routepolicy;

import com.fuhao.esb.common.vo.Constants.ROUTE_POLICY;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * package name is  com.fuhao.esb.core.route.routepolicy
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class RoutePolicyFactory {
    /**
     * 路由规则处理器map
     */
    private static Map<String, IRoutePolicyHandler> mapRouteHandler = new ConcurrentHashMap<String, IRoutePolicyHandler>();

    private static ESBLogUtils log = ESBLogUtils
            .getLogger(RoutePolicyFactory.class);

    /**
     *
     *@name 获取路由策略处理器
     *@Description 相关说明
     *@Time 创建时间:2011-12-15上午10:04:51
     *@param handlerName
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static IRoutePolicyHandler getRoutePolicyHandler(String handlerName)throws ESBBaseCheckedException{
        if (handlerName == null || handlerName.isEmpty())
            return null;
        IRoutePolicyHandler handler = mapRouteHandler.get(handlerName);
        if (handler != null)
            return handler;
        if (handlerName.equalsIgnoreCase(ROUTE_POLICY.FullName.name()))// 根据开始规则去匹配
            handler = new FullnameRouteLocateHandler();
        else if (handlerName.equalsIgnoreCase(ROUTE_POLICY.StartWith.name()))// 根据开始规则去匹配
            handler = new StartwithRouteLocateHandler();
        else if (handlerName.equalsIgnoreCase(ROUTE_POLICY.EndWith.name())) //根据结尾规则匹配
            handler = new EndwithRouteLocateHandler();
        else if (handlerName.equalsIgnoreCase(ROUTE_POLICY.Regex.name()))// 根据正则表达式去匹配
            handler = new RegexRouteLocateHandler();
        else if (handlerName.equals(ROUTE_POLICY.User_defined.name())){// 用户自定义路由策略
            try {
                handler = (IRoutePolicyHandler) Class.forName(handlerName).newInstance();
            } catch (InstantiationException e1) {
                log.error("路由规则处理器"+handlerName+"实例化时出现问题："+e1.getMessage(),e1);
                throw new ESBBaseCheckedException("路由规则处理器"+handlerName+"没有实例化类："+e1.getMessage(),e1);
            } catch (IllegalAccessException e1) {
                log.error("路由规则处理器"+handlerName+"非法方法实例："+e1.getMessage(),e1);
                throw new ESBBaseCheckedException("路由规则处理器"+handlerName+"没有实例化类："+e1.getMessage(),e1);
            } catch (ClassNotFoundException e1) {
                log.error("路由规则处理器"+handlerName+"没有实例化类："+e1.getMessage(),e1);
                throw new ESBBaseCheckedException("路由规则处理器"+handlerName+"没有实例化类："+e1.getMessage(),e1);
            }
        }else if (handlerName.equalsIgnoreCase(ROUTE_POLICY.Dynamic.name())){//根据机关策略匹配
            handler = new DynamicRouteLocateHandler();
        }else if(handlerName.equalsIgnoreCase(ROUTE_POLICY.Any.name())){// 根据任意去匹配
            handler = new AnyRouteLocateHandler();
        }else{
            throw new ESBBaseCheckedException("路由规则处理器未定义");
        }
        mapRouteHandler.put(handlerName, handler);
        return handler;
    }

}
