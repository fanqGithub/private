package com.commai.commaplayer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.SelectedMediaItem;
import com.commai.commaplayer.MainActivity;
import com.commai.commaplayer.R;
import com.commai.commaplayer.adapter.MusicItemAdapter;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.listener.ClickItemTouchListener;

import java.util.Map;

/**
 * Created by fanqi on 2018/3/16.
 * Description:
 */

public class LocalMusicFragment extends BaseFragment implements MusicItemAdapter.OnMyCheckChangeListener{

    private RecyclerView mediaListView=null;
    private MusicItemAdapter adapter=null;
    private onMusicClickCallBackListener musicClickListener=null;

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
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayout.VERTICAL);
        mediaListView.setLayoutManager(manager);
        mediaListView.setItemAnimator(new DefaultItemAnimator());
        mediaListView.addOnItemTouchListener(new ClickItemTouchListener(mediaListView) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                if (musicClickListener!=null && !isMutiableCheckShow){
                    musicClickListener.onMusicClickCallBack(position);
                }else if(musicClickListener!=null && isMutiableCheckShow){
                    Map<Integer, Boolean> map=adapter.getSelectedData();
                    map.put(position,map.get(position)?false:true);
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
                adapter.notifyDataSetChanged();
                if (musicClickListener!=null && isMutiableCheckShow){
                    musicClickListener.onItemLongPressCallBack(position);
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
        if (MainActivity.audioItemList!=null){
            adapter=new MusicItemAdapter(getContext(),MainActivity.audioItemList);
            adapter.setCheckChangeListener(this);
            mediaListView.setAdapter(adapter);
        }

    }

    @Override
    public void checkChanged(int position, boolean isChecked) {
        AudioItem item= MainActivity.audioItemList.get(position);
        //添加
        SelectedMediaItem selected=new SelectedMediaItem();
        selected.setMediaName(item.getTitle());
        selected.setMediaPath(item.getPath());
        selected.setMediaType("audio");
        selected.setArtist(item.getArtist());
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

    public interface onMusicClickCallBackListener{
        void onMusicClickCallBack(int position);
        void onItemLongPressCallBack(int position);
    }

    public void setOnMusicClickCallBackListener(onMusicClickCallBackListener listener){
        this.musicClickListener=listener;
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
