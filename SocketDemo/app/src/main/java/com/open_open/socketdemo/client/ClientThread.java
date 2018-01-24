package com.open_open.socketdemo.client;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.open_open.socketdemo.client.bean.Transmission;
import com.open_open.socketdemo.client.bean.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by boby on 2017/2/18.
 */

public class ClientThread extends Thread {

    PrintWriter mPrintWriter;
    BufferedReader mBufferedReader;
    Socket mSocket;

    Handler mSendHandler;
    Handler mWriteHandler;

    Gson mGson;


    public ClientThread(Handler handler) {
        mSendHandler = handler;
        mGson = new Gson();
    }

    @Override
    public void run() {
        super.run();

        try {
            //创建socket
            mSocket = new Socket(Constants.HOST, Constants.PORT);
            //获取到读写对象
            mPrintWriter = new PrintWriter(mSocket.getOutputStream());//写消息发送给服务端
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));//读取服务端发来的消息

            //新开线程读取消息 并发送消息
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String content = null;
                    try {
                        while ((content = mBufferedReader.readLine()) != null) {
                            Transmission trans = mGson.fromJson(content, Transmission.class);
                            if (trans.transmissionType == Constants.TRANSFER_STR) {
                                Message msg = new Message();
                                msg.what = Constants.RECEIVE_MSG;
                                msg.obj = content;
                                mSendHandler.sendMessage(msg);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            //当前线程创建 handler
            Looper.prepare();//handleMessage用来接收消息的
            mWriteHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constants.SEND_MSG) {
                        mPrintWriter.write(msg.obj.toString() + "\r\n");
                        mPrintWriter.flush();
                    }
                }
            };
            Looper.loop();

        } catch (IOException e) {
            e.printStackTrace();
            //出现异常关闭资源
            try {
                if (mPrintWriter != null) {
                    mPrintWriter.close();
                }
                if (mBufferedReader != null) {
                    mBufferedReader.close();
                }
                if (mSocket != null) {
                    mSocket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }


    public Handler getWriteHandler() {
        return mWriteHandler;
    }
    //接受消息的使用的handler
    public void setWriteHandler(Handler writeHandler) {
        mWriteHandler = writeHandler;
    }
    //发送消息的时候的handler
    public Handler getSendHandler() {
        return mSendHandler;
    }

    public void setSendHandler(Handler sendHandler) {
        mSendHandler = sendHandler;
    }
}
