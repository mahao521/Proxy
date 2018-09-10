package com.moudle.okhttpstudy;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Administrator on 2018/8/9.
 */

public class HttpConnection {

    Socket mSocket;
    InputStream mInputStream;
    OutputStream mOutputStream;
    long lastUsetime;
    Request mRequest;
    static final String HTTPS = "https";

    public void setRequest(Request request){
        this.mRequest = request;
    }

    public boolean isSameAddress(String host,int port){
        if(mSocket == null){
            return false;
        }
        return TextUtils.equals(mSocket.getInetAddress().getHostName(),host)
                && port == mSocket.getPort();
    }

    /**
     *   HTTPS = Http + ssL
     * @throws IOException
     */
    public void createSocket() throws IOException {
        if(null == mSocket || mSocket.isClosed()){
            HttpUrl url = mRequest.getUrl();
            if(url.protocol.equalsIgnoreCase(HTTPS)){
                mSocket = SSLSocketFactory.getDefault().createSocket();
            }else {
                mSocket = new Socket();
            }
            mSocket.connect(new InetSocketAddress(url.host,url.port));
            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();
        }
    }

    public InputStream call(HttpCodec httpCodec) throws IOException {
      try {
          createSocket();
          //写出请求
          httpCodec.writeRequest(mOutputStream,mRequest);
          return mInputStream;
      }catch (IOException e){
          throw  new IOException(e);
      }
    }

    public void cloaseQuitely(){
            try {
                if(mSocket != null){
                    mSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void updateLastUseTime(){
        //更新最后的使用时间
        lastUsetime = System.currentTimeMillis();
    }
}
