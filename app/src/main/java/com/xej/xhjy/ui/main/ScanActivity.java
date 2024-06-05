package com.xej.xhjy.ui.main;

import android.view.View;

import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;

/**
 * @class ScanActivity
 * @author dazhi
 * @Createtime 2018/8/2 09:35
 * @description 二维码扫描二次定制，去掉相册选择二维码
 * @Revisetime
 * @Modifier
 */
public class ScanActivity  extends CaptureActivity{
    @Override
    protected void onResume() {
        super.onResume();
        findViewById(com.github.shenyuanqing.zxingsimplify.R.id.ll_album).setVisibility(View.GONE);
    }
}

