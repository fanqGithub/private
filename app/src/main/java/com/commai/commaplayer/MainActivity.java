package com.commai.commaplayer;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.VideoItem;
import com.commai.commaplayer.adapter.MusicItemAdapter;
import com.commai.commaplayer.fragment.LocalMusicFragment;
import com.commai.commaplayer.fragment.LocalVideoFragment;
import com.commai.commaplayer.fragment.PlayerFragment;
import com.commai.commaplayer.fragment.SelfPlayListFragment;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.PermissionUtil;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;
import com.commai.commaplayer.widget.SegmentControl;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SegmentControl segment_ct;
    private LocalMusicFragment musicFragment=null;
    private LocalVideoFragment videoFragment=null;
    private SelfPlayListFragment selfPlayListFragment=null;
    private ViewPager mVp;
    private MyPageAdapter myPageAdapter;

    //底部播放控制相关
    public Toolbar spHomePlayerBar;
    public ImageView playerControllerHome;
    public FrameLayout bottomToolbar;
    public CircleImageView spImgHome;
    public TextView spTitleHome;
    private ImageLoader imageLoader;

    //playing fragment show content
    private FrameLayout playerContent;

    public static List<AudioItem> audioItemList=new ArrayList<>();
    public static List<VideoItem> videoItemList=new ArrayList<>();

    public static AudioItem currentPlayingMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        segment_ct=findViewById(R.id.segment_ct);
        mVp=findViewById(R.id.vp_content);
        spHomePlayerBar=findViewById(R.id.smallPlayer_home);
        spImgHome=findViewById(R.id.selected_track_image_sp_home);
        spTitleHome=findViewById(R.id.selected_track_title_sp_home);
        playerContent=findViewById(R.id.playerContent);

        imageLoader=new ImageLoader(this);
        initMediaData();
        if (musicFragment==null){
            musicFragment=new LocalMusicFragment();
        }
        if (videoFragment==null) {
            videoFragment = new LocalVideoFragment();
        }
        if (selfPlayListFragment==null) {
            selfPlayListFragment = new SelfPlayListFragment();
        }
        initCallBack();
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
        spHomePlayerBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.smallPlayer_home:
                //展示播放内容
                FragmentManager fm = getSupportFragmentManager();
                PlayerFragment playerFragment= (PlayerFragment) fm.findFragmentByTag("player");
                if (playerFragment==null){
                    playerFragment=new PlayerFragment();
                }
                fm.beginTransaction()
                        .add(R.id.playerContent, playerFragment, "player")
                        .show(playerFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                break;
            default:
                break;
        }
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

    private void initCallBack(){
        musicFragment.setOnMusicClickCallBackListener(new LocalMusicFragment.onMusicClickCallBackListener() {
            @Override
            public void onMusicClickCallBack(int position) {
                currentPlayingMusic=audioItemList.get(position);
                spTitleHome.setText(audioItemList.get(position).getTitle());
                imageLoader.DisplayImage(audioItemList.get(position).getPath(),spImgHome);
//                spImgHome.setImageBitmap(MediaUtil.getAlbumArt(MainActivity.this,audioItemList.get(position).getAlbum_id()));
            }
        });
    }

    private void initMediaData(){
        PermissionUtil.requestPermissionsResult(this, 1, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , new PermissionUtil.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        audioItemList= MediaUtil.scanAudios(MainActivity.this);
                        videoItemList= MediaUtil.scanVideos(MainActivity.this);
                        for (AudioItem item:audioItemList){
                            Log.d("TAG_MUSICS",item.toString());
                        }
                        for (VideoItem item:videoItemList){
                            Log.d("TAG_Video",item.toString());
                        }
                    }

                    @Override
                    public void onPermissionDenied() {
                        PermissionUtil.showTipsDialog(MainActivity.this);
                    }
                });


    }





}
