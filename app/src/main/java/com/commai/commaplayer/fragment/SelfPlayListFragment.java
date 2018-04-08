package com.commai.commaplayer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.commai.commaplayer.Entity.AllPlayLists;
import com.commai.commaplayer.Entity.RecentPlay;
import com.commai.commaplayer.R;
import com.commai.commaplayer.activity.CmVideoViewActivity;
import com.commai.commaplayer.activity.PlayListItemActivity;
import com.commai.commaplayer.adapter.PlayListAdapter;
import com.commai.commaplayer.adapter.RecentPlayAdapter;
import com.commai.commaplayer.base.BaseFragment;
import com.commai.commaplayer.greendao.bean.PlayListBean;
import com.commai.commaplayer.greendao.dao.DBManager;
import com.commai.commaplayer.greendao.dao.PlayListBeanDao;
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

    private List<PlayListBean> createPlayList=null;

    private PlayListAdapter mPlayListAdapter=null;

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
        recentPlayList= DBManager.get().getRecentPlayDao().queryBuilder().offset(0).limit(10).list();
        createPlayList=DBManager.get().getPlayListBeanDao().loadAll();
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
                RecentPlay item=recentPlayList.get(position);
                Intent intent = new Intent(getContext(), CmVideoViewActivity.class);
                intent.putExtra("mediaPath", item.getMediaPath());
                intent.putExtra("videoTitle", item.getMediaName());
                intent.putExtra("duration",item.getDuration());
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                final RecentPlay item=recentPlayList.get(position);
                new AlertDialog.Builder(getContext()).setMessage("删除此条播放记录！").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.get().getRecentPlayDao().delete(item);
                        recentPlayList.remove(item);
                        mAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", null).create().show();
                return true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setOrientation(LinearLayout.VERTICAL);
        createRecycleView.setLayoutManager(manager1);
        createRecycleView.setItemAnimator(new DefaultItemAnimator());
        if (createPlayList!=null && createPlayList.size()>0){
            tvCreateNothing.setVisibility(View.GONE);
            mPlayListAdapter=new PlayListAdapter(getActivity(),createPlayList);
            createRecycleView.setAdapter(mPlayListAdapter);
        }else {
            tvCreateNothing.setVisibility(View.VISIBLE);
        }
        createRecycleView.addOnItemTouchListener(new ClickItemTouchListener(createRecycleView) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                PlayListBean listBean=createPlayList.get(position);
                String listJson=listBean.getPlayListJson();
                PlayListItemActivity.action(getContext(),listJson,listBean.getPlayListName());
                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                PlayListBean listBean=createPlayList.get(position);
                showDeleteDialog(listBean);
                return true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void showDeleteDialog(final PlayListBean bean){
        new AlertDialog.Builder(getContext()).setMessage("删除此播放列表！").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBManager.get().getPlayListBeanDao().delete(bean);
                createPlayList.remove(bean);
                mPlayListAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("取消", null).create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
