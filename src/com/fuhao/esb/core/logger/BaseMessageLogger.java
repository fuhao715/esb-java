package com.fuhao.esb.core.logger;

import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.logger
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class BaseMessageLogger {

    /**
     * 交易统一消息
     */
    private String transMessage;
    /**
     * 日志器
     */
    private ESBLogUtils logger ;//

    public BaseMessageLogger(IESBAccessMessage message,Class<?> clazz) {
        if(null != clazz){
            logger = ESBLogUtils.getLogger(clazz);
        }else {
            logger = ESBLogUtils.getLogger(this.getClass());
        }

        StringBuffer logInfo=new StringBuffer();
        logInfo.append("---交易码")
                .append(message.getTranID())
                .append("、交易流水")
                .append(message.getTranSeq())
                .append("、渠道系统")
                .append(message.getChannelID())
                .append("、目标")
                .append(message.getDestinationID());
        transMessage =logInfo.toString() ;
    }

    /**
     *
     *@name 输出debug级别信息
     *@Description 相关说明
     *@Time 创建时间:2011-12-21上午09:53:20
     *@param info
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void debug(String info) {
        logger.debug(info,transMessage);
    }

    /**
     *
     *@name 输出info级别信息
     *@Description 相关说明
     *@Time 创建时间:2011-12-21上午09:53:08
     *@param info
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void info(String info) {
        logger.info(info,transMessage);
    }

    /**
     *
     *@name 输出error级别信息
     *@Description 相关说明
     *@Time 创建时间:2011-12-21上午09:53:26
     *@param info
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public void error(String info) {
        logger.error(info,transMessage);
    }

    public void error(String msg,Throwable thr){
        logger.error(msg,transMessage);
        if (thr !=null)
            logger.error(thr);
    }

    public ESBBaseCheckedException handleException(String message, Throwable throwable) {
		/*打印异常码和异常信息 */
        StringBuffer logInfo= new StringBuffer();
        logInfo.append("---").append(message);
        error(logInfo.toString(), throwable);
        ESBBaseCheckedException ex = null;
        if (throwable != null)
            ex = new ESBBaseCheckedException(message,throwable);
        else
            ex = new ESBBaseCheckedException(message);
        return ex;
    }

    public ESBBaseCheckedException handleExceptionReason(String message,Map<String,Object> reason){
        ESBBaseCheckedException ex = new ESBBaseCheckedException(message,reason);
        StringBuffer logInfo=new StringBuffer();
        logInfo.append(message).append(":").append(ex.getMessage());
        error(logInfo.toString());
        ex.setReasonMap(reason);
        return ex;
    }

    public ESBBaseCheckedException handleExceptionReason(String message,Map<String,Object> reason, Throwable throwable){
        ESBBaseCheckedException ex = null;
        if (throwable != null)
            ex = new ESBBaseCheckedException(message,reason,throwable);
        else
            ex = new ESBBaseCheckedException(message,reason);
        StringBuffer logInfo=new StringBuffer();
        logInfo.append(message).append(":").append(ex.getMessage());
        error(logInfo.toString());
        ex.setReasonMap(reason);
        return ex;
    }
}

