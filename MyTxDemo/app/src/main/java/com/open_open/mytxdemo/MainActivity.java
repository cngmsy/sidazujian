package com.open_open.mytxdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.zhibo)
    Button zhibo;
    @Bind(R.id.guankan)
    Button guankan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.zhibo, R.id.guankan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zhibo:
                Intent intent = new Intent(MainActivity.this, ZhiBoActivity.class);
                startActivity(intent);
                break;
            case R.id.guankan:
                Intent intent1 = new Intent(MainActivity.this, GuanKaiserActivity.class);
                startActivity(intent1);
                break;
        }
    }


}
