package com.miaodao.Fragment.Account.gesturepd;


import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;

/**
 *
 */
public class GestureMainFragment extends ContentBaseFragment implements View.OnClickListener {

    public static final String GESTURE_WORK = "gestureWork";

    private RelativeLayout rlModify;//修改手势密码,默认隐藏
    private CheckBox showGesture;//是否开始手势密码
    private String gesturePd;//保存的手势密码

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_gesture_main, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.security_message_2);
        rlModify = (RelativeLayout) fgView.findViewById(R.id.rl_modify);
        showGesture = (CheckBox) fgView.findViewById(R.id.gesture_checkbox);
    }

    @Override
    protected void initEvent() {
        rlModify.setOnClickListener(this);
        showGesture.setOnClickListener(this);
//        showGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                //如果没有密码那么就是跳转到手势密码编辑页面
//                if (TextUtils.isEmpty(gesturePd)) {
//                    ServiceBaseActivity.startActivity(getActivity(), EditGestureFragment.class.getName());
//                } else {
//                    //如果有密码，那么久跳转到验证手势密码页面
//                }
//
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();

        gesturePd = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().GESTURE_PD, "");
        if (TextUtils.isEmpty(gesturePd)) {
            rlModify.setVisibility(View.GONE);
            showGesture.setChecked(false);
        } else {
            rlModify.setVisibility(View.VISIBLE);
            showGesture.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_modify:
                ServiceBaseActivity.startActivity(getActivity(), ModifyGestureFragment.class.getName());
                break;

            case R.id.gesture_checkbox:
                //如果没有密码那么就是跳转到手势密码编辑页面
                if (TextUtils.isEmpty(gesturePd)) {
                    ServiceBaseActivity.startActivity(getActivity(), EditGestureFragment.class.getName());
                } else {
                    //如果有密码，那么就跳转到验证手势密码页面
//                    ServiceBaseActivity.startActivity(getActivity(), LoginGestureActivity.class.getName());
                    Intent intent = new Intent(getActivity(), LoginGestureActivity.class);
                    intent.putExtra(GESTURE_WORK, "shutGesture");
                    startActivity(intent);
                }
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
