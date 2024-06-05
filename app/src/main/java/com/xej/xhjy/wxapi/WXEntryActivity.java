/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.xej.xhjy.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xej.xhjy.bean.LiveingAuthBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.live.LiveingMoreActivity;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.main.UpdateUtils;
import com.xej.xhjy.ui.metting.OnlineMeetingDetailActivity;
import com.xej.xhjy.ui.web.LiveingWebview;

import java.util.HashMap;

import static com.xej.xhjy.ui.metting.OnlineMeetingDetailActivity.MEETING_ID;
import static com.xej.xhjy.ui.metting.OnlineMeetingDetailActivity.MEETING_PREFIX;

/**
 *
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化和注册
        api = WXAPIFactory.createWXAPI(this, AppConstants.APPID, false);
        Intent intent = getIntent();
        api.handleIntent(intent, this);
        //从外部进来需要设置app verison
        AppConstants.APP_VERSION = UpdateUtils.getVerName(mActivity);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //获取开放标签传递的extinfo数据逻辑
        if(baseReq.getType() == ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX && baseReq instanceof ShowMessageFromWX.Req) {
            ShowMessageFromWX.Req showReq = (ShowMessageFromWX.Req) baseReq;
            WXMediaMessage mediaMsg = showReq.message;
            String extInfo = mediaMsg.messageExt;
            if(!TextUtils.isEmpty(extInfo)){
                if(extInfo.startsWith(MEETING_PREFIX)){
                    //会议
                    String meetId = extInfo.replace(MEETING_PREFIX, "");
                    if(!TextUtils.isEmpty(meetId)){
                        Intent intent = new Intent(this, OnlineMeetingDetailActivity.class);
                        intent.putExtra(MEETING_ID, meetId);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    LoginCheckUtils.isCertification = true;
                    LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                        @Override
                        public void onlogin() {
                            queryAuthority(extInfo);
                        }
                    }, WXEntryActivity.this);
                }
            }else{
                ToastUtils.shortToast(WXEntryActivity.this, "获取参数有误，请重试");
                finish();
            }

        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp != null && !TextUtils.isEmpty(baseResp.errStr)){
            ToastUtils.shortToast(this,baseResp.errStr);
        }
        finish();
    }

    //直播观看权限查询
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
                                    Intent intent = new Intent(WXEntryActivity.this, LiveingWebview.class);
                                    if("1".equals(content.getLiveStatus())){
                                        intent.putExtra(LiveingWebview.LOAD_URL, NetConstants.BASE_IP+"xhyjcms/mobile/index.html#/livescreamdetail");
                                    }else{
                                        intent.putExtra(LiveingWebview.LOAD_URL, content.getUrl()+content.getViewUrlPath()+content.getParameters());
                                    }
                                    intent.putExtra("liveId", liveId);
                                    intent.putExtra("name",content.getLiveName());
                                    intent.putExtra("coverImage", content.getCoverImage());
                                    intent.putExtra("source","external");//外部跳转到更多聚合页标识
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(WXEntryActivity.this, new PositiveListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        Intent intent = new Intent(WXEntryActivity.this, LiveingMoreActivity.class);
                                        intent.putExtra("source","external");//外部跳转到更多聚合页标识
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                if(dialog != null){
                                    dialog.setTitle("提示");
                                    dialog.setMessage(bean.getMsg());
                                    dialog.show();
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
                ToastUtils.shortToast(WXEntryActivity.this, errorMsg);
                finish();
            }
        });
    }
}
