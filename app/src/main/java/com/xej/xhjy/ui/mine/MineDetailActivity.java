package com.xej.xhjy.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
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
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.main.HasMessageEvent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dazhi
 * @class MineDetailActivity
 * @Createtime 2018/6/28 16:21
 * @description 个人信息详情页
 * @Revisetime
 * @Modifier
 */
public class MineDetailActivity extends BaseActivity {
    private final int edit_code = 21;
    @BindView(R.id.img_back)
    ImageView img_bak;
    @BindView(R.id.tv_user_company)
    TextView tvUserCompany;
    @BindView(R.id.tv_user_department)
    TextView tvUserDepartment;
    @BindView(R.id.tv_user_gender)
    TextView tvUserGender;
    @BindView(R.id.tv_user_job)
    TextView tvUserJob;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_user_tel)
    TextView tvUserTel;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.img_usericon)
    ImageView imgUsericon;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_user_branch)
    TextView tv_user_branch; 
    private ClubLoadingDialog mLoadingDialog;
    private final int REQUEST_IMG_CODE = 100;
    private final int REQUEST_CROP_CODE = 101;
    private String IMAGE_FILE_LOCATION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        mLoadingDialog = new ClubLoadingDialog(this);
        tvUsername.setText(PerferenceUtils.get(AppConstants.User.NAME, ""));
        tvUserCompany.setText(PerferenceUtils.get(AppConstants.User.COMPLANY, ""));
        tvUserDepartment.setText(PerferenceUtils.get(AppConstants.User.DEPARTMENT, "").replace("\n", ""));
        tvUserJob.setText(PerferenceUtils.get(AppConstants.User.JOB, ""));
        tvUserPhone.setText(PerferenceUtils.get(AppConstants.User.HIDDEN_PHONE, ""));
        tvUserTel.setText(PerferenceUtils.get(AppConstants.User.TEL, ""));
        tvUserEmail.setText(PerferenceUtils.get(AppConstants.User.EMAIL, ""));
        tvUserAddress.setText(PerferenceUtils.get(AppConstants.User.ADDRESS, ""));
        tv_user_branch.setText(PerferenceUtils.get(AppConstants.User.BRANCH_OF_COMMIT_NAME,""));
        String gender = PerferenceUtils.get(AppConstants.User.GENDER,"");
        if ("MAN".equals(gender)){
            tvUserGender.setText("男");
        } else if ("WOMAN".equals(gender)){
            tvUserGender.setText("女");
        }
        ImageLoadUtils.showHttpImageCycleNoCache(mActivity, LoginUtils.getHeadImagUrl(), imgUsericon, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
        EventBus.getDefault().register(this);
    }

    /**
     * 收到头像修改成功的消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeadEventMainThread(HeadEditEvent event) {
        ImageLoadUtils.showHttpImageCycleNoCache(mActivity, LoginUtils.getHeadImagUrl(), imgUsericon, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
    }

    /**
     * 更新专委会
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeadEventMainThread(BranchOfEditEvent event) {
        tv_user_branch.setText(PerferenceUtils.get(AppConstants.User.BRANCH_OF_COMMIT_NAME,""));
    }
    /**
     * 登出
     */
    void loginOut() {
        String TAG = "login_out";
        addTag(TAG);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.LOGIN_OUT, TAG, new HashMap<String, String>(), new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("登出-----》" + jsonString);
                mLoadingDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        EventBus.getDefault().post(new HasMessageEvent(false));
                        LoginUtils.clearLoginInfo();
                        //退出聊天登录
                        NimUIKit.logout();
                        //SessionHelper.cleanSession();
                        //退出时退出云信状态
                        finishWithAnim();
                    } else {
                        ToastUtils.shortToast(MineDetailActivity.this, json.optString("msg"));
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

    @OnClick(R.id.ll_goto_detail)
    void editDetail() {
        if ("N".equals(AppConstants.USER_STATE)) {//必须认证
            startActivityForResultWithAnim(new Intent(this, MineDetailEditActivity.class), edit_code);
        } else {
            LoginUtils.showCerMessage(mActivity);
        }
    }
    //@OnClick(R.id.img_usericon)
    void editHeadImg() {
        Album.album(mActivity)
                .requestCode(REQUEST_IMG_CODE) // 请求码，返回时onActivityResult()的第一个参数。
                .toolBarColor(ContextCompat.getColor(this, R.color.red)) // Toolbar 颜色
                .statusBarColor(ContextCompat.getColor(this, R.color.red)) // StatusBar 颜色。
                .navigationBarColor(ContextCompat.getColor(this, R.color.red)) // NavigationBar 颜色
                .selectCount(1) // 最多选择几张图片。
                .columnCount(3) // 相册展示列数，默认是2列。
                .camera(true) // 是否有拍照功能。
                .start();
    }

    @OnClick(R.id.img_back)
    void goBack(){
        finishWithAnim();
    }

    /**
     * 上传头像
     */
    private void uploadHeadImg() {
        String TAG2 = "update_head_img";
        addTag(TAG2);
        //使用用户ID作为头像名字
        final String filename = PerferenceUtils.get(AppConstants.User.ID, "") + ".jpg";
        Map<String, String> maps = new HashMap<>();
        maps.put("imgName", filename);
        maps.put("imgBody", Base64Utils.fileToBase64(IMAGE_FILE_LOCATION));
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity,NetConstants.HEAD_IMG_UPLOAD, TAG2, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("头像上传成功----》"+jsonString);
                ImageLoadUtils.showHttpImageCycleNoCache(mActivity, NetConstants.HEAD_IMAG_URL + filename, imgUsericon, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
                EventBus.getDefault().post(new HeadEditEvent("success"));
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(mActivity, "头像上传失败！");
                mLoadingDialog.dismiss();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == edit_code){
                tvUserJob.setText(PerferenceUtils.get(AppConstants.User.JOB, ""));
                tvUserTel.setText(PerferenceUtils.get(AppConstants.User.TEL, ""));
                tvUserEmail.setText(PerferenceUtils.get(AppConstants.User.EMAIL, ""));
                tvUserAddress.setText(PerferenceUtils.get(AppConstants.User.ADDRESS, ""));
                tvUserDepartment.setText(PerferenceUtils.get(AppConstants.User.DEPARTMENT, ""));
                String gender = PerferenceUtils.get(AppConstants.User.GENDER,"");
                if ("MAN".equals(gender)){
                    tvUserGender.setText("男");
                } else if ("WOMAN".equals(gender)){
                    tvUserGender.setText("女");
                }
            } else if (requestCode == REQUEST_IMG_CODE) {
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
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
