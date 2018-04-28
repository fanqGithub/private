package com.commai.commaplayer.netservices;

import com.commai.commaplayer.Entity.MusicIndexBannerResult;
import com.commai.commaplayer.Entity.SearchMusicResponse;
import com.commai.module_baselib.constance.Constance;
import com.commai.module_baselib.network.DefaultObserver;
import com.commai.module_baselib.network.RetrofitFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fanqi on 2018/4/26.
 * Description:
 */

public class ApiMethod {

    /**
     * 获取banner
     * @param observer
     */
    public static void getBanner(DefaultObserver<MusicIndexBannerResult> observer){
        RetrofitFactory.getInstance().create(ApiService.class)
                .getOnlyMusicBanner()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void searchMusic(String kwd,int type,int offset,DefaultObserver<SearchMusicResponse> observer){
        RetrofitFactory.getInstance().create(ApiService.class)
                .searchMusic(kwd,Constance.LIMIT,offset)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
