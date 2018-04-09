package com.commai.commaplayer.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

/**
 * Created by fanqi on 2018/3/27.
 * Description:
 */

public class CmVideoView extends VideoView{

    //最终的视频资源宽度
    private int mVideoWidth=480;
    //最终视频资源高度
    private int mVideoHeight=480;
    //视频资源原始宽度
    private int videoRealW=1;
    //视频资源原始高度
    private int videoRealH=1;

    private float scale;

    private String orientation;

    public CmVideoView(Context context) {
        super(context);
    }

    public CmVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CmVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void setVideoPath(String path) {
        super.setVideoPath(path);
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(path);
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        orientation=retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);//原视频的方向 90竖屏，0横屏
        Log.d("设orientation",orientation);
        try {
            //竖屏
            if ("90".equals(orientation)){
                videoRealH=Integer.parseInt(width);
                videoRealW=Integer.parseInt(height);
            }else {
                videoRealH=Integer.parseInt(height);
                videoRealW=Integer.parseInt(width);
            }
        } catch (NumberFormatException e) {
            Log.e("----->" + "VideoView", "setVideoPath:" + e.toString());
        }
        scale = (float) videoRealH / (float) videoRealW;
        Log.e("----->" + "VideoViewScale:", scale + "");

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width,height);
//        Log.d("设备方向的宽高：",width+":"+height);
//        Log.i("设原视频的宽高：",videoRealW+":"+videoRealH);
//        if(height>width){
//            //资源竖屏
//            if(videoRealH>videoRealW){
//                //如果视频资源是竖屏
//                //占满屏幕
//                mVideoHeight=height;
//                mVideoWidth=width;
//            }else {
//                //如果视频资源是横屏
//                //宽度占满，高度比例
//                mVideoWidth=width;
//                float r=videoRealH/(float)videoRealW;
//                mVideoHeight= (int) (mVideoWidth*r);
//            }
//        }else {
//            //横屏
//            if(videoRealH>videoRealW){
//                //如果视频资源是竖屏
//                //高度占满，宽度比例
//                mVideoHeight=height;
//                float r=videoRealW/(float)videoRealH;
//                mVideoWidth= (int) (mVideoHeight*r);
//            }else {
//                //如果视频资源是横屏
//                mVideoHeight=height;
//                //占满屏幕
//                mVideoWidth=width;
//            }
//        }
//        if(videoRealH==videoRealW&&videoRealH==1){
//            //没能获取到视频真实的宽高，自适应就可以了，什么也不用做
//            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//        }else {
//            Log.i("设调整的宽高：",mVideoWidth+":"+mVideoHeight);
//            setMeasuredDimension(mVideoWidth, mVideoHeight);
//        }
//        requestLayout();
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //屏蔽触摸点击事件
        return true;
    }
}
