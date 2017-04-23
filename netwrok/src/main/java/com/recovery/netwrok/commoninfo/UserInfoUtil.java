package com.recovery.netwrok.commoninfo;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.recovery.netwrok.model.UserInfo;

/**
 * Created by tom on 2017/4/23.
 */

public class UserInfoUtil {
    private static final String KEY_SPNAME="userINFO_SPNAME";
   private static Gson gson = new Gson();

    private static final String KEY_USERINFO="userINFO";
    private static UserInfo mUserInfo;
    public static void putUserInfo(UserInfo userInfo){
        if (userInfo!=null) {
            mUserInfo=userInfo;
            new SPUtils(KEY_SPNAME).put(KEY_USERINFO, gson.toJson(userInfo));
        }else {
            mUserInfo=null;
            new SPUtils(KEY_SPNAME).put(KEY_USERINFO,"");
        }
    }

    public static UserInfo getUserInfo(){
        if (mUserInfo==null) {
            String myinfostr = new SPUtils(KEY_SPNAME).getString(KEY_USERINFO);
            if (TextUtils.isEmpty(myinfostr)) {
                return null;
            }
            mUserInfo= gson.fromJson(myinfostr, UserInfo.class);
        }
        return mUserInfo;

    }
}
