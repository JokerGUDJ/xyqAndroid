package com.xej.xhjy.ui.mine;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dazhi
 * @class RegisterActivity 修改手机号的activity
 * @Createtime 2018/6/21 16:12
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class ResetPhoneActivity extends BaseActivity {

    private List<Fragment> mFragments = new ArrayList<>();
    private int mCurrentStepIndex;
    private ResetPhoneFragment1 mFragment1;
    private ResetPhoneFragment2 mFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_phone);
        initView();
    }

    private void initView() {
        mFragment1 = new ResetPhoneFragment1();
        mFragment2 = new ResetPhoneFragment2();
        mFragments.add(mFragment1);
        mFragments.add(mFragment2);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ll_fragment_contain, mFragment1);
        ft.commit();
    }

    /**
     * 开始切换页
     *
     * @param index
     */
    public void switchToFragment(int index) {
        Fragment fragment = mFragments.get(index);
        FragmentTransaction ft = getFragmentTransaction(index);

        mFragments.get(mCurrentStepIndex).onPause();
        if (fragment.isAdded()) {
            fragment.onResume();
        } else {
            ft.add(R.id.ll_fragment_contain, fragment);
        }
        showFragment(index);
        ft.commit();
    }

    /**
     * 切换页并改变页头和页尾状态
     *
     * @param index
     */
    private void showFragment(int index) {
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            FragmentTransaction ft = getFragmentTransaction(index);

            if (index == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        mCurrentStepIndex = index;
    }

    /**
     * 设置切换动画效果
     *
     * @param index
     * @return
     */
    private FragmentTransaction getFragmentTransaction(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (index > mCurrentStepIndex) {
            ft.setCustomAnimations(R.anim.anim_right_in_3s, R.anim.anim_left_out_3s);
        } else {
            ft.setCustomAnimations(R.anim.anim_left_in_3s, R.anim.anim_right_out_3s);
        }
        return ft;
    }
}
