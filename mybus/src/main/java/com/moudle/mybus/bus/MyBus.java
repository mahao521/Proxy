package com.moudle.mybus.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/8.
 */

public class MyBus {

    private Map<Class,List<SubscribeMethod>> mCacheMap = new HashMap<>();
    private Map<Class,List<String>> mRegisterMap = new HashMap<>();
    private Map<String,List<Subscrption>> mSubscribeMap = new HashMap<>();

    private static  MyBus instance;
    private MyBus(){

    }

    public static MyBus getInstance(){
        if(instance == null){
            synchronized (MyBus.class){
                if (instance == null){
                    instance = new MyBus();
                }
            }
        }
        return instance;
    }

    /**
     *   注册
     * @param object
     */
    public void register(Object object){
        Class<?> subscribeClass = object.getClass();
        List<SubscribeMethod> subscribeMethods = findClassMethod(subscribeClass);
        List<String> strings = mRegisterMap.get(subscribeClass);
        if(strings == null){
            strings = new ArrayList<>();
            mRegisterMap.put(subscribeClass,strings);
        }
        for(SubscribeMethod submethod : subscribeMethods){
            String lable = submethod.getLable();
            if(!strings.contains(lable)){
                strings.add(lable);
            }
            //制作执行表
            List<Subscrption> listSub = mSubscribeMap.get(lable);
            if(listSub == null){
                listSub = new ArrayList<>();
                Subscrption subscrption = new Subscrption(submethod,object);
                listSub.add(subscrption);
            }
            mSubscribeMap.put(lable,listSub);
        }
    }

    /**
     *  依据类名--获取方法--获取方法上的参数---建立注解，参数类型，方法
     *
     *  很消耗性能
     * @param clas
     * @return
     */
    public  List<SubscribeMethod> findClassMethod(Class<?> clas){

        List<SubscribeMethod> subscribeMethods = mCacheMap.get(clas);
        if(subscribeMethods == null){
            subscribeMethods = new ArrayList<>();
            Method[] declaredMethods = clas.getDeclaredMethods();
            for(Method method : declaredMethods){
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                if(annotation != null){
                    String[] value = annotation.value();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for(int i = 0 ; i < value.length ; i++){
                      method.setAccessible(true);
                      subscribeMethods.add(new SubscribeMethod(value[i],method,parameterTypes));
                    }
                }
            }
            mCacheMap.put(clas,subscribeMethods);
        }
       return subscribeMethods;
    }

    /**
     *  发送事件给订阅者
     * @param lable
     * @param params
     */
    public void post(String lable,Object... params){

        List<Subscrption> subscribeList = mSubscribeMap.get(lable);
        if(subscribeList == null){
            return;
        }
        for(int i = 0; i < subscribeList.size() ; i++){
            Subscrption subscrption = subscribeList.get(i);
            Class[] parameterClass = subscrption.getSubscribeMethod().getParameterClass();
            int length = parameterClass.length;
            Object[] objects = new Object[length];
            if(params != null){
                for(int j = 0; j < length; j++){
                    if(j < params.length && parameterClass[j].isInstance(params[j])){
                        objects[j] = params[j];
                    }else {
                        objects[j] = null;
                    }
                }
            }
            try {
                subscrption.mSubscribeMethod.getMethod().invoke(subscrption.getSubscribe(),objects);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  反注册
     * @param object
     */
    public void unRigister(Object object){
        List<String> list = mRegisterMap.get(object.getClass());
        if(list != null){
            for (int i = 0; i <list.size() ; i++) {
                String label = list.get(i);
                //获取执行表中对应的所有函数
                List<Subscrption> subscrptionList = mSubscribeMap.get(label);
                if(subscrptionList != null){
                    for(int j = 0 ; j < subscrptionList.size() ; j++){
                        Subscrption subscrption = subscrptionList.get(j);
                        Object subscribe = subscrption.getSubscribe();
                        //对象是同一个删除
                        if(subscribe == object){
                           subscrptionList.remove(j);
                        }
                    }
                }
            }
        }
    }

    public void clear(){
        mSubscribeMap.clear();
        mRegisterMap.clear();
        mCacheMap.clear();
    }
}

































