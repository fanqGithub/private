package com.commai.commaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commai.commaplayer.Entity.AudioItem;
import com.commai.commaplayer.Entity.RecentPlay;
import com.commai.commaplayer.R;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;
import com.commai.commaplayer.widget.SuperscriptView;

import java.util.List;

/**
 * Created by fanqi on 2018/4/2.
 * Description:
 */

public class RecentPlayAdapter extends RecyclerView.Adapter<RecentPlayAdapter.RecentItemHolder>{

    private Context mContext=null;
    private List<RecentPlay> list=null;
    private ImageLoader imageLoader;

    public RecentPlayAdapter(Context context,List<RecentPlay> Items){
        this.list=Items;
        this.mContext=context;
        this.imageLoader=new ImageLoader(context);
    }

    @Override
    public RecentItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_recent_play_item, parent, false);
        RecentItemHolder holder=new RecentItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecentItemHolder holder, int position) {
        RecentPlay recentItem=list.get(position);
        if ("audio".equals(recentItem.getMediaType())){
            imageLoader.DisplayImage(recentItem.getMediaPath(),holder.recent_img);
            holder.recent_title.setText(recentItem.getMediaName());
//            holder.superscriptView.setText("音频");
        }else {
            Glide.with(mContext).load(recentItem.getThumbImgPath()).into(holder.recent_img);
            holder.recent_title.setText(recentItem.getMediaName());
//            holder.superscriptView.setText("视频");
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }


    class RecentItemHolder  extends RecyclerView.ViewHolder{
        ImageView recent_img;
        TextView recent_title;
        SuperscriptView superscriptView;
        public RecentItemHolder(View itemView) {
            super(itemView);
            recent_img=itemView.findViewById(R.id.media_img);
            recent_title = itemView.findViewById(R.id.media_title);
            superscriptView=itemView.findViewById(R.id.tv_superscript);
        }
    }

}
