package com.xej.xhjy.ui.main;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;

import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.LoginCallBack;
import com.xej.xhjy.ui.web.WebPagerActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: XHJY_NEW_UAT
 * @Package: com.xej.xhjy.ui.main
 * @ClassName: MainDialogShowUtils
 * @Description: 类作用描述  如果有活动弹框操作， 登录后校验是否维护培训信息
 * @Author: lihy_0203
 * @CreateDate: 2019/3/15 上午9:55
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/15 上午9:55
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MainDialogShowUtils {

    public static void ShowDialog(Context context) {
        String tag = "fete_check";
        Map<String, String> map = new HashMap<>();
        map.put("mobile", PerferenceUtils.get(AppConstants.User.PHONE, ""));
        RxHttpClient.doPostStringWithUrl((BaseActivity) context, NetConstants.QUERY_TRAINING, tag, map, new HttpCallBack() {

            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("培训成功----》" + jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONObject content = jsonObject.optJSONObject("content");
                    if ("0".equals(jsonObject.optString("code")) && content != null) {
                        String auth = content.optString("auth");
                        PerferenceUtils.put(AppConstants.DATA_TRAINING_ID_KEY, auth);
                        if (!TextUtils.isEmpty(auth)) {
                            if ("yes".equals(auth)) {
                                EventBus.getDefault().post(new TrainMeetEvent(true));
                            } else {
                                EventBus.getDefault().post(new TrainMeetEvent(false));
                            }
                        }
                        String box = content.optString("box");
                        if (!TextUtils.isEmpty(box) && "yes".equals(box)) {
                            ClubDialog dialog = new ClubDialog(context);
                            dialog.setMessage("请录入2019年会议及培训报名信息");
                            dialog.setTitle("温馨提示");
                            dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
                            dialog.setPositiveListener("去录入", new PositiveListener() {
                                @Override
                                public void onPositiveClick() {
                                    LoginCallBack mCallBack = new LoginCallBack() {
                                        @Override
                                        public void loginAfterRun() {
                                            Intent intent = new Intent(context, WebPagerActivity.class);
                                            intent.putExtra(WebPagerActivity.LOAD_URL, "TrainSignUp");
                                            intent.putExtra(WebPagerActivity.MISS_MESSAGE, true);
                                            ((BaseActivity) context).startActivityWithAnim(intent);
                                        }
                                    };
                                    mCallBack.isPass = true;
                                    LoginUtils.startCheckLogin((BaseActivity) context, mCallBack);
                                }
                            });
                            dialog.setNegativeListener("取消", new NegativeListener() {
                                @Override
                                public void onNegativeClick() {
                                }
                            });
                            dialog.show();

                        }
                        //发送完之后注销
                        EventBus.getDefault().unregister(this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.dazhiLog("培训失败----》" + errorMsg);
            }
        });

    }
}
