package com.xej.xhjy.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.ui.adapter.PageAdatper;

import java.lang.reflect.Field;
import java.util.List;


public class CustomBanner<T> extends FrameLayout {

    private Context mContext;

    private CustomViewPager mBannerViewPager;
    private PageAdatper<T> mAdapter;
    private CustomViewPagerScroller mScroller;
    private long mIntervalTime = 5000;

    private Drawable mIndicatorSelectDrawable;
    private Drawable mIndicatorUnSelectDrawable;
    private int mIndicatorInterval;

    private int mBannerCount;
    private boolean isTurning;
    private MyCallBack myCallBack;

    private OnPageClickListener mOnPageClickListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    private Handler mTimeHandler = new Handler();
    private Runnable mTurningTask = new Runnable() {
        @Override
        public void run() {
            if (isTurning && mBannerViewPager != null) {
                int page = mBannerViewPager.getCurrentItem() + 1;
                mBannerViewPager.setCurrentItem(page);
                mTimeHandler.postDelayed(mTurningTask, mIntervalTime);
            }
        }
    };
    public CustomBanner(@NonNull Context context) {
        super(context);
    }
    public CustomBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init(context);
    }

    public CustomBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        init(context);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_banner);
            int gravity = mTypedArray.getInt(R.styleable.custom_banner_indicatorGravity, 3);

            mIndicatorInterval = mTypedArray.getDimensionPixelOffset(
                    R.styleable.custom_banner_indicatorInterval, (int) GenalralUtils.dp2px(context,5));
            int indicatorSelectRes = mTypedArray.getResourceId(
                    R.styleable.custom_banner_indicatorSelectRes, 0);
            int indicatorUnSelectRes = mTypedArray.getResourceId(
                    R.styleable.custom_banner_indicatorUnSelectRes, 0);
            if (indicatorSelectRes != 0) {
                mIndicatorSelectDrawable = context.getResources().getDrawable(indicatorSelectRes);
            }
            if (indicatorUnSelectRes != 0) {
                mIndicatorUnSelectDrawable = context.getResources().getDrawable(indicatorUnSelectRes);
            }
            mTypedArray.recycle();
        }
    }

    private void init(Context context) {
        mContext = context;
        addBannerViewPager(context);
        // addIndicatorLayout(context);
    }

    private void addBannerViewPager(Context context) {
        mBannerViewPager = new CustomViewPager(context);
        mBannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetP) {
                if (!isMarginal(position) && mOnPageChangeListener != null && mBannerCount > 1) {
                    mOnPageChangeListener.onPageScrolled(getActualPosition(position),
                            positionOffset, positionOffsetP);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (!isMarginal(position) && mOnPageChangeListener != null && mBannerCount > 1) {
                    mOnPageChangeListener.onPageSelected(getActualPosition(position));
                }
                if (position >= 0 && position < mAdapter.getCount()&& myCallBack != null) {
                    myCallBack.onCallBack(getActualPosition(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int position = mBannerViewPager.getCurrentItem();
                if (!isMarginal(position) && mOnPageChangeListener != null&& mBannerCount > 1) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
                if (state == ViewPager.SCROLL_STATE_IDLE && mBannerCount > 1) {
                    if (position == 0) {
                        mScroller.setSudden(true);
                        mBannerViewPager.setCurrentItem(mAdapter.getCount() - 2, true);
                        mScroller.setSudden(false);
                    } else if (position == mAdapter.getCount() - 1) {

                        mScroller.setSudden(true);
                        mBannerViewPager.setCurrentItem(1, true);
                        mScroller.setSudden(false);
                    }
                }
            }
        });
        replaceViewPagerScroll();
        this.addView(mBannerViewPager);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            startTurning(mIntervalTime);
        } else {
            stopTurning();
        }
    }

    private boolean isMarginal(int position) {
        return position == 0 || position == getCount() + 1;
    }

    /**
     * 设置轮播图数据
     *
     * @param creator 创建和更新轮播图View的接口
     * @param data    轮播图数据
     * @return
     */
    public CustomBanner<T> setPages(ViewCreator<T> creator, List<T> data, MyCallBack myCallBack) {
        mAdapter =  new PageAdatper<T>(mContext, creator, data);
        if (mOnPageClickListener != null) {
            mAdapter.setOnPageClickListener(mOnPageClickListener);
        }
        this.myCallBack = myCallBack;
        mBannerViewPager.setAdapter(mAdapter);
        if (data == null) {
            mBannerCount = 0;
        } else {
            mBannerCount = data.size();
        }
        if(mBannerCount <= 1){
            mBannerViewPager.setbScroll(false);//一张不让滑动
        }else{
            mBannerViewPager.setbScroll(true);
        }
        setCurrentItem(0);
        return this;
    }

    public boolean getIsTurning(){
        return isTurning;
    }

    /**
     * 启动轮播
     *
     * @param intervalTime 轮播间隔时间
     * @return
     */
    public CustomBanner<T> startTurning(long intervalTime) {
        if (isTurning || mBannerCount == 1) {
            //stopTurning();
            return this;
        }
        isTurning = true;
        mIntervalTime = intervalTime;
        mTimeHandler.postDelayed(mTurningTask, mIntervalTime);
        return this;
    }

    /**
     * 停止轮播
     *
     * @return
     */
    public CustomBanner<T> stopTurning() {
        isTurning = false;
        mTimeHandler.removeCallbacks(mTurningTask);
        return this;
    }

    public int getCount() {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return 0;
        }
        return mAdapter.getCount() - 2;
    }

    public CustomBanner setCurrentItem(int position) {
        if (position >= 0 && position < mAdapter.getCount()) {
            mBannerViewPager.setCurrentItem(position + 1);
        }
        return this;
    }

    public int getCurrentItem() {
        return getActualPosition(mBannerViewPager.getCurrentItem());
    }

    public int getActualPosition(int position) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return -1;
        }

        if (position == 0) {
            return getCount() - 1;
        } else if (position == getCount() + 1) {
            return 0;
        } else {
            return position - 1;
        }
    }

    /**
     * 通过反射替换掉mBannerViewPager的mScroller属性。
     * 这样做是为了改变和控件ViewPager的滚动速度。
     */
    private void replaceViewPagerScroll() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            mScroller = new CustomViewPagerScroller(mContext,
                    new AccelerateInterpolator());
            field.set(mBannerViewPager, mScroller);
        } catch (Exception e) {
        }
    }

    /**
     * 设置轮播图的滚动速度
     *
     * @param scrollDuration
     */
    public CustomBanner<T> setScrollDuration(int scrollDuration) {
        mScroller.setScrollDuration(scrollDuration);
        return this;
    }

    public int getScrollDuration() {
        return mScroller.getScrollDuration();
    }

    public CustomBanner setOnPageChangeListener(ViewPager.OnPageChangeListener l) {
        mOnPageChangeListener = l;
        return this;
    }

    public CustomBanner<T> setOnPageClickListener(OnPageClickListener l) {
        if (mAdapter != null) {
            mAdapter.setOnPageClickListener(l);
        }

        mOnPageClickListener = l;
        return this;
    }

    /**
     * 通知数据刷新
     */
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface OnPageClickListener<T> {
        void onPageClick(int position, T t);
    }
}
