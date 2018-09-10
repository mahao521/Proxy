package com.moudle.okhttpstudy.chain;

import android.util.Log;

import com.moudle.okhttpstudy.HttpClient;
import com.moudle.okhttpstudy.HttpCodec;
import com.moudle.okhttpstudy.HttpConnection;
import com.moudle.okhttpstudy.HttpUrl;
import com.moudle.okhttpstudy.Request;
import com.moudle.okhttpstudy.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/10.
 */

public class ConnectionInterceptor implements Interceptor {

    private static final String TAG = "ConnectionInterceptor";

    @Override
    public Response intercept(InterceptorChain chain) throws IOException {

        Log.e(TAG, "intercept: "+"连接拦截器");
        Request request = chain.mCall.getRequest();
        HttpClient client = chain.mCall.getClient();
        HttpUrl url = request.getUrl();
        String host = url.getHost();
        int port = url.getPort();
        HttpConnection connection = client.getConnectPool().get(host,port);
        if(connection == null){
            connection = new HttpConnection();
        }else{
            Log.e(TAG, "intercept:  使用连接池");
        }
        connection.setRequest(request);
        Response response = chain.proceed(connection);
        if(response.isKeepLive()){
            client.getConnectPool().put(connection);
        }
        return response;
    }
}
