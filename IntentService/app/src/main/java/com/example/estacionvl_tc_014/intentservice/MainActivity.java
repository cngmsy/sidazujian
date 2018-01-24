package com.example.estacionvl_tc_014.intentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.estacionvl_tc_014.intentservice.services.ContadorService;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button a1,a3,a2;
    BroadcastReceiver activityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("activity", intent.getStringExtra("msg"));
            Toast.makeText(context, intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
            unregisterReceiver(activityReceiver);
        }
    };
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter = new IntentFilter("activity");


        a1 = (Button) findViewById(R.id.btn_a1);
        a2 = (Button) findViewById(R.id.btn_a2);
        a3 = (Button) findViewById(R.id.btn_a3);

        a1.setOnClickListener(this);
        a2.setOnClickListener(this);
        a3.setOnClickListener(this);
    }
    public void sendBroadcast(View view) {
        //指明action属性值
        Intent intent = new Intent("service");
        intent.putExtra("msg", "activity向广播传递一个hello broadcast");
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ContadorService.class);
        switch (v.getId()){
            case R.id.btn_a1:
                intent.setAction(ContadorService.ACTION_1);
                registerReceiver(activityReceiver, filter);
                break;
            case R.id.btn_a2:
                intent.setAction(ContadorService.ACTION_2);
                registerReceiver(activityReceiver, filter);
                break;
            case R.id.btn_a3:
                intent.setAction(ContadorService.ACTION_3);
                registerReceiver(activityReceiver, filter);
                break;
        }
        startService(intent);

    }
}
