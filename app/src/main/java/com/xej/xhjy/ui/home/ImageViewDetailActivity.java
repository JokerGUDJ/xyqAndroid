package com.xej.xhjy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.ui.web.WebPagerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.ui.home
 * @ClassName: ImageViewDetailActivity
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/6/12 下午4:22
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/12 下午4:22
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ImageViewDetailActivity extends BaseActivity {
    public static final String LOAD_URL = "load_image";
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_detail)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_detail);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra(LOAD_URL);
        LogUtils.dazhiLog("url---"+url);
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(url).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(mImageView);

    }

    @OnClick(R.id.iv_back)
    void back() {
        Intent intent = new Intent(ImageViewDetailActivity.this, WebPagerActivity.class);
        intent.putExtra(WebPagerActivity.LOAD_URL, "WebsiteTechnology");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishWithAnim();
    }
    @OnClick(R.id.iv_detail)
    void backList(){
        finishWithAnim();
    }


}
