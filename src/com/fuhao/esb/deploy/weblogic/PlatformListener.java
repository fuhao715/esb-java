package com.fuhao.esb.deploy.weblogic;

import com.fuhao.esb.core.component.ESBPlatformManager;
import weblogic.application.ApplicationException;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

/**
 * package name is  com.fuhao.esb.deploy.weblogic
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class PlatformListener extends ApplicationLifecycleListener  {
    @Override
    public void postStart(ApplicationLifecycleEvent arg0) throws ApplicationException {
        ESBPlatformManager.startPlatform();
    }

    @Override
    public void preStop(ApplicationLifecycleEvent arg0) throws ApplicationException {
        ESBPlatformManager.stopPlatform();
    }
}
