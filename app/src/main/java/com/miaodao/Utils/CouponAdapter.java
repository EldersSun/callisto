package com.miaodao.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Model.Coupon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daixinglong on 2017/5/6.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private final String MONEY_COUPON = "001";//抵扣券
    private final String RATE_COUPON = "002";//免息券

    private final String STATE_UNUSE = "0";//未使用
    private final String STATE_USED = "1";//已使用
    private final String STATE_DELAY = "2";//已过期


    private Context mContext;
    private List<Coupon> couponList;
    private LayoutInflater inflater;
    private String useCouponFrom;

    private IUseCoupon iUseCoupon;

    public interface IUseCoupon {
        void useCoupon(Coupon coupon);
    }

    public CouponAdapter(Context context, String useCouponFrom) {
        this.mContext = context;
        couponList = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
        this.useCouponFrom = useCouponFrom;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_coupon_can_use, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Coupon coupon = couponList.get(position);

        if (MONEY_COUPON.equals(coupon.getCouponType())) {
            holder.tvCouponType.setText("元");
            holder.tvCouponTypeName.setText("- 抵扣券 -");
            holder.tvCoupName.setText(coupon.getValue() + "元抵扣券");
        } else {
            holder.tvCouponType.setText("天");
            holder.tvCouponTypeName.setText("- 免息券 -");
            holder.tvCoupName.setText(coupon.getValue() + "天免息券");
        }

        String state = coupon.getState();

        if (STATE_UNUSE.equals(state)) {
            holder.llLeftParent.setBackgroundResource(R.drawable.coupon_can_use_bg);
            if (coupon.isAvailable()) {
                holder.tvCouponUseBtn.setVisibility(View.VISIBLE);
            } else {
                holder.tvCouponUseBtn.setVisibility(View.GONE);
            }
            holder.ivCouponUsed.setVisibility(View.GONE);
            holder.tvCouponUseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iUseCoupon != null) {
                        iUseCoupon.useCoupon(coupon);
                    }
                }
            });

        } else if (STATE_USED.equals(state)) {
            holder.llLeftParent.setBackgroundResource(R.drawable.coupon_can_not_use_bg);
            holder.tvCouponUseBtn.setVisibility(View.GONE);
            holder.ivCouponUsed.setVisibility(View.VISIBLE);
        } else {
            holder.llLeftParent.setBackgroundResource(R.drawable.coupon_can_not_use_bg);
            holder.tvCouponUseBtn.setVisibility(View.GONE);
            holder.ivCouponUsed.setVisibility(View.GONE);
        }

        if ("account".equals(useCouponFrom)) {
            holder.tvCouponUseBtn.setVisibility(View.GONE);
        }
        holder.tvCouponValue.setText(coupon.getValue());
        holder.tvCouponExpired.setText(coupon.getExpiredDate());
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llLeftParent;
        private TextView tvCouponValue;
        private TextView tvCouponType;
        private TextView tvCouponTypeName;
        private TextView tvCoupName;
        private TextView tvCouponExpired;
        private TextView tvCouponUseBtn;
        private ImageView ivCouponUsed;
        private ImageView ivDetail;


        public ViewHolder(View itemView) {
            super(itemView);

            llLeftParent = (LinearLayout) itemView.findViewById(R.id.ll_left_parent);
            tvCouponValue = (TextView) itemView.findViewById(R.id.tv_coupon_value);
            tvCouponType = (TextView) itemView.findViewById(R.id.tv_coupon_type);
            tvCouponTypeName = (TextView) itemView.findViewById(R.id.tv_coupon_type_name);
            tvCoupName = (TextView) itemView.findViewById(R.id.tv_coup_name);
            tvCouponExpired = (TextView) itemView.findViewById(R.id.tv_coupon_expired);
            tvCouponUseBtn = (TextView) itemView.findViewById(R.id.tv_coupon_use_btn);
            ivCouponUsed = (ImageView) itemView.findViewById(R.id.iv_coupon_used);
            ivDetail = (ImageView) itemView.findViewById(R.id.iv_detail);


        }
    }


    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
        notifyDataSetChanged();
    }

    public void clear(){
        this.couponList.clear();
        notifyDataSetChanged();
    }


    public void setiUseCoupon(IUseCoupon iUseCoupon) {
        this.iUseCoupon = iUseCoupon;
    }
}
