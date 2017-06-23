package com.example.laravelchen.toutiao.alladapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laravelchen.toutiao.Interface.OnItemClickListener;
import com.example.laravelchen.toutiao.R;
import com.example.laravelchen.toutiao.allbean.PhotoBean;
import com.example.laravelchen.toutiao.extra.ImgLoader;


import java.util.Collections;
import java.util.List;

/**
 * Created by LaravelChen on 2017/6/12.
 */

public class PhotoAdapter extends RecyclerView.Adapter {
    private List<PhotoBean> lists;
    private Context context;

    public PhotoAdapter(List<PhotoBean> lists,Context context) {
        this.lists = lists;
        this.context=context;
    }

    private OnItemClickListener onItemClickListener;

    public void add(List<PhotoBean> newlist) {
        Collections.addAll(newlist);
    }

    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView photoTitle;
        private TextView photoExtra;
        private ImageView img1;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            photoTitle = (TextView) itemView.findViewById(R.id.photo_title);
            img1 = (ImageView) itemView.findViewById(R.id.photo_image);
            photoExtra = (TextView) itemView.findViewById(R.id.photo_extra);

            //设置点击事件
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, getLayoutPosition());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_main, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        vh.photoTitle.setText(lists.get(position).getTitle());
        vh.photoExtra.setText(lists.get(position).getSource() + " - 共" + lists.get(position).getGallary_image_count() + "张图片");
        new ImgLoader(context).disPlayimg(lists.get(position).getImg1(),vh.img1);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
