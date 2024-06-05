package com.xej.xhjy.ui.web;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.dsbridge.CompletionHandler;
import com.xej.xhjy.common.dsbridge.DSWebView;
import com.xej.xhjy.common.utils.JsonUtils;
import com.xej.xhjy.common.utils.StatusBarUtils;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;
import com.xej.xhjy.tools.AndroidBug5497Workaround;
import com.xej.xhjy.ui.dialog.ClubLoadingDialog;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.dialog.ShareDialog;
import com.xej.xhjy.ui.live.LiveingMoreActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;


public class LiveingWebview extends BaseActivity implements View.OnClickListener {

    private  TextView tv_title, tv_close;
    /**
     * 网页地址
     */
    public static final String LOAD_URL = "url_address";
    public static final String HIDE_HEAD = "hide_head";
    private ClubLoadingDialog mLoadingDialog;
    private String liveId, mUrl, name, subName, coverImage, source;
    private DSWebView mWebView;
    private boolean isLoadSuccess;
    private RelativeLayout ll_head;
    private FrameLayout mLayout;
    //  横屏时，显示视频的view
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private boolean needCloseBtn = false;
    private ImageView img_share,img_back;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liveing_webview);
        initView();
        initData();
    }

    private void initView(){
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        img_share = findViewById(R.id.img_share);
        img_share.setOnClickListener(this);
        mWebView = findViewById(R.id.webview);
        mLoadingDialog = new ClubLoadingDialog(this);
        mLayout = findViewById(R.id.fl_video);
        ll_head = findViewById(R.id.ll_head);
        tv_close = findViewById(R.id.tv_close);
        tv_close.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        AndroidBug5497Workaround.assistActivity(this);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(StatusBarUtils.hasNavigationBar(this)){
            LinearLayout ll_webview = findViewById(R.id.ll_webview);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)ll_webview.getLayoutParams();
            layoutParams.bottomMargin = StatusBarUtils.getNavigationBarHeight(this);
            ll_webview.setLayoutParams(layoutParams);
        }
    }

    private void initData(){
        Intent intent = getIntent();
        if(intent != null){
            mUrl = intent.getStringExtra(LOAD_URL);
            liveId = intent.getStringExtra("liveId");
            name = intent.getStringExtra("name");
            subName = intent.getStringExtra("subName");
            coverImage = intent.getStringExtra("coverImage");
            needCloseBtn = intent.getBooleanExtra("closeBtn", false);
            source = intent.getStringExtra("source");
            boolean bHideTitle = intent.getBooleanExtra(HIDE_HEAD, false);
            if(needCloseBtn){
                tv_close.setVisibility(View.VISIBLE);
                img_share.setVisibility(View.GONE);
                tv_title.setText(name);
            }else{
                tv_title.setText(name);
            }
            if(bHideTitle){
                ll_head.setVisibility(View.GONE);
            }
        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptObject(new Plugin(), "");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i == 100) {
                    if (!isLoadSuccess) {
                        isLoadSuccess = true;
                        //页面加载完成首先传字典过去
                        mLoadingDialog.dismiss();
                    }
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);

                //如果view 已经存在，则隐藏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                ll_head.setVisibility(View.GONE);

                //设置横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            }

            // 取消全屏调用的方法
            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                if (mCustomView == null) {
                    return;
                }
                mCustomView.setVisibility(View.GONE);
                mLayout.removeView(mCustomView);
                mCustomView = null;
                mLayout.setVisibility(View.GONE);
                ll_head.setVisibility(View.VISIBLE);
                try {
                    mCustomViewCallback.onCustomViewHidden();
                } catch (Exception e) {
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                isLoadSuccess = false;
                mLoadingDialog.dismiss();
                super.onReceivedError(webView, i, s, s1);
            }
        });
        mWebView.loadUrl(mUrl);
        mLoadingDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {//webview退出，否则会造成内存泄漏
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

    private void showShareDialog(){
        ShareDialog dialog = new ShareDialog(LiveingWebview.this,
                NetConstants.BASE_IP+"xhyjcms/mobile/#/livescreamshare?liveId="+liveId,
                "鑫课堂："+name,
                "诚邀您一起观看鑫课堂，精彩内容不容错过！",
                        coverImage);
        if(dialog != null){
            dialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(needCloseBtn){
                if(mWebView.canGoBack()){
                    mWebView.goBack();
                }else{
                    finish();
                }
            }else{
                if("external".equals(source)){
                    //外部分享进入的直播页面，返回到更多页面
                    Intent intent = new Intent(this, LiveingMoreActivity.class);
                    intent.putExtra("source","external");//外部跳转到更多聚合页标识
                    startActivity(intent);
                    finish();
                }else{
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.img_back:
                if(needCloseBtn){
                    if(mWebView.canGoBack()){
                        mWebView.goBack();
                    }else{
                        finish();
                    }
                }else{
                    if("external".equals(source)){
                        //外部分享进入的直播页面，返回到更多页面
                        Intent intent = new Intent(this, LiveingMoreActivity.class);
                        intent.putExtra("source","external");//外部跳转到更多聚合页标识
                        startActivity(intent);
                        finish();
                    }else{
                        finish();
                    }
                }

                break;
            case R.id.tv_close:
                finish();
                break;
            case R.id.img_share:
                showShareDialog();
                break;
        }
    }

    private void showMessage(String msg, CompletionHandler<String> handler){
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage(msg);
        dialog.setPositiveListener("知道了", new PositiveListener() {

            @Override
            public void onPositiveClick() {
                handler.complete("YES");
            }
        });
        dialog.show();
    }

    public class Plugin{
        /**
         * 弹窗提示
         */
        @JavascriptInterface
        public void showMsgAlert(Object msg, final CompletionHandler<String> handler) {
            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(msg));
                final String message = jsonObject.optString("message");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMessage(message, handler);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取liveId
         */
        @JavascriptInterface
        public void getLiveId(Object msg, final CompletionHandler<String> handler){
            handler.complete(liveId);
        }

        /**
         * 网络请求
         */
        @JavascriptInterface
        public void sendRequest(Object msg, final CompletionHandler<String> handler) {
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
         * web的网络请求给原生来做
         *
         * @param url
         * @param map
         */
        public void doNetPostString(final CompletionHandler<String> handler, final String url, Map<String, String> map) {
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.show();
            }
            RxHttpClient.doPostStringWithUrl(mActivity, url, url, map, new HttpCallBack() {
                @Override
                public void onSucess(String jsonString) {
                    mLoadingDialog.dismiss();
                    handler.complete(jsonString);
                }

                @Override
                public void onError(String errorMsg) {
                    mLoadingDialog.dismiss();
                }
            });
        }

        /**
         * 设置标题
         */
        @JavascriptInterface
        public void setTitle(Object msg) {
            try {
                final JSONObject jsonObject = new JSONObject(String.valueOf(msg));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String title = jsonObject.optString("title");
                        tv_title.setText(title);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
