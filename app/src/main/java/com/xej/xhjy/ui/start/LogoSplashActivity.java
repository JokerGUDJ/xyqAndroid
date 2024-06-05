package com.xej.xhjy.ui.start;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.ui.dialog.ClubTextHighlightDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.main.ClubMainActivty;

import io.reactivex.functions.Consumer;

/**
 * @author dazhi
 * @class LogoSplashActivity
 * @Createtime 2018/6/6 16:17
 * @description 最开始的启动页，保证应用无缝启动，没有白屏，再做事件分发
 * @Revisetime
 * @Modifier
 */
public class LogoSplashActivity extends BaseActivity {
    //三个权限必须获取的state，每次请求检查权限，缺一不可。0为未请求，1为成功，2位拒绝
    private int readState = 0, readStorage = 0, writeStorage = 0;
    //防止多次回调
    private boolean isUsed, isShowMessage, isTime;

    private void goNext(){
        if (isTime && !isUsed && readState == 1 && readStorage == 1 && writeStorage == 1) {//权限全部获取到进入
            isUsed = true;
            startActivityWithAnim(new Intent(LogoSplashActivity.this, ClubMainActivty.class));
            finish();
        } else if (!isShowMessage && (readState == 2 || readStorage == 2 || writeStorage == 2)) {//权限有被拒绝的
            isShowMessage = true;
            showMessage();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决华为手机从安装页面启动应用程序，进入后台以后，点击桌面图标，重新启动应用程序的问题。
        // 判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
        if (!this.isTaskRoot()) {
            // 如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        //启动页设置全屏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //第一次使用app需要提示协议
        boolean isFirst = PerferenceUtils.get(AppConstants.IS_FIRST, true);
        if (isFirst) {
            isFirstOpen(this);
        }else{
            init();
        }
    }

    private void init(){
        PerferenceUtils.put(AppConstants.IS_FIRST, false);
        //动态权限管理
        RxPermissions subscribe = new RxPermissions(LogoSplashActivity.this);
        subscribe.requestEach(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.name.equals(Manifest.permission.READ_PHONE_STATE)) {
                            if (permission.granted) {
                                readState = 1;
                            } else {
                                readState = 2;
                            }
                            //当权限获取成功时，permission.granted=true
                            LogUtils.dazhiLog(Manifest.permission.READ_PHONE_STATE + "：" + permission.granted);
                        } else if (permission.name.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            if (permission.granted) {
                                readStorage = 1;
                            } else {
                                readStorage = 2;
                            }
                            LogUtils.dazhiLog(Manifest.permission.READ_EXTERNAL_STORAGE + "：" + permission.granted);
                        } else if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            LogUtils.dazhiLog(Manifest.permission.WRITE_EXTERNAL_STORAGE + "：" + permission.granted);
                            if (permission.granted) {
                                writeStorage = 1;
                            } else {
                                writeStorage = 2;
                            }
                        }else if (permission.name.equals(Manifest.permission.CAMERA)) {
                            LogUtils.dazhiLog(Manifest.permission.CAMERA + "：" + permission.granted);
                            if (permission.granted) {
                                writeStorage = 1;
                            } else {
                                writeStorage = 2;
                            }
                        }
                        goNext();
                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isTime = true;
                goNext();
            }
        }, 2000);
    }

    private void isFirstOpen(Context context) {
        ClubTextHighlightDialog dialog = new ClubTextHighlightDialog(context);
        dialog.setTitle("温馨提示");
        dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
        dialog.setPositiveListener("同意", new PositiveListener() {
            @Override
            public void onPositiveClick() {
                init();
            }
        });
        dialog.setNegativeListener("取消", new NegativeListener() {
            @Override
            public void onNegativeClick() {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        //设置点击屏幕不消失
        dialog.setCanceledOnTouchOutside(false);
        //设置点击返回键不消失
        dialog.setCancelable(false);
        dialog.show();

    }
    /**
     * 权限提示
     */
    private void showMessage() {
        String message = "鑫和家园未获得手机状态权限,将影响功能使用，请在设置中修改";
        if (readState == 2) {
            message = "鑫和家园未获得手机状态权限，将影响功能使用，请在设置中修改";
        } else if (readStorage == 2 || writeStorage == 2) {
            message = "鑫和家园未获得手机存储权限，将影响功能使用，请在设置中修改";
        }
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(this);
        dialog.setMessage(message);
        dialog.setPositiveListener("确定", new PositiveListener() {
            @Override
            public void onPositiveClick() {
                finish();
            }
        });
        dialog.show();
    }
}
