package com.fuhao.esb.common.xml.packagetransformer;


import com.fuhao.esb.common.apimessage.Service;

/**
 * 
 *  金税三期工程核心征管及应用总集成项目 
 * <p>com.css.sword.esb.core.inbound
 * <p>File: IXmlTransformer.java 创建时间:2013-5-19下午10:47:04</p> 
 * <p>Title: 标题（要求能简洁地表达出类的功能和职责）</p>
 * <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
 * <p>Copyright: Copyright (c) 2013 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  周善保
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public interface IAipPackageTransform {
	/**
	 * 
	 *@name    中文名称
	 *@Description 相关说明	 
	 *@Time    创建时间:2013-5-19下午10:47:24
	 *@param messageXML
	 是否请求报文还是返回报文
	 *@return
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	Service stringToService(String messageXML);
	/**
	 * 
	 *@name    中文名称
	 *@Description 相关说明	 
	 *@Time    创建时间:2013-5-19下午10:47:31
	 *@param messageObject
	 *@return
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	 String serviceToString(Service messageObject);
}
