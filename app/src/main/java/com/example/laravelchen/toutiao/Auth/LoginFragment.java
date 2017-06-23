package com.example.laravelchen.toutiao.Auth;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.laravelchen.toutiao.Constant;
import com.example.laravelchen.toutiao.MainActivity;
import com.example.laravelchen.toutiao.R;
import com.example.laravelchen.toutiao.UserLoginAndRegister;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.ResponseCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    private EditText email;
    private EditText password;
    private Novate novate;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);

        //初始化novate
        novate = new Novate.Builder(getContext())
                .baseUrl("http://cxboss.cn/laravelandroid/public/api/")
                .build();

        view.findViewById(R.id.register).setOnClickListener(this);
        view.findViewById(R.id.fanhui).setOnClickListener(this);
        view.findViewById(R.id.login).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                getFragmentManager().beginTransaction().replace(R.id.usermainfragment, new RegisterFragment()).commit();
                break;
            case R.id.fanhui:
                getActivity().finish();
                break;
            case R.id.login:
                login(email.getText().toString(), password.getText().toString());
                break;
        }
    }

    private void login(String emailstr, String passwordstr) {
        Boolean is_validate = validate(emailstr, passwordstr);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", emailstr);
        map.put("password", passwordstr);
        if (is_validate) {
            novate.rxPost("user_login", map, new ResponseCallback<Object, ResponseBody>() {
                @Override
                public Object onHandleResponse(ResponseBody response) throws Exception {
                    String string = new String(response.bytes());
                    JSONObject user = new JSONObject(string);
                    JSONObject userinfo = new JSONObject(user.getString("user"));
                    if (user.optBoolean("message")) {
                        SharedPreferences sp = getActivity().getSharedPreferences("user_auth", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("id", userinfo.optInt("id"));
                        editor.putString("name", userinfo.optString("name"));
                        editor.putInt("followers", userinfo.optInt("followers"));
                        editor.putInt("following", userinfo.optInt("following"));
                        editor.putString("api_token", userinfo.optString("api_token"));
                        editor.putString("created_at", userinfo.optString("created_at"));
                        editor.putString("introduction", userinfo.optString("introduction"));
                        editor.putString("avatar", userinfo.optString("avatar"));
                        editor.putString("sex", userinfo.optString("sex"));
                        editor.putString("birthday", userinfo.optString("birthday"));
                        editor.putBoolean("message", user.optBoolean("message"));
                        editor.commit();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                    return null;
                }

                @Override
                public void onError(Object tag, Throwable e) {

                }

                @Override
                public void onCancel(Object tag, Throwable e) {

                }

                @Override
                public void onNext(Object tag, Call call, Object response) {

                }
            });
        }
    }

    private Boolean validate(String emailstr, String passwordstr) {
        String rex = "[\\w-]+[\\.\\w]*@[\\w]+(\\.[\\w]{2,3})";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(emailstr);
        if (m.find() == false) {
            email.setError("邮箱地址不正确");
            return false;
        }
        if (passwordstr.length() < 6 || passwordstr.length() > 15) {
            password.setError("密码长度为6~15");
            return false;
        }
        return true;
    }
}
