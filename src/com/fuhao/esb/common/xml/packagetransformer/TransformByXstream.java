package com.fuhao.esb.common.xml.packagetransformer;

import java.io.Writer;

import com.fuhao.esb.common.apimessage.Expand;
import com.fuhao.esb.common.apimessage.HeadType;
import com.fuhao.esb.common.apimessage.RtnMeg;
import com.fuhao.esb.common.apimessage.Service;
import org.apache.commons.lang.StringEscapeUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 
 *  金税三期工程核心征管及应用总集成项目 
 * <p>com.css.sword.esb.comm.xml.packagetransformer
 * <p>File: TransformByXstream.java 创建时间:2013-9-7下午11:59:57</p> 
 * <p>Title: 标题（要求能简洁地表达出类的功能和职责）</p>
 * <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
 * <p>Copyright: Copyright (c) 2013 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  周善保
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class TransformByXstream  implements IAipPackageTransform{
	public static String PREFIX_CDATA    = "<![CDATA[";
	public static String SUFFIX_CDATA    = "]]>";  
	
	/**
	 * 线程相关的XML转换成Bean的Xtream，避免每次实例化导致锁出现并发问题
	 */
	private ThreadLocal<XStream> threadLocalStream = new ThreadLocal<XStream>();
	
	/**
	 * 线程相关的Bean转换成XML的Xtream，避免每次实例化导致锁出现并发问题
	 */
	private ThreadLocal<XStream> threadLocalBeanStream = new ThreadLocal<XStream>();
	
	
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
	 * 获取当前线程的String转Bean的XStream
	 */
	private XStream getXStreamS2O(){
		/*  XStream解析 satrt */
		if (threadLocalStream.get()==null){
			XStream xstreamS2O = new XStream(new XppDriver(){public HierarchicalStreamWriter createWriter(Writer out) {
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
			threadLocalStream.set(xstreamS2O);
		}
		return threadLocalStream.get();
	}
	
	/**
	 * 获取当前线程的Bean转String的XStream
	 */
	private XStream getXStreamBean2String(){
		/*  XStream解析 satrt */
		if (threadLocalBeanStream.get()==null){
			XStream xstream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
			threadLocalBeanStream.set(xstream);
		}
		return threadLocalBeanStream.get();
	}

	/**
	 * 报文转换字符串转换成报文对象Service
	 * 
	 * @param messageXML
	 * @return 
	 * @throws Exception
	 * 修改历史->
	 * modify by liangshanpeng-20120320-改成XStream解析报文
	 */
	public  Service stringToService(String messageXML) {
		setElementInXStream(getXStreamS2O());
		Service messageObject =  (Service)getXStreamS2O().fromXML(messageXML);
		String body = PREFIX_CDATA + messageObject.getBody() + SUFFIX_CDATA;
		messageObject.setBody(body);
		return messageObject;
		/* XStream解析 satrt end */
		
		
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
			throw new SwordBaseCheckedException(
					"技术报文解析错误：技术报文XML转换成报文对象时出错，请参照相关XSD确认报文格式！"
							+ "\r\n具体异常内容如下:" + e.getMessage(),e);
		}

		return request;
		JAXB解析 end*/

		
		
		/*方案三字符串解析-start 
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
		方案三字符串解析-end */
	}
	/**
	 * @param messageObject
	 * @return
	 * @throws Exception
	 * 修改历史->
	 * modify by liangshanpeng-20120320-改成XStream解析报文
	 */
	public  String serviceToString(Service messageObject)  {
		setElementInXStream(getXStreamBean2String());
		//将xSteam设置为“无引用”模式，这样如果报文java对象中引用了同一对象，生成的xml也不会存在references关系
		getXStreamBean2String().setMode(XStream.NO_REFERENCES);
		
		String messageXML = getXStreamBean2String().toXML(messageObject);
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
			throw new SwordBaseCheckedException(
					"技术报文解析错误：技术报文对象转换成XML时出错，请参照相关XSD确认报文对象格式！"
							+ "\r\n具体异常内容如下:" + e.getMessage(),e);
		}
		//jaxb会对body中的业务报文转义，因此需要还原原始报文
		//替换字符对网报有影响的
		return request_Str.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		JAXB解析 -end*/
		
		
		
		/*方案三字符串解析-start	
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
		方案三字符串解析-end*/	
	}
	/**
	 *@name    设置XML内元素的格式
	 *@Description 根据《税务行业数据交换技术协议标准》在转换过程中设置XML的元素格式
	 *@Time    创建时间:2011-12-13下午04:05:13
	 *@param xStream
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	private  void setElementInXStream(XStream xStream){
		//指定XML中的元素重命名
		xStream.alias("service", Service.class );
		xStream.alias("head", HeadType.class );
		xStream.alias("rtn_msg", RtnMeg.class );
		xStream.alias("expand", Expand.class );
		xStream.addImplicitCollection(HeadType.class, "expands");
		xStream.useAttributeFor(Service.class, "xmlns");
		xStream.aliasAttribute(Service.class, "xmlnsXsi", "xmlns:xsi");
	}
}
