package com.xej.xhjy.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xej.xhjy.common.R;


/**
 * @author dazhi
 * @class BaseFragmentForViewpager  为viewpager专用的Fragment
 * @Createtime 2018/6/12 15:39
 * @description 使用懒加载来加载数据，只有等Fragment显示出来再进行异步数据加载
 * 所以有两个实现方法，一个实现初始化数据，一个实现异步数据
 * initview里要首先初始化根布局mRootView
 * @Revisetime
 * @Modifier
 */
public abstract class BaseFragmentForViewpager extends Fragment{
    /**
     * 所依附的activity
     */
    protected BaseActivity mActivity;
    /**
     * 根布局
     */
    protected View mRootView;
    /**
     * Fragment的View加载完毕的标记
     */
    private boolean isViewCreated;
    /**
     * Fragment对用户可见的标记
     */
    private boolean isUIVisible;
    /**
     * Fragment第一次加载的标记
     */
    private boolean isFirstLoad;

    /**
     * 初始化页面控件及主线程数据
     */
    public abstract void initView();

    /**
     * 初始化网络请求等异步线程数据
     */
    public abstract void initDatas();

    /**
     * Api23以下onAttach(Context context)不执行，故还是用这个老api
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstLoad = true;
        initView();
    }

    /**
     * 懒加载数据，第一次只有界面显示后再加载数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            firstLoadData();
        } else {
            isUIVisible = false;
        }
    }

    /**
     * 避免每次显示fragment都走此方法而重复加载数据，故所有一些初始化都在oncreat里执行，这里只返回根布局即可
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup group = (ViewGroup) mRootView.getParent();
        if (group != null) {
            group.removeView(mRootView);
        }
        isViewCreated = true;
        firstLoadData();
        return mRootView;
    }

    /**
     * 带动画启动
     */
    public void startActivityForResultWithAnim(Intent intent, int code) {
        startActivityForResult(intent, code);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 懒加载数据，第一次只有界面显示后再加载异步数据
     */
    private void firstLoadData() {
        if (isFirstLoad && isUIVisible && isViewCreated) {
            isFirstLoad = false;
            initDatas();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
