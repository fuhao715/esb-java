package com.fuhao.esb.common.request;

import java.io.Serializable;

/**
 * Created by fuhao on 13-12-6.
 */
public interface IESBAccessMessage extends Serializable {
    /**
     *
     *@name    获取交易码
     *@Description 相关说明
     *@Time    创建时间:2013-11-10上午10:48:36
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    String getTranID();

    /**
     *
     *@name    获取交易流水
     *@Description 相关说明
     *@Time    创建时间:2012-3-10上午10:48:57
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    String getTranSeq();

    /**
     *
     *@name    获取接入系统编码
     *@Description 相关说明
     *@Time    创建时间:2012-3-10上午10:49:23
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    String getSystemID();

    /**
     *
     *@name    获取消息目标ID
     *@Description 相关说明
     *@Time    创建时间:2012-3-10上午10:49:43
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    String getDestinationID();

    String getSecurityPublicKey();

    void setSecurityPublicKey(String securityPublicKey);

    /**
     *  获得发送方IP
     *@name    中文名称
     *@Description 相关说明
     *@Time    创建时间:2012-3-13下午03:27:48
     *@author fuhao
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    String getIP();
    /**
     * 设置发送方IP
     *@name    中文名称
     *@Description 相关说明
     *@Time    创建时间:2012-3-13下午03:37:04
     *@author fuhao
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    void setIP(String ip);
    /**
     * 设置当前节点编码，为系统接入配置传递当前节点到下一个交易节点。
     *@name    中文名称
     *@Description 相关说明
     *@Time    创建时间:2012-3-13下午03:39:44
     *@author fuhao
     *@param nodeCode
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    void setNodeCode(String nodeCode);
    /**
     * 获得当前节点编码，为系统接入配置传递当前节点到下一个交易节点。
     *@name    中文名称
     *@Description 相关说明
     *@Time    创建时间:2012-3-13下午03:39:59
     *@author fuhao
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    String getNodeCode();

    /**
     *
     *@name    获取业务数据标识
     *@Description 相关说明
     *@Time    创建时间:2013-5-8上午09:18:43
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */

    void setTransferMode(String transferMode);

    String getTransferMode();

    void setHasAttachment(boolean has);

    boolean isHasAttachment();

    void setTradeReqStartTime(long t);
    long getTradeReqStartTime();
    void setTradeInStartTime(long t);
    long getTradeInStartTime();
    String getEsbMessageType();
    void setEsbMessageType(String esbMessageType);
}
