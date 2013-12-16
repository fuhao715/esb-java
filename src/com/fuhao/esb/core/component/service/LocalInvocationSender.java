package com.fuhao.esb.core.component.service;

import com.fuhao.esb.common.request.IProtocolConf;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.ClassUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.service
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
public class LocalInvocationSender extends AbsServiceFacade{


    @Override
    public IMediationResponse send(IMediationRequest req) throws ESBBaseCheckedException {
        DefaultMediationResponse response = null;
        boolean callLocalFacade = ESBServerContext.getSession().getProtocolType() == null;
        boolean callLocalService = false;

        if (req instanceof DefaultMediationRequest) {
            callLocalService = ((DefaultMediationRequest) req).isLocalService();
        }

        if (callLocalFacade) {
            if (callLocalService) {
                response = (DefaultMediationResponse) this.send(req, IProtocolConf.ProtocolType.LOCAL, null, true);
            } else {
                try {
                    ClassUtils.checkSerializable(req.getServiceParam(), "开始检查服务调用的参数是否可以进行序列化");
                    response = (DefaultMediationResponse) this.send(req, IProtocolConf.ProtocolType.LOCAL, null, true);
                    ClassUtils.checkSerializable(response.getResponseValue(), "开始检查服务调用的返回值是否可以进行序列化");
                } catch (ESBBaseCheckedException ex) {
                    try {
                        ClassUtils.checkSerializable(ex.getParameters(), "开始检查服务抛出的异常对象携带的参数是否可以进行序列化");
                    } catch (ESBBaseCheckedException e) {
                        e.addException(ex);
                        throw e;
                    }
                    throw ex;
                }
            }
        } else {
            String serviceName = req.getServiceName();
            response = new DefaultMediationResponse();
            response.setSessionID(req.getSessionID());
            response.setResponseValue(ESBServiceUtils.callLocalServiceByServiceContainer(null, serviceName, req.getServiceParam()));
        }
        return response;
    }

    @Override
    public IMediationResponse send(String protocolID, IMediationRequest req) throws ESBBaseCheckedException {
        return send(req);
    }
}
