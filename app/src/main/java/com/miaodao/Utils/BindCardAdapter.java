package com.miaodao.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Model.Channels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daixinglong on 2017/4/5.
 */

public class BindCardAdapter extends RecyclerView.Adapter<BindCardAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Channels> channelsList = new ArrayList<>();

    public BindCardAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.v_bank_list_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bankName.setText(position + 1 + "、" + channelsList.get(position).getInstName());
    }

    @Override
    public int getItemCount() {
        return channelsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bankName;

        public ViewHolder(View itemView) {
            super(itemView);

            bankName = (TextView) itemView.findViewById(R.id.tv_bank_name);
        }
    }

    public void setChannelsList(List<Channels> channelsList) {
        this.channelsList.clear();
        this.channelsList = channelsList;
        notifyDataSetChanged();
    }


}
