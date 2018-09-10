package com.moudle.okhttpstudy.chain;

import android.util.Log;

import com.moudle.okhttpstudy.HttpCodec;
import com.moudle.okhttpstudy.HttpConnection;
import com.moudle.okhttpstudy.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/10.
 */

public class CallServiceInterceptor implements Interceptor {

    private static final String TAG = "CallServiceInterceptor";
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.e(TAG, "intercept: " + "通信拦截器");
        HttpCodec httpCodec = chain.mHttpCodec;
        HttpConnection connection = chain.mConnection;
        InputStream is = connection.call(httpCodec);
        //空格隔开响应状态
        String statusLine = httpCodec.readLine(is);
        Map<String,String> headers = httpCodec.readHeader(is);
        //是否保持连接
        boolean isKeepAlive = false;
        if(headers.containsKey(HttpCodec.HEAD_CONNECTION)){
            isKeepAlive = headers.get(HttpCodec.HEAD_CONNECTION).equalsIgnoreCase(HttpCodec.HEAD_VALUE_KEEP_ALIVE);
        }
        int contentLength = -1;
        if(headers.containsKey(httpCodec.HEAD_CONTENT_LENGTH)){
            contentLength = Integer.valueOf(headers.get(HttpCodec.HEAD_CONTENT_LENGTH));
        }
        //分块编码数据
        boolean isChunked = false;
        if(headers.containsKey(HttpCodec.HEAD_TRANSFER_ENCODING)){
            isChunked = headers.get(HttpCodec.HEAD_TRANSFER_ENCODING).equalsIgnoreCase(HttpCodec.HEAD_VALUE_CHUNKED);
        }
        String body = null;
        if(contentLength > 0){
            byte[] bytes = httpCodec.readBytes(is,contentLength);
            body = new String(bytes);
        }else if(isChunked){
            body = httpCodec.readChunked(is);
        }
        String[] status = statusLine.split(" ");
        connection.updateLastUseTime();
        return new Response(Integer.valueOf(status[1]),contentLength,headers,body,isKeepAlive);
    }
}
