package com.miaodao.Sys.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;


public class LoginDialog extends Dialog {
    Button btn_submit;
    Button btn_cancle;
    EditText et_password;
    TextView tvPhoneNum;
    Context mctx;
    private OnClickListener listener;

    public LoginDialog(Context context, OnClickListener listener) {
        super(context, R.style.pwdDialog);
        this.mctx = context;
        this.listener = listener;
        initView();
    }


    public void setTvPhoneNum(String tvPhoneNum) {
        this.tvPhoneNum.setText(tvPhoneNum);
    }

    public void setTvPhoneNum(int tvPhoneNum) {
        this.tvPhoneNum.setText(tvPhoneNum);
    }

    public EditText getPDView() {
        return et_password;
    }

    public interface OnClickListener {
        void onCancel();
        void onSure(String pwd);
    }

    private void initView() {
        View view = View.inflate(mctx, R.layout.v_login_pd, null);
//        setContentView(view);
        getWindow().getAttributes().gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        getWindow().getAttributes().height = (int) mctx.getResources().getDimension(R.dimen.d200);
        getWindow().getAttributes().width = (int) mctx.getResources().getDimension(R.dimen.d300);
        getWindow().getAttributes().verticalMargin = 0.2f;
        setContentView(view);

        et_password = (EditText) view.findViewById(R.id.et_pwd);
        tvPhoneNum = (TextView) view.findViewById(R.id.tv_phone_num);
        btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel();
                }
                dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSure(et_password.getText().toString().trim());
                }
                dismiss();
            }
        });
    }
}
