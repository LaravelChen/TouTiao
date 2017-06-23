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
import com.example.laravelchen.toutiao.allbean.JokeBean;
import com.example.laravelchen.toutiao.extra.ImgLoader;

import java.util.Collections;
import java.util.List;

/**
 * Created by LaravelChen on 2017/6/11.
 */

public class JokeAdapter extends RecyclerView.Adapter {

    private List<JokeBean> lists;

    private Context context;
    public JokeAdapter(List<JokeBean> lists,Context context) {
        this.lists = lists;
        this.context=context;
    }

    public void add(List<JokeBean> newlist) {
        Collections.addAll(newlist);
    }
    private OnItemClickListener onItemClickListener;
    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView jokeExtra;
        private ImageView jokeImg1;
        private TextView jokename;
        private TextView jokecontent;
        private OnItemClickListener onItemClickListener;
        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            jokeImg1 = (ImageView) itemView.findViewById(R.id.joke_image);
            jokeExtra = (TextView) itemView.findViewById(R.id.joke_extra);
            jokename = (TextView) itemView.findViewById(R.id.joke_name);
            jokecontent=(TextView) itemView.findViewById(R.id.joke_content);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joke_main, parent, false);
        return new ViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        vh.jokecontent.setText(lists.get(position).getText());
        vh.jokeExtra.setText(lists.get(position).getSource() + " - " + lists.get(position).getComment_count() + "条评论");
        new ImgLoader(context).disPlayimg(lists.get(position).getAvatar_url(),vh.jokeImg1);
        vh.jokename.setText(lists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
