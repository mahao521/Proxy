package com.moudle.mybus.bus;

/**
 * Created by Administrator on 2018/8/8.
 */

public class Subscrption {

    SubscribeMethod mSubscribeMethod;

    Object subscribe;

    public SubscribeMethod getSubscribeMethod() {
        return mSubscribeMethod;
    }

    public void setSubscribeMethod(SubscribeMethod subscribeMethod) {
        mSubscribeMethod = subscribeMethod;
    }

    public Object getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Object subscribe) {
        this.subscribe = subscribe;
    }

    public Subscrption(SubscribeMethod subscribeMethod, Object subscribe) {
        mSubscribeMethod = subscribeMethod;
        this.subscribe = subscribe;

    }


}
