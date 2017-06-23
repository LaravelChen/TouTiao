package com.example.laravelchen.toutiao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.example.laravelchen.toutiao.extra.ImgLoader;
import com.example.laravelchen.toutiao.extra.ImgUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class PhotoActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String PATH;
    private Intent data;
    private String json;
    private int now;
    private String image_list;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ImageView[] imageViews;
    private List<String> imgIdArray;
    private TextView tv_count;
    private TextView tv_save;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        toolbar = (Toolbar) findViewById(R.id.contentToolbar);
        toolbar.setTitle("今日头条 - 图片内容");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        tv_count = (TextView) findViewById(R.id.image_count);
        tv_save = (TextView) findViewById(R.id.image_save);
        sp=getSharedPreferences("user_auth", Activity.MODE_PRIVATE);
        toolbar.setBackgroundColor(Color.parseColor(sp.getString("theme", "#3F51B5")));

        //初始化整个图片当前的位置（很重要的一个flag）
        now = 1;
        //设置viewpager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imgIdArray = new ArrayList<String>();
        data = getIntent();
        PATH = data.getStringExtra("share_url");
        //启动运行
        createThread();

        //点击保存
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = imgIdArray.get(now - 1);
                ImageLoader imageLoader = ImageLoader.getInstance();
                Bitmap bmp = imageLoader.loadImageSync(url);
                Boolean is_save = new ImgUtils().saveImageToGallery(PhotoActivity.this, bmp);
                if (is_save) {
                    Toast.makeText(PhotoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createThread() {
        new Thread() {
            @Override
            public void run() {
                postJson();
            }
        }.start();
    }

    private void postJson() {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, "photo");
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String str = msg.obj + "";
            JSONObject root = null;
            image_list = parseHTML(str);
            try {
                root = new JSONObject(image_list);
                JSONArray ary = root.getJSONArray("sub_images");
                for (int i = 0; i < ary.length() - 1; i++) {
                    JSONObject root1 = ary.getJSONObject(i);
                    imgIdArray.add(i, root1.optString("url"));
                }
                imageViews = new ImageView[imgIdArray.size()];
                tv_count.setText(1 + "/" + imageViews.length);
                //设置图片
                for (int i = 0; i < imageViews.length; i++) {
                    ImageView imageView = new ImageView(PhotoActivity.this);
                    imageViews[i] = imageView;
                    new ImgLoader(PhotoActivity.this).disPlayimg(imgIdArray.get(i), imageView);
                }
                viewPager.setAdapter(new MyAdapter());
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        System.out.println(position);
                        now = position + 1;
                        tv_count.setText(now + "/" + imageViews.length);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private String parseHTML(String HTML) {
        boolean flag = false;
        Document doc = Jsoup.parse(HTML);
        // 取得所有的script tag
        Elements scripts = doc.getElementsByTag("script");
        for (Element e : scripts) {
            // 过滤字符串
            String script = e.toString();
            if (script.contains("var gallery = {")) {
                // 只取得script的內容
                script = e.childNode(0).toString();
                // 取得JS变量数组
                String[] vars = script.split("var ");
                // 取得单个JS变量
                for (String var : vars) {
                    // 取到满足条件的JS变量
                    if (var.contains("gallery = ")) {
                        int start = var.indexOf("=");
                        int end = var.lastIndexOf(";");
                        json = var.substring(start + 1, end + 1);
                    }
                }
            }
        }
        return json;
    }

    public class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(imageViews[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                ((ViewPager) container).addView(imageViews[position], 0);
            } catch (Exception e) {
                //handler something
            }
            return imageViews[position];
        }
    }
}
