package com.miaodao.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;

/**
 * Created by daixinglong on 2017/4/13.
 */

public class TestFrag extends ContentBaseFragment {

    private ListView lv;

    @Override
    public void onResponsSuccess(int TAG, Object result) {

    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_cameraauthent_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {

    }


    @Override
    protected void initEvent() {

    }
}
