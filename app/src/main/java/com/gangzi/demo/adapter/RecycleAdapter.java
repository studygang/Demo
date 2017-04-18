package com.gangzi.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gangzi.demo.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Administrator on 2017/4/12.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{

    private Context mcontext;
    private int[]iamges;

    public RecycleAdapter(Context mcontext,int[]iamges) {
       this.mcontext=mcontext;
        this.iamges=iamges;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (holder==null){
            View view= LayoutInflater.from(mcontext).inflate(R.layout.item_recycle,null);
            holder=new ViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       Glide.with(mcontext).load(iamges[position]).into(holder.mImageView);
       // holder.mImageView.setImageResource(iamges[position]);
    }

    @Override
    public int getItemCount() {
        return iamges.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImageView;
        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.image);
            tv_title= (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}
