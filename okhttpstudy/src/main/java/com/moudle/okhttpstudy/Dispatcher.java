package com.moudle.okhttpstudy;

import android.support.annotation.NonNull;
import android.util.Log;

import com.moudle.okhttpstudy.call.Call;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.moudle.okhttpstudy.call.Call;

/**
 * Created by Administrator on 2018/8/9.
 */

public class Dispatcher {

    private static final String TAG = "Dispatcher";
    //最多的同时请求
    private int maxRequests;
    //同一个host同时最多请求
    private int maxRequestPerHost;

    public Dispatcher(){this(64,2);}

    public Dispatcher(int maxRequests,int maxRequestPerHost){
        this.maxRequests = maxRequests;
        this.maxRequestPerHost = maxRequestPerHost;
    }
    private ExecutorService mExecutorService;

    /**
     *  等待执行队列
     */
    private final Deque<Call.AsyncCall> readAsyncCalls = new ArrayDeque<>();

    /**
     *  正在执行的队列
     */
    private final Deque<Call.AsyncCall> runningAsyncCalls = new ArrayDeque<>();

    public synchronized ExecutorService executorService(){
        if(mExecutorService == null){
            ThreadFactory threadFactory = new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread result = new Thread(r,"http Client");
                    return result;
                }
            };
            mExecutorService = new ThreadPoolExecutor(0,Integer.MAX_VALUE,60, TimeUnit.SECONDS
            ,new SynchronousQueue<Runnable>(),threadFactory);
        }
        return mExecutorService;
    }

    public void enqueue(Call.AsyncCall call){
        //不能超过最大请求数目，同时执行相同的host请求不超过最大的host数
        if(runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestPerHost){
            Log.e(TAG, "enqueue:  提交执行");
            runningAsyncCalls.add(call);
            executorService().execute(call);
        }else {
            Log.e(TAG, "enqueue:  等待执行");
            readAsyncCalls.add(call);
        }
    }

    /**
     *  同一host的同时请求
     * @param call
     * @return
     */
    private int runningCallsForHost(Call.AsyncCall call){
        int result = 0;
        //如果执行这个请求，则相同的host数量为result
        for (Call.AsyncCall c : runningAsyncCalls) {
           if(c.host().equals(call.host())){
               result++;
           }
        }
        return result;
    }

    /**
     *  请求结束，移除正在运行的队列
     *  并判断是否执行等待队列中的请求
     * @param asyncCall
     */
    public void finished(Call.AsyncCall asyncCall){
        synchronized (this){
            runningAsyncCalls.remove(asyncCall);
            //判断是否执行等待队列中的请求
             promoteCalls();
        }
    }

    private void promoteCalls(){
        if(runningAsyncCalls.size() >= maxRequests){
            return;
        }
        //没有等待执行请求
        if(readAsyncCalls.isEmpty()){
            return ;
        }
        for(Iterator<Call.AsyncCall> i = readAsyncCalls.iterator(); i.hasNext();){
            Call.AsyncCall call = i.next();
            //同一host同时请求达上线
            if(runningCallsForHost(call) < maxRequests){
                i.remove();
                runningAsyncCalls.add(call);
                executorService().execute(call);
            }
            //到达同时请求上线
            if(runningAsyncCalls.size() >= maxRequests){
                return ;
            }
        }
    }
}
