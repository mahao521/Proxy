package com.moudle.okhttpstudy;

import android.os.Build;
import android.text.TextUtils;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/9.
 */

public class Request {

    private Map<String,String> headers;
    private String method;
    private HttpUrl url;
    private RequestBody mBody;


    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpUrl getUrl() {
        return url;
    }

    public void setUrl(HttpUrl url) {
        this.url = url;
    }

    public RequestBody getBody() {
        return mBody;
    }

    public void setBody(RequestBody body) {
        mBody = body;
    }

    public Request(Builder builder){
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.mBody = builder.mBody;
    }

    /**
     *  建造者模式，
     */
    public final static class Builder{

        HttpUrl url;
        Map<String,String> headers = new HashMap<>();
        String method;
        RequestBody mBody;

        public Builder url(String url){
            try {
                this.url = new HttpUrl(url);
                return this;
            } catch (MalformedURLException e) {
                throw new IllegalStateException("failed Http Url",e);
            }
        }

        public Builder addHeader(String name,String value){
            headers.put(name,value);
            return this;
        }

        public Builder removeHeader(String name){
            headers.remove(name);
            return this;
        }

        public Builder get(){
            method = "GET";
            return this;
        }

        public Builder post(RequestBody body){
            this.mBody = body;
            method = "POST";
            return this;
        }

        public Request build(){
            if(url == null){
                throw  new IllegalStateException("url == null");
            }
            if(TextUtils.isEmpty(method)){
                method = "GET";
            }
            return new Request(this);
        }
    }




}







