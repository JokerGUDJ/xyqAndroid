package com.xej.xhjy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.PagerViewBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.ui.home
 * @ClassName: ImageViewPagerActivity
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/6/12 下午4:21
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/12 下午4:21
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ImageViewPagerActivity extends BaseActivity {
    public static final String IMGINDEX = "img_index";
    @BindView(R.id.iv_back_list)
    ImageView iv_back;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.iv_left_back)
    ImageView iv_left_back;
    @BindView(R.id.iv_right_back)
    ImageView iv_right_back;
    @BindView(R.id.iv_hand)
    ImageView iv_animation;
    private List<String> networkImages;
    private List<String> networkContent;


    private final int FAKE_BANNER_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ButterKnife.bind(this);
        getviewPager();
        Animation();

    }

    @OnClick(R.id.iv_back_list)
    void back() {
        finishWithAnim();
    }

    private void Animation() {
        Animation scaleAnimation = new ScaleAnimation(1, 0.8f, 1, 0.8f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
        scaleAnimation.setRepeatMode(Animation.RESTART);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setDuration(1000);
        iv_animation.startAnimation(scaleAnimation);
    }

    /**
     * 获取首页轮播图
     */
    public void getviewPager() {
        networkImages = new ArrayList<>();
        networkContent = new ArrayList<>();
        String TAG3 = "image_viewpager";
        mActivity.addTag(TAG3);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.IMAGE_VIEW_PAGER, TAG3, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    LogUtils.dazhiLog("轮播图-----》" + jsonString);
                    PagerViewBean bean = JsonUtils.stringToObject(jsonString, PagerViewBean.class);
                    if (bean != null && bean.getCode().equals("0")) {
                        if (bean.getContent().size() > 0) {
                            for (int i = 0; i < bean.getContent().size(); i++) {
                                networkImages.add(NetConstants.IMAGE_VIEWPAGER + bean.getContent().get(i).getTitlePic());
                                networkContent.add(NetConstants.IMAGE_VIEWPAGER + bean.getContent().get(i).getContentPic());
                            }
                            BannerAdapter mBannerAdapter = new BannerAdapter(ImageViewPagerActivity.this);
                            mViewPager.setAdapter(mBannerAdapter);
                            mViewPager.setOnPageChangeListener(mBannerAdapter);
                            mViewPager.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    int action = event.getAction();
                                    if (action == MotionEvent.ACTION_DOWN
                                            || action == MotionEvent.ACTION_MOVE) {
                                    } else if (action == MotionEvent.ACTION_UP) {
                                    }
                                    return false;
                                }
                            });
                            String index = getIntent().getStringExtra(IMGINDEX);
                            if (!TextUtils.isEmpty(index)) {
                                mViewPager.setCurrentItem(Integer.parseInt(index));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
            }
        });
    }


    private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private LayoutInflater mInflater;

        public BannerAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= networkImages.size();
            ImageView mImageView = new ImageView(ImageViewPagerActivity.this);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(ImageViewPagerActivity.this).load(networkImages.get(position)).into(mImageView);
            final int pos = position;
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ImageViewPagerActivity.this, ImageViewDetailActivity.class);
                    intent.putExtra(ImageViewDetailActivity.LOAD_URL, networkContent.get(pos));
                    startActivity(intent);
                }
            });
            container.addView(mImageView);
            return mImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            int position = mViewPager.getCurrentItem();
            if (position == 0) {
                position = networkImages.size();
                mViewPager.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = networkImages.size() - 1;
                mViewPager.setCurrentItem(position, false);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

}
