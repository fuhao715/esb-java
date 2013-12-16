package com.fuhao.esb.core.component.service;

import com.fuhao.esb.common.request.IProtocolConf;
import com.fuhao.esb.core.component.ESBComponentRef;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.exception.ESBExceptionParser;
import com.fuhao.esb.core.session.BaseSession;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component.service
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
public abstract class AbsServiceFacade implements IMessageSender{


    private static final ESBLogUtils log = ESBLogUtils.getLogger(AbsServiceFacade.class);

    final protected IMediationResponse send(IMediationRequest req, IProtocolConf.ProtocolType protocolType,
                                            Map<String, Object> parameter, boolean isLocalService) throws ESBBaseCheckedException {
        if (ESBComponentRef.serviceManager == null) {
            throw new ESBBaseCheckedException("平台未启动，请稍候重试!");
        }

        BaseSession session = ESBServerContext.getSession().clear();
        loadSession(session, req, protocolType);

        try {
            String serviceName = req.getServiceName();
            Object value = null;
            if (isLocalService) {
                    value = ESBServiceUtils.callLocalServiceByServiceContainer(null, serviceName,
                            req.getServiceParam());
             }


            DefaultMediationResponse response = new DefaultMediationResponse();
            response.setSessionID(req.getSessionID());
            response.setResponseValue(value);

            // 同步ESBsession
            response.setSystemContext(ESBServerContext.getSession().getSystemContext());
            response.setApplicationContext(ESBServerContext.getSession().getApplicationContext());
            response.setTmpData(ESBServerContext.getSession().getTmpData());



            return response;
        } catch (Throwable e) {
            ESBBaseCheckedException ex = ESBExceptionParser.processException(e);
            log.error(ex);
            throw ex;
        } finally {
            session.setProtocolType(null);
            session.setLocalTempData(null);
        }
    }

    @Override
    public IMediationResponse send(String protocolID, IMediationRequest req) throws ESBBaseCheckedException {
        return this.send(req);
    }

    private static void loadSession(BaseSession session, IMediationRequest req, IProtocolConf.ProtocolType protocolType) {
        session.setSessionId(req.getSessionID());
        session.setProtocolType(protocolType);
        session.setUserID(req.getUserID());
        session.setOrgID(req.getOrgID());
        session.setSystemContext(req.getSystemContext());
        session.setApplicationContext(req.getApplicationContext());
        session.setTmpData(req.getTmpData());
        session.setServers(req.getServers());
        session.addThisServer();
    }

}
