package com.moudle.okhttpstudy.chain;

import android.util.Log;

import com.moudle.okhttpstudy.HttpCodec;
import com.moudle.okhttpstudy.HttpConnection;
import com.moudle.okhttpstudy.Request;
import com.moudle.okhttpstudy.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/10.
 */

public class HeaderInterceptor implements Interceptor {

    private static final String TAG = "HeaderInterceptor";
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.d(TAG, "intercept: ");
        Request request = chain.mCall.getRequest();
        Map<String,String> headers = request.getHeaders();
        headers.put(HttpCodec.HEAD_HOST,request.getUrl().getHost());
        headers.put(HttpCodec.HEAD_CONNECTION, HttpCodec.HEAD_VALUE_KEEP_ALIVE);
        if(null != request.getBody()){
            String contentType = request.getBody().contentType();
            if(contentType != null){
                headers.put(HttpCodec.HEAD_CONTENT_TYPE,contentType);
            }
            long contentLength = request.getBody().contentLength();
            if(contentLength != -1){
                headers.put(HttpCodec.HEAD_CONTENT_LENGTH,Long.toString(contentLength));
            }
        }
        return chain.proceed();
    }

}
