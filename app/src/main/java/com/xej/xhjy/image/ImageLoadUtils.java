package com.xej.xhjy.image;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.xej.xhjy.R;

/**
 * @author dazhi
 * @class ImageUtils
 * @Createtime 2018/6/5 09:05
 * @description 图片加载工具，使用Glide，有默认的加载，有自定义加载，有圆形加载，有圆角加载
 * @Revisetime
 * @Modifier
 */
public class ImageLoadUtils {

    /**
     * Activity使用，默认错误图，占位图，淡入淡出动画
     *
     * @param activity       activity使用
     * @param imageUrl       图片地址
     * @param imageView      需要加载图片的控件
     * @param errorResouseID 错误资源ID
     * @param loadResouseID  加载中占位资源ID
     */
    public static void loadImageDefault(Activity activity, String imageUrl, ImageView imageView, int errorResouseID, int loadResouseID) {
        RequestOptions options = new RequestOptions()
                .error(errorResouseID)
                .placeholder(loadResouseID);
        Glide.with(activity).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    /**
     * Activity使用，默认错误图，占位图，淡入淡出动画
     *
     * @param activity  activity使用
     * @param imageUrl  图片地址
     * @param imageView 需要加载图片的控件
     */
    public static void loadImageDefault(Activity activity, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_image_error)
                .placeholder(R.drawable.ic_image_loding);
        Glide.with(activity).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    /**
     * Fragment使用，默认错误图，占位图，淡入淡出动画
     *
     * @param fragment  fragment使用
     * @param imageUrl  图片地址
     * @param imageView 需要加载图片的控件
     */
    public static void loadImageDefault(Fragment fragment, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        Glide.with(fragment).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    /**
     * Fragment使用，默认错误图，占位图，淡入淡出动画
     *
     * @param context  fragment使用
     * @param imageUrl  图片地址
     * @param imageView 需要加载图片的控件
     */
    public static void loadImageDefault(Context context, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_image_error)
                .placeholder(R.drawable.ic_image_loding)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(imageUrl).apply(options).into(imageView);
    }

    public static void loadImageRadius(Context activity, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_image_error)
                .placeholder(R.drawable.ic_image_loding)
                .transform(new GlideRoundTransform(activity,5));
        Glide.with(activity).load(imageUrl).apply(options).into(imageView);
    }

    /**
     * Activity使用，默认错误图，占位图，无动画
     *
     * @param activity  activity使用
     * @param imageUrl  图片地址
     * @param imageView 需要加载图片的控件
     */
    public static void loadImageNoAnim(Activity activity, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_image_error)
                .placeholder(R.drawable.ic_image_loding)
                .transform(new GlideRoundTransform(activity,5));
        Glide.with(activity).load(imageUrl).apply(options).into(imageView);
    }

    /**
     * Fragment使用，默认错误图，占位图，无动画
     *
     * @param fragment  fragment使用
     * @param imageUrl  图片地址
     * @param imageView 需要加载图片的控件
     */
    public static void loadImageNoAnim(Fragment fragment, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_image_error)
                .placeholder(R.drawable.ic_image_loding);
        Glide.with(fragment).load(imageUrl).apply(options).into(imageView);
    }

    /**
     * 加载图片，不适用缓存，Activity使用
     *
     * @param activity  传入Activity
     * @param imageUrl  图片地址
     * @param imageView 控件
     */
    public static void showHttpImage(Activity activity, String imageUrl, ImageView imageView, int onLoadingRes, int onErrorRes) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(onErrorRes)
                .placeholder(onLoadingRes);
        Glide.with(activity).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }


    /**
     * 加载圆形图片，默认占位图，错误图，Activity使用
     *
     * @param activity  传入Activity
     * @param imageUrl  图片地址
     * @param imageView 控件
     */
    public static void showHttpImageCycle(Activity activity, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .error(R.drawable.ic_image_error)
                .placeholder(R.drawable.ic_image_loding);
        Glide.with(activity).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    /**
     * 加载圆形图片，Activity使用
     *
     * @param activity     传入Activity
     * @param imageUrl     图片地址
     * @param imageView    控件
     * @param onLoadingRes 加载时占位图
     * @param onErrorRes   错误图片
     */
    public static void showHttpImageCycle(Activity activity, String imageUrl, ImageView imageView, int onLoadingRes, int onErrorRes) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .error(onErrorRes)
                .placeholder(onLoadingRes);
        Glide.with(activity).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    /**
     * 加载圆形图片不使用缓存，Activity使用
     *
     * @param activity     传入Activity
     * @param imageUrl     图片地址
     * @param imageView    控件
     * @param onLoadingRes 加载时占位图
     * @param onErrorRes   错误图片
     */
    public static void showHttpImageCycleNoCache(Activity activity, String imageUrl, ImageView imageView, int onLoadingRes, int onErrorRes) {
        String updateTime = String.valueOf(System.currentTimeMillis());
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop()
                .error(onErrorRes)
                .placeholder(onLoadingRes)
                .signature(new ObjectKey(updateTime));

        Glide.with(activity).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    /**
     * 加载圆角图片，传入圆角大小，默认占位图，错误图，Activity使用
     *
     * @param activity  传入Activity
     * @param imageUrl  图片地址
     * @param imageView 控件
     * @param angleDp   圆角大小，dp
     */
    public static void showHttpImageRound(Activity activity, String imageUrl, ImageView imageView, int angleDp) {
        RequestOptions options = new RequestOptions()
                .transform(new GlideRoundTransform(activity, angleDp))
                .circleCrop()
                .error(R.drawable.ic_user_default_icon)
                .placeholder(R.drawable.ic_user_default_icon);
        Glide.with(activity).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    /**
     * 加载圆形图片，传入圆角大小，Activity使用
     *
     * @param activity     传入Activity
     * @param imageUrl     图片地址
     * @param imageView    控件
     * @param onLoadingRes 加载时占位图
     * @param onErrorRes   错误图片
     * @param angleDp      圆角大小，dp
     */
    public static void showHttpImageRound(Activity activity, String imageUrl, ImageView imageView, int onLoadingRes, int onErrorRes, int angleDp) {
        RequestOptions options = new RequestOptions()
                .transform(new GlideRoundTransform(activity, angleDp))
                .circleCrop()
                .error(onErrorRes)
                .placeholder(onLoadingRes);
        Glide.with(activity).load(imageUrl).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }
}
