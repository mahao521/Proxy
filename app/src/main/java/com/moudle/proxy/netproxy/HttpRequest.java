package com.moudle.proxy.netproxy;

import java.util.Map;

/**
 * Created by Administrator on 2018/8/8.
 */

public interface HttpRequest {

    void get(String url, Map<String,String> params,ICallback callback);

    void post(String url,Map<String,String> params,ICallback callback);
}
