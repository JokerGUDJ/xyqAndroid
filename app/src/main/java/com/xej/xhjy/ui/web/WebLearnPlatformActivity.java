package com.xej.xhjy.ui.web;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.dsbridge.CompletionHandler;
import com.xej.xhjy.common.dsbridge.DSWebView;
import com.xej.xhjy.common.http.upload.UploadRetrofit;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpDialogUtils;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.society.VideoRecordActivity;
import com.yanzhenjie.album.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @author dazhi
 * @class WebLearnPlatformActivity
 * @Createtime 2019/4/2 17:02
 * @description 移动学习平台（执行力）
 * @Revisetime
 * @Modifier
 */
public class WebLearnPlatformActivity extends BaseActivity {

    @BindView(R.id.webview)
    DSWebView mWebView;
    @BindView(R.id.fl_video)
    FrameLayout mLayout;
    public static final String LOAD_URL = "url_address";
    private static final int QRCODE_RESULT = 9021;
    private static final int PICTURES_RESULT = 9023;
    private static final int PHOTO_RESULT = 9024;
    public static final int VIDEO_RESULT = 9025;
    private ClubLoadingDialog mLoadingDialog;
    private String filePath;
    private JSONObject jsonObject, jsonVideo;
    private boolean isLoadSuccess, isError;

    private CallBackClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red), 0);
        //控件绑定
        ButterKnife.bind(this);
        mLoadingDialog = new ClubLoadingDialog(this);
        mWebView.setWebContentsDebuggingEnabled(AppConstants.IS_DEBUG);//打开可调式
        mWebView.addJavascriptObject(new JavaScript(), null);
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                isError = true;
                isLoadSuccess = false;
                super.onReceivedError(webView, i, s, s1);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
                if (url.startsWith(NetConstants.BASE_IP)) {
                    return super.shouldInterceptRequest(webView, url);
                } else {
                    return null;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });

        mWebView.loadUrl(getIntent().getStringExtra(LOAD_URL));
    }

    private class MyWebChromeClient extends WebChromeClient {

        private CustomViewCallback mCustomViewCallback;
        //  横屏时，显示视频的view
        private View mCustomView;

        // 点击全屏按钮时，调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);

            //如果view 已经存在，则隐藏
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mCustomView = view;
            mCustomView.setVisibility(View.VISIBLE);
            mCustomViewCallback = callback;
            mLayout.addView(mCustomView);
            mLayout.setVisibility(View.VISIBLE);
            mLayout.bringToFront();

            //设置横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }

        // 取消全屏调用的方法
        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            mLayout.removeView(mCustomView);
            mCustomView = null;
            mLayout.setVisibility(View.GONE);
            try {
                mCustomViewCallback.onCustomViewHidden();
            } catch (Exception e) {
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        }
    }

    /**
     * 横竖屏切换监听
     */
    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }


    public class JavaScript {
        /**
         * 拍照上传图片
         */
        @JavascriptInterface
        public void getPicture(Object msg, CompletionHandler<Object> handler) {
            LogUtils.dazhiLog("imagePicker---->" + msg);
            try {
                jsonObject = new JSONObject(String.valueOf(msg));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new RxPermissions(WebLearnPlatformActivity.this)
                                .requestEach(Manifest.permission.CAMERA)
                                .subscribe(new Consumer<Permission>() {
                                    @Override
                                    public void accept(Permission permission) {
                                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
                                        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
                                            File imageFile = createImageFile();//创建用来保存照片的文件

                                            if (imageFile.exists()) {
                                                LogUtils.dazhiLog("文件存在");
                                                filePath = imageFile.getAbsolutePath();
                                            } else {
                                                LogUtils.dazhiLog("文件不存在");
                                            }
                                            Uri mImageUri = null;
                                            if (imageFile != null) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    mImageUri = FileProvider.getUriForFile(WebLearnPlatformActivity.this, mActivity.getPackageName() + ".generic.file.provider", imageFile);
                                                } else {
                                                    mImageUri = Uri.fromFile(imageFile);
                                                }
                                                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                                                startActivityForResult(takePhotoIntent, PICTURES_RESULT);//打开相机
                                            }
                                        }

                                        mListener = new CallBackClickListener() {
                                            @Override
                                            public void callBack(Object jsonObject) {
                                                LogUtils.dazhiLog("jsonObject---->" + jsonObject);
                                                handler.complete(jsonObject);

                                            }
                                        };


                                    }

                                });

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 选图
         */
        @JavascriptInterface
        public void imagePicker(Object msg, CompletionHandler<Object> handler) {
            LogUtils.dazhiLog("imagePicker---->" + msg);
            try {
                jsonObject = new JSONObject(String.valueOf(msg));
                final String message = jsonObject.optString("maximumImagesCount");
                if (TextUtils.isEmpty(message))
                    return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new RxPermissions(WebLearnPlatformActivity.this)
                                .requestEach(Manifest.permission.CAMERA)
                                .subscribe(new Consumer<Permission>() {
                                    @Override
                                    public void accept(Permission permission) {
                                        if (permission.name.equals(Manifest.permission.CAMERA)) {
                                            //当权限获取成功时，permission.granted=true
                                            if (permission.granted) {
                                                Album.album(WebLearnPlatformActivity.this)
                                                        .requestCode(PHOTO_RESULT) // 请求码，返回时onActivityResult()的第一个参数。
                                                        .toolBarColor(WebLearnPlatformActivity.this.getResources().getColor(R.color.red)) // Toolbar 颜色，默认蓝色。
                                                        .statusBarColor(WebLearnPlatformActivity.this.getResources().getColor(R.color.red)) // StatusBar 颜色，默认蓝色。
                                                        .navigationBarColor(WebLearnPlatformActivity.this.getResources().getColor(R.color.red)) // NavigationBar 颜色，默认黑色，建议使用默认。
                                                        .selectCount(Integer.parseInt(message)) // 最多选择几张图片。
                                                        .columnCount(3) // 相册展示列数，默认是2列。
                                                        .camera(true)// 是否有拍照功能。
                                                        .start();
                                                mListener = new CallBackClickListener() {
                                                    @Override
                                                    public void callBack(Object jsonObject) {
                                                        LogUtils.dazhiLog("jsonObject---->" + jsonObject);
                                                        handler.complete(jsonObject);

                                                    }
                                                };
                                            } else {
                                                //未获得权限直接关闭页面
                                                ToastUtils.shortToast(WebLearnPlatformActivity.this, "未获得相机使用权限，请在设置中修改！");
                                                finishWithAnim();
                                            }


                                        }

                                    }


                                });
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        /**
         * 小视频
         */
        @JavascriptInterface
        public void videoCapture(Object msg, CompletionHandler<Object> handler) {
            LogUtils.dazhiLog("videoCapture---->" + msg);
            try {
                jsonVideo = new JSONObject(String.valueOf(msg));
                final String message = jsonVideo.optString("duration");
                if (TextUtils.isEmpty(message))
                    return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //同时请求多个权限
                        new RxPermissions(mActivity)
                                .request(Manifest.permission.CAMERA,
                                        Manifest.permission.RECORD_AUDIO)
                                .subscribe(granted -> {
                                    if (granted) {
                                        Intent intent = new Intent(WebLearnPlatformActivity.this, VideoRecordActivity.class);
                                        WebLearnPlatformActivity.this.startActivityForResult(intent, VIDEO_RESULT);
                                        mListener = new CallBackClickListener() {
                                            @Override
                                            public void callBack(Object jsonObject) {
                                                LogUtils.dazhiLog("jsonObject---->" + jsonObject);
                                                handler.complete(jsonObject);

                                            }
                                        };
                                    } else {
                                        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(mActivity);
                                        dialog.setMessage("鑫合家园未获得录像相关权限，请到设置中修改！");
                                        dialog.setPositiveListener("确定", new PositiveListener() {
                                            @Override
                                            public void onPositiveClick() {
                                                finishWithAnim();
                                            }
                                        });
                                        dialog.show();
                                    }
                                });
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        /**
         * 清除mLoadingDialog
         */
        @JavascriptInterface
        public void hideMask(Object msg) {
            LogUtils.dazhiLog("取消遮罩层");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                        LogUtils.dazhiLog("取消遮罩层啦");
                        mLoadingDialog.dismiss();
                    }
                }
            });
        }

        /**
         * 显示遮罩层
         */
        @JavascriptInterface
        public void showMask(Object msg) {
            LogUtils.dazhiLog("显示遮罩层");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
                        mLoadingDialog.show();
                    }
                }
            });
        }

        /**
         * 分享微信和朋友圈
         */

        @JavascriptInterface
        public void shareAppMessage(Object msg, CompletionHandler<JSONObject> handler) {
            LogUtils.dazhiLog("shareAppMessage---->" + msg);
            try {
                JSONObject jsonShareApp = new JSONObject(String.valueOf(msg));
                final String title = jsonShareApp.optString("title");
                final String desc = jsonShareApp.optString("desc");
                final String imgUrl = jsonShareApp.optString("imgUrl");
                final String link = jsonShareApp.optString("link");
                final String type = jsonShareApp.optString("type");
                final String dataUrl = jsonShareApp.optString("dataUrl");
                boolean isWechat = jsonShareApp.optBoolean("shareTimeline");
                final OnekeyShare oks = new OnekeyShare();
                //指定分享的平台，false为朋友圈 true为好友
                if (isWechat) {
                    oks.setPlatform(WechatMoments.NAME);
                } else {
                    oks.setPlatform(Wechat.NAME);
                }
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(title);
                // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
                oks.setTitleUrl(dataUrl);
                // text是分享文本，所有平台都需要这个字段
                oks.setText(desc);
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                oks.setImageUrl(imgUrl);
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl(link);
                //启动分享
                oks.show(WebLearnPlatformActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        /**
         * 返回处理
         */

        @JavascriptInterface
        public void finish(Object msg) {
            finishWithAnim();
        }

    }

    @Override
    public void onBackPressed() {
        if (isLoadSuccess && !isError) {
            //把用户点击返回告诉H5来处理
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            mWebView.callHandler("nativeBack", new Object[]{});
        } else {
            finishWithAnim();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == QRCODE_RESULT) {
                String backStr = data.getStringExtra("barCode");
                LogUtils.dazhiLog("扫码返回数据------" + backStr);
            } else if (requestCode == PICTURES_RESULT) {
                LogUtils.dazhiLog("拍照返回数据------");
                List<String> list = new ArrayList<>();
                LogUtils.dazhiLog("拍照返回文件地址------" + filePath);
                File newFile = CompressorFile(filePath);
                if (newFile.exists()) {
                    list.add(newFile.getPath());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(list);
                    }
                }).start();

            } else if (requestCode == PHOTO_RESULT) {
                List<String> pathList = Album.parseResult(data);
                if (pathList.size() > 0) {
                    List<String> list = new ArrayList<>();
                    for (String str : pathList) {
                        File newFile = CompressorFile(str);
                        if (newFile.exists()) {
                            list.add(newFile.getPath());
                        }

                    }
                    LogUtils.dazhiLog("发送交易");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadFile(list);
                        }
                    }).start();

                }

            } else if (requestCode == VIDEO_RESULT) {
                String path = data.getStringExtra("video_path");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        upLoadVideo(path);
                    }
                }).start();

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片
     *
     * @param pathList
     */

    public void uploadFile(List<String> pathList) {
        Observable<ResponseBody> observable = UploadRetrofit.uploadImgs(NetConstants.WEB_LEARN_PLATFROM_UPLOADIMG, pathList, jsonObject);
        observable.subscribe(new ResourceObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                LogUtils.dazhiLog("上传读片地址-----" + NetConstants.WEB_LEARN_PLATFROM_UPLOADIMG);
                try {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody.string().toString());
                        String codeString = jsonObject.optString("code");
                        int code = Integer.parseInt(codeString);
                        if (code == 0) {
                            return;
                        }
                        if (code == 200) {
                            LogUtils.dazhiLog("上传图片code====" + code);
                            JSONArray data = jsonObject.getJSONArray("data");
                            LogUtils.dazhiLog("JSONObjectCode==200" + data);
                            if (mListener != null) {
                                mListener.callBack(data);
                            }
                        } else {
                            String mess = jsonObject.optString("msg");
                            PositiveListener listener = new PositiveListener() {
                                @Override
                                public void onPositiveClick() {
                                    if(dialog != null){
                                        dialog.dismiss();
                                        dialog = null;
                                    }
                                }
                            };
                            HttpDialogUtils.doHttpDialog(WebLearnPlatformActivity.this, mess, listener);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    ResponseBody body = ((HttpException) e).response().errorBody();
                    try {
                        String result = body.string();
                        LogUtils.dazhiLog("Throwable------->" + result);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }

            @Override
            public void onComplete() {
                LogUtils.dazhiLog("onComplete------->");
            }
        });
    }

    /**
     * 上传视频
     */
    private void upLoadVideo(String path) {
        Observable<ResponseBody> observable = UploadRetrofit.uploadVideo(NetConstants.WEB_LEARN_PLATFROM_UPLOADVIDEO, path, jsonVideo);
        observable.subscribe(new ResourceObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody.string().toString());
                        String codeString = jsonObject.optString("code");
                        int code = Integer.parseInt(codeString);
                        if (code == 0) {
                            return;
                        }
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            LogUtils.dazhiLog("视频上传成功-----" + data);
                            if (mListener != null) {
                                mListener.callBack(data);
                            }
                        } else {
                            String mess = jsonObject.optString("msg");
                            PositiveListener listener = new PositiveListener() {
                                @Override
                                public void onPositiveClick() {
                                    if(dialog != null){
                                        dialog.dismiss();
                                        dialog = null;
                                    }
                                }
                            };
                            HttpDialogUtils.doHttpDialog(WebLearnPlatformActivity.this, mess, listener);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    ResponseBody body = ((HttpException) e).response().errorBody();
                    try {
                        String result = body.string();
                        LogUtils.dazhiLog("Throwable------->" + result);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }

            @Override
            public void onComplete() {
                LogUtils.dazhiLog("onComplete------->");
            }
        });
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /**
     * 压缩文件
     *
     * @param filePath
     * @return File
     */
    private File CompressorFile(String filePath) {
        File newFile = null;
        try {
            newFile = new Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(new File(filePath));
            LogUtils.dazhiLog("拍照压缩前文件大小------->" + new File(filePath).length() / 1024);
            LogUtils.dazhiLog("拍照压缩后文件大小------->" + newFile.length() / 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {//webview退出，否则会造成内存泄漏
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            if (mLoadingDialog != null) {
                mLoadingDialog = null;
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    interface CallBackClickListener {
        void callBack(Object jsonObject);
    }


}


