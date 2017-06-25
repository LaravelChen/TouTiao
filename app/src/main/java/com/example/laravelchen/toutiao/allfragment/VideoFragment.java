package com.example.laravelchen.toutiao.allfragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laravelchen.toutiao.ArticleContentShow;
import com.example.laravelchen.toutiao.Constant;
import com.example.laravelchen.toutiao.Interface.OnItemClickListener;
import com.example.laravelchen.toutiao.PathRandom;
import com.example.laravelchen.toutiao.R;
import com.example.laravelchen.toutiao.VideoActivity;
import com.example.laravelchen.toutiao.alladapter.RecommendAdapter;
import com.example.laravelchen.toutiao.alladapter.VideoAdapter;
import com.example.laravelchen.toutiao.allbean.RecommendBean;
import com.example.laravelchen.toutiao.allbean.VideoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private static String PATH = new PathRandom().getVideoPath();
    private List<VideoBean> newList;
    private VideoAdapter adapter;
    private RecyclerView rv;
    private VideoBean mainAdapterBean;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_video, container, false);
        initView();
        return view;
    }

    public void initView() {
        newList = new ArrayList<VideoBean>();
        adapter = new VideoAdapter(newList, getContext());
        rv = (RecyclerView) view.findViewById(R.id.rv);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.setRefreshing(true);
        createThread();
        seeRefresh();
    }

    //设置刷新
    public void seeRefresh() {
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        //监听刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createThread();
                PATH = new PathRandom().getVideoPath();
            }
        });
    }


    //创建进程
    public void createThread() {
        new Thread() {
            public void run() {
                postJson();
            }
        }.start();
    }

    //获取数据
    private void postJson() {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, "video");
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(PATH)
                .header("User-Agent", Constant.USER_AGENT_MOBILE)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                String content = response.body().string();
                Message msg = new Message();
                msg.obj = content;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //数据处理
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String str = msg.obj + "";
            JSONObject root = null;
            try {
                root = new JSONObject(str);
                JSONArray ary = root.getJSONArray("data");
                for (int i = 0; i < ary.length() - 1; i++) {
                    JSONObject root1 = ary.getJSONObject(i);
                    mainAdapterBean = new VideoBean();
                    mainAdapterBean.setTitle(root1.optString("title"));
                    mainAdapterBean.setComment_count(root1.optString("comments_count"));
                    mainAdapterBean.setSource(root1.optString("source"));
                    mainAdapterBean.setImage_url(root1.optString("image_url"));
                    mainAdapterBean.setSource_url(root1.optString("source_url"));
                    newList.add(0, mainAdapterBean);
                }
                adapter.add(newList);
                //设置点击事件
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(getContext(), VideoActivity.class);
                        i.putExtra("share_url", "http://www.365yg.com" + newList.get(position).getSource_url());
                        i.putExtra("video_title",newList.get(position).getTitle());
                        startActivity(i);
                    }
                });
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
