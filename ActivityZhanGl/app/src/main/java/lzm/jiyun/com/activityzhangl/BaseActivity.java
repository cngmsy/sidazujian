package lzm.jiyun.com.activityzhangl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lenovo on 2018/1/19.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityManger.getScreenManager().AddToTack(this);
        super.onCreate(savedInstanceState);

    }
}
