package com.open_open.mytxdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class ZhiBoActivity extends AppCompatActivity {
    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_bo);
        String sdkver = TXLiveBase.getSDKVersionStr();
        Log.e("liteavsdk", "liteav sdk version is : " + sdkver);

        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.enableNearestIP(true);
        mLivePusher.setConfig(mLivePushConfig);

        String rtmpUrl = "rtmp://19407.livepush.myqcloud.com/live/19407_d4bd8d6181?bizid=19407&txSecret=1fae9cd50e81791342549765c0b3a888&txTime=5A68AD7F";
        mLivePusher.startPusher(rtmpUrl);

        TXCloudVideoView mCaptureView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLivePusher.startCameraPreview(mCaptureView);

        mLivePusher.switchCamera();

       mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION,true,true);
        mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_HARDWARE);


    }

    public void onJie(View view){
        stopRtmpPublish();
        finish();

    }


    //结束推流，注意做好清理工作
    public void stopRtmpPublish() {
        mLivePusher.stopCameraPreview(true); //停止摄像头预览
        mLivePusher.stopPusher();            //停止推流
        mLivePusher.setPushListener(null);   //解绑 listener
    }
}
