package com.commai.commaplayer.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.R;
import com.commai.commaplayer.activity.PlayerActivity;
import com.commai.commaplayer.adapter.MusicItemAdapter;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.listener.ClickItemTouchListener;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.PermissionUtil;

import java.util.List;

/**
 * Created by fanqi on 2018/3/16.
 * Description:
 */

public class LocalMusicFragment extends BaseFragment {

    private RecyclerView mediaListView=null;
    private List<AudioItem> audioItemList=null;
    private MusicItemAdapter adapter=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        mediaListView=view.findViewById(R.id.mediaList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayout.VERTICAL);
        mediaListView.setLayoutManager(manager);
        mediaListView.setItemAnimator(new DefaultItemAnimator());
        mediaListView.addOnItemTouchListener(new ClickItemTouchListener(mediaListView) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                if (position >= 0) {
                    Intent intent=new Intent(getContext(), PlayerActivity.class);
                    intent.putExtra("mediaPath",audioItemList.get(position).getPath());
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
        PermissionUtil.requestPermissionsResult(this, 1, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                , new PermissionUtil.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        audioItemList= MediaUtil.scanAudios(getContext());
                        for (AudioItem item:audioItemList){
                            Log.d("TAG_MUSICS",item.toString());
                        }

                        if (audioItemList!=null){
                            adapter=new MusicItemAdapter(getContext(),audioItemList);
                            mediaListView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onPermissionDenied() {
                        PermissionUtil.showTipsDialog(getContext());
                    }
                });


    }
}
