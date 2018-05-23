package com.miaodao.Sys.Widgets;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.WebViewFragment;
import com.miaodao.Sys.Config.AppConfig;

/**
 * Created by daixinglong on 2017/4/13.
 */

public class SummaryPop extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater inflater;
    private View popView;

    private ImageView ivCancel;
    private TextView xieyi, xinyong;
    private CheckBox all_user_selector_Agreement;
    private Button all_submit, all_user_Agreement;

    private boolean checked;
    private ISummary iSummary;

    public interface ISummary {

        void upLoad();

        void setChecked(boolean isChecked);
    }

    public SummaryPop(Context context) {

        this.mContext = context;
        mActivity = (Activity) context;
        inflater = LayoutInflater.from(mContext);
        initPopWindow();
        setParams();
        initActions();
    }


    /**
     * 初始化view
     */
    private void initPopWindow() {
        popView = inflater.inflate(R.layout.view_summary_pop, null);
        ivCancel = (ImageView) popView.findViewById(R.id.iv_cancel);
        xieyi = (TextView) popView.findViewById(R.id.xieyi);
        xinyong = (TextView) popView.findViewById(R.id.xinyong);
        all_user_selector_Agreement = (CheckBox) popView.findViewById(R.id.all_user_selector_Agreement);
        all_submit = (Button) popView.findViewById(R.id.all_submit);
        all_user_Agreement = (Button) popView.findViewById(R.id.all_user_Agreement);

    }


    /**
     * 初始化popWindow的参数
     */
    private void setParams() {
        this.setContentView(popView);// 设置View
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        this.setFocusable(false);
        this.setAnimationStyle(R.style.repay_popwindow);// 设置动画
        backgroundAlpha(0.7f);
    }

    private void initActions() {
        ivCancel.setOnClickListener(this);
        xieyi.setOnClickListener(this);
        xinyong.setOnClickListener(this);
        all_submit.setOnClickListener(this);
        all_user_Agreement.setOnClickListener(this);

        all_user_selector_Agreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
                if (iSummary != null) {
                    iSummary.setChecked(checked);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        switch (v.getId()) {

            case R.id.iv_cancel:
                dismiss();
                backgroundAlpha(1);
                break;

            case R.id.xieyi:
                bundle.putString("url", AppConfig.getInstance().LOAN_URL);
                ServiceBaseActivity.startActivity(mActivity, WebViewFragment.class.getName(), bundle);
                break;

            case R.id.xinyong:
                bundle.putString("url", AppConfig.getInstance().XINYONG_URL);
                ServiceBaseActivity.startActivity(mActivity, WebViewFragment.class.getName(), bundle);
                break;

            case R.id.all_submit:
                if (iSummary != null) {
                    iSummary.upLoad();
                    if (checked) {
                        dismiss();
                        backgroundAlpha(1);
                    }
                }
                break;

            case R.id.all_user_Agreement:
                if (all_user_selector_Agreement.isChecked()) {
                    all_user_selector_Agreement.setChecked(false);
                } else {
                    all_user_selector_Agreement.setChecked(true);
                }
                break;

            default:
                break;

        }


    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    public void setISummary(ISummary iSummary) {
        this.iSummary = iSummary;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        all_user_selector_Agreement.setChecked(checked);
    }
}
