package com.fuhao.esb.common.xml;

import com.fuhao.esb.common.request.IESBAccessMessage;

/**
 * package name is  com.fuhao.esb.common.xml
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public interface IXmlPackageTransformer {
    /**
     *
     *@name    xml转对象
     *@Description 相关说明
     *@param xml
     *@param request 是否请求报文还是返回报文
     */
    IESBAccessMessage xml2Bean(String xml,boolean request) throws Exception;
    /**
     * 对象转xml
     */
    String bean2XML(IESBAccessMessage bean) throws Exception;
}
