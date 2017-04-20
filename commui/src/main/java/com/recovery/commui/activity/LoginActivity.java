package com.recovery.commui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.recovery.commui.R;

import hotjavi.lei.com.base_module.activity.BaseTopActivity;

/**
 * Created by tom on 2017/4/20.
 */

public class LoginActivity extends BaseTopActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
