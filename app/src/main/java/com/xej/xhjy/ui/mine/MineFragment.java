package com.xej.xhjy.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.api.NimUIKit;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseFragmentForViewpager;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.LoginActivity;
import com.xej.xhjy.ui.login.LoginCallBack;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginFailedEvent;
import com.xej.xhjy.ui.login.LoginOutEvent;
import com.xej.xhjy.ui.login.cropper.LoginCheckUtils;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.main.TrainMeetEvent;
import com.xej.xhjy.ui.metting.MyMeetingsActivity;
import com.xej.xhjy.ui.society.AttentionActivity;
import com.xej.xhjy.ui.view.TitleView;
import com.xej.xhjy.ui.web.LiveingWebview;
import com.xej.xhjy.ui.web.WebPagerActivity;
import com.yanzhenjie.album.Album;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dazhi
 * @class MineFragment
 * @Createtime 2018/5/28 11:28
 * @description 首页我的页面
 * @Revisetime
 * @Modifier
 */
public class MineFragment extends BaseFragmentForViewpager implements View.OnClickListener {
    private View mMyMeetting, mMettingEnter, mAttention, mSafe, mHeadArea, mArrow, mAboutus, mMeettingAndTran, mLogoutAccout;
    /**
     * 头像
     */
    private ImageView mHeadImg;
    /**
     * 姓名，公司名，是否认证
     */
    private TextView mName, mComplany, noLogin, tv_logout, tv_points;

    private LoginCallBack mCallBack;
    private final int REQUEST_IMG_CODE = 100;
    private final int REQUEST_CROP_CODE = 101;
    private String IMAGE_FILE_LOCATION;
    private TitleView mTitleView;

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_mine, null);
        mMeettingAndTran = mRootView.findViewById(R.id.ll_my_meeting_and_training_registration);
        mMyMeetting = mRootView.findViewById(R.id.ll_my_meet);
        mMettingEnter = mRootView.findViewById(R.id.ll_my_enter);
        mAttention = mRootView.findViewById(R.id.ll_my_attent);
        mAboutus = mRootView.findViewById(R.id.ll_my_aboutus);
        mSafe = mRootView.findViewById(R.id.ll_my_safe);
        mHeadArea = mRootView.findViewById(R.id.ll_user_area);
        mMyMeetting.setOnClickListener(this);
        mMettingEnter.setOnClickListener(this);
        mAttention.setOnClickListener(this);
        mSafe.setOnClickListener(this);
        mAboutus.setOnClickListener(this);
        mHeadArea.setOnClickListener(this);
        mMeettingAndTran.setOnClickListener(this);
        mArrow = mRootView.findViewById(R.id.arrow);
        mArrow.setOnClickListener(this);
        mHeadImg = mRootView.findViewById(R.id.img_head);
        mHeadImg.setOnClickListener(this);
        mName = mRootView.findViewById(R.id.tv_username);
        mComplany = mRootView.findViewById(R.id.tv_complany_name);
        noLogin = mRootView.findViewById(R.id.tv_no_login);
        tv_logout = mRootView.findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(this);
        mTitleView = mRootView.findViewById(R.id.mine_titleview);
        tv_points = mRootView.findViewById(R.id.tv_points);
        tv_points.setOnClickListener(this);
        mLogoutAccout = mRootView.findViewById(R.id.ll_logout_account);
        mLogoutAccout.setOnClickListener(this);
        setUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!AppConstants.IS_LOGIN) {
            tv_points.setText("0积分");
            tv_logout.setVisibility(View.GONE);
        }else{
            getPoints();
            tv_logout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化要加载的数据
     */
    @Override
    public void initDatas() {

    }

    private void setUserInfo() {
        if (!AppConstants.IS_LOGIN) {
            mHeadImg.setImageResource(R.drawable.ic_user_default_icon);
            mName.setVisibility(View.GONE);
            mComplany.setVisibility(View.GONE);
            mName.setCompoundDrawables(null,null,null,null);
            noLogin.setVisibility(View.VISIBLE);
            mTitleView.setZxingVisibile(false);
            mTitleView.setMessageVisibile(false);
            mMettingEnter.setVisibility(View.GONE);
            mAttention.setVisibility(View.GONE);
            mMeettingAndTran.setVisibility(View.GONE);
            mTitleView.setMessageVisibile(false);
            mLogoutAccout.setVisibility(View.GONE);
        } else {
            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_user_default_icon)
                    .placeholder(R.drawable.ic_user_default_icon);
            ImageLoadUtils.showHttpImageCycleNoCache(mActivity, LoginUtils.getHeadImagUrl(), mHeadImg, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
            mTitleView.setZxingVisibile(true);
            mTitleView.setMessageVisibile(true);
            mName.setVisibility(View.VISIBLE);
            noLogin.setVisibility(View.GONE);
            mAttention.setVisibility(View.VISIBLE);
            mName.setText(PerferenceUtils.get(AppConstants.User.NAME, ""));
            Drawable drawable = LoginUtils.getUserCerInfo(getContext());
            if(drawable != null){
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), (int) (drawable.getMinimumHeight()));
            }
            mName.setCompoundDrawables(null,null, drawable, null);
            mComplany.setVisibility(View.VISIBLE);
            mComplany.setText(PerferenceUtils.get(AppConstants.User.COMPLANY, ""));
            String auth = PerferenceUtils.get(AppConstants.DATA_TRAINING_ID_KEY, "");
            LogUtils.dazhiLog("培训会议show---->" + auth);
            if (!TextUtils.isEmpty(auth) && "yes".equals(auth)) {
                mMeettingAndTran.setVisibility(View.VISIBLE);
            } else {
                mMeettingAndTran.setVisibility(View.GONE);
            }
            if (LoginUtils.isOrgUser()) {
                mMettingEnter.setVisibility(View.VISIBLE);
            }
            mLogoutAccout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取积分
     */
    private void getPoints(){
        String TAG = "getPoints";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.MY_POINTS, TAG, new HashMap<String, String>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject body = json.getJSONObject("content");
                                    if(body != null && !body.isNull("usableAmount")){
                                        tv_points.setText((int)(body.optDouble("usableAmount"))+"积分");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        ToastUtils.shortToast(getContext(), json.optString("msg"));
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
     * 收到头像修改成功的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeadEventMainThread(HeadEditEvent event) {
       ImageLoadUtils.showHttpImageCycleNoCache(mActivity, LoginUtils.getHeadImagUrl(), mHeadImg, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
    }

    /**
     * 收到登录成功的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEventMainThread(LoginEvent event) {
        setUserInfo();
        if (mCallBack != null && mCallBack.canUse) {
            if (mCallBack.isPass) {
                if ("N".equals(AppConstants.USER_STATE)) {//必须认证
                    if (mCallBack.isOrg) {
                        if (LoginUtils.isOrgUser()) {
                            mCallBack.loginAfterRun();
                        } else {
                            LoginUtils.showOrgMessage(mActivity);
                        }
                    } else {
                        mCallBack.loginAfterRun();
                    }
                } else {
                    LoginUtils.showCerMessage(mActivity);
                }
            } else {
                mCallBack.loginAfterRun();
            }
        }
        mCallBack = null;
    }

    /**
     * 收到登录失败的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginFailedEventMainThread(LoginFailedEvent event) {
        mCallBack = null;
    }

    /**
     * 收到登出的消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginOutEventMainThread(LoginOutEvent event) {
        setUserInfo();
        tv_points.setText("0积分");
        tv_logout.setVisibility(View.GONE);
    }

    /**
     * 收到年度会议以及培训会议消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrainMeetEventMainThread(TrainMeetEvent event) {
        LogUtils.dazhiLog("收到培训报名消息------>" + event.getMessage());
        if (event.getMessage()) {
            mMeettingAndTran.setVisibility(View.VISIBLE);
        } else {
            mMeettingAndTran.setVisibility(View.GONE);
        }
    }

    private long mLasttime = 0;

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
            return;
        mLasttime = System.currentTimeMillis();
        if (v == mMyMeetting) {
            mCallBack = new LoginCallBack() {
                @Override
                public void loginAfterRun() {
                    Intent intent = new Intent(mActivity, MyMeetingsActivity.class);
                    mActivity.startActivity(intent);
                }
            };
            mCallBack.isPass = true;
            LoginUtils.startCheckLogin(mActivity, mCallBack);
        } else if (v == mMettingEnter) {
            mCallBack = new LoginCallBack() {
                @Override
                public void loginAfterRun() {
                    Intent intent = new Intent(mActivity, WebPagerActivity.class);
                    intent.putExtra(WebPagerActivity.LOAD_URL, "MeetSignUpList");
                    startActivity(intent);
                }
            };
            mCallBack.isPass = true;
            mCallBack.isOrg = true;
            LoginUtils.startCheckLogin(mActivity, mCallBack);
        } else if (v == mAttention) {
            mCallBack = new LoginCallBack() {
                @Override
                public void loginAfterRun() {
                    Intent intent = new Intent(mActivity, AttentionActivity.class);
                    startActivity(intent);
                }
            };
            mCallBack.isPass = true;
            LoginUtils.startCheckLogin(mActivity, mCallBack);
        } else if (v == mHeadArea || v == mArrow) {
            if (AppConstants.IS_LOGIN) {
                mActivity.startActivityWithAnim(new Intent(mActivity, MineDetailActivity.class));
            } else {
                mActivity.startActivityWithAnim(new Intent(mActivity, LoginActivity.class));
            }
        } else if (v == mSafe) {
            mCallBack = new LoginCallBack() {
                @Override
                public void loginAfterRun() {
                    mActivity.startActivityWithAnim(new Intent(mActivity, MineSafeActivity.class));
                }
            };
            LoginUtils.startCheckLogin(mActivity, mCallBack);
        } else if (v == mHeadImg) {
            if (AppConstants.IS_LOGIN) {
                Album.album(this)
                        .requestCode(REQUEST_IMG_CODE) // 请求码，返回时onActivityResult()的第一个参数。
                        .toolBarColor(ContextCompat.getColor(mActivity, R.color.red)) // Toolbar 颜色
                        .statusBarColor(ContextCompat.getColor(mActivity, R.color.red)) // StatusBar 颜色。
                        .navigationBarColor(ContextCompat.getColor(mActivity, R.color.red)) // NavigationBar 颜色
                        .selectCount(1) // 最多选择几张图片。
                        .columnCount(3) // 相册展示列数，默认是2列。
                        .camera(true) // 是否有拍照功能。
                        .start();
            } else {
                mActivity.startActivityWithAnim(new Intent(mActivity, LoginActivity.class));
            }
        } else if (v == mAboutus) {
            mActivity.startActivityWithAnim(new Intent(mActivity, AboutUsActivity.class));
        } else if (v == mMeettingAndTran) {
            mCallBack = new LoginCallBack() {
                @Override
                public void loginAfterRun() {
                    Intent intent = new Intent(mActivity, WebPagerActivity.class);
                    intent.putExtra(WebPagerActivity.LOAD_URL, "SignList");
                    startActivity(intent);
                }
            };
            mCallBack.isPass = true;
            mCallBack.isOrg = true;
            LoginUtils.startCheckLogin(mActivity, mCallBack);
        }else if(v == tv_logout){
            loginOut();
        }else if(v == tv_points){
            LoginCheckUtils.isCertification = true;
            LoginCheckUtils.setIlogin(new LoginCheckUtils.ILogin() {
                @Override
                public void onlogin() {
                    gotoPoint();
                }
            }, mActivity);

        }else if(v == mLogoutAccout){
            ClubDialog dialog = new ClubDialog(getContext());
            dialog.setTitle("温馨提示");
            dialog.setMessage("确定注销账号？");
            dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
            dialog.setPositiveListener("确定", new PositiveListener() {
                @Override
                public void onPositiveClick() {
                    logoutAccount();

                }
            });
            dialog.setNegativeListener("取消", new NegativeListener() {
                @Override
                public void onNegativeClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /**
     * 账户注销
     */
    private void logoutAccount(){
        String TAG = "login_account";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.LOGOUT_ACCOUNT, TAG, new HashMap<String, String>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        EventBus.getDefault().post(new HasMessageEvent(false));
                        LoginUtils.clearLoginInfo();
                        //退出聊天登录
                        NimUIKit.logout();
                        //SessionHelper.cleanSession();
                        //退出时退出云信状态
                    } else {
                        ToastUtils.shortToast(getContext(), json.optString("msg"));
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
     * 跳转积分权益系统
     */
    private void gotoPoint(){
        Intent intent = new Intent(mActivity, LiveingWebview.class);
        intent.putExtra(LiveingWebview.LOAD_URL,NetConstants.POINT_BASE_URL+"?g_token="+PerferenceUtils.get("g_token", "")+"&wljySessionId="+
                PerferenceUtils.get("wljySessionId", "")+"&orgId="+PerferenceUtils.get(AppConstants.User.ORGID, ""));
        intent.putExtra("closeBtn", true);
        mActivity.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) { // 判断是否成功。
            if (requestCode == REQUEST_IMG_CODE) {
                List<String> pathList = Album.parseResult(data);
                startPhotoZoom(GenalralUtils.getImageContentUri(mActivity, pathList.get(0)));
            } else if (requestCode == REQUEST_CROP_CODE) {
                File file = new File(IMAGE_FILE_LOCATION);
                if (file.exists()) {
                    ClubDialog dialog = new ClubDialog(mActivity);
                    dialog.setMessage("是否要修改您的头像？");
                    dialog.setPositiveListener("确定", new PositiveListener() {
                        @Override
                        public void onPositiveClick() {
                            uploadHeadImg();
                        }
                    });
                    dialog.show();
                } else {
                    ToastUtils.shortToast(mActivity, "头像上传失败！");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void loginOut() {
        String TAG = "login_out";
        mActivity.addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.LOGIN_OUT, TAG, new HashMap<String, String>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("登出-----》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        EventBus.getDefault().post(new HasMessageEvent(false));
                        LoginUtils.clearLoginInfo();
                        //退出聊天登录
                        NimUIKit.logout();
                        //SessionHelper.cleanSession();
                        //退出时退出云信状态
                    } else {
                        ToastUtils.shortToast(getContext(), json.optString("msg"));
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
     * 上传头像
     */
    private void uploadHeadImg() {
        String TAG2 = "update_head_img";
        mActivity.addTag(TAG2);
        //使用用户ID作为头像名字
        final String filename = PerferenceUtils.get(AppConstants.User.ID, "") + ".jpg";
        Map<String, String> maps = new HashMap<>();
        maps.put("imgName", filename);
        maps.put("imgBody", Base64Utils.fileToBase64(IMAGE_FILE_LOCATION));
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.HEAD_IMG_UPLOAD, TAG2, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_FILE_LOCATION);
                mHeadImg.setImageBitmap(bitmap);
                LogUtils.dazhiLog("头像地址=======" + NetConstants.HEAD_IMAG_URL + filename);
                EventBus.getDefault().post(new HeadEditEvent("success"));
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(mActivity, "头像上传失败！");
            }
        });
    }

    /**
     * 图片裁剪
     */
    public void startPhotoZoom(Uri uri) {
        IMAGE_FILE_LOCATION = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xhjy_head.jpg";
        File temp = new File(IMAGE_FILE_LOCATION);
        Uri mPhotoUri = Uri.fromFile(temp);//获取文件的Uri
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);//定义输出的File Uri，之后根据这个Uri去拿裁剪好的图片信息  ————代码B
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP_CODE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasNewMessage(HasMessageEvent event) {
        LogUtils.dazhiLog("mine收到是否有消息----------" + event.getHasMessage());
        mTitleView.setNewMessageVisibile(event.getHasMessage());
    }

    /**
     * 选择专委会
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBranchOf(checkBranchOfEvent event) {
        //setUserInfo();
        LogUtils.dazhiLog("mine收到添加专委会消息----------" + event.getMessage());
        ClubDialog dialog = new ClubDialog(getContext());
        dialog.setTitle("提示");
        dialog.setMessage("请完善所属专委会信息，获取更多专委会服务");
        dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
        dialog.setPositiveListener("去完善", new PositiveListener() {
            @Override
            public void onPositiveClick() {
                startActivity(new Intent(getContext(), MineDetailEditActivity.class));

            }
        });
        dialog.setNegativeListener("不再提醒", new NegativeListener() {
            @Override
            public void onNegativeClick() {
                setIgnoreBranchOf();
            }
        });
        dialog.show();

    }

    /**
     * 忽略修改专委会
     */

    private void setIgnoreBranchOf() {
        String TAG2 = "set_ignore";
        mActivity.addTag(TAG2);
        Map<String, String> maps = new HashMap<>();
        String id = PerferenceUtils.get(AppConstants.User.ID, "");
        maps.put("id", id);
        String url = NetConstants.SET_IGNORE;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG2, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("忽略===" + jsonString);


            }

            @Override
            public void onError(String errorMsg) {
                LogUtils.dazhiLog("忽略===" + errorMsg);
            }
        });

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
