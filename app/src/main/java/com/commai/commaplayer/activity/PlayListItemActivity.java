package com.commai.commaplayer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commai.commaplayer.Entity.AllPlayLists;
import com.commai.commaplayer.R;
import com.commai.commaplayer.adapter.PlayListDetailAdapter;
import com.commai.commaplayer.base.BaseActivity;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fanqi on 2018/4/4.
 * Description:
 */

public class PlayListItemActivity extends BaseActivity implements View.OnClickListener{

    private final static String EXTRA_NAME="extra_name";

    private final static String EXTRA_LISJSON="extra_listjson";

    private String listJson=null;
    private String name=null;

    private Gson gson;

    private AllPlayLists playLists;

    @BindView(R.id.mediaList)
    RecyclerView playListRecyclerView;

    @BindView(R.id.play_detail_page_title)
    TextView listTitle;

    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.fab_play)
    FloatingActionButton floatPlayAll;

    private PlayListDetailAdapter adapter;


    public static void action(Context context,String listJson,String name) {
        Intent intent = new Intent(context, PlayListItemActivity.class);
        intent.putExtra(EXTRA_LISJSON,listJson);
        intent.putExtra(EXTRA_NAME,name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_detail);
        ButterKnife.bind(this);
        imgBack.setOnClickListener(this);
        floatPlayAll.setOnClickListener(this);
        initData();
    }


    private void initData(){
        gson=new Gson();
        if (getIntent().getExtras()!=null){
            listJson=getIntent().getExtras().getString(EXTRA_LISJSON);
            name=getIntent().getExtras().getString(EXTRA_NAME);
        }
        if (listJson!=null){
            listTitle.setText(name);
            playLists=gson.fromJson(listJson,AllPlayLists.class);
            adapter=new PlayListDetailAdapter(this,playLists.getSelectedList());
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayout.VERTICAL);
            playListRecyclerView.setLayoutManager(manager);
            playListRecyclerView.setItemAnimator(new DefaultItemAnimator());
            playListRecyclerView.setAdapter(adapter);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                this.finish();
                break;
            case R.id.fab_play:
                Intent intent = new Intent(this, CmVideoViewActivity.class);
                intent.putExtra("isPlayList", true);
                intent.putExtra("listPlayJson", listJson);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
