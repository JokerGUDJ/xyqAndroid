package com.xej.xhjy.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.google.gson.JsonArray;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.XHMeeting;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.metting.OnlineMeetingDetailActivity;
import com.xej.xhjy.ui.metting.OnlineMeetingEditActivity;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xej.xhjy.ui.metting.OnlineMeetingDetailActivity.MEETING_ID;

public class RecycleViewAdapter extends Adapter<RecycleViewAdapter.MyViewHolder> {


    private List<XHMeeting> meetList;
    private Context context;
    private SimpleDateFormat dateFormat;
    private String accId;

    public RecycleViewAdapter(Context context, List<XHMeeting> meetList) {
        super();
        this.meetList = meetList;
        this.context = context;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        accId = PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, "");
    }

    public void setMeetList(ArrayList<XHMeeting> meetList){
        this.meetList = meetList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO 自动生成的方法存根
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == 1){
            view = inflater.inflate(R.layout.item_meeting,parent,false);
        }else{
            view = inflater.inflate(R.layout.item_meeting_small,parent,false);
        }
        RecycleViewAdapter.MyViewHolder holder = new RecycleViewAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        XHMeeting meeting = meetList.get(position);
        if(("U".equals(meeting.getStt()) || "00".equals(meeting.getStt())) &&
                (LoginUtils.isOrgUser() || (!LoginUtils.isOrgUser() && !"Y".equals(meeting.getWhetherJoin()))))
            return 1;
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        XHMeeting meeting = meetList.get(position);
        try{
            if(meeting != null){
                holder.tv_meeting_name.setText(meeting.getName());
                if(holder.itemView.getScrollX() != 0){
                    holder.itemView.scrollBy(-holder.ll_menu.getWidth(), 0);
                }
                if("M".equals(meeting.getMeetFrom())){
                    holder.tv_meet_addr.setText("会议地点："+meeting.getAddress());
                    String stt = meeting.getStt();
                    if(("U".equals(stt)|| "00".equals(stt))){
                        //已报名的，不是机构管理员，隐藏报名按钮
                        if("Y".equals(meeting.getWhetherJoin()) && !LoginUtils.isOrgUser()){
                            if(holder.btn_signup != null){
                                holder.btn_signup.setVisibility(View.GONE);
                            }
                            if(holder.btn_view != null){
                                holder.btn_view.setVisibility(View.GONE);
                            }
                        }else{
                            if(holder.btn_signup != null){
                                holder.btn_signup.setVisibility(View.VISIBLE);
                                holder.btn_signup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LoginCheckUtils.isCertification = true;
                                        LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                                            @Override
                                            public void onlogin() {
                                                checkQuery(meeting);
                                            }
                                        }, (Activity)context);
                                    }
                                });
                            }
                            if(holder.btn_view != null){
                                if(LoginUtils.isOrgUser()){
                                    holder.btn_view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            LoginCheckUtils.isCertification = true;
                                            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                                                @Override
                                                public void onlogin() {
                                                    Intent intent = new Intent(context, WebPagerActivity.class);
                                                    intent.putExtra(WebPagerActivity.LOAD_URL, "MSignedPersonList?meetId="+meeting.getId());
                                                    context.startActivity(intent);
                                                }
                                            }, (Activity)context);
                                        }
                                    });
                                }else{
                                    holder.btn_view.setVisibility(View.GONE);
                                }
                            }
                        }
                    }else{
                        if(holder.btn_signup != null){
                            holder.btn_signup.setVisibility(View.GONE);
                        }
                    }
                    setMeetStatus(holder.img_meet_label, stt);
                    if("Y".equals(meeting.getWhetherJoin())){
                        if("1".equals(meeting.getSistt())){
                            holder.img_meet_apply.setBackgroundResource(R.drawable.icon_signed);
                        }else{
                            holder.img_meet_apply.setBackgroundResource(R.drawable.icon_applyed);
                        }
                        holder.img_meet_apply.setVisibility(View.VISIBLE);;
                    }else{
                        holder.img_meet_apply.setVisibility(View.GONE);
                    }
                    holder.ll_menu_icon.setVisibility(View.GONE);
                    holder.ll_menu.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String meetId = meeting.getId();
                            if (meetId != null) {
                                LoginCheckUtils.isCertification = true;
                                LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                                    @Override
                                    public void onlogin() {
                                        checkQuery(meetId, meeting.getWhetherJoin());
                                    }
                                }, (BaseActivity)context);
                            } else {
                                ToastUtils.shortToast(context, "会议Id为空");
                            }
                        }
                    });
                    holder.tv_meeting_time.setText("开幕时间："+meeting.getBeginDate());
                }else{
                    if("Y".equals(meeting.getWhetherJoin())){
                        holder.img_meet_label.setImageResource(R.drawable.meeting_lable_finish);
                        holder.img_meet_label.setVisibility(View.VISIBLE);;
                    }else{
                        holder.img_meet_label.setVisibility(View.GONE);
                    }
                    holder.img_meet_apply.setVisibility(View.GONE);
                    holder.tv_meet_addr.setText("会议地点：线上会议");
                    if(meeting.getCreateAccId().equals(accId)){
                        holder.ll_menu.setVisibility(View.VISIBLE);
                        holder.ll_menu_icon.setVisibility(View.VISIBLE);
                        holder.ll_menu_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(holder.itemView.getScrollX() == 0){
                                    holder.itemView.scrollBy(holder.ll_menu.getWidth(), 0);
                                }else{
                                    holder.itemView.scrollBy(-holder.ll_menu.getWidth(), 0);
                                }
                            }
                        });
                        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.putExtra(MEETING_ID, meeting.getId());
                                intent.setClass(context, OnlineMeetingEditActivity.class);
                                context.startActivity(intent);
                            }
                        });
                        holder.tv_del.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ClubDialog dialog = new ClubDialog(context);
                                dialog.setMessage("确认删除该会议么？");
                                dialog.setTitle("温馨提示");
                                dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                                dialog.setPositiveListener("确定", new PositiveListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        deleteOnlineMeeting(meeting);
                                    }
                                });
                                dialog.setNegativeListener("取消", new NegativeListener() {
                                    @Override
                                    public void onNegativeClick() {
                                    }
                                });
                                dialog.show();
                            }
                        });
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(holder.itemView.getScrollX() == 0){
                                Intent intent = new Intent();
                                intent.putExtra(MEETING_ID, meeting.getId());
                                intent.setClass(context, OnlineMeetingDetailActivity.class);
                                EventTrackingUtil.EventTrackSubmit((BaseActivity)context, "pageHome", "channel=android",
                                        "eventLatestMeets", "meetId="+meeting.getId());
                                context.startActivity(intent);
                            }else{
                                holder.itemView.scrollBy(-holder.ll_menu.getWidth(), 0);
                            }
                        }
                    });
                    holder.tv_meeting_time.setText("时间："+dateFormat.format(Long.valueOf(meeting.getBeginDate()))+"--"+dateFormat.format(Long.valueOf(meeting.getEndDate())));
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /***
     * 获取机构联系人,获取到多个时只取第一个
     */
    private void getOrgManager(String meetName){
        BaseActivity mActivity = (BaseActivity)context;
        String TAG = "query_org_manamger";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("orgId", PerferenceUtils.get(AppConstants.User.ORGID, ""));
        map.put("roleId", "org_manager");
        String url = NetConstants.QUERY_ORG_MANAGER;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONArray content = json.optJSONArray("content");
                        if(content != null && content.length() > 0){
                            showSignUpDialog(meetName, content.getJSONObject(0).optString("userName"));
                        }else{

                            showSignUpDialog(meetName, "");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(context, errorMsg);
            }
        });
    }

    /**
     * 向机构联系人发短信弹框
     */
    private void showSignUpDialog(String meetName, String orgName){
        ClubDialog dialog = new ClubDialog(context);
        dialog.setMessage("您非贵行机构联系人，请联系贵行机构联系人"+orgName+"报名,如需变更机构联系人请联系会务组");
        dialog.setTitle("温馨提示");
        dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
        dialog.setPositiveListener("确定", new PositiveListener() {
            @Override
            public void onPositiveClick() {
                sendSMSToOrgManager(meetName);
            }
        });
        dialog.setNegativeListener("取消", new NegativeListener() {
            @Override
            public void onNegativeClick() {
            }
        });
        dialog.show();
    }

    /**
     * 向机构联系人发送短信
     * @param meetName
     */
    private void sendSMSToOrgManager(String meetName){
        BaseActivity mActivity = (BaseActivity)context;
        String TAG = "send_sms_to_org_manager";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("orgId", PerferenceUtils.get(AppConstants.User.ORGID, ""));
        map.put("mobile", PerferenceUtils.get(AppConstants.User.PHONE, ""));
        map.put("meetName", meetName);
        map.put("department", PerferenceUtils.get(AppConstants.User.DEPARTMENT, ""));
        map.put("userName", PerferenceUtils.get(AppConstants.User.NAME, ""));
        String url = NetConstants.SEND_SMS_TO_ORG_MANAGER;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        ToastUtils.shortToast(context, "短信发送成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(context, errorMsg);
            }
        });
    }

    /**
     * 校验接口
     */
    private void checkQuery(XHMeeting meeting) {
        BaseActivity mActivity = (BaseActivity)context;
        String TAG = "meeting_check_query";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("meetId", meeting.getId());
        String url = NetConstants.NEW_MEETTING_SIGNUP_CHECKED;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        if(LoginUtils.isOrgUser()){
                            Intent intent = new Intent(context, WebPagerActivity.class);
                            intent.putExtra(WebPagerActivity.LOAD_URL, "MSignUpFirst?meetId="+meeting.getId());
                            context.startActivity(intent);
                        }else{
                            getOrgManager(meeting.getName());
                        }
                    }else{
                        ToastUtils.shortToast(context, json.optString("msg"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String errorMsg) {

            }
        });

    }

    /**
     * 校验接口
     */
    private void checkQuery(final String str, String whetherJoin) {
        BaseActivity mActivity = (BaseActivity)context;
        String TAG = "meeting_check_query";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("meetId", str);
        String url = NetConstants.NEW_MEETTING_CHECKED;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        toWebViewShow(str, whetherJoin);
                    }
                    EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                            "eventLatestMeets", "meetId="+str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String errorMsg) {

            }
        });

    }

    /**
     *
     * @param str 会议id
     * @param whetherJoin 会议是否报名
     */
    private void toWebViewShow(String str, String whetherJoin) {
        Intent intent = new Intent(context, WebPagerActivity.class);
        if("Y".equals(whetherJoin)){
            intent.putExtra(WebPagerActivity.LOAD_URL, "MyMDetail");
        }else{
            intent.putExtra(WebPagerActivity.LOAD_URL, "MeetDetail");
        }
        intent.putExtra(WebPagerActivity.MEETTING_ID, str);
        context.startActivity(intent);
    }

    private void deleteOnlineMeeting(XHMeeting bean){
        BaseActivity mActivity = (BaseActivity)context;
        String TAG = "delete_online_meeting";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("id", bean.getId());
        String url = NetConstants.DEL_ONLINE_MEETING;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        ToastUtils.shortToast(context, "删除成功");
                        if(meetList != null){
                            meetList.remove(bean);
                            notifyDataSetChanged();
                        }
                    }else{
                        ToastUtils.shortToast(context, "删除失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(context, "删除失败");
            }
        });
    }

    private void setMeetStatus(ImageView imageView, String meetStatus){
        switch (meetStatus){
            //U:未结束，W:进行中，S:已结束
            case "W":
            case "10":
                imageView.setImageResource(R.drawable.label_meeting);
                break;
            case "U":
            case "00":
                imageView.setImageResource(R.drawable.label_applying);
                break;
            case "30":
            case "E":
                imageView.setImageResource(R.drawable.label_apply_finish);
                break;
            default:
                imageView.setImageResource(R.drawable.label_meet_finish);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return meetList == null? 0 : meetList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_meet_label, img_meet_apply, btn_signup, btn_view;
        private TextView tv_meeting_name, tv_meeting_time, tv_meet_addr, tv_edit, tv_del;
        private LinearLayout ll_menu, ll_menu_icon;



        /**
         * @param itemView
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            // TODO 自动生成的构造函数存根
            img_meet_label= itemView.findViewById(R.id.img_meeting_status);
            img_meet_apply= itemView.findViewById(R.id.img_apply_status);
            tv_meeting_name = itemView.findViewById(R.id.tv_meeting_name);
            tv_meeting_time = itemView.findViewById(R.id.tv_meeting_time);
            tv_meet_addr = itemView.findViewById(R.id.tv_meet_addr);
            ll_menu = itemView.findViewById(R.id.ll_menu);
            ll_menu_icon = itemView.findViewById(R.id.ll_menu_icon);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            tv_del = itemView.findViewById(R.id.tv_del);
            btn_signup = itemView.findViewById(R.id.btn_signup);
            btn_view = itemView.findViewById(R.id.btn_view);
        }

    }
}
