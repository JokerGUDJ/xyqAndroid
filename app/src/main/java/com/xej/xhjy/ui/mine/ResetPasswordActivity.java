package com.xej.xhjy.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.tools.PassGuardUtils;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.LoginActivity;
import com.xej.xhjy.ui.view.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.passguard.PassGuardEdit;

/**
 * @author dazhi
 * @class RegisterActivity 修改密码的activity
 * @Createtime 2018/6/21 16:12
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class ResetPasswordActivity extends BaseActivity {
    private final String TAG = "reset_password";
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.edt_old_password)
    PassGuardEdit edtOldPassword;
    @BindView(R.id.edt_new_password1)
    PassGuardEdit edtNewPassword1;
    @BindView(R.id.edt_new_password2)
    PassGuardEdit edtNewPassword2;
    private ClubLoadingDialog mLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        PassGuardUtils.initialize(edtOldPassword);
        PassGuardUtils.initialize(edtNewPassword1);
        PassGuardUtils.initialize(edtNewPassword2);
        initView();
    }

    private void initView() {
        mLoadingDialog = new ClubLoadingDialog(this);
    }

    @OnClick(R.id.btn_save)
    void doConfirm() {
        if (checkData()) {
            getTimeMills();
        }
    }

    private boolean checkData() {
        String p0 = edtOldPassword.getMD5();
        String p1 = edtNewPassword1.getMD5();
        String p2 = edtNewPassword2.getMD5();
        if (TextUtils.isEmpty(p0)){
            showMessageDialog("请填写原密码！", null);
            return false;
        }
        if (TextUtils.isEmpty(p1)) {
            showMessageDialog("请填写新密码！", null);
            return false;
        }
        if (TextUtils.isEmpty(p2)) {
            showMessageDialog("请填写确认密码！", null);
            return false;
        }
        if (p0.equals(p1) || p0.equals(p2)){
            showMessageDialog("旧密码和新密码不能相同", null);
            return false;
        }
        if (!p1.equals(p2)) {
            showMessageDialog("两次新密码不一致！请重新填写！", null);
            return false;
        }
          String newpwd1 =  edtNewPassword1.getText().toString().trim();
          String newpwd2 = edtNewPassword2.getText().toString().trim();
        if (newpwd1.length()<6 || newpwd2.length()<6) {
            showMessageDialog("密码的长度不能小于六位", null);
            return false;
        }
        return true;
    }

    void getTimeMills() {
        String TAG = "getTimeMills-reset";
        mActivity.addTag(TAG);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.PWD_TIME_MILLIS, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("getTimeMills---》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONObject content = json.getJSONObject("content");
                        String currTime = content.optString("currTime");
                        String sKey = content.optString("sKey");
                        String affineX = content.optString("affineX");
                        String affineY = content.optString("affineY");
                        LogUtils.dazhiLog("sKey=" + sKey);

                        if (sKey != null) {
                            // 返回时间戳放入密码控件生成密文
                            edtOldPassword.setCipherKey(sKey);
                            edtOldPassword.setEccKey(affineX + "|" + affineY);
                            edtNewPassword1.setCipherKey(sKey);
                            edtNewPassword1.setEccKey(affineX + "|" + affineY);
                            edtNewPassword2.setCipherKey(sKey);
                            edtNewPassword2.setEccKey(affineX + "|" + affineY);
                        }
                        if (!GenalralUtils.isEmpty(sKey)) {
                            postNewPassword(currTime);
                        } else {
                            ToastUtils.shortToast(mActivity, "修改密码失败！");
                            mLoadingDialog.dismiss();
                        }

                    } else {
                        ToastUtils.shortToast(mActivity, "修改密码失败！");
                        mLoadingDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
                ToastUtils.shortToast(mActivity, "登录失败！");
            }
        });


    }


    private void postNewPassword(String timeToken) {
        Map<String, String> maps = new HashMap<>();
        maps.put("mobilephone", PerferenceUtils.get(AppConstants.User.PHONE, ""));
        maps.put("originalPassword", edtOldPassword.getSM2SM4Ciphertext());
        maps.put("password", edtNewPassword1.getSM2SM4Ciphertext());
        maps.put("confPassword", edtNewPassword2.getSM2SM4Ciphertext());
        maps.put("timeToken",timeToken);
        addTag(TAG);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.MODIFY_PASSWORD, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("修改密码---》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        showMessageDialog("密码修改成功！", new PositiveListener() {
                            @Override
                            public void onPositiveClick() {
                                finishWithAnim();
                                LoginUtils.clearLoginInfo();
                                mActivity.startActivityWithAnim(new Intent(mActivity, LoginActivity.class));

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
            }
        });
    }

    private void showMessageDialog(String mess, PositiveListener listener) {
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(this);
        dialog.setMessage(mess);
        if (listener != null) {
            dialog.setPositiveListener("确定", listener);
        }
        dialog.show();
    }

}
