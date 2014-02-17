package com.fuhao.esb.adapter.ws;

import com.fuhao.esb.common.request.ESBAipAccessMessageReq;
import com.fuhao.esb.common.response.ESBAipReturnMessageRes;

/**
 * Created by fuhao on 13-12-6.
 */
public class BaseESBWebServiceImpl implements IBaseESBWebService{
    @Override
    public ESBAipReturnMessageRes sendBaseBeanEsbWebService(ESBAipAccessMessageReq request) {
        return null;
    }

    @Override
    public String sendBaseXMLEsbWebService(String request) {
        return null;
    }

    @Override
    public Object sendBaseObjectEsbWebService(String tranID, Object request) {
        return null;
    }
}
