package com.fuhao.esb.core.logger;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.vo.Constants;

/**
 * package name is  com.fuhao.esb.core.logger
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public interface IESBSeqLogger {
    public void logTransaction(Constants.SEQLOG_TYPE type,IESBAccessMessage message) ;
}
