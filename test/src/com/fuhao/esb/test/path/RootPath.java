package com.fuhao.esb.test.path;

import com.fuhao.esb.core.component.utils.ESBFileUtils;
import com.fuhao.esb.core.component.utils.SystemUtils;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;

/**
 * package name is  com.fuhao.esb.test.path
 * Created by fuhao on 13-12-12.
 * Project Name esb-java
 */
public class RootPath {
    public static void main(String []args){
        try {
            System.out.println(ESBFileUtils.getESBRootPath() + "/ESB.xml");
        } catch (ESBBaseCheckedException e) {
            e.printStackTrace();
        }
    }
}
