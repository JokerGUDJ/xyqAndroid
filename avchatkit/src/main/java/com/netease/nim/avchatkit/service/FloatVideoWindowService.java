package com.netease.nim.avchatkit.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.netease.nim.avchatkit.R;
import com.netease.nim.avchatkit.teamavchat.activity.TeamAVChatActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class FloatVideoWindowService extends Service {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private LayoutInflater inflater;
    private int seconds;
    private Timer timer;
    private TextView time;

    //constant
    private boolean clickflag;

    //view
    private View mFloatingLayout;    //浮动布局

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            if ( msg.what == 0 ) {
                // 比如隐藏悬浮框
                time.setText(time2String(seconds));
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(intent != null){
            seconds = intent.getIntExtra("seconds", 0);
            startTimer();
        }
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public FloatVideoWindowService getService() {
            return FloatVideoWindowService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initWindow();//设置悬浮窗基本参数（位置、宽高等）
    }

    /**
     * 设置悬浮框基本参数（位置、宽高等）
     */
    private void initWindow() {
        mWindowManager = (WindowManager)(getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        wmParams = getParams();//设置好悬浮窗的参数
        // 悬浮窗默认显示以左上角为起始坐标
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0
        wmParams.x = 70;
        wmParams.y = 210;
        wmParams.format = PixelFormat.RGBA_8888;
        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(getApplicationContext());
        // 获取浮动窗口视图所在布局
        mFloatingLayout = inflater.inflate(R.layout.layout_float_view, null);
        time = mFloatingLayout.findViewById(R.id.tv_call_time);
        // 添加悬浮窗的视图
       showFloatingWindow();
    }


    private WindowManager.LayoutParams getParams() {
        wmParams = new WindowManager.LayoutParams();
        //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置可以显示在状态栏上
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return wmParams;
    }


    private void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//判断系统版本
            if (Settings.canDrawOverlays(this)) {
                mFloatingLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), TeamAVChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                });
                mFloatingLayout.setOnTouchListener(new FloatingListener());
                mWindowManager.addView(mFloatingLayout, wmParams);
            }
        } else {
            mFloatingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TeamAVChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
            mFloatingLayout.setOnTouchListener(new FloatingListener());
            mWindowManager.addView(mFloatingLayout, wmParams);
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
        time.setText(time2String(seconds));
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            seconds++;
            Message message = new Message();
            message.what = 0;
            mHandler.sendMessage(message);
        }
    };

    private String time2String(int seconds){
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format(Locale.CHINA, "%02d:%02d", m, s);
    }

    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private int mTouchStartX, mTouchStartY, mTouchCurrentX, mTouchCurrentY;
    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private int mStartX, mStartY, mStopX, mStopY;
 //判断悬浮窗口是否移动，这里做个标记，防止移动后松手触发了点击事件
    private boolean isMove;

    private class FloatingListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    mStartX = (int) event.getX();
                    mStartY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchCurrentX = (int) event.getRawX();
                    mTouchCurrentY = (int) event.getRawY();
                    wmParams.x += mTouchCurrentX - mTouchStartX;
                    wmParams.y += mTouchCurrentY - mTouchStartY;
                    mWindowManager.updateViewLayout(mFloatingLayout, wmParams);

                    mTouchStartX = mTouchCurrentX;
                    mTouchStartY = mTouchCurrentY;
                    break;
                case MotionEvent.ACTION_UP:
                    mStopX = (int) event.getX();
                    mStopY = (int) event.getY();
                    if (Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1) {
                        isMove = true;
                    }
                    break;
            }

            //如果是移动事件不触发OnClick事件，防止移动的时候一放手形成点击事件
            return isMove;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingLayout != null && mFloatingLayout.isAttachedToWindow()) {
            // 移除悬浮窗口
            mWindowManager.removeView(mFloatingLayout);
        }
        if(timer != null){
            timer.cancel();
        }
    }
}