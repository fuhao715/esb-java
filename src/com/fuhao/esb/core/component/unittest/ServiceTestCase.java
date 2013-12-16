package com.fuhao.esb.core.component.unittest;

import com.fuhao.esb.common.request.IProtocolConf;
import com.fuhao.esb.core.component.ESBPlatformManager;
import com.fuhao.esb.core.component.ESBServerContext;
import com.fuhao.esb.core.component.utils.ESBLogUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExternalResource;

/**
 * package name is  com.fuhao.esb.core.component.unittest
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
public abstract class ServiceTestCase {

    private final ESBLogUtils log = ESBLogUtils.getLogger(this.getClass());

    @Rule
    public final ExternalResource monitor = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            ESBServerContext.getSession().clear().setProtocolType(IProtocolConf.ProtocolType.LOCAL);
        }

        @Override
        protected void after() {

        }
    };

    /**
     * 初始化方法
     */
    @BeforeClass
    public static void startUp() throws ESBBaseCheckedException {
        ESBPlatformManager.startPlatform();
    }

    /**
     * 停止清理方法
     */
    @AfterClass
    public static void tearDown() throws ESBBaseCheckedException {
        ESBPlatformManager.stopPlatform();
    }
}
