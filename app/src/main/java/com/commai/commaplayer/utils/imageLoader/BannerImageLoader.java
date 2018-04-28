package com.commai.commaplayer.utils.imageLoader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.*;

/**
 * Created by fanqi on 2018/4/26.
 * Description:
 */

public class BannerImageLoader extends com.youth.banner.loader.ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load((String) path).into(imageView);
    }
}