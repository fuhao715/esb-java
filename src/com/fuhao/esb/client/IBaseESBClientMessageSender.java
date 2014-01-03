package com.fuhao.esb.client;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.response.IESBReturnMessage;

/**
 * package name is  com.fuhao.esb.client
 * Created by fuhao on 14-1-2.
 * Project Name esb-java
 */
public interface IBaseESBClientMessageSender {
    /**
     *@name    对象方式接入
     *@Description 相关说明
     *@Time    创建时间:2012-3-12下午07:32:15
     *@author fuhao
     *@param message
     *@return
     *@throws Exception
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public IESBReturnMessage send(IESBAccessMessage message)throws Exception;

    /**
     * XML 接入接口
     *
     * @param xml xml字符串
     * @return xml字符串
     * @throws Exception
     */
    String sendXML(String xml) throws Exception;

    IXmlMessageReceiver getReceiver() throws Exception;
}
