package com.moudle.proxy.netproxy;

import java.util.Map;

import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by Administrator on 2018/8/8.
 */

public class HttpRequestPresenter implements HttpRequest{

    public  static HttpRequestPresenter instance;
    private  HttpRequest mRequest;
    private HttpRequestPresenter (HttpRequest request){
        mRequest = request;
    }
    public static void init(HttpRequest request){
        if(instance == null){
            synchronized (HttpRequestPresenter.class){
                if(instance == null){
                    instance = new HttpRequestPresenter(request);
                }
            }
        }
    }

    public static HttpRequestPresenter getInsatnce(){
        return instance;
    }

    @Override
    public void get(String url, Map<String, String> params, ICallback callback) {
        mRequest.get(url,params,callback);
    }

    @Override
    public void post(String url, Map<String, String> params, ICallback callback) {
       mRequest.post(url,params,callback);
    }
}
