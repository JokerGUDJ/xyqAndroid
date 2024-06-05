package com.xej.xhjy.ui.society.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.utils.DateUtils;
import com.xej.xhjy.tools.Utils;
import com.xej.xhjy.ui.society.bean.PostListBean;
import com.xej.xhjy.ui.society.spannable.CircleMovementMethod;
import com.xej.xhjy.ui.society.spannable.SpannableClickable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.widgets
 * @ClassName: ColorFilterImageView
 * @Description: 评论list
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CommentListView extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<PostListBean.ContentBean.ReplyListBean> mDatas;
    private LayoutInflater layoutInflater;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<PostListBean.ContentBean.ReplyListBean> datas) {
        if (datas == null) {
            datas = new ArrayList<PostListBean.ContentBean.ReplyListBean>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<PostListBean.ContentBean.ReplyListBean> getDatas() {
        return mDatas;
    }

    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.color_black_ff333333));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.gray_text));

        } finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged() {

        removeAllViews();
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mDatas.size(); i++) {
            final int index = i;
            View view = getView(index);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    private View getView(final int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.item_comment, null, false);

        TextView commentTv = (TextView) convertView.findViewById(R.id.commentTv);
        TextView commentReplayName = convertView.findViewById(R.id.tv_name);
        ImageView commentHighlight = convertView.findViewById(R.id.img_message_highlight);
        TextView commentTime = convertView.findViewById(R.id.tv_time);
        View diviver = convertView.findViewById(R.id.diviver);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);

        if(position == 0){
            commentHighlight.setVisibility(VISIBLE);
        }
        if(position == mDatas.size() - 1){
            diviver.setVisibility(GONE);
        }
        final PostListBean.ContentBean.ReplyListBean bean = mDatas.get(position);
        String name = bean.getFrom();
        String id = bean.getId();
        String toReplyName = "";
        if (bean.getTo() != null) {
            toReplyName = bean.getTo();
        }
        commentTime.setText(DateUtils.FormatTime(bean.getCreateTime()));

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(name);

        if (!TextUtils.isEmpty(toReplyName)) {
            builder.append(setClickableSpan(" 回复 ", Color.parseColor("#494F5A")));
            builder.append(toReplyName);
        }
        //builder.append(": ");
        commentReplayName.setText(builder);
        //转换表情字符
        String contentBodyStr = bean.getContent();
        if (!TextUtils.isEmpty(contentBodyStr)) {
            try {
                contentBodyStr = URLDecoder.decode(contentBodyStr, "utf-8");
            } catch (UnsupportedEncodingException e) {
                contentBodyStr = "";
            }
        }
        commentTv.setText(contentBodyStr);
        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });
        commentTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, int color) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(color) {
                                    @Override
                                    public void onClick(View widget) {
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public static interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public static interface OnItemLongClickListener {
        public void onItemLongClick(int position);
    }


}
