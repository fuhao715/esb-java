package com.fuhao.esb.core.route;

import com.fuhao.esb.client.IBaseESBClientMessageSender;
import com.fuhao.esb.client.ejb.AipEJBClient;
import com.fuhao.esb.client.hessian.BaseHessianClient;
import com.fuhao.esb.client.socket.SocketClient;
import com.fuhao.esb.common.request.ESBAipAccessMessageReq;
import com.fuhao.esb.common.request.IESBAccessMessage;
import com.fuhao.esb.common.request.IProtocolConf.ProtocolType;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import com.fuhao.esb.core.inbound.aip.AipXmlTransformer;
import com.fuhao.esb.core.logger.BaseMessageLogger;
import com.fuhao.esb.core.route.protocal.*;

import java.math.BigDecimal;

/**
 * package name is  com.fuhao.esb.core.route
 * Created by fuhao on 14-1-2.
 * Project Name esb-java
 */
public class BaseCommunicationProtocalFactory {

    private static final ESBLogUtils logger = ESBLogUtils.getLogger(BaseCommunicationProtocalFactory.class);

    /**
     *
     *@name    获取通信协议适配发送器
     *@Description 相关说明
     *@Time    创建时间:2012-5-17下午02:44:58
     *@return
     *@throws ESBBaseCheckedException
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static IBaseESBClientMessageSender getAdapter(IESBAccessMessage message)
            throws ESBBaseCheckedException {
        RouteProtocalInfo routeProtocalInfo = new RouteManager().getRoute(message);
        if (routeProtocalInfo==null)
            throw new ESBBaseCheckedException("路由信息没有配置：交易码"+message.getTranID());
        return getAdapter(routeProtocalInfo,message);
    }

    /**
     *
     *@name 获取通信协议适配器
     *@Description 相关说明
     *@Time 创建时间:2011-12-13下午10:51:30
     *@param routeProtocalInfo
     *@return
     * @throws Exception
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static IBaseESBClientMessageSender getAdapter (
            RouteProtocalInfo routeProtocalInfo,IESBAccessMessage message)throws ESBBaseCheckedException{
        ProtocolType type = routeProtocalInfo.getProtocalInfo().getProtocalType();
        if (type == ProtocolType.EJB) {
            EJBProtocalInfo po = (EJBProtocalInfo)routeProtocalInfo.getProtocalInfo();
            if (po==null){
                throw new ESBBaseCheckedException("路由表中：路由ID"+routeProtocalInfo.getRouteProtocalInfoID()+"、协议类型"+type+"协议ID"+routeProtocalInfo.getProtocalID()+"的协议信息没在在协议信息中配置");
            }
            AipEJBClient client = new AipEJBClient();
            client.setProviderUrl(po.getProviderUrls());
            client.setContextFactory(po.getContextFactory());
            client.setEjbName(po.getJndi());
            client.setUserName(po.getUserName());
            client.setPassword(po.getPassWord());
            BigDecimal requestTimeout = new BigDecimal(po.getConnetTimeOut());
            BigDecimal clientTimeout = new BigDecimal(po.getCallTimeOut());
            if(requestTimeout!=null){
                client.setRequestTimeout(requestTimeout.longValue());
            }
            if(clientTimeout!=null){
                client.setClientTimeout(clientTimeout.longValue());
            }
            logger.info("向节点"+po.getNodeName()+",IP地址为：" + po.getProviderUrls() + "的EJB组件" + po.getJndi()+"发送");
            return client;
        }  else if (type == ProtocolType.WEBSERVICE) {/*
            QzWebserviceXyPO po = (QzWebserviceXyPO)RouteCache.getInstance().getMapProtocalConf().get(serviceProtocalInfo.getProtocal());
            logger.info("向节点"+serviceProtocalInfo.getNodeCode()+",IP地址为：" + po.getFwdz().value + "发起Webservice调用服务"+"，配置的初始连接数为："+po.getInitconnectioncount().value);
            if (po==null){
                throw new SwordBaseCheckedException("路由表中：路由ID"+serviceProtocalInfo.getRouteConf()+"、协议类型"+type+"协议ID"+serviceProtocalInfo.getProtocal()+"的协议信息没在在协议表中配置");
            }
            Client client = null;
            try {
                client = WebServiceClientCache.newInstance().getClient(po);
            }catch (Exception e) {
                throw new SwordBaseCheckedException("从webservice连接缓存池获得实例异常：",e);
            }
            WebServiceClient eclient = new WebServiceClient(client,serviceProtocalInfo,message);
            return eclient;*/
        }else if (type == ProtocolType.JMS) {
           /* QzJmsXyPO po = (QzJmsXyPO)RouteCache.getInstance().getMapProtocalConf().get(serviceProtocalInfo.getProtocal());
            if (po==null){
                throw new SwordBaseCheckedException("路由表中：路由ID"+serviceProtocalInfo.getRouteConf()+"、协议类型"+type+"协议ID"+serviceProtocalInfo.getProtocal()+"的协议信息没在在协议表中配置");
            }
            JmsMessageVO vo = new JmsMessageVO(po.getSxwgc().value, po.getLjgc().value,
                    po.getTgzdz().value, po.getJndim().value,false,po.getSfdlfs().value);
            String jmsInitCacheCount = po.getJmsinitcachecount().value;
            String jndiRequestTimeout=po.getJndirequesttimeout().value;
            String rmiRequestTimeout=po.getRmirequesttimeout().value;
            logger.info("配置的jms初始化缓存连接数："+jmsInitCacheCount+",jndi请求超时数："+jndiRequestTimeout+",rmi请求超时数："+rmiRequestTimeout+",不配置将会使用默认值");
            vo.setJmsInitCacheCount(jmsInitCacheCount);
            vo.setJndiRequestTimeout(jndiRequestTimeout);
            vo.setRmiRequestTimeout(rmiRequestTimeout);
            InternalJMSClient client = new InternalJMSClient(vo);
            logger.info("向节点"+serviceProtocalInfo.getNodeCode()+",IP地址为：" + po.getTgzdz().value + "的JMS组件连接工厂为："+po.getLjgc().value+"，队列的jndi名为：" + po.getJndim().value+"发送");
            return client;*/
        }else if (type == ProtocolType.HESSIAN) {
            HessianProtocalInfo po = (HessianProtocalInfo)routeProtocalInfo.getProtocalInfo();
            if (po==null){
                throw new ESBBaseCheckedException("路由表中：路由ID"+routeProtocalInfo.getRouteProtocalInfoID()+"、协议类型"+type+"协议ID"+routeProtocalInfo.getProtocalID()+"的协议信息没在在协议信息中配置");
            }
            BaseHessianClient client = new BaseHessianClient(po.getProviderUrls());
            return client;
        } else if (type == ProtocolType.SOCKET) {
            SocketProtocalInfo po = (SocketProtocalInfo)routeProtocalInfo.getProtocalInfo();
            try {
                SocketClient sc = new SocketClient(po.getProviderUrls());
                return sc;
            } catch (Exception e) {
                logger.error(e);
                throw new ESBBaseCheckedException(e.getMessage(),e);
            }
        }else  if (type == ProtocolType.IBMMQ){/*
            //ESBMessageRequest req = (ESBMessageRequest) message;
            String transferMode = message.getTransferMode();
            //List<FileInfoTypeVO> lstAttachment = req.getDataExchangeVO().getExpandInfo().getFileInfoList();
            MqConfigInfo mci = MqConfigInfo.getInstance();
            // modify by shijianzhong
            String mqxyUUID = RouteCache.getInstance().getMapRouteConf().get(serviceProtocalInfo.getRouteConf()).getXyuuid().value;

//            String nodeUUID = RouteCache.getInstance().getMapRouteConf().get(serviceProtocalInfo.getRouteConf()).getXygjyjd().value;
//            if(nodeUUID==null||nodeUUID.trim().equals(""))
//                throw new ESBBaseCheckedException("F0502006201");
//            QzMqXyPO po = mci.getMqConfigInfo(nodeUUID);
            if(mqxyUUID==null||mqxyUUID.trim().equals(""))
                throw new ESBBaseCheckedException("F0502006201");

            QzMqXyPO po = mci.getMqConfigInfoByXyUUID(mqxyUUID);
            MQMessageParmsVO messageParms = new MQMessageParmsVO();
            messageParms.setHostName(po.getTgzdz().value);
            messageParms.setChannelName(po.getTdmc().value);
            messageParms.setQueueManagerName(po.getDlglqmc().value);

            if(message.isHasAttachment()){//有附件
                messageParms.setQueueName(po.getWjcsdl().value);
                messageParms.setFileTransMsgQueueName(po.getWjxxdl().value);
                messageParms.setTempPath(po.getWjfslsml().value);
                message.setTransferMode("B0");
            }else{
                if(transferMode==null||transferMode.trim().equalsIgnoreCase("A1")){//同步需要回复的交换方式
                    message.setTransferMode("A1");
                    messageParms.setAsync(false);
                }else{
                    message.setTransferMode("A0");
                }
                messageParms.setQueueName(po.getFsdlmc().value);

            }

            // 如果需要回复，则指定对方回复队列的队列管理器名称和队列名称
            if(!messageParms.isAsync()){
                if(null != po.getJsfhfdlglq().value && null != po.getJsfhfdlglq().value &&
                        !"".equals(po.getJsfhfdlmc().value.trim()) && !"".equals(po.getJsfhfdlmc().value.trim())){
                    // 接收方回复队列管理器名称
                    messageParms.setReplyQueueMqnagerName(po.getJsfhfdlglq().value);
                    // 接收方回复队列名称
                    messageParms.setJsfReplyQueueName(po.getJsfhfdlmc().value);
                    // 接收方回复队列名称
                    messageParms.setReplyQueueName(po.getJsdlmc().value);
                }
                else
                    throw new ESBBaseCheckedException("GDE04FS001030");
            }
            IBMMQClient client = new IBMMQClient(messageParms);
            // 默认的解析器是ESB的外部解析器，如果需要请重新覆盖
            client.getProducer().setXmlPackageTransformer(new AipXmlTransformer());
            return client;*/
        }else  if (type == ProtocolType.FTP){
            //如果是FTP协议
            FTPProtocalInfo po = (FTPProtocalInfo)routeProtocalInfo.getProtocalInfo();
            //如果是FTP协议
            /*InternalFTPClient client = new InternalFTPClient(po.getFtpurl().value,Integer.parseInt(po.getPort().value),
                    po.getFwyh().value,po.getFwmm().value,po.getFtpwjlj().value);
            //todo:取消强制转换，加入到message里边
            String filepath = ((ESBAipAccessMessageReq)message).getService().getHead().getFilePath();
            logger.info("客户端传来的filepath:"+filepath);
            client.setFilepath(filepath);
            logger.info("获得路由信息为：向节点"+serviceProtocalInfo.getNodeCode()+",IP地址为：" + po.getFtpurl().value + "文件路径为："+po.getFtpwjlj().value+"通过FTP方式发送");
            logger.info(po.getFtpurl().value+"-"+Integer.parseInt(po.getPort().value)+"-"+po.getFwyh().value
                    +"-"+po.getFwmm().value+"-"+po.getFtpwjlj().value);

            return client;*/
        }else if(type == ProtocolType.DYNAMIC){
            DynamicProtocalInfo po = (DynamicProtocalInfo)routeProtocalInfo.getProtocalInfo();
            try {
            IBaseESBClientMessageSender client = (IBaseESBClientMessageSender)Class.forName(po.getClientClass()).newInstance();
            } catch (InstantiationException e1) {
                logger.error("动态协议规则处理器"+po.getClientClass()+"实例化时出现问题："+e1.getMessage(),e1);
                throw new ESBBaseCheckedException("动态协议规则处理器"+po.getClientClass()+"没有实例化类："+e1.getMessage(),e1);
            } catch (IllegalAccessException e1) {
                logger.error("动态协议规则处理器"+po.getClientClass()+"非法方法实例："+e1.getMessage(),e1);
                throw new ESBBaseCheckedException("动态协议规则处理器"+po.getClientClass()+"没有实例化类："+e1.getMessage(),e1);
            } catch (ClassNotFoundException e1) {
                logger.error("动态协议规则处理器"+po.getClientClass()+"没有实例化类："+e1.getMessage(),e1);
                throw new ESBBaseCheckedException("动态协议规则处理器"+po.getClientClass()+"没有实例化类："+e1.getMessage(),e1);
            }
        }else if(type == ProtocolType.EXTHANG){
            throw new ESBBaseCheckedException(message.getTranID()+"路由配置选择协议类型不正确");
        }else {
            // logger.error("此协议不支持：" + type);
            return null;
        }
        return null;
    }


}
