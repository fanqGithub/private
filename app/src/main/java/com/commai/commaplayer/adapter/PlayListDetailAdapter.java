package com.commai.commaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commai.commaplayer.Entity.SelectedMediaItem;
import com.commai.commaplayer.R;
import com.commai.commaplayer.widget.SuperscriptView;

import java.util.List;

/**
 * Created by fanqi on 2018/4/4.
 * Description:
 */

public class PlayListDetailAdapter extends RecyclerView.Adapter<PlayListDetailAdapter.DetailListItemHolder> {

    private Context mContext=null;
    private List<SelectedMediaItem> list=null;

    public PlayListDetailAdapter(Context mContext, List<SelectedMediaItem> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public DetailListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_play_list_detail_item, parent, false);
        DetailListItemHolder holder=new DetailListItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DetailListItemHolder holder, int position) {
        SelectedMediaItem item=list.get(position);
        holder.media_name.setText(item.getMediaName());
        holder.media_info.setText("");
        if ("audio".equals(item.getMediaType())){
            holder.superscriptView.setText("音频");
        }else {
            holder.superscriptView.setText("视频");
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

    class DetailListItemHolder  extends RecyclerView.ViewHolder{
        TextView media_name;
        TextView media_info;
        SuperscriptView superscriptView;
        public DetailListItemHolder(View itemView) {
            super(itemView);
            media_name=itemView.findViewById(R.id.media_name);
            media_info = itemView.findViewById(R.id.media_info);
            superscriptView=itemView.findViewById(R.id.tv_superscript);
        }
    }
}
