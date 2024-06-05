package com.xej.xhjy.common.service;

import android.app.Activity;
import android.widget.ImageView;

public interface AppService {

    //获取头像url
    String getHeadImgUrl();
    //显示用户头像
    void showHeadImagUrl(Activity mActivity, ImageView mHeadImg, String url);
}
