package com.xej.xhjy.ui.society.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.LiveingBean;
import com.xej.xhjy.bean.LiveingMoreBean;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.image.CornerTransform;
import com.xej.xhjy.ui.live.XingLiveingActivity;
import com.xej.xhjy.ui.web.LiveingWebview;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XingLiveingAdapter extends RecyclerView.Adapter<XingLiveingAdapter.MyViewHolder> {

    private Context context;
    private int resId;
    private List<LiveingMoreBean.ContentBean> datas = new ArrayList<>();
    private SimpleDateFormat format;
    private RequestOptions requestOptions;
    private SimpleCallback<LiveingMoreBean.ContentBean> callback;//点击回调

    public XingLiveingAdapter(Context context, int resId, SimpleCallback<LiveingMoreBean.ContentBean> callback){
        this.context = context;
        this.resId = resId;
        this.callback = callback;
        CornerTransform transformation = new CornerTransform(context, 15);
        transformation.setExceptCorner(false,false, true, true);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.liveing_default_img)
                .error(R.drawable.liveing_default_img)
                .centerCrop()
                .transform(transformation);
    }

    public void setDatas(List<LiveingMoreBean.ContentBean> datas){
        this.datas = datas;
    }

    @NonNull
    @Override
    public XingLiveingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resId,parent,false);
        XingLiveingAdapter.MyViewHolder holder = new XingLiveingAdapter.MyViewHolder(view);
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull XingLiveingAdapter.MyViewHolder holder, int position) {
        LiveingMoreBean.ContentBean contentBean = datas.get(position);
        if(contentBean != null){
            String imgUrl = contentBean.getCoverImage();
            if(imgUrl != null){
                String url = contentBean.getCoverImage().contains("http")?contentBean.getCoverImage():AppConstants.BYTE_IMG_PREFIX + contentBean.getCoverImage() + AppConstants.BYTE_IMG_SUFFIX;
                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .into(holder.img_liveing);
            }else{
                holder.img_liveing.setImageResource(R.drawable.liveing_default_img);
            }
            holder.tv_liveing_time.setText(format.format(new Date(Long.valueOf(contentBean.getLiveTime()))));
            holder.tv_name.setText(contentBean.getName());
            String count = getCount(contentBean);
            holder.tv_num.setText((TextUtils.isEmpty(count)?0:count)+("1".equals(contentBean.getLiveStatus())?"人已预约":"人参与"));
            holder.img_label.setImageResource(getLiveingLabel(contentBean.getLiveStatus()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(callback != null){
                        callback.onResult(true, contentBean, 0);
                    }
                }
            });
        }
    }

    private String getCount( LiveingMoreBean.ContentBean  contentBean ){
        String count = "0";
        switch (contentBean.getLiveStatus()){
            case "0":
                count = contentBean.getUnrealCount();
                break;
            case "1":
                count = contentBean.getSubscribeCount();
                break;
            case "2":
            case "3":
                count = contentBean.getJoinCount();
                break;
        }
        return count;
    }

    private int getLiveingLabel(String liveStatus){
        int resId = 0;
        switch (liveStatus){
            case "0"://直播中
                resId = R.drawable.label_liveing;
                break;
            case "1"://预告
                resId = R.drawable.label_order_liveing;
                break;
            case "2"://可回看
                resId = R.drawable.label_review_liveing;
                break;
            case "3"://已结束
                resId = R.drawable.label_lived;
                break;
        }
        return resId;
    }

    @Override
    public int getItemCount() {
        return (datas == null || datas.size() == 0)?0:datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_num;
        private ImageView img_liveing;
        private ImageView img_label;
        private TextView tv_liveing_time;

        /**
         * @param itemView
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            // TODO 自动生成的构造函数存根
            tv_name = itemView.findViewById(R.id.tv_liveing_name);
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_liveing_time = itemView.findViewById(R.id.tv_liveing_time);
            img_liveing = itemView.findViewById(R.id.img_liveing);
            img_label = itemView.findViewById(R.id.img_label);
        }

    }
}
