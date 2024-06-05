package com.netease.nim.uikit.business.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.netease.nim.uikit.R;


public class InputDialog extends Dialog {
	// private View content;
	private EditText editText;
	private Button positiveButton, negativeButton;
	private TextView title;

	public InputDialog(Context context, int theme) {
		super(context, theme);
	}

	public InputDialog(Context context) {
		this(context, android.R.style.Theme_Panel);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// content = View.inflate(getContext(), R.layout.mydialog, null);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nim_input_dialog);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		// getWindow().getDecorView().getDisplay().getSize(outSize);
		lp.gravity = Gravity.CENTER;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.dimAmount = 0.5f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initCustomDialog();
	}

	private void initCustomDialog() {
		title = (TextView) findViewById(R.id.tv_title);
		editText = (EditText) findViewById(R.id.et_input);
		if (!TextUtils.isEmpty(hint))
			editText.setHint(hint);
		if (!TextUtils.isEmpty(texts))
			editText.setText(texts);
		if (texts!=null){
			editText.setSelection(texts.length());
		}
		positiveButton = (Button) findViewById(R.id.btn_positive);
		positiveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (null != mOnPositiveListener)
					mOnPositiveListener.onClick(InputDialog.this);
			}
		});
		negativeButton = (Button) findViewById(R.id.btn_negative);
		negativeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (null != mOnNegativeListener)
					mOnNegativeListener.onClick(InputDialog.this);
			}
		});
	}

	private OnClickListener mOnPositiveListener;
	private OnClickListener mOnNegativeListener;

	/**
	 * 确定键监听器
	 * 
	 * @param listener
	 */
	public InputDialog setOnPositiveListener(OnClickListener listener) {
		mOnPositiveListener = listener;
		return this;
	}

	/**
	 * 取消键监听器
	 * 
	 * @param listener
	 */
	public InputDialog setOnNegativeListener(OnClickListener listener) {
		mOnNegativeListener = listener;
		return this;
	}

	public interface OnClickListener {
		void onClick(InputDialog dialog);
	}

	public Editable getText() {
		return editText.getText();
	}


	private CharSequence hint;
	private CharSequence texts;

	public InputDialog setHint(CharSequence chars) {
		hint = chars;
		return this;
	}

	public InputDialog setText(CharSequence chars) {
		texts = chars;
		return this;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

}