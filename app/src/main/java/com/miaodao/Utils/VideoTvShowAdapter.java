package com.miaodao.Utils;

import android.content.Context;
import android.support.v13.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Utils.ArrayUtils;

import java.util.List;

/**
 * Created by Home_Pc on 2017/3/15.
 */

public class VideoTvShowAdapter extends BaseAdapter {

    private Context context;
    private List<String> dateString;

    private int colorPosition = -1;

    public void setColorPosition(int colorPosition) {
        this.colorPosition = colorPosition;
        this.notifyDataSetChanged();
    }


    public VideoTvShowAdapter(Context context){
        this.context = context;
        dateString = ArrayUtils.arrayToListForString(context.getResources().getStringArray(R.array.Viedo_message_4));
    }


    @Override
    public int getCount() {
        return dateString.size();
    }

    @Override
    public Object getItem(int position) {
        return dateString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if(convertView == null){
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.v_viedotvshow_item_layout,null);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.Viedo_TvShow_item = (TextView) convertView.findViewById(R.id.Viedo_TvShow_item);
        viewHold.Viedo_TvShow_item.setText(dateString.get(position) == null ? "" : dateString.get(position));

        if(colorPosition >= position){
            viewHold.Viedo_TvShow_item.setTextColor(ActivityCompat.getColor(context,R.color.appColor));
        } else {
            viewHold.Viedo_TvShow_item.setTextColor(ActivityCompat.getColor(context,R.color.textColor));
        }

        return convertView;
    }

    class ViewHold{
        TextView Viedo_TvShow_item;
    }
}
