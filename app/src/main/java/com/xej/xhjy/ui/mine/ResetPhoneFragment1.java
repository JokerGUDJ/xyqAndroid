package com.xej.xhjy.ui.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseFragment;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.countdown.CountdownView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;

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
public class ResetPhoneFragment1 extends BaseFragment {
    private final String TAG1 = "img_token", TAG2 = "sms_token", TAG3 = "check_pone";
    Unbinder mUnbinder;
    @BindView(R.id.tv_old_phone)
    TextView edtOldPhone;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_reset_phone1, null);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void initView() {
        mLoadingDialog = new ClubLoadingDialog(mActivity);
        edtOldPhone.setText(PerferenceUtils.get(AppConstants.User.HIDDEN_PHONE, ""));
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
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.GET_IMAGE_AUTH, TAG1, maps, new HttpCallBack() {
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
        String phone =PerferenceUtils.get(AppConstants.User.PHONE, "");
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.shortToast(mActivity, "请填写手机号!");
            return;
        }
        mActivity.addTag(TAG2);
        Map<String, String> maps = new HashMap<>();
        maps.put("imgCode", img_code);
        maps.put("mobilephone", phone);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.GET_SMS_AUTH, TAG2, maps, new HttpCallBack() {
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
                LogUtils.dazhiLog("获取验证码失败----》" + errorMsg);
            }
        });
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(edtAuth.getText().toString().trim())) {
            ToastUtils.shortToast(mActivity, "请填图形验证码！");
            return false;
        }
        if (TextUtils.isEmpty(edtSms.getText().toString().trim())) {
            ToastUtils.shortToast(mActivity, "请填写短信验证码！");
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_step1_next)
    void postNewPassword() {
        if (!checkData()) {
            return;
        }
        Map<String, String> maps = new HashMap<>();
        maps.put("mobilephone", PerferenceUtils.get(AppConstants.User.PHONE, ""));
        maps.put("imgCode", edtAuth.getText().toString().trim());
        maps.put("mToken", edtSms.getText().toString().trim());
        mActivity.addTag(TAG3);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.RESET_PHONE_PRE, TAG3, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("验证手机号---》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        ((ResetPhoneActivity) mActivity).switchToFragment(1);
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
        if (mImageAute != null){
            mImageAute.recycle();
            mImageAute = null;
        }
        mUnbinder.unbind();
    }
}
