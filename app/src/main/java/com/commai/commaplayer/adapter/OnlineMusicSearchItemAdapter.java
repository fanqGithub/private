package com.commai.commaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commai.commaplayer.Entity.SearchMusicResponse;
import com.commai.commaplayer.R;
import com.commai.commaplayer.utils.imageLoader.ImageLoader;

import java.util.List;

/**
 * Created by fanqi on 2018/3/15.
 * Description:
 */

public class OnlineMusicSearchItemAdapter extends RecyclerView.Adapter<OnlineMusicSearchItemAdapter.SearchItemHolder>{

    private Context mContext=null;
    private List<SearchMusicResponse.ResultBean.SongsBean> list=null;

    public OnlineMusicSearchItemAdapter(Context context, List<SearchMusicResponse.ResultBean.SongsBean> audioItems){
        this.list=audioItems;
        this.mContext=context;
    }

    @Override
    public SearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_online_search_list_iten, parent, false);
        SearchItemHolder holder = new SearchItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SearchItemHolder holder, final int position) {
        final int mPosition=position;
        SearchMusicResponse.ResultBean.SongsBean item=list.get(position);
        Glide.with(mContext).load(item.getAlbum().getBlurPicUrl()).into(holder.music_img);

        holder.name.setText(item.getName());
        holder.artist_alum.setText(item.getAlbum().getName()+" - "+item.getArtists().get(0).getName());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    class SearchItemHolder  extends RecyclerView.ViewHolder{
        ImageView music_img;
        TextView name;
        TextView info;
        TextView artist_alum;
        public SearchItemHolder(View itemView) {
            super(itemView);
            music_img=itemView.findViewById(R.id.online_music_img);
            name = itemView.findViewById(R.id.online_music_name);
            artist_alum=itemView.findViewById(R.id.online_artist_alum);
        }
    }
}
