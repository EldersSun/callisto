package com.miaodao.Sys.Widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Utils.SelectorBankAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义选择dialog
 * Created by Home_Pc on 2017/3/14.
 */

public class CustomSelectorDialog {

    private Context context;
    private List<String> dateList = new ArrayList<>();

    private Dialog customViewDialog;
    private Button Custom_Dialog_Selector_cancel, Custom_Dialog_Selector_ok;
    private ListView Custom_Dialog_Selector;
    private TextView Custom_Dialog_title;

    public CustomSelectorDialog(Context context, String title, List<String> dateList) {
        this.context = context;
        this.dateList = dateList;
        iniWidgets(title);
    }

    private String dateString = "";

    public interface onCustomDialogOperationclick {
        /**
         * 确认
         */
        void Confirm(String dateString);

        /**
         * 取消
         */
        void cancel();
    }

    public onCustomDialogOperationclick dialogOperationclick;

    public void setOnDialogOperationclick(onCustomDialogOperationclick dialogOperationclick) {
        this.dialogOperationclick = dialogOperationclick;
    }

    public void show() {
        customViewDialog.show();
    }

    private void iniWidgets(String title) {
        customViewDialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View layout = inflater.inflate(R.layout.v_custom_dialog_selector, null);//获取自定义布局
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                1000);
        layoutParams.setMargins(0, 0, 0, 0);
        customViewDialog.addContentView(layout, layoutParams);
        Custom_Dialog_Selector = (ListView) layout.findViewById(R.id.Custom_Dialog_Selector);
        Custom_Dialog_Selector_cancel = (Button) layout.findViewById(R.id.Custom_Dialog_Selector_cancel);
        Custom_Dialog_Selector_ok = (Button) layout.findViewById(R.id.Custom_Dialog_Selector_ok);
        Custom_Dialog_title = (TextView) layout.findViewById(R.id.Custom_Dialog_title);
        Custom_Dialog_title.setText(title);

        SelectorBankAdapter selectorBankAdapter = new SelectorBankAdapter(context, dateList);
        Custom_Dialog_Selector.setAdapter(selectorBankAdapter);

        Custom_Dialog_Selector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dateString = dateList.get(position);
                dialogOperationclick.Confirm(dateString);
                customViewDialog.dismiss();
            }
        });

        Custom_Dialog_Selector_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOperationclick.cancel();
                customViewDialog.dismiss();
            }
        });

        Custom_Dialog_Selector_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOperationclick.Confirm(dateString);
                customViewDialog.dismiss();
            }
        });
    }

}
