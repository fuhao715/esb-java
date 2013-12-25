package com.fuhao.esb.common.apimessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for headType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="headType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tran_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="channel_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tran_seq">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tran_date">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tran_time">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="file_path" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="512"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="rtn_code" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="rtn_msg" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="512"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="serv_version" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="expand" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
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
 * <p>File: HeadType.java 创建时间:2011-12-19下午09:25:02</p> 
 * <p>Title: 技术报文格式</p>
 * <p>Description: 技术报文格式-技术报文头</p>
 * <p>Copyright: Copyright (c) 2011 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  liangshanpeng
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）

 */
/*@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "headType", propOrder = { "tran_id", "channel_id", "tran_seq",
		"tran_date", "tran_time", "rtn_code", "rtn_msg", "file_path", "serv_version",
		"expands" })*/
public class HeadType  implements Serializable{

	private static final long serialVersionUID = 5317711220395361018L;

	public void setExpand(List<Expand> expand) {
		this.expands = expand;
	}
	//@XmlElement(name = "tran_id", required = true)
    protected String tran_id;
	
	//@XmlElement(name = "channel_id", required = true)
    protected String channel_id;
	
	//@XmlElement(name = "tran_seq", required = true)
    protected String tran_seq;
	
	//@XmlElement(name = "tran_date", required = true)
    protected String tran_date;
	
	//@XmlElement(name = "tran_time", required = true)
    protected String tran_time;
	
	//@XmlElement(name = "file_path")
    protected String file_path;
	
	//@XmlElement(name = "rtn_code")
    protected String rtn_code;
	
	//@XmlElement(name = "rtn_msg")
    protected RtnMeg rtn_msg;
    
	//@XmlElement(name = "serv_version")
    protected String serv_version;
    
    
    protected List<Expand> expands;
    
    public final static String DESTINATION_ID = "destination_id";
    public final static String YWSJBS = "ywsjbs";
    public final static String SECURITY_PUBLIC_KEY = "securityPublicKey";

    /**
     * Gets the value of the tranId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranId() {
        return tran_id;
    }

    /**
     * Sets the value of the tranId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranId(String value) {
        this.tran_id = value;
    }

    /**
     * Gets the value of the channelId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelId() {
        return channel_id;
    }

    /**
     * Sets the value of the channelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelId(String value) {
        this.channel_id = value;
    }

    /**
     * Gets the value of the tranSeq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranSeq() {
        return tran_seq;
    }

    /**
     * Sets the value of the tranSeq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranSeq(String value) {
        this.tran_seq = value;
    }

    /**
     * Gets the value of the tranDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranDate() {
        return tran_date;
    }

    /**
     * Sets the value of the tranDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranDate(String value) {
        this.tran_date = value;
    }

    /**
     * Gets the value of the tranTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranTime() {
        return tran_time;
    }

    /**
     * Sets the value of the tranTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranTime(String value) {
        this.tran_time = value;
    }

    /**
     * Gets the value of the filePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilePath() {
        return file_path;
    }

    /**
     * Sets the value of the filePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilePath(String value) {
        this.file_path = value;
    }

    /**
     * Gets the value of the rtnCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRtnCode() {
        return rtn_code;
    }

    /**
     * Sets the value of the rtnCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRtnCode(String value) {
        this.rtn_code = value;
    }

    public RtnMeg getRtnMeg() {
		return rtn_msg;
	}

	public void setRtnMeg(RtnMeg rtnMeg) {
		this.rtn_msg = rtnMeg;
	}

	/**
     * Gets the value of the servVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServVersion() {
        return serv_version;
    }

    /**
     * Sets the value of the servVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServVersion(String value) {
        this.serv_version = value;
    }

    /**
     * Gets the value of the expand property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the expand property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExpand().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Expand> getExpand() {
        if (expands == null) {
            expands = new ArrayList<Expand>();
        }
        return this.expands;
    }
    
    /* 通过key获得扩展的值 */
    public String getExpandValue(String key){
    	if(null == expands){
    		return null;
    	}
		for (Expand expand : expands) {
			if(key.equalsIgnoreCase(expand.getName())){
				return expand.getValue();
			}
		}
		return null;
    }

}
