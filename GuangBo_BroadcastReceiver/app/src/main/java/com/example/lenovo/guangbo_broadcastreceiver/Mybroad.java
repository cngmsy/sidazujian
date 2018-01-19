package com.example.lenovo.guangbo_broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lenovo on 2018/1/17.
 */
public class Mybroad extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String resultData = getResultData();
        Log.e("经理",resultData);
//        abortBroadcast();
        setResultData("50万");
    }
}
