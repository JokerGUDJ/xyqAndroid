package com.xej.xhjy.ui.metting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentTransaction;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.netease.yunxin.nertc.ui.team.TeamG2Activity;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.service.AppService;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.receiver.NetWorkStatusEvent;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginOutEvent;
import com.xej.xhjy.ui.view.TextviewTobTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.prototypez.appjoint.AppJoint;

public class MyMeetingsActivity extends BaseActivity {
    private LinearLayout ll_av_meeting, ll_add_meeting;
    private static final int REQUEST_VIDEO_CONTACT = 20213;
    private ImageView img_back;
    private TextviewTobTabView tab_All_meeting;
    private TextviewTobTabView tab_online_meeting;
    private TextviewTobTabView tab_offline_meeting;
    private OnlineMeetingFragment onlineMeetingFragment;
    private OfflineMeetingFragment offlineMeetingFragment;
    private int tab = 0;

    /**
     * 初始化view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_my_metting);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initTab();
//        ll_av_meeting = mTitleView.findViewById(R.id.ll_av_meeting);
//        ll_av_meeting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<String> disableAccounts = new ArrayList<>();
//                Intent intent = new Intent(mActivity, ContactListSelectAtivity.class);
//                intent.putExtra(ContactListSelectAtivity.FLAG, "1");
//                intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST, disableAccounts);
//                startActivityForResultWithAnim(intent, REQUEST_VIDEO_CONTACT);
//            }
//        });
        img_back = findViewById(R.id.head_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithAnim();
            }
        });
    }

    private void initTab(){

        tab_offline_meeting = findViewById(R.id.tb_offline_meeting);
        tab_online_meeting = findViewById(R.id.tb_online_meeting);
        tab_offline_meeting.setText("线下会议");
        tab_offline_meeting.setCheck(false);
        tab_offline_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab = 0;
                select(tab);
            }
        });
        tab_online_meeting.setText("线上会议");
        tab_online_meeting.setCheck(false);
        tab_online_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab = 1;
                select(tab);
            }
        });
        select(tab);
    }

    private void select(int i) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hidtFragment(ft);
        switch (i) {
            case 0:
                tab_online_meeting.setCheck(false);
                tab_offline_meeting.setCheck(true);
                if (offlineMeetingFragment == null) {
                    offlineMeetingFragment = new OfflineMeetingFragment(this, AppConstants.MY_MEETING);
                    ft.add(R.id.container_fg, offlineMeetingFragment);
                } else {
                    ft.show(offlineMeetingFragment);
                }

                break;
            case 1:
                tab_offline_meeting.setCheck(false);
                tab_online_meeting.setCheck(true);
                if (onlineMeetingFragment == null) {
                    onlineMeetingFragment = new OnlineMeetingFragment(this, AppConstants.MY_MEETING);
                    ft.add(R.id.container_fg, onlineMeetingFragment);
                } else {
                    ft.show(onlineMeetingFragment);
                }

                break;

        }
        //提交事务
        ft.commit();
    }

    //隐藏所有Fragment
    private void hidtFragment(FragmentTransaction fragmentTransaction) {
        if (offlineMeetingFragment != null) {
            fragmentTransaction.hide(offlineMeetingFragment);
        }
        if (onlineMeetingFragment != null) {
            fragmentTransaction.hide(onlineMeetingFragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 收到登录成功的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEventMainThread(LoginEvent event) {
        if(tab == 0){
            offlineMeetingFragment.autoRefresh();
        }else if(tab == 1){
            onlineMeetingFragment.autoRefresh();
        }
    }

    /**
     * 收到登出的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginOutEventMainThread(LoginOutEvent event) {
        if(tab == 0){
            offlineMeetingFragment.autoRefresh();
        }else if(tab == 1){
            onlineMeetingFragment.autoRefresh();
        }
    }

    /**
     * 收到网络状态变化消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetWorkEvent(NetWorkStatusEvent event) {
        if(tab == 0){
            offlineMeetingFragment.autoRefresh();
        }else if(tab == 1){
            onlineMeetingFragment.autoRefresh();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
