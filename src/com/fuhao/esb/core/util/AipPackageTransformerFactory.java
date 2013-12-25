package com.fuhao.esb.core.util;

import com.fuhao.esb.common.xml.packagetransformer.IAipPackageTransform;
import com.fuhao.esb.common.xml.packagetransformer.TransformByMatchcharacters;
import com.fuhao.esb.common.xml.packagetransformer.TransformByXstream;
import com.fuhao.esb.core.component.ESBComponentManager;

/**
 * package name is  com.fuhao.esb.core.util
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class AipPackageTransformerFactory {
    /**
     * 集成平台技术报文转换器
     */
    private static IAipPackageTransform aipPackageTransform  ;
    public static IAipPackageTransform getPackageTransformer() {
        if(aipPackageTransform==null)
        {
            //如果sword.xml中指定了使用字符方式解析则使用字符匹配解析，否则默认用XStream解析
            String transformerType= ESBComponentManager.getInitMapParameter().get("aipPackageTransformer");;//ESBSystemParameterUtil.getESBComponentParameter("aipPackageTransformer");
            if("MatchStr".equalsIgnoreCase(transformerType))
            {
                aipPackageTransform = new TransformByMatchcharacters();
            }else
            {
                aipPackageTransform = new TransformByXstream();
            }
            //未知情况下默认使用XStream解析
            //aipPackageTransform = new TransformByXstream();
        }
        return aipPackageTransform;
    }
}
