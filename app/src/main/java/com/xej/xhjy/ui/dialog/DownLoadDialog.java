package com.xej.xhjy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.view.CustomProgressBar;

/**
 * @author dazhi
 * @class ClubSingleBtnDialog
 * @Createtime 2018/6/11 10:42
 * @description 只有确定按钮的提示框
 * @Revisetime
 * @Modifier
 */
public class DownLoadDialog extends Dialog {

    private String titleStr;//从外界设置的title文本
    private CustomProgressBar customProgressBar;
    private Context context;
    private ProgressBar progressBar;

    public DownLoadDialog(Context context) {
        super(context, R.style.ClubDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_club);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
    }


    /**
     * 初始化界面控件
     */
    private void initView() {
        customProgressBar = findViewById(R.id.customProgressBar);
        progressBar = findViewById(R.id.progressbar);
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
     * 传入进度值
     * @param progress
     */
    public void setProgress(int progress) {
        progressBar.setProgress(progress);
        customProgressBar.setProgress(progress);
        customProgressBar.setText(progress+"%");
        customProgressBar.postInvalidate();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AppConstants.IS_DIALOG_SHOW = false;
    }

    @Override
    public void show() {
        if (!AppConstants.IS_DIALOG_SHOW){
//            AppConstants.IS_DIALOG_SHOW = true;
            super.show();
        }
    }
}
