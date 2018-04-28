package com.commai.commaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commai.commaplayer.R;
import com.commai.commaplayer.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fanqi on 2018/4/24.
 * Description:
 */

public class MusicFragment extends BaseFragment {

    @BindView(R.id.music_main_tabs)
    TabLayout tabLayout;

    @BindView(R.id.music_main_viewpager)
    ViewPager viewPager;

    public LocalMusicFragment localMusicFragment= new LocalMusicFragment();
    public OnLineMusicFragment onLineMusicFragment=new OnLineMusicFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_music_main,container,false);
        ButterKnife.bind(this,view);
        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(2);
        }
//        tabLayout.setTabTextColors(R.color.bg_black, R.color.colorblue);
//        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorblue));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        MainMusicAdapter adapter = new MainMusicAdapter(getChildFragmentManager());
        adapter.addFragment(localMusicFragment, "本地音乐");
        adapter.addFragment(onLineMusicFragment, "在线音乐");
        viewPager.setAdapter(adapter);
    }

    static class MainMusicAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MainMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
