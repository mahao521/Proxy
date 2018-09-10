package com.moudle.okhttpstudy.chain;

import com.moudle.okhttpstudy.HttpCodec;
import com.moudle.okhttpstudy.HttpConnection;
import com.moudle.okhttpstudy.Response;
import com.moudle.okhttpstudy.call.Call;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class InterceptorChain {

     List<Interceptor> interceptors;
     int index;
     Call mCall;
     HttpConnection mConnection;
     HttpCodec mHttpCodec = new HttpCodec();

     public InterceptorChain(List<Interceptor> interceptors,int index,Call call,HttpConnection connection){
         this.interceptors = interceptors;
         this.index = index;
         this.mCall = call;
         this.mConnection = connection;
     }

     public Response proceed() throws IOException{
         return proceed(mConnection);
     }

     public Response proceed(HttpConnection connection) throws IOException {
         Interceptor interceptor = interceptors.get(index);
         InterceptorChain next  = new InterceptorChain(interceptors,index + 1,mCall,connection);
         Response response = interceptor.intercept(next);
         return response;
     }
}
