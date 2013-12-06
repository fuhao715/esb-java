package com.fuhao.esb.adapter.ws;

/**
 * Created by fuhao on 13-12-6.
 */
import com.fuhao.esb.common.request.ESBAipAccessMessageReq;
import com.fuhao.esb.common.response.ESBAipReturnMessageRes;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService(name = "EsbBaseWebService", targetNamespace = "http://www.fuhao.net/spec/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface IBaseESBWebService {
    @WebMethod
    @WebResult(name = "reqbaseBean", targetNamespace = "http://www.fuhao.net/spec/", partName = "response")
    public ESBAipReturnMessageRes sendBaseBeanEsbWebService(
            @WebParam(name = "reqbaseBean", targetNamespace = "http://www.fuhao.net/spec/", partName = "request")
            ESBAipAccessMessageReq request);

    @WebMethod
    @WebResult(name = "reqbaseXml", targetNamespace = "http://www.fuhao.net/spec/", partName = "response")
    public String sendBaseXMLEsbWebService(
            @WebParam(name = "reqbaseXml", targetNamespace = "http://www.fuhao.net/spec/", partName = "request")
            String request);
}

