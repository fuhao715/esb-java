package com.fuhao.esb.core.component;

import com.fuhao.esb.core.component.service.IMessageSender;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

import java.util.Map;

/**
 * package name is  com.fuhao.esb.core.component
 * Created by fuhao on 13-12-13.
 * Project Name esb-java
 */
public class ESBComponentManager extends AbsESBComponentManager implements IESBComponentManager {

    private ESBLogUtils logger = ESBLogUtils.getLogger(this.getClass());
    private static Map<String, String> mapParameter = null;
    public static Map<String, String> getInitMapParameter() {
        return mapParameter;
    }

    private volatile boolean isRefreshRemoteRouteInfoNow = false;

    /* (non-Javadoc)
     * @see com.css.ESB.kernel.base.component.AbsESBComponentManager#reload()
     */
    @Override
    public void reload() throws ESBBaseCheckedException {
        // RouteCache.getInstance().clean();
        start(mapParameter);
    }

    /* (non-Javadoc)
     * @see com.css.ESB.kernel.base.component.AbsESBComponentManager#start(java.util.Map)
     */
    @Override
    public void start(Map<String, String> parameter)
            throws ESBBaseCheckedException {  /*
        state = ComponentState.STARTING;
        try{
            ESBComponentRef.esbManager = this;
            mapParameter = parameter;
            String sDefaultAccesHandler = mapParameter.get("default_access_handler");//默认统一接入组件
            if (sDefaultAccesHandler ==null)
                sDefaultAccesHandler = ESBAipInboundManager.class.getName();//ESB的缺省接入组件
            AccessHandlerManager.setAccessHandler(sDefaultAccesHandler);
            String esbDS= parameter.get("ESBEsbDs");
            String logDBSeprate= parameter.get("logDBSeprate");
            if(null != esbDS && !("".equals(esbDS))){
                ESBPersistenceUtils.setESB_DS(esbDS);
                //只有配置为1表示和ESB数据源独立,即使用默认数据源
                ESBPersistenceUtils.setLogDBSeprate("1".equals(logDBSeprate));
            }
            String nodeName = ESBServerContext.getNodeName();
            if (nodeName == null){
                String msg = "ESB.xml中没有配置nodeName，请配置";
                logger.error(msg);
                throw new ESBBaseCheckedException(msg);
            }
            ESBNodeConfigInfo.getInstance().setCurrentNodeBM(nodeName);
            QzJyjdPO qzJyjdPO = (QzJyjdPO)ESBServiceUtils.callService("esb_getNodeInforByJdbm", nodeName);//mapParameter.get("nodeid")
            if (qzJyjdPO == null){
                String msg = "当前节点"+nodeName+"没有在数据库中配置，将以文件ESB.xml中配置nodeName为节点标识";
                logger.info(msg);
                ESBNodeConfigInfo.getInstance().setCurrentNodeID(nodeName);
                ESBNodeConfigInfo.getInstance().setCurrentNodeName(nodeName);
                ESBNodeConfigInfo.getInstance().setOrgID(mapParameter.get("orgid"));
            }else{
                ESBNodeConfigInfo.getInstance().setCurrentNodeID(qzJyjdPO.getJyjduuid().value);
                ESBNodeConfigInfo.getInstance().setCurrentNodeName(qzJyjdPO.getJyjdmc().value);
                ESBNodeConfigInfo.getInstance().setOrgID(qzJyjdPO.getOrgid().value);
            }
            //缓存路由
            ESBServiceUtils.callService("esb_loadRoute", new Object[]{});//采用服务方式调用，以免连接不释放

            String totalFlowNumber = mapParameter.get("total_flow_number");
            if (totalFlowNumber !=null)
                ESBNodeConfigInfo.getInstance().setTotalFlowNumber(Integer.parseInt(totalFlowNumber));
            String isFlowControl = mapParameter.get("is_flow_control");
            if("Y".equals(isFlowControl)){
                FlowControlManager.initInstance();// 启用流控组件
            }
			*//*返回码初始化 *//*
            ReturnCodeManager.initReturnCodeManager();
			*//*路由自动刷新 *//*
            new RouteRefreshManager().start();
            // 注册JMX管理器
            ESBPlatformMXBeanManager.registerMBean(null, "ServiceRouteManager", this);
            logger.info("ESB启动成功");
            state = ComponentState.RUNNING;
        } catch(ESBBaseCheckedException e){
            logger.info("ESB启动失败");
            state = ComponentState.ERROR;
            throw e;
        } catch(Throwable thr){
            logger.info("ESB启动失败");
            state = ComponentState.ERROR;
            throw new ESBBaseCheckedException("ESB启动失败",thr);
        }  */
    }

    /* (non-Javadoc)
     * @see com.css.ESB.kernel.base.component.AbsESBComponentManager#stop()
     */
    @Override
    public void stop() throws ESBBaseCheckedException {
       // RouteCache.getInstance().clean();
    }

    @Override
    public IMessageSender getMessageSender() {
        return null ;// new InternalMessageSender();
    }
}
