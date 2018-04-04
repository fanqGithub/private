package com.commai.commaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.VideoItem;
import com.commai.commaplayer.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanqi on 2018/3/16.
 * Description:
 */

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoItemHolder>{

    private Context mContext=null;
    private List<VideoItem> list=null;
    private boolean isShowCheckMe=false;

    private Map<Integer, Boolean> map = new HashMap<>();

    public VideoItemAdapter(Context context,List<VideoItem> videoItems){
        this.list=videoItems;
        this.mContext=context;
        initMap();
    }


    @Override
    public VideoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_video_item, parent, false);
        VideoItemHolder holder = new VideoItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoItemHolder holder, int position) {
        VideoItem item=list.get(position);
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
        holder.name.setText(item.getName());
        holder.info.setText("时长："+showM+":"+showS+"  大小："+size+"M");
        Glide.with(mContext).load(item.getThumbImgPath()).into(holder.thumbImg);
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


    class VideoItemHolder  extends RecyclerView.ViewHolder{
        ImageView thumbImg;
        TextView name;
        TextView info;
        CheckBox checkMe;
        public VideoItemHolder(View itemView) {
            super(itemView);
            thumbImg=itemView.findViewById(R.id.video_img);
            name = itemView.findViewById(R.id.video_name);
            info = itemView.findViewById(R.id.video_info);
            checkMe=itemView.findViewById(R.id.checkme_video);
        }
    }
}
