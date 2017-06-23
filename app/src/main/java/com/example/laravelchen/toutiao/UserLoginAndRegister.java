package com.example.laravelchen.toutiao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.laravelchen.toutiao.Auth.LoginFragment;

public class UserLoginAndRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_and_register);
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.usermainfragment,new LoginFragment()).commit();
    }
}
