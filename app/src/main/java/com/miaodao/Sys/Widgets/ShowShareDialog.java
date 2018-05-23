package com.miaodao.Sys.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;


public class ShowShareDialog extends Dialog implements View.OnClickListener {
    private ImageView ivCancel;
    private Context mCtx;
    private TextView tvShare;
    private Button btnShare;
    private IShare listener;

    public interface IShare {
        void share();
    }

    public ShowShareDialog(Context context) {
        super(context, R.style.share_dialog);
        mCtx = context;
        init();
    }

    private void init() {
        View view = View.inflate(mCtx, R.layout.v_share_dialog, null);
        setContentView(view);
        ivCancel = (ImageView) view.findViewById(R.id.iv_cancel);
        btnShare = (Button) view.findViewById(R.id.btn_share);
        tvShare = (TextView) view.findViewById(R.id.tv_share);
        ivCancel.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                dismiss();
                break;

            case R.id.btn_share:
                if (listener != null) {
                    listener.share();
                }
                dismiss();
                break;

            default:
                break;
        }
    }


    public void setText(String notice) {
        tvShare.setText(notice);
    }

    public void setText(int notice) {
        tvShare.setText(notice);
    }

    public void setListener(IShare listener) {
        this.listener = listener;
    }
}
