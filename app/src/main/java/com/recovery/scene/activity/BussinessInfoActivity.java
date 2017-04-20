package com.recovery.scene.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.recovery.scene.R;
import com.recovery.scene.utils.ImageLoadManager;
import com.recovery.scene.utils.ImageManager;

import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;

/**
 * Created by tom on 2017/4/20.
 */

public class BussinessInfoActivity extends BaseSetMainActivity{
    LinearLayout llPics;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_bussiness_info);
        getCustomToolBar().setTitle("商户信息");
        llPics= (LinearLayout) findViewById(R.id.ll_pics);

        createImage("http://placekitten.com/500/300");
        createImage("http://placekitten.com/400/300");
        createImage("http://placekitten.com/300/500");


    }

    private void createImage(String url){
        ImageView iv=new ImageView(this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        llPics.addView(iv);
        ImageLoadManager.loadImageByDefaultImage(url,iv,R.drawable.farmer_bg_select_image);

    }
}
