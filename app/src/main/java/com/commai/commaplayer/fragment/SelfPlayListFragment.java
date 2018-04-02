package com.commai.commaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commai.commaplayer.Entity.RecentPlay;
import com.commai.commaplayer.R;
import com.commai.commaplayer.adapter.RecentPlayAdapter;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.greendao.dao.DBManager;
import com.commai.commaplayer.listener.ClickItemTouchListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fanqi on 2018/3/16.
 * Description:包括最近播放历史记录，自定义的播放列表
 */

public class SelfPlayListFragment extends BaseFragment {

    @BindView(R.id.recents_musicList)
    RecyclerView recentRecycleView;

    @BindView(R.id.create_musicplay_list)
    RecyclerView createRecycleView;

    @BindView(R.id.tv_recents_nothing)
    TextView tvRecentNothing;

    @BindView(R.id.tv_custom_nothing)
    TextView tvCreateNothing;

    Unbinder unbinder;

    private List<RecentPlay> recentPlayList=null;

    private RecentPlayAdapter mAdapter=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_play_list_all,container,false);
        unbinder=ButterKnife.bind(this,view);
        initData();
        return view;
    }

    private void initData(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayout.HORIZONTAL);
        recentRecycleView.setLayoutManager(manager);
        recentRecycleView.setItemAnimator(new DefaultItemAnimator());
        recentPlayList= DBManager.get().getRecentPlayDao().loadAll();
        if (recentPlayList!=null && recentPlayList.size()>0){
            tvRecentNothing.setVisibility(View.GONE);
            mAdapter=new RecentPlayAdapter(getActivity(),recentPlayList);
            recentRecycleView.setAdapter(mAdapter);
        }else {
            tvRecentNothing.setVisibility(View.VISIBLE);
        }
        recentRecycleView.addOnItemTouchListener(new ClickItemTouchListener(recentRecycleView) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {

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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
