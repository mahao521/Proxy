package com.moudle.proxy.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.moudle.proxy.netproxy.HttpRequest;
import com.moudle.proxy.netproxy.ICallback;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2018/8/8.
 */

public class AsyncHttpRequest implements HttpRequest {

    private AsyncHttpClient mAsyncHttpClient;
    public AsyncHttpRequest(){
        mAsyncHttpClient = new AsyncHttpClient();
    }

    @Override
    public void get(String url, Map<String, String> params, final ICallback callback) {
        StringBuffer sb = new StringBuffer("?");
        //拼接url
        if(params != null){
            for (Map.Entry<String,String> entry : params.entrySet()){
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append("&");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        url+=sb.toString();
        mAsyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                callback.onSuccess(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onFailure(statusCode,error);
            }
        });
    }

    @Override
    public void post(String url, Map<String, String> params, ICallback callback) {

    }
}
