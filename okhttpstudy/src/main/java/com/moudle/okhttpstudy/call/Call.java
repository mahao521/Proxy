package com.moudle.okhttpstudy.call;

import com.moudle.okhttpstudy.HttpClient;
import com.moudle.okhttpstudy.Request;
import com.moudle.okhttpstudy.Response;
import com.moudle.okhttpstudy.chain.CallServiceInterceptor;
import com.moudle.okhttpstudy.chain.ConnectionInterceptor;
import com.moudle.okhttpstudy.chain.HeaderInterceptor;
import com.moudle.okhttpstudy.chain.Interceptor;
import com.moudle.okhttpstudy.chain.InterceptorChain;
import com.moudle.okhttpstudy.chain.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/9.
 */

public class Call {

    Request mRequest;
    HttpClient mClient;

    public Request getRequest() {
        return mRequest;
    }

    public HttpClient getClient() {
        return mClient;
    }

    /**
     *  是否执行过
     */
    boolean canceled;
    boolean executed;

    public Call(HttpClient client,Request request){
        this.mClient = client;
        this.mRequest = request;
    }

    public void cancel(){
        canceled = true;
    }

    public boolean isCanceled(){
        return canceled;
    }

    Response getResponse() throws IOException{
        ArrayList<Interceptor> interceptors = new ArrayList<>();
        interceptors.addAll(mClient.getInterceptors());
        interceptors.add(new RetryInterceptor());
        interceptors.add(new HeaderInterceptor());
        interceptors.add(new ConnectionInterceptor());
        interceptors.add(new CallServiceInterceptor());
        InterceptorChain interceptorChain = new InterceptorChain(interceptors,0,this,null);
        return interceptorChain.proceed();
    }

    public Call enqueue(ICallback callback){
        //不能重复执行
        synchronized (this){
            if(executed){
                throw  new IllegalStateException("Already executed");
            }
            executed = true;
        }
        mClient.getDispatcher().enqueue(new AsyncCall(callback));
        return this;
    }

    final public class AsyncCall implements Runnable{

        private ICallback mCallback;
        public  AsyncCall(ICallback callback){
           this.mCallback = callback;
        }

        @Override
        public void run() {
            //是否已经通知过callBack
            boolean singaledCallback = false;
            try {
                Response response = getResponse();
                if(canceled){
                    singaledCallback = true;
                    mCallback.onFailure(Call.this,new IOException("Canceled"));
                }else {
                    singaledCallback = true;
                    mCallback.onResponse(Call.this,response);
                }
            } catch (IOException e) {
                if(!singaledCallback){
                    mCallback.onFailure(Call.this,e);
                }
            }finally {
                mClient.getDispatcher().finished(this);
            }
        }

        public String host(){
            return mRequest.getUrl().getHost();
        }
    }
}
