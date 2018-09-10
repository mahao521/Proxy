package com.moudle.okhttpstudy.chain;

import com.moudle.okhttpstudy.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2018/8/10.
 */

public interface Interceptor {

    Response intercept(InterceptorChain chain) throws IOException;
}
