package com.commai.module_baselib.network;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.commai.module_baselib.R;
import com.google.gson.JsonParseException;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by fanqi on 2018/4/27.
 * Description:统一处理的观察者
 */

public abstract class DefaultObserver<T> implements Observer<T> {

    private Context mContext;
    private KProgressHUD progressHUD;
    public DefaultObserver(Context context) {
        this(context,false);
    }

    public DefaultObserver(Context context, boolean isShowLoading) {
        this.mContext=context;
        progressHUD=KProgressHUD.create(mContext).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        if (isShowLoading) {
            progressHUD.show();
        }
    }
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        dismissProgress();
        onSuccess(t);
    }

    private void dismissProgress(){
        if(progressHUD!=null && progressHUD.isShowing()){
            progressHUD.dismiss();
        }
    }


    @Override
    public void onError(@NonNull Throwable e) {
        dismissProgress();
        if (e instanceof HttpException) {
            //HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);

//    /**
//     * 服务器返回数据，但响应码不为200
//     *
//     * @param response 服务器返回的数据
//     */
//    public void onFail(T response) {
//        String message = response.getMessage();
//        if (TextUtils.isEmpty(message)) {
//            ToastUtils.show(R.string.response_return_error);
//        } else {
//            ToastUtils.show(message);
//        }
//    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                showTips("呀！网络异常！");
                break;

            case CONNECT_TIMEOUT:
                showTips("连接超时！");
                break;

            case BAD_NETWORK:
                showTips("网络异常！");
                break;

            case PARSE_ERROR:
                showTips("解析返回数据出错！");
                break;

            case UNKNOWN_ERROR:
            default:
                showTips("未知错误！");
                break;
        }
    }

    private void showTips(String str){
        final KProgressHUD hud=KProgressHUD.create(mContext).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(str)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 2000);
    }
}