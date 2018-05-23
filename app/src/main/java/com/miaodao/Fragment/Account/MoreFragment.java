package com.miaodao.Fragment.Account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.WebViewFragment;
import com.miaodao.Sys.Config.AppConfig;

/**
 * Description:
 * Author:     daixinglong
 * Version     V1.0
 * Date:       2017/6/20 10:01
 */

public class MoreFragment extends ContentBaseFragment implements View.OnClickListener {

    private RelativeLayout moreAbout, moreAdvise, moreShengming;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_more, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText("更多");
        moreAbout = (RelativeLayout) fgView.findViewById(R.id.more_about);
        moreAdvise = (RelativeLayout) fgView.findViewById(R.id.more_advise);
        moreShengming = (RelativeLayout) fgView.findViewById(R.id.more_shengming);
    }

    @Override
    protected void initEvent() {
        moreShengming.setOnClickListener(this);
        moreAdvise.setOnClickListener(this);
        moreAbout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Bundle moreBundle = new Bundle();
        switch (v.getId()) {

            case R.id.more_about:
                moreBundle.putString("url", AppConfig.getInstance().ABOUT_URL);
                moreBundle.putString("title", "关于麦子秒到");
                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), moreBundle);
                break;

            case R.id.more_advise:
                ServiceBaseActivity.startActivity(getActivity(), AdviseFragment.class.getName());
                break;

            case R.id.more_shengming:
                moreBundle.putString("url", AppConfig.getInstance().REGIST_URL);
                moreBundle.putString("title", "免责声明");
                ServiceBaseActivity.startActivity(getActivity(), WebViewFragment.class.getName(), moreBundle);
                break;

            default:
                break;

        }

    }


    @Override
    public void onResponsSuccess(int TAG, Object result) {

    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }


}
