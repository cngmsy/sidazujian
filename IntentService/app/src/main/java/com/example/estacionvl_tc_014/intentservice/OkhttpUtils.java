package com.example.estacionvl_tc_014.intentservice;


import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by TauchMe on 2018/1/18.
 */

public class OkhttpUtils {
    private static OkhttpUtils utils;
    private OkHttpClient client;
    private OkhttpUtils(){
       client = new OkHttpClient();
    }
    public static OkhttpUtils getInstance(){
        if (utils==null){
            synchronized (OkhttpUtils.class){
                if (utils==null){
                    utils=new OkhttpUtils();
                }
            }
        }
        return utils;
    }
    public void sendGet(String url , Callback callback ){
        Request build = new Request.Builder().url(url).build();
        client.newCall(build).enqueue(callback);
    }
}
