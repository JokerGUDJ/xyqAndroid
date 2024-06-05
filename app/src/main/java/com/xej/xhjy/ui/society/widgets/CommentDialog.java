package com.xej.xhjy.ui.society.widgets;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.ui.society.bean.PostListBean;
import com.xej.xhjy.ui.society.mvp.presenter.CirclePresenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.widgets.dialog
 * @ClassName: CommentDialog
 * @Description: 评论长按对话框，保护复制和删除
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CommentDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private CirclePresenter mPresenter;
    private PostListBean.ContentBean.ReplyListBean mCommentItem;
    private int mCirclePosition;

    public CommentDialog(Context context, CirclePresenter presenter,
                         PostListBean.ContentBean.ReplyListBean commentItem, int circlePosition) {
        super(context, R.style.comment_dialog);
        mContext = context;
        this.mPresenter = presenter;
        this.mCommentItem = commentItem;
        this.mCirclePosition = circlePosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Window dialogWindow = getWindow();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        TextView copyTv = (TextView) findViewById(R.id.copyTv);
        copyTv.setOnClickListener(this);
        TextView deleteTv = (TextView) findViewById(R.id.deleteTv);
        deleteTv.setVisibility(View.VISIBLE);
        deleteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyTv:
                if (mCommentItem != null) {
                    String  str = "";
                    try {
                        str = URLDecoder.decode(mCommentItem.getContent(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                    LogUtils.dazhiLog("复制--------->"+str);
                    // 获取系统剪贴板
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
                    ClipData clipData = ClipData.newPlainText(null, str);
                    // 把数据集设置（复制）到剪贴板
                    clipboard.setPrimaryClip(clipData);
                }
                dismiss();
                break;
            case R.id.deleteTv:
                if (mPresenter != null && mCommentItem != null) {
                    mPresenter.deleteComment((BaseActivity) mContext, mCirclePosition, mCommentItem.getId());
                }
                dismiss();
                break;
            default:
                break;
        }
    }

}
