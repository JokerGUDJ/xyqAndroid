package com.xej.xhjy.ui.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.ui.view.OnBackClickListener;
import com.xej.xhjy.ui.view.TitleView;

import io.reactivex.functions.Consumer;

/**
 * @class RegistStartActivity  用户注册引导页
 * @author dazhi
 * @Createtime 2018/10/24 11:01
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class RegisterStartActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_start);
        TitleView titleView = findViewById(R.id.titleview);
        titleView.setBackListener(new OnBackClickListener() {
            @Override
            public void backClick() {
                finishWithAnim();
            }
        });
        findViewById(R.id.img_scan_start).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RegisterActivity.class);
                startActivity(intent);
                finishWithAnim();
            }
        });
    }
}
