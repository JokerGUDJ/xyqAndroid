package com.xej.xhjy.tools;

import android.app.Activity;
import android.widget.ImageView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.service.AppService;
import com.xej.xhjy.image.ImageLoadUtils;

import io.github.prototypez.appjoint.core.ServiceProvider;

@ServiceProvider
public class AppServiceImpl implements AppService {
    @Override
    public String getHeadImgUrl() {
        return LoginUtils.getHeadImagUrl();
    }

    @Override
    public void showHeadImagUrl(Activity mActivity, ImageView mHeadImg, String url) {
        ImageLoadUtils.showHttpImageCycleNoCache(mActivity, url, mHeadImg, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
    }
}
