package com.miaodao.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miaodao.Fragment.Account.bean.Card;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Communication.OnItemRecyclerViewClickListener;
import com.miaodao.Sys.Config.AppConfig;

import org.json.JSONObject;

/**
 * 银行卡adapter
 * Created by Home_Pc on 2017/3/10.
 */

public class AccountCarShowAdapter extends RecyclerView.Adapter<AccountCarShowAdapter.ViewHold> {
    private Context context;
    private Card myCard;
    private JSONObject colorMap;

    private OnItemRecyclerViewClickListener onItemRecyclerViewClickListener;

    public void setOnItemRecyclerViewClickListener(OnItemRecyclerViewClickListener onItemRecyclerViewClickListener) {
        this.onItemRecyclerViewClickListener = onItemRecyclerViewClickListener;
    }

    public AccountCarShowAdapter(Context context) {
        this.context = context;
        myCard = new Card();
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHold hold = new ViewHold(LayoutInflater.from(context).inflate(R.layout.v_accountcat_item_layout, null));
        return hold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {
        holder.cardType.setText(myCard.getCardType());
        holder.cardName.setText(myCard.getInstName());
        holder.cardAccount.setText(myCard.getCardIdxNo());
        String bankIcon = myCard.getInstId();
        if (!TextUtils.isEmpty(bankIcon)) {
//            Glide.with(context).load(MResource.getResource(myCard.get("instId").toLowerCase(), context)).into(holder.cardImg);
            Glide.with(context).load(AppConfig.getInstance().BASE_BANK + "/" + myCard.getInstId() + ".png").into(holder.cardImg);
            if (colorMap == null) return;

            String bankColor = colorMap.optString(bankIcon);
            if (!TextUtils.isEmpty(bankColor) && !bankColor.equals(null)) {
                GradientDrawable myGrad = (GradientDrawable) holder.itemLayout.getBackground();
                myGrad.setColor(Color.parseColor(bankColor));
            }

        }

        holder.itemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemRecyclerViewClickListener != null) {
                    onItemRecyclerViewClickListener.onItemClickListener(null, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myCard == null ? 0 : 1;
    }

    class ViewHold extends RecyclerView.ViewHolder {
        RelativeLayout itemLayout;
        ImageView cardImg;
        TextView cardName;
        TextView cardType;
        Button itemDetail;
        TextView cardAccount;

        public ViewHold(View itemView) {
            super(itemView);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.accountCar_Item_Layout);
            cardImg = (ImageView) itemView.findViewById(R.id.accountCar_Item_imgShow);
            cardName = (TextView) itemView.findViewById(R.id.accountCar_Item_CarName);
            cardType = (TextView) itemView.findViewById(R.id.accountCar_Item_CarType);
            itemDetail = (Button) itemView.findViewById(R.id.accountCar_Item_CarSet);
            cardAccount = (TextView) itemView.findViewById(R.id.accountCar_Item_CarNum);
        }
    }

    public void setCardList(Card myCard) {
        this.myCard = myCard;
        notifyDataSetChanged();
    }

    public void setBackGround(JSONObject colorMap) {
        this.colorMap = colorMap;
        notifyDataSetChanged();
    }
}
