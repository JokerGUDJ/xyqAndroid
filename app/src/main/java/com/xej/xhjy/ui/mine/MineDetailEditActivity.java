package com.xej.xhjy.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.bean.JobBean;
import com.xej.xhjy.bean.UserBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.common.view.Dialog.ActionSheetDialog;
import com.xej.xhjy.common.view.spinner.NiceSpinner;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.image.ImageLoadUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.yanzhenjie.album.Album;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.core.content.FileProvider.getUriForFile;

/**
 * @author dazhi
 * @class MineDetailActivity
 * @Createtime 2018/6/28 16:21
 * @description 个人信息详情页
 * @Revisetime
 * @Modifier
 */
public class MineDetailEditActivity extends BaseActivity {

    @BindView(R.id.tv_user_company)
    TextView tvUserCompany;
    @BindView(R.id.edt_user_department)
    EditText edtUserDepartment;
    @BindView(R.id.spinner_gender)
    NiceSpinner spinnerGender;
    @BindView(R.id.spinner_job)
    NiceSpinner spinnerJob;
    @BindView(R.id.spinner_branch_of)
    NiceSpinner spinnerBranchOf;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.edt_tel)
    EditText edtTel;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.img_usericon)
    ImageView imgUsericon;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.btn_edit)
    TextView btnEdit;

    private final int REQUEST_IMG_CODE = 100;
    private final int REQUEST_CROP_CODE = 101;
    private final int REQUEST_CAMERA_CODE = 102;
    String jobName, jobID;
    private List<JobBean.ContentBean> jobList;
    private int jobPosition = 0;
    private ClubLoadingDialog mLoadingDialog;
    private String IMAGE_FILE_LOCATION;
    private int genderPosition = 0;
    private int branchPos = 0;
    private File photoFile, cropFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_edit);
        ButterKnife.bind(this);
        initView();
        getJobList();
    }

    private void initView() {
        mLoadingDialog = new ClubLoadingDialog(this);
        tvUsername.setText(PerferenceUtils.get(AppConstants.User.NAME, ""));
        tvUserCompany.setText(PerferenceUtils.get(AppConstants.User.COMPLANY, ""));
        edtUserDepartment.setText(PerferenceUtils.get(AppConstants.User.DEPARTMENT, "").replace("\n", ""));
        edtUserDepartment.setSelection(edtUserDepartment.getText().length());
        tvUserPhone.setText(PerferenceUtils.get(AppConstants.User.HIDDEN_PHONE, ""));
        edtTel.setText(PerferenceUtils.get(AppConstants.User.TEL, ""));
        edtTel.setSelection(edtTel.getText().length());
        edtAddress.setText(PerferenceUtils.get(AppConstants.User.ADDRESS, ""));
        edtAddress.setSelection(edtAddress.getText().length());
        edtEmail.setText(PerferenceUtils.get(AppConstants.User.EMAIL, ""));
        edtEmail.setSelection(edtEmail.getText().length());
        ImageLoadUtils.showHttpImageCycleNoCache(mActivity, LoginUtils.getHeadImagUrl(), imgUsericon, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
        //先从缓存获取职务列表
        String job_list = PerferenceUtils.get(AppConstants.DATA_JOB_LIS_KEY, "");
        if (!TextUtils.isEmpty(job_list)) {
            LogUtils.dazhiLog("缓存职务列表-----" + job_list);
            setJoblist(job_list);
        }
        /*** 设置专委会 */
        setBranchOf();

        String gender = PerferenceUtils.get(AppConstants.User.GENDER, "");
        if ("MAN".equals(gender)) {
            genderPosition = 0;
        } else if ("WOMAN".equals(gender)) {
            genderPosition = 1;
        }
        List<String> dataList = new ArrayList<>();
        dataList.add("男");
        dataList.add("女");
        spinnerGender.attachDataSource(dataList);
        spinnerGender.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                genderPosition = position;
            }
        });
        spinnerGender.setSelectedIndex(genderPosition);
    }

    /**
     * 设置职务列表
     *
     * @param json
     */
    private void setJoblist(String json) {
        String job = PerferenceUtils.get(AppConstants.User.JOB, "");
        JobBean bean = JsonUtils.stringToObject(json, JobBean.class);
        if (bean.getCode().equals("0") && bean.getContent().size() > 0) {
            jobList = new ArrayList<>();
            jobList.addAll(bean.getContent());
            List<String> list = new ArrayList<>();
            for (JobBean.ContentBean item : jobList) {
                list.add(item.getJobName());
                if (job.equals(item.getJobName())) {
                    jobPosition = list.size() - 1;
                    jobName = item.getJobName();
                    jobID = item.getId();
                }
            }
            spinnerJob.attachDataSource(list);
            spinnerJob.addOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    jobName = jobList.get(position).getJobName();
                    jobID = jobList.get(position).getId();
                }
            });
            spinnerJob.setSelectedIndex(jobPosition);
        }
    }

    /**
     * 设置专委会
     */
    private List<UserBean.ContentBean.CommitListBean> branchBean;
    private String branchId,branchName;
    private int pos;
    private void setBranchOf() {
        String TAG = "branch_of_list";
        addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.GET_USER_INFO, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("专委会列表---》" + jsonString);
                String branch= PerferenceUtils.get(AppConstants.User.BRANCH_OF_COMMIT_ID, "");
                UserBean bean = JsonUtils.stringToObject(jsonString, UserBean.class);
                if (bean != null && "0".equals(bean.getCode()) && bean.getContent() != null) {
                    branchBean = new ArrayList<>();
                    branchBean.addAll(bean.getContent().getCommitList());
                    List<String> list = new ArrayList<>();
                    for (UserBean.ContentBean.CommitListBean  item : branchBean) {
                        list.add(item.getName());
                        if (branch.equals(item.getId())) {
                            pos = list.size() - 1;
                            branchName = item.getName();
                            branchId = item.getId();
                        }
                    }

//                    if ("".equals(branch)){
//                        pos = list.size()-1;
//                        branchName = branchBean.get(pos).getName();
//                        branchId = branchBean.get(pos).getId();
//                    }
                    spinnerBranchOf.attachDataSource(list);
                    spinnerBranchOf.addOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            branchName = branchBean.get(position).getName();
                            branchId = branchBean.get(position).getId();
                            PerferenceUtils.put(AppConstants.User.COMMIT_ID, branchId);
                        }
                    });
                    spinnerBranchOf.setSelectedIndex(pos);
                }
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    /**
     * 职位列表
     */
    private void getJobList() {
        String TAG = "JOB_LIST";
        addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.JOB_LIST, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                LogUtils.dazhiLog("职务列表---》" + jsonString);
                setJoblist(jsonString);
                PerferenceUtils.put(AppConstants.DATA_JOB_LIS_KEY, jsonString);
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    private boolean checkData() {
//        if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
//            ToastUtils.longToast(mActivity, "请填写邮箱地址！");
//            return false;
//        }
//        if (TextUtils.isEmpty(edtTel.getText().toString().trim())) {
//            ToastUtils.longToast(mActivity, "请填写固话号码！");
//            return false;
//        }
        if (TextUtils.isEmpty(edtUserDepartment.getText().toString().trim())) {
            ToastUtils.longToast(mActivity, "请填写所属部门！");
            return false;
        }
        return true;
    }

    @OnClick(R.id.img_back)
    void goBack(){
        finishWithAnim();
    }

    @OnClick(R.id.btn_edit)
    void postNewPassword() {
        if (!checkData()) {
            return;
        }
        Map<String, String> maps = new HashMap<>();
        maps.put("jobId", jobID);
        maps.put("userId", AppConstants.USER_ID);
        maps.put("jobName", jobName);
        maps.put("phone", edtTel.getText().toString().trim());
        maps.put("email", edtEmail.getText().toString().trim());
        maps.put("divName", edtUserDepartment.getText().toString().trim());
        maps.put("commitId", branchId);
        String address = edtAddress.getText().toString();
        if (!GenalralUtils.isEmpty(address)) {
            maps.put("addr", address);
        } else {
            maps.put("addr", "");
        }
        if (genderPosition == 0) {
            maps.put("gender", "MAN");
        } else {
            maps.put("gender", "WOMAN");
        }
        String TAG1 = "update_userinfo";
        mActivity.addTag(TAG1);
        mLoadingDialog.show();
        LogUtils.dazhiLog("个人修改map=" + maps);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.UPDATE_USERINFO, TAG1, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("个人修改成功---》" + jsonString);
                try {
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        PerferenceUtils.put(AppConstants.User.TEL, edtTel.getText().toString().trim());
                        PerferenceUtils.put(AppConstants.User.JOB, jobName);
                        PerferenceUtils.put(AppConstants.User.GENDER, genderPosition == 0 ? "MAN" : "WOMAN");
                        PerferenceUtils.put(AppConstants.User.EMAIL, edtEmail.getText().toString().trim());
                        PerferenceUtils.put(AppConstants.User.ADDRESS, edtAddress.getText().toString());
                        PerferenceUtils.put(AppConstants.User.DEPARTMENT, edtUserDepartment.getText().toString().trim());
                        PerferenceUtils.put(AppConstants.User.BRANCH_OF_COMMIT_NAME, branchName);
                        PerferenceUtils.put(AppConstants.User.BRANCH_OF_COMMIT_ID, branchId);
                        EventBus.getDefault().post(new BranchOfEditEvent("更新专委会"));
                        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(mActivity);
                        dialog.setMessage("个人信息修改成功！");
                        dialog.setPositiveListener("确定", new PositiveListener() {
                            @Override
                            public void onPositiveClick() {
                                setResult(RESULT_OK);
                                mActivity.finishWithAnim();
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

    @OnClick(R.id.ll_edit_icon)
    void editHeadImg() {
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(this).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                goToTakePhoto();
                            }
                        })
                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Album.album(mActivity)
                                        .requestCode(REQUEST_IMG_CODE) // 请求码，返回时onActivityResult()的第一个参数。
                                        .toolBarColor(ContextCompat.getColor(MineDetailEditActivity.this, R.color.red)) // Toolbar 颜色
                                        .statusBarColor(ContextCompat.getColor(MineDetailEditActivity.this, R.color.red)) // StatusBar 颜色。
                                        .navigationBarColor(ContextCompat.getColor(MineDetailEditActivity.this, R.color.red)) // NavigationBar 颜色
                                        .selectCount(1) // 最多选择几张图片。
                                        .columnCount(3) // 相册展示列数，默认是2列。
                                        .camera(false) // 是否有拍照功能。
                                        .start();
                            }
                        });

        actionSheetDialog.show();

    }

    /**
     * 调用系统相机
     */
    private void goToTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        photoFile = new File(photoFile,  "temp.jpg");
        if(!photoFile.getParentFile().exists()){
            photoFile.getParentFile().mkdirs();
        }
        if(photoFile.exists()){
            photoFile.delete();
        }
        if (Build.VERSION.SDK_INT > 23) {
            /**Android 7.0以上的方式**/
            String authority = getPackageName()+".fileprovider";
            Uri contentUri = getUriForFile(this, authority, photoFile);
            grantUriPermission(getPackageName(), contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            /**Android 7.0以下的方式**/
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        }//content://cn.teachcourse.fileProvider/root_path/storage/emulated/0/Pictures/1617336520974.jpg
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { // 判断是否成功。
            if (requestCode == REQUEST_IMG_CODE) {
                List<String> pathList = Album.parseResult(data);
                startPhotoZoom(GenalralUtils.getImageContentUri(mActivity, pathList.get(0)));
            }else if(requestCode == REQUEST_CAMERA_CODE){
                if(photoFile.exists()){
                    if (Build.VERSION.SDK_INT > 23) {
                        /**Android 7.0以上的方式**/
                        String authority = getPackageName()+".fileprovider";
                        Uri contentUri = getUriForFile(this, authority, photoFile);
                        startPhotoZoom(contentUri);
                    } else {
                        /**Android 7.0以下的方式**/
                        startPhotoZoom(Uri.fromFile(photoFile));
                    }
                }
            } else if (requestCode == REQUEST_CROP_CODE) {
                cropFile = new File(IMAGE_FILE_LOCATION);
                if (cropFile.exists()) {
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
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.HEAD_IMG_UPLOAD, TAG2, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("头像上传成功----》" + jsonString);
                ImageLoadUtils.showHttpImageCycleNoCache(mActivity, NetConstants.HEAD_IMAG_URL + filename, imgUsericon, R.drawable.ic_user_default_icon, R.drawable.ic_user_default_icon);
                if(photoFile != null && photoFile.exists()){
                    photoFile.delete();
                }
                if(cropFile != null && cropFile.exists()){
                    cropFile.delete();
                }

                EventBus.getDefault().post(new HeadEditEvent("success"));
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.shortToast(mActivity, "头像上传失败！");
                mLoadingDialog.dismiss();
                if(photoFile != null && photoFile.exists()){
                    photoFile.delete();
                }
                if(cropFile != null && cropFile.exists()){
                    cropFile.delete();
                }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(intent, REQUEST_CROP_CODE);
    }
}
