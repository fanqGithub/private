package com.commai.commaplayer.netservices;

import com.commai.commaplayer.Entity.MusicIndexBannerResult;
import com.commai.commaplayer.Entity.SearchMusicResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by fanqi on 2018/4/26.
 * Description:MusicApi服务
 */

public interface ApiService {


    @GET("banner")
    Observable<MusicIndexBannerResult> getOnlyMusicBanner();

    @GET("search")
    Observable<SearchMusicResponse> searchMusic(@Query("keywords") String keywords,
                                                @Query("limit") int limit,
                                                @Query("offset") int offset);


//    @GET("/music/url")

}
