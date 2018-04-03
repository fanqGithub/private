package com.commai.commaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commai.commaplayer.R;
import com.commai.commaplayer.greendao.bean.PlayListBean;

import java.util.List;

/**
 * Created by fanqi on 2018/4/3.
 * Description:
 */

public class AddPlayListAdapter extends RecyclerView.Adapter<AddPlayListAdapter.PlayListHolder> {

    private Context mContext=null;
    private List<PlayListBean> listBeans=null;

    public AddPlayListAdapter(Context context,List<PlayListBean> list){
        this.listBeans=list;
        this.mContext=context;
    }

    @Override
    public PlayListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_add_play_list_item, parent, false);
        PlayListHolder holder = new PlayListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlayListHolder holder, int position) {
        if (listBeans!=null) {
            PlayListBean bean = listBeans.get(position);
            holder.name.setText(bean.getPlayListName());
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

        TextView name;

        public PlayListHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_list);
        }
    }
}
