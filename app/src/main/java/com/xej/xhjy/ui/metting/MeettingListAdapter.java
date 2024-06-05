package com.xej.xhjy.ui.metting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xej.xhjy.R;
import com.xej.xhjy.bean.MeettingListBean;
import com.xej.xhjy.bean.XHMeeting;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @class MettingListAdapter
 * @author dazhi
 * @Createtime 2018/6/20 11:08
 * @description
 * @Revisetime
 * @Modifier
 */
public class MeettingListAdapter extends RecyclerView.Adapter<MeettingListAdapter.MyViewHolder> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context context;
    private List<MeettingListBean.ContentBean> dataList;
    private int type = 0; //0, 鑫合会议，1，我的会议

    public MeettingListAdapter(Context context, List<MeettingListBean.ContentBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener, int type) {
        this.context = context;
        this.dataList = dataList;
        this.commonClickListener = commonClickListener;
        this.type = type;
    }

    @NonNull
    @Override
    public MeettingListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == 1){
            view = inflater.inflate(R.layout.item_meeting,parent,false);
        }else{
            view = inflater.inflate(R.layout.item_meeting_small,parent,false);
        }
        MeettingListAdapter.MyViewHolder holder = new MeettingListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MeettingListBean.ContentBean bean = dataList.get(position);
        holder.tv_meeting_name.setText(bean.getName());
        holder.tv_meeting_time.setText("开幕时间："+bean.getBeginDate());
        holder.tv_meet_addr.setText("会场地点："+bean.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonClickListener.onItemClickListener(position);
            }
        });
        String state = bean.getStt();
        if ("00".equals(state) || "U".equals(state)){
            holder.img_meet_label.setImageResource(R.drawable.label_applying);
        }else if ("10".equals(state) || "W".equals(state)){
            holder.img_meet_label.setImageResource(R.drawable.label_meeting);
        } else if ("20".equals(state) || "S".equals(state)){
            holder.img_meet_label.setImageResource(R.drawable.label_meet_finish);
        } else if ("30".equals(state)){
            holder.img_meet_label.setImageResource(R.drawable.label_apply_finish);
        } else {
            holder.img_meet_label.setImageResource(R.drawable.label_meet_finish);
        }
        if("Y".equals(bean.getWhetherJoin())){
            if("1".equals(bean.getSistt())){
                holder.img_meet_apply.setBackgroundResource(R.drawable.icon_signed);
            }else{
                holder.img_meet_apply.setBackgroundResource(R.drawable.icon_applyed);
            }
            holder.img_meet_apply.setVisibility(View.VISIBLE);;
        }else{
            holder.img_meet_apply.setVisibility(View.GONE);
        }
        if(("U".equals(state)|| "00".equals(state)) && type == 0){
            //已报名的，不是机构管理员，隐藏报名按钮
            if("Y".equals(bean.getWhetherJoin()) && !LoginUtils.isOrgUser()){
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
                                    checkQuery(bean);
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
                                        intent.putExtra(WebPagerActivity.LOAD_URL, "MSignedPersonList?meetId="+bean.getId());
                                        context.startActivity(intent);
                                    }
                                }, (Activity)context);
                            }
                        });
                    }else{
                        if(holder.btn_view != null){
                            holder.btn_view.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }else{
            if(holder.btn_signup != null){
                holder.btn_signup.setVisibility(View.GONE);
            }
            if(type == 1){
                if(holder.btn_signup != null){
                    holder.btn_signup.setVisibility(View.GONE);
                }
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        MeettingListBean.ContentBean meeting = dataList.get(position);
        if(("U".equals(meeting.getStt()) || "00".equals(meeting.getStt())) &&
                (LoginUtils.isOrgUser() || (!LoginUtils.isOrgUser() && !"Y".equals(meeting.getWhetherJoin()))) && type == 0)
            return 1;
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataList == null?0:dataList.size();
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
     * 校验接口
     */
    private void checkQuery(MeettingListBean.ContentBean meeting) {
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

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_meet_label,img_meet_apply,btn_signup, btn_view;
        private TextView tv_meeting_name, tv_meeting_time, tv_meet_addr, tv_edit, tv_del;
        private LinearLayout ll_menu, ll_menu_icon;


        /**
         * @param itemView
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            // TODO 自动生成的构造函数存根
            img_meet_label= itemView.findViewById(R.id.img_meeting_status);
            tv_meeting_name = itemView.findViewById(R.id.tv_meeting_name);
            tv_meeting_time = itemView.findViewById(R.id.tv_meeting_time);
            tv_meet_addr = itemView.findViewById(R.id.tv_meet_addr);
            ll_menu = itemView.findViewById(R.id.ll_menu);
            ll_menu_icon = itemView.findViewById(R.id.ll_menu_icon);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            tv_del = itemView.findViewById(R.id.tv_del);
            img_meet_apply = itemView.findViewById(R.id.img_apply_status);
            btn_signup = itemView.findViewById(R.id.btn_signup);
            btn_view = itemView.findViewById(R.id.btn_view);
        }

    }
}
