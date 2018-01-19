package lzm.jiyun.com.activityzhangl;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainBActivity extends BaseActivity {

    @InjectView(R.id.activity_main_b)
    RelativeLayout activityMainB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MainBactivity", "onCreate");
        setContentView(R.layout.activity_main_b);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.activity_main_b)
    public void onViewClicked() {
        ActivityManger.getScreenManager().popActivity(MainBActivity.this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainBactivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainBactivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainBactivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainBactivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainBactivity", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MainBactivity", "onRestart");
    }
}
