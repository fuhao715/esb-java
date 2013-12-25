package com.fuhao.esb.common.apimessage;

import java.io.Serializable;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="head" type="{http://www.chinatax.gov.cn/spec/}headType"/>
 *         &lt;element name="body" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
/**
 *  金税三期工程核心征管及应用总集成项目 
 * <p>com.css.tax.integratedPlatform.packetFormat
 * <p>File: HeadType.java 创建时间:2011-12-13下午01:00:05</p> 
 * <p>Title: 集成平台报文-返回信息</p>
 * <p>Description: 集成平台XML报文对应的实体</p>
 * <p>Copyright: Copyright (c) 2011 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  liangshanpeng
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）

 */
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "RtnMsg", propOrder = { "Code", "Message", "Reason" })
/**
 * 
 *  金税三期工程核心征管及应用总集成项目 
 * <p>创建时间:2013-4-25</p> 
 * <p>Title:  </p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright (c) 2011 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  liangshanpeng
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class RtnMeg  implements Serializable,  Cloneable{

	private static final long serialVersionUID = -2406623850142671192L;
	//@XmlElement(name = "Code", required = true)
	protected String Code;
	//@XmlElement(name = "Message", required = true)
	protected String Message;
	//@XmlElement(name = "Reason", required = true)
    protected String Reason;
	
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	/**
	 *@name    根据四位返回码获取返回大类
	 *@Description 相关说明	 
	 *@Time    创建时间:2012-3-19上午09:55:12
	 *@param entireCode
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	public String getDLCodeByCode(String entireCode) {
		return entireCode.substring(0,1);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){ 
		RtnMeg clone = null; 
		try{ 
		    clone = (RtnMeg)super.clone(); 
		}catch(CloneNotSupportedException e){ 
			clone=new RtnMeg(); 
		} 
		return clone; 
		} 
}
