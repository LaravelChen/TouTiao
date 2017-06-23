package com.example.laravelchen.toutiao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laravelchen.toutiao.extra.ImgLoader;


public class JokeContent extends AppCompatActivity {
    private Intent data;
    private Toolbar toolbar;
    private String image_url;
    private String name;
    private String text;
    private String source;
    private String comment_count;
    private ImageView imageView;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvExtra;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_content);
        toolbar = (Toolbar) findViewById(R.id.contentToolbar);
        toolbar.setTitle("今日头条 - 段子内容");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        sp=getSharedPreferences("user_auth", Activity.MODE_PRIVATE);
        toolbar.setBackgroundColor(Color.parseColor(sp.getString("theme", "#3F51B5")));
        initView();
    }


    private void initView() {
        data = getIntent();
        image_url = data.getStringExtra("image_url");
        name = data.getStringExtra("name");
        text = data.getStringExtra("text");
        source = data.getStringExtra("source");
        comment_count = data.getStringExtra("comment_count");
        imageView = (ImageView) findViewById(R.id.joke_image);
        tvTitle = (TextView) findViewById(R.id.joketitle);
        tvContent = (TextView) findViewById(R.id.joke_content);
        tvExtra = (TextView) findViewById(R.id.joke_extra);
        new ImgLoader(this).disPlayimg(image_url,imageView);
        tvTitle.setText(name);
        tvContent.setText(text);
        tvExtra.setText("来源:" + source + " ~ " + "共" + comment_count + "条评论");
    }
}
