package com.recovery.scene.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.recovery.scene.R;

import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;

/**
 * Created by tom on 2017/4/18.
 */

public class MainActivity extends BaseSetMainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCustomToolBar().setTitle("主页");
        setContentView(R.layout.activity_main);
    }

    public void gotoCamera(View view) {
        startActivity(new Intent(this,ProductsCollectActivity.class));
    }
}
