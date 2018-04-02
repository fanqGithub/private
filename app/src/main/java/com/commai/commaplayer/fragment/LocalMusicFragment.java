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
import com.commai.commaplayer.MainActivity;
import com.commai.commaplayer.R;
import com.commai.commaplayer.adapter.MusicItemAdapter;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.listener.ClickItemTouchListener;

/**
 * Created by fanqi on 2018/3/16.
 * Description:
 */

public class LocalMusicFragment extends BaseFragment {

    private RecyclerView mediaListView=null;
//    private List<AudioItem> audioItemList=null;
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
                    AudioItem item= MainActivity.audioItemList.get(position);
                    item.setCheck(item.isCheck()?false:true);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                isMutiableCheckShow=true;
                adapter.notifyCheckMeShow();
                AudioItem item= MainActivity.audioItemList.get(position);
                item.setCheck(item.isCheck()?false:true);
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
            mediaListView.setAdapter(adapter);
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
        adapter.notifyCheckMeHidel();
    }


}
