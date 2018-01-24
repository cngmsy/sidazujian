package com.example.lenovo.guangbo_broadcastreceiver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.Button)
    android.widget.Button Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.Button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Button:
                Intent intent=new Intent();
                intent.setAction("com.jiyun");
                //有序广播
                sendOrderedBroadcast(intent,null,null,null,0,"一百万",null);
                break;
        }
    }
}
