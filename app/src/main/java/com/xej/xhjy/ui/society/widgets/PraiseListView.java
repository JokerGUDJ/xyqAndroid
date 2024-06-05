package com.xej.xhjy.ui.society.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xej.xhjy.ClubApplication;
import com.xej.xhjy.R;
import com.xej.xhjy.ui.society.bean.FavortItem;
import com.xej.xhjy.ui.society.bean.PostListBean;
import com.xej.xhjy.ui.society.spannable.CircleMovementMethod;
import com.xej.xhjy.ui.society.spannable.SpannableClickable;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.widgets
 * @ClassName: PraiseListView
 * @Description: 点赞
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PraiseListView extends TextView {


    private int itemColor;
    private int itemSelectorColor;
    private List<PostListBean.ContentBean.SocialLikeBean> datas;
    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PraiseListView(Context context) {
        super(context);
    }

    public PraiseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public PraiseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.color_black_ff333333));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.color_split_line_cccccc));

        } finally {
            typedArray.recycle();
        }
    }

    public List<PostListBean.ContentBean.SocialLikeBean> getDatas() {
        return datas;
    }

    public void setDatas(List<PostListBean.ContentBean.SocialLikeBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    public void notifyDataSetChanged() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (datas != null && datas.size() > 0) {
            //添加点赞图标
            builder.append(setImageSpan());
            PostListBean.ContentBean.SocialLikeBean item = null;
            for (int i = 0; i < datas.size(); i++) {
                item = datas.get(i);
                if (item != null) {
                    builder.append(setClickableSpan(item.getLikeName(), i));
                    if (i != datas.size() - 1) {
                        builder.append("、 ");
                    }
                }
            }
        }

        setText(builder);
        setMovementMethod(new CircleMovementMethod(itemSelectorColor));
    }


    private SpannableString setImageSpan() {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(ClubApplication.getContext(), R.drawable.icon_praise, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, final int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
                                    @Override
                                    public void onClick(View widget) {
                                        if (onItemClickListener != null) {
                                            onItemClickListener.onClick(position);
                                        }
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }


    public static interface OnItemClickListener {
        public void onClick(int position);
    }
}
