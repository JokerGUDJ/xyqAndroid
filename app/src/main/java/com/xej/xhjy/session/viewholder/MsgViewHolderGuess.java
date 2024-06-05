package com.xej.xhjy.session.viewholder;

import android.widget.ImageView;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.xej.xhjy.R;
import com.xej.xhjy.session.extension.GuessAttachment;

/**
 * Created by hzliuxuanlin on 17/9/15.
 */

public class MsgViewHolderGuess extends MsgViewHolderBase {

    private GuessAttachment guessAttachment;
    private ImageView imageView;

    public MsgViewHolderGuess(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.rock_paper_scissors;
    }

    @Override
    public void inflateContentView() {
        imageView = (ImageView) view.findViewById(R.id.rock_paper_scissors_text);
    }

    @Override
    public void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        guessAttachment = (GuessAttachment) message.getAttachment();
        switch (guessAttachment.getValue().getDesc()) {
            default:
                break;
        }

    }
}
