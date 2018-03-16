package com.commai.commaplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.adapter.MusicItemAdapter;
import com.commai.commaplayer.utils.MediaUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mediaListView=null;
    private List<AudioItem> audioItemList=null;
    private MusicItemAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaListView=findViewById(R.id.mediaList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.VERTICAL);
        mediaListView.setLayoutManager(manager);
        mediaListView.setItemAnimator(new DefaultItemAnimator());
        initData();
    }

    private void initData(){
        audioItemList= MediaUtil.scanAudios(this);
        for (AudioItem item:audioItemList){
            Log.d("TAG_MUSICS",item.toString());
        }

        if (audioItemList!=null){
            adapter=new MusicItemAdapter(this,audioItemList);
            mediaListView.setAdapter(adapter);
        }
    }
}
