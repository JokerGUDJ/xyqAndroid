
package com.xej.xhjy.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.lava.base.util.StringUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.event.VideoCallEvent;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.util.string.MD5;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.utils.ImUtils;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.yunxin.nertc.ui.team.TeamG2Activity;
import com.xej.xhjy.R;
import com.xej.xhjy.bean.AdvertBean;
import com.xej.xhjy.bean.LiveBanner;
import com.xej.xhjy.bean.LiveingAuthBean;
import com.xej.xhjy.bean.MeettingListBean;
import com.xej.xhjy.bean.NewsBean;
import com.xej.xhjy.bean.XHMeeting;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.base.BaseFragmentForViewpager;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.common.view.marqueeview.MarqueeView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.receiver.NetWorkStatusEvent;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.tools.GlideImageLoader;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.adapter.RecycleViewAdapter;
import com.xej.xhjy.ui.datePicker.Util;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.ui.live.XingLiveingActivity;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginOutEvent;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.metting.MeetingActivity;
import com.xej.xhjy.ui.view.StatusBarHoldView;
import com.xej.xhjy.ui.view.TitleView;
import com.xej.xhjy.ui.web.LiveingWebview;
import com.xej.xhjy.ui.web.WebLearnPlatformActivity;
import com.xej.xhjy.ui.web.WebOtherPagerActivity;
import com.xej.xhjy.ui.web.WebPagerActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragmentForViewpager implements View.OnClickListener {
    private LinearLayout mLLPlatform, mLLTechnology, mLLOtherCourse, mLLTrends, mLLXej, mLLTrend, ll_liveing, ll_library,ll_more_meeting, ll_no_meeting;
    private ImageView mMeetState, mMeetEnter;
    private TextView mMeetName, mMeetTime, mMeetAddress, video_audio_call_txt;
    private Banner mBanner;
    private List<String> networkImages;
    private MarqueeView mMarqueeView;
    private NewsBean hotlineBean;
    private AdvertBean advertBean;
    private MeettingListBean.ContentBean meetBean;
    private TitleView mTitleView;
    private RelativeLayout video_audio_view;
    private String teamId = null, roomName, teamName, teamNick, headUrl;
    private ArrayList<String> accounts;
    private RecyclerView recyclerView;
    private ArrayList<XHMeeting> meetList = new ArrayList<>();
    private RecycleViewAdapter adapter;
    private Map<String, String> liveMap = new HashMap<>();


    /**
     * 初始化view
     */
    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        networkImages = new ArrayList<>();
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home, null);
        mLLPlatform = mRootView.findViewById(R.id.ll_platform);
        mLLPlatform.setOnClickListener(this);
        mLLOtherCourse = mRootView.findViewById(R.id.ll_other_course);
        mLLOtherCourse.setOnClickListener(this);
        mLLTechnology = mRootView.findViewById(R.id.ll_technology);
        mLLTechnology.setOnClickListener(this);
        mLLTrends = mRootView.findViewById(R.id.ll_course);
        mLLTrends.setOnClickListener(this);
        mLLXej = mRootView.findViewById(R.id.ll_xej);
        mLLXej.setOnClickListener(this);
        mLLTrend = mRootView.findViewById(R.id.ll_trend);
        mLLTrend.setOnClickListener(this);
        mMarqueeView = mRootView.findViewById(R.id.tv_hotline);
        video_audio_view = mRootView.findViewById(R.id.video_audio_view);
        video_audio_call_txt = mRootView.findViewById(R.id.video_audio_call_txt);
        video_audio_view.setOnClickListener(this);
        ll_liveing = mRootView.findViewById(R.id.ll_liveing);
        ll_liveing.setOnClickListener(this);
        ll_library = mRootView.findViewById(R.id.ll_library);
        ll_library.setOnClickListener(this);
        StatusBarHoldView status_bar = mRootView.findViewById(R.id.status_bar);
        NestedScrollView scrollView = mRootView.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if(scrollView.getScrollY() > Util.dpToPx(mActivity,240)){
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }else{
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            }
        });
        boolean bVideoMeet = PerferenceUtils.get("bVideoMeet", false);
        if (bVideoMeet) {
            teamNick = PerferenceUtils.get("teamNick", "");
            String teamId = PerferenceUtils.get("teamId","");
            String accId = PerferenceUtils.get("accId","");
            LogUtils.dazhiLog("进入的teamId="+teamId);
            Team team = NimUIKit.getTeamProvider().getTeamById(teamId);
            if (team!=null){
                video_audio_call_txt.setText((team.getType() == TeamTypeEnum.Advanced ? TeamHelper.getTeamName(teamId) : UserInfoHelper.getUserName(accId)) + "邀请您加入音视频通话，点击此处加入");
                video_audio_view.setVisibility(View.VISIBLE);
            }
        }

        mMarqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                NewsBean.ContentBean bean = hotlineBean.getContent().get(position);
                Intent intent = new Intent(mActivity, WebOtherPagerActivity.class);
                if("1".equals(bean.getWhetherUrl())){
                    intent.putExtra(WebOtherPagerActivity.LOAD_URL, bean.getContent());
                }else{
                    intent.putExtra(WebOtherPagerActivity.LOAD_CONTENT, bean.getContent());
                }
                EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                        "eventHeadline", "id="+bean.getId());
                intent.putExtra(WebOtherPagerActivity.HEAD_TITLE, "鑫合日讯");
                startActivity(intent);
            }
        });
        ll_more_meeting = mRootView.findViewById(R.id.ll_more_meeting);
        ll_more_meeting.setOnClickListener(this);
        recyclerView = mRootView.findViewById(R.id.recyclerview);
        //mMeetName = mRootView.findViewById(R.id.tv_meet_title);
        mTitleView = mRootView.findViewById(R.id.titleview);
        //mMeetTime = mRootView.findViewById(R.id.tv_meet_time);
        //mMeetAddress = mRootView.findViewById(R.id.tv_meet_address);
        //mMeetState = mRootView.findViewById(R.id.view_meet_state);
        //mMeetEnter = mRootView.findViewById(R.id.view_meet_enter);
        mBanner = mRootView.findViewById(R.id.home_auto_banner);
        ll_no_meeting = mRootView.findViewById(R.id.ll_no_meeting);
        if (AppConstants.IS_LOGIN) {
           // mMeetName.setText("近期无会议安排");
        } else {
            //mMeetName.setText("请先登录后查看会议信息");
        }
        getDataDictionary();
        //UV
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android", "uv",
                "mobilephone="+PerferenceUtils.get(AppConstants.User.PHONE,""));
    }

    /**
     * 初始化要加载的数据
     */
    @Override
    public void initDatas() {
        //检查首页会议缓存
        String firstMeet = PerferenceUtils.get(AppConstants.DATA_FIRST_MEET, "");
        if (!GenalralUtils.isEmpty(firstMeet) && !firstMeet.contains("[]")) {
            setFirstMeetting(firstMeet);
        } else {
//            getFirstMeetting();
        }
        getAdvertImage();
        getHeadline();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (networkImages.size() > 1) {
            mBanner.startAutoPlay();
        }
        getFirstMeetting();
        boolean bVideoMeet = PerferenceUtils.get("bVideoMeet", false);
        LogUtils.dazhiLog("更新首页最新="+bVideoMeet);
        if (bVideoMeet) {
            teamNick = PerferenceUtils.get("teamNick", "");
            String teamId = PerferenceUtils.get("teamId","");
            String accId = PerferenceUtils.get("accId","");
            Team team = NimUIKit.getTeamProvider().getTeamById(teamId);
            if (team!=null){
                video_audio_call_txt.setText((team.getType() == TeamTypeEnum.Advanced ? TeamHelper.getTeamName(teamId) : UserInfoHelper.getUserName(accId))  + "邀请您加入音视频通话，点击此处加入");
                video_audio_view.setVisibility(View.VISIBLE);
            }

        }
        adapter = new RecycleViewAdapter(mActivity, meetList);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mActivity);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager1);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (networkImages.size() > 1) {
            mBanner.stopAutoPlay();
        }
    }


    /**
     * 读取保存的图片
     */
    private void getCacheImage() {
        String bannerString = PerferenceUtils.get(AppConstants.DATA_BANNER_KEY, "");
        if (!GenalralUtils.isEmpty(bannerString)) {
            AdvertBean bean = JsonUtils.stringToObject(bannerString, AdvertBean.class);
            advertBean = bean;
            setBanner();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View v) {
        if (v == mLLPlatform) {//鑫合讲坛
            Intent intent = new Intent(mActivity, WebPagerActivity.class);
            intent.putExtra(WebPagerActivity.LOAD_URL, "WebsiteInfoList");
            EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                    "eventPlatform", "");
            startActivity(intent);
        } else if (v == mLLOtherCourse) {  //他山之石
            Intent intent = new Intent(mActivity, WebPagerActivity.class);
            intent.putExtra(WebPagerActivity.LOAD_URL, "WebsiteOtherList");
            EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                    "eventOtherCase", "");
            startActivity(intent);
        } else if (v == mLLTechnology) {//鑫合科技
            Intent intent = new Intent(mActivity, WebPagerActivity.class);
            intent.putExtra(WebPagerActivity.LOAD_URL, "WebsiteTechnology");
            EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                    "eventTechnology", "");
            startActivity(intent);
        } else if (v == mLLTrend) {//移动学习平台
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    checkWhiteList();
                }
            }, mActivity);

        } else if (v == mLLTrends) {//鑫合动态
            Intent intent = new Intent(mActivity, WebPagerActivity.class);
            intent.putExtra(WebPagerActivity.LOAD_URL, "WebsiteClubList");
            EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                    "eventTrends", "");
            startActivity(intent);
        } else if (v == mLLXej) {//鑫E家平台,装了就启动，没装去下载
            EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                    "eventEHome", "");
            Intent intent = new Intent(mActivity, ImageViewDetailActivity.class);
            intent.putExtra(ImageViewDetailActivity.LOAD_URL, "android.resource://" + mActivity.getPackageName() + "/" + R.drawable.xej);
            startActivity(intent);
        } else if (v == video_audio_view) {//喚起被邀请页面
            LogUtils.dazhiLog("点击了");
            getAVList();
        }else if( v == ll_liveing){
            //鑫直播
            startXinLivingActivity();
        }else if(v == ll_library){
            //鑫书房
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    startZXLibraryActivity();
                }
            }, mActivity);
        }else if(v == ll_more_meeting){
            //更多会议
            startMeetingActivity();
        }
    }
    /**
     * 跳转更多会议
     */

    private void startMeetingActivity(){
        Intent intent = new Intent(mActivity, MeetingActivity.class);
        mActivity.startActivity(intent);
    }


    /**
     * 跳转鑫直播
     */

    private void startXinLivingActivity(){
        Intent intent = new Intent(mActivity, XingLiveingActivity.class);
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                "eventLive", "");
        mActivity.startActivity(intent);
    }

    /**
     * 跳转鑫书房
     */

    private void startXinLibraryActivity(){
        Intent intent = new Intent(mActivity, LiveingWebview.class);
        intent.putExtra(LiveingWebview.LOAD_URL,"http://open.ximalaya.com/new-site/#/index/346/d865edfae3e77b073cdcc2384223bd30");
        intent.putExtra("name", "鑫书房");
        intent.putExtra("closeBtn", true);
        mActivity.startActivity(intent);
    }

    /**
     * 跳转中信书院
     */
    private void startZXLibraryActivity(){
        Intent intent = new Intent(mActivity, LiveingWebview.class);
        String pAppKey = "1434719899468693504";
        String pAppSec = "cfcc7c0506a1ad567052430ffde63480";
        String pUid = PerferenceUtils.get(AppConstants.User.PHONE, "");
        long pTimestamp = System.currentTimeMillis()/1000;
        StringBuffer signString = new StringBuffer();
        signString.append("pAppKey=")
                  .append(pAppKey)
                  .append("&pUid=")
                  .append(pUid)
                  .append("&pTimestamp=")
                  .append(pTimestamp);
        String params = signString.toString();
        String pSign = MD5.getStringMD5(signString.append("&pAppSec=")
                .append(pAppSec).toString());
        intent.putExtra(LiveingWebview.LOAD_URL,"https://v.yunpub.cn/platform?" + params + "&pSign=" + pSign);
        intent.putExtra("name", "鑫书房");
        intent.putExtra("closeBtn", true);
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageHome", "channel=android",
                "eventLibrary", "");
        mActivity.startActivity(intent);
    }

    /**
     * 获取首页轮播图
     */
    public void getAdvertImage() {
        String TAG3 = "advert_image";
        mActivity.addTag(TAG3);
        Map<String, String> paras = new HashMap<>();
        paras.put("pageNum", "0");
        paras.put("pageSize", "5");
        paras.put("picType", "2");
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.ADVERT_IMAGE, TAG3, paras, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    networkImages.clear();
                    getBanner();
                    AdvertBean bean = JsonUtils.stringToObject(jsonString, AdvertBean.class);
                    if (bean != null && bean.getCode().equals("0")) {
                        LogUtils.dazhiLog("轮播图返回成功----->" + jsonString);
                        if (bean.getContent().size() > 0) {
                            advertBean = bean;
                            for (int i = 0; i < bean.getContent().size(); i++) {
                                String url = NetConstants.IMAGE_NORMAL + bean.getContent().get(i).getPicFileAddr();
                                networkImages.add(url);
                                liveMap.put(url, bean.getContent().get(i).getReferLink());
                            }
                            setBanner();
                            PerferenceUtils.put(AppConstants.DATA_BANNER_KEY, jsonString);
                        }
                    } else {
                        //如果失败读取缓存图片
                        getCacheImage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.dazhiLog("banner error===" + errorMsg);
                //如果失败读取缓存图片
                getCacheImage();
            }
        });
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
                            for(int i = 0; data != null && i < data.size(); i++){
                                LiveBanner.ContentBean contentBean = data.get(i);
                                String coverImage = contentBean.getCoverImage();
                                String url;
                                if(!TextUtils.isEmpty(coverImage)){
                                    url = coverImage.startsWith("http")?contentBean.getCoverImage():(AppConstants.BYTE_IMG_PREFIX + contentBean.getCoverImage() + AppConstants.BYTE_IMG_SUFFIX);
                                }else{
                                    url = "res://"+ mActivity.getPackageName()+"/" + R.drawable.liveing_default_img;
                                }
                                liveMap.put(url, contentBean.getId());
                                networkImages.add(0,url);
                            }
                            setBanner();
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


    /**
     * 设置轮播图数据
     *
     * @param
     */
    private void setBanner() {
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(networkImages);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Accordion);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置点击事件
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String obj = liveMap.get(networkImages.get(position));
                if(!TextUtils.isEmpty(obj)){
                    //兼容直播轮播图
                    if(networkImages.get(position).contains("xhyjcms/meet") ){

                        if(obj.contains("MSignUpFirst")){
                            //跳转报名页面
                            String[] params = obj.split("\\?");
                            try {
                                if(params.length > 1){
                                    String[] meetids = params[1].split("&");
                                    if(meetids != null && meetids.length > 0){
                                        String meetId = "", meetName = "";
                                        for(int i = 0; i < meetids.length; i++){
                                            if(meetids[i].contains("meetId")){
                                                String[] array = meetids[i].split("=");
                                                if(array != null && array.length == 2){
                                                    meetId = array[1];
                                                }
                                            }else if(meetids[i].contains("meetName")){
                                                String[] array = meetids[i].split("=");
                                                if(array != null && array.length == 2){
                                                    meetName = array[1];
                                                }
                                            }
                                        }
                                        if(!StringUtils.isEmpty(meetId) && !StringUtils.isEmpty(meetName)){
                                            checkQuery(meetId, meetName);
                                        }else{
                                            ToastUtils.shortToast(mActivity, "请配置鑫合家园相关跳转链接！");
                                        }
                                    }
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }else{
                            Uri uri = Uri.parse(obj);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }else{
                        queryAuthority(obj);
                    }
                }
            }
        });
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        mBanner.startAutoPlay();
    }

    /**
     * 会议报名校验接口
     */
    private void checkQuery(String meetId, String meetName) {
        String TAG = "meeting_check_query";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("meetId", meetId);
        String url = NetConstants.NEW_MEETTING_SIGNUP_CHECKED;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        if(LoginUtils.isOrgUser()){
                            Intent intent = new Intent(mActivity, WebPagerActivity.class);
                            intent.putExtra(WebPagerActivity.LOAD_URL, "MSignUpFirst?meetId="+meetId);
                            startActivity(intent);
                        }else{
                            showSignUpDialog(meetName);
                        }
                    }else{
                        ToastUtils.shortToast(mActivity, json.optString("msg"));
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
     * 向机构联系人发短信弹框
     */
    private void showSignUpDialog(String meetName){
        ClubDialog dialog = new ClubDialog(mActivity);
        dialog.setMessage("向机构联系人发送报名申请？");
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
                        ToastUtils.shortToast(mActivity, "短信发送成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(mActivity, "短信发送成功");
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
                                    Intent intent = new Intent(mActivity, LiveingWebview.class);
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
                ToastUtils.shortToast(mActivity, errorMsg);
            }
        });
    }

    /**
     * 获取头条数据
     */
    public void getHeadline() {
        String TAG2 = "hot_line";
        mActivity.addTag(TAG2);
        Map<String, String> maps = new HashMap<>();
        maps.put("pageSize", "3");
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.NEW_HOT_NEWS, TAG2, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    NewsBean bean = JsonUtils.stringToObject(jsonString, NewsBean.class);
                    if (bean != null && bean.getCode().equals("0")) {
                        if (bean.getContent().size() > 0) {
                            hotlineBean = bean;
                            List<String> info = new ArrayList<>();
                            for (int i = 0; i < bean.getContent().size(); i++) {
                                info.add(bean.getContent().get(i).getName());
                            }
                            mMarqueeView.startWithList(info);
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

    /**
     * 请求全局数据匹配字典
     */
    private void getDataDictionary() {
        String data_dic = PerferenceUtils.get(AppConstants.DATA_DICTIONARY_KEY, "");
        if (!GenalralUtils.isEmpty(data_dic)) {//有就直接获取
            try {
                AppConstants.DATA_DICTIONARY = new JSONObject(data_dic);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String TAG = "getDataDictionary";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.DATA_DICTIONARY, TAG, new HashMap<>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONObject content = json.optJSONObject("content");
                        if (content != null) {
                            AppConstants.DATA_DICTIONARY = content;
                            PerferenceUtils.put(AppConstants.DATA_DICTIONARY_KEY, content.toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.dazhiLog("字典---》失败");
            }
        });
    }


    /**
     * 获取最新会议
     */
    public void getFirstMeetting() {
        String TAG1 = "getFirstMeetting";
        mActivity.addTag(TAG1);
        String url = NetConstants.QUERY_MEETINGS_HOME;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG1, new HashMap<>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("一条会议数据---》" + jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        org.json.JSONArray meets = jsonObject.optJSONArray("content");
                        meetList.clear();
                        for(int i = 0; meets != null && i < meets.length(); i++){
                            meetList.add(JsonUtils.stringToObject(meets.get(i).toString(), XHMeeting.class));
                        }
                        if(!meetList.isEmpty() && adapter != null){
                            adapter.setMeetList(meetList);
                            adapter.notifyDataSetChanged();
                            ll_no_meeting.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }else{
                            ll_no_meeting.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }else{
                        ll_no_meeting.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                ll_no_meeting.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 校验接口
     */
    private void checkQuery(final String str) {
        String TAG = "check_query_metting";
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
                        LogUtils.dazhiLog("会议校验----" + jsonString);
                        toLoginShow(str);
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

    /**
     * 移动学习平台校验白名单
     */
    private void checkWhiteList() {
        String TAG = "check_white_list";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("phone", PerferenceUtils.get(AppConstants.User.PHONE, ""));
        String url = NetConstants.CHECK_WHITE_LIST;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        String token = PerferenceUtils.get("g_token", "");
//                    String url ="https://test.exexm.com:8060/test/xhjy/main.html";
                        LogUtils.dazhiLog("g_token-->" + token);
                        String url = NetConstants.WEB_LEARN_PLATFORM_URL + token;

                        Intent intent = new Intent(mActivity, WebLearnPlatformActivity.class);
                        intent.putExtra(WebPagerActivity.LOAD_URL, url);
                        startActivity(intent);

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


    /**
     * 获取当前用户被邀请的适配会议列表
     */
    private void getAVList() {
        String TAG = "get_av_list";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        final String url = ImUtils.QUERY_AV_LIST;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map,  new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    //存储字典
                    org.json.JSONObject json = new org.json.JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        org.json.JSONArray array = json.getJSONArray("content");
                        if (array != null && array.length() > 0) {
                            JSONObject content = (JSONObject) array.get(0);
                            accounts = new ArrayList<>();
                            if (content != null) {
                                roomName = content.getString("roomId");
                                teamId = content.getString("tId");
                                teamName = content.getString("tName");
                                String inviteUser = content.getString("inviteUser");
                                UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(inviteUser);
                                teamNick = userInfo.getName();
                                headUrl = userInfo.getAvatar();
                                org.json.JSONArray accIdList = content.getJSONArray("accIdList");
                                for (int i = 0; accIdList != null && i < accIdList.length(); i++) {
                                    accounts.add((String) (accIdList.get(i)));
                                }
                                LogUtils.dazhiLog("teamId="+teamId);
                                if (TextUtils.isEmpty(teamId)) {
                                    LogUtils.dazhiLog("teamId为空");
                                    ToastUtils.shortToast(mActivity, "您参加的会议已结束");
                                    video_audio_view.setVisibility(View.GONE);
                                    PerferenceUtils.put("bVideoMeet", false);
                                    PerferenceUtils.put("teamNick", "");
                                    PerferenceUtils.put("teamId", "");
                                    PerferenceUtils.put("accId", "");

                                } else {
                                    LogUtils.dazhiLog("teamId不为为空");
                                    String accId = PerferenceUtils.get("accId","");
                                    TeamG2Activity.startActivity(mActivity, false, teamId, accounts, teamName);
                                }
                            }
                        } else {
                            ToastUtils.shortToast(mActivity, "您参加的会议已结束");
                            video_audio_view.setVisibility(View.GONE);
                            PerferenceUtils.put("bVideoMeet", false);
                            PerferenceUtils.put("teamNick", "");
                            PerferenceUtils.put("teamId", "");
                            PerferenceUtils.put("accId", "");
                        }
                    }else if(json.optString("code").equals("4005")){
                        video_audio_view.setVisibility(View.GONE);
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

    /**
     * 跳转登录
     *
     * @param str
     */
    private void toLoginShow(String str) {
        Intent intent = new Intent(mActivity, WebPagerActivity.class);
        intent.putExtra(WebPagerActivity.LOAD_URL, "MeetDetail");
        intent.putExtra(WebPagerActivity.MEETTING_ID, str);
        startActivity(intent);
    }

    /**
     * 设置首页会议信息
     *
     * @param json
     */
    private void setFirstMeetting(String json) {
        try{
            meetBean = JsonUtils.stringToObject(json, MeettingListBean.ContentBean.class);
            if (meetBean != null) {
                mMeetName.setText(meetBean.getName());
                mMeetAddress.setText(meetBean.getAddress());
                mMeetTime.setText(meetBean.getBeginDate().split(" ")[0]);
                if (AppConstants.DATA_DICTIONARY == null) {
                    return;
                }
                String state = LoginUtils.getMeetState(meetBean.getStt());
                if ("报名中".equals(state)) {
                    mMeetState.setVisibility(View.GONE);
                    mMeetEnter.setVisibility(View.VISIBLE);
                } else {
                    mMeetState.setVisibility(View.VISIBLE);
                    mMeetEnter.setVisibility(View.GONE);
                    if ("进行中".equals(state)) {
                        mMeetState.setImageResource(R.drawable.ic_meeting_going);
                    } else if ("已结束".equals(state)) {
                        mMeetState.setImageResource(R.drawable.ic_meeting_over);
                    } else if ("报名结束".equals(state)) {
                        mMeetState.setImageResource(R.drawable.ic_meeting_enter_over);
                    } else {
                        mMeetState.setImageResource(R.drawable.ic_meeting_over);
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 收到登录成功的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        getFirstMeetting();
    }


    /**
     * 收到视频邀请事件
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoAudioEvent(CustomNotification customNotification) {
//        if (customNotification != null) {
//            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(customNotification.getContent());
//            final Map<String, Object> payload = customNotification.getPushPayload();
//            int msgType = (int) payload.get("msgType");
//            if (msgType == CustomMsgType.MSG_INVITE.getValue()) {
//                video_audio_view.setVisibility(View.VISIBLE);
//                video_audio_call_txt.setText("点击此处加入会议");
////                roomName = jsonObject.getString(TeamAVChatProfile.KEY_RID);
//                teamId = jsonObject.getString(TeamAVChatProfile.KEY_TID);
//                JSONArray accountArray = jsonObject.getJSONArray(TeamAVChatProfile.KEY_MEMBER);
//                accounts = new ArrayList<>();
//                if (accountArray != null) {
//                    for (Object o : accountArray) {
//                        accounts.add((String) o);
//                    }
//                }
//                teamName = jsonObject.getString(TeamAVChatProfile.KEY_TNAME);
//                teamNick = payload.get("teamNick").toString();
//                //呼叫人头像
//                headUrl = payload.get("headUrl").toString();
//                String teamNick = payload.get("teamNick").toString();
//                String accId = payload.get("accId").toString();
//                Team team = NimUIKit.getTeamProvider().getTeamById(teamId);
//                if (team != null) {
//                    video_audio_call_txt.setText((team.getType() == TeamTypeEnum.Advanced ? TeamHelper.getTeamName(teamId) : UserInfoHelper.getUserName(accId))+ "邀请您加入音视频通话，点击此处加入");
//                }else{
//                    video_audio_call_txt.setText(teamNick+"邀请您加入音视频通话，点击此处加入");
//                }
////                video_audio_view.setVisibility(View.VISIBLE);
//                PerferenceUtils.put("bVideoMeet", true);
//                PerferenceUtils.put("teamNick", teamNick);
//                PerferenceUtils.put("teamId", teamId);
//                PerferenceUtils.put("accId", accId);
//            } else if (msgType == CustomMsgType.MSG_HANDUP.getValue()) {
//                teamId = null;
//                if (video_audio_view.getVisibility() == View.VISIBLE) {
//                    video_audio_view.setVisibility(View.GONE);
//                    PerferenceUtils.put("bVideoMeet", false);
//                    PerferenceUtils.put("teamNick", "");
//                    PerferenceUtils.put("teamId", "");
//                    PerferenceUtils.put("accId", "");
//                }
//            }
//        }
    }

    @Subscribe
    public void onVideoCallEvent(VideoCallEvent event) {
       String accId = PerferenceUtils.get("accId","");
        TeamG2Activity.startActivity(mActivity, false, teamId, accounts, teamName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 收到创建会议成功的消息
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIncomeingCallEvent(boolean b) {
        getFirstMeetting();
    }

    /**
     * 收到登出的消息刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginOutEvent(LoginOutEvent event) {
        getFirstMeetting();
    }

    /**
     * 收到是否有新Message消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasNewMessage(HasMessageEvent event) {
        LogUtils.dazhiLog("Home收到是否有消息----------" + event.getHasMessage());
        if(mTitleView != null){
            mTitleView.setNewMessageVisibile(event.getHasMessage());
        }
    }


    /**
     * 收到网络状态变化消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetWorkEvent(NetWorkStatusEvent event) {
        getDataDictionary();
        getAdvertImage();
        getHeadline();
        getFirstMeetting();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        LoginCheckUtils.clear();
        super.onDestroy();
    }
}
