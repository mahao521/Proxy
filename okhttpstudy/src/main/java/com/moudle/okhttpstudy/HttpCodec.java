package com.moudle.okhttpstudy;

import android.preference.PreferenceActivity;
import android.renderscript.ScriptGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.cert.CRL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/9.
 */

public class HttpCodec {

    //回车和换行
    static final String CRLF = "\r\n";
    static final int CR = 13;
    static final int LF = 10;
    static final String SPACE = " ";
    static final String VERSION = "HTTP/1.1";
    static final String COLON = ":";


    public static final String HEAD_HOST = "Host";
    public static final String HEAD_CONNECTION = "Connection";
    public static final String HEAD_CONTENT_TYPE = "Content-Type";
    public static final String HEAD_CONTENT_LENGTH = "Content-Length";
    public static final String HEAD_TRANSFER_ENCODING = "Transfer-Encoding";

    public static final String HEAD_VALUE_KEEP_ALIVE = "Keep-Alive";
    public static final String HEAD_VALUE_CHUNKED = "chunked";

    ByteBuffer mByteBuffer;

    public HttpCodec(){
        //申请足够大的内存记录数据(一行）
        mByteBuffer = ByteBuffer.allocate(10*1024);
    }

    public void writeRequest(OutputStream out,Request request) throws IOException {
        StringBuffer protocol = new StringBuffer();

        //请求行
        protocol.append(request.getMethod());
        protocol.append(SPACE);
        protocol.append(request.getUrl().file);
        protocol.append(SPACE);
        protocol.append(VERSION);
        protocol.append(CRLF);

        //请求行
        Map<String,String> header = request.getHeaders();
        for (Map.Entry<String,String> entry : header.entrySet()){
            protocol.append(entry.getKey());
            protocol.append(COLON);
            protocol.append(SPACE);
            protocol.append(entry.getValue());
            protocol.append(CRLF);
        }
        protocol.append(CRLF);

        //http请求体 如果存在
        RequestBody body = request.getBody();
        if(body != null){
            protocol.append(body.body());
        }
        //写出
        out.write(protocol.toString().getBytes());
        out.flush();
    }

    public Map<String,String> readHeader(InputStream inputStream)throws IOException{
        HashMap<String,String> headers = new HashMap<>();
        while (true){
            String line = readLine(inputStream);
            //读取到空行，则下面是内容
            if(isEmptyLine(line)){
                break;
            }
            int index = line.indexOf(":");
            if(index > 0){
                String name = line.substring(0,index);
                // ": " 移动两位到总长度减去两个（“\r\n")
                String value = line.substring(index+2,line.length()-2);
                headers.put(name,value);
            }
        }
        return headers;
    }

    public String readLine(InputStream is) throws IOException {
        byte b;
        boolean isMabeyEofLine = false;
        //标记
        mByteBuffer.clear();
        mByteBuffer.mark();
        while((b = (byte) is.read()) != -1){
            mByteBuffer.put(b);
            //读取到/r则记录，判断下一个字节是否/n
            if(b == CR){
                isMabeyEofLine = true;
            }else if(isMabeyEofLine){
                //上一个字节是/r 并且本次读取到/n
                if(b == LF){
                    //获取目前读取的所有字节
                    byte[] lineBytes = new byte[mByteBuffer.position()];
                    //返回标记位置
                    mByteBuffer.reset();
                    mByteBuffer.get(lineBytes);
                    //清空所有index并且重新标记
                    mByteBuffer.clear();
                    mByteBuffer.mark();
                    String line = new String(lineBytes);
                    return line;
                }
            }
        }
        throw new IOException("读一行，读取错误");
    }

    boolean isEmptyLine(String line){
        return line.equals("\r\n");
    }

    public byte[] readBytes(InputStream is,int len) throws IOException {
        byte[] bytes = new byte[len];
        int readNum = 0;
        while (true){
            readNum += is.read(bytes,0,len-readNum);
            //读取完毕
            if(len == readNum){
                return bytes;
            }
        }
    }

    public String readChunked(InputStream is) throws IOException {
        int len = -1;
        boolean isEmptyData = false;
        StringBuffer chunked = new StringBuffer();
        while (true){
            //解析下一个chunked的长度
            if(len < 0){
                String line = readLine(is);
                line = line.substring(0,line.length() - 2);
                len = Integer.valueOf(line,16);
                isEmptyData = len == 0;
            }else {
                //解析长度不包括\r\n 所有+2将\r\n
                byte[] bytes = readBytes(is,len+2);
                chunked.append(new String(bytes));
                len = -1;
                if(isEmptyData){
                    return chunked.toString();
                }
            }
        }
    }

}
