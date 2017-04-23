package com.recovery.scene.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.recovery.netwrok.commoninfo.UserInfoUtil;
import com.recovery.scene.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;

/**
 * Created by tom on 2017/4/22.
 */

public class SelectFuncActivity extends BaseSetMainActivity {
    Timer timer;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @BindView(R.id.tv_user_info)
    TextView tvUserInfo;
    @BindView(R.id.product_collect)
    TextView productCollect;
    @BindView(R.id.tv_package)
    TextView tvPackage;
    @BindView(R.id.tv_collect_record)
    TextView tvCollectRecord;
    @BindView(R.id.tv_currentTime)
    TextView tvCurrentTime;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_selcet_func);
        ButterKnife.bind(this);
        getCustomToolBar().setTitle("采收功能选择");
        getCustomToolBar().setBackVisble(false,null);
        getCustomToolBar().setRightIcon(R.mipmap.ic_logout);
        getCustomToolBar().setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog==null) {
                    alertDialog = new AlertDialog.Builder(SelectFuncActivity.this).setCancelable(true)
                            .setTitle("是否退出登录").setPositiveButton("退出登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserInfoUtil.putUserInfo(null);
                                    startActivity(new Intent( SelectFuncActivity.this,LoginActivity.class));
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            }).create();
                }
                alertDialog.show();
            }
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (UserInfoUtil.getUserInfo()!=null&&UserInfoUtil.getUserInfo().getName()!=null)
                        tvCurrentTime.setText("您好，"+UserInfoUtil.getUserInfo().getName()+"\n"+simpleDateFormat.format(new Date()));
                    }
                });
            }
        }, 0, 1000);

    }

    @OnClick({R.id.tv_user_info, R.id.product_collect, R.id.tv_package, R.id.tv_collect_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user_info:
                startActivity(new Intent(this,BussinessInfoActivity.class));
                break;
            case R.id.product_collect:
                startActivity(new Intent(this,ProductsCollectActivity.class));

                break;
            case R.id.tv_package:
                startActivity(new Intent(this,PackageActivity.class));

                break;
            case R.id.tv_collect_record:
                startActivity(new Intent(this,CollectHistoryActivity.class));

                break;
        }
    }
}
