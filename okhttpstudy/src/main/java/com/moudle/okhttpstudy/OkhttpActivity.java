package com.moudle.okhttpstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.moudle.okhttpstudy.call.Call;
import com.moudle.okhttpstudy.call.ICallback;
import com.moudle.okhttpstudy.chain.Interceptor;
import com.moudle.okhttpstudy.chain.InterceptorChain;

import java.io.IOException;
import java.net.HttpURLConnection;
public class OkhttpActivity extends AppCompatActivity {

    private static final String TAG = "OkhttpActivity";
    private HttpClient mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        mClient = new HttpClient.Builder().retrys(2).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(InterceptorChain chain) throws IOException {

                Response proceed = chain.proceed();
                return proceed;
            }
        }).build();
    }

    public void get(View view){
        final Request request = new Request.Builder().get().url("http://www.kuaidi100.com/query?type=yuantong&postid=11111111111")
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new ICallback() {
            @Override
            public void onFailure(Call call, Throwable throwable) {
                throwable.toString();
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e(TAG, "onResponse:  " + response.getBody());
            }
        });
    }

    public void post(View view){
        RequestBody body = new RequestBody()
                .add("city","北京")
                .add("key","13cb58f5884f9749287abbead9c658f2");
        final Request request = new Request.Builder().post(body).url("http://restapi.amap.com/v3/weather/weatherInfo").build();
        Call call = mClient.newCall(request);
        call.enqueue(new ICallback() {
            @Override
            public void onFailure(Call call, Throwable throwable) {
                throwable.toString();
            }

            @Override
            public void onResponse(Call call, Response response) {
                String resBody = response.getBody();
                Log.i(TAG, "onResponse: " + resBody);
            }
        });
    }
}
