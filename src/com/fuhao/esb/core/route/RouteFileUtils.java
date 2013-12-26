package com.fuhao.esb.core.route;
import com.fuhao.esb.common.vo.Constants.ROUTE_POLICY;
import com.fuhao.esb.core.component.utils.ESBFileUtils;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-26.
 * Project Name esb-java
 */
public class RouteFileUtils {
    private ESBLogUtils logger = ESBLogUtils.getLogger(RouteFileUtils.class);
    Element rootElement = null;

    public  RouteFileUtils(){
         this.rootElement =   readRouteConfFile();
    }


    public Long getRouteConfVersion(){
        String version = rootElement.attributeValue("version") ;
         return Long.parseLong(version);
    }

    public void setRouteConfVersion(){
        RouteCache.getInstance().setRouteVersion(getRouteConfVersion());
    }

    public Long getRefreshCapacity(){
        String version = rootElement.attributeValue("refreshCapacity") ;
        return Long.parseLong(version);
    }


    public Element readRouteConfFile(){
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(new File(ESBFileUtils.getESBRootPath() + "/router.xml"));
            rootElement = doc.getRootElement();

        } catch (Exception ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("路由配置读取失败", ex);
            logger.error(e);
        }
        return rootElement;
    }




    public void loadRoutConfigParameters() throws ESBBaseCheckedException {

        List<Element> routsRule = null;
        List<Element> routs = null;
        List<Element> protocals = null;
        // 设置路由版本号
        setRouteConfVersion();


        // 组件配置信息
        routsRule = rootElement.element("routePolicyInfos").elements();
        routs = rootElement.element("routes").elements();
        protocals = rootElement.element("protocals").elements();

        logger.info("开始初始化已经注册的" + routs.size() + "个路由");


        // 加载路由规则处理器:
        getRoutesRule(protocals);
        // 路由处理器
        getRouts(routs);
        // 协议处理器
        getprotocals(protocals);
    }

    public Object getParamter2Object(Element element,Object object) throws ESBBaseCheckedException{
        if (element == null) {
            return null;
        }
        Iterator<Attribute> itr = element.attributeIterator();
        while (itr.hasNext()) {
            Attribute attribute = itr.next();
            // parameter.put(attribute.getName(), attribute.getValue());
            try{
            String setMethodName = "set" + attribute.getName().substring(0,1).toUpperCase() + attribute.getName().substring(1);
            Method method = object.getClass().getMethod(setMethodName, new Class[] { String.class });
            if(method ==null){
                    method = object.getClass().getMethod(setMethodName, new Class[] { Boolean.class });
            }
            if(method ==null){
                method = object.getClass().getMethod(setMethodName, new Class[] { ROUTE_POLICY.class });
            }

            method.invoke(object, new Object[] { attribute.getValue() });
            }catch(Exception ex){
               throw new ESBBaseCheckedException("利用反射构造路由信息对象错误");
            }
        }

        return object;
     }


    public void   getRoutesRule(List<Element> routsRule)  throws ESBBaseCheckedException{
        // 从配置文件加载组件
        Map<String, RoutePolicyInfo> ruleConfigParameters = new HashMap<String, RoutePolicyInfo>();
        for (Element rule : routsRule) {
            String id = rule.attributeValue("routeRuleID");
            // 读取结点的属性信息
            RoutePolicyInfo routePolicyInfo = (RoutePolicyInfo)getParamter2Object(rule,new RoutePolicyInfo());

            // 生成配置信息对象
            ruleConfigParameters.put(id, routePolicyInfo);
        }

    }
    public void   getRouts(List<Element> routs){

    }
    public void   getprotocals(List<Element> protocals){

    }


}
