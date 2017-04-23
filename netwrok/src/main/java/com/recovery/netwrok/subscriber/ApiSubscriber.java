package com.recovery.netwrok.subscriber;

import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.recovery.netwrok.exception.NetException;
import com.recovery.netwrok.model.ResponseResult;

import rx.Subscriber;

/**
 * Created by tom on 2017/4/23.
 */

public abstract class  ApiSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof NetException) {
            Toast.makeText(Utils.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(Utils.getContext(),"请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();

        }
        Log.e(" rx_error",Log.getStackTraceString(e));

    }

    @Override
    public void onNext(T t) {

    }
}
