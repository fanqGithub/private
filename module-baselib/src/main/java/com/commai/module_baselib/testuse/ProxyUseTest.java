package com.commai.module_baselib.testuse;

/**
 * Created by fanqi on 2018/5/7.
 * Description:
 */

public class ProxyUseTest {

    private void test1(){
        DynamicProxy proxy=new DynamicProxy(new UserImp());
        Action doAction=proxy.getProxy();
        doAction.say("动态");
    }
}