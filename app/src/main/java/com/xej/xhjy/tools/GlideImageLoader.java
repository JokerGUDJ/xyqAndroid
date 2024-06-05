package com.xej.xhjy.tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.image.ImageLoadUtils;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        loadImageDefault(context,(String) path,imageView);
        LogUtils.dazhiLog("图片地址----"+path);
    }


    private void loadImageDefault(Context context, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.liveing_default_img)
                .placeholder(R.drawable.ic_image_loding)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(imageUrl).apply(options).into(imageView);
    }
    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}