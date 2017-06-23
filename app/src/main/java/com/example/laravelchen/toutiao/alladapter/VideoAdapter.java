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
import com.example.laravelchen.toutiao.allbean.VideoBean;
import com.example.laravelchen.toutiao.extra.ImgLoader;


import java.util.Collections;
import java.util.List;

/**
 * Created by LaravelChen on 2017/6/11.
 */

public class VideoAdapter extends RecyclerView.Adapter {
    private List<VideoBean> lists;
    private Context context;
    public VideoAdapter(List<VideoBean> lists,Context context) {
        this.lists = lists;
        this.context=context;
    }
    private OnItemClickListener onItemClickListener;
    public void add(List<VideoBean> newlist) {
        Collections.addAll(newlist);
    }

    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView videoTitle;
        private TextView videoExtra;
        private ImageView videoImg1;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            videoImg1 = (ImageView) itemView.findViewById(R.id.video_image);
            videoExtra = (TextView) itemView.findViewById(R.id.video_extra);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_main, parent, false);
        return new ViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        vh.videoTitle.setText(lists.get(position).getTitle());
        vh.videoExtra.setText(lists.get(position).getSource() + " - " + lists.get(position).getComment_count() + "条评论");
        new ImgLoader(context).disPlayimg(lists.get(position).getImage_url(),vh.videoImg1);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
