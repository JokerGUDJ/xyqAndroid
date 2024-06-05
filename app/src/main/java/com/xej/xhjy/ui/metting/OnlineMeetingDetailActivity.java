package com.xej.xhjy.ui.metting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.meetinglib.sdk.NECallback;
import com.netease.meetinglib.sdk.NEJoinMeetingOptions;
import com.netease.meetinglib.sdk.NEJoinMeetingParams;
import com.netease.meetinglib.sdk.NEMeetingError;
import com.netease.meetinglib.sdk.NEMeetingInfo;
import com.netease.meetinglib.sdk.NEMeetingOnInjectedMenuItemClickListener;
import com.netease.meetinglib.sdk.NEMeetingOptions;
import com.netease.meetinglib.sdk.NEMeetingSDK;
import com.netease.meetinglib.sdk.menu.NEMeetingMenuItem;
import com.netease.meetinglib.sdk.menu.NEMenuClickInfo;
import com.netease.meetinglib.sdk.menu.NEMenuItemInfo;
import com.netease.meetinglib.sdk.menu.NEMenuItemListBuilder;
import com.netease.meetinglib.sdk.menu.NEMenuStateController;
import com.netease.meetinglib.sdk.menu.NEMenuVisibility;
import com.netease.meetinglib.sdk.menu.NESingleStateMenuItem;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.MeetingInfo;
import com.xej.xhjy.bean.XHMeeting;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ShareDialog;
import com.xej.xhjy.ui.main.UpdateUtils;
import com.xej.xhjy.ui.view.TitleView;
import com.xej.xhjy.ui.web.LiveingWebview;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlineMeetingDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_meeting_name,tv_attend_people, tv_start_time, tv_end_time, tv_master_people, tv_report_people, tv_survey , tv_deadline_time, tv_meet_type, tv_btn;
    private String meeting_name, meeting_id, whether_join, questionUrl, meet_id, startTime, endTime;//meet_id鑫合会议id，meeting_id云信会议id
    private RadioButton rd_video, rd_audio;
    private boolean rd_video_checked = false, rd_audio_checked = false;
    public final static String MEETING_ID = "meetId";
    public final static String WHETHER_JOIN = "whether_join";
    public final static int MENU_ITEM_DOC = 101;
    public final static int MENU_ITEM_SHARE = 102;
    public final static String MEETING_PREFIX = "meetId_";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_meeting_detail);
        initView();
        getData();
    }

    private void initView(){
        tv_meet_type = findViewById(R.id.tv_meeting_type);
        tv_meeting_name = findViewById(R.id.tv_meeting_name);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_attend_people = findViewById(R.id.tv_attend_people);
        tv_deadline_time = findViewById(R.id.tv_deadline_time);
        tv_master_people = findViewById(R.id.tv_master_people);
        //tv_report_people = findViewById(R.id.tv_report_people);
        tv_survey = findViewById(R.id.tv_survey);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_share).setOnClickListener(this);
        rd_audio = findViewById(R.id.rd_audio);
        rd_video = findViewById(R.id.rd_video);
        findViewById(R.id.rl_meeing_doc).setOnClickListener(this);
        tv_btn = findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(this);
    }

    private void getData(){
        AppConstants.APP_VERSION = UpdateUtils.getVerName(mActivity);
        Intent intent = getIntent();
        String meetId = null;
        if(intent != null){
            meetId = intent.getStringExtra(MEETING_ID);
            whether_join = intent.getStringExtra(WHETHER_JOIN);
            if(TextUtils.isEmpty(meetId))return;
        }
        HashMap params = new HashMap();
        params.put("id", meetId);
        String TAG1 = "query_meeting_info";
        mActivity.addTag(TAG1);
        String url = NetConstants.QUERY_MEETING_INFO;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG1, params, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        final String meetingInfoString = jsonObject.optString("content");
                        if(!TextUtils.isEmpty(meetingInfoString)){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        MeetingInfo meetingInfo = JSON.parseObject(meetingInfoString, MeetingInfo.class);
                                        whether_join = meetingInfo.getWhetherJoin();
                                        //登录用户accid
                                        String accId = PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, "");
                                        if(accId != null && accId.equals(meetingInfo.getOwnerId())){
                                            whether_join = "Y";
                                        }
                                        if ("Y".equals(whether_join)) {
                                            if("1".equals(meetingInfo.getStatus()) || "2".equals(meetingInfo.getStatus())){
                                                tv_btn.setText("加入会议");
                                                tv_btn.setVisibility(View.VISIBLE);
                                            }else{
                                                tv_btn.setVisibility(View.GONE);
                                            }
                                        }else{
                                            long curTime = System.currentTimeMillis();
                                            String lastAppleDate = meetingInfo.getLastApplyDate();
                                            if(!TextUtils.isEmpty(lastAppleDate)){
                                                if(curTime >= Long.valueOf(lastAppleDate)){
                                                    //超过报名时间
                                                    tv_btn.setVisibility(View.GONE);
                                                }else{
                                                    tv_btn.setText("报名");
                                                    tv_btn.setVisibility(View.VISIBLE);
                                                }
                                            }else{
                                                //报名截至时间为空，就用会议结束时间
                                                if(curTime >= Long.valueOf(meetingInfo.getReserveEndTime())){
                                                    //超过报名时间
                                                    tv_btn.setVisibility(View.GONE);
                                                }else{
                                                    tv_btn.setText("报名");
                                                    tv_btn.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                        tv_meet_type.setText("2".equals(meetingInfo.getMeetType())?"预约会议":"快速会议");
                                        meeting_id = meetingInfo.getMeetingId();
                                        meet_id = meetingInfo.getId();
                                        meeting_name = meetingInfo.getMeetName();
                                        tv_meeting_name.setText(meeting_name);
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        startTime = format.format(Long.valueOf(meetingInfo.getReserveStartTime()));
                                        tv_start_time.setText(startTime);
                                        endTime = format.format(Long.valueOf(meetingInfo.getReserveEndTime()));
                                        tv_end_time.setText(endTime);
                                        String deadlineTime = meetingInfo.getLastApplyDate();
                                        if(!TextUtils.isEmpty(deadlineTime)){
                                            String deadline_time = format.format(Long.valueOf(deadlineTime));
                                            tv_deadline_time.setText(deadline_time);
                                        }
                                        if(jsonString.contains("ownerName")){
                                            tv_master_people.setText(meetingInfo.getOwnerName());
                                        }
//                                        if(jsonString.contains("speakAccName")){
//                                            tv_report_people.setText(meetingInfo.getSpeakAccName());
//                                        }
                                        if(jsonString.contains("questionUrl")){
                                            questionUrl = meetingInfo.getQuestionUrl();
                                            tv_survey.setText(questionUrl);
                                            tv_survey.setOnClickListener(OnlineMeetingDetailActivity.this);
                                        }else{
                                            tv_survey.setText("暂无");
                                        }

                                        rd_audio_checked = "1".equals(meetingInfo.getWhetherVoice())?true:false;
                                        rd_audio.setChecked(rd_audio_checked);
                                        rd_audio.setEnabled(false);
                                        rd_video_checked = "1".equals(meetingInfo.getWhetherVideo())?true:false;
                                        rd_video.setChecked(rd_video_checked);
                                        rd_video.setEnabled(false);

                                        JSONArray array = new JSONArray(meetingInfo.getMember());
                                        String attendPeople = "";
                                        for(int i = 0; i < array.length(); i++){
                                            if(i == array.length() -1){
                                                attendPeople += array.getJSONObject(i).optString("name");
                                            }else{
                                                attendPeople += array.getJSONObject(i).optString("name") + ",";
                                            }
                                        }
                                        tv_attend_people.setText(attendPeople);
                                        //快速会议时隐藏
                                        if(!"2".equals(meetingInfo.getMeetType())){
                                            findViewById(R.id.ll_meeting_deadline_time).setVisibility(View.GONE);
                                            findViewById(R.id.rl_master).setVisibility(View.GONE);
                                            findViewById(R.id.ll_survey).setVisibility(View.GONE);
                                            findViewById(R.id.rl_meeing_doc).setVisibility(View.GONE);
                                            findViewById(R.id.divier1).setVisibility(View.GONE);
                                            findViewById(R.id.divier2).setVisibility(View.GONE);
                                            findViewById(R.id.divier3).setVisibility(View.GONE);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    private void joinMeeting(){
        NEJoinMeetingParams params = new NEJoinMeetingParams();     //会议参数
        params.meetingId = meeting_id;                             //会议号
        params.displayName = PerferenceUtils.get(AppConstants.User.NAME, "");;                          //会议昵称
        //params.password = "123456";                                 //会议密码

        NEJoinMeetingOptions options = new NEJoinMeetingOptions();   //会议选项
        options.noVideo = !rd_video_checked;;                                      //入会时关闭视频，默认为true
        options.noAudio = !rd_audio_checked;;                                      //入会时关闭音频，默认为true
        options.noWhiteBoard = true;                                //入会隐藏白板入口，默认为false
        options.noInvite = true;                                    //入会隐藏"邀请"按钮，默认为false
        options.noChat = true;                                      //入会隐藏"聊天"按钮，默认为false
        options.noMinimize = true;                              //入会是否允许最小化会议页面，默认为true
        configToolbarMenuItems(options);                        //自定义菜单
        NEMeetingSDK.getInstance().getMeetingService().joinMeeting(this, params, options, new NECallback<Void>() {
            @Override
            public void onResult(int resultCode, String resultMsg, Void result) {
                if (resultCode == NEMeetingError.ERROR_CODE_SUCCESS) {
                    //加入会议成功
                } else {
                    //加入会议失败
                    ToastUtils.shortToast(OnlineMeetingDetailActivity.this, resultMsg);
                }
            }
        });
    }

    private void configToolbarMenuItems(NEMeetingOptions options){
        List<NEMeetingMenuItem> menuItems = new ArrayList<>();
        NEMenuItemListBuilder toolbarMenuBuilder = NEMenuItemListBuilder.moreMenuBuilder();
        NESingleStateMenuItem neSingleStateMenuItem = new NESingleStateMenuItem(MENU_ITEM_DOC, NEMenuVisibility.VISIBLE_ALWAYS,  new NEMenuItemInfo("资料", R.drawable.icon_doc));
        toolbarMenuBuilder.addMenu(neSingleStateMenuItem);
        NESingleStateMenuItem neSingleStateMenuItemShare = new NESingleStateMenuItem(MENU_ITEM_SHARE, NEMenuVisibility.VISIBLE_ALWAYS,  new NEMenuItemInfo("分享", R.drawable.icon_video_share));
        toolbarMenuBuilder.addMenu(neSingleStateMenuItemShare);
        NEMeetingSDK.getInstance().getMeetingService().setOnInjectedMenuItemClickListener(new NEMeetingOnInjectedMenuItemClickListener(){
            @Override
            public void onInjectedMenuItemClick(Context context, NEMenuClickInfo clickInfo, NEMeetingInfo meetingInfo, NEMenuStateController stateController){
                if(meetingInfo != null && clickInfo.getItemId() == MENU_ITEM_DOC){
                    goDocPage();
                }else if(meetingInfo != null && clickInfo.getItemId() == MENU_ITEM_SHARE){
                    showShareDialog(context);
                }
            }
        });
        options.fullMoreMenuItems = toolbarMenuBuilder.build();
    }

    private void showShareDialog(Context context){
        ShareDialog dialog = new ShareDialog(context,
                NetConstants.BASE_IP+"xhyjcms/mobile/#/livescreamshare?liveId="+MEETING_PREFIX+meet_id,
                "会议名称："+meeting_name+"\n"+
                        "会议时间：" + startTime + "--" + endTime+"\n"+
                        "会议链接：",
                "鑫合会议:"+meeting_name,
                "诚邀您一起参加鑫合会议，精彩内容不容错过！",
                null);
        if(dialog != null){
            dialog.show();
        }
    }

    //查看文档
    private void goDocPage(){
        if(!TextUtils.isEmpty(meet_id)){
            Intent intent = new Intent(mActivity, WebPagerActivity.class);
            intent.putExtra(WebPagerActivity.LOAD_URL, "MeetingFile");
            intent.putExtra(WebPagerActivity.MEETTING_ID, meet_id);
            startActivity(intent);
        }else{
            ToastUtils.shortToast(mActivity, "会议信息有误");
        }
    }

    //查看调查问卷
    private void gotoSurvey(){
        Intent intent = new Intent(mActivity, LiveingWebview.class);
        intent.putExtra(LiveingWebview.LOAD_URL,(questionUrl.startsWith("http://")||questionUrl.startsWith("https://"))?questionUrl:"http://"+questionUrl);
        intent.putExtra("name", "调查问卷");
        intent.putExtra("closeBtn", true);
        mActivity.startActivity(intent);
    }

    //会议报名
    private void signUpMeeting( String meetId, String mobile){
        String TAG1 = "signUpMeeting";
        mActivity.addTag(TAG1);
        String url = NetConstants.SINGUP_MEETING;
        HashMap params = new HashMap<>();
        params.put("meetId", meetId);
        params.put("mobilephone", mobile);
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG1, params, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("一条报名数据---》" + jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if("0".equals(jsonObject.optString("code"))){
                        ToastUtils.shortToast(OnlineMeetingDetailActivity.this, "报名成功");
                        tv_btn.setText("加入会议");
                        whether_join = "Y";
                    }else{
                        ToastUtils.shortToast(OnlineMeetingDetailActivity.this, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(OnlineMeetingDetailActivity.this, errorMsg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.tv_btn:
                if ("Y".equals(whether_join)) {
                    //加入会议
                    joinMeeting();
                }else{
                    //报名
                    signUpMeeting(meet_id, PerferenceUtils.get(AppConstants.User.PHONE, ""));
                }
                break;
            case R.id.img_back:
                finishWithAnim();
                break;
            case R.id.img_share:
                showShareDialog(this);
                break;
            case R.id.rl_meeing_doc:
                goDocPage();
                break;
            case R.id.tv_survey:
                gotoSurvey();
                break;
        }
    }
}
