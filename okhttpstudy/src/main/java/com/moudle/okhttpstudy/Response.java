package com.moudle.okhttpstudy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/10.
 */

public class Response {

    int code;
    int cotentLength = -1;
    Map<String,String> headers = new HashMap<>();
    String body;
    //保持连接
    boolean isKeepLive;

    public Response(){
    }

    public Response(int code ,int contentLength , Map<String,String> headers,String body,boolean isKeepLive){
        this.code = code;
        this.cotentLength = contentLength;
        this.headers = headers;
        this.body = body;
        this.isKeepLive = isKeepLive;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCotentLength() {
        return cotentLength;
    }

    public void setCotentLength(int cotentLength) {
        this.cotentLength = cotentLength;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isKeepLive() {
        return isKeepLive;
    }

    public void setKeepLive(boolean keepLive) {
        isKeepLive = keepLive;
    }
}

















