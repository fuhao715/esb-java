
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
 * <p>com.css.sword.aip.ws.messageFormat
 * <p>File: Service.java 创建时间:2011-12-19下午09:25:50</p> 
 * <p>Title: 技术报文格式</p>
 * <p>Description: 技术报文格式-包括技术报文头和技术报文体</p>
 * <p>Copyright: Copyright (c) 2011 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  liangshanpeng
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）

 */
/*@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "head", "body" })
@XmlRootElement(name = "service")*/
public class Service implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8617840598698115862L;
	
	//@XmlElement(required = true)
    protected HeadType head;
	//@XmlElement(required = true)
    protected String body;
    
	//对应的XML命名空间
	@SuppressWarnings("unused")
	private final String xmlns = "http://www.chinatax.gov.cn/spec/";
	//XML解析器根据某个 schema 来验证此文档
	@SuppressWarnings("unused")
	private final String xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";

    /**
     * Gets the value of the head property.
     * 
     * @return
     *     possible object is
     *     {@link HeadType }
     *     
     */
    public HeadType getHead() {
        return head;
    }

    /**
     * Sets the value of the head property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeadType }
     *     
     */
    public void setHead(HeadType value) {
        this.head = value;
    }

    /**
     * Gets the value of the body property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBody() {
    	return body;
/*    	body=body.trim();
    	if(!body.startsWith(IntegratedPlatformUtil.PREFIX_CDATA)&&!body.endsWith(IntegratedPlatformUtil.SUFFIX_CDATA))
    	{
    		return IntegratedPlatformUtil.PREFIX_CDATA+body+ IntegratedPlatformUtil.SUFFIX_CDATA;
    	}
    	else
    	{
    		return body;
    	}*/
    }

    /**
     * Sets the value of the body property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBody(String value) {
        this.body = value;
    }

/*	public String getXmlns() {
		return xmlns;
	}
	public String getXmlnsXsi() {
		return xmlnsXsi;
	}*/
}
