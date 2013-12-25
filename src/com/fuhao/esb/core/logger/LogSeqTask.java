package com.fuhao.esb.core.logger;

import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.vo.TransSeqVO;

/**
 * package name is  com.fuhao.esb.core.logger
 * Created by fuhao on 13-12-25.
 * Project Name esb-java
 */
public class LogSeqTask implements Runnable {

    ESBLogUtils logger = ESBLogUtils.getLogger(this.getClass());

    private TransSeqVO po;

    public LogSeqTask(TransSeqVO po) {
        super();
        this.po = po;
    }

    public void run() { //todo po持久化
       /* try {


        } catch (ESBBaseCheckedException ex) {
            logger.error(ex);
        }*/
    }
}
