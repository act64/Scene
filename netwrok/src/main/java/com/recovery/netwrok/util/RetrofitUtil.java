package com.recovery.netwrok.util;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.recovery.netwrok.commoninfo.UserInfoUtil;
import com.recovery.netwrok.exception.NetException;
import com.recovery.netwrok.model.ResponseResult;
import com.recovery.netwrok.model.UserInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tom on 2017/4/22.
 */

public class RetrofitUtil {
    private static final String baseUtl="http://dev.chnagiot.com:8648/";
    private static final long DEFAULT_TIMEOUT = 20;
    private static Retrofit retrofit;

    public static Retrofit get(){
        if (retrofit==null){
            synchronized (RetrofitUtil.class){
                if (retrofit==null){
                    OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder builder = chain.request()
                                    .newBuilder();
                           if (UserInfoUtil.getUserInfo()!=null){
                               builder.addHeader("Current-User",UserInfoUtil.getUserInfo().getId());
                           }
                            Request request = builder
                                    .build();
                            return chain.proceed(request);
                        }
                    }) .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();
                   retrofit= new Retrofit.Builder().baseUrl(baseUtl)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                }
            }
        }
        return retrofit;

    }

    private static class CheckNetFunc<T> implements Func1<T, T>{
        @Override
        public T call(T t) {
            if (!NetworkUtils.isConnected()){
                throw new NetException(NetException.NoNetWork,"请检查网络连接");
            }
            return t;
        }
    }


    private static <T> Observable<T> checkObservable(Observable<T>  originalObserval){
      return Observable.just(originalObserval).subscribeOn(Schedulers.io())
              .observeOn(Schedulers.io())
              .flatMap(new CheckNetFunc<Observable<T>>());
    }

   public static<T> Observable<T>  handleApiCall(Observable<ResponseResult<T>> resultObservable){

             return    checkObservable(resultObservable)
               .observeOn(Schedulers.io())
               .map(new Func1<ResponseResult<T>, T>() {
                   @Override
                   public T call(ResponseResult<T> tResponseResult) {
                       if (tResponseResult==null||tResponseResult.getCode()!=NetException.NetWorkOk){
                           throw new NetException(tResponseResult.getCode(),tResponseResult.getMsg());
                       }
                       return tResponseResult.getData();
                   }
               }).observeOn(AndroidSchedulers.mainThread());
   }
}
