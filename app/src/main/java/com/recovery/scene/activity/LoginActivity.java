package com.recovery.scene.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.recovery.netwrok.apiservice.ApiService;
import com.recovery.netwrok.commoninfo.UserInfoUtil;
import com.recovery.netwrok.model.UserInfo;
import com.recovery.netwrok.subscriber.ApiSubscriber;
import com.recovery.scene.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import hotjavi.lei.com.base_module.activity.BaseTopActivity;
import hotjavi.lei.com.base_module.present.BaseObjectPresent;

/**
 * Created by tom on 2017/4/20.
 */

public class LoginActivity extends BaseTopActivity {

    @BindView(R.id.tv_account)
    EditText tvAccount;
    @BindView(R.id.tv_pwd)
    EditText tvPwd;
    private Present present;
    private View tvpower;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
       tvpower= findViewById(R.id.tv_power);
        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput(tvAccount, "用户名") && checkInput(tvPwd, "密码")) {
                    showLoading("登录中");
                    present = new Present(LoginActivity.this);
                    present.Login(tvAccount.getText().toString(), tvPwd.getText().toString());
                }
            }
        });

    }

    private boolean checkInput(EditText et, String type) {
        if (et.getText().toString().trim().length() == 0) {
            Toast.makeText(this, type + "不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private static class Present extends BaseObjectPresent<LoginActivity> {
        public Present(LoginActivity loginActivity) {
            super(loginActivity);
        }

        public void Login(String account, String pwd) {
            ApiService.login(account, pwd).subscribe(new ApiSubscriber<UserInfo>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                }

                @Override
                public void onNext(UserInfo userInfo) {
                    super.onNext(userInfo);
                    if (!isAvaiable() || userInfo == null) return;
                    UserInfoUtil.putUserInfo(userInfo);
                    if (getRefObj().tvpower==null) {
                        getRefObj().startActivity(new Intent(getRefObj(), SelectFuncActivity.class));
                    }else {
                        getRefObj().startActivity(new Intent(getRefObj(), PackageLandActivity.class));
                    }
                    getRefObj().finish();
                    getRefObj().hideLoading();
                }
            });
        }


    }
}
