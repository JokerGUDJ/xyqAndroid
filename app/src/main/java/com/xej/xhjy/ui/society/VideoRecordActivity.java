package com.xej.xhjy.ui.society;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.LogUtils;

import java.io.File;

/**
 * @author dazhi
 * @class VideoRecordActivity 短视频录制页面，采用谷歌提供的CameraView来录制，考虑到通用性，故不使用框架的页面结构
 * @Createtime 2018/12/4 16:17
 * @description describe
 * @Revisetime
 * @Modifier
 */

public class VideoRecordActivity extends FragmentActivity {
    private JCameraView mCameraView;
    //判断是否用高清
    public static final String ISQualityHight = "isQualityHight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
        setContentView(R.layout.activity_video_record);
        mCameraView = findViewById(R.id.jcameraview);
        //设置视频保存路径
        mCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        mCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        //设置视频质量
        mCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_POOR);
        mCameraView.setTip("长按录像，最长10秒");
        //mCameraView监听
        mCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
                LogUtils.dazhiLog("open camera error----》");
            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                LogUtils.dazhiLog("AudioPermissionError-----》");
            }
        });

        mCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                //获取图片bitmap
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                Intent intent = new Intent();
                intent.putExtra("video_path", url);
                intent.putExtra("video_first_image", Base64Utils.bitmapToString(firstFrame));
                setResult(RESULT_OK, intent);
                VideoRecordActivity.this.finish();
                if (firstFrame != null) {
                    firstFrame.recycle();
                }
            }
            //@Override
            //public void quit() {
            //    (1.1.9+后用左边按钮的点击事件替换)
            //}
        });
        //左边按钮点击事件
        mCameraView.setLeftClickListener(() -> {
            VideoRecordActivity.this.finish();
        });
        //右边按钮点击事件
        mCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(VideoRecordActivity.this, "Rightclick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCameraView.onPause();
    }
}
