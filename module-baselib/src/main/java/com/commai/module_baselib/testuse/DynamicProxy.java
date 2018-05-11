package com.commai.module_baselib.testuse;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fanqi on 2018/5/7.
 * Description:动态代理学习
 * 应避免静态代理的使用，静态代理使用过度，会导致代码的冗余
 */

public class DynamicProxy implements InvocationHandler {

    private Object target;

    public DynamicProxy(Object target) {
        this.target = target;
    }


    @SuppressWarnings("unchecked")
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //do something before
        Log.d("Proxy","代理中干活,before");

        //被代理类do something
        Object result = method.invoke(target, args);

        //do something after
        Log.d("Proxy","代理中干活,after");
        return result;
    }
}
