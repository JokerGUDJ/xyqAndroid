package com.xej.xhjy.ui.metting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
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
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity;
import com.netease.nim.uikit.business.team.activity.NormalTeamInfoActivity;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.spinner.NiceSpinner;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.receiver.MeetingEvent;
import com.xej.xhjy.ui.datePicker.MDatePickerDialog;
import com.xej.xhjy.ui.dialog.ShareDialog;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineMeetingOrderActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_attend_people, rl_master, rl_report_people, rl_meeing_doc;
    private LinearLayout ll_meeting_start_time, ll_meeting_end_time, ll_meeting_deadline_time, ll_survey;
    private TextView tv_attend_people, tv_start_time, tv_end_time, tv_master_people, tv_report_people, tv_deadline_time, tv_btn;
    private ImageView img_arrow;
    private SimpleDateFormat dateFormat;
    private String meeting_name, url_survey, accId_attend_meeting;
    private long start_time, end_time, deadline_time;
    private ArrayList<ContactGroupBean.ContentBean> selected = new ArrayList<>(), memberList = new ArrayList<>()/*参会人员列表*/;
    private ContactGroupBean.ContentBean master/*主持人*/, reporter/*主讲人*/;
    private ArrayList<String> disableAccounts = new ArrayList<>();
    private static int REQUEST_CODE_SELECT_ATTEND_PEOPLE = 1;//选择参会人员
    private static int REQUEST_CODE_SELECT_MASTER = 2;//选择主持人
    private static int REQUEST_CODE_SELECT_REPORTER = 3;//选择主讲人
    private NiceSpinner spinner_meeting_type;
    private int meeting_type = 1;//1 预约会议，0，快速会议
    private RadioButton rd_video, rd_audio;
    private boolean rd_video_checked = false, rd_audio_checked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_meeting_order);
        initView();
        initData();
    }

    private void initView(){
        rl_attend_people = findViewById(R.id.rl_attend_people);
        rl_attend_people.setOnClickListener(this);
        tv_attend_people = findViewById(R.id.tv_attend_people);
        img_arrow = findViewById(R.id.img_arrow);
        ll_meeting_start_time = findViewById(R.id.ll_meeting_start_time);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        ll_meeting_start_time.setOnClickListener(this);
        ll_meeting_end_time = findViewById(R.id.ll_meeting_end_time);
        ll_meeting_end_time.setOnClickListener(this);
        ll_meeting_deadline_time = findViewById(R.id.ll_meeting_deadline_time);
        ll_meeting_deadline_time.setOnClickListener(this);
        tv_deadline_time = findViewById(R.id.tv_deadline_time);
        rl_master = findViewById(R.id.rl_master);
        rl_master.setOnClickListener(this);
        tv_master_people = findViewById(R.id.tv_master_people);
        spinner_meeting_type = findViewById(R.id.spinner_meeting_type);
        rd_audio = findViewById(R.id.rd_audio);
        rd_audio.setOnClickListener(this);
        rd_video = findViewById(R.id.rd_video);
        rd_video.setOnClickListener(this);
        //rl_report_people = findViewById(R.id.rl_report);
        //rl_report_people.setOnClickListener(this);
        //tv_report_people = findViewById(R.id.tv_report_people);
        EditText et_meeting_name = findViewById(R.id.et_meeting_name);
        et_meeting_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                meeting_name = editable.toString();
            }
        });

        EditText et_survey = findViewById(R.id.et_survey);
        et_survey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                url_survey = editable.toString();
            }
        });
        tv_btn = findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(this);
        ll_survey = findViewById(R.id.ll_survey);
        rl_meeing_doc = findViewById(R.id.rl_meeing_doc);
    }

    private void initData(){
        initMeetingType();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        String[] times = dateFormat.format(date).split(":");
        if(times.length == 2){
            int delta = Integer.valueOf(times[1]) - 30;
            if(delta > 0){
                //当前时间超过半点
                start_time = curTime - delta*60*1000 + 30*60*1000;
            }else{
                start_time = curTime - Integer.valueOf(times[1])*60*1000 + 30*60*1000;
            }
            end_time = start_time + 30*60*1000;
            deadline_time = start_time;
            tv_start_time.setText(dateFormat.format(new Date(start_time)));
            tv_end_time.setText(dateFormat.format(new Date(end_time)));
            tv_deadline_time.setText(dateFormat.format(new Date(start_time)));
        }

       //把自己加入不可选择列表中
        disableAccounts.add(PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, ""));
        tv_master_people.setText(PerferenceUtils.get(AppConstants.User.NAME, ""));
        //tv_attend_people.setText(PerferenceUtils.get(AppConstants.User.NAME, ""));
    }

    private void initMeetingType(){
        List<String> dataList = new ArrayList<>();;
        dataList.add("快速会议");
        dataList.add("预约会议");
        spinner_meeting_type.attachDataSource(dataList);
        spinner_meeting_type.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                meeting_type = position;
                selected.clear();
                master = null;
                tv_master_people.setText("");
                accId_attend_meeting = null;
                if(meeting_type == 1){
                    ll_meeting_deadline_time.setVisibility(View.VISIBLE);
                    rl_master.setVisibility(View.VISIBLE);
                    rl_master.setVisibility(View.VISIBLE);
                    ll_survey.setVisibility(View.VISIBLE);
                    rl_meeing_doc.setVisibility(View.VISIBLE);
                    rl_master.setVisibility(View.VISIBLE);
                    tv_attend_people.setText("请到PC端维护");
                    tv_attend_people.setVisibility(View.VISIBLE);
                    img_arrow.setVisibility(View.GONE);
                    rl_attend_people.setOnClickListener(null);
                    tv_master_people.setText(PerferenceUtils.get(AppConstants.User.NAME, ""));
                    findViewById(R.id.divier1).setVisibility(View.VISIBLE);
                    findViewById(R.id.divier2).setVisibility(View.VISIBLE);
                    findViewById(R.id.divier3).setVisibility(View.VISIBLE);
                }else if(meeting_type == 0){
                    ll_meeting_deadline_time.setVisibility(View.GONE);
                    rl_master.setVisibility(View.GONE);
                    rl_master.setVisibility(View.GONE);
                    ll_survey.setVisibility(View.GONE);
                    rl_meeing_doc.setVisibility(View.GONE);
                    rl_master.setVisibility(View.GONE);
                    img_arrow.setVisibility(View.VISIBLE);
                    tv_attend_people.setText(PerferenceUtils.get(AppConstants.User.NAME, ""));
                    rl_attend_people.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectAttendPeople();
                        }
                    });
                    findViewById(R.id.divier1).setVisibility(View.GONE);
                    findViewById(R.id.divier2).setVisibility(View.GONE);
                    findViewById(R.id.divier3).setVisibility(View.GONE);
                }
            }
        });
        spinner_meeting_type.setSelectedIndex(meeting_type);
        if(meeting_type == 1){
            tv_attend_people.setText("请到PC端维护");
            tv_attend_people.setVisibility(View.VISIBLE);
        }else{
            tv_attend_people.setText(PerferenceUtils.get(AppConstants.User.NAME, ""));
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.ll_meeting_start_time:
                showDatePickerDialog(this, new MDatePickerDialog.OnDateResultListener() {
                    @Override
                    public void onDateResult(long date) {
                        if(date >= end_time){
                            ToastUtils.shortToast(OnlineMeetingOrderActivity.this, "会议开始时间不能晚于会议结束时间");
                            return;
                        }
                        start_time = date;
                        tv_start_time.setText(dateFormat.format(start_time));
                    }
                });
                break;
            case R.id.ll_meeting_end_time:
                showDatePickerDialog(this, new MDatePickerDialog.OnDateResultListener() {
                    @Override
                    public void onDateResult(long date) {
                        if(date <= start_time){
                            ToastUtils.shortToast(OnlineMeetingOrderActivity.this, "会议结束时间不能早于会议开始时间");
                            return;
                        }
                        end_time = date;
                        tv_end_time.setText(dateFormat.format(end_time));
                    }
                });
                break;
            case R.id.ll_meeting_deadline_time:
                showDatePickerDialog(this, new MDatePickerDialog.OnDateResultListener() {
                    @Override
                    public void onDateResult(long date) {
                        deadline_time = date;
                        tv_deadline_time.setText(dateFormat.format(deadline_time));
                    }
                });
                break;
            case R.id.rl_master:
                selectMaster();
                break;
            case R.id.tv_btn:
                LoginCheckUtils.isCertification = true;
                LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                    @Override
                    public void onlogin() {
                        createOnlineOrderMeeting();
                    }
                }, mActivity);
                break;
            case R.id.rd_audio:
                rd_audio.setChecked(rd_audio_checked = !rd_audio_checked);
                break;
            case R.id.rd_video:
                rd_video.setChecked(rd_video_checked = !rd_video_checked);
                break;
        }
    }

    private void selectAttendPeople(){
        Intent intent = new Intent(OnlineMeetingOrderActivity.this, com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.class);
        intent.putExtra(com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.FLAG, 1);//选择参会人员
        intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST,disableAccounts);
        intent.putParcelableArrayListExtra(ContactListSelectAtivity.SELECTED_MEMBER_LIST,selected);
        startActivityForResultWithAnim(intent, REQUEST_CODE_SELECT_ATTEND_PEOPLE);
    }

    private void selectMaster(){
        Intent intent = new Intent(OnlineMeetingOrderActivity.this, com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.class);
        intent.putExtra(com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.FLAG, 2);//选择主持人
        intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST,disableAccounts);
        intent.putParcelableArrayListExtra(ContactListSelectAtivity.SELECTED_MEMBER_LIST,selected);
        startActivityForResultWithAnim(intent, REQUEST_CODE_SELECT_MASTER);
    }

    private void selectReporter(){
        Intent intent = new Intent(OnlineMeetingOrderActivity.this, SelectMeetingPeopleActivity.class);
        intent.putExtra(com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.FLAG, 3);
        intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST,new ArrayList<>());
        intent.putParcelableArrayListExtra(SelectMeetingPeopleActivity.DATA_SOURCE,selected);
        intent.putExtra(SelectMeetingPeopleActivity.SELECTED_MEMBER_LIST,reporter);
        startActivityForResultWithAnim(intent, REQUEST_CODE_SELECT_REPORTER);
    }

    private void showDatePickerDialog(Context context, MDatePickerDialog.OnDateResultListener onDateResultListener) {
        MDatePickerDialog dialog = new MDatePickerDialog.Builder(context)
                //附加设置(非必须,有默认值)
                .setCanceledTouchOutside(true)
                .setGravity(Gravity.BOTTOM)
                .setSupportTime(true)
                .setTwelveHour(false)
                //结果回调(必须)
                .setOnDateResultListener(onDateResultListener)
                .build();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_ATTEND_PEOPLE) {
                final ArrayList<ContactGroupBean.ContentBean> beans = data.getExtras().getParcelableArrayList(ContactListSelectAtivity.RESULT_DATA);
                if (beans != null && !beans.isEmpty()) {
                    selected.clear();
                    selected.addAll(beans);
                    String names="";
                    ArrayList<JSONObject> jsonArray = new ArrayList<>();
                    for(int i = 0; i < beans.size(); i++){
                        ContactGroupBean.ContentBean bean = beans.get(i);
                        if(i == beans.size() - 1){
                            names += bean.getName();
                        }else{
                            names += bean.getName()+",";
                        }
                        if(master == null || !master.getAccId().equals(bean.getAccId())){
                            bean.setForPhone(false);
                        }
                        JSONObject object = new JSONObject();
                        object.put("accId", bean.getAccId());
                        jsonArray.add(object);
                    }
                    accId_attend_meeting = jsonArray.toString();
                    if(!TextUtils.isEmpty(names) && meeting_type == 0){
                        tv_attend_people.setText(names);
                        tv_attend_people.setVisibility(View.VISIBLE);
                    }else{
                        tv_attend_people.setVisibility(View.GONE);
                    }
                }
            }else if(requestCode == REQUEST_CODE_SELECT_MASTER){
                final ArrayList<ContactGroupBean.ContentBean> beans = data.getExtras().getParcelableArrayList(ContactListSelectAtivity.RESULT_DATA);
                if(beans != null && !beans.isEmpty()){
                    master = beans.get(0);
                    selected.clear();
                    selected.add(master);
                    if(reporter != null && master.getAccId().equals(reporter.getAccId())){
                        reporter = null;
                        //tv_report_people.setText("");
                    }
                    tv_master_people.setText(master.getName());
                }
            }else if(requestCode == REQUEST_CODE_SELECT_REPORTER){
                final ArrayList<ContactGroupBean.ContentBean> beans = data.getExtras().getParcelableArrayList(ContactListSelectAtivity.RESULT_DATA);
                if(beans != null && !beans.isEmpty()){
                    reporter = beans.get(0);
                    if(master != null && reporter.getAccId().equals(master.getAccId())){
                        master = null;
                        tv_master_people.setText("");
                    }
                   // tv_report_people.setText(reporter.getName());
                }
            }
        }

    }

    private void joinMeeting(String meeting_id, String id){
        NEJoinMeetingParams params = new NEJoinMeetingParams();     //会议参数
        params.meetingId = meeting_id;                             //会议号
        params.displayName = PerferenceUtils.get(AppConstants.User.NAME, "");;                          //会议昵称
        //params.password = "123456";                                 //会议密码

        NEJoinMeetingOptions options = new NEJoinMeetingOptions();   //会议选项
        options.noVideo = !rd_video_checked;                         //入会时关闭视频，默认为true
        options.noAudio = !rd_audio_checked;                         //入会时关闭音频，默认为true
        options.noWhiteBoard = true;                                //入会隐藏白板入口，默认为false
        options.noInvite = true;                                    //入会隐藏"邀请"按钮，默认为false
        options.noChat = true;                                      //入会隐藏"聊天"按钮，默认为false
        options.noMinimize = true;                              //入会是否允许最小化会议页面，默认为true
        configToolbarMenuItems(options, id);
        NEMeetingSDK.getInstance().getMeetingService().joinMeeting(this, params, options, new NECallback<Void>() {
            @Override
            public void onResult(int resultCode, String resultMsg, Void result) {
                if (resultCode == NEMeetingError.ERROR_CODE_SUCCESS) {
                    //加入会议成功
                    //通知首页刷新
                    EventBus.getDefault().post(true);
                } else {
                    //加入会议失败
                    ToastUtils.shortToast(OnlineMeetingOrderActivity.this, "加入会议失败");
                }
            }
        });
    }

    private void configToolbarMenuItems(NEMeetingOptions options, String id){
        List<NEMeetingMenuItem> menuItems = new ArrayList<>();
        NEMenuItemListBuilder toolbarMenuBuilder = NEMenuItemListBuilder.moreMenuBuilder();
        NESingleStateMenuItem neSingleStateMenuItem = new NESingleStateMenuItem(OnlineMeetingDetailActivity.MENU_ITEM_DOC, NEMenuVisibility.VISIBLE_ALWAYS,  new NEMenuItemInfo("资料", R.drawable.icon_doc));
        toolbarMenuBuilder.addMenu(neSingleStateMenuItem);
        NESingleStateMenuItem neSingleStateMenuItemShare = new NESingleStateMenuItem(OnlineMeetingDetailActivity.MENU_ITEM_SHARE, NEMenuVisibility.VISIBLE_ALWAYS,  new NEMenuItemInfo("分享", R.drawable.icon_video_share));
        toolbarMenuBuilder.addMenu(neSingleStateMenuItemShare);
        NEMeetingSDK.getInstance().getMeetingService().setOnInjectedMenuItemClickListener(new NEMeetingOnInjectedMenuItemClickListener(){
            @Override
            public void onInjectedMenuItemClick(Context context, NEMenuClickInfo clickInfo, NEMeetingInfo meetingInfo, NEMenuStateController stateController){
                if(meetingInfo != null && clickInfo.getItemId() == OnlineMeetingDetailActivity.MENU_ITEM_DOC){
                    goDocPage(id);
                }else if(meetingInfo != null && clickInfo.getItemId() == OnlineMeetingDetailActivity.MENU_ITEM_SHARE){
                    showShareDialog(context, id);
                }
            }
        });
        options.fullMoreMenuItems = toolbarMenuBuilder.build();
    }

    private void showShareDialog(Context context, String id){
        ShareDialog dialog = new ShareDialog(context,
                NetConstants.BASE_IP+"xhyjcms/mobile/#/livescreamshare?liveId="+OnlineMeetingDetailActivity.MEETING_PREFIX+id,
                "会议名称："+meeting_name+"\n"+
                        "会议时间：" + dateFormat.format(new Date(start_time)) + "--" + dateFormat.format(new Date(end_time))+"\n"+
                        "会议链接：",
                "鑫合会议:"+meeting_name,
                "诚邀您一起参加鑫合会议，精彩内容不容错过！",
                null);
        if(dialog != null){
            dialog.show();
        }
    }

    //查看文档
    private void goDocPage(String meeting_id){
        if(!TextUtils.isEmpty(meeting_id)){
            Intent intent = new Intent(mActivity, WebPagerActivity.class);
            intent.putExtra(WebPagerActivity.LOAD_URL, "MeetingFile");
            intent.putExtra(WebPagerActivity.MEETTING_ID, meeting_id);
            startActivity(intent);
        }else{
            ToastUtils.shortToast(mActivity, "会议信息有误");
        }
    }

    private void createOnlineOrderMeeting(){
        Map<String, String> maps = new HashMap<>();
        if(TextUtils.isEmpty(meeting_name)){
            ToastUtils.shortToast(this, "请添加会议名称");
            return;
        }
        maps.put("meetName", meeting_name);
        maps.put("meetType", (meeting_type+1)+"");//转化为后端1,快速会议，2，预约会议
        if(master != null){
            maps.put("ownerId", master.getAccId());
        }
        if(reporter != null){
            maps.put("speakAccId", reporter.getAccId());
        }
        maps.put("createAccId", PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, ""));

        if(!TextUtils.isEmpty(accId_attend_meeting)){
            maps.put("accIdArray", accId_attend_meeting);
        }
        maps.put("reserveStartTime", start_time+"");
        maps.put("reserveEndTime", end_time+"");
        maps.put("lastApplyDate", deadline_time+"");
        maps.put("whetherVoice", rd_audio_checked?"1":"0");
        maps.put("whetherVideo", rd_video_checked?"1":"0");
        if(!TextUtils.isEmpty(url_survey)){
            maps.put("questionUrl", url_survey);
        }
        String TAG = "ONLINE_MEETING_ORDER";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.ONLINE_MEETING_ORDER, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        ToastUtils.shortToast(OnlineMeetingOrderActivity.this, "创建会议成功");
                        if(meeting_type == 1){
                            //预约会议创建成功后返回列表页
                            EventBus.getDefault().post(new MeetingEvent("success"));
                            finishWithAnim();
                        }else if(meeting_type == 0){
                            //快速会议创建成功加入会议
                            finishWithAnim();
                            String content = jsonObject.getString("content");
                            if(!TextUtils.isEmpty(content)){
                                org.json.JSONObject jsonObjectContent = new org.json.JSONObject(content);
                                String meetId = jsonObjectContent.optString("meetingId");
                                String id = jsonObjectContent.optString("id");
                                if(!TextUtils.isEmpty(meetId) && !TextUtils.isEmpty(id)){
                                    joinMeeting(meetId, id);
                                }
                            }
                        }
                    }else{
                        String msg = jsonObject.optString("msg");
                        if(!TextUtils.isEmpty(msg)){
                            ToastUtils.shortToast(OnlineMeetingOrderActivity.this, msg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                tv_btn.setEnabled(true);
                ToastUtils.shortToast(OnlineMeetingOrderActivity.this, "创建会议失败");
            }
        });
    }
}
