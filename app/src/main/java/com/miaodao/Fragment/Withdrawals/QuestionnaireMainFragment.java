package com.miaodao.Fragment.Withdrawals;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.miaodao.Base.BaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.fcloud.licai.miaodao.R;

/**
 * 填写问卷
 * Created by Home_Pc on 2017/3/21.
 */

public class QuestionnaireMainFragment extends BaseFragment implements View.OnClickListener {

    private Button submit;

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_questionnairemain_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        submit = (Button) fgView.findViewById(R.id.btn_question);
    }


    @Override
    protected void initEvent() {
        submit.setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_question:
                ServiceBaseActivity.startActivity(getActivity(), QuestionFragment.class.getName(), null);
                break;
        }
    }
}
