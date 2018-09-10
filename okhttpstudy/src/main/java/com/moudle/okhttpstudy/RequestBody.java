package com.moudle.okhttpstudy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/9.
 */

public class RequestBody {

    /**
     *  表单提交 使用Urlencode编码
     */
    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private final static String CHARSET = "utf-8";

    Map<String,String> encodeBodys = new HashMap<>();

    public String contentType(){
        return CONTENT_TYPE;
    }

    public long contentLength(){
        return body().getBytes().length;
    }

    public String body(){
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String,String> entry : encodeBodys.entrySet()){
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        if(sb.length() != 0){
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public RequestBody add(String name , String value){
        try {
            encodeBodys.put(URLEncoder.encode(name,CHARSET),URLEncoder.encode(value,CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }
}












