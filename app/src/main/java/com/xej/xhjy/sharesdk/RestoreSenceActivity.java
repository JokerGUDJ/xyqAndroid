package com.xej.xhjy.sharesdk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.mob.moblink.MobLink;
import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;
import com.xej.xhjy.bean.LiveingAuthBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.HttpDialogUtils;
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


public class RestoreSenceActivity extends BaseActivity implements SceneRestorable{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化和注册
        AppConstants.APP_VERSION = UpdateUtils.getVerName(this);
    }
    @Override
    // 必须重写该方法，防止MobLink在某些情景下无法还原
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MobLink.updateNewIntent(getIntent(), this);
    }

    @Override
    public void onReturnSceneData(Scene scene) {
        boolean succ = true;
        if(scene != null){
            HashMap<String, Object> params = scene.getParams();
            if(params != null){
                String liveId = (String)params.get("liveId");
                if(!TextUtils.isEmpty(liveId)){
                    if(liveId.startsWith(MEETING_PREFIX)){
                        //会议
                        String meetId = liveId.replace(MEETING_PREFIX, "");
                        if(!TextUtils.isEmpty(meetId)){
                            Intent intent = new Intent(this, OnlineMeetingDetailActivity.class);
                            intent.putExtra(MEETING_ID, meetId);
                            startActivity(intent);
                            finish();
                        }else{

                            succ = false;
                        }
                    }else{
                        //直播
                        LoginCheckUtils.isCertification = true;
                        LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                            @Override
                            public void onlogin() {
                                queryAuthority(liveId);
                            }
                        }, RestoreSenceActivity.this);
                    }
                }
            }else{
                succ = false;
            }
        }else{
            succ = false;
        }
        if(!succ){
            ToastUtils.shortToast(this, "跳转失败");
            finish();
        }
    }

    //直播观看权限查询
    private void queryAuthority(String liveId){
        String TAG = "get_authority";
        //从外部进来需要设置app verison
        HashMap<String ,String> params = new HashMap<>();
        params.put("liveId", liveId);
        RxHttpClient.doPostStringWithUrl(this, NetConstants.QUERY_VIEW_AUTHORITY, TAG, params, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    if(!TextUtils.isEmpty(jsonString)){
                        LiveingAuthBean bean = JsonUtils.stringToObject(jsonString, LiveingAuthBean.class);
                        if(bean != null){
                            if("0".equals(bean.getCode())){
                                LiveingAuthBean.Content content = bean.getContent();
                                if(content != null){
                                    Intent intent = new Intent(RestoreSenceActivity.this, LiveingWebview.class);
                                    if("1".equals(content.getLiveStatus())){
                                        intent.putExtra(LiveingWebview.LOAD_URL, NetConstants.BASE_IP+"xhyjcms/mobile/index.html#/livescreamdetail");
                                        intent.putExtra("liveId", liveId);
                                    }else{
                                        intent.putExtra(LiveingWebview.LOAD_URL, content.getUrl()+content.getViewUrlPath()+content.getParameters());
                                    }
                                    intent.putExtra("name",content.getLiveName());
                                    intent.putExtra("coverImage", content.getCoverImage());
                                    intent.putExtra("source","external");//外部跳转到更多聚合页标识
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                HttpDialogUtils.doHttpDialog(mActivity, bean.getMsg(),new PositiveListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        Intent intent = new Intent(RestoreSenceActivity.this, LiveingMoreActivity.class);
                                        intent.putExtra("source","external");//外部跳转到更多聚合页标识
                                        startActivity(intent);
                                        finish();
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
                ToastUtils.shortToast(RestoreSenceActivity.this, errorMsg);
                finish();
            }
        });
    }
}
