package com.fuhao.esb.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  金税三期工程核心征管及应用总集成项目 
 * <p>com.css.sword.aip.ws.util
 * <p>File: IntegratedPlatformUtil.java 创建时间:2011-12-19下午02:21:10</p> 
 * <p>Title: 集成平台接入工具类</p>
 * <p>Description: 集成平台接入工具类,报文转换，XML解析等</p>
 * <p>Copyright: Copyright (c) 2011 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  liangshanpeng
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）

 */
public class StringUtil {

    /**
     * @param source
     * @param priStr
     * @param suxStr
     * @return
     */
    public static String getIntervalValue(String source,String priStr,String suxStr)
    {
        if(source.indexOf(priStr)<0||source.indexOf(suxStr)<0)
        {
        	return null;
        }
        int beginIndex=source.indexOf(priStr)+priStr.length();
        int endIndex=source.indexOf(suxStr);
        return source.substring(beginIndex, endIndex);
    }
    /** 
     *  html要解析的html文档内容
     * @return 解析结果，可以多次匹配，每次匹配的结果按文档中出现的先后顺序添加进结果List 
     */  
    public static List<String> getIntervalValues(String source,String priStr,String suxStr) {  
        if(source.indexOf(priStr)<0||source.indexOf(suxStr)<0)
        {
        	return null;
        }
        List<String> resultList = new ArrayList<String>();  
        Pattern p = Pattern.compile(priStr+"(.*?)"+suxStr);  
        Matcher m = p.matcher(source);// 开始编译  
        while (m.find()) {  
            String group = m.group(1);  
            if (!"".equals(group)) {  
                resultList.add(group);  
            }  
        }  
        return resultList;  
    }  

    public static void main(String args[]) {  
       
/*        // 简单示例，相当于String html=getHtml(String urlString);  
        Pattern p = Pattern.compile("<title>(.*?)</title>");

        Matcher m = p.matcher(html);
        while (m.find()) {
          System.out.println(m.group(1));
        }*/
    	String html = "<service>ABCD</service><head>gsdggas</head><service>005</service>";  
        String res1=getIntervalValue(html,"<head>","</head>");
        System.out.println(res1);  
        
        String html2 = "<title>ABCD</title><title>gsdggas</title><title>005</title>";  
        List<String> list=getIntervalValues(html2,"<title>","</title>");
        System.out.println(list);
       
    }  
}
