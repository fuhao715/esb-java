package com.fuhao.esb.core.inbound.aip;

import com.fuhao.esb.common.apimessage.Service;
import com.fuhao.esb.common.request.ESBAipAccessMessageReq;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.ESBAipReturnMessageRes;
import com.fuhao.esb.common.response.IESBReturnMessage;
import com.fuhao.esb.common.xml.IXmlPackageTransformer;
import com.fuhao.esb.common.xml.IntegratedPlatformUtil;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.inbound.aip
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class AipXmlTransformer implements IXmlPackageTransformer {

    @Override
    public IESBAccessMessage xml2Bean(String xmlMessage,boolean bRequest) throws ESBBaseCheckedException {
        // XmlValidatorUtil.validateXMLByXSD(com.css.ESB.esb.comm.request.Constants.MESSAGE_YYJC,xmlMessage);
        try {
            Service service = IntegratedPlatformUtil.stringToService(xmlMessage);
            if (bRequest){
                ESBAipAccessMessageReq req= new ESBAipAccessMessageReq();
                req.setService(service);
                return req;
            }else{
                ESBAipReturnMessageRes res= new ESBAipReturnMessageRes();
                res.setService(service);
                return res;
            }
        } catch (Exception e) {
            ESBLogUtils.getLogger(this.getClass()).error(e.getMessage(),e);
            ESBBaseCheckedException ex = new ESBBaseCheckedException("ESB02GG002014",e);//xml转对象出错
            throw ex;
        }
    }

    @Override
    public String bean2XML(IESBAccessMessage beanMessage) throws ESBBaseCheckedException{
        try {
            Service service = null;
            if (beanMessage instanceof IESBReturnMessage)//返回报文bean
                service = ((ESBAipReturnMessageRes)beanMessage).getService();
            else
                service = ((ESBAipAccessMessageReq)beanMessage).getService();
            return IntegratedPlatformUtil.serviceToString(service);
        } catch (Exception e) {
            ESBLogUtils.getLogger(this.getClass()).error(e.getMessage(),e);//对象转xml出错
            throw new ESBBaseCheckedException("ESB02GG002015",e);
        }
    }
}
