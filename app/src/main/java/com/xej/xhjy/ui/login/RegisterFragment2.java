package com.xej.xhjy.ui.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.countdown.CountdownView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.tools.EventTrackingUtil;
import com.xej.xhjy.tools.PassGuardUtils;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.passguard.PassGuardEdit;

/**
 * @author dazhi
 * @class RegisterFragment1 找回密码第二个页面
 * @Createtime 2018/6/21 16:43
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class RegisterFragment2 extends BaseFragment {
    private final String TAG1 = "img_auth", TAG2 = "add_user", TAG3 = "sms_auth";
    Unbinder mUnbinder;
    @BindView(R.id.edt_auth)
    EditText edtAuth;
    @BindView(R.id.img_auth)
    ImageView imgAuth;
    @BindView(R.id.edt_sms)
    EditText edtSms;
    @BindView(R.id.cd_sms)
    CountdownView tvSms;
    @BindView(R.id.register_password1)
    PassGuardEdit edtPassword1;
    @BindView(R.id.register_password2)
    PassGuardEdit edtPassword2;

    private Bitmap mImageAute;
    private ClubLoadingDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_register2, null);
        mUnbinder = ButterKnife.bind(this, view);
        PassGuardUtils.initialize(edtPassword1);
        PassGuardUtils.initialize(edtPassword2);
        mDialog = new ClubLoadingDialog(mActivity);
        getAuthImage();
        return view;
    }

    @OnClick(R.id.btn_step2_next)
     void getTimeMills() {
        if (!checkData()) {
            return;
        }
        String TAG = "getTimeMillsRegister";
        mActivity.addTag(TAG);
        mDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.PWD_TIME_MILLIS, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONObject content = json.getJSONObject("content");
                        String sKey = content.optString("sKey");
                        String affineX = content.optString("affineX");
                        String affineY = content.optString("affineY");
                        LogUtils.dazhiLog("sKey=" + sKey);

                        if (!GenalralUtils.isEmpty(sKey)) {
                            // 返回时间戳放入密码控件生成密文
                            edtPassword1.setCipherKey(sKey);
                            edtPassword1.setEccKey(affineX + "|" + affineY);
                            edtPassword2.setCipherKey(sKey);
                            edtPassword2.setEccKey(affineX + "|" + affineY);
                            postAddUser();
                        } else {
                            ToastUtils.shortToast(mActivity, "登录失败！");
                            mDialog.dismiss();
                        }
                    } else {
                        ToastUtils.shortToast(mActivity, "登录失败！");
                        mDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mDialog.dismiss();
                ToastUtils.shortToast(mActivity, "登录失败！");
            }
        });
    }


    private void postAddUser() {
        //注册点击埋点
        EventTrackingUtil.EventTrackSubmit(mActivity, "pageRegister", "channel=android",
                "eventRegister", "mobilephone="+PerferenceUtils.get(AppConstants.User.PHONE,""));
        mActivity.addTag(TAG2);
        Map<String, String> maps = ((RegisterActivity) mActivity).getData();
        maps.put("password", edtPassword1.getSM2SM4Ciphertext());
        maps.put("confPassword",edtPassword2.getSM2SM4Ciphertext());
        maps.put("imgCode", edtAuth.getText().toString().trim());
        maps.put("mToken", edtSms.getText().toString().trim());
        maps.put("createChannel", "APP");
        maps.put("createModule", "MEET");
        mDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.REGISTER_USER, TAG2, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mDialog.dismiss();
                LogUtils.dazhiLog("注册返回---" + jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        ((RegisterActivity) mActivity).switchToFragment(2);
                    } else {
                        ToastUtils.shortToast(mActivity, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mDialog.dismiss();
                LogUtils.dazhiLog("注册返回shibai---" + errorMsg);
            }
        });
    }

    private boolean checkData() {
        if (GenalralUtils.isEmpty(edtAuth.getText().toString().trim())) {
            showMessageDialog("请填写图形验证码！");
            return false;
        }
        if (GenalralUtils.isEmpty(edtSms.getText().toString().trim())) {
            showMessageDialog("请填写短信验证码！");
            return false;
        }
        String p1 = edtPassword1.getText().toString().trim();
        String p2 = edtPassword2.getText().toString().trim();
        if (GenalralUtils.isEmpty(p1)) {
            showMessageDialog("请填写密码！");
            return false;
        }
        if (!GenalralUtils.isPassword(p1)) {
            showMessageDialog("6-18位数字、字母、符号任意组合");
            return false;
        }
        if (GenalralUtils.isEmpty(p2)) {
            showMessageDialog("请填写确认密码！");
            return false;
        }

        if (!p1.equals(p2)) {
            showMessageDialog("请填写密两次密码不一致,请重新填写码！");
            return false;
        }
        return true;
    }


    private void showMessageDialog(String msg) {
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(mActivity);
        dialog.setMessage(msg);
        dialog.show();
    }

    /**
     * 获取图形验证码
     */
    @OnClick(R.id.img_auth)
    void getAuthImage() {
        mActivity.addTag(TAG1);
        Map<String, String> maps = new HashMap<>();
        maps.put("width", "200");
        maps.put("height", "100");
        mDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.GET_IMAGE_AUTH, TAG1, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mDialog.dismiss();
                try {
                    JSONObject js = new JSONObject(jsonString);
                    if (js != null && js.optString("code").equals("0")) {
                        if (mImageAute != null) {
                            mImageAute.recycle();
                            mImageAute = null;
                        }
                        JSONObject content = js.optJSONObject("content");
                        String str = content.optString("imgStr");
                        mImageAute = Base64Utils.base64ToBitmap(str);
                        imgAuth.setImageBitmap(mImageAute);
                    } else {
                        ToastUtils.shortToast(mActivity, "获取图片验证码失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mDialog.dismiss();
                LogUtils.dazhiLog("获取验证码失败----》" + errorMsg);
            }
        });
    }

    /**
     * 获取短信验证码
     */
    @OnClick(R.id.cd_sms)
    void getAuthMessage() {
        mActivity.addTag(TAG3);
        String img_code = edtAuth.getText().toString().trim();
        if (GenalralUtils.isEmpty(img_code)) {
            ToastUtils.shortToast(mActivity, "请填写图形验证码!");
            return;
        }
        Map<String, String> maps = new HashMap<>();
        maps.put("imgCode", img_code);
        maps.put("mobilephone", ((RegisterActivity) mActivity).getMobilePhone());
        mDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.GET_SMS_AUTH, TAG3, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mDialog.dismiss();
                try {
                    JSONObject js = new JSONObject(jsonString);
                    int code = js.optInt("code");
                    if (code == 0) {
                        tvSms.startCountdown();
                        ToastUtils.shortToast(mActivity, "短信验证码发送成功!");
                    } else {
                        ToastUtils.shortToast(mActivity, js.optString("msg"));
                        edtAuth.setText("");
                        getAuthImage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mDialog.dismiss();
                edtAuth.setText("");
                getAuthImage();
                ToastUtils.shortToast(mActivity, "获取短信验证码失败");
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (mImageAute != null) {
            mImageAute.recycle();
            mImageAute = null;
        }
    }
}
