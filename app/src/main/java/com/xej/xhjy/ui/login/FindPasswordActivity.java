package com.xej.xhjy.ui.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.countdown.CountdownView;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.tools.PassGuardUtils;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;

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
 * @class RegisterActivity 找回密码的activity
 * @Createtime 2018/6/21 16:12
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class FindPasswordActivity extends BaseActivity {
    private final String TAG1 = "find_img_auth", TAG2 = "find_reset_password", TAG3 = "find_sms_auth";
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_auth)
    EditText edtAuth;
    @BindView(R.id.img_auth)
    ImageView imgAuth;
    @BindView(R.id.edt_sms)
    EditText edtSms;
    @BindView(R.id.tv_sms)
    CountdownView tvSms;
    @BindView(R.id.edt_find_password)
    PassGuardEdit findPassword1;
    @BindView(R.id.edt_find_password2)
    PassGuardEdit findPassword2;
    private Bitmap mImageAute;
    private ClubLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        PassGuardUtils.initialize(findPassword1);
        PassGuardUtils.initialize(findPassword2);
        mLoadingDialog = new ClubLoadingDialog(this);
        getAuthImage();
    }

    /**
     * 获取图形验证码
     */
    @OnClick(R.id.img_auth)
    void getAuthImage() {
        addTag(TAG1);
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
                LogUtils.dazhiLog("获取验证码失败----》" + errorMsg);
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
        String phone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.shortToast(mActivity, "请填写手机号!");
            return;
        }
        addTag(TAG2);
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
                LogUtils.dazhiLog("获取验证码失败----》" + errorMsg);
            }
        });
    }

    private boolean checkData() {
        String phone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showMessageDialog("请填写手机号！", null);
            return false;
        }
        if (!GenalralUtils.checkMobilePhome(phone)) {
            showMessageDialog("手机号码格式不正确！", null);
            return false;
        }

        if (GenalralUtils.isEmpty(edtAuth.getText().toString().trim())) {
            showMessageDialog("请填写图形验证码！", null);
            return false;
        }
        if (GenalralUtils.isEmpty(edtSms.getText().toString().trim())) {
            showMessageDialog("请填写短信验证码！", null);
            return false;
        }
        String p1 = findPassword1.getText().toString().trim();
        String p2 = findPassword2.getText().toString().trim();
        if (GenalralUtils.isEmpty(p1)) {
            showMessageDialog("请填写密码！", null);
            return false;
        }
        if (!GenalralUtils.isPassword(p1)) {
            showMessageDialog("6-18位数字、字母、符号任意组合", null);
            return false;
        }
        if (GenalralUtils.isEmpty(p2)) {
            showMessageDialog("请填写确认密码！", null);
            return false;
        }

        if (!p1.equals(p2)) {
            showMessageDialog("请填写密两次密码不一致,请重新填写码！", null);
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_save)
    void getTimeMills() {
        if (!checkData()) {
            return;
        }
        String TAG = "getTimeMillsFindPwd";
        mActivity.addTag(TAG);
        mLoadingDialog.show();
        Map<String, String> maps = new HashMap<>();
//        maps.put("mobile",  edtPhone.getText().toString().trim());
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.PWD_TIME_MILLIS, TAG,maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONObject content = json.getJSONObject("content");
//                        currTime = content.optString("currTime");
                        String sKey = content.optString("sKey");
                        String affineX = content.optString("affineX");
                        String affineY = content.optString("affineY");

                        LogUtils.dazhiLog("sKey=" + sKey);

                        if (!GenalralUtils.isEmpty(sKey)) {
                            // 返回时间戳放入密码控件生成密文
                            findPassword1.setCipherKey(sKey);
                            findPassword2.setCipherKey(sKey);
                            findPassword1.setEccKey(affineX + "|" + affineY);
                            findPassword2.setEccKey(affineX + "|" + affineY);
                            postNewPassword();
                        } else {
                            ToastUtils.shortToast(mActivity, "登录失败！");
                            mLoadingDialog.dismiss();
                        }
                    } else {
                        ToastUtils.shortToast(mActivity, "登录失败！");
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

    /**
     * 重置密码
     */
    void postNewPassword() {
        Map<String, String> maps = new HashMap<>();
        maps.put("mobilephone", edtPhone.getText().toString().trim());
        maps.put("password", findPassword1.getSM2SM4Ciphertext());
        maps.put("confPassword", findPassword2.getSM2SM4Ciphertext());
        maps.put("imgCode", edtAuth.getText().toString().trim());
        maps.put("mToken", edtSms.getText().toString().trim());
        LogUtils.dazhiLog("password="+findPassword1.getSM2SM4Ciphertext()+"        confPassword="+ findPassword2.getSM2SM4Ciphertext());
        addTag(TAG3);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.RESET_PASSWORD, TAG3, maps, new HttpCallBack() {
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
//                                Intent intent = new Intent(FindPasswordActivity.this, LoginActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);

                            }
                        });
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

    private void showMessageDialog(String mess, PositiveListener listener) {
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(this);
        dialog.setMessage(mess);
        if (listener != null) {
            dialog.setPositiveListener("确定", listener);
        }
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (mImageAute != null) {
            mImageAute.recycle();
            mImageAute = null;
        }
        super.onDestroy();
    }
}
