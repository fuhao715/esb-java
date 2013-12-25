package com.fuhao.esb.common.xml.packagetransformer;

import com.fuhao.esb.common.apimessage.Expand;
import com.fuhao.esb.common.apimessage.HeadType;
import com.fuhao.esb.common.apimessage.RtnMeg;
import com.fuhao.esb.common.apimessage.Service;
import com.fuhao.esb.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TransformByMatchcharacters implements IAipPackageTransform{
	public static String PREFIX_CDATA    = "<![CDATA[";
	public static String SUFFIX_CDATA    = "]]>";  
	
//	private static SwordLogUtils logger = SwordLogUtils.getLogger(IntegratedPlatformUtil.class);
	
	/*初始化技术报文解析器
	public static JAXBContext context  ;
	static {
		try {
			context=JAXBContext.newInstance(Service.class);
		} catch (JAXBException e) {
			logger.error("初始化技术报文解析器时异常，异常信息："+e.getMessage());
			context=null;
		} 
	} 
	*/
	
	/**
	 * 报文转换字符串转换成报文对象Service
	 * 
	 * @param messageXML
	 * @return 
	 * @throws Exception
	 * @throws javax.xml.bind.JAXBException
	 * 修改历史->
	 * modify by liangshanpeng-20120320-改成XStream解析报文
	 */
	public Service stringToService(String messageXML) {
		/*  XStream解析 satrt 
		XStream xstream = new XStream(new XppDriver(){public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				protected void writeText(QuickWriter writer, String text) {
					if (text.startsWith(PREFIX_CDATA)&& text.endsWith(SUFFIX_CDATA)) {
						
						writer.write(text);
					} else {
						super.writeText(writer, text);
					}
				}
			};
		};});
		
		setElementInXStream(xstream);
		
		Service messageObject =  (Service)xstream.fromXML(messageXML);
		String body = PREFIX_CDATA + messageObject.getBody() + SUFFIX_CDATA;
		messageObject.setBody(body);
		return messageObject;
		 XStream解析 satrt end */
		
		
		/*JAXB解析 start; 
		Service request = null;
		StringReader writer = new StringReader(messageXML);
		Unmarshaller um;
		try {
			if(context==null)
			{
				context = JAXBContext.newInstance(Service.class);
			}
			um = context.createUnmarshaller();
			request = (Service) um.unmarshal(writer);
			
			//如果XML未转义，jaxb遇到CDATA自动会去掉CDATA，此处还原加上CDATA，还原初始报文
			request.setBody(PREFIX_CDATA+request.getBody()+ SUFFIX_CDATA);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			throw new SwordBaseCheckedException(
					"技术报文解析错误：技术报文XML转换成报文对象时出错，请参照相关XSD确认报文格式！"
							+ "\r\n具体异常内容如下:" + e.getMessage(),e);
		}

		return request;
		JAXB解析 end*/

		
		
		/*方案三字符串解析-start */
		Service request = new Service();
		//组装报文头
		//必选项
		HeadType head = new HeadType();
		head.setTranId(StringUtil.getIntervalValue(messageXML, "<tran_id>", "</tran_id>"));
		head.setChannelId(StringUtil.getIntervalValue(messageXML, "<channel_id>", "</channel_id>"));
		head.setTranSeq(StringUtil.getIntervalValue(messageXML, "<tran_seq>", "</tran_seq>")); 
		head.setTranDate(StringUtil.getIntervalValue(messageXML, "<tran_date>", "</tran_date>"));
		head.setTranTime(StringUtil.getIntervalValue(messageXML, "<tran_time>", "</tran_time>"));
		//可选项
		head.setFilePath(StringUtil.getIntervalValue(messageXML, "<file_path>", "</file_path>"));
		head.setServVersion(StringUtil.getIntervalValue(messageXML, "<serv_version>", "</serv_version>"));
		
		
		//组装扩展报文
		List<String> expandNodes =StringUtil.getIntervalValues(messageXML, "<expand>", "</expand>");
		if(expandNodes!=null)
		{
			List<Expand> expands=new ArrayList<Expand>();
			
			Iterator<String> it=expandNodes.iterator();
			while(it.hasNext())
			{
				Expand expand=new Expand();
				String expandStr=it.next();
				expand.setName(StringUtil.getIntervalValue(expandStr, "<name>", "</name>"));
				expand.setValue(StringUtil.getIntervalValue(expandStr, "<value>", "</value>"));
				expands.add(expand);
			}
			head.setExpand(expands);
		}

		
        //组织返回报文
		head.setRtnCode(StringUtil.getIntervalValue(messageXML, "<rtn_code>", "</rtn_code>"));
		
		String rtnXlCode=StringUtil.getIntervalValue(messageXML, "<Code>", "</Code>");
		RtnMeg rtnMeg;
		if(rtnXlCode!=null)
		{
			rtnMeg=new RtnMeg();
			rtnMeg.setCode(StringUtil.getIntervalValue(messageXML, "<Code>", "</Code>"));
			rtnMeg.setMessage(StringUtil.getIntervalValue(messageXML, "<Message>", "</Message>"));
			rtnMeg.setReason(StringUtil.getIntervalValue(messageXML, "<Reason>", "</Reason>"));
			head.setRtnMeg(rtnMeg);
		}
		else
		{
			rtnMeg=null;
		}

		
		//组织报文头
		request.setHead(head);
		//组织业务报文
		request.setBody(StringUtil.getIntervalValue(messageXML, "<body>", "</body>"));
		
		return request;
		/*方案三字符串解析-end */
	}
	/**
	 * @param messageObject
	 * @return
	 * @throws Exception
	 * 修改历史->
	 * modify by liangshanpeng-20120320-改成XStream解析报文
	 */
	public  String serviceToString(Service messageObject)  {
		/*  XStream解析-start 
		XStream xStream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));

		setElementInXStream(xStream);
		//将xSteam设置为“无引用”模式，这样如果报文java对象中引用了同一对象，生成的xml也不会存在references关系
		xStream.setMode(XStream.NO_REFERENCES);
		
		String messageXML = xStream.toXML(messageObject);
		messageXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+ messageXML;
		messageXML = StringEscapeUtils.unescapeXml(messageXML);

		return messageXML; 
		/* XStream解析-start */
		
		/*JAXB解析 -start; 
		Marshaller m;
		StringWriter request_Str = new StringWriter();
		try {
			if(context==null)
			{
				context = JAXBContext.newInstance(Service.class);
			}
			m = context.createMarshaller();
			m.marshal(messageObject, request_Str);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			throw new SwordBaseCheckedException(
					"技术报文解析错误：技术报文对象转换成XML时出错，请参照相关XSD确认报文对象格式！"
							+ "\r\n具体异常内容如下:" + e.getMessage(),e);
		}
		//jaxb会对body中的业务报文转义，因此需要还原原始报文
		//替换字符对网报有影响的
		return request_Str.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		JAXB解析 -end*/
		
		
		
		/*方案三字符串解析-start	*/
        StringBuffer xml=new StringBuffer();
        
        StringBuffer expandStr=new StringBuffer("");
        List<Expand> expands=messageObject.getHead().getExpand();
        if(expands!=null&&expands.size()!=0)
        {
			Iterator<Expand> it = expands.iterator();
			while (it.hasNext()) {
				Expand kzsx=it.next();
				expandStr
				.append("<expand>")
				.append("<name>").append(kzsx.getName()).append("</name>")
				.append("<value>").append(kzsx.getValue()).append("</value>")
				.append("</expand>");
			}
        }
     
		xml
		.append("<service xmlns=\"http://www.chinatax.gov.cn/spec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
		.append("<head>")
		.append("<tran_id>").append(messageObject.getHead().getTranId()).append("</tran_id>")
		.append("<channel_id>").append(messageObject.getHead().getChannelId()).append("</channel_id>")
		.append("<tran_seq>").append(messageObject.getHead().getTranSeq()).append("</tran_seq>")
		.append("<tran_date>").append(messageObject.getHead().getTranDate()).append("</tran_date>")
		.append("<tran_time>").append(messageObject.getHead().getTranTime()).append("</tran_time>")
		.append(messageObject.getHead().getRtnCode()==null?"" :  "<rtn_code>"+messageObject.getHead().getRtnCode()+"</rtn_code>")
		.append(messageObject.getHead().getRtnMeg() ==null?"" :  "<rtn_msg><Code>"+messageObject.getHead().getRtnMeg().getCode()+"</Code>"
	                                                                    +" <Message>"+messageObject.getHead().getRtnMeg().getMessage()+"</Message>" 
	                                                                    +"<Reason>"+messageObject.getHead().getRtnMeg().getReason()+"</Reason>" 
	                                                           + "</rtn_msg>")
		.append( (messageObject.getHead().getFilePath())==null?""  :  "<file_path>"+messageObject.getHead().getFilePath()+"</file_path>" )
		.append( (messageObject.getHead().getServVersion())==null?""  :  "<serv_version>"+messageObject.getHead().getServVersion()+"</serv_version>" )
		.append(expandStr)
		.append("</head>")
		.append("<body>").append(messageObject.getBody()).append("</body>")
		.append("</service>");
		
		return xml.toString();
		/*方案三字符串解析-end*/	
	}
	
}
