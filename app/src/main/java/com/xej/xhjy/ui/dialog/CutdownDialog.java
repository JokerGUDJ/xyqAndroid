package com.xej.xhjy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.xej.xhjy.R;

public class CutdownDialog extends Dialog {

    private Context context;
    public CutdownDialog(@NonNull Context context) {
        super(context, R.style.ClubDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cutdown);
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        //initView();
    }
}
