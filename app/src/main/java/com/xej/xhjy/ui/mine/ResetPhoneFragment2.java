package com.xej.xhjy.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.countdown.CountdownView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.tools.NormalUtils;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.LoginActivity;
import com.xej.xhjy.ui.login.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author dazhi
 * @class RegisterFragment1 修改手机号第一个页面
 * @Createtime 2018/6/21 16:43
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class ResetPhoneFragment2 extends BaseFragment {
    private final String TAG1 = "img_token1", TAG2 = "sms_token1", TAG3 = "check_pone1";
    Unbinder mUnbinder;
    @BindView(R.id.edt_new_phone)
    EditText edtNewPhone;
    @BindView(R.id.edt_auth)
    EditText edtAuth;
    @BindView(R.id.img_auth)
    ImageView imgAuth;
    @BindView(R.id.edt_sms)
    EditText edtSms;
    @BindView(R.id.tv_sms)
    CountdownView tvSms;
    private Bitmap mImageAute;
    private ClubLoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reset_phone2, null);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void initView() {
        mLoadingDialog = new ClubLoadingDialog(mActivity);
        getAuthImage();
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
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.GET_IMAGE_AUTH, TAG1, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                try {
                    JSONObject js = new JSONObject(jsonString);
                    if (js.optString("code").equals("0")) {
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
                mLoadingDialog.dismiss();
            }
        });
    }

    /**
     * 获取短信验证码
     */
    @OnClick(R.id.tv_sms)
    void getAuthMessage() {
        String img_code = edtAuth.getText().toString().trim();
        if (TextUtils.isEmpty(img_code)) {
            ToastUtils.shortToast(mActivity, "请填写图形验证码!");
            return;
        }
        String phone = edtNewPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.shortToast(mActivity, "请填写手机号!");
            return;
        }
        mActivity.addTag(TAG2);
        Map<String, String> maps = new HashMap<>();
        maps.put("imgCode", img_code);
        maps.put("mobilephone", phone);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.GET_SMS_AUTH, TAG2, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
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
                mLoadingDialog.dismiss();
                edtAuth.setText("");
                getAuthImage();
                ToastUtils.shortToast(mActivity, "获取短信验证码失败");
            }
        });
    }

    private boolean checkData() {
        String phone = edtNewPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.shortToast(mActivity, "请填写手机号！");
            return false;
        }
        if (!GenalralUtils.checkMobilePhome(phone)) {
            ToastUtils.shortToast(mActivity, "手机号码格式不正确！");
            return false;
        }
        if (TextUtils.isEmpty(edtAuth.getText().toString().trim())) {
            ToastUtils.shortToast(mActivity, "请填图形验证码！");
            return false;
        }
        return true;
    }

    /**
     * 检查手机号是否被注册
     */
    @OnClick(R.id.btn_step2_next)
    void checkPhone() {
        if (!checkData()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("mobilephone", edtNewPhone.getText().toString().trim());
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.CHECK_MOBILE, TAG1, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("检查手机号---》" + jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (GenalralUtils.isEmpty(jsonObject.optString("content"))) {
                        postNewPassword();
                    } else {
                        edtNewPhone.setText("");
                        ToastUtils.shortToast(mActivity, "新手机号已经被注册!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
                ((RegisterActivity) mActivity).switchToFragment(1);
                LogUtils.dazhiLog("检查手机号错误---》" + errorMsg);
            }
        });
    }


    /**
     * 修改新的手机号
     */
    private void postNewPassword() {
        Map<String, String> maps = new HashMap<>();
        maps.put("mobilephone", edtNewPhone.getText().toString().trim());
        maps.put("imgCode", edtAuth.getText().toString().trim());
        maps.put("mToken", edtSms.getText().toString().trim());
        mActivity.addTag(TAG3);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.RESET_PHONE_NEW, TAG3, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("验证手机号---》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        String newphone = edtNewPhone.getText().toString().trim();
                        //修改手机号号码，存新号码
                        PerferenceUtils.put(AppConstants.User.PHONE, newphone);
                        PerferenceUtils.put(AppConstants.User.HIDDEN_PHONE, NormalUtils.mobileEncrypt(newphone));
                        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(mActivity);
                        dialog.setMessage("手机号已修改成功！");
                        dialog.setPositiveListener("确定", new PositiveListener() {
                            @Override
                            public void onPositiveClick() {
                                mActivity.finishWithAnim();
                                LoginUtils.clearLoginInfo();
                                mActivity.startActivityWithAnim(new Intent(mActivity, LoginActivity.class));
                            }
                        });
                        dialog.show();
                    } else {
                        ToastUtils.shortToast(mActivity, json.optString("msg"));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mImageAute != null) {
            mImageAute.recycle();
            mImageAute = null;
        }
        mUnbinder.unbind();
    }
}
