package com.fuhao.esb.core.transaction;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.common.xml.IXmlPackageTransformer;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.logger.BaseMessageLogger;

/**
 * package name is  com.fuhao.esb.core.transaction
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class BaseTransManager {
    /**
     * XML解释转换器
     */
    private IXmlPackageTransformer xmlTransformer = null;

    public IXmlPackageTransformer getXmlPackageTransformer() {
        return xmlTransformer;
    }

    public void setXmlPackageTransformer(IXmlPackageTransformer xmlTransformer) {
        this.xmlTransformer = xmlTransformer;
    }





    public IESBReturnMessage dealTransaction(IESBAccessMessage message) throws ESBBaseCheckedException {
        BaseMessageLogger logger = new BaseMessageLogger(message,this.getClass());



        IESBReturnMessage response = null;



        return response;
    }

}
