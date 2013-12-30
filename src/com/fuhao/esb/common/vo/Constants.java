package com.fuhao.esb.common.vo;

import com.fuhao.esb.common.request.IProtocolConf.ProtocolType;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.route.protocal.*;

/**
 * package name is  com.fuhao.esb.common.vo
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class Constants {
    private static ESBLogUtils logger = ESBLogUtils.getLogger(Constants.class);
    public enum SEQLOG_TYPE {
        TRANS_IN,SERVICE_REQ,SERVICE_RES,TRANS_OUT;
    }
    /**
     * 路由策略
     */
    public enum ROUTE_POLICY{
        Any,FullName,StartWith,EndWith,Regex,Dynamic,User_defined // 动态路由算法
    }


    public static ROUTE_POLICY getRoutePolicy(String name) {
        if (name.equalsIgnoreCase(ROUTE_POLICY.FullName.name())) {
            return ROUTE_POLICY.FullName;
        } else if (name.equalsIgnoreCase(ROUTE_POLICY.StartWith.name())) {
            return ROUTE_POLICY.StartWith;
        } else if (name.equalsIgnoreCase(ROUTE_POLICY.EndWith.name())) {
            return ROUTE_POLICY.EndWith;
        } else if (name.equalsIgnoreCase(ROUTE_POLICY.Regex.name())) {
            return ROUTE_POLICY.Regex;
        } else if (name.equalsIgnoreCase(ROUTE_POLICY.Dynamic.name())) {
            return ROUTE_POLICY.Dynamic;
        } else if (name.equalsIgnoreCase(ROUTE_POLICY.User_defined.name())) {
            return ROUTE_POLICY.User_defined;
        } else if (name.equalsIgnoreCase(ROUTE_POLICY.Any.name())) {
            return ROUTE_POLICY.Any;
        }else {
            logger.error("此处理器类型不支持：" + name);
            return null;
        }
    }


    public static ProtocolType getProtocalType(String name) {
        if (name.equalsIgnoreCase(ProtocolType.EJB.name())) {
            return ProtocolType.EJB;
        } else if (name.equalsIgnoreCase(ProtocolType.WEBSERVICE.name())) {
            return ProtocolType.WEBSERVICE;
        } else if (name.equalsIgnoreCase(ProtocolType.JMS.name())) {
            return ProtocolType.JMS;
        } else if (name.equalsIgnoreCase(ProtocolType.IBMMQ.name())) {
            return ProtocolType.IBMMQ;
        } else if (name.equalsIgnoreCase(ProtocolType.ACTIVEMQ.name())) {
            return ProtocolType.ACTIVEMQ;
        } else if (name.equalsIgnoreCase(ProtocolType.RABBITMQ.name())) {
            return ProtocolType.RABBITMQ;
        } else if (name.equalsIgnoreCase(ProtocolType.FTP.name())) {
            return ProtocolType.FTP;
        } else if (name.equalsIgnoreCase(ProtocolType.HESSIAN.name())) {
            return ProtocolType.HESSIAN;
        } else if (name.equalsIgnoreCase(ProtocolType.SOCKET.name())) {
            return ProtocolType.SOCKET;
        } else if (name.equalsIgnoreCase(ProtocolType.LOCAL.name())) {
            return ProtocolType.LOCAL;
        }else if (name.equalsIgnoreCase(ProtocolType.LocalXML.name())) {
            return ProtocolType.LocalXML;
        }else {
            logger.error("此协议不支持：" + name);
            return null;
        }
    }

    public static IProtocalInfo getProtocalInfoType(String name) {
        if (name.equalsIgnoreCase(ProtocolType.EJB.name())) {
            return new EJBProtocalInfo();
        } else if (name.equalsIgnoreCase(ProtocolType.WEBSERVICE.name())) {
            return new WebServiceProtocalInfo();
        } else if (name.equalsIgnoreCase(ProtocolType.JMS.name())) {
            return new JMSProtocalInfo();
        } else if (name.equalsIgnoreCase(ProtocolType.IBMMQ.name())) {
            return new IBMMQProtocalInfo();
        } else if (name.equalsIgnoreCase(ProtocolType.ACTIVEMQ.name())) {
            return new ACTIVEMQProtocalInfo();
        } else if (name.equalsIgnoreCase(ProtocolType.RABBITMQ.name())) {
            return new RABBITMQProtocalInfo();
        } else if (name.equalsIgnoreCase(ProtocolType.FTP.name())) {
            return new FTPProtocalInfo();
        } else if (name.equalsIgnoreCase(ProtocolType.HESSIAN.name())) {
            return new HessianProtocalInfo();
        } else if (name.equalsIgnoreCase(ProtocolType.SOCKET.name())) {
            return new SocketProtocalInfo();
        }else {
            logger.error("此协议不支持：" + name);
            return null;
        }
    }


    /*ejb小类 */
    public enum COMMUN_PROTOCOL_EJB_SUB_TYPE{
        SKYEJB,ZCEJB,ESBEJB,CTAISEJB
    }
    /*jms小类 */
    public enum COMMUN_PROTOCOL_JMS_SUB_TYPE{
        SKYJMS,ESBJMS
    }
    /*
     * 记录报文记录点类型
     * MSG_IN 进入报文
     * MSG_RES 返回报文
     */
    public enum MsgLOG_TYPE {
        MSG_IN,MSG_RES;
    }

    public enum ServiceNodeParameters {
        virtualNodeName,extraRowNodeName,mapToXmlParser,xmlToBeanParser
    }

    //总集成XSD效验
    public static final String MESSAGE_YYJC = "xsd/esb/gt3aip.xsd";

    /**
     * JMS上下文工厂
     */
    public static final String CONTEXT_FACTORY="weblogic.jndi.WLInitialContextFactory";

    public static final String JMS_CONNECTION_FACTORY="gt3.esb.jms.con.ESBConnectionFactory";

    public static final String JMS_QUEUE_MDB_NAME="gt3.esb.jms.mdb.BaseQueueAsynMDBean";

    public static final String JMS_REPLY_CONNECTION_FACTORY="gt3.esb.jms.con.ESBReplyConnectionFactory";

    public static final String JMS_REPLY_QUEUE_MDB_NAME="gt3.esb.jms.mdb.ReplyBaseQueueSynMDBean";



    public static final String SKY_JMS_CONNECTION_FACTORY="gt3.sky.jms.con.SKYConnectionFactory";
    public static final String SKY_JMS_QUEUE_MDB_NAME="gt3.sky.jms.mdb.SkyQueueAsynMDBean";

    //mq文件传送信息 文件所在主机
    public static final String MQ_FILEINFO_FILE_HOST = "fileHost";
    //mq文件传送信息 文件块数
    public static final String MQ_FILEINFO_FILE_PARTS = "fileParts";
    //mq文件传送信息 文件消息ID
    public static final String MQ_FILEINFO_FILE_MESSAGEID = "messageId";
    //mq文件传送信息 文件接接收路径
    public static final String MQ_FILEINFO_FILE_RECV_PATH = "recvFilePath";

    //跨层XSD效验
    public static final String MESSAGE_GDEP = "kcbw.xsd";

    //IP验证开关
    public static final String VALIDATE_IP_SWITCH = "VALIDATE_IP_SWITCH";
    /*IP验证开关值
     * 0表示关闭
     * 1表示打开
     */
    public static final String VALIDATE_IP_SWITCH_OFF = "0";
    public static final String VALIDATE_IP_SWITCH_ON = "1";
    // 远程路由合并开关
    public static final String REFRESH_REMOTE_ROUTEINFO_SWITCH="REFRESH_REMOTE_ROUTEINFO_SWITCH";
    /*
     * 0,表示关闭
     * 1，表示开启
     */
    public static final String REFRESH_REMOTE_ROUTEINFO_SWITCH_OFF="0";
    public static final String REFRESH_REMOTE_ROUTEINFO_SWITCH_ON="1";

    // 远程路由合并入库开关
    public static final String LOAD2DB_REMOTE_ROUTEINFO_SWITCH="LOAD2DB_REMOTE_ROUTEINFO_SWITCH";

    // 路由自动刷新时间
    public static final String REFRESH_ROUTE_CAPACITY="refreshRouteCapacity";
    // 路由版本号
    public static final String RROUTE_VERSION="RouteVersion";

    //流水记录开关
    public static final String Log_Transaction_Seqence = "log_transaction_seqence";

    //流水异常记录开关
    public static final String Log_exception= "log_exception";

    /*
     * 统一开关： 0表示关闭；1表示开启
     */
    public static final String ESB_SWITCH_OFF="0";
    public static final String ESB_SWITCH_ON="1";

    /*
     * 0,表示关闭
     * 1，表示开启
     */
    public static final String LOAD2DB_REMOTE_ROUTEINFO_SWITCH_OFF="0";
    public static final String LOAD2DB_REMOTE_ROUTEINFO_SWITCH_ON="1";

    //ESB本地发送，自己发给自己
    public static final String ESB_LOCAL_SEND = "ESB_LOCAL_SEND";

    //服务发送方系统id
    public static final String ESB_SYSTEM_ID = "ESBSystemID";
    //服务起点原始发送方id
    public static final String ESB_ORIGINATOR_SYSTEM_ID = "ESBOriginatorSystemID";
    //ESB调用链路状态表
    public static final String ESB_TRANSACTION_CHAIN = "ESBTransactionChain";
    //服务接收方系统ip
    public static final String ESB_SYSTEM_IP = "ESBSystemIP";
    //路由版本增加
    public static final String ESB_LY_ADD="ADD";
    //路由版本删除
    public static final String ESB_LY_DELETE="DELETE";
    //路由版本修改
    public static final String ESB_LY_UPDATE="UPDATE";

    //交易发起时间 ,交易请求时间
    public static final String ESB_JYLS_STARTTIME="ESBJylsStartTime";
    public static final String ESB_JYLS_REQTIME="ESBJylsReqTime";


}
