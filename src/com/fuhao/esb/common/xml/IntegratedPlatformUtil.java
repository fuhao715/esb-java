package com.fuhao.esb.common.xml;

import com.fuhao.esb.common.apimessage.Expand;
import com.fuhao.esb.common.apimessage.HeadType;
import com.fuhao.esb.common.apimessage.RtnMeg;
import com.fuhao.esb.common.apimessage.Service;
import com.fuhao.esb.common.utils.StringUtil;
import com.fuhao.esb.core.util.AipPackageTransformerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * package name is  com.fuhao.esb.common.xml
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class IntegratedPlatformUtil {

    public static String PREFIX_CDATA    = "<![CDATA[";
    public static String SUFFIX_CDATA    = "]]>";

//	private static ESBLogUtils logger = ESBLogUtils.getLogger(IntegratedPlatformUtil.class);

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
     * 修改历史->
     * modify by liangshanpeng-20120320-改成XStream解析报文
     */
    public static Service stringToService(String messageXML) {
        return AipPackageTransformerFactory.getPackageTransformer().stringToService(messageXML);
    }
    /**
     * @param messageObject
     * @return
     * @throws Exception
     * 修改历史->
     * modify by liangshanpeng-20120320-改成XStream解析报文
     */
    public static String serviceToString(Service messageObject)  {
        return AipPackageTransformerFactory.getPackageTransformer().serviceToString(messageObject);
    }

    /**
     *@name
     *@Description 根据报文节点名称获取报文内容
     *@Time 创建时间:2011-12-18上午12:42:28
     *@return
     *@throws Exception
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String getNodeValueFromPacket(String infoXML,String nodeName) throws Exception
    {
        Service service;
        service = stringToService(infoXML);
        if("tran_id".equals(nodeName))
        {
            return service.getHead().getTranId();
        }
        else if("channel_id".equals(nodeName))
        {
            return service.getHead().getChannelId();
        }
        else if("tran_seq".equals(nodeName))
        {
            return service.getHead().getTranSeq();
        }
        else if("tran_date".equals(nodeName))
        {
            return service.getHead().getTranDate();
        }
        else if("tran_time".equals(nodeName))
        {
            return service.getHead().getTranTime();
        }
        else if("rtn_code".equals(nodeName))
        {
            return service.getHead().getRtnCode();
        }
        else
        {
            return "";
        }
    }
    /**
     *@name
     *@Description 获取返回报文结果
     *@Time 创建时间:2012-11-26上午12:42:28
     *@return
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static boolean getResponseResult(String responseXML)
    {
        //性能考虑，直接从XML搜索返回结果，不再进行报文解析
        String rtnCode=(StringUtil.getIntervalValue(responseXML, "<rtn_code>", "</rtn_code>")).trim();
        if("0".equals(rtnCode))
        {
            return true;
        }else
        {
            return false;
        }
    }
    /**
     * 测试
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        //获取扩展字段
        Service request = new Service();
        //组装报文头
        HeadType head = new HeadType();
        head.setTranId("C00.SP.SPHM.ZGCZ.Get");
        head.setChannelId("22");
        head.setTranSeq(UUID.randomUUID().toString().replaceAll("-", ""));
        java.util.Date date = new java.util.Date();
        SimpleDateFormat tran_date = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat tran_time = new SimpleDateFormat("hhmmssSSS");
        head.setTranDate(tran_date.format(date));
        head.setTranTime(tran_time.format(date));
        //组装扩展报文
        List<Expand> expands=new ArrayList<Expand>();
        Expand pasESB=new Expand();
        pasESB.setName("pasESB");
        pasESB.setValue("123");
        Expand pasESBNew=new Expand();
        pasESBNew.setName("pasESBNew");
        pasESBNew.setValue("456");
        Expand signOutReason=new Expand();
        signOutReason.setName("signOutReason");
        signOutReason.setValue("系统签退");

        expands.add(pasESB);
        expands.add(pasESBNew);
        expands.add(signOutReason);

        //expands.add("<name>pasESB</name><value>1234</value>");
        //expands.add("<name>pasESBConfirm</name><value>1234</value>");
        head.setExpand(expands);

        //组织返回报文
        head.setRtnCode("1");
        RtnMeg rtnMeg=new RtnMeg();
        rtnMeg.setCode("001");
        rtnMeg.setMessage("error");
        //rtnMeg.setReason("timeOut");
        head.setRtnMeg(rtnMeg);

        request.setHead(head);
        //组装业务报文
        String ywbw=PREFIX_CDATA+"<?xml version=\"1.0\" encoding=\"UTF-8\"?><taxML xsi:type=\"swdjbbgswdjYwbw\" bbh=\"v1.0\" xmlbh=\"String\" xmlmc=\"String\" xsi:schemaLocation=\"http://www.chinatax.gov.cn/dataspec/TaxML011002003_v1.0.xsd\" xmlns=\"http://www.chinatax.gov.cn/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><swdjbbgswdj><swdjbbgswdjHead><skssqq>aaaaaaaaaaa</skssqq><skssqz>String</skssqz><sbrq>String</sbrq></swdjbbgswdjHead><swdjbbgswdjBody><bgswdjjbxx><nsrmc>String</nsrmc><nsrsbh>bgswdjjbxx</nsrsbh></bgswdjjbxx><bgswdjbgjbxx><bgqnr>String</bgqnr><bgxm>bgswdjbgjbxx</bgxm></bgswdjbgjbxx><bgswdjtzfxx><tzfmc>bgswdjtzfxx</tzfmc><tzfjjxz>String</tzfjjxz></bgswdjtzfxx><bgswdjhhrxx><gj>String</gj><hhrmc>bgswdjhhrxx</hhrmc></bgswdjhhrxx><bgswdjfzjgxx><fzjgmc>fzjgmc</fzjgmc><fzjgnsrsbh>bgswdjfzjgxx</fzjgnsrsbh></bgswdjfzjgxx><list><row><row><nsrmc>String</nsrmc><nsrsbh>bgswdjjbxx</nsrsbh></row><row><row><nsrmc>String</nsrmc><nsrsbh>bgswdjjbxx</nsrsbh></row></row></row><row><nsrmc>String</nsrmc><nsrsbh>bgswdjjbxx</nsrsbh></row><row><fzjgmc>fzjgmc</fzjgmc><fzjgnsrsbh>bgswdjfzjgxx</fzjgnsrsbh></row></list></swdjbbgswdjBody></swdjbbgswdj></taxML>"
                +SUFFIX_CDATA;
        request.setBody(ywbw);
        //String str="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><service><head><tran_id>110000000</tran_id><channel_id>111</channel_id><tran_seq>144b21324081453183829ccecfbd2c50</tran_seq><tran_date>20120314</tran_date><tran_time>100139671</tran_time></head><body><?xml version=\"1.0\" encoding=\"UTF-8\"?><taxML xsi:type=\"swdjbbgswdjYwbw\" bbh=\"v1.0\" xmlbh=\"String\" xmlmc=\"String\" xsi:schemaLocation=\"http://www.chinatax.gov.cn/dataspec/TaxML011002003_v1.0.xsd\" xmlns=\"http://www.chinatax.gov.cn/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><swdjbbgswdj><swdjbbgswdjHead><skssqq>aaaaaaaaaaa</skssqq><skssqz>String</skssqz><sbrq>String</sbrq></swdjbbgswdjHead><swdjbbgswdjBody><bgswdjjbxx><nsrmc>String</nsrmc><nsrsbh>bgswdjjbxx</nsrsbh></bgswdjjbxx><bgswdjbgjbxx><bgqnr>String</bgqnr><bgxm>bgswdjbgjbxx</bgxm></bgswdjbgjbxx><bgswdjtzfxx><tzfmc>bgswdjtzfxx</tzfmc><tzfjjxz>String</tzfjjxz></bgswdjtzfxx><bgswdjhhrxx><gj>String</gj><hhrmc>bgswdjhhrxx</hhrmc></bgswdjhhrxx><bgswdjfzjgxx><fzjgmc>fzjgmc</fzjgmc><fzjgnsrsbh>bgswdjfzjgxx</fzjgnsrsbh></bgswdjfzjgxx><list><row><row><nsrmc>String</nsrmc><nsrsbh>bgswdjjbxx</nsrsbh></row><row><row><nsrmc>String</nsrmc><nsrsbh>bgswdjjbxx</nsrsbh></row></row></row><row><nsrmc>String</nsrmc><nsrsbh>bgswdjjbxx</nsrsbh></row><row><fzjgmc>fzjgmc</fzjgmc><fzjgnsrsbh>bgswdjfzjgxx</fzjgnsrsbh></row></list></swdjbbgswdjBody></swdjbbgswdj></taxML></body></service>";
        String str2=serviceToString(request);
        System.out.println("serviceToString报文如下:"+str2);
        Service service=stringToService(str2);
        System.out.println("转换对象后对象业务报文:"+service.getBody());

        String lastStr=serviceToString(service);
        System.out.println("再次转换:"+lastStr);
        Service lastService=stringToService(lastStr);
        System.out.println("再次转换:"+lastService.getBody());




        String messageRd="<service xmlns=\"http://www.chinatax.gov.cn/spec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                +"<head>"
                +"<tran_id>C00.SB.SBJYSB.GSCX.jysbcsh</tran_id>"
                +"<channel_id>C000AHXNS</channel_id>"
                +"<tran_seq>92a49d1700cc4c13929314b67a6ecf6f</tran_seq>"
                +"<tran_date>20120609</tran_date>"
                +"<tran_time>011419692</tran_time>"
                +"<rtn_code>0</rtn_code>"
                +"<rtn_msg>"
                +"<Code>000</Code>"
                +"<Message>调用接口成功</Message>"
                +"<Reason>调用接口成功</Reason>"
                + "</rtn_msg>"
                +"</head>"
                +"<body><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                +"<taxML xsi:type=\"zrrxxcxrequest\" xmlbh=\"I\" bbh=\"1.0\" xmlmc=\"自然人信息查询\" xsi:schemaLocation=\"http://www.chinatax.gov.cn/dataspec/ TaxML_I_HX_SB_001_Request_v1.0.xsd\" xmlns=\"http://www.chinatax.gov.cn/dataspec/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><djxh></djxh><zjzlDm></zjzlDm><zjhm></zjhm><nsrsbh>420703197212230041</nsrsbh></taxML>]]></body>"
                +"</service>";
        //如果XML未转义，jaxb遇到CDATA自动会去掉CDATA，并取出其中内容
/*		long startTime=System.currentTimeMillis();
		for(int i=0;i<1000;i++)
		{
			Service service2=stringToService(messageRd);
		    serviceToString(service2);
		}
		System.out.println("时间差:"+(System.currentTimeMillis()-startTime));*/


        System.out.println("获取返回报文结果:"+IntegratedPlatformUtil.getResponseResult(messageRd));

    }
}
