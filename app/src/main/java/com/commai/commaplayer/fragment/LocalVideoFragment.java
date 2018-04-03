package com.commai.commaplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commai.commaplayer.Entity.RecentPlay;
import com.commai.commaplayer.Entity.VideoItem;
import com.commai.commaplayer.MainActivity;
import com.commai.commaplayer.R;
import com.commai.commaplayer.activity.CmVideoViewActivity;
import com.commai.commaplayer.adapter.VideoItemAdapter;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.greendao.dao.DBManager;
import com.commai.commaplayer.greendao.dao.RecentPlayDao;
import com.commai.commaplayer.listener.ClickItemTouchListener;
import com.commai.commaplayer.threadpool.ThreadPoolProxyFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fanqi on 2018/3/16.
 * Description:
 */

public class LocalVideoFragment extends BaseFragment {

    private RecyclerView mediaListView=null;
    private VideoItemAdapter adapter=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        mediaListView=view.findViewById(R.id.mediaList);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setOrientation(LinearLayout.VERTICAL);
        mediaListView.setLayoutManager(manager);
        mediaListView.setItemAnimator(new DefaultItemAnimator());
        mediaListView.addOnItemTouchListener(new ClickItemTouchListener(mediaListView) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                if (position >= 0) {
                    final VideoItem videoItem=MainActivity.videoItemList.get(position);
                    ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            RecentPlay playItem=new RecentPlay();
                            playItem.setMediaName(videoItem.getName());
                            playItem.setMediaPath(videoItem.getPath());
                            playItem.setDuration(videoItem.getDuration());
                            playItem.setSize(videoItem.getSize());
                            playItem.setMediaType("video");
                            playItem.setThumbImgPath(videoItem.getThumbImgPath());
                            playItem.setPlayTime(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
                            RecentPlay existItem= DBManager.get().getRecentPlayDao().queryBuilder().where(RecentPlayDao.Properties.MediaName.eq(videoItem.getName())).unique();
                            if (existItem==null) {
                                DBManager.get().getRecentPlayDao().insert(playItem);
                            }else {
                                playItem.setId(existItem.getId());
                                DBManager.get().getRecentPlayDao().update(playItem);
                            }
                        }
                    });
                    Intent intent=new Intent(getContext(), CmVideoViewActivity.class);
                    intent.putExtra("mediaPath",videoItem.getPath());
                    intent.putExtra("videoTitle",videoItem.getName());
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                return true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        initData();
        return view;
    }

    private void initData(){
        if (MainActivity.videoItemList!=null){
            adapter=new VideoItemAdapter(getContext(),MainActivity.videoItemList);
            mediaListView.setAdapter(adapter);
        }
    }

}
