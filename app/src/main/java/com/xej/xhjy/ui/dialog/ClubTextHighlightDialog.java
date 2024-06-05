package com.xej.xhjy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.AppConstants;

import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.tools.MyClickText;
import com.xej.xhjy.ui.web.WebOtherPagerActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.ui.dialog
 * @ClassName: ClubTextHighlightDialog
 * @Description: 文本高量点击链接
 * @Author: lihy_0203
 * @CreateDate: 2019/12/10 下午3:07
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/10 下午3:07
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ClubTextHighlightDialog extends Dialog {
    private Button mPositiveBtn;//确定按钮
    private Button mNegativeBtn;//取消按钮
    private TextView mTitleTv;//消息标题文本
    private TextView mMsgTv;//消息提示文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String mPositiveBtnStr, mNegativeBtnStr;
    private PositiveListener mPositiveOnclickListener;//确定按钮被点击了的监听器
    private NegativeListener mNegativeOnclickListener;//取消按钮被点击了的监听器
    private int mMessageGravit = 0;
    private Context mContext;

    public ClubTextHighlightDialog(Context context) {
        super(context, R.style.ClubDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_light_dialog_club);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNegativeListener(String str, NegativeListener onNoOnclickListener) {
        if (str != null) {
            mNegativeBtnStr = str;
        }
        this.mNegativeOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setPositiveListener(String str, PositiveListener onYesOnclickListener) {
        if (str != null) {
            mPositiveBtnStr = str;
        }
        this.mPositiveOnclickListener = onYesOnclickListener;
    }


    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPositiveOnclickListener != null) {
                    mPositiveOnclickListener.onPositiveClick();
                }
                ClubTextHighlightDialog.this.dismiss();
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        mNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNegativeOnclickListener != null) {
                    mNegativeOnclickListener.onNegativeClick();
                }
                ClubTextHighlightDialog.this.dismiss();
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(titleStr)) {
            mTitleTv.setText(titleStr);
        }

        String url = "感谢您使用鑫合家园APP，为了保证对您的个人隐私信息合法、合理、适度的收集、使用，并在安全、可控的情况下进行传输、存储，我们依据《中华人民共和国网络安全法》、《信息安全技术 个人信息安全规范》（GB/T35273-2017）以及其他相关法律法规和技术规范，制定了\"《鑫合家园App用户隐私政策》\"。您在使用鑫合家园APP时，我们将按照本政策处理和保护您的个人信息。\n" +
                "APP中用到以下权限:\n" +
                "1,电话权限：在您使用鑫合家园的鑫合科技-联系我们功能时，需要允许鑫合家园拨打电话和管理通话；\n" +
                "2,存储权限：为确保登录后app的正常运行，缓存图文信息及进行统计，需要您授权开启存储权限；\n" +
                "3,文件权限：在您使用鑫合家园的鑫合圈发帖拍照或录制视频功能时，鑫合家园需要访问您设备上的照片媒体内容和文件；\n" +
                "4,摄像头权限：在您使用鑫合家园的鑫合圈发帖拍照或录制视频功能时，需要您授权开启摄像头权限；\n" +
                "5,获取位置信息：在您使用鑫合圈发帖定位功能时，鑫合家园需要获取您的位置信息。\n";
        setTextHighLightWithClick(mMsgTv, url, "《鑫合家园App用户隐私政策》", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebOtherPagerActivity.class);
                intent.putExtra(WebOtherPagerActivity.LOAD_URL, AppConstants.AGREEMENT_URL);
                intent.putExtra(WebOtherPagerActivity.HEAD_TITLE, "协议");
                mContext.startActivity(intent);
            }
        });

//        mMsgTv.setText(style);
        mMsgTv.setGravity(Gravity.LEFT);
        //设置超链接为可点击状态
        mMsgTv.setMovementMethod(LinkMovementMethod.getInstance());
        //如果设置按钮的文字
        if (!TextUtils.isEmpty(mPositiveBtnStr)) {
            mPositiveBtn.setText(mPositiveBtnStr);
        }
        if (!TextUtils.isEmpty(mNegativeBtnStr)) {
            mNegativeBtn.setText(mNegativeBtnStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mPositiveBtn = (Button) findViewById(R.id.dialog_yes);
        mNegativeBtn = (Button) findViewById(R.id.dialog_no);
        mTitleTv = (TextView) findViewById(R.id.dialog_title);
        mMsgTv = (TextView) findViewById(R.id.dialog_message);
        if (mMessageGravit != 0) {
            mMsgTv.setGravity(mMessageGravit);
        }
    }

    public void setMessageGravity(int gravity) {
        mMessageGravit = gravity;
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AppConstants.IS_DIALOG_SHOW = false;

    }

    @Override
    public void show() {
        super.show();
    }


    public static void setTextHighLightWithClick(TextView tv, String text, String keyWord, View.OnClickListener listener) {
        tv.setClickable(true);
        tv.setHighlightColor(Color.TRANSPARENT);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyWord);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new MyClickText(listener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        tv.setText(s);

    }

}
