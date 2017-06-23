package com.example.laravelchen.toutiao;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.laravelchen.toutiao.allfragment.HomeFragment;
import com.example.laravelchen.toutiao.allfragment.PhotoFragment;
import com.example.laravelchen.toutiao.allfragment.UserFragment;
import com.example.laravelchen.toutiao.allfragment.VideoFragment;
import com.example.laravelchen.toutiao.allfragment.JokeFragment;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.ResponseCallback;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;
import okhttp3.Call;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener {

    private Toolbar mtoolbar;
    private HomeFragment homeFragment;
    private PhotoFragment photoFragment;
    private VideoFragment videoFragment;
    private JokeFragment weiTouTiao;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private CircleImageView imageView;
    private View headerlayout;
    private Novate novate;
    private String username;
    private int followers;
    private int following;
    private String api_token;
    private Boolean message;
    private SharedPreferences sp;
    private ShareListener mShareListener;
    private BottomSheetDialog dialog;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment = new HomeFragment();
        photoFragment = new PhotoFragment();
        videoFragment = new VideoFragment();
        weiTouTiao = new JokeFragment();

        //navgationview
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);


        //绑定headerlayout
        get_info();

        //初始化
        initView();
    }

    //获取用户信息并且判断是否登录
    private void get_info() {
        sp = getSharedPreferences("user_auth", Activity.MODE_PRIVATE);
        editor = sp.edit();
        message = sp.getBoolean("message", false);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (!message) {
            navigationView.addHeaderView(layoutInflater.inflate(R.layout.navigation_header_before, navigationView, false));
            headerlayout = navigationView.getHeaderView(0);
            imageView = (CircleImageView) headerlayout.findViewById(R.id.profile_image_before);
            imageView.setOnClickListener(this);
        } else {
            navigationView.addHeaderView(layoutInflater.inflate(R.layout.navigation_header, navigationView, false));
            headerlayout = navigationView.getHeaderView(0);
            TextView tv_header = (TextView) headerlayout.findViewById(R.id.tv_header);
            TextView followers = (TextView) headerlayout.findViewById(R.id.followers);
            TextView following = (TextView) headerlayout.findViewById(R.id.following);
            tv_header.setText(sp.getString("name", "Laravel"));
            followers.setText(sp.getInt("followers", 0) + "");
            following.setText(sp.getInt("following", 0) + "");
        }
    }


    public void initView() {
        //显示toolbar
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitle("今日头条 - 新闻");
        setSupportActionBar(mtoolbar);
        mtoolbar.setBackgroundColor(Color.parseColor(sp.getString("theme", "#3F51B5")));
        //绑定侧边栏
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mtoolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //显示底部导航
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        bottomNavigationBar.setBarBackgroundColor("#FCFCFC");
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.home2, "主页").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colorblue))
                .addItem(new BottomNavigationItem(R.mipmap.comment, "段子").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.mipmap.photo, "图片").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colororange))
                .addItem(new BottomNavigationItem(R.mipmap.play, "视频").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colorpurple2))
                .setFirstSelectedPosition(0)
                .initialise();
        setDefaultFragment();

        //初始化Listener
        ShareConfig config = ShareConfig.instance()
                .qqId("101410114")
                .weiboId("10121212")
                .wxId("12224412");
        ShareManager.init(config);

        mShareListener = new ShareListener() {
            @Override
            public void shareSuccess() {
                Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void shareFailure(Exception e) {
                Toast.makeText(MainActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void shareCancel() {
                Toast.makeText(MainActivity.this, "取消分享", Toast.LENGTH_SHORT).show();
            }
        };

        //侧边栏NavgationView的监听
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);//设置选项是否选中
                item.setCheckable(false);//选项是否可选
                switch (item.getItemId()) {
                    case R.id.item_setting:
                        if (message) {
                            startActivity(new Intent(MainActivity.this, UserSetting.class));
                        } else {
                            alert_info();
                        }
                        break;
                    case R.id.item_theme:
                        if (message) {
                            dialog = new BottomSheetDialog(MainActivity.this);
                            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.theme_choose, null);
                            dialog.setContentView(view);
                            dialog.show();
                            theme_choose(view);
                        } else {
                            alert_info();
                        }
                        break;
                    case R.id.item_love:
                        if (message) {
                        } else {
                            alert_info();
                        }
                        break;
                    case R.id.item_share:
                        if (message) {
                            dialog = new BottomSheetDialog(MainActivity.this);
                            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.user_share, null);
                            dialog.setContentView(view);
                            dialog.show();
                            user_share(view, dialog);
                        } else {
                            alert_info();
                        }
                        break;
                    case R.id.logout:
                        if (message) {
                            new AlertDialog.Builder(MainActivity.this).setTitle("退出登录").setMessage("确认退出登录吗?").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    logout();
                                }
                            }).show();
                        } else {
                            alert_info();
                        }
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        //底部导航监听事件
        bottomNavigationBar.setTabSelectedListener(this);
    }

    //主题选择
    private void theme_choose(View view) {
        view.findViewById(R.id.theme_black).setOnClickListener(this);
        view.findViewById(R.id.theme_blue).setOnClickListener(this);
        view.findViewById(R.id.theme_pink).setOnClickListener(this);
        view.findViewById(R.id.theme_purple).setOnClickListener(this);
        view.findViewById(R.id.theme_yellow).setOnClickListener(this);
    }

    //用户分享
    private void user_share(View view, final BottomSheetDialog dialog) {
        view.findViewById(R.id.share_qq).setOnClickListener(this);
        view.findViewById(R.id.share_weixin).setOnClickListener(this);
        view.findViewById(R.id.share_friend).setOnClickListener(this);
        view.findViewById(R.id.share_weibo).setOnClickListener(this);
        view.findViewById(R.id.share_zone).setOnClickListener(this);
    }

    //设置启动页
    private void setDefaultFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.maindfragment, homeFragment).commit();
    }

    //toolbar的监听


    //请先登录的提示
    public void alert_info() {
        new AlertDialog.Builder(MainActivity.this).setTitle("消息提示").setMessage("请您先登录?").setNegativeButton("确定", null).show();
    }

    //退出登录
    public void logout() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //显示这个的搜索绑定
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                System.out.println("open");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                System.out.println("close");
                return true;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    //底部导航监听事件
    @Override
    public void onTabSelected(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                mtoolbar.setTitle("今日头条 - 新闻");
                ft.replace(R.id.maindfragment, homeFragment).commit();
                break;
            case 1:
                ft.replace(R.id.maindfragment, weiTouTiao).commit();
                mtoolbar.setTitle("今日头条 - 段子");
                break;
            case 2:
                ft.replace(R.id.maindfragment, photoFragment).commit();
                mtoolbar.setTitle("今日头条 - 图片");
                break;
            case 3:
                ft.replace(R.id.maindfragment, videoFragment).commit();
                mtoolbar.setTitle("今日头条 - 视频");
                break;
        }
    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    //头像登录监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image_before:
                drawerLayout.closeDrawers();//关闭navigationview
                startActivity(new Intent(this, UserLoginAndRegister.class));//启动用户登录界面
                break;
            case R.id.share_qq:
                ShareUtil.shareImage(MainActivity.this, SharePlatform.QQ,
                        "http://shaohui.me/images/avatar.gif", mShareListener);
                dialog.hide();
                break;
            case R.id.share_weixin:
                ShareUtil.shareText(MainActivity.this, SharePlatform.WX, "分享文字", mShareListener);
                dialog.hide();
                break;
            case R.id.share_weibo:
                ShareUtil.shareImage(MainActivity.this, SharePlatform.WEIBO,
                        "http://shaohui.me/images/avatar.gif", mShareListener);
                dialog.hide();
                break;
            case R.id.share_zone:
                ShareUtil.shareMedia(MainActivity.this, SharePlatform.QZONE, "Title", "summary",
                        "http://www.google.com", "http://shaohui.me/images/avatar.gif",
                        mShareListener);
                dialog.hide();
                break;
            case R.id.share_friend:
                ShareUtil.shareText(MainActivity.this, SharePlatform.WX_TIMELINE, "测试分享文字",
                        mShareListener);
                dialog.hide();
                break;
            case R.id.theme_black:
                mtoolbar.setBackgroundColor(Color.parseColor("#000000"));
                editor.putString("theme", "#000000");
                editor.commit();
                break;
            case R.id.theme_blue:
                mtoolbar.setBackgroundColor(Color.parseColor("#3F51B5"));
                editor.putString("theme", "#3F51B5");
                editor.commit();
                break;
            case R.id.theme_pink:
                mtoolbar.setBackgroundColor(Color.parseColor("#d4237a"));
                editor.putString("theme", "#d4237a");
                editor.commit();
                break;
            case R.id.theme_purple:
                mtoolbar.setBackgroundColor(Color.parseColor("#6A5ACD"));
                editor.putString("theme", "#6A5ACD");
                editor.commit();
                break;
            case R.id.theme_yellow:
                mtoolbar.setBackgroundColor(Color.parseColor("#FF7F00"));
                editor.putString("theme", "#FF7F00");
                editor.commit();
                break;
        }
    }
}
