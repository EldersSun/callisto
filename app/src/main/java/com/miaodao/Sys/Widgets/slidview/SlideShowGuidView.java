package com.miaodao.Sys.Widgets.slidview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fcloud.licai.miaodao.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SlideShowGuidView extends ViewPager {

    Context mContext;
    int mFocus;// 焦点图片的索引
    Handler mHandler;// 用于播放图片动画的Handler
    private Handler otherHandler;// 外部Handler 用于处理最后一张引导图片的跳转
    FlowIndicator mFlowIndicator;// 指示器view
    boolean autoSwitch;// true 自动播放 false 手动切换
    int mCount;// 播放图片的数量
    static final int ACTION_PLAY = 0;
    Timer mTimer;
    static final int mDuration = 3000;// 轮播图片的时间间隔
    SlideShowViewAdapter mAdapter;
    boolean isRunning;// true 表示正在播放 false 表示没有播放
    boolean isAutoPly;//是否允许自动播放

//    public OnLastGuidListener listener;
//    public interface OnLastGuidListener{
//        void onLastGuid();
//    }

    public SlideShowGuidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
//        listener=(OnLastGuidListener)context;
        initHandler();// 初始化Handler,接收消息播放动画
        setListener();// 设置监听器
    }

    /**
     * 设置手指触摸监听器
     */
    private void setOnTouch() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        autoSwitch = false;// 设置不自动播放
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        // 设置手指触摸监听器
        setOnTouch();

    }

    boolean isEnd;

    /**
     * 设置ViewPager页面改变的监听器
     */
    private void setOnPagerChangeListener() {
        this.setOnPageChangeListener(new OnPageChangeListener() {
            boolean scrollLast = false;
            int pageNum = 0;

            @Override
            public void onPageSelected(int position) {
                autoSwitch = false;
                if (mCount != 0) {
                    mFocus = position % mCount;
                    mFlowIndicator.setFocus(mFocus);
                }
                if (position == mAdapter.getData().size() - 1) {

                } else {

                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //arg0 :当前页面，及你点击滑动的页面  arg1:当前页面偏移的百分比  arg2:当前页面偏移的像素位置
                //LogUtils.i("onPageScrolled() arg0="+arg0+"/"+arg1+"/"+arg2);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                //LogUtils.i("onPageScrollStateChanged() arg0="+arg0);

            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeAllViews();
    }

    /**
     * 初始化Handler,接收消息播放动画
     */
    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ACTION_PLAY:
                        // 若是手动滑动ViewPager时
                        if (!autoSwitch) {
                            autoSwitch = true;
                        } else {
                            int pos = SlideShowGuidView.this.getCurrentItem();// 获取ViewPager中正在播放的索引
                            pos++;
                            // 设置下一个页面为当前的页面
                            SlideShowGuidView.this.setCurrentItem(pos);
                        }
                        break;
                }
            }
        };
    }

    /**
     * auto play picture
     */
    public void playLoop() {
        if (isAutoPly) {
//            if(getChildCount() <= 1){
//                return;
//            }
            if (mTimer == null) {
                mTimer = new Timer();
            }
            if (!isRunning) {// 如果图片没有不在播放则，开始播放
                isRunning = true;//set playing
                // 设置计划任务立即执行，每隔3000毫秒发送一次轮播消息
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(ACTION_PLAY);
                    }
                }, 0, mDuration);
            }
        }
    }

    /**
     * stop auto play
     */
    public void stopPlay() {
        if (mTimer == null) {
            return;
        }
        isRunning = false;//set pausing
        // if timer exit,timer set null
        mTimer.cancel();
        mTimer = null;
    }

    public void setAutoPlay(boolean auto) {
        this.isAutoPly = auto;
    }


    /**
     * 由MainActivity调用
     *
     * @param flowIndicator FlowIndicator对象
     * @param showGoods     轮播图片的网络地址
     */
    public void initData(FlowIndicator flowIndicator, ArrayList<Integer> showGoods) {
        // 设置ViewPager页面改变的监听器
        mFlowIndicator = flowIndicator;
        if (showGoods == null || showGoods.isEmpty()) {
            mCount = 0;
        } else {
            mCount = showGoods.size();
        }
        setOnPagerChangeListener();
        mAdapter = new SlideShowViewAdapter(mContext, showGoods);
        this.setAdapter(mAdapter);
        if (mFlowIndicator != null) {
            mFlowIndicator.setCount(mCount);
        }
    }

    private static final ViewGroup.LayoutParams M_M = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


    public void setOtherHandler(Handler otherHandler) {
        this.otherHandler = otherHandler;
    }

    /**
     * custom ViewPager adapter
     */
    class SlideShowViewAdapter extends PagerAdapter {

        private Context context;
        private ArrayList<Integer> data;
        private int currentIndex;

        public SlideShowViewAdapter(Context context, ArrayList<Integer> data) {
            this.context = context;
            this.data = data;
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View rootView = null;
            rootView = View.inflate(mContext, R.layout.v_guide_image, null);
            ImageView imgView = (ImageView) rootView.findViewById(R.id.iv_item_guide);
            int url = 0;
            currentIndex = position % data.size();
            url = data.get(currentIndex);
            imgView.setBackgroundResource(url);
//            Glide.with(mContext).load(url).into(imgView);
            if (data.size() != 0) {
                rootView.setTag(position % data.size());
            }
            rootView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer i = (Integer) v.getTag();
                }
            });

            ((ViewPager) container).addView(rootView, M_M);
            return rootView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        public ArrayList<Integer> getData() {
            return data;
        }
    }
}

