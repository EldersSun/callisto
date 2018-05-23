package com.miaodao.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;

/**
 * Created by Home_Pc on 2017/3/29.
 */

public class MenuMsgAdapter extends RecyclerView.Adapter<MenuMsgAdapter.ViewHold>{
    private Context context;

    public MenuMsgAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHold viewHold = new ViewHold(LayoutInflater.from(context).inflate(R.layout.v_menumsg_item_layout,null));
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHold extends RecyclerView.ViewHolder{
        TextView menumsg_item_tvShow;

        public ViewHold(View itemView) {
            super(itemView);
            menumsg_item_tvShow = (TextView) itemView.findViewById(R.id.menumsg_item_tvShow);
        }
    }
}
