package com.miaodao.Sys.Widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Utils.StringUtils;


/**
 * 自定义对话框
 *
 * @author Home_Pc
 *
 */
public class CustomDialog extends AlertDialog implements
		View.OnClickListener {

	private LinearLayout dialogShowTitleLayout;
	private TextView dialogShowTitle;
	private TextView dialogShowMsg;
	private Button dialogShowCancel,dialogShowSure,dialogShowdismiss;
	private View dialogShowOnlcikLinear;

	private String title;
	private String msg;
	private boolean isShowCancel;

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public CustomDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v_dialogshow_layout);
		iniWidgets();
		init();
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		this.title = title.toString();
	}

	@Override
	public void setMessage(CharSequence message) {
		super.setMessage(message);
		this.msg = message.toString();
	}

	public boolean isShowCancel() {
		return isShowCancel;
	}

	public void setShowCancel(boolean isShowCancel) {
		this.isShowCancel = isShowCancel;
	}

	private void init() {
		if (!StringUtils.isBlank(title)) {
			dialogShowTitleLayout.setVisibility(View.VISIBLE);
			dialogShowTitle.setText(title);
		} else {
			dialogShowTitleLayout.setVisibility(View.GONE);
			dialogShowTitle.setText("");
		}
		if (isShowCancel) {
			dialogShowCancel.setVisibility(View.VISIBLE);
			dialogShowOnlcikLinear.setVisibility(View.VISIBLE);
		} else {
			dialogShowCancel.setVisibility(View.GONE);
			dialogShowOnlcikLinear.setVisibility(View.GONE);
		}
		dialogShowMsg.setText(msg == null ? "" : msg);
		setCancelable(false);
	}

	private void iniWidgets() {
		dialogShowTitleLayout = (LinearLayout) findViewById(R.id.dialogShowTitleLayout);
		dialogShowTitle = (TextView) findViewById(R.id.dialogShowTitle);
		dialogShowMsg = (TextView) findViewById(R.id.dialogShowMsg);
		dialogShowCancel = (Button) findViewById(R.id.dialogShowCancel);
		dialogShowSure = (Button) findViewById(R.id.dialogShowSure);
		dialogShowdismiss = (Button) findViewById(R.id.dialogShowdismiss);
		dialogShowOnlcikLinear = findViewById(R.id.dialogShowOnlcikLinear);
		dialogShowCancel.setOnClickListener(this);
		dialogShowSure.setOnClickListener(this);
		dialogShowdismiss.setOnClickListener(this);
	}

	public void setBtnText(String leftStr, String rightStr) {
		if (!StringUtils.isBlank(leftStr)) {
			dialogShowCancel.setText(leftStr);
		}
		if (!StringUtils.isBlank(rightStr)) {
			dialogShowSure.setText(rightStr);
		}
	}

	public OnSureInterface mOnSureInterface;

	public void setmOnSureInterface(OnSureInterface mOnSureInterface) {
		this.mOnSureInterface = mOnSureInterface;
	}

	public interface OnSureInterface {
		/** 确认，右边 */
		public void getOnSure();

		/** 取消，左边 */
		public void getOnDesmiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.dialogShowSure:
				this.dismiss();
				if (mOnSureInterface != null) {
					mOnSureInterface.getOnSure();
				}
				break;


			case R.id.dialogShowCancel:
				this.dismiss();
				if (mOnSureInterface != null) {
					mOnSureInterface.getOnDesmiss();
				}
				break;

			case R.id.dialogShowdismiss:
				this.dismiss();
				break;

			default:
				break;
		}
	}
}
