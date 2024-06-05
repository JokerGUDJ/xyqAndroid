package com.netease.nim.avchatkit.anim;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.netease.nim.avchatkit.R;

public class AnimUtils {
    public static Animation getTopInAnim(Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.top_in);
        return  animation;
    }
    public static Animation getTopOutAnim(Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.top_out);
        return  animation;
    }
    public static Animation getBottomInAnim(Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottom_in);
        return  animation;
    }
    public static Animation getBottomOutAnim(Context context){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottom_out);
        return  animation;
    }
}
