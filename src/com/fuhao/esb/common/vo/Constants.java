package com.fuhao.esb.common.vo;

/**
 * package name is  com.fuhao.esb.common.vo
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public class Constants {

    public enum SEQLOG_TYPE {
        TRANS_IN,SERVICE_REQ,SERVICE_RES,TRANS_OUT;
    }
    /**
     * 路由策略
     */
    public enum ROUTE_POLICY{
        Any,FullName,StartWith,EndWith,Regex,Dynamic,User_defined // 动态路由算法
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
