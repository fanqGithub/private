package com.commai.commaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.VideoItem;
import com.commai.commaplayer.R;
import com.commai.commaplayer.adapter.MusicItemAdapter;
import com.commai.commaplayer.adapter.VideoItemAdapter;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.utils.MediaUtil;

import java.util.List;

/**
 * Created by fanqi on 2018/3/16.
 * Description:
 */

public class LocalVideoFragment extends BaseFragment {

    private RecyclerView mediaListView=null;
    private List<VideoItem> videoItemList=null;
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
        initData();
        return view;
    }

    private void initData(){
        videoItemList= MediaUtil.scanVideos(getContext());
        for (VideoItem item:videoItemList){
            Log.d("TAG_Video",item.toString());
        }

        if (videoItemList!=null){
            adapter=new VideoItemAdapter(getContext(),videoItemList);
            mediaListView.setAdapter(adapter);
        }
    }
}
