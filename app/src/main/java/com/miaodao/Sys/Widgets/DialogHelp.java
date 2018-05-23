package com.miaodao.Sys.Widgets;

import android.content.Context;

import com.miaodao.Application.WheatFinanceApplication;
import com.miaodao.Sys.Widgets.CustomDialog.OnSureInterface;

import com.miaodao.Sys.Utils.StringUtils;

/**
 * DialogHelpUtil Created by Home_Pc on 2016/7/27.
 */
public class DialogHelp {
	private Context context;

	public static DialogHelp getInstance(Context context) {
		return new DialogHelp(context);
	}

	public DialogHelp(Context context) {
		this.context = context;
	}

	/** 显示取消按钮 */
	public void showDialog(String Title, String msg,
						   OnSureInterface mOnSureInterface) {
		showDialog(Title, msg, true, null, null, mOnSureInterface);
	}

	/** 显示取消按钮 */
	public void showDialog(String Title, String msg, String leftStr,
						   String rightStr, OnSureInterface mOnSureInterface) {
		showDialog(Title, msg, true, leftStr, rightStr, mOnSureInterface);
	}

	public void showDialog(int Title, int msg, OnSureInterface mOnSureInterface) {
		showDialog(
				WheatFinanceApplication.getInstance().getResources().getString(Title),
				WheatFinanceApplication.getInstance().getResources().getString(msg),
				true, null, null, mOnSureInterface);
	}

	public void showDialog(int Title, int msg, int leftStr, int rightStr,
						   OnSureInterface mOnSureInterface) {
		showDialog(
				WheatFinanceApplication.getInstance().getResources().getString(Title),
				WheatFinanceApplication.getInstance().getResources().getString(msg),
				true,
				WheatFinanceApplication.getInstance().getResources().getString(leftStr),
				WheatFinanceApplication.getInstance().getResources().getString(rightStr),
				mOnSureInterface);
	}

	/** 不显示取消按钮 */
	public void showSuccDialog(String Title, String msg,
							   OnSureInterface mOnSureInterface) {
		showDialog(Title, msg, false, null, null, mOnSureInterface);
	}

	/** 不显示取消按钮 */
	public void showSuccDialog(String Title, String msg, String leftStr,
							   String rightStr, OnSureInterface mOnSureInterface) {
		showDialog(Title, msg, false, leftStr, rightStr, mOnSureInterface);
	}

	public void showSuccDialog(int Title, int msg,
							   OnSureInterface mOnSureInterface) {
		showDialog(
				WheatFinanceApplication.getInstance().getResources().getString(Title),
				WheatFinanceApplication.getInstance().getResources().getString(msg),
				false, null, null, mOnSureInterface);
	}

	public void showSuccDialog(int Title, int msg, int leftStr, int rightStr,
							   OnSureInterface mOnSureInterface) {
		showDialog(
				WheatFinanceApplication.getInstance().getResources().getString(Title),
				WheatFinanceApplication.getInstance().getResources().getString(msg),
				false,
				WheatFinanceApplication.getInstance().getResources().getString(leftStr),
				WheatFinanceApplication.getInstance().getResources().getString(rightStr),
				mOnSureInterface);
	}

	/** 显示dialog */
	private void showDialog(String Title, String msg, Boolean isShowCancel,
							String leftStr, String rightStr, OnSureInterface mOnSureInterface) {
		CustomDialog mCustomDialog = new CustomDialog(context);
		mCustomDialog.setmOnSureInterface(mOnSureInterface);
		mCustomDialog.setTitle(Title);
		mCustomDialog.setMessage(msg);
		mCustomDialog.setShowCancel(isShowCancel);
		mCustomDialog.show();
		if (!StringUtils.isBlank(leftStr) && !StringUtils.isBlank(rightStr)) {
			mCustomDialog.setBtnText(leftStr, rightStr);
		}
	}
}
