package com.netease.nim.uikit.business.team.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.team.Bean.ContactGroupBean;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.utils.ImUtils;

import java.util.List;

public class ContactSelectAvatarAdapter extends BaseAdapter {
    private Context context;

    private List<ContactGroupBean.ContentBean> selectedContactItems;

    public ContactSelectAvatarAdapter(Context context, List<ContactGroupBean.ContentBean> list) {
        this.context = context;
        this.selectedContactItems = list;
    }

    @Override
    public int getCount() {
        return selectedContactItems == null?0: selectedContactItems.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedContactItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HeadImageView imageView;
        TextView tv_userName;
        ContactGroupBean.ContentBean item = selectedContactItems.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.nim_contact_select_area_item, null);
            imageView = (HeadImageView) convertView.findViewById(R.id.contact_select_area_image);
            tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
            GalleryItemViewHolder holder = new GalleryItemViewHolder();
            holder.imageView = imageView;
            holder.tv_userName = tv_userName;
            convertView.setTag(holder);
        } else {
            GalleryItemViewHolder holder = (GalleryItemViewHolder) convertView.getTag();
            imageView = holder.imageView;
            tv_userName = holder.tv_userName;
        }

        try {
            if (item == null) {
                imageView.setBackgroundResource(R.drawable.nim_contact_select_dot_avatar);
                imageView.setImageDrawable(null);
            } else {
                tv_userName.setText(item.getName());
                String url = ImUtils.HEAD_IMAG_URL + item.getCenterId() + ".jpg";
                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_user_default_icon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(context.getApplicationContext())
                        .load(url)
                        .apply(requestOptions)
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public ContactGroupBean.ContentBean remove(int pos) {
        return this.selectedContactItems.remove(pos);
    }


    class GalleryItemViewHolder {
        HeadImageView imageView;
        TextView tv_userName;
    }
}
