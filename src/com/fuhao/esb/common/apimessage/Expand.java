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
 * <p>Title: 集成平台报文</p>
 * <p>Description: 集成平台XML报文对应的实体</p>
 * <p>Copyright: Copyright (c) 2011 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  liangshanpeng
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）

 */
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "expand", propOrder = { "name", "value" })
public class Expand  implements Serializable{

	private static final long serialVersionUID = 6978705700002965471L;
	protected String name;
    protected String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

   
}
