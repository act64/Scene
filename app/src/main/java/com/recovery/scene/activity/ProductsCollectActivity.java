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

public class ProductsCollectActivity extends BaseSetMainActivity {
    private static final int THUMBNAIL_ACTIVITY = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_prodcts_collects);
        getCustomToolBar().setTitle("产品采收");
        getCustomToolBar().setBackVisble(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void gotophotos(View view) {
        Intent intent=new Intent(this,PhotoSelectedThumbnailActivity.class);
        intent.putExtra("maxImageSelectCount", 9);
        startActivityForResult(intent, THUMBNAIL_ACTIVITY);
    }
}
