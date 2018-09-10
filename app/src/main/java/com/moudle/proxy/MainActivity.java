package com.moudle.proxy;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moudle.proxy.bean.WeatherInfo;
import com.moudle.proxy.net.AsyncHttpRequest;
import com.moudle.proxy.netproxy.HttpRequestPresenter;
import com.moudle.proxy.netproxy.ModelCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 *   代理模式： 1 ：  代理人，2 : 代理对象； 3 : 代理人的方法  4 : 使用者；
 *
 *   代理模式目的： 暴露代理人公开方法给使用者； 隐藏代理人的私有方法，隐藏内部实现；
 *
 *   1 ： 代理人 和  代理对象 都要实现 代理人的方法
 *
 *   2 ： 代理对象持有代理人的引用；
 *
 *   3 ： 用户持有代理对象的引用； 用户操作代理对象，代理对象操作代理人。 最终传递一个回调接口。
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView mTxtWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtWeather = findViewById(R.id.txt_weather);
        mTxtWeather.setOnClickListener(this);
        HttpRequestPresenter.init(new AsyncHttpRequest());
        Map<String,String> params = new HashMap<>();
        params.put("city","北京");
        params.put("key","13cb58f5884f9749287abbead9c658f2");
        HttpRequestPresenter.getInsatnce().get("http://restapi.amap.com/v3/weather/weatherInfo"
                , params, new ModelCallBack<WeatherInfo>() {
                    @Override
                    public void onSuccess(WeatherInfo weatherInfo) {
                        mTxtWeather.setText(weatherInfo.getLives().get(0).getProvince());
                    }

                    @Override
                    public void onFailure(int code, Throwable t) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        ComponentName name = new ComponentName("com.moudle.proxy","com.moudle.mybus.MainActivity");
        intent.setComponent(name);
        startActivity(intent);
    }
}
