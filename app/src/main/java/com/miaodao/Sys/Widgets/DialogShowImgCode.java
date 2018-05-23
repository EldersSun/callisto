package com.miaodao.Sys.Widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Utils.StringUtils;

/**
 * 主要获取图形验证码的dialog
 * Created by Home_Pc on 2017/3/14.
 */

public class DialogShowImgCode {

    private Context context;
    private Dialog customViewDialog;
    private View layout;

    private EditText dialogShowImg_inputVaule;
    private ImageView dialogShowImg_imgShow;
    private Button dialogShowImg_cancel;
    private Button dialogShowImg_ok;

    public DialogShowImgCode(Context context) {
        this.context = context;
        init();
    }

    public interface onDialogOperationclick {
        /**
         * 确认
         */
        void Confirm(String imgCode);

        /**
         * 取消
         */
        void cancel();


        void reGet();
    }

    public onDialogOperationclick dialogOperationclick;

    public void setOnDialogOperationclick(onDialogOperationclick dialogOperationclick) {
        this.dialogOperationclick = dialogOperationclick;
    }

    private void init() {
        customViewDialog = new Dialog(context, R.style.dialog);
        customViewDialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        layout = inflater.inflate(R.layout.v_showimgcode_layout, null);//获取自定义布局
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        customViewDialog.addContentView(layout, layoutParams);

        dialogShowImg_inputVaule = (EditText) layout.findViewById(R.id.dialogShowImg_inputVaule);
        dialogShowImg_imgShow = (ImageView) layout.findViewById(R.id.dialogShowImg_imgShow);
        dialogShowImg_cancel = (Button) layout.findViewById(R.id.dialogShowImg_cancel);
        dialogShowImg_ok = (Button) layout.findViewById(R.id.dialogShowImg_ok);

        dialogShowImg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOperationclick.cancel();
                customViewDialog.dismiss();
            }
        });

        dialogShowImg_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOperationclick.Confirm(dialogShowImg_inputVaule.getText().toString() == null ? "" :
                        dialogShowImg_inputVaule.getText().toString());
                customViewDialog.dismiss();
            }
        });

        dialogShowImg_imgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOperationclick.reGet();
                customViewDialog.dismiss();
            }
        });

    }

    public void showImgDialog(Bitmap bitmap) {
        if (!StringUtils.isBlank(dialogShowImg_inputVaule.getText().toString())) {
            dialogShowImg_inputVaule.setText("");
        }
        dialogShowImg_imgShow.setImageBitmap(bitmap);
        customViewDialog.show();
    }
}
