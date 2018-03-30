package com.commai.commaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commai.commaplayer.R;
import com.commai.commaplayer.base.BaseFragment;

/**
 * Created by fanqi on 2018/3/16.
 * Description:包括最近播放历史记录，自定义的播放列表
 */

public class SelfPlayListFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_play_list_all,container,false);
        return view;
    }
}
