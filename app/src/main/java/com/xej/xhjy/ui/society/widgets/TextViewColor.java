package com.xej.xhjy.ui.society.widgets;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;


/**
 * @ProjectName: XHJY_NEW_UAT
 * @Package: com.xej.xhjy.ui.society.widgets
 * @ClassName: TextViewColor
 * @Description: 文本重点描述
 * @Author: lihy_0203
 * @CreateDate: 2019/5/15 上午11:37
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/15 上午11:37
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TextViewColor {
    public static CharSequence matcherSearchText(int color, String string, String keyWord) {
        if (TextUtils.isEmpty(keyWord)){
            return "";
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(string);
        int indexOf = string.indexOf(keyWord);
        if (indexOf != -1) {
            builder.setSpan(new ForegroundColorSpan(color), indexOf, indexOf + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
}
