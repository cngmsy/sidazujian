package com.open_open.socketdemo.client;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.open_open.socketdemo.R;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 请求权限成功失败
     * @param allowPermission
     */
    public abstract void requestPermissionResult(boolean allowPermission);

    /**
     * 请求权限列表
     * @param permissions
     */
    private void checkPermissions(String... permissions){
        List<String> needRequestPermissonList =findDeniedPermissions(permissions);

    }


    /**
     * 获取多个权限的列表
     * @param permissions
     * @return
     */
    protected  List<String> findDeniedPermissions(String[] permissions){
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm:permissions) {
            //判断是否已经获取了权限ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED
            //ActivityCompat.shouldShowRequestPermissionRationale(this,perm)为了帮助查找用户可能需要解释的情形，Android 提供了一个实用程序方法，即 shouldShowRequestPermissionRationale()。如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            //注：如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false。
            if (ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED|| ActivityCompat.shouldShowRequestPermissionRationale(this,perm))

            {
                needRequestPermissonList.add(perm);

            }
        }
        return needRequestPermissonList;

    };

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults){
        for (int result: grantResults) {
            if (result !=  PackageManager.PERMISSION_GRANTED) {
                return  false;
            }
        }
        return true;
    }

    /**
     * 声明权限授权
     * @param permissions
     * @return
     */
    public boolean mayRequestPermission(String[] permissions){
        boolean mayPermission=false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String req:permissions) {
            if (checkSelfPermission(req) != PackageManager.PERMISSION_GRANTED) {
                mayPermission = false;
                break;
            } else {
                mayPermission = true;
            }
        }
        if (mayPermission) {
            return true;
        }
        checkPermissions(permissions);
        return false;
    }




    /**
     * @param requestCode   申请的权限回调结果
     * @param permissions
     * @param grantResults
     *
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (verifyPermissions(grantResults)) {
               requestPermissionResult(true);
            } else {
                requestPermissionResult(false);
                showMissingPermissionDialog();
            }
        }

    }


    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    public void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }


                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:"+getPackageName()));
        startActivity(intent);

    }


}
