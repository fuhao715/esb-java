package com.fuhao.esb.test.annotation;

import com.fuhao.esb.core.component.service.ESBServiceUtils;
import com.fuhao.esb.core.component.unittest.ServiceTestCase;
import com.fuhao.esb.core.exception.ESBBaseCheckedException;
import org.junit.Test;

/**
 * package name is  com.fuhao.esb.test.annotation
 * Created by fuhao on 13-12-16.
 * Project Name esb-java
 */
public class TestCallService extends ServiceTestCase {

    @Test
    public void testCall() throws ESBBaseCheckedException {
        System.out.println("----11111111111111111----");
        String str = (String)ESBServiceUtils.callService("esb_insertPO","fuhao");
        System.out.println("----2222222222222222----"+str);
    }
}
