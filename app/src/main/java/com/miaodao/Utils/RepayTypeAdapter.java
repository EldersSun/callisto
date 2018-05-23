package com.miaodao.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Model.RepayType;

import java.util.ArrayList;
import java.util.List;

import static com.fcloud.licai.miaodao.R.id.iv_choose;
import static com.fcloud.licai.miaodao.R.id.rl_parent;

/**
 * Created by daixinglong on 2017/4/13.
 */

public class RepayTypeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<RepayType> repayTypeList = new ArrayList<>();


    public RepayTypeAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }


    public void setRepayTypeList(List<RepayType> repayTypeList) {
        this.repayTypeList = repayTypeList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return repayTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return repayTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_repay_type, null);
            viewHolder = new ViewHolder();
            viewHolder.typeName = (TextView) convertView.findViewById(R.id.type_name);
            viewHolder.typeCardNum = (TextView) convertView.findViewById(R.id.type_card_num);
            viewHolder.typeIcon = (ImageView) convertView.findViewById(R.id.iv_type_icon);
            viewHolder.choose = (ImageView) convertView.findViewById(iv_choose);
            viewHolder.rlParent = (RelativeLayout) convertView.findViewById(rl_parent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RepayType repayType = repayTypeList.get(position);
        viewHolder.typeName.setText(repayType.getInstName());
        Glide.with(mContext).load(AppConfig.getInstance().BASE_BANK + "/" + repayType.getInstId() + ".png").into(viewHolder.typeIcon);

        //如果有卡号,显示卡号
        String carNum = repayType.getCardNo();
        if (!TextUtils.isEmpty(carNum)) {
            viewHolder.typeCardNum.setText("(" + carNum + ")");
        }

        //判断支付渠道是否可用
        if (repayType.getValid() == false) {
            viewHolder.choose.setVisibility(View.GONE);
            viewHolder.rlParent.setBackgroundColor(mContext.getResources().getColor(R.color.background_holo_light));
        } else {
            if (repayType.getIsChoose() == 1) {
                viewHolder.choose.setImageResource(R.drawable.icon_pay_selected);
            } else {
                viewHolder.choose.setImageResource(R.drawable.icon_pay_unselected);
            }
        }

        return convertView;
    }


    class ViewHolder {

        ImageView typeIcon;
        TextView typeName, typeCardNum;
        ImageView choose;
        RelativeLayout rlParent;

    }


}
