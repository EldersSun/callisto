package com.miaodao.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miaodao.Fragment.Account.bean.Bill;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Communication.OnItemRecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 历史账单适配器
 * Created by Home_Pc on 2017/3/24.
 */

public class HistoryBillAdapter extends RecyclerView.Adapter<HistoryBillAdapter.ViewHold> {
    private Context context;

    public void setMobileUserLoanInfos(List<Bill> mobileUserLoanInfos) {
        this.mobileUserLoanInfos = mobileUserLoanInfos;
        notifyDataSetChanged();
    }

    private List<Bill> mobileUserLoanInfos = new ArrayList<>();

    public void setOnItemRecyclerViewClickListener(OnItemRecyclerViewClickListener onItemRecyclerViewClickListener) {
        this.onItemRecyclerViewClickListener = onItemRecyclerViewClickListener;
    }

    private OnItemRecyclerViewClickListener onItemRecyclerViewClickListener;

    public HistoryBillAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHold viewHold = new ViewHold(LayoutInflater.from(context).inflate(R.layout.v_historybill_item_layout, null));
        return viewHold;
    }

    @Override
    public void onBindViewHolder(final ViewHold holder, final int position) {

        holder.history_Number.setText(mobileUserLoanInfos.get(position).getAmount());
        holder.history_timer.setText(mobileUserLoanInfos.get(position).getApplyDate());

        holder.history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemRecyclerViewClickListener != null) {
                    onItemRecyclerViewClickListener.onItemClickListener(holder.history_layout, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mobileUserLoanInfos == null ? 0 : mobileUserLoanInfos.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {

        TextView history_Number;
        TextView history_timer;
        RelativeLayout history_layout;


        public ViewHold(View itemView) {
            super(itemView);
            history_layout = (RelativeLayout) itemView.findViewById(R.id.history_layout);
            history_Number = (TextView) itemView.findViewById(R.id.history_Number);
            history_timer = (TextView) itemView.findViewById(R.id.history_timer);
        }
    }
}
