package com.recovery.scene.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.recovery.netwrok.commoninfo.UserInfoUtil;

import hotjavi.lei.com.base_module.activity.BaseTopActivity;

/**
 * Created by tom on 2017/4/18.
 */

public class MainActivity extends BaseTopActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserInfoUtil.getUserInfo()==null) {
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            startActivity(new Intent(this, SelectFuncActivity.class));

        }
    }



}
