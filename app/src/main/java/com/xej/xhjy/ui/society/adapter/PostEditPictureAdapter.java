package com.xej.xhjy.ui.society.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xej.xhjy.R;
import com.xej.xhjy.common.view.draggridview.DragGridBaseAdapter;
import com.xej.xhjy.image.ImageLoadUtils;

import java.util.Collections;
import java.util.List;

/**
 * @class PostEditPictureAdapter 选择的图片展示adapter
 * @author dazhi
 * @Createtime 2018/12/3 16:50
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class PostEditPictureAdapter extends BaseAdapter implements DragGridBaseAdapter {
    private LayoutInflater mInflater;
    private List<String> mList;
    private Context context;
    private DeleteListener mListener;

    public PostEditPictureAdapter(Context context, List<String> list, DeleteListener listener) {
        this.context = context;
        this.mList = list;
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_edit_post_picture, null);
        ImageView dragImage = convertView.findViewById(R.id.item_image);
        ImageView deleteIcon = convertView.findViewById(R.id.item_delete);
        if (mList.get(position).equals("add")) {
            deleteIcon.setVisibility(View.GONE);
            dragImage.setImageResource(R.drawable.ic_edit_add);
        } else {
            deleteIcon.setVisibility(View.VISIBLE);
            ImageLoadUtils.loadImageRadius(context, mList.get(position), dragImage);
        }
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteItem(position);
            }
        });
        dragImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickItem(position);
            }
        });
        return convertView;
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        String temp = mList.get(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }

        mList.set(newPosition, temp);
    }

    @Override
    public void setHideItem(int hidePosition) {
        notifyDataSetChanged();
    }

    @Override
    public void removeItem(int removePosition) {
        mList.remove(removePosition);
        notifyDataSetChanged();
    }

    @Override
    public int getItemsCount() {
        return mList.size();
    }

    public interface DeleteListener {
        void deleteItem(int position);
        void clickItem(int position);
    }

}
