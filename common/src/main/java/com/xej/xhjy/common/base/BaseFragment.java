package com.xej.xhjy.common.base;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.xej.xhjy.common.R;

/**
 * @class BaseFragment 基类BaseFragment
 * @author dazhi
 * @Createtime 2018/6/21 19:37
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class BaseFragment extends Fragment {
    public BaseActivity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity)activity;
    }
    /**带动画启动*/
    public void startActivityWithAnim(Intent intent) {
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /**带动画启动*/
    public void startActivityForResultWithAnim(Intent intent,int code) {
        startActivityForResult(intent,code);
        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
