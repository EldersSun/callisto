package com.miaodao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import com.fcloud.licai.miaodao.R;
import com.githang.statusbar.StatusBarCompat;
import com.miaodao.Sys.Widgets.slidview.FlowIndicator;
import com.miaodao.Sys.Widgets.slidview.SlideShowGuidView;

import java.util.ArrayList;

/**
 * Created by daixinglong on 2017/4/1.
 */

public class GuideActivity extends Activity {


    private SlideShowGuidView slideView;
    private FlowIndicator indicator;
    private ArrayList<Integer> images = new ArrayList<Integer>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.whiteColor));
        setContentView(R.layout.ac_guide);
        initParams();
        initActions();
    }

    /**
     * 初始化参数
     */
    private void initParams() {
        slideView = (SlideShowGuidView) findViewById(R.id.show_image);
        indicator = (FlowIndicator) findViewById(R.id.indicator);
        images.add(R.drawable.v_guide1);
        images.add(R.drawable.v_guide2);
        images.add(R.drawable.v_guide3);
        images.add(R.drawable.v_guide4);

        slideView.setAutoPlay(false);//不自动播放
        slideView.initData(indicator, images);
    }

    /**
     * 设置事件监听
     */
    private void initActions() {

        slideView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == images.size() - 1) {
                    handler.sendEmptyMessageDelayed(1, 500);
                }
            }

            @Override
            public void onPageSelected(int position) {
                int mFocus = position % images.size();
                indicator.setFocus(mFocus);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(GuideActivity.this, MainAct.class);
            startActivity(intent);
            finish();
        }
    };


}
