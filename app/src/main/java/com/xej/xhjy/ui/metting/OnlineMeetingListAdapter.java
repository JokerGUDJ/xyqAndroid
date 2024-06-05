package com.xej.xhjy.ui.metting;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.bean.MeettingListBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.recyclerview.CommonRecycleAdapter;
import com.xej.xhjy.common.view.recyclerview.CommonViewHolder;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xej.xhjy.ui.metting.OnlineMeetingDetailActivity.MEETING_ID;

public class OnlineMeetingListAdapter extends CommonRecycleAdapter<MeettingListBean.ContentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private SimpleDateFormat simpleDateFormat;
    private Context context;
    private String accId;//登录人的accid

    public OnlineMeetingListAdapter(Context context, List<MeettingListBean.ContentBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_online_metting_list);
        this.context = context;
        this.commonClickListener = commonClickListener;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        accId = PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, "");
    }

    @Override
    public void bindData(CommonViewHolder holder, MeettingListBean.ContentBean bean) {
        try{
            if(bean != null){
                holder.setText(R.id.tv_meeting_name, bean.getMeetName())
                        .setText(R.id.tv_meeting_time, "时间: "+simpleDateFormat.format(Long.valueOf(bean.getReserveStartTime())) + "--"
                                + simpleDateFormat.format(Long.valueOf(bean.getReserveEndTime())));
                if("Y".equals(bean.getWhetherJoin())){
                    holder.getView(R.id.img_meeting_status).setVisibility(View.VISIBLE);
                }else{
                    holder.getView(R.id.img_meeting_status).setVisibility(View.GONE);
                }
                if(bean.getCreateAccId().equals(accId)){
                    LinearLayout ll_menu = holder.getView(R.id.ll_menu);
                    LinearLayout ll_menu_icon = holder.getView(R.id.ll_menu_icon);
                    ll_menu.setVisibility(View.VISIBLE);
                    ll_menu_icon.setVisibility(View.VISIBLE);
                    holder.itemView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if(v.getScrollX() > 0){
                                ll_menu_icon.setVisibility(View.GONE);
                            }else{
                                ll_menu_icon.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(v.getScrollX() > 0){
                                holder.itemView.scrollTo(0,0);
                            }else{
                                commonClickListener.onItemClickListener(holder.getPosition());
                            }
                        }
                    });
                    ll_menu_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ll_menu_icon.setVisibility(View.GONE);
                            holder.itemView.scrollTo(ll_menu.getWidth(),0);
                        }
                    });
                    TextView tv_edit = holder.getView(R.id.tv_edit);
                    tv_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.putExtra(MEETING_ID, bean.getId());
                            intent.putExtra(OnlineMeetingDetailActivity.WHETHER_JOIN, bean.getWhetherJoin());
                            intent.setClass(context, OnlineMeetingEditActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    TextView tv_del = holder.getView(R.id.tv_del);
                    tv_del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ClubDialog dialog = new ClubDialog(context);
                            dialog.setMessage("确认删除该会议么？");
                            dialog.setTitle("温馨提示");
                            dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                            dialog.setPositiveListener("确定", new PositiveListener() {
                                @Override
                                public void onPositiveClick() {
                                    if(holder.itemView.getScrollX() > 0){
                                        holder.itemView.scrollTo(0,0);
                                    }
                                    deleteOnlineMeeting(bean);
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
                }else{
                    LinearLayout ll_menu = holder.getView(R.id.ll_menu);
                    LinearLayout ll_menu_icon = holder.getView(R.id.ll_menu_icon);
                    ll_menu.setVisibility(View.GONE);
                    ll_menu_icon.setVisibility(View.GONE);
                    //防止不是自己创建的会议可以滑动、
                    holder.itemView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if(v.getScrollX() > 0){
                                holder.itemView.scrollTo(0,0);
                            }
                        }
                    });
                    holder.setCommonClickListener(commonClickListener);;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void deleteOnlineMeeting(MeettingListBean.ContentBean bean){
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
                        if(dataList != null){
                            dataList.remove(bean);
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
}
