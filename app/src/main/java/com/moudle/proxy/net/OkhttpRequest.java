package com.moudle.proxy.net;

import com.moudle.proxy.netproxy.HttpRequest;
import com.moudle.proxy.netproxy.ICallback;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/8.
 */

public class OkhttpRequest implements HttpRequest{

    @Override
    public void get(String url, Map<String, String> params, ICallback callback) {

    }

    @Override
    public void post(String url, Map<String, String> params, ICallback callback) {

    }
}
