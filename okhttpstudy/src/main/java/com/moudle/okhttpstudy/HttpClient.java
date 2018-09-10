package com.moudle.okhttpstudy;

import com.moudle.okhttpstudy.call.Call;
import com.moudle.okhttpstudy.chain.Interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/9.
 */

public class HttpClient  {

    private Dispatcher mDispatcher;
    private ConnectPool mConnectPool;
    private int retrys;
    private List<Interceptor> interceptors;

    public HttpClient(Builder builder) {
        mDispatcher = builder.mDispatcher;
        mConnectPool = builder.mConnectPool;
        this.retrys = builder.retrys;
        this.interceptors = builder.mInterceptors;
    }

    public Call newCall(Request request){
        return new Call(this,request);
    }

    public Dispatcher getDispatcher() {
        return mDispatcher;
    }

    public ConnectPool getConnectPool() {
        return mConnectPool;
    }

    public int getRetrys() {
        return retrys;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public static final class Builder{
        /**
         *  队列 任务分发
         */
        Dispatcher mDispatcher;
        ConnectPool mConnectPool;
        //默认重试次数
        int retrys = 3;
        List<Interceptor> mInterceptors = new ArrayList<>();

        public Builder dispatcher(Dispatcher dispatcher){
            this.mDispatcher = dispatcher;
            return this;
        }

        public Builder connectionPool(ConnectPool connectPool){
            this.mConnectPool = connectPool;
            return this;
        }

        public Builder retrys(int retrys){
            this.retrys = retrys;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor){
            mInterceptors.add(interceptor);
            return this;
        }

        public HttpClient build(){
            if(null == mDispatcher){
                mDispatcher = new Dispatcher();
            }
            if(null == mConnectPool){
                mConnectPool = new ConnectPool();
            }
            return new HttpClient(this);
        }
    }

}
