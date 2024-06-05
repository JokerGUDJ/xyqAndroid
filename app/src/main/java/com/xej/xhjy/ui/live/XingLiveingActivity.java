package com.xej.xhjy.ui.live;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.LiveBanner;
import com.xej.xhjy.bean.LiveingAuthBean;
import com.xej.xhjy.bean.LiveingBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.image.CornerTransform;
import com.xej.xhjy.ui.main.ClubMainActivty;
import com.xej.xhjy.ui.view.CustomBanner;
import com.xej.xhjy.ui.view.CustomBannerIndicator;
import com.xej.xhjy.ui.view.MyCallBack;
import com.xej.xhjy.ui.view.ViewCreator;
import com.xej.xhjy.ui.web.LiveingWebview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class XingLiveingActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_content,ll_no_data, ll_back, ll_banner;
    private ImageView img_back;
    private ScrollView sl_content;
    private SimpleDateFormat format;
    private RequestOptions requestOptions, bannerOptions;
    private int offset = -1;
    private boolean hasLiveingData = false;
    private String source;
    private CustomBanner<LiveBanner.ContentBean> banner;
    private CustomBannerIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xing_liveing_activity);
        initView();
        initData();
    }

    private void initView(){
        sl_content = findViewById(R.id.sl_content);
        ll_content = findViewById(R.id.ll_content);
        ll_no_data = findViewById(R.id.ll_no_data);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        banner = findViewById(R.id.banner);
        indicator = findViewById(R.id.indicator);
        ll_banner = findViewById(R.id.ll_banner);
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        CornerTransform transformation = new CornerTransform(XingLiveingActivity.this, 15);
        transformation.setExceptCorner(false,false, true, true);
        CornerTransform transformation1 = new CornerTransform(XingLiveingActivity.this, 15);
        transformation1.setExceptCorner(false,false, false, false);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.liveing_default_img)
                .error(R.drawable.liveing_default_img)
                .centerCrop()
                .transform(transformation);
        bannerOptions = new RequestOptions()
                .placeholder(R.drawable.liveing_default_img)
                .error(R.drawable.liveing_default_img)
                .centerCrop()
                .transform(transformation1);
    }

    private void initData(){
        Intent intent = getIntent();
        if(intent != null){
            source = intent.getStringExtra("source");
        }
        getBanner();
        getLiveingInfo();
    }

    private void getBanner(){
        String TAG = "getBanner";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.GET_BANNER_LIVEING, TAG, new HashMap<>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    if(!TextUtils.isEmpty(jsonString)){
                        LiveBanner bean = JsonUtils.stringToObject(jsonString, LiveBanner.class);
                        if(bean != null){
                            List<LiveBanner.ContentBean> data = bean.getContent();
                            if(data != null && data.size() > 0){
                                List<String> imgs = new ArrayList<>();
                                ll_banner.setVisibility(View.VISIBLE);
                                indicator.setNum(data.size());
                                banner.setPages(new ViewCreator<LiveBanner.ContentBean>() {
                                    @Override
                                    public View createView(Context context, int position) {
                                        return LayoutInflater.from(context).inflate(R.layout.item_banner, null);
                                    }

                                    @Override
                                    public void updateUI(Context context, View view, int position, LiveBanner.ContentBean contentBean) {
                                        try{
                                            ImageView img_cover = view.findViewById(R.id.img_cover);
                                            TextView tv_name = view.findViewById(R.id.tv_name);
                                            String coverImage = contentBean.getCoverImage();
                                            if(!TextUtils.isEmpty(coverImage)){
                                                String url = coverImage.startsWith("http")?contentBean.getCoverImage():(AppConstants.BYTE_IMG_PREFIX + contentBean.getCoverImage() + AppConstants.BYTE_IMG_SUFFIX);
                                                Glide.with(context).load(url).apply(bannerOptions).into(img_cover);
                                            }else{
                                                img_cover.setImageResource(R.drawable.liveing_default_img);
                                            }
                                            tv_name.setText(contentBean.getName());
                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                    }
                                }, data, new MyCallBack<Integer>() {
                                    @Override
                                    public void onCallBack(Integer position) {
                                        indicator.move(0, position, false);
                                    }
                                });
                                banner.startTurning(5000);
                                banner.setOnPageClickListener(new CustomBanner.OnPageClickListener<LiveBanner.ContentBean>() {
                                    @Override
                                    public void onPageClick(int position, LiveBanner.ContentBean contentBean) {
                                        queryAuthority(contentBean.getId());
                                    }
                                });
                            }
                        }
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

    private void getLiveingInfo(){
        String TAG = "get_liveing_info";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.QUERY_XING_LIVEING, TAG, new HashMap<>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    if(!TextUtils.isEmpty(jsonString)){
                        LiveingBean bean = JsonUtils.stringToObject(jsonString, LiveingBean.class);
                        if(bean != null){
                            if("0".equals(bean.getCode())){
                                List<List<LiveingBean.ContentBean>> contentBeans = bean.getContent();
                                for(int i = 0; contentBeans != null && i < contentBeans.size(); i++){
                                    List<LiveingBean.ContentBean> list = contentBeans.get(i);
                                    if(list != null && list.size() > 0){
                                        addContent(i, contentBeans.get(i));
                                        hasLiveingData = true;
                                    }
                                }
                                if(!hasLiveingData){
                                    sl_content.setVisibility(View.GONE);
                                    ll_no_data.setVisibility(View.VISIBLE);
                                }
                            }else{
                                sl_content.setVisibility(View.GONE);
                                ll_no_data.setVisibility(View.VISIBLE);
                                ToastUtils.shortToast(XingLiveingActivity.this, bean.getMsg());
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                sl_content.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
                ToastUtils.shortToast(XingLiveingActivity.this, errorMsg);
            }
        });
    }

    //查询权限
    private void queryAuthority(String liveId){
        String TAG = "get_authority";
        mActivity.addTag(TAG);
        HashMap<String ,String> params = new HashMap<>();
        params.put("liveId", liveId);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.QUERY_VIEW_AUTHORITY, TAG, params, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    if(!TextUtils.isEmpty(jsonString)){
                        LiveingAuthBean bean = JsonUtils.stringToObject(jsonString, LiveingAuthBean.class);
                        if(bean != null){
                            if("0".equals(bean.getCode())){
                                LiveingAuthBean.Content content = bean.getContent();
                                if(content != null){
                                    Intent intent = new Intent(XingLiveingActivity.this, LiveingWebview.class);
                                    intent.putExtra("liveId", content.getLiveId());
                                    intent.putExtra("name",content.getLiveName());
                                    intent.putExtra("coverImage", content.getCoverImage());
                                    intent.putExtra("subName", content.getAnnounCement());
                                    if("1".equals(content.getLiveStatus())){
                                        intent.putExtra(LiveingWebview.LOAD_URL, NetConstants.BASE_IP+"xhyjcms/mobile/index.html#/livescreamdetail");
                                    }else{
                                        intent.putExtra(LiveingWebview.LOAD_URL, content.getUrl()+content.getViewUrlPath()+content.getParameters());
                                    }
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(XingLiveingActivity.this, errorMsg);
            }
        });
    }

    private int getLiveingTypeImage(String liveStatus){
        int resId = 0;
        switch (liveStatus){
            case "0"://直播中
                resId = R.drawable.icon_liveing;
                break;
            case "1"://预告
                resId = R.drawable.icon_order_livingpng;
                break;
            case "2"://可回看
                resId = R.drawable.icon_review_liveing;
                break;
            case "3"://已结束
                resId = R.drawable.icon_lived;
                break;
        }
        return resId;
    }

    private String getLiveingType(String liveStatus){
        String txt = "";
        switch (liveStatus){
            case "0"://直播中
                txt = "正在直播";
                break;
            case "1"://预告
                txt = "精彩预告";
                break;
            case "2"://可回看
                txt = "往期回顾";
                break;
            case "3"://已结束
                txt = "已结束";
                break;
        }
        return txt;
    }

    private String getCount( LiveingBean.ContentBean contentBean ){
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

    private void setOneContentBeanData(int index, LiveingBean.ContentBean contentBean){
        View view = ll_content.getChildAt(offset);
        //直播封面
        ImageView img_liveing_cover_image = view.findViewById(R.id.img_liveing_cover_image);
        String imgUrl = contentBean.getCoverImage();
        if(imgUrl != null){
            String url = imgUrl.contains("http")?contentBean.getCoverImage():AppConstants.BYTE_IMG_PREFIX + contentBean.getCoverImage() + AppConstants.BYTE_IMG_SUFFIX;
            Glide.with(XingLiveingActivity.this)
                    .load(url)
                    .apply(requestOptions)
                    .into(img_liveing_cover_image);
        }

        //直播类型图标
        ImageView img_liveing_type = view.findViewById(R.id.img_liveing_type);
        img_liveing_type.setImageResource(getLiveingTypeImage(contentBean.getLiveStatus()));
        //直播类型
        TextView tv_liveing_type = view.findViewById(R.id.tv_liveing_type);
        tv_liveing_type.setText(getLiveingType(contentBean.getLiveStatus()));
        TextView tv_more_liveing = view.findViewById(R.id.tv_more_liveing);
        //直播名称
        TextView tv_liveing_name = view.findViewById(R.id.tv_liveing_name);
        tv_liveing_name.setText(contentBean.getName());
        //直播时间
        TextView tv_liveing_time = view.findViewById(R.id.tv_liveing_time);
        tv_liveing_time.setText(format.format(new Date(contentBean.getLiveTime())));
        //观看人数
        TextView tv_join_count = view.findViewById(R.id.tv_join_count);
        String count = getCount(contentBean);
        tv_join_count.setText((TextUtils.isEmpty(count)?0:count)+("1".equals(contentBean.getLiveStatus())?"人已预约":"人参与"));
        //更多
        tv_more_liveing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(XingLiveingActivity.this, LiveingMoreActivity.class);
                intent.putExtra("liveStatus", contentBean.getLiveStatus());
                startActivity(intent);
            }
        });
        view.findViewById(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryAuthority(contentBean.getId());
            }
        });
    }

    private void setTwoContentBeanData(int index, List<LiveingBean.ContentBean> contentBeans){
        View view = ll_content.getChildAt(offset);
        if(contentBeans.size() == 1){
            setOneContentBeanData(index, contentBeans.get(0));
            view.findViewById(R.id.ll_item_1).setVisibility(View.GONE);
        }else if(contentBeans.size() > 1){
            setOneContentBeanData(index, contentBeans.get(0));
            LiveingBean.ContentBean contentBean = contentBeans.get(1);
            //直播封面
            ImageView img_liveing_cover_image1 = view.findViewById(R.id.img_liveing_cover_image1);
            String imgUrl = contentBean.getCoverImage();
            if(imgUrl != null){
                String url = imgUrl.contains("http")?contentBean.getCoverImage():AppConstants.BYTE_IMG_PREFIX + contentBean.getCoverImage() + AppConstants.BYTE_IMG_SUFFIX;
                Glide.with(XingLiveingActivity.this)
                        .load(url)
                        .apply(requestOptions)
                        .into(img_liveing_cover_image1);
            }

            //直播名称
            TextView tv_liveing_name1 = view.findViewById(R.id.tv_liveing_name1);
            tv_liveing_name1.setText(contentBean.getName());
            //直播时间
            TextView tv_liveing_time1 = view.findViewById(R.id.tv_liveing_time1);
            tv_liveing_time1.setText(format.format(new Date(contentBean.getLiveTime())));
            //观看人数
            TextView tv_join_count1 = view.findViewById(R.id.tv_join_count1);
            String count = getCount(contentBean);
            tv_join_count1.setText((TextUtils.isEmpty(count)?0:count)+("1".equals(contentBean.getLiveStatus())?"人已预约":"人参与"));
            view.findViewById(R.id.ll_item_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    queryAuthority(contentBean.getId());
                }
            });
        }
    }

    private void addContent(int index, List<LiveingBean.ContentBean> contentBean){
        if(contentBean != null && contentBean.size() > 0){
            if("0".equals(contentBean.get(0).getLiveStatus()) || "2".equals(contentBean.get(0).getLiveStatus()) || contentBean.size() == 1){
                offset++;
                LayoutInflater.from(this).inflate(R.layout.item_liveing, ll_content,true);
                setOneContentBeanData(index, contentBean.get(0));
            }else{
                offset++;
                LayoutInflater.from(this).inflate(R.layout.two_item_liveing, ll_content,true);
                setTwoContentBeanData(index, contentBean);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                goBack();
                break;
        }
    }

    private void goBack(){
        if("external".equals(source)){
            //外部分享进入的直播页面，返回到更多页面
            Intent intent = new Intent(this, ClubMainActivty.class);
            startActivity(intent);
            finish();
        }else{
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
