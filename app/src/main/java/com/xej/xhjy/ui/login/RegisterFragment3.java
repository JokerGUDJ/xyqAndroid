package com.xej.xhjy.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.ui.main.BackToHomeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author dazhi
 * @class RegisterFragment1 注册第三个页面
 * @Createtime 2018/6/21 16:43
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class RegisterFragment3 extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_register3, null);
        ((RegisterActivity) mActivity).setBackGone();
        view.findViewById(R.id.btn_step3_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BackToHomeEvent("返回首页"));
                mActivity.finishWithAnim();
            }
        });
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageRegisterSucc", "channel=android", "eventRegisterSucc", "mobilephone="+PerferenceUtils.get(AppConstants.User.PHONE,""));
        return view;
    }
}
