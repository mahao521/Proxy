package com.moudle.mybus.bus;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/8/8.
 */

public class SubscribeMethod {

    private String lable;

    private Method mMethod;

    private Class[] parameterClass;

    public SubscribeMethod(String lable, Method method, Class[] parameterClass) {
        this.lable = lable;
        mMethod = method;
        this.parameterClass = parameterClass;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public Method getMethod() {
        return mMethod;
    }

    public void setMethod(Method method) {
        mMethod = method;
    }

    public Class[] getParameterClass() {
        return parameterClass;
    }

    public void setParameterClass(Class[] parameterClass) {
        this.parameterClass = parameterClass;
    }
}
