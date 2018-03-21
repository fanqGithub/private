package com.commai.commaplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.commai.commaplayer.listener.ServiceCallBack;
import com.commai.commaplayer.service.Constants;
import com.commai.commaplayer.service.MediaPlayerService;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.PermissionUtil;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;
import com.commai.commaplayer.widget.SegmentControl;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static com.commai.commaplayer.fragment.PlayerFragment.ctx;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        PlayerFragment.PlayerFragmentCallbackListener ,
        PlayerFragment.onPlayPauseListener,ServiceCallBack{

    private SegmentControl segment_ct;
    private LocalMusicFragment musicFragment=null;
    private LocalVideoFragment videoFragment=null;
    private SelfPlayListFragment selfPlayListFragment=null;
    private ViewPager mVp;
    private MyPageAdapter myPageAdapter;

    //底部播放控制相关
    public Toolbar spHomePlayerBar;
    public ImageView playerControllerHome,playerNextMusic;
    public FrameLayout bottomToolbar;
    public CircleImageView spImgHome;
    public TextView spTitleHome;
    private ImageLoader imageLoader;

    //playing fragment show content
    private FrameLayout playerContent;

    public static List<AudioItem> audioItemList=new ArrayList<>();
    public static List<VideoItem> videoItemList=new ArrayList<>();

    public static AudioItem currentPlayingMusic;

    boolean isNotificationVisible = false;
    public boolean isPlayerVisible=false;

    public PlayerFragment playerFragment;
    public LinearLayout mainView;

    private ServiceConnection serviceConnection;
    private MediaPlayerService myService;
    private boolean bound = false;

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
        bottomToolbar=findViewById(R.id.bottomMargin);
        mainView=findViewById(R.id.mainView);
        playerControllerHome=findViewById(R.id.player_control_sp_home);
        playerNextMusic=findViewById(R.id.player_next_sp_home);

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

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
                myService = binder.getService();
                bound = true;
                myService.setCallbacks(MainActivity.this);
            }
            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                bound = false;
            }
        };
        playerControllerHome.setOnClickListener(this);
        playerNextMusic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.smallPlayer_home:
                showPlayer();
                break;
            case R.id.player_control_sp_home:
                if (PlayerFragment.mMediaPlayer.isPlaying()){
                    PlayerFragment.mMediaPlayer.pause();
                    playerControllerHome.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }else {
                    if (!PlayerFragment.completed){
                        PlayerFragment.mMediaPlayer.start();
                        playerControllerHome.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                }
                break;
            case R.id.player_next_sp_home:

                break;
            default:
                break;
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onPreviousTrack() {

    }

    @Override
    public void onEqualizerClicked() {

    }

    @Override
    public void onQueueClicked() {

    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onFullScreen() {

    }

    @Override
    public void onSettingsClicked() {

    }

    @Override
    public void onAddedtoFavfromPlayer() {

    }

    @Override
    public void onShuffleEnabled() {

    }

    @Override
    public void onShuffleDisabled() {

    }

    @Override
    public void onSmallPlayerTouched() {
        if (!isPlayerVisible) {
            isPlayerVisible = true;
            showPlayer();
        } else {
            isPlayerVisible = false;
            hidePlayer();
        }
    }

    @Override
    public void addCurrentSongtoPlaylist(AudioItem ut) {

    }

    /**
     * 播放音乐暂停
     */
    @Override
    public void onPlayPause() {

    }

    @Override
    public PlayerFragment getPlayerFragment() {
        return playerFragment;
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
                isPlayerVisible = true;
                playerControllerHome.setImageResource(R.drawable.ic_pause_black_24dp);
//                hideMainView();
                showPlayer();
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

    public void showPlayer(){
        isPlayerVisible=true;
        //展示播放内容
        PlayerFragment frag = playerFragment;
        FragmentManager fm = getSupportFragmentManager();
        if (frag==null){
            frag=new PlayerFragment();
            playerFragment=frag;
            fm.beginTransaction()
                    .add(R.id.playerContent, frag, "player")
                    .show(frag)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }else {
            frag.refresh();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void hidePlayer() {
        isPlayerVisible = false;
        if (playerFragment != null && playerFragment.smallPlayer != null) {
            playerFragment.smallPlayer.setAlpha(0.0f);
            playerFragment.smallPlayer.setVisibility(View.VISIBLE);
            playerFragment.smallPlayer.animate()
                    .alpha(1.0f);
        }
        if (playerFragment != null && playerFragment.spToolbar != null) {
            playerFragment.spToolbar.animate()
                    .alpha(0.0f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerFragment.spToolbar.setVisibility(GONE);
                        }
                    });
        }

        playerContent.setVisibility(View.VISIBLE);
        if (playerFragment != null) {
            playerContent.animate()
                    .translationY(playerContent.getHeight() - playerFragment.smallPlayer.getHeight())
                    .setDuration(300);
        } else {
            playerContent.animate()
                    .translationY(playerContent.getHeight() - playerFragment.smallPlayer.getHeight())
                    .setDuration(300)
                    .setStartDelay(500);
        }

        if (playerFragment != null) {
            playerFragment.mainTrackController.setAlpha(0.0f);
            playerFragment.mainTrackController.setImageDrawable(playerFragment.mainTrackController.getDrawable());
            playerFragment.mainTrackController.animate().alpha(1.0f);
        }
    }

    public void hideFragment(String tag) {
        if (tag.equals("player")) {
            showMainView();
            isPlayerVisible = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("player");
            if (frag!= null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        }
    }

    public void hideAllFrags() {
        hideFragment("player");
    }

    public void showNotification() {
        if (!isNotificationVisible) {
            Intent intent = new Intent(this, MediaPlayerService.class);
            intent.setAction(Constants.ACTION_PLAY);
            startService(intent);
            isNotificationVisible = true;
        }
    }

    public void hideMainView() {
        mainView.setVisibility(View.INVISIBLE);
    }

    public void showMainView(){
        mainView.setVisibility(View.VISIBLE);
    }





}
