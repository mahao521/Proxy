package com.moudle.proxy.netproxy;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/8/8.
 */

public abstract class  ModelCallBack<T> implements ICallback {

    @Override
    public void onSuccess(String result) {
        Class<? extends T> geneticClass = getGeneticClass(this);
        T t = new Gson().fromJson(result,geneticClass);
        //重定向到新的success函数
        onSuccess(t);
    }

    /**
     *   获取泛型
     * @param object
     * @return
     */
    protected Class<? extends T> getGeneticClass(Object object){
        //带有泛型的直接父类，
        Type genericSuperClass = object.getClass().getGenericSuperclass();
        //ParameterizedType 带参数的类型 泛型
        //getActualArguments参数的类型，泛型类型
        return (Class<? extends T>)((ParameterizedType)genericSuperClass).getActualTypeArguments()[0];
    }
    public abstract void onSuccess(T t);
}
