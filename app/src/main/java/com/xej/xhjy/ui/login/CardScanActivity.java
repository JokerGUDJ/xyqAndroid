package com.xej.xhjy.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.bean.CardScanResultBean;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.FileUtil;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.PathUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.ui.login.cropper.CameraPreview;
import com.xej.xhjy.ui.login.cropper.CropImageView;
import com.xej.xhjy.ui.login.cropper.CropListener;
import com.xej.xhjy.ui.login.cropper.ImageUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author dazhi
 * @class CardScanActivity 名片识别
 * @Createtime 2018/10/19 10:46
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class CardScanActivity extends BaseActivity implements View.OnClickListener {
    private CropImageView mCropImageView;
    private Bitmap mCropBitmap;
    private CameraPreview mCameraPreview;
    private View mLlCameraCropContainer;
    private View mViewLeft;
    private ImageView mIvCameraCrop;
    private TextView mIvCameraFlash;
    private View mLlCameraOption;
    private View mLlCameraResult;
    private TextView mViewCameraCropBottom;
    private ClubLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        initView();
        initListener();
    }

    private void initView() {
        mLoadingDialog = new ClubLoadingDialog(this);
        mViewLeft = findViewById(R.id.camera_preview_layout);
        mCameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
        mLlCameraCropContainer = findViewById(R.id.ll_camera_crop_container);
        mIvCameraCrop = (ImageView) findViewById(R.id.iv_camera_crop);
        mIvCameraFlash = (TextView) findViewById(R.id.btn_jump_over);
        mLlCameraOption = findViewById(R.id.re_camera_option);
        mLlCameraResult = findViewById(R.id.ll_camera_result);
        mCropImageView = (CropImageView) findViewById(R.id.crop_image_view);
        mIvCameraCrop.setImageResource(R.drawable.ic_bg_camera);
        mViewCameraCropBottom = (TextView) findViewById(R.id.tv_camera_crop_bottom);

        //获取屏幕最小边，设置为cameraPreview较窄的一边
        float screenMinSize = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels)*0.8f;
        //根据screenMinSize，计算出cameraPreview的较宽的一边，长宽比为标准名片的宽高比
        float maxSize = screenMinSize / 54.0f * 90.0f;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) maxSize, (int) screenMinSize);
        mViewLeft.setLayoutParams(layoutParams);

        float height = (int) (screenMinSize * 0.75);
        float width = height / 54.0f * 90.0f;
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
        mLlCameraCropContainer.setLayoutParams(containerParams);
        mIvCameraCrop.setLayoutParams(cropParams);
    }

    private void initListener() {
        mCameraPreview.setOnClickListener(this);
        mIvCameraFlash.setOnClickListener(this);
        findViewById(R.id.btn_camera_close).setOnClickListener(this);
        findViewById(R.id.btn_camera_take).setOnClickListener(this);
        findViewById(R.id.iv_camera_result_ok).setOnClickListener(this);
        findViewById(R.id.iv_camera_result_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.camera_preview) {
            mCameraPreview.focus();
        } else if (id == R.id.btn_camera_close) {
            finish();
        } else if (id == R.id.btn_camera_take) {
            takePhoto();
        } else if (id == R.id.btn_jump_over) {
            startActivityWithAnim(new Intent(mActivity,RegisterActivity.class));
            finishWithAnim();
        } else if (id == R.id.iv_camera_result_ok) {
            confirm();
        } else if (id == R.id.iv_camera_result_cancel) {
            mCameraPreview.setEnabled(true);
            mCameraPreview.startPreview();
            setTakePhotoLayout();
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        mCameraPreview.setEnabled(false);
        mCameraPreview.takePhoto(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                LogUtils.dazhiLog("图片大小data1=" + data.length);
                camera.stopPreview();
                //子线程处理图片，防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = GenalralUtils.getCompressImage(BitmapFactory.decodeByteArray(data, 0, data.length));
                        if (bitmap == null)
                            return;
                        /*计算裁剪位置*/
                        float left, top, right, bottom;
                        left = ((float) mLlCameraCropContainer.getLeft() - (float) mCameraPreview.getLeft()) / (float) mCameraPreview.getWidth();
                        top = (float) mIvCameraCrop.getTop() / (float) mCameraPreview.getHeight();
                        right = (float) mLlCameraCropContainer.getRight() / (float) mCameraPreview.getWidth();
                        bottom = (float) mIvCameraCrop.getBottom() / (float) mCameraPreview.getHeight();

                        /*自动裁剪*/
                        mCropBitmap = Bitmap.createBitmap(bitmap,
                                (int) (left * (float) bitmap.getWidth()),
                                (int) (top * (float) bitmap.getHeight()),
                                (int) ((right - left) * (float) bitmap.getWidth()),
                                (int) ((bottom - top) * (float) bitmap.getHeight()));

                        /*手动裁剪*/
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //将裁剪区域设置成与扫描框一样大
                                mCropImageView.setLayoutParams(new LinearLayout.LayoutParams(mIvCameraCrop.getWidth(), mIvCameraCrop.getHeight()));
                                setCropLayout();
                                mCropImageView.setImageBitmap(mCropBitmap);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    /**
     * 设置裁剪布局
     */
    private void setCropLayout() {
        mIvCameraCrop.setVisibility(View.GONE);
        mCameraPreview.setVisibility(View.GONE);
        mLlCameraOption.setVisibility(View.GONE);
        mCropImageView.setVisibility(View.VISIBLE);
        mLlCameraResult.setVisibility(View.VISIBLE);
        mViewCameraCropBottom.setText("");
    }

    /**
     * 设置拍照布局
     */
    private void setTakePhotoLayout() {
        mIvCameraCrop.setVisibility(View.VISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mLlCameraOption.setVisibility(View.VISIBLE);
        mCropImageView.setVisibility(View.GONE);
        mLlCameraResult.setVisibility(View.GONE);
        mViewCameraCropBottom.setText("触摸屏幕对焦");

        mCameraPreview.focus();
    }

    /**
     * 点击确认，返回图片路径
     */
    private void confirm() {
        /*裁剪图片*/
        mCropImageView.crop(new CropListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
                LogUtils.dazhiLog("名片bitmap大小---" + bitmap.getByteCount());
                /*保存图片到sdcard并返回图片路径*/
                if (FileUtil.createOrExistsDir(PathUtils.DIR_ROOT)) {
                    StringBuffer buffer = new StringBuffer();
                    String imagePath = buffer.append(PathUtils.DIR_ROOT).append("xejimg.jpg").toString();
                    if (ImageUtils.save(bitmap, imagePath, Bitmap.CompressFormat.JPEG)) {
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                        String str = Base64Utils.fileToBase64(imagePath);
                        cardRecongnition(str);
                    }
                }
            }
        }, true);
    }

    private void cardRecongnition(String base64) {
        String TAG = "cardRecongnition";
        Map<String, String> maps = new HashMap<>();
        maps.put("files", base64);
        addTag(TAG);
        mLoadingDialog.show();
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.CARD_RECOGNITION, TAG, maps, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("名片识别结果---》" + jsonString);
                CardScanResultBean bean = JsonUtils.stringToObject(jsonString, CardScanResultBean.class);
                if (bean != null) {
                    if (Integer.parseInt(bean.getCode()) == 0) {
                        if (bean.getContent() != null && bean.getContent().getJson() != null && bean.getContent().getJson().size() > 0) {
                            Intent intent = new Intent(mActivity,RegisterActivity.class);
                            intent.putExtra("scan_result",bean);
                            startActivityWithAnim(intent);
                            finishWithAnim();
                            return;
                        }
                    }
                }
                startRegister();
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("名片识别错误----》"+errorMsg);
                startRegister();
            }
        });
    }

    private void startRegister(){
        ToastUtils.shortToast(mActivity, "名片识别失败,请手动注册！");
        Intent intent = new Intent(mActivity,RegisterActivity.class);
        startActivityWithAnim(intent);
        finishWithAnim();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCameraPreview != null) {
            mCameraPreview.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraPreview != null) {
            mCameraPreview.onStop();
        }
    }
}