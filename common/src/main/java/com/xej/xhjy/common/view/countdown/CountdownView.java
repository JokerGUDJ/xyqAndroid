package com.xej.xhjy.common.view.countdown;

import android.content.Context;
import android.os.CountDownTimer;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * @author dazhi
 * @class CountdownView
 * @Createtime 2018/7/23 16:13
 * @description 倒计时控件
 * @Revisetime
 * @Modifier
 */
public class CountdownView extends AppCompatTextView {
    private CountDownTimer mTimer;

    public CountdownView(Context context) {
        super(context);
        init();
    }

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setEnabled(false);
                setText( millisUntilFinished / 1000 + "S重发");
            }

            @Override
            public void onFinish() {
                setEnabled(true);
                setText("发送");
            }
        };
    }

    public void startCountdown() {
        mTimer.start();
    }
}
