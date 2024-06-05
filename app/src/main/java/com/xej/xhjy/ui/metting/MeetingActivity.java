package com.xej.xhjy.ui.metting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.service.AppService;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.receiver.MeetingEvent;
import com.xej.xhjy.receiver.NetWorkStatusEvent;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginOutEvent;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.view.TextviewTobTabView;
import com.xej.xhjy.ui.view.TitleView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.prototypez.appjoint.AppJoint;

public class MeetingActivity extends BaseActivity {

    private LinearLayout ll_av_meeting, ll_add_meeting;
    private static final int REQUEST_VIDEO_CONTACT = 20213;
    private ImageView img_back;
    private TextviewTobTabView tab_All_meeting;
    private TextviewTobTabView tab_online_meeting;
    private TextviewTobTabView tab_offline_meeting;
    private OnlineMeetingFragment onlineMeetingFragment;
    private OfflineMeetingFragment offlineMeetingFragment;
    private AllMeetingFragment allMeetingFragment;
    private int tab = 0;

    /**
     * 初始化view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_metting);
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
        ll_add_meeting = findViewById(R.id.ll_add_meeting);
        ll_add_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MeetingActivity.this, OnlineMeetingOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initTab(){

        tab_All_meeting = findViewById(R.id.tb_all_meeting);
        tab_All_meeting.setText("全部会议");
        tab_All_meeting.setCheck(true);
        tab_All_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab = 0;
                select(tab);
            }
        });
        tab_offline_meeting = findViewById(R.id.tb_offline_meeting);
        tab_online_meeting = findViewById(R.id.tb_online_meeting);
        tab_offline_meeting.setText("线下会议");
        tab_offline_meeting.setCheck(false);
        tab_offline_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab = 1;
                select(tab);
            }
        });
        tab_online_meeting.setText("线上会议");
        tab_online_meeting.setCheck(false);
        tab_online_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab = 2;
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
                tab_All_meeting.setCheck(true);
                tab_online_meeting.setCheck(false);
                tab_offline_meeting.setCheck(false);
                if (allMeetingFragment == null) {
                    allMeetingFragment = new AllMeetingFragment(this);
                    ft.add(R.id.container_fg, allMeetingFragment);
                } else {
                    ft.show(allMeetingFragment);
                }
                break;
            case 1:
                tab_All_meeting.setCheck(false);
                tab_online_meeting.setCheck(false);
                tab_offline_meeting.setCheck(true);
                if (offlineMeetingFragment == null) {
                    offlineMeetingFragment = new OfflineMeetingFragment(this, AppConstants.XH_MEETING);
                    ft.add(R.id.container_fg, offlineMeetingFragment);
                } else {
                    ft.show(offlineMeetingFragment);
                }

                break;
            case 2:
                tab_All_meeting.setCheck(false);
                tab_offline_meeting.setCheck(false);
                tab_online_meeting.setCheck(true);
                if (onlineMeetingFragment == null) {
                    onlineMeetingFragment = new OnlineMeetingFragment(this, AppConstants.XH_MEETING);
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
        if (allMeetingFragment != null) {
            fragmentTransaction.hide(allMeetingFragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppConstants.isClubManager() || AppConstants.isComManager()) {
            //ll_add_meeting.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ll_add_meeting.setVisibility(View.GONE);
    }

    /**
     * 收到登录成功的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEventMainThread(LoginEvent event) {
        if(tab == 0){
            allMeetingFragment.autoRefresh();
        }else if(tab == 1){
            offlineMeetingFragment.autoRefresh();
        }else if(tab == 2){
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
            allMeetingFragment.autoRefresh();
        }else if(tab == 1){
            offlineMeetingFragment.autoRefresh();
        }else if(tab == 2){
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
            allMeetingFragment.autoRefresh();
        }else if(tab == 1){
            offlineMeetingFragment.autoRefresh();
        }else if(tab == 2){
            onlineMeetingFragment.autoRefresh();
        }
    }

    /**
     * 创建会议成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMeetCreate(MeetingEvent event) {
        if(tab == 0){
            allMeetingFragment.autoRefresh();
        }else if(tab == 1){
            offlineMeetingFragment.autoRefresh();
        }else if(tab == 2){
            onlineMeetingFragment.autoRefresh();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
