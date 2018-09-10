package com.moudle.okhttpstudy;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/8/9.
 */

public class ConnectPool {

    private static final String TAG = "ConnectPool";
    /**
     *  垃圾回收机制
     *  设置为守护线程(主线程退出后，没有用户线程则会自动销毁）
     */
    private static ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread result = new Thread(r,"http connect pool");
            result.setDaemon(true);
            return result;
        }
    };

    private static final Executor execctur = new ThreadPoolExecutor(0,Integer.MAX_VALUE,60L
    , TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),threadFactory);

    /**
     *  每个链接的最大存活时间
     */
    private  long keepLiveDuration;

    private final Runnable cleanRunnable = new Runnable() {
        @Override
        public void run() {
            while(true){
                long waitTime = cleanup(System.currentTimeMillis());
                if(waitTime == -1){  //没有连接，直接返回
                    return ;
                }
                if(waitTime > 0){
                    synchronized (ConnectPool.this){
                        try {
                            ConnectPool.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };


    private final Deque<HttpConnection> mHttpConnections = new ArrayDeque<>();
    private boolean cleanupRunning;
    public ConnectPool(){
        this(1,TimeUnit.MINUTES);
    }
    public ConnectPool(long keepLiveDuration,TimeUnit timeUnit){
        this.keepLiveDuration = timeUnit.toMillis(keepLiveDuration);
    }

    public HttpConnection get(String host, int port){
        Iterator<HttpConnection> iterator = mHttpConnections.iterator();
        while (iterator.hasNext()){
            HttpConnection connection = iterator.next();
            //检查是否复用同样的host
            if(connection.isSameAddress(host,port)){
                //正在使用的移出连接池
                iterator.remove();
                return connection;
            }
        }
        return null;
    }

    public void put(HttpConnection connection){
        //执行检测清理
        if(!cleanupRunning){
            cleanupRunning = true;
            execctur.execute(cleanRunnable);
        }
        mHttpConnections.add(connection);
    }


    /**
     *  检查需要移除的连接 返回下次的检查时间
     * @param now
     * @return
     */
    long cleanup(long now){
        long longestIdleDuration = -1;
        synchronized (this){
            for (Iterator<HttpConnection> i = mHttpConnections.iterator(); i.hasNext();){
                HttpConnection connection = i.next();
                //获取闲置时间 ，多长时间没有使用这个了
                long idleDuration = now - connection.lastUsetime;
                //如果闲置时间超过允许
                if(idleDuration > keepLiveDuration){
                    connection.cloaseQuitely();
                    i.remove();
                    Log.d(TAG, "cleanup: " + "移除了连接池");
                    continue;
                }
                //获取最大闲置时间
                if (longestIdleDuration < idleDuration){
                    longestIdleDuration = idleDuration;
                }
            }
            //下次检查时间
            if(longestIdleDuration >= 0){
                return keepLiveDuration - longestIdleDuration;
            }else {
                //连接池没有链接 可以退出
                cleanupRunning = false;
                return longestIdleDuration;
            }
        }
    }


}
