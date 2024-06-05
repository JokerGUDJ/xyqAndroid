package com.xej.xhjy.ui.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.ToastUtils;
import com.xej.xhjy.tools.QRCodeUtil;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String content, name, subName, coverImage, extra;
    private ClipboardManager myClipboard;
    public ShareDialog(@NonNull Context context, String content,String name, String subName, String coverImage) {
        super(context, R.style.TranslucentFullScreenTheme);
        this.context = context;
        this.content = content;
        this.name = name;
        this.subName = subName;
        this.coverImage = coverImage;
    }

    public ShareDialog(@NonNull Context context, String content, String extra, String name, String subName, String coverImage) {
        super(context, R.style.TranslucentFullScreenTheme);
        this.context = context;
        this.content = content;
        this.name = name;
        this.subName = subName;
        this.coverImage = coverImage;
        this.extra = extra;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_liveing);
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();
    }

    private void initView(){
        myClipboard = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        LinearLayout ll_wechat = findViewById(R.id.ll_wechat);
        ll_wechat.setOnClickListener(this);
        LinearLayout ll_link = findViewById(R.id.ll_link);
        ll_link.setOnClickListener(this);

        TextView tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);

    }

    private void copyShareLink(){
        ClipData myClip = ClipData.newPlainText("text", TextUtils.isEmpty(extra)? content + "&isLink=1":extra + content + "&isLink=1");
        myClipboard.setPrimaryClip(myClip);
        ToastUtils.shortToast(context, "复制成功");
    }

    private void shareWechat(){
        OnekeyShare oks = new OnekeyShare();
        // text是分享文本，所有平台都需要这个字段
        oks.setTitle(name);
        oks.setText(subName);
        // setImageUrl是网络图片的url
        if(TextUtils.isEmpty(coverImage)){
            Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_share_defaulte);
            String path = null;
            try {
                path = BitmapHelper.saveBitmap(context, img);
                oks.setImagePath(path);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            oks.setImageData(img);
        }else{
            if(coverImage.contains("http")){
                oks.setImageUrl(coverImage);
            }else {
                oks.setImageUrl(AppConstants.BYTE_IMG_PREFIX+coverImage+AppConstants.BYTE_IMG_SUFFIX);
            }
        }
        // url在微信、Facebook等平台中使用
        if(content.contains("鑫直播")){

        }
        oks.setUrl(content);
        oks.setPlatform(Wechat.NAME);
        // 启动分享GUI
        oks.show(MobSDK.getContext());
        dismiss();
    }

    @Override
    public void onClick(View view) {
        if(view == null) return;
        switch (view.getId()){
            case R.id.ll_wechat:
                shareWechat();
                break;
            case R.id.ll_link:
                copyShareLink();
                break;
            case R.id.tv_cancel:
                break;
        }
        dismiss();
    }
}
