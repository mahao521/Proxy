package com.moudle.mybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.moudle.mybus.bus.MyBus;
import com.moudle.mybus.bus.Subscribe;
import com.moudle.okhttpstudy.OkhttpActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_main);
        MyBus.getInstance().register(this);

        MyBus.getInstance().post("main");   //输出 null null
        MyBus.getInstance().post("main",1+""); //输出 1 null
        MyBus.getInstance().post("zhangsan"); //输出 null null
        MyBus.getInstance().post("zhangsan","haha","2"); //输出haha  2
    }

    @Subscribe("main")
    public void test(){
        Log.d(TAG, "test: ");
    }

    @Subscribe({"zhangsan","lisi"})
    public void setName(String a,String b){
        Log.d(TAG, "setName: " +a+"........" + b);
    }

    @Subscribe("main")
    public void setBook(String a,String b){
        Log.d(TAG, "setBook: " + a +"....." + b);
    }

    public void load(View view){
        Intent intent = new Intent(this, OkhttpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyBus.getInstance().unRigister(this);
    }
}
