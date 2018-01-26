package com.open_open.mytxdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import static android.R.attr.type;

public class GuanKaiserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guan_kaiser);

        //mPlayerView即step1中添加的界面view
        TXCloudVideoView mView = (TXCloudVideoView) findViewById(R.id.video_view);

//创建player对象
        TXLivePlayer mLivePlayer = new TXLivePlayer(this);

//关键player对象与界面view
        mLivePlayer.setPlayerView(mView);

        String flvUrl = "http://19407.liveplay.myqcloud.com/live/19407_d4bd8d6181.flv";
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV); //推荐FLV
        mLivePlayer.stopPlay(true);
        mLivePlayer.enableHardwareDecode(true);
        mLivePlayer.startPlay(flvUrl, type);

        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();

//自动模式
//        mPlayConfig.setAutoAdjustCacheTime(true);
//        mPlayConfig.setMinAutoAdjustCacheTime(1);
//        mPlayConfig.setMaxAutoAdjustCacheTime(5);

//极速模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(1);

//流畅模式
//        mPlayConfig.setAutoAdjustCacheTime(false);
//        mPlayConfig.setCacheTime(5);

        mLivePlayer.setConfig(mPlayConfig);
//设置完成之后再启动播放

    }
}
