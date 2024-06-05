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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity;
import com.netease.nim.uikit.business.team.activity.NormalTeamInfoActivity;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.datePicker.MDatePickerDialog;
import com.xej.xhjy.ui.view.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.xej.xhjy.ui.metting.OnlineMeetingDetailActivity.MEETING_ID;

public class OnlineMeetingEditActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout rl_attend_people, rl_master, rl_report_people;
    private LinearLayout ll_meeting_start_time, ll_meeting_end_time, ll_meeting_deadline_time;
    private TextView tv_attend_people, tv_start_time, tv_end_time, tv_master_people, tv_report_people, tv_meet_type,tv_deadline_time;
    private EditText et_meeting_name, et_survey;
    private SimpleDateFormat dateFormat;
    private String meeting_name, url_survey, accId_attend_meeting, id;
    private long start_time, end_time,deadline_time;
    private ArrayList<ContactGroupBean.ContentBean> selected = new ArrayList<>()/*参会人员列表*/, selectableMasterList = new ArrayList<>(), selectableReporterList = new ArrayList<>()/*主讲人，主持人可选人员列表*/;
    private ContactGroupBean.ContentBean master/*主持人*/, reporter/*主讲人*/;
    private ArrayList<String> disableAccounts = new ArrayList<>();
    private static int REQUEST_CODE_SELECT_ATTEND_PEOPLE = 1;//选择参会人员
    private static int REQUEST_CODE_SELECT_MASTER = 2;//选择主持人
    private static int REQUEST_CODE_SELECT_REPORTER = 3;//选择主讲人
    private RadioButton rd_video, rd_audio;
    private boolean rd_video_checked = false, rd_audio_checked = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_meeting_order);
        initView();
        initData();
        getData();
    }

    private void initView(){
        TitleView titleView = findViewById(R.id.titleview);
        titleView.setTitle("修改会议");
        ((TextView)findViewById(R.id.tv_btn)).setText("确定修改");
        tv_meet_type = findViewById(R.id.tv_meeting_type);
        tv_meet_type.setVisibility(View.VISIBLE);
        findViewById(R.id.spinner_meeting_type).setVisibility(View.GONE);
        rl_attend_people = findViewById(R.id.rl_attend_people);
        rl_attend_people.setOnClickListener(this);
        tv_attend_people = findViewById(R.id.tv_attend_people);
        ll_meeting_start_time = findViewById(R.id.ll_meeting_start_time);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_deadline_time = findViewById(R.id.tv_deadline_time);
        ll_meeting_start_time.setOnClickListener(this);
        ll_meeting_end_time = findViewById(R.id.ll_meeting_end_time);
        ll_meeting_end_time.setOnClickListener(this);
        ll_meeting_deadline_time = findViewById(R.id.ll_meeting_deadline_time);
        ll_meeting_deadline_time.setOnClickListener(this);
        rl_master = findViewById(R.id.rl_master);
        rl_master.setOnClickListener(this);
        tv_master_people = findViewById(R.id.tv_master_people);
        //tv_report_people = findViewById(R.id.tv_report_people);
        et_meeting_name = findViewById(R.id.et_meeting_name);
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

        et_survey = findViewById(R.id.et_survey);
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
        TextView tv_btn = findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(this);
        rd_audio = findViewById(R.id.rd_audio);
        rd_audio.setOnClickListener(this);
        rd_video = findViewById(R.id.rd_video);
        rd_video.setOnClickListener(this);
    }

    private void initData(){
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    private void getData(){
        Intent intent = getIntent();
        String meetId = null;
        if(intent != null){
            meetId = intent.getStringExtra(MEETING_ID);
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
                        JSONObject meetingInfo = jsonObject.optJSONObject("content");
                        if(meetingInfo != null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String ownerAccId = "", speakAccId = "";
                                        id = meetingInfo.optString("id");
                                        tv_meet_type.setText("2".equals(meetingInfo.optString("meetType"))?"预约会议":"快速会议");
                                        meeting_name = meetingInfo.optString("meetName");
                                        et_meeting_name.setText(meeting_name);
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        start_time = Long.valueOf(meetingInfo.optString("reserveStartTime"));
                                        String startTime = format.format(start_time);
                                        tv_start_time.setText(startTime);
                                        end_time = Long.valueOf(meetingInfo.optString("reserveEndTime"));
                                        String endTime = format.format(end_time);
                                        tv_end_time.setText(endTime);

                                        rd_audio_checked = "1".equals(meetingInfo.optString("whetherVoice"))?true:false;
                                        rd_audio.setChecked(rd_audio_checked);
                                        rd_video_checked = "1".equals(meetingInfo.optString("whetherVideo"))?true:false;
                                        rd_video.setChecked(rd_video_checked);

                                        JSONArray array = null;
                                        try {
                                            array = meetingInfo.getJSONArray("member");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        String attendPeople = "";
                                        selected.clear();
                                        ArrayList<JSONObject> jsonArray = new ArrayList<>();
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject object = array.getJSONObject(i);
                                            if(i == array.length() -1){
                                                attendPeople += object.optString("name");
                                            }else{
                                                attendPeople += object.optString("name") + ",";
                                            }
                                            ContactGroupBean.ContentBean bean = new ContactGroupBean.ContentBean(object.optString("accId"), object.optString("name"), object.optString("orgName"));
                                            selected.add(bean);
                                            if(!TextUtils.isEmpty(ownerAccId) && ownerAccId.equals(object.optString("accId"))){
                                                master = bean;
                                            }
                                            if(!TextUtils.isEmpty(speakAccId) && speakAccId.equals(object.optString("accId"))){
                                                reporter = bean;
                                            }
                                            JSONObject memberObj = new JSONObject();
                                            memberObj.put("accId", bean.getAccId());
                                            jsonArray.add(memberObj);
                                        }
                                        accId_attend_meeting = jsonArray.toString();
                                        if("2".equals(meetingInfo.optString("meetType"))){
                                            //截至日期
                                            deadline_time = Long.valueOf(meetingInfo.optString("lastApplyDate"));
                                            String deadlineTime = format.format(deadline_time);
                                            tv_deadline_time.setText(deadlineTime);

                                            //预约会议不允许点击
                                            rl_attend_people.setOnClickListener(null);

                                            if(jsonString.contains("ownerName")){
                                                tv_master_people.setText(meetingInfo.optString("ownerName"));
                                                ownerAccId = meetingInfo.getString("ownerId");
                                            }
                                            if(jsonString.contains("speakAccName")){
                                                tv_report_people.setText(meetingInfo.optString("speakAccName"));
                                                speakAccId = meetingInfo.optString("speakAccId");
                                            }
                                            if(jsonString.contains("questionUrl")){
                                                et_survey.setText(meetingInfo.optString("questionUrl"));
                                            }else{
                                                et_survey.setText("暂无");
                                            }
                                        }else{
                                            ll_meeting_deadline_time.setVisibility(View.GONE);
                                            rl_master.setVisibility(View.GONE);
                                            findViewById(R.id.ll_survey).setVisibility(View.GONE);
                                            findViewById(R.id.rl_meeing_doc).setVisibility(View.GONE);
                                            //参会人员
                                            tv_attend_people.setText(attendPeople);
                                            findViewById(R.id.img_arrow).setVisibility(View.VISIBLE);
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

    private void updateMeetingInfo(){
        Map<String, String> maps = new HashMap<>();
        if(TextUtils.isEmpty(meeting_name)){
            ToastUtils.shortToast(this, "请添加会议名称");
            return;
        }
        maps.put("meetName", meeting_name);
        maps.put("id", id);
        maps.put("meetType", "2");//1,快速会议，2，预约会议
        if(master != null){
            maps.put("onwerId", master.getAccId());
            maps.put("ownerName", master.getName());
        }
        if(reporter != null){
            maps.put("speakAccId", reporter.getAccId());
        }
        maps.put("createAccId", PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, ""));
        maps.put("whetherVoice", rd_audio_checked?"1":"0");
        maps.put("whetherVideo", rd_video_checked?"1":"0");
        maps.put("accIdArray", accId_attend_meeting);
        maps.put("reserveStartTime", start_time+"");
        maps.put("reserveEndTime", end_time+"");
        maps.put("lastApplyDate", deadline_time+"");
        if(!TextUtils.isEmpty(url_survey)){
            maps.put("questionUrl", url_survey);
        }
        String TAG = "update_meeting_info";
        mActivity.addTag(TAG);
        String url = NetConstants.UPDATE_MEETING_INFO;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, maps, new HttpCallBack(){
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        ToastUtils.shortToast(OnlineMeetingEditActivity.this, "会议更新成功");
                        finishWithAnim();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(OnlineMeetingEditActivity.this, "会议更新失败");
            }
        });
    }

    private void selectAttendPeople(){
        Intent intent = new Intent(OnlineMeetingEditActivity.this, com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.class);
        intent.putExtra(com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.FLAG, "1");
        intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST,disableAccounts);
        intent.putParcelableArrayListExtra(ContactListSelectAtivity.SELECTED_MEMBER_LIST,selected);
        startActivityForResultWithAnim(intent, REQUEST_CODE_SELECT_ATTEND_PEOPLE);
    }

    private void selectMaster(){
        Intent intent = new Intent(OnlineMeetingEditActivity.this, SelectMeetingPeopleActivity.class);
        intent.putExtra(com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.FLAG, "1");
        intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST,disableAccounts);
        intent.putParcelableArrayListExtra(SelectMeetingPeopleActivity.DATA_SOURCE,selected);
        intent.putExtra(SelectMeetingPeopleActivity.SELECTED_MEMBER_LIST,master);
        startActivityForResultWithAnim(intent, REQUEST_CODE_SELECT_MASTER);
    }

    private void selectReporter(){
        Intent intent = new Intent(OnlineMeetingEditActivity.this, SelectMeetingPeopleActivity.class);
        intent.putExtra(com.netease.nim.uikit.business.team.activity.ContactListSelectAtivity.FLAG, "1");
        intent.putStringArrayListExtra(NormalTeamInfoActivity.DIAABLE_MEMBER_LIST,disableAccounts);
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
                    ArrayList<com.alibaba.fastjson.JSONObject> jsonArray = new ArrayList<>();
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
                        com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
                        object.put("accId", bean.getAccId());
                        jsonArray.add(object);
                        selectableMasterList.add(bean);
                        selectableReporterList.add(bean);
                    }
                    accId_attend_meeting = jsonArray.toString();
                    tv_attend_people.setText(names);
                }
            }else if(requestCode == REQUEST_CODE_SELECT_MASTER){
                final ArrayList<ContactGroupBean.ContentBean> beans = data.getExtras().getParcelableArrayList(ContactListSelectAtivity.RESULT_DATA);
                if(beans != null && !beans.isEmpty()){
                    master = beans.get(0);
                    if(reporter != null && master.getAccId().equals(reporter.getAccId())){
                        reporter = null;
                        tv_report_people.setText("");
                        selectableReporterList.remove(master);
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
                        selectableMasterList.remove(reporter);
                    }
                    tv_report_people.setText(reporter.getName());
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_attend_people:
                selectAttendPeople();
                break;
            case R.id.ll_meeting_start_time:
                showDatePickerDialog(this, new MDatePickerDialog.OnDateResultListener() {
                    @Override
                    public void onDateResult(long date) {
                        if(date >= end_time){
                            ToastUtils.shortToast(OnlineMeetingEditActivity.this, "会议开始时间不能晚于会议结束时间");
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
                            ToastUtils.shortToast(OnlineMeetingEditActivity.this, "会议结束时间不能早于会议开始时间");
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
//            case R.id.rl_report:
//                selectReporter();
//                break;
            case R.id.tv_btn:
                updateMeetingInfo();
                break;
            case R.id.rd_audio:
                rd_audio.setChecked(rd_audio_checked = !rd_audio_checked);
                break;
            case R.id.rd_video:
                rd_video.setChecked(rd_video_checked = !rd_video_checked);
                break;
        }
    }
}
