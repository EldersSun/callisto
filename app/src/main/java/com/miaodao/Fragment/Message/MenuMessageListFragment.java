package com.miaodao.Fragment.Message;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Utils.MenuMsgAdapter;

/**
 * title消息列表
 * Created by Home_Pc on 2017/3/29.
 */
public class MenuMessageListFragment extends ContentBaseFragment {

    private RecyclerView menumsg_recycle;
    private MenuMsgAdapter adapter;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_menumessage_layout,null);
    }

    @Override
    protected void initWidgets(View fgView) {
        menumsg_recycle = (RecyclerView) fgView.findViewById(R.id.menumsg_recycle);
    }

    @Override
    protected void initEvent() {
        adapter = new MenuMsgAdapter(getActivity());
        menumsg_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        menumsg_recycle.setAdapter(adapter);
        //添加分割线
        menumsg_recycle.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL));
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
