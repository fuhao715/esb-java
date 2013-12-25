package com.fuhao.esb.core.component.sequence;

import com.fuhao.esb.core.component.AbsESBComponentManager;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.core.component.sequence
 * Created by fuhao on 13-12-23.
 * Project Name esb-java
 */
public abstract class AbsSequenceComponentManager extends AbsESBComponentManager {
    private String datasource;
    private boolean generateRandomByGUID = true;

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public boolean isGenerateRandomByGUID() {
        return generateRandomByGUID;
    }

    public void setGenerateRandomByGUID(boolean generateRandomByGUID) {
        this.generateRandomByGUID = generateRandomByGUID;
    }

    public abstract IBillCodeContext createContext(String djbs, String xgry) throws ESBBaseCheckedException;

}

