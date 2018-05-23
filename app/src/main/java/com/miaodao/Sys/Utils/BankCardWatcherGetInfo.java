package com.miaodao.Sys.Utils;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.maizi.http.OkHttpUtils;
import com.maizi.http.callback.StringCallback;
import com.miaodao.Fragment.Loan.Apply.bean.BankBin;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class BankCardWatcherGetInfo implements TextWatcher {

    private Context context;
    private EditText et;
    private TextView tv;
    private TextView scaleTextView;
    private int requestLength;

    public BankCardWatcherGetInfo(Context context, EditText editText, TextView textView, int length) {
        et = editText;
        tv = textView;
        requestLength = length;
        this.context = context;
    }

    public BankCardWatcherGetInfo(Context context, EditText editText, TextView textView, TextView scaleTextView, int length) {
        et = editText;
        tv = textView;
        requestLength = length;
        this.scaleTextView = scaleTextView;
        this.context = context;
    }

    int beforeTextLength = 0;
    int onTextLength = 0;
    boolean isChanged = false;

    int location = 0;// 记录光标的位置
    private char[] tempChar;
    private StringBuffer buffer = new StringBuffer();
    int konggeNumberB = 0;

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
        onTextLength = s.length();


        if (TextUtils.isEmpty(s.toString().trim())) {
            scaleTextView.setVisibility(View.GONE);
        } else {
            scaleTextView.setVisibility(View.VISIBLE);
            scaleTextView.setText(s.toString());
        }

        buffer.append(s.toString());
        if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
            isChanged = false;
            return;
        }
        isChanged = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub
        beforeTextLength = s.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        konggeNumberB = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                konggeNumberB++;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        if (isChanged) {
            location = et.getSelectionEnd();
            int index = 0;
            while (index < buffer.length()) {
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }

            index = 0;
            int konggeNumberC = 0;
            while (index < buffer.length()) {
                if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                    buffer.insert(index, ' ');
                    konggeNumberC++;
                }
                index++;
            }

            if (konggeNumberC > konggeNumberB) {
                location += (konggeNumberC - konggeNumberB);
            }

            tempChar = new char[buffer.length()];
            buffer.getChars(0, buffer.length(), tempChar, 0);
            String str = buffer.toString();
            if (location > str.length()) {
                location = str.length();
            } else if (location < 0) {
                location = 0;
            }

            et.setText(str);
            scaleTextView.setVisibility(View.VISIBLE);
            scaleTextView.setText(str);
            Editable etable = et.getText();
            Selection.setSelection(etable, location);
            isChanged = false;


            String tem = et.getText().toString().trim();
            if (TextUtils.isEmpty(tem)) {
                scaleTextView.setVisibility(View.GONE);
                return;
            }
            tem = tem.replaceAll(" ", "");
            if (tem.length() == requestLength) {
                submitQueryCardBankJSON(tem);
            }
        }
    }


    private void submitQueryCardBankJSON(String tem) {

        final Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtil.getString(context, AppConfig.getInstance().RESULT_USERID, ""));
        map.put("tokenId", SharedPreferencesUtil.getString(context, AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        map.put("cardNo", tem);

        OkHttpUtils.post().content(StringUtils.toJsonString(map)).url(StringUtils.toUtl(AppConfig.getInstance().queryBankByNo))
                .build().execute(new StringCallback() {
            @Override
            public void onError(String msg, Call call, Exception e, int id) {
                if (!TextUtils.isEmpty(msg)) {
                    ToastUtils.shortShow(msg);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response))
                    return;

                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.optString("data");

                    if (data != null) {
                        JSONObject dataObject = new JSONObject(data);
                        String info = dataObject.optString("cardInfo");
                        BankBin card = JSON.parseObject(info, BankBin.class);
                        if (card != null) {
                            tv.setText(card.getBankName());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
