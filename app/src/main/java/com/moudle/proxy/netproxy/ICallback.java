package com.moudle.proxy.netproxy;

/**
 * Created by Administrator on 2018/8/8.
 */

public interface ICallback {

    void onSuccess(String result);

    void onFailure(int code,Throwable t);
}
