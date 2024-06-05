package com.xej.xhjy.ui.live;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;

import androidx.annotation.Nullable;
import cn.jzvd.JzvdStd;
import io.reactivex.functions.Consumer;


/**
 * @Description: 直播主页面
 * @Author: lihy_0203
 * @CreateDate: 2019/6/20下午5:08
 * @UpdateUser: 更新者
 * @Version: 1.0
 */

public class LiveActivity extends BaseActivity {
    private JzvdStd jzVideoPlayerStandard;
    public static final String PLAY_URL = "play_url";
    private String url_play;
    private ImageView head_back;


    // 标题列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_player);
        url_play = getIntent().getStringExtra(PLAY_URL);
        head_back = findViewById(R.id.head_back);

        //动态权限管理
        RxPermissions subscribe = new RxPermissions(mActivity);
        subscribe.requestEach(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            jzVideoPlayerStandard = (JzvdStd) findViewById(R.id.videoplayer);
                            jzVideoPlayerStandard.setUp(url_play, "  ");
                        } else {
                            ToastUtils.shortToast(mActivity, "未获取拨音视频权限，请在设置中允许鑫和家园使用相机和音视频！");
                            return;
                        }
                    }
                });

        LogUtils.dazhiLog("视频地址=====" + url_play);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (jzVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jzVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
