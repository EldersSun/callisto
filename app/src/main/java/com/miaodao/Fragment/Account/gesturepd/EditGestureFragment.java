package com.miaodao.Fragment.Account.gesturepd;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.miaodao.Base.ContentBaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Widgets.getsture.GestureContentView;
import com.miaodao.Sys.Widgets.getsture.GestureDrawline;

/**
 * Created by daixinglong on 2017/3/29.
 */

public class EditGestureFragment extends ContentBaseFragment {

    private TextView tvShowCreateState;//显示密码编辑状态
    private FrameLayout gestureContainer, checkGestureContainer;//手势密码容器
    private GestureContentView createGestureView, checkGestureView;//手势密码控件
    private String gesturePd = "";

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_gesture_create, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_tvShow.setText(R.string.security_message_4);
        tvShowCreateState = (TextView) fgView.findViewById(R.id.tv_show_create_state);
        gestureContainer = (FrameLayout) fgView.findViewById(R.id.gesture_container);
        checkGestureContainer = (FrameLayout) fgView.findViewById(R.id.check_gesture_container);

        createGestureView = new GestureContentView(getActivity(), false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {

                if (!isInputPassValidate(inputCode)) {
                    tvShowCreateState.setText(Html.fromHtml("<font color='#c70c1e'>最少连接6个点, 请重新绘制</font>"));
                    createGestureView.clearDrawlineState(0L);
                    return;
                }
                tvShowCreateState.setText(Html.fromHtml("<font color='#666666'>再次绘制解锁图案</font>"));
                createGestureView.clearDrawlineState(0L);
                gesturePd = inputCode;
                gestureContainer.setVisibility(View.GONE);
                checkGestureContainer.setVisibility(View.VISIBLE);
                checkGesture(inputCode);
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });

        //设置手势密码显示到哪个view里面
        createGestureView.setParentView(gestureContainer);
    }


    /**
     * 再次输入手势密码
     *
     * @param inputCode
     */
    private void checkGesture(String inputCode) {
        checkGestureView = new GestureContentView(getActivity(), true, inputCode, new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
            }

            @Override
            public void checkedSuccess() {
                checkGestureView.clearDrawlineState(0L);
                SharedPreferencesUtil.putString(getActivity(), AppConfig.getInstance().GESTURE_PD, gesturePd);
                getActivity().finish();
            }

            @Override
            public void checkedFail() {
                checkGestureView.clearDrawlineState(1300L);
                tvShowCreateState.setText(Html.fromHtml("<font color='#c70c1e'>图案绘制错误,请重新绘制</font>"));
                // 左右移动动画
                Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                tvShowCreateState.startAnimation(shakeAnimation);
            }
        });
        checkGestureView.setParentView(checkGestureContainer);
    }


    @Override
    protected void initEvent() {

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


    /**
     * 判断用输入的验证码是否有效
     *
     * @param inputPassword
     * @return
     */
    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 6) {
            return false;
        }
        return true;
    }


}
