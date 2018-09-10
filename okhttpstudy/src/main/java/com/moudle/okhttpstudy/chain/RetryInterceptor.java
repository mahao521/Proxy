package com.moudle.okhttpstudy.chain;

import android.util.Log;

import com.moudle.okhttpstudy.Response;
import com.moudle.okhttpstudy.call.Call;

import java.io.IOException;

/**
 * Created by Administrator on 2018/8/10.
 */

public class RetryInterceptor implements Interceptor {

    private static final String TAG = "RetryInterceptor";
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {

        Log.e(TAG, "intercept: "+"重试拦截器");
        Call call = chain.mCall;
        IOException exception = null;
        for(int i = 0; i < chain.mCall.getClient().getRetrys();i++){
            if(call.isCanceled()){
                throw new IOException("canceled");
            }
            try {
                Response response = chain.proceed();
                return response;
            }catch (IOException e) {
                exception = e;
            }
        }
        throw exception;
    }
}
