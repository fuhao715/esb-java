package com.fuhao.esb.common.xml.jaxb.xmladaptor;




import java.text.DecimalFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

 /**
 *  金税三期工程核心征管及应用总集成项目 
 * <p>cn.css.sword.xmlBeanconvert
 * <p>File: DoubleXmlAdaptor.java 创建时间:2012-5-18下午12:30:47</p> 
 * <p>Title: jaxb自定义编组解析类</p>
 * <p>Description: double类型格式化</p>
 * <p>Copyright: Copyright (c) 2012 中国软件与技术服务股份有限公司</p>
 * <p>Company: 中国软件与技术服务股份有限公司</p>
 * <p>模块: ESB</p>
 * @author  liangshanpeng
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）

 */
public class DoubleXmlAdaptor extends XmlAdapter<String, Double> {

    public Double unmarshal(String value) {
        return (javax.xml.bind.DatatypeConverter.parseDouble(value));
    }

    public String marshal(Double value) {
        if (value == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("0.##"); // ##表示2位小数
        String doubleValue=df.format(value);
        //return (javax.xml.bind.DatatypeConverter.printDouble(value));
        return doubleValue;
    }

}

