package com.commai.commaplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.commai.commaplayer.Entity.AllPlayLists;
import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.RecentPlay;
import com.commai.commaplayer.Entity.SelectedMediaItem;
import com.commai.commaplayer.Entity.VideoItem;
import com.commai.commaplayer.adapter.AddPlayListAdapter;
import com.commai.commaplayer.fragment.LocalMusicFragment;
import com.commai.commaplayer.fragment.LocalVideoFragment;
import com.commai.commaplayer.fragment.PlayingFragment;
import com.commai.commaplayer.fragment.SelfPlayListFragment;
import com.commai.commaplayer.greendao.bean.PlayListBean;
import com.commai.commaplayer.greendao.dao.DBManager;
import com.commai.commaplayer.greendao.dao.PlayListBeanDao;
import com.commai.commaplayer.service.MusicPlayService;
import com.commai.commaplayer.service.MusicPlayer;
import com.commai.commaplayer.service.OnPlayerEventListener;
import com.commai.commaplayer.shareprefrence.Preferences;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.PermissionUtil;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;
import com.commai.commaplayer.widget.ControlViewPager;
import com.commai.commaplayer.widget.SegmentControl;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,OnPlayerEventListener{

    private SegmentControl segment_ct;
    private LocalMusicFragment musicFragment=null;
    private LocalVideoFragment videoFragment=null;
    private SelfPlayListFragment selfPlayListFragment=null;
    private ControlViewPager mVp;
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

    public LinearLayout mainView;

    private boolean bound = false;
    protected MusicPlayService playService;
    private ServiceConnection serviceConnection;

    private boolean isPlayFragmentShow=false;
    private PlayingFragment mPlayFragment;

    private FrameLayout bottomSmallPalyer;
    private int lastplayposition=0;

    //选择相关
    private FrameLayout chooseTopView;
    private FrameLayout chooseBottomView;
    private LinearLayout toggleView;
    private TextView tvAddPlayList;
    private TextView tvCancel;

    //选中item，加入播放列表相关
    public static List<SelectedMediaItem> selectedList=new ArrayList<>();
    private AllPlayLists allPlayLists;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        segment_ct=findViewById(R.id.segment_ct);
        mVp=findViewById(R.id.vp_content);
        bottomSmallPalyer=findViewById(R.id.bottomMargin);
        spHomePlayerBar=findViewById(R.id.smallPlayer_home);
        spImgHome=findViewById(R.id.selected_track_image_sp_home);
        spTitleHome=findViewById(R.id.selected_track_title_sp_home);
        mainView=findViewById(R.id.mainView);
        playerControllerHome=(ImageView) findViewById(R.id.player_control_sp_home);
        playerNextMusic=findViewById(R.id.player_next_sp_home);
        mProgressBar=findViewById(R.id.pb_play_bar);
        chooseTopView=findViewById(R.id.choose_top_view);
        chooseBottomView=findViewById(R.id.choose_bottom_view);
        toggleView=findViewById(R.id.toggle_view);
        tvAddPlayList=findViewById(R.id.tv_add_play_list);
        tvCancel=findViewById(R.id.cancel_choose);

        gson=new Gson();
        allPlayLists=new AllPlayLists();

        lastplayposition = Preferences.getPlayPosition();
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
        tvAddPlayList.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
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
    }

    @Override
    public void onPlayerPause() {
        playerControllerHome.setSelected(false);
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
            case R.id.tv_add_play_list:
                //添加到播放列表
                if (selectedList.size()==0){
                    Toast.makeText(MainActivity.this,"请填选择要添加的文件",Toast.LENGTH_SHORT).show();
                    return;
                }
                showAddToPlayListDialog();
                break;
            case R.id.cancel_choose:
                resetView();
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

            @Override
            public void onItemLongPressCallBack(int position) {
                //item长按，toggleview隐藏，choose_top_view显示，choose_bottom_view显示
                mVp.setVpScrollAble(false);
                toggleView.setVisibility(View.GONE);
                bottomSmallPalyer.setVisibility(View.GONE);
                chooseTopView.setVisibility(View.VISIBLE);
                chooseBottomView.setVisibility(View.VISIBLE);

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
                        if (audioItemList.size()>0){
                            bottomSmallPalyer.setVisibility(View.VISIBLE);
                            if (lastplayposition>audioItemList.size()){
                                spTitleHome.setText(audioItemList.get(0).getTitle());
                                imageLoader.DisplayImage(audioItemList.get(0).getPath(),spImgHome);
                            }else if (lastplayposition==audioItemList.size()){
                                spTitleHome.setText(audioItemList.get(audioItemList.size()-1).getTitle());
                                imageLoader.DisplayImage(audioItemList.get(audioItemList.size()-1).getPath(),spImgHome);
                            }else {
                                spTitleHome.setText(audioItemList.get(lastplayposition).getTitle());
                                imageLoader.DisplayImage(audioItemList.get(lastplayposition).getPath(),spImgHome);
                            }
                        }else {
                            bottomSmallPalyer.setVisibility(View.GONE);
                        }
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

    private void showAddToPlayListDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_add_to_play_list);
        dialog.setTitle("添加到播放列表");
        allPlayLists.setSelectedList(selectedList);
        Log.d("Tag_selected",allPlayLists.getSelectedList().size()+"");
        Log.d("Tag_selected_obj", gson.toJson(allPlayLists));

        RecyclerView playListView=dialog.findViewById(R.id.play_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.VERTICAL);
        playListView.setLayoutManager(manager);
        List<PlayListBean> listBeans=DBManager.get().getPlayListBeanDao().loadAll();
        AddPlayListAdapter addPlayListAdapter=new AddPlayListAdapter(this,listBeans);
        playListView.setAdapter(addPlayListAdapter);

        final EditText editTextNewList=dialog.findViewById(R.id.new_playlist_name);
        ImageView confrimButtom=dialog.findViewById(R.id.confirm_button);

        confrimButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTextNewList.getText())){
                    Toast.makeText(MainActivity.this,"请填写列表名",Toast.LENGTH_SHORT).show();
                    return;
                }
                PlayListBean bean=new PlayListBean();
                bean.setPlayListName(editTextNewList.getText().toString());
                bean.setPlayListJson(gson.toJson(allPlayLists));
                PlayListBean existBean=DBManager.get().getPlayListBeanDao().queryBuilder().where(PlayListBeanDao.Properties.PlayListName.eq(editTextNewList.getText().toString())).unique();
                if (existBean==null) {
                    DBManager.get().getPlayListBeanDao().insert(bean);
                }else {
                    bean.setId(existBean.getId());
                    DBManager.get().getPlayListBeanDao().update(bean);
                }
                Toast.makeText(MainActivity.this,"已添加至"+editTextNewList.getText().toString()+"列表中",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                resetView();
            }
        });

        dialog.show();

    }

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        if (musicFragment!=null&&musicFragment.isMutiableCheckShow){
            resetView();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 重置头部和底部的视图
     */
    private void resetView(){
        musicFragment.onKeyBackPress();
        selectedList.clear();
        if (allPlayLists.getSelectedList()!=null) {
            allPlayLists.getSelectedList().clear();
        }
        mVp.setVpScrollAble(true);
        toggleView.setVisibility(View.VISIBLE);
        bottomSmallPalyer.setVisibility(View.VISIBLE);
        chooseTopView.setVisibility(View.GONE);
        chooseBottomView.setVisibility(View.GONE);
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
