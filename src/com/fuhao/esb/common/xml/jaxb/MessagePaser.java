package com.fuhao.esb.common.xml.jaxb;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


 /**
 *  金税三期工程核心征管及应用总集成项目 
 * <p>com.css.sword.esb.comm.xml.jaxb
 * <p>File: MessagePaser.java 创建时间:2012-5-19上午11:43:44</p> 
 * <p>Title: 动态解析报文</p>
 * <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
 * <p>Copyright: Copyright (c) 2012 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  liangshanpeng
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）

 */
public class MessagePaser {
	private Class<?> clz;
	public MessagePaser( Class<?> clz)
	{
		 this.clz= clz;
	}
	/**
	 *@name    xml转换成业务对象
	 */
	public Object xml2Bean(String  xml)  
	{
		JAXBContext context;
		Object obj=null;
		//获取XML转换成BEAN的对象路径
		try {
		StringReader writer = new StringReader(xml.toString());
		Unmarshaller um;
		
			context = JAXBContext.newInstance(clz);
			um = context.createUnmarshaller();
			obj =  um.unmarshal(writer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		return obj;
	}
	/**
	 *@name    业务对象转换成XML
	 */
	public String  beant2Xml(Object  obj)  
	{
		JAXBContext context;
		Marshaller m;
		StringWriter request_Str = new StringWriter();
		try {
			context = JAXBContext.newInstance(clz);
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "GBK");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(obj, request_Str);
		} catch (JAXBException e) {
			return null;
		}
		return request_Str.toString();
	}
}
