package com.miaodao.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaodao.Fragment.Loan.bean.WaitPay;
import com.fcloud.licai.miaodao.R;

import java.util.List;

/**
 * Created by daixinglong on 2017/5/15.
 */

public class RepayAdapter extends RecyclerView.Adapter<RepayAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<WaitPay> waitPays;
    private IRePay listener;


    public interface IRePay {
        void repay();
    }

    public void setListener(IRePay listener) {
        this.listener = listener;
    }

    public RepayAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_wait_repay, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        WaitPay waitPay = waitPays.get(position);
        holder.tvRepayMoney.setText("还款金额 ￥" + waitPay.getUnRepayedAmount());
        holder.tvLoanMoney.setText("贷款金额:" + waitPay.getAmount() + "元");
        holder.tvLoanRate.setText("利息:" + waitPay.getIntegrateFee() + "元");
        holder.tvLoanDate.setText(waitPay.getPayTime() + " - " + waitPay.getRepayDate());

        holder.tvFailRepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.repay();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return waitPays == null ? 0 : waitPays.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRepayMoney;
        private TextView tvLoanMoney;
        private TextView tvLoanRate;
        private TextView tvFailRepay;
        private TextView tvLoanDate;
        private ImageView ivDetail;


        public ViewHolder(View itemView) {
            super(itemView);

            tvRepayMoney = (TextView) itemView.findViewById(R.id.tv_repay_money);
            tvLoanMoney = (TextView) itemView.findViewById(R.id.tv_loan_money);
            tvLoanRate = (TextView) itemView.findViewById(R.id.tv_loan_rate);
            tvFailRepay = (TextView) itemView.findViewById(R.id.tv_fail_repay);
            tvLoanDate = (TextView) itemView.findViewById(R.id.tv_loan_date);
            ivDetail = (ImageView) itemView.findViewById(R.id.iv_detail);
        }
    }


    public void setList(List<WaitPay> waitPays) {
        this.waitPays = waitPays;
        notifyDataSetChanged();
    }

}
