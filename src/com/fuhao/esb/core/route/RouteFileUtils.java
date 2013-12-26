package com.fuhao.esb.core.route;

import com.fuhao.esb.core.component.AbsESBComponentManager;
import com.fuhao.esb.core.component.ESBPlatformConstrants;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.ESBFileUtils;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.component.utils.ObjectUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 13-12-26.
 * Project Name esb-java
 */
public class RouteFileUtils {
    private ESBLogUtils logger = ESBLogUtils.getLogger(RouteFileUtils.class);
    public Map<String, String> getRouteConfInfor(){
        Map<String, String> parameter = null;
        SAXReader reader = new SAXReader();
        Document doc = null;
        Element rootElement = null;

        try {
            doc = reader.read(new File(ESBFileUtils.getESBRootPath() + "/router.xml"));
            rootElement = doc.getRootElement();
            loadRoutConfigParameters(rootElement);
        } catch (Exception ex) {
            ESBBaseCheckedException e = new ESBBaseCheckedException("路由配置读取失败", ex);
            logger.error(e);
        }
        return parameter;
    }

    private void loadRoutConfigParameters(Element element) throws ESBBaseCheckedException {
        Map<String, Map<String, String>> routConfigParameters = new HashMap<String, Map<String, String>>();
        List<Element> routs = null;

        // 组件配置信息
        routs = element.element("routes").elements();

        logger.info("开始初始化已经注册的" + routs.size() + "个路由");

        // 从配置文件加载组件
        for (Element rout : routs) {
            String name = rout.attributeValue("name");
            String managerClass = rout.attributeValue("component");
            AbsESBComponentManager manager = null;

            // 读取结点的属性信息
            Map<String, String> parameter = ObjectUtils.getParamter(rout);

            // 生成配置信息对象
            routConfigParameters.put(name, parameter);

            // 创建组件管理器实例
            try {
                manager = (AbsESBComponentManager) Class.forName(managerClass).newInstance();
            } catch (Exception ex) {
                logger.error(new ESBBaseCheckedException("PF-00008:创建组件" + name + "的管理器" + managerClass + "实例时发生错误", ex));
                continue;
            }

            // 将组件管理器加入组件管理器索引中

            // 设置组件说明信息
            manager.setMemo(parameter.get("memo"));
        }

    }

}
