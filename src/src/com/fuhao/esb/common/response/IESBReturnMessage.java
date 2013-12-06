package com.fuhao.esb.common.response;

import com.fuhao.esb.common.request.IESBAccessMessage;

/**
 * Created by fuhao on 13-12-6.
 */
public interface IESBReturnMessage extends IESBAccessMessage {
    /**
     *
     *@name    得到返回码
     *@Description 相关说明
     *@Time    创建时间:2012-3-10上午11:00:48
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    String getReturnCode();
    /**
     *
     *@name    设置返回码
     *@Description 相关说明
     *@Time    创建时间:2012-3-10上午11:00:48
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    void setReturnCode(String returnCode);
}
