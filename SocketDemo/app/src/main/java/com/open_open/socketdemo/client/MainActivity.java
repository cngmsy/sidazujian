package com.open_open.socketdemo.client;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.open_open.socketdemo.R;
import com.open_open.socketdemo.client.adapter.ChatAdapter;
import com.open_open.socketdemo.client.bean.Constants;
import com.open_open.socketdemo.client.bean.Transmission;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/HpWens/BaseRecyclerAdapter   通用的adapter
 */
public class MainActivity extends PermissionActivity {


    protected String[] mFilePermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private String mFilePath;
    private EditText mEtContent;
    private RecyclerView mRecyclerView;
    private RelativeLayout mRelativeProgress;
    private TextView mTvProgress;
    private Gson mGson;
    private ChatAdapter mAdapter;
    private ClientThread mClientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();

    }

    private void initDatas() {
        mGson = new Gson();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new ChatAdapter(getDefaultData()));

        mClientThread = new ClientThread(mHandler);
        mClientThread.start();

    }

    private void initViews() {

        mEtContent = (EditText) findViewById(R.id.et_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRelativeProgress = (RelativeLayout) findViewById(R.id.relative);
        mTvProgress = (TextView) findViewById(R.id.progress);
    }

    @Override
    public void requestPermissionResult(boolean allowPermission) {

    }


    /**
     * get default adapter data
     *
     * @return
     */
    public List<Transmission> getDefaultData() {
        List<Transmission> datas = new ArrayList<>();

        Transmission trans = new Transmission();
        trans.itemType = Constants.CHAT_FROM;
        trans.transmissionType = Constants.TRANSFER_STR;
        trans.content = "昆仑";
        datas.add(trans);

        trans = new Transmission();
        trans.itemType = Constants.CHAT_SEND;
        trans.transmissionType = Constants.TRANSFER_STR;
        trans.content = "英雄志";
        datas.add(trans);

        return datas;
    }




    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.RECEIVE_MSG) {
                String content = msg.obj.toString();
                Log.e("MainActivity", "handleMessage--------" + content);
                Transmission trans = mGson.fromJson(content, Transmission.class);
                if (trans.transmissionType == Constants.TRANSFER_STR) {
                    mAdapter.addData(trans);
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                }
            }
        }
    };

    /**
     * send  message
     *
     * @param v
     * 发送文本消息
     */
    public void send(View v) {
        if (mEtContent.getText().toString().equals("")) {
            Toast.makeText(this, "输入的内容不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        Transmission trans = new Transmission();
        trans.itemType = Constants.CHAT_SEND;
        trans.content = mEtContent.getText().toString();
        trans.transmissionType = Constants.TRANSFER_STR;

        Message message = new Message();
        message.what = Constants.SEND_MSG;
        message.obj = mGson.toJson(trans);

        if (mClientThread.getWriteHandler() != null) {
            mClientThread.getWriteHandler().sendMessage(message);
            mEtContent.setText("");
        }

        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }


}
