package com.example.estacionvl_tc_014.intentservice.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.estacionvl_tc_014.intentservice.OkhttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ContadorService extends IntentService {

    public static final String ACTION_1="accion 1";
    public static final String ACTION_2="accion 2";
    public static final String ACTION_3="accion 3";

    public ContadorService() {
        super("ContadorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        IntentFilter filter = new IntentFilter("service");
        registerReceiver(serviceReceiver, filter);

        if(intent.getAction().equals(ACTION_1)){
            action1();
        }else if(intent.getAction().equals(ACTION_2)){
            action2();
        }else{
            action3();
        }
    }
    //定义一个广播接收者BroadcastReceiver
    BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("service", "接收到了activity发送的广播:" + intent.getStringExtra("msg"));
//发送广播给MainActivity

        }
    };

    private void action3() {

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Servicio Contador")
                .setContentText("Contando ...")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();

        startForeground(101,notification);

        for(int i = 0;i<15;i++){
            try {
                Thread.sleep(1000);
                Log.i("ContadorServicio", "Segundos:"+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stopForeground(true);

    }

    private void action2() {
        OkhttpUtils.getInstance().sendGet("https://www.baidu.com/", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("string",string);
                sendBroadcast(new Intent("activity").putExtra("msg", string));
            }
        });
        Log.i("ContadorServicio","Entro en Accion 2");
    }

    private void action1() {
        OkhttpUtils.getInstance().sendGet("https://guge.bban.top/", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("string",string);
                sendBroadcast(new Intent("activity").putExtra("msg", string));
            }
        });
        Log.i("ContadorServicio","Entro en Accion 1");
    }


}
