package com.example.laravelchen.toutiao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.example.laravelchen.toutiao.Interface.OnItemClickListener;
import com.example.laravelchen.toutiao.allbean.JokeBean;
import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Random;
import java.util.zip.CRC32;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class VideoActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private View rootView;
    private PlayerView player;
    private Intent data;
    private Toolbar toolbar;
    private SharedPreferences sp;
    private String json;
    private Novate novate;
    private String share_url;
    private String mainurl;
    private String video_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        toolbar = (Toolbar) findViewById(R.id.contentToolbar);
        toolbar.setTitle("今日头条 - 图片内容");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("user_auth", Activity.MODE_PRIVATE);
        toolbar.setBackgroundColor(Color.parseColor(sp.getString("theme", "#3F51B5")));
        novate = new Novate.Builder(VideoActivity.this)
                .baseUrl("http://ib.365yg.com/")
                .build();

        data = getIntent();
        share_url = data.getStringExtra("share_url");
        video_title=data.getStringExtra("video_title");

        createThread();
    }

    private void playVideo(String url) {
        rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
        setContentView(rootView);
        player = new PlayerView(this)
                .setTitle(video_title)
                .hideSteam(true)
                .setScaleType(PlayStateParams.f4_3)
                .hideMenu(true)
                .forbidTouch(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(VideoActivity.this)
                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                .error(R.color.colorPrimary)
                                .into(ivThumbnail);
                    }
                })
                .setPlaySource(url)
                .startPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        //MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
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
                .url(share_url)
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
            final String str = msg.obj + "";
            try {
                JSONObject jsonObject = new JSONObject(parseHTML(str));
                String path = doLoadVideoData(jsonObject.optString("videoid"));
                novate.get(path.substring(path.indexOf("video")), null, new BaseSubscriber<ResponseBody>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = new String(responseBody.bytes());
                            JSONObject jp = new JSONObject(string);
                            JSONObject jp1 = jp.optJSONObject("data");
                            JSONObject jp2 = jp1.optJSONObject("video_list");
                            JSONObject jp3 = jp2.optJSONObject("video_1");
                            String base64 = jp3.optString("main_url");
                            mainurl = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                            playVideo(mainurl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            if (script.contains("var player")) {
                // 只取得script的內容
                script = e.childNode(0).toString();
//                System.out.println(script);
                // 取得JS变量数组
                String[] vars = script.split("var ");
                // 取得单个JS变量
                for (String var : vars) {
                    // 取到满足条件的JS变量
                    if (var.contains("player=")) {
                        int start = var.indexOf("=");
                        int end = var.lastIndexOf(";");
                        json = var.substring(start + 1, end + 1);
//                        System.out.println(json);
                    }
                }
            }
        }
        return json;
    }

    public String doLoadVideoData(String videoid) {
        String url = getVideoContentApi(videoid);
        return url;
    }

    private static String getVideoContentApi(String videoid) {
        String VIDEO_HOST = "http://ib.365yg.com";
        String VIDEO_URL = "/video/urls/v/1/toutiao/mp4/%s?r=%s";
        String r = getRandom();
        String s = String.format(VIDEO_URL, videoid, r);
        // 将/video/urls/v/1/toutiao/mp4/{videoid}?r={Math.random()} 进行crc32加密
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes());
        String crcString = crc32.getValue() + "";
        String url = VIDEO_HOST + s + "&s=" + crcString;
        return url;
    }

    private static String getRandom() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
