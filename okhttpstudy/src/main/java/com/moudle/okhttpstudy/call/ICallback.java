package com.moudle.okhttpstudy.call;


import com.moudle.okhttpstudy.Response;

/**
 * Created by Administrator on 2018/8/9.
 */

public interface ICallback {

    void onFailure(Call call,Throwable throwable);

    void onResponse(Call call, Response response);
}
