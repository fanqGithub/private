package com.commai.commaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.R;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanqi on 2018/3/15.
 * Description:
 */

public class MusicItemAdapter extends RecyclerView.Adapter<MusicItemAdapter.MusicItemHolder>{

    private Context mContext=null;
    private List<AudioItem> list=null;
    private ImageLoader imageLoader;
    private boolean isShowCheckMe=false;

    private Map<Integer, Boolean> map = new HashMap<>();

    public MusicItemAdapter(Context context,List<AudioItem> audioItems){
        this.list=audioItems;
        this.mContext=context;
        this.imageLoader=new ImageLoader(mContext);
        initMap();
    }

    @Override
    public MusicItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_music_item, parent, false);
        MusicItemHolder holder = new MusicItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MusicItemHolder holder, final int position) {
        final int mPosition=position;
        AudioItem item=list.get(position);
        imageLoader.DisplayImage(item.getPath(),holder.music_img);
        holder.name.setText(item.getTitle());
        long size = item.getSize()/1024/1024;
        int minute = item.getDuration()/1000/60;
        int second = item.getDuration()/1000%60;
        String showM="";
        String showS="";
        if (minute<10){
            showM="0"+minute;
        }else {
            showM=minute+"";
        }
        if (second<10){
            showS="0"+second;
        }else {
            showS=second+"";
        }
        holder.checkMe.setTag(position);
        holder.checkMe.setOnCheckedChangeListener(null);
        holder.info.setText("时长: " + showM + ":" + showS);
        holder.artist_alum.setText(item.getArtist()+"——"+item.getAlbum());

        holder.checkMe.setChecked(map.get(position));

        if (isShowCheckMe){
            holder.checkMe.setVisibility(View.VISIBLE);
        }else {
            holder.checkMe.setVisibility(View.GONE);
        }
        holder.itemView.setTag(position);
    }

    public void notifyCheckMeShow(){
        this.isShowCheckMe=true;
        notifyDataSetChanged();
    }

    public void notifyCheckMeHidel(){
        this.isShowCheckMe=false;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    private void initMap() {
        for (int i = 0; i < list.size(); i++) {
            map.put(i, false);
        }
    }

    public  Map<Integer, Boolean> getSelectedData() {
        return map;
    }

    class MusicItemHolder  extends RecyclerView.ViewHolder{
        ImageView music_img;
        TextView name;
        TextView info;
        TextView artist_alum;
        CheckBox checkMe;
        public MusicItemHolder(View itemView) {
            super(itemView);
            music_img=itemView.findViewById(R.id.music_img);
            name = itemView.findViewById(R.id.music_name);
            info = itemView.findViewById(R.id.detail_txt);
            artist_alum=itemView.findViewById(R.id.artist_alum);
            checkMe=itemView.findViewById(R.id.checkme);
        }
    }
}
