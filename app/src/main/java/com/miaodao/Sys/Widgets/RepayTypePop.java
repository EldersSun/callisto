package com.miaodao.Sys.Widgets;

import android.app.Activity;
import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Model.RepayType;
import com.miaodao.Utils.RepayTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daixinglong on 2017/4/13.
 */

public class RepayTypePop extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater inflater;
    private View popView;

    private ImageView cancelPay;//取消支付图标
    private TextView repayMoney;//需要支付的金额
    private TextView tvShowDetail;//支付的详情
    private ListView repayType;//支持的支付方式列表
    private LinearLayout repay;//立即支付
    private ImageView ivWait;//安全支付图标
    private ProgressBar repaying;//支付等待图标
    private IActionListener listener;

    private RepayTypeAdapter repayTypeAdapter;
    private List<RepayType> repayTypeList = new ArrayList<>();

    public interface IActionListener {
        //点击 x 图标
        void cancelPay();

        //点击立即支付
        void payNow(ImageView ivWait, ProgressBar repaying, View clickView);

        //
        void payItem(int position);
    }


    public RepayTypePop(Context context) {

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
        popView = inflater.inflate(R.layout.view_repay_pop, null);
        cancelPay = (ImageView) popView.findViewById(R.id.iv_cancel_pay);
        repayMoney = (TextView) popView.findViewById(R.id.repay_money);
        tvShowDetail = (TextView) popView.findViewById(R.id.tv_show_detail);
        repayType = (ListView) popView.findViewById(R.id.repay_type);
        repay = (LinearLayout) popView.findViewById(R.id.repay);
        ivWait = (ImageView) popView.findViewById(R.id.iv_wait);
        repaying = (ProgressBar) popView.findViewById(R.id.repaying);

        repayTypeAdapter = new RepayTypeAdapter(mContext);
        repayType.setAdapter(repayTypeAdapter);
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
        cancelPay.setOnClickListener(this);
        repay.setOnClickListener(this);
        repayType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.payItem(position);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_cancel_pay:
                dismiss();
                backgroundAlpha(1);
                break;

            case R.id.repay:
                if (listener != null) {
                    listener.payNow(ivWait, repaying, repay);
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


    public void setListener(IActionListener listener) {
        this.listener = listener;
    }

    /**
     * 设置支付渠道
     *
     * @param repayTypeList
     */
    public void setRepayTypeList(List<RepayType> repayTypeList) {
        repayTypeAdapter.setRepayTypeList(repayTypeList);
    }


    /**
     * 设置支付金额
     *
     * @param money
     */
    public void setRepayMoney(String money) {
        repayMoney.setText(money);
    }

    public void setDetail(Spanned detail){
        tvShowDetail.setText(detail);
    }

}
