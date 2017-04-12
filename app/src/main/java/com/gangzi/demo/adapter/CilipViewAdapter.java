package com.gangzi.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/4/12.
 */

public class CilipViewAdapter extends RecyclingPagerAdapter{

    private Context mContext;
    private int[]images;

    public CilipViewAdapter(Context mContext,int[]images) {
        this.mContext=mContext;
        this.images=images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ImageView imageView = null;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }
        //imageView.setTag(position);
        //imageView.setImageResource(images[position]);
      Glide.with(mContext).load(images[position]).into(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return images.length;
    }
}
