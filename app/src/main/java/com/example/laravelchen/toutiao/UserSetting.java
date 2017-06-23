package com.example.laravelchen.toutiao;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.dou361.dialogui.listener.DialogUIListener;
import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class UserSetting extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tvname;
    private TextView tvinfo;
    private TextView tvsex;
    private TextView tvbirthday;
    private BottomSheetDialog dialog;
    private TimePickerView pvTime;
    private String name;
    private String info;
    private String sex;
    private String birthday;
    private View view;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Novate novate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("今日头条 - 个人设置");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);


        //初始化novate
        novate = new Novate.Builder(UserSetting.this)
                .baseUrl("http://cxboss.cn/laravelandroid/public/api/")
                .build();

        //初始化Textview
        tvname = (TextView) findViewById(R.id.name);
        tvinfo = (TextView) findViewById(R.id.info);
        tvsex = (TextView) findViewById(R.id.sex);
        tvbirthday = (TextView) findViewById(R.id.date);

        //初始化信息
        sp = getSharedPreferences("user_auth", Activity.MODE_PRIVATE);
        editor = sp.edit();
        toolbar.setBackgroundColor(Color.parseColor(sp.getString("theme", "#3F51B5")));
        name = sp.getString("name", "无");
        sex = sp.getString("sex", "男");
        info = sp.getString("introduction", "");
        birthday = sp.getString("birthday", "无");
        tvname.setText(name);
        tvinfo.setText(info);
        tvsex.setText(sex);
        tvbirthday.setText(birthday);

        //初始化View
        findViewById(R.id.lineinfo).setOnClickListener(this);
        findViewById(R.id.linedate).setOnClickListener(this);
        findViewById(R.id.linename).setOnClickListener(this);
        findViewById(R.id.linesex).setOnClickListener(this);

        //初始化日期选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                birthday = format.format(date);
                tvbirthday.setText(format.format(date));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("birthday", birthday);
                map.put("id", sp.getInt("id", 0));
                novate.post("set_birthday", map, new BaseSubscriber<ResponseBody>() {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        editor.putString("birthday", birthday);
                        editor.commit();
                    }
                });
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).isCenterLabel(false).build();
    }

    //视图点击加载
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lineinfo:
                info_choose();
                break;
            case R.id.linedate:
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
            case R.id.linename:
                name_choose();
                break;
            case R.id.linesex:
                sex_choose();
                break;
        }
    }

    //信息选择器
    private void info_choose() {
        DialogUIUtils.showAlert(UserSetting.this, null, null, "Your info", null, "确定", "取消", false, true, true, new DialogUIListener() {

            @Override
            public void onPositive() {
                System.out.println("yes");
            }

            @Override
            public void onNegative() {
                System.out.println("cancle");
            }

            public void onGetInput(CharSequence input1, CharSequence input2) {
                info = input1.toString();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("introduction", info);
                map.put("id", sp.getInt("id", 0));
                tvinfo.setText(info);
                novate.post("set_info", map, new BaseSubscriber<ResponseBody>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        editor.putString("introduction", info);
                        editor.commit();
                    }
                });
            }

        }).show();

    }

    //名字选择器
    public void name_choose() {
        DialogUIUtils.showAlert(UserSetting.this, null, null, "Your Name", null, "确定", "取消", false, true, true, new DialogUIListener() {

            @Override
            public void onPositive() {
                System.out.println("yes");
            }

            @Override
            public void onNegative() {
                System.out.println("cancle");
            }

            public void onGetInput(CharSequence input1, CharSequence input2) {
                name = input1.toString();
                if (name.length() > 3 && name.length() < 20) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", name);
                    map.put("id", sp.getInt("id", 0));
                    novate.post("set_name", map, new BaseSubscriber<ResponseBody>() {
                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                String string = new String(responseBody.bytes());
                                JSONObject jsonObject = new JSONObject(string);
                                if (!jsonObject.getBoolean("message")) {
                                    Toast.makeText(UserSetting.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    tvname.setText(name);
                                    editor.putString("name", name);
                                    editor.commit();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Toast.makeText(UserSetting.this, "用户名长度应为3~20", Toast.LENGTH_SHORT).show();
                }
            }
        }).show();
    }

    //性别选择器
    public void sex_choose() {
        List<TieBean> data = new ArrayList<TieBean>();
        data.add(0, new TieBean("男"));
        data.add(1, new TieBean("女"));
        DialogUIUtils.showSheet(this, data, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                sex = text.toString();
                tvsex.setText(sex);
                editor.putString("sex", sex);
                editor.commit();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sex", sex);
                map.put("id", sp.getInt("id", 0));
                novate.post("set_sex", map, new BaseSubscriber<ResponseBody>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = new String(responseBody.bytes());
                            System.out.println(string);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }).show();
    }
}
