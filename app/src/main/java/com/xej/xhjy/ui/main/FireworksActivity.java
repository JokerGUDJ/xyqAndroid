package com.xej.xhjy.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.dsbridge.DWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FireworksActivity extends BaseActivity {

    @BindView(R.id.fire_web)
    DWebView fireWeb;
    @BindView(R.id.img_cancel)
    ImageView imgCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firework);
        ButterKnife.bind(this);
//        fireWeb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        fireWeb.setBackgroundColor(0);
//        fireWeb.loadUrl("file:///android_asset/firework.html");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgCancel.setVisibility(View.VISIBLE);
            }
        },1500);
    }

    @OnClick(R.id.img_cancel)
    void cancelPage() {
        finishWithAnim();
    }

    @Override
    protected void onDestroy() {
        if (fireWeb != null) {//webview退出，否则会造成内存泄漏
            ViewParent parent = fireWeb.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(fireWeb);
            }
            fireWeb.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            fireWeb.getSettings().setJavaScriptEnabled(false);
            fireWeb.clearHistory();
            fireWeb.clearView();
            fireWeb.removeAllViews();
            fireWeb.destroy();
        }
        super.onDestroy();
    }
}
