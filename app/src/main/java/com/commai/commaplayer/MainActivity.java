package com.commai.commaplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.adapter.MusicItemAdapter;
import com.commai.commaplayer.fragment.LocalMusicFragment;
import com.commai.commaplayer.fragment.LocalVideoFragment;
import com.commai.commaplayer.fragment.SelfPlayListFragment;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.widget.SegmentControl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private SegmentControl segment_ct;
    private LocalMusicFragment musicFragment=null;
    private LocalVideoFragment videoFragment=null;
    private SelfPlayListFragment selfPlayListFragment=null;
    private ViewPager mVp;
    private MyPageAdapter myPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        segment_ct=findViewById(R.id.segment_ct);
        mVp=findViewById(R.id.vp_content);
        if (musicFragment==null){
            musicFragment=new LocalMusicFragment();
        }
        if (videoFragment==null) {
            videoFragment = new LocalVideoFragment();
        }
        if (selfPlayListFragment==null) {
            selfPlayListFragment = new SelfPlayListFragment();
        }
        myPageAdapter=new MyPageAdapter(getSupportFragmentManager());
        myPageAdapter.addFragment(musicFragment);
        myPageAdapter.addFragment(videoFragment);
        myPageAdapter.addFragment(selfPlayListFragment);
        mVp.setAdapter(myPageAdapter);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                segment_ct.setSelectedIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        segment_ct.setOnSegmentChangedListener(new SegmentControl.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                mVp.setCurrentItem(newSelectedIndex);
            }
        });
        mVp.setCurrentItem(0);
    }

    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

    }


}
