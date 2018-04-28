package com.commai.commaplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.commai.commaplayer.Entity.MusicIndexBannerResult;
import com.commai.commaplayer.R;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.netservices.ApiMethod;
import com.commai.commaplayer.utils.imageLoader.BannerImageLoader;
import com.commai.module_baselib.network.DefaultObserver;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fanqi on 2018/4/24.
 * Description:
 */

public class OnLineMusicFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner mBanner;

    private ArrayList<String> listPath;
    private ArrayList<String> listTitle;

    private List<MusicIndexBannerResult.BannersBean> bannersBeanList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_online_music,container,false);
        ButterKnife.bind(this,view);
        initViewAndData();
        return view;
    }

    private void initViewAndData() {
        listPath = new ArrayList<>();
        listTitle = new ArrayList<>();
        ApiMethod.getBanner(new DefaultObserver<MusicIndexBannerResult>(getContext()) {
            @Override
            public void onSuccess(MusicIndexBannerResult response) {
                if (response.getCode() == 200) {
                    Log.d("TAG_BANNER", response.toString());
                    bannersBeanList = response.getBanners();
                    for (MusicIndexBannerResult.BannersBean bannersBean : bannersBeanList) {
                        listPath.add(bannersBean.getPic());
                        listTitle.add(bannersBean.getTypeTitle());
                    }
                    mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                    mBanner.setImageLoader(new BannerImageLoader());
                    mBanner.setImages(listPath);
                    mBanner.setImageLoader(new BannerImageLoader() {
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            super.displayImage(context, path, imageView);
                        }
                    });
                    mBanner.setBannerAnimation(Transformer.DepthPage);
                    mBanner.setBannerTitles(listTitle);
                    mBanner.isAutoPlay(true);
                    mBanner.setDelayTime(3000);
                    mBanner.setIndicatorGravity(BannerConfig.RIGHT);
                    mBanner.start();
                }
            }
        });
    }


}
