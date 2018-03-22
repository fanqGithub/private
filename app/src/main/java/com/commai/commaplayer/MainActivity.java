package com.commai.commaplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.VideoItem;
import com.commai.commaplayer.fragment.LocalMusicFragment;
import com.commai.commaplayer.fragment.LocalVideoFragment;
import com.commai.commaplayer.fragment.PlayerFragment;
import com.commai.commaplayer.fragment.PlayingFragment;
import com.commai.commaplayer.fragment.SelfPlayListFragment;
import com.commai.commaplayer.service.MusicPlayService;
import com.commai.commaplayer.service.MusicPlayer;
import com.commai.commaplayer.service.OnPlayerEventListener;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.PermissionUtil;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;
import com.commai.commaplayer.widget.SegmentControl;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,OnPlayerEventListener{

    private SegmentControl segment_ct;
    private LocalMusicFragment musicFragment=null;
    private LocalVideoFragment videoFragment=null;
    private SelfPlayListFragment selfPlayListFragment=null;
    private ViewPager mVp;
    private MyPageAdapter myPageAdapter;

    //底部播放控制相关
    public Toolbar spHomePlayerBar;
    public ImageView playerControllerHome;
    public ImageView playerNextMusic;
    public CircleImageView spImgHome;
    public TextView spTitleHome;
    private ImageLoader imageLoader;
    private ProgressBar mProgressBar;

    public static List<AudioItem> audioItemList=new ArrayList<>();
    public static List<VideoItem> videoItemList=new ArrayList<>();

    public static AudioItem currentPlayingMusic;

    public PlayerFragment playerFragment;
    public LinearLayout mainView;

    private boolean bound = false;
    protected MusicPlayService playService;
    private ServiceConnection serviceConnection;

    private boolean isPlayFragmentShow=false;
    private PlayingFragment mPlayFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        segment_ct=findViewById(R.id.segment_ct);
        mVp=findViewById(R.id.vp_content);
        spHomePlayerBar=findViewById(R.id.smallPlayer_home);
        spImgHome=findViewById(R.id.selected_track_image_sp_home);
        spTitleHome=findViewById(R.id.selected_track_title_sp_home);
        mainView=findViewById(R.id.mainView);
        playerControllerHome=(ImageView) findViewById(R.id.player_control_sp_home);
        playerNextMusic=findViewById(R.id.player_next_sp_home);
        mProgressBar=findViewById(R.id.pb_play_bar);

        bindService();
        MusicPlayer.get().addOnPlayEventListener(this);

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

        playerControllerHome.setOnClickListener(this);
        playerNextMusic.setOnClickListener(this);
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, MusicPlayService.class);
        serviceConnection = new PlayServiceConnection();
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playService = ((MusicPlayService.PlayBinder) service).getService();
            onServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getClass().getSimpleName(), "service disconnected");
        }
    }

    protected void onServiceBound() {
        Log.d("Service connect","connected");

    }



    @Override
    public void onChange(AudioItem music) {
        if (music == null) {
            return;
        }
        spTitleHome.setText(music.getTitle());
        imageLoader.DisplayImage(music.getPath(),spImgHome);
        playerControllerHome.setSelected(MusicPlayer.get().isPlaying() || MusicPlayer.get().isPreparing());
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress((int) MusicPlayer.get().getAudioPosition());
    }

    @Override
    public void onPlayerStart() {
        playerControllerHome.setSelected(true);
//        Toast.makeText(this,"播放了",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayerPause() {
        playerControllerHome.setSelected(false);
//        Toast.makeText(this,"暂停了",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPublish(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void onBufferingUpdate(int percent) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.smallPlayer_home:
                showPlayingFragment();
                break;
            case R.id.player_control_sp_home:
                MusicPlayer.get().playPause();
                break;
            case R.id.player_next_sp_home:
                MusicPlayer.get().next();
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
                AudioItem music = audioItemList.get(position);
                MusicPlayer.get().addAndPlay(music);
                showPlayingFragment();
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

    private void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new PlayingFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }

    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        MusicPlayer.get().removeOnPlayEventListener(this);
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
        super.onDestroy();
    }


}
