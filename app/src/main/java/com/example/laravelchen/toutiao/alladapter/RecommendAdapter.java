package com.example.laravelchen.toutiao.alladapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laravelchen.toutiao.Interface.OnItemClickListener;
import com.example.laravelchen.toutiao.R;
import com.example.laravelchen.toutiao.allbean.RecommendBean;
import com.example.laravelchen.toutiao.extra.ImgLoader;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * Created by LaravelChen on 2017/6/10.
 */

public class RecommendAdapter extends RecyclerView.Adapter {
    private List<RecommendBean> lists;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public RecommendAdapter(List<RecommendBean> lists, Context context) {
        this.lists = lists;
        this.context=context;
    }

    public void add(List<RecommendBean> newlist) {
        Collections.addAll(newlist);
    }

    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    //有图片的模板
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView hotTitle;
        private TextView hotExtra;
        private ImageView hotImg1;
        private ImageView hotImg2;
        private ImageView hotImg3;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            hotTitle = (TextView) itemView.findViewById(R.id.hot_title);
            hotExtra = (TextView) itemView.findViewById(R.id.hot_extra);
            hotImg1 = (ImageView) itemView.findViewById(R.id.hot_image);
            hotImg2 = (ImageView) itemView.findViewById(R.id.hot_image2);
            hotImg3 = (ImageView) itemView.findViewById(R.id.hot_image3);

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

    //没有图片的模板
    class NoImageView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView hotTitle;
        private TextView hotExtra;
        private OnItemClickListener onItemClickListener = null;

        public NoImageView(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            hotTitle = (TextView) itemView.findViewById(R.id.hot_title);
            hotExtra = (TextView) itemView.findViewById(R.id.hot_extra);

            //点击事件
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
    public int getItemViewType(int position) {
        //判断有没有图片
        if (lists.get(position).isHasimg()) {
            return 1;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_home_has_image, parent, false);
            return new ViewHolder(view, onItemClickListener);
        }
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_home_no_image, parent, false);
            return new NoImageView(view, onItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) holder;
            vh.hotTitle.setText(lists.get(position).getTitle());
            vh.hotExtra.setText(lists.get(position).getSource() + " - " + lists.get(position).getComment_couont() + "条评论");
            if (lists.get(position).getImg() != null) {
                new ImgLoader(context).disPlayimg(lists.get(position).getImg(),vh.hotImg1);
                new ImgLoader(context).disPlayimg(lists.get(position).getImg2(),vh.hotImg2);
                new ImgLoader(context).disPlayimg(lists.get(position).getImg3(),vh.hotImg3);
            }
        }
        if (holder instanceof NoImageView) {
            NoImageView vh = (NoImageView) holder;
            vh.hotTitle.setText(lists.get(position).getTitle());
            vh.hotExtra.setText(lists.get(position).getSource() + " - " + lists.get(position).getComment_couont() + "条评论");
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
