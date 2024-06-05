package com.xej.xhjy.ui.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.xej.xhjy.R;
import com.xej.xhjy.bean.CardScanResultBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.ui.view.OnBackClickListener;
import com.xej.xhjy.ui.view.TitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @class RegisterActivity 注册的activity
 * @author dazhi
 * @Createtime 2018/6/21 16:12
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class RegisterActivity extends BaseActivity {

    private List<Fragment> mFragments = new ArrayList<>();
    private int mCurrentStepIndex;
    private RegisterFragment1 mFragment1;
    private RegisterFragment2 mFragment2;
    private RegisterFragment3 mFragment3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView(){
        CardScanResultBean bean = (CardScanResultBean)getIntent().getSerializableExtra("scan_result");
        mFragment1 = new RegisterFragment1();
        if (bean !=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("scan_result",bean);
            mFragment1.setArguments(bundle);
        }
        mFragment2 = new RegisterFragment2();
        mFragment3 = new RegisterFragment3();
        mFragments.add(mFragment1);
        mFragments.add(mFragment2);
        mFragments.add(mFragment3);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ll_fragment_contain, mFragment1);
        ft.commit();
    }


    /**
     * 获取第一步页面的资料数据
     * @return
     */
    public Map<String,String> getData(){
        return mFragment1.getData();
    }

    /**
     * 获取第一步页面填写的手机号
     * @return
     */
    public String getMobilePhone(){
        return mFragment1.getMobilePhone();
    }

    /**
     * 隐藏返回按钮
     * @return
     */
    public void setBackGone(){
        ((TitleView)findViewById(R.id.titleview)).setBackVisibile(false);
    }

    /**
     * 开始切换页
     * @param index
     */
    public void switchToFragment(int index) {
        Fragment fragment = mFragments.get(index);
        FragmentTransaction ft = getFragmentTransaction(index);

        mFragments.get(mCurrentStepIndex).onPause();
        if(fragment.isAdded()){
            fragment.onResume();
        } else {
            ft.add(R.id.ll_fragment_contain, fragment);
        }
        showFragment(index);
        ft.commit();
    }
    /**
     * 切换页并改变页头和页尾状态
     * @param index
     */
    private void showFragment(int index){
        for(int i = 0; i < mFragments.size(); i++){
            Fragment fragment = mFragments.get(i);
            FragmentTransaction ft = getFragmentTransaction(index);

            if(index == i){
                ft.show(fragment);
            }else{
                ft.hide(fragment);
            }
            ft.commit();
        }
        mCurrentStepIndex = index;
    }
    /**
     * 设置切换动画效果
     * @param index
     * @return
     */
    private FragmentTransaction getFragmentTransaction(int index){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(index > mCurrentStepIndex){
            ft.setCustomAnimations(R.anim.anim_right_in_3s, R.anim.anim_left_out_3s);
        }else{
            ft.setCustomAnimations(R.anim.anim_left_in_3s, R.anim.anim_right_out_3s);
        }
        return ft;
    }
}
