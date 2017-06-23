package com.example.laravelchen.toutiao.Auth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {


    private EditText name;
    private EditText email;
    private EditText password;
    private Novate novate;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);

        //初始化novate
        novate = new Novate.Builder(getContext())
                .baseUrl("http://cxboss.cn/laravelandroid/public/api/")
                .build();


        view.findViewById(R.id.signin).setOnClickListener(this);
        view.findViewById(R.id.register).setOnClickListener(this);
        view.findViewById(R.id.fanhui).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin:
                getFragmentManager().beginTransaction().replace(R.id.usermainfragment, new LoginFragment()).commit();
                break;
            case R.id.register:
                register(name.getText().toString(), email.getText().toString(), password.getText().toString());
                break;
            case R.id.fanhui:
                getActivity().finish();
                break;
        }
    }

    private void register(String namestr, String emailstr, String paswordstr) {
        Boolean is_validate = validate(namestr, emailstr, paswordstr);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", namestr);
        map.put("email", emailstr);
        map.put("password", paswordstr);
        //进行post请求
        if (is_validate) {
            novate.rxPost("user_register", map, new ResponseCallback<Object, ResponseBody>() {
                @Override
                public Object onHandleResponse(ResponseBody response) throws Exception {
                    String string = new String(response.bytes());
                    JSONObject user = new JSONObject(string);
                    if (user.optBoolean("message")) {
                        getFragmentManager().beginTransaction().replace(R.id.usermainfragment, new LoginFragment()).commit();
                        Toast.makeText(getContext(), "注册成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(user.optString("type"));
                        if (user.optString("type").equals("name")) {
                            System.out.println("hello");
                            name.setError(user.optString("info"));
                        }
                        if (user.optString("type").equals("email")) {
                            email.setError(user.optString("info"));
                        }
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
                public void onNext(Object tag, okhttp3.Call call, Object response) {

                }
            });
        }
    }

    //验证字符格式
    public boolean validate(String namestr, String emailstr, String paswordstr) {
        String rex = "[\\w-]+[\\.\\w]*@[\\w]+(\\.[\\w]{2,3})";
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(emailstr);
        if (namestr.length() < 3 || namestr.length() > 20) {
            name.setError("用户名长度为3~20");
            return false;
        }
        if (m.find() == false) {
            email.setError("邮箱地址不正确");
            return false;
        }
        if (paswordstr.length() < 6 || paswordstr.length() > 15) {
            password.setError("密码长度为6~15");
            return false;
        }
        return true;
    }
}
