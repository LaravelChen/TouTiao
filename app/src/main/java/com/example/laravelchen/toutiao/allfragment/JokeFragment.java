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
import com.example.laravelchen.toutiao.JokeContent;
import com.example.laravelchen.toutiao.R;
import com.example.laravelchen.toutiao.alladapter.JokeAdapter;
import com.example.laravelchen.toutiao.alladapter.VideoAdapter;
import com.example.laravelchen.toutiao.allbean.JokeBean;
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
public class JokeFragment extends Fragment {

    private static final String PATH = "https://www.toutiao.com/api/article/feed/?category=essay_joke&utm_source=toutiao&max_behot_time=0&as=A105177907376A5&cp=5797C7865AD54E1";
//    private static  String PATH="http://www.toutiao.com/api/article/feed/?category=essay_joke&utm_source=toutiao&widen=1&max_behot_time=1498129739&max_behot_time_tmp=1498129739&tadrequire=true&as=A1B5A974DB6A55D&cp=594B0A25450D8E1";
    private List<JokeBean> newList;
    private JokeAdapter adapter;
    private RecyclerView rv;
    private JokeBean mainAdapterBean;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public JokeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wei_tou_tiao, container, false);
        initView();
        return view;
    }


    public void initView() {
        newList = new ArrayList<JokeBean>();
        adapter = new JokeAdapter(newList,getContext());
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
        RequestBody requestBody = RequestBody.create(JSON, "joke");
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(PATH)
                .header("User-Agent", Constant.USER_AGENT_MOBILE)
                .addHeader("Accept", "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
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
                    JSONObject root2 = root1.getJSONObject("group");
                    JSONObject root3 = root2.getJSONObject("user");
                    mainAdapterBean = new JokeBean();
                    mainAdapterBean.setText(root2.optString("text"));
                    mainAdapterBean.setComment_count(root2.optString("comment_count"));
                    mainAdapterBean.setSource(root2.optString("category_name"));
                    mainAdapterBean.setAvatar_url(root3.optString("avatar_url"));
                    mainAdapterBean.setName(root3.optString("name"));
                    mainAdapterBean.setShare_url(root2.optString("share_url"));
                    newList.add(0, mainAdapterBean);
                }
                adapter.add(newList);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i=new Intent(getContext(), JokeContent.class);
                        i.putExtra("text",newList.get(position).getText());
                        i.putExtra("name",newList.get(position).getName());
                        i.putExtra("source",newList.get(position).getSource());
                        i.putExtra("image_url",newList.get(position).getAvatar_url());
                        i.putExtra("comment_count",newList.get(position).getComment_count());
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
