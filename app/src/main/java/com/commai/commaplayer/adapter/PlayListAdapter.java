package com.commai.commaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commai.commaplayer.Entity.AllPlayLists;
import com.commai.commaplayer.Entity.SelectedMediaItem;
import com.commai.commaplayer.R;
import com.commai.commaplayer.greendao.bean.PlayListBean;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanqi on 2018/4/3.
 * Description:用在播放列表中的-》创建的播放列表
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListHolder> {

    private Context mContext=null;
    private List<PlayListBean> listBeans=null;
    private Gson gson;

    public PlayListAdapter(Context context, List<PlayListBean> list){
        this.listBeans=list;
        this.mContext=context;
        this.gson=new Gson();
    }

    @Override
    public PlayListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_play_list_item, parent, false);
        PlayListHolder holder = new PlayListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlayListHolder holder, int position) {
        if (listBeans!=null) {
            PlayListBean bean = listBeans.get(position);
            AllPlayLists playLists=gson.fromJson(bean.getPlayListJson(), AllPlayLists.class);
            holder.tvName.setText(bean.getPlayListName());
            holder.tvNum.setText(playLists.getSelectedList().size()+"个媒体文件");
            holder.itemView.setTag(position);
        }

    }

    @Override
    public int getItemCount() {
        if (listBeans!=null){
            return listBeans.size();
        }
        return 0;
    }

    class PlayListHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvNum;

        public PlayListHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_list_name);
            tvNum=itemView.findViewById(R.id.tv_list_size);
        }
    }
}
