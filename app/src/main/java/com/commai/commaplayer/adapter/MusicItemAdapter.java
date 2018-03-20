package com.commai.commaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.R;
import com.commai.commaplayer.utils.MediaUtil;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;

import java.util.List;

/**
 * Created by fanqi on 2018/3/15.
 * Description:
 */

public class MusicItemAdapter extends RecyclerView.Adapter<MusicItemAdapter.MusicItemHolder>{

    private Context mContext=null;
    private List<AudioItem> list=null;
    private ImageLoader imageLoader;

    public MusicItemAdapter(Context context,List<AudioItem> audioItems){
        this.list=audioItems;
        this.mContext=context;
        this.imageLoader=new ImageLoader(mContext);
    }

    @Override
    public MusicItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_music_item, parent, false);
        MusicItemHolder holder = new MusicItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MusicItemHolder holder, int position) {
        imageLoader.DisplayImage(list.get(position).getPath(),holder.music_img);
        holder.name.setText(list.get(position).getTitle());
        long size = list.get(position).getSize()/1024/1024;
        int minute = list.get(position).getDuration()/1000/60;
        int second = list.get(position).getDuration()/1000%60;
        holder.info.setText("歌手: " + list.get(position).getArtist() +", 时长: " + minute + ":" + second + ",大小：" + size + "M");
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    class MusicItemHolder  extends RecyclerView.ViewHolder{
        ImageView music_img;
        TextView name;
        TextView info;
        public MusicItemHolder(View itemView) {
            super(itemView);
            music_img=itemView.findViewById(R.id.music_img);
            name = itemView.findViewById(R.id.music_name);
            info = itemView.findViewById(R.id.detail_txt);
        }
    }
}
