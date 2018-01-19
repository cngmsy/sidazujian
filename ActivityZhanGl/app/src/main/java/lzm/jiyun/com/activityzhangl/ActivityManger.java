package lzm.jiyun.com.activityzhangl;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Created by lenovo on 2018/1/19.
 */

public class ActivityManger {

    private static ActivityManger activityManger;
    private Stack<Activity> activitystack;

    private ActivityManger() {
    }
    public static  ActivityManger getScreenManager(){
        if (activityManger==null){
            activityManger = new ActivityManger();
        }
        return activityManger;
    }
    //添加Activity到栈管理
    public void ActivityAdd(Activity activity){
        if (activitystack==null){
            activitystack = new Stack<Activity>();
        }
        activitystack.add(activity);
    }
    //将指定的Activity弹出栈
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activitystack.remove(activity);
            activity = null;
        }
    }
    /**手动添加到栈中*/
    public  void AddToTack(Activity mActivity){
        Log.i("ActivityJump", "mActivity="+mActivity.getClass().getName());
        //如果当前activity是栈顶则不添加
        if (! (mActivity).equals(activityManger.topActivity())) {
            activityManger.ActivityAdd(mActivity);
        }
    }
    /**
     * 获得栈顶的activity，也就是当前界面的上一个界面*/
    public Activity topActivity() {
        if (activitystack == null || activitystack.size() == 0) {
            return null;
        }
        Activity activity = activitystack.lastElement();
        return activity;
    }
}
