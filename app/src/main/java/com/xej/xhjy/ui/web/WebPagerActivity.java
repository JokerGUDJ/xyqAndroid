package com.xej.xhjy.ui.web;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.dsbridge.CompletionHandler;
import com.xej.xhjy.common.dsbridge.DWebView;
import com.xej.xhjy.common.dsbridge.OnReturnValue;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.Base64Utils;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.tools.Utils;
import com.xej.xhjy.ui.dialog.ClubDialog;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.home.ImageViewPagerActivity;
import com.xej.xhjy.ui.live.LiveActivity;
import com.xej.xhjy.ui.login.LoginCallBack;
import com.xej.xhjy.ui.login.LoginEvent;
import com.xej.xhjy.ui.login.LoginFailedEvent;
import com.xej.xhjy.ui.main.BridgeActivity;
import com.xej.xhjy.ui.main.HasMessageEvent;
import com.xej.xhjy.ui.society.ImagePagerActivity;
import com.xej.xhjy.ui.view.BottomPopupOption;
import com.xej.xhjy.ui.view.OnBackClickListener;
import com.xej.xhjy.ui.view.TitleView;
import com.yanzhenjie.album.Album;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import io.reactivex.functions.Consumer;

/**
 * @author dazhi
 * @class WebPagerActivity
 * @Createtime 2018/6/7 17:02
 * @description 正常H5加载页面
 * @Revisetime
 * @Modifier
 */
public class WebPagerActivity extends BaseActivity {
    /**
     * 网页地址
     */
    public static final String LOAD_URL = "url_address";
    /**
     * 传过来的头条ID
     */
    public static final String HOTLINE_ID = "hotline_id";
    /**
     * 传过来的会议ID
     */
    public static final String MEETTING_ID = "meetting_id";
    /**
     * 传过来的会议ID
     */
    public static final String MEETTING_PARAMS = "meetting_params";
    /**
     * 是否显示消息按钮
     */
    public static final String MISS_MESSAGE = "miss_message";
    /**
     * 传过来的PlaceName
     */
    public static final String MEETTING_PLACENAME = "placeName";

    private static final int PHOTO_RESULT = 9029;

    @BindView(R.id.ll_webContain)
    LinearLayout llWebContain;
    DWebView mWebView;
    String mUrl;
    @BindView(R.id.titleview)
    TitleView titleview;
    private ClubLoadingDialog mLoadingDialog;
    private boolean isLoadSuccess, isError;
    private String meettingID, hotlineID, placeName;
    private LoginCallBack mCallBack;
    private String meetParams;
    private WebLearnPlatformActivity.CallBackClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_pager);
        EventBus.getDefault().register(this);
        meettingID = getIntent().getStringExtra(MEETTING_ID);
        hotlineID = getIntent().getStringExtra(HOTLINE_ID);
        meetParams = getIntent().getStringExtra(MEETTING_PARAMS);
        placeName = getIntent().getStringExtra(MEETTING_PLACENAME);
        //控件绑定
        ButterKnife.bind(this);
        if (getIntent().getBooleanExtra(MISS_MESSAGE, false)) {
            titleview.setMessageVisibile(false);
        }
        mLoadingDialog = new ClubLoadingDialog(this);
        mUrl = NetConstants.H5_BASE_URL + getIntent().getStringExtra(LOAD_URL);
        mWebView = new DWebView(this);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llWebContain.addView(mWebView, ll);
        DWebView.setWebContentsDebuggingEnabled(AppConstants.IS_DEBUG);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i == 100) {
                    if (!isLoadSuccess) {
                        isLoadSuccess = true;
                        //页面加载完成首先传字典过去
                        mWebView.callHandler("dict", new Object[]{AppConstants.DATA_DICTIONARY}, new OnReturnValue<JSONObject>() {
                            @Override
                            public void onValue(JSONObject retValue) {

                            }
                        });
                    }
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                isError = true;
                isLoadSuccess = false;
                mLoadingDialog.dismiss();
                super.onReceivedError(webView, i, s, s1);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
//                LogUtils.dazhiLog("拦截的url--------"+url);
                if (url.startsWith(NetConstants.BASE_IP)) {
                    return super.shouldInterceptRequest(webView, url);
                } else {
                    return null;
                }
            }
        });
        //映射.可以调用js里面的方法
        mWebView.addJavascriptObject(new JsApi(), null);
        mWebView.loadUrl(mUrl);
        titleview.setBackListener(new OnBackClickListener() {
            @Override
            public void backClick() {
                if (isLoadSuccess && !isError) {
                    //把用户点击返回告诉H5来处理
                    mWebView.callHandler("nativeBack", new Object[]{});
                } else {
                    finishWithCheckMessage();
                }
            }
        });
        mLoadingDialog.show();
    }


    /**
     * 检测是否是否有新消息再关闭
     */
    private void finishWithCheckMessage() {
        if (!getIntent().getBooleanExtra(MISS_MESSAGE, false)) {
            finishWithAnim();
            return;
        }
        mLoadingDialog.show();
        String TAG = "message_new_message";
        addTag(TAG);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.POST_NEW_MESSAGE, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        JSONArray jsonArray = jsonObject.optJSONArray("content");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            EventBus.getDefault().post(new HasMessageEvent(true));
                            AppConstants.HAS_NEW_MESSAGE = true;
                        } else {
                            EventBus.getDefault().post(new HasMessageEvent(false));
                            AppConstants.HAS_NEW_MESSAGE = false;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    finishWithAnim();
                }
                LogUtils.dazhiLog("main_new_message---》" + jsonString);
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
                finishWithAnim();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {//webview退出，否则会造成内存泄漏
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
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


    /**
     * web的网络请求给原生来做
     *
     * @param url
     * @param map
     */
    public void doNetPostString(final CompletionHandler<String> handler, final String url, Map<String, String> map) {
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        LogUtils.dazhiLog("urlPath----" + url);
        RxHttpClient.doPostStringWithUrl(mActivity, url, url, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("H5请求结果返回----" + jsonString);
                handler.complete(jsonString);
            }

            @Override
            public void onError(String errorMsg) {
                mLoadingDialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (isLoadSuccess && !isError) {
            //把用户点击返回告诉H5来处理
            mWebView.callHandler("nativeBack", new Object[]{});
        } else {
            finishWithCheckMessage();
        }
    }

    /**
     * 弹窗交给原生
     *
     * @param msg      弹窗内容
     * @param handler  回调内容
     * @param isSingle 是否单确定按钮
     */
    private void showMessage(String msg, final CompletionHandler<String> handler, boolean isSingle) {
        if (isSingle) {
            ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(this);
            dialog.setMessage(msg);
            dialog.setPositiveListener("确定", new PositiveListener() {

                @Override
                public void onPositiveClick() {
                    handler.complete("YES");
                }
            });
            dialog.show();
        } else {
            ClubDialog dialog = new ClubDialog(this);
            dialog.setMessage(msg);
            dialog.setPositiveListener("确定", new PositiveListener() {

                @Override
                public void onPositiveClick() {
                    handler.complete("YES");
                }
            });
            dialog.setNegativeListener("取消", new NegativeListener() {

                @Override
                public void onNegativeClick() {
                    handler.complete("NO");
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_RESULT) {
                List<String> pathList = Album.parseResult(data);
                if (pathList.size() > 0) {
                    //只能上传一张图片
                    String path = pathList.get(0);
                    File file = CompressorFile(path);
                    uploadFile(file.getAbsolutePath(), file.getName());

                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * @author dazhi
     * @class JsApi
     * @Createtime 2018/7/5 17:39
     * @description H5调用原生的Api集合
     * @Revisetime
     * @Modifier
     */
    public class JsApi {
        /**
         * 设置标题
         */
        @JavascriptInterface
        public void setTitle(Object msg) {
            LogUtils.dazhiLog("setTitle---->" + msg);
            try {
                final JSONObject jsonObject = new JSONObject(String.valueOf(msg));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String title = jsonObject.optString("title");
                        titleview.setTitle(title);
                        if (title.contains("报名结果")) {
                            titleview.setBackVisibile(false);
                        } else {
                            titleview.setBackVisibile(true);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * H5取头条ID
         */
        @JavascriptInterface
        public String getHotlineID(Object msg) {
            if (TextUtils.isEmpty(hotlineID)) {
                return "";
            } else {
                return hotlineID;
            }
        }

        @JavascriptInterface
        public String getCommitId(Object msg){
            return PerferenceUtils.get(AppConstants.User.COMMIT_ID, "");
        }


        /**
         * H5取当前登录人机构ID
         */
        @JavascriptInterface
        public String getUserOrgId(Object msg) {
            return PerferenceUtils.get(AppConstants.User.ORGID, "");
        }

        /**
         * H5取岗位列表
         */
        @JavascriptInterface
        public String getJobArr(Object msg) {
            String job = PerferenceUtils.get(AppConstants.DATA_JOB_LIS_KEY, "");
            if (!TextUtils.isEmpty(job)) {
                try {
                    JSONObject js = new JSONObject(job);
                    return js.optString("content");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        /**
         * H5取会议ID
         */
        @JavascriptInterface
        public String getMeettingID(Object msg) {
            if (TextUtils.isEmpty(meettingID)) {
                return "";
            } else {
                return meettingID;
            }
        }

        /**
         * 获取个人手机号码
         */
        @JavascriptInterface
        public String getMobilePhone(Object msg) {
            return PerferenceUtils.get(AppConstants.User.PHONE, "");
        }

        /**
         * 获取accId
         */
        @JavascriptInterface
        public String getAccId(Object msg) {
            return PerferenceUtils.get(AppConstants.User.IM_CHAT_ACCOUNT, "");
        }

        /**
         * H5取placeName
         */
        @JavascriptInterface
        public String getPlaceID(Object msg) {
            if (TextUtils.isEmpty(placeName)) {
                return "";
            } else {
                LogUtils.dazhiLog("placeName---->" + placeName);
                return placeName;
            }
        }

        /**
         * H5取异步字典
         */
        @JavascriptInterface
        public void getDict(Object msg, final CompletionHandler<String> handler) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getDataDictionary(handler);
                }
            });
        }

        /**
         * H5取会议参数
         */
        @JavascriptInterface
        public void getParams(Object msg, final CompletionHandler<String> handler) {
            if (!GenalralUtils.isEmpty(meetParams)) {
                handler.complete(meetParams);
            }
        }

        /**
         * H5取App是否登录
         */
        @JavascriptInterface
        public void checkLogin(Object msg, final CompletionHandler<String> handler) {
            LogUtils.dazhiLog("AppConstants.IS_LOGIN---->" + AppConstants.IS_LOGIN);
            mCallBack = new LoginCallBack() {
                @Override
                public void loginAfterRun() {
                    handler.complete("YES");
                }
            };
            mCallBack.isPass = true;
            LoginUtils.startCheckLogin(mActivity, mCallBack);
        }

        /**
         * H5调用返回
         */
        @JavascriptInterface
        public void VueBack(Object msg) {
            LogUtils.dazhiLog("VueBack---->" + msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finishWithCheckMessage();
                }
            });

        }

        /**
         * 调用扫码
         */
        @JavascriptInterface
        public void scanSign(Object msg) {
            LogUtils.dazhiLog("scanSign---->" + msg);
            if (msg != null) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(msg));
                    String meetId = jsonObject.optString("meetId");
                    Intent intent = new Intent(WebPagerActivity.this, BridgeActivity.class);
                    intent.putExtra(BridgeActivity.START_ZXING, true);
//                    intent.putExtra(BridgeActivity.CHECK_MEETID, meetId);
                    WebPagerActivity.this.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * NetWorkStatusEvent
         * 文件浏览
         */
        @JavascriptInterface
        public void fileShow(Object fileUrl) {
            LogUtils.dazhiLog("fileShow---->" + fileUrl);
            Intent intent = new Intent(WebPagerActivity.this, WebFileBrowserActivity.class);
            intent.putExtra(WebFileBrowserActivity.FILE_URL_KEY, String.valueOf(fileUrl));
            WebPagerActivity.this.startActivity(intent);
        }

        /**
         * 清除mLoadingDialog
         */
        @JavascriptInterface
        public void hideMask(Object msg) {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }


        /**
         * 拨打电话
         */
        @JavascriptInterface
        public void callPhone(Object msg) {
            String phone = String.valueOf(msg);
            LogUtils.dazhiLog("callPhone---->" + phone);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(phone)) {
                        //先申请权限
                        new RxPermissions(WebPagerActivity.this)
                                .requestEach(Manifest.permission.CALL_PHONE)
                                .subscribe(new Consumer<Permission>() {
                                    @Override
                                    public void accept(Permission permission) {
                                        if (permission.name.equals(Manifest.permission.CALL_PHONE)) {
                                            //当权限获取成功时，permission.granted=true
                                            if (permission.granted) {
                                                final BottomPopupOption bottomPopupOption = new BottomPopupOption(WebPagerActivity.this);
                                                bottomPopupOption.setItemText(phone);
                                                bottomPopupOption.showPopupWindow();
                                                bottomPopupOption.setItemClickListener(new BottomPopupOption.onPopupWindowItemClickListener() {
                                                    @Override
                                                    public void onItemClick(int position) {
                                                        switch (position) {
                                                            case 0:
                                                                bottomPopupOption.dismiss();//点击项目消失的方法
                                                                Utils.callPhone(WebPagerActivity.this, phone);

                                                                break;
                                                        }
                                                    }
                                                });


                                            } else {
                                                //未获得权限直接关闭页面
                                                ToastUtils.shortToast(WebPagerActivity.this, "未获得相机使用权限，请在设置中修改！");
                                                finish();
                                            }
                                        }
                                    }
                                });

                    }


                }
            });


        }

        /**
         * 拷贝文字
         */
        @JavascriptInterface
        public void copyText(Object msg) {
            LogUtils.dazhiLog("邮箱----" + String.valueOf(msg));
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(String.valueOf(msg));
            ToastUtils.shortToast(WebPagerActivity.this, "复制成功!");

        }

        /**
         * 返回图片地址
         *
         * @param msg
         */
        @JavascriptInterface
        public void imgUrlPath(Object msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //imagesize是作为loading时的图片size
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(400, 400);
                    List<String> photoUrls = new ArrayList<String>();
                    photoUrls.add(String.valueOf(msg));
                    //复用帖子列表
                    ImagePagerActivity.startImagePagerActivity(WebPagerActivity.this, photoUrls, 0, imageSize);


                }
            });

        }


        /**
         * 头像上传
         *
         * @param msg
         */
        @JavascriptInterface
        public void uploadHead(Object msg, CompletionHandler<Object> handler) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new RxPermissions(WebPagerActivity.this)
                            .requestEach(Manifest.permission.CAMERA)
                            .subscribe(new Consumer<Permission>() {
                                @Override
                                public void accept(Permission permission) {
                                    if (permission.name.equals(Manifest.permission.CAMERA)) {
                                        //当权限获取成功时，permission.granted=true
                                        if (permission.granted) {
                                            Album.album(WebPagerActivity.this)
                                                    .requestCode(PHOTO_RESULT) // 请求码，返回时onActivityResult()的第一个参数。
                                                    .toolBarColor(WebPagerActivity.this.getResources().getColor(R.color.red)) // Toolbar 颜色，默认蓝色。
                                                    .statusBarColor(WebPagerActivity.this.getResources().getColor(R.color.red)) // StatusBar 颜色，默认蓝色。
                                                    .navigationBarColor(WebPagerActivity.this.getResources().getColor(R.color.red)) // NavigationBar 颜色，默认黑色，建议使用默认。
                                                    .selectCount(Integer.parseInt(String.valueOf(msg))) // 最多选择几张图片。
                                                    .columnCount(3) // 相册展示列数，默认是2列。
                                                    .camera(true)// 是否有拍照功能。
                                                    .start();
                                            mListener = new WebLearnPlatformActivity.CallBackClickListener() {
                                                @Override
                                                public void callBack(Object jsonObject) {
                                                    LogUtils.dazhiLog("handler---->" + jsonObject);
                                                    handler.complete(jsonObject);

                                                }
                                            };
                                        } else {
                                            //未获得权限直接关闭页面
                                            ToastUtils.shortToast(WebPagerActivity
                                                    .this, "未获得相机使用权限，请在设置中修改！");
                                            finishWithAnim();
                                        }


                                    }

                                }


                            });

                }
            });

        }


        /**
         * 返回数据科技产品
         */
        @JavascriptInterface
        public void technologyProductsIndex(Object msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WebPagerActivity.this, ImageViewPagerActivity.class);
                    intent.putExtra(ImageViewPagerActivity.IMGINDEX, String.valueOf(msg));
                    WebPagerActivity.this.startActivity(intent);

                }
            });
        }

        /**
         * 跳转到外部浏览器
         */
        @JavascriptInterface
        public void openBrowser(Object msg) {
            LogUtils.dazhiLog("打开外部链接----" + String.valueOf(msg));
            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(msg));
                Uri uri = Uri.parse(jsonObject.optString("webURL"));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        /**
         * 弹窗提示
         */
        @JavascriptInterface
        public void showMsgAlert(Object msg, final CompletionHandler<String> handler) {
            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(msg));
                final String message = jsonObject.optString("message");
                final boolean isSingle = "single".equals(jsonObject.optString("type"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMessage(message, handler, isSingle);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        /**
         * 网络请求
         */
        @JavascriptInterface
        public void sendRequest(Object msg, final CompletionHandler<String> handler) {
            LogUtils.dazhiLog("sendRequest---->" + msg);
            try {
                JSONObject data = new JSONObject(String.valueOf(msg));
                final String url = data.optString("transactionId").replace("\\", "");
                JSONObject params = data.optJSONObject("params");
                final Map<String, String> map = JsonUtils.jsonObjectToMap(params);
                //使用url当tag，加入tag以关闭时取消请求
                addTag(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        doNetPostString(handler, url, map);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 播放视频
         */
        @JavascriptInterface
        public void videoPlay(Object msg) {
            try {
                JSONObject jsonVideo = new JSONObject(String.valueOf(msg));
                final String url = jsonVideo.optString("duration");
                if (TextUtils.isEmpty(url))
                    return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(WebPagerActivity.this, LiveActivity.class);
                        intent.putExtra(LiveActivity.PLAY_URL, url);
                        startActivity(intent);


                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收到登录成功的消息，执行之前的跳转代码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent event) {
        if (mCallBack != null && mCallBack.canUse) {
            if (mCallBack.isPass) {
                if ("N".equals(AppConstants.USER_STATE)) {//必须认证
                    mCallBack.loginAfterRun();
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
     * 获取全局数据匹配字典
     */
    private void getDataDictionary(final CompletionHandler<String> handler) {
        String TAG = "get_dict";
        addTag(TAG);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.DATA_DICTIONARY, TAG, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                try {
                    //存储字典
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optString("code").equals("0")) {
                        JSONObject content = json.optJSONObject("content");
                        if (content != null) {
                            AppConstants.DATA_DICTIONARY = content;
                            PerferenceUtils.put(AppConstants.DATA_DICTIONARY_KEY, content.toString());
                            handler.complete(AppConstants.DATA_DICTIONARY.toString());
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (AppConstants.DATA_DICTIONARY != null) {
                    handler.complete(AppConstants.DATA_DICTIONARY.toString());
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (AppConstants.DATA_DICTIONARY != null) {
                    handler.complete(AppConstants.DATA_DICTIONARY.toString());
                }
                mLoadingDialog.dismiss();
            }
        });
    }

    /**
     * 上传图片
     *
     * @param
     */
    private void uploadFile(String fileurl, String fileName) {
        mLoadingDialog.show();
        String TAG = "send_upload_image";
        mActivity.addTag(TAG);
        Map<String, String> map = new HashMap<>();
        map.put("imgStrList", Base64Utils.fileToBase64(fileurl));
        map.put("name", fileName);
        String url = NetConstants.MEET_UPLOAD_HEAD;
        RxHttpClient.doPostStringWithUrl(mActivity, url, TAG, map, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                mLoadingDialog.dismiss();
                LogUtils.dazhiLog("jsonString" + jsonString);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                    if ("0".equals(jsonObject.optString("code"))) {
                        JSONObject content = jsonObject.getJSONObject("content");
                        if (mListener != null) {
                            mListener.callBack(content);
                        }
                    } else {
                        ToastUtils.shortToast(mActivity, "上传失败");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }
}
