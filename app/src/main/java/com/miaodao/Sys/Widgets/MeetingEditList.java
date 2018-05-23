package com.miaodao.Sys.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MeetingEditList extends ListView {
	public MeetingEditList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public MeetingEditList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MeetingEditList(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
