package com.recovery.scene.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


import com.recovery.scene.R;

import hotjavi.lei.com.base_module.activity.BaseTopActivity;

/**
 * Created by tom on 2017/4/20.
 */

public class LoginActivity extends BaseTopActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SelectFuncActivity.class));
            }
        });
    }
}
