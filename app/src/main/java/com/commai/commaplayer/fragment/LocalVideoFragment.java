package com.commai.commaplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.RecentPlay;
import com.commai.commaplayer.Entity.SelectedMediaItem;
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
import java.util.Map;

/**
 * Created by fanqi on 2018/3/16.
 * Description:
 */

public class LocalVideoFragment extends BaseFragment {

    private RecyclerView mediaListView=null;
    private VideoItemAdapter adapter=null;
    private OnVideoCallBackListener mListener;

    public boolean isMutiableCheckShow=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        mediaListView=view.findViewById(R.id.mediaList);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setOrientation(LinearLayout.VERTICAL);
        mediaListView.setLayoutManager(manager);
        mediaListView.setItemAnimator(new DefaultItemAnimator());
        mediaListView.addOnItemTouchListener(new ClickItemTouchListener(mediaListView) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                final VideoItem videoItem = MainActivity.videoItemList.get(position);
                if (!isMutiableCheckShow) {
                    ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            RecentPlay playItem = new RecentPlay();
                            playItem.setMediaName(videoItem.getName());
                            playItem.setMediaPath(videoItem.getPath());
                            playItem.setDuration(videoItem.getDuration());
                            playItem.setSize(videoItem.getSize());
                            playItem.setMediaType("video");
                            playItem.setThumbImgPath(videoItem.getThumbImgPath());
                            playItem.setPlayTime(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
                            RecentPlay existItem = DBManager.get().getRecentPlayDao().queryBuilder().where(RecentPlayDao.Properties.MediaName.eq(videoItem.getName())).unique();
                            if (existItem == null) {
                                DBManager.get().getRecentPlayDao().insert(playItem);
                            } else {
                                playItem.setId(existItem.getId());
                                DBManager.get().getRecentPlayDao().update(playItem);
                            }
                        }
                    });
                    Intent intent = new Intent(getContext(), CmVideoViewActivity.class);
                    intent.putExtra("mediaPath", videoItem.getPath());
                    intent.putExtra("videoTitle", videoItem.getName());
                    intent.putExtra("duration",videoItem.getDuration());
                    startActivity(intent);
                }else{
                    //进入点击选择模式
                    Map<Integer, Boolean> map=adapter.getSelectedData();
                    map.put(position,map.get(position)?false:true);
                    addSelected(position,map.get(position));
                    adapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                isMutiableCheckShow=true;
                adapter.notifyCheckMeShow();

                Map<Integer, Boolean> map=adapter.getSelectedData();
                map.put(position,map.get(position)?false:true);
                addSelected(position,map.get(position));
                adapter.notifyDataSetChanged();
                if (mListener!=null){
                    mListener.onVideoLongPressCallBack(position);
                }
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

    public interface OnVideoCallBackListener{
        void onVideoClickCallBack(int position);
        void onVideoLongPressCallBack(int position);
    }

    public void setVideoClickCallBackListener(OnVideoCallBackListener listener){
        this.mListener=listener;
    }

    public void addSelected(int position, boolean isChecked) {
        Log.d("Tag_videoselect",position+"--"+isChecked);
        VideoItem item= MainActivity.videoItemList.get(position);
        //添加
        SelectedMediaItem selected=new SelectedMediaItem();
        selected.setMediaName(item.getName());
        selected.setMediaPath(item.getPath());
        selected.setMediaType("video");
        selected.setSize(item.getSize());
        selected.setDuration(item.getDuration());
        if (isChecked) {
            //需要判断原selectlist里面是否已经有了这个选项。
            if (!MainActivity.selectedList.contains(selected)) {
                MainActivity.selectedList.add(selected);
            }
        }else {
            MainActivity.selectedList.remove(selected);
        }
    }

    public void onKeyBackPress() {
        isMutiableCheckShow=false;
        Map<Integer, Boolean> map=adapter.getSelectedData();
        for (int i=0;i<map.size();i++){
            map.put(i,false);
        }
        adapter.notifyCheckMeHidel();
    }

}
