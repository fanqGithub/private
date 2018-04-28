package com.commai.module_baselib.network;

import com.commai.module_baselib.constance.Constance;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fanqi on 2018/4/26.
 * Description:创建Retrofit实例，并且提供createAPi创建具体的请求代理服务。
 * client部分，可以按实际项目网络请求所需要的具体情况来定义
 */

public class RetrofitFactory {

    private static Retrofit mRetrofit;

    private static OkHttpClient createHttpClient(){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        //DEBUG模式下 添加日志拦截器
        if(Constance.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request()
//                        .newBuilder()
//                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                        .addHeader("Accept-Encoding", "gzip, deflate")
//                        .addHeader("Connection", "keep-alive")
//                        .addHeader("Accept", "*/*")
//                        .addHeader("Cookie", "add cookies here")
//                        .build();
//                return chain.proceed(request);
//            }
//        });
        return builder.build();
    }

    public static Retrofit getInstance(){
        if (mRetrofit==null){
            mRetrofit=new Retrofit.Builder()
                    .baseUrl(Constance.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(createHttpClient())
                    .build();
        }
        return mRetrofit;
    }

}
