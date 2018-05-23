package com.miaodao.Sys.Widgets.slidview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Model.PictureURL;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SlideShowView extends ViewPager {

    static final int mDuration = 2000;// 轮播图片的时间间隔
    static final int ACTION_PLAY = 0;

    Context mContext;
    int mFocus;// 焦点图片的索引
    Handler mHandler;// 用于播放图片动画的Handler
    FlowIndicator mFlowIndicator;// 指示器view
    boolean autoSwitch;// true 自动播放 false 手动切换
    int mCount;// 播放图片的数量
    Timer mTimer;
    SlideShowViewAdapter mAdapter;//没网络轮播图的默认轮播图适配器
    SlideShowWebImageViewAdapter mWebImageAdapter;//有网络轮播图的适配器
    SlideViewAdapter resViewAdapter;
    boolean isRunning;// true 表示正在播放 false 表示没有播放
    boolean isAutoPly;//是否允许自动播放
    //    private ImageLoader imageLoader;//工具类用于下载图片
//    private DisplayImageOptions options;//设置imageLoader方式
    private ArrayList<String> imageList;//网络图片地址List

    public SlideShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
//        startGetPicurls();//开始获取网络图片的url
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
            @Override
            public void onPageSelected(int position) {
                autoSwitch = false;
                if (mCount != 0) {
                    mFocus = position % mCount;
                    mFlowIndicator.setFocus(mFocus);
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeAllViews();
    }

//    /**
//     * 开始获取网络图片的url
//     */
//    private void startGetPicurls() {
//        HashMap<String, String> queryUrlMap = new HashMap<String, String>();
//        queryUrlMap.put("SOFTTYPE", "ANDROID");//‘IOS’ ‘ANDROID’
//        String versionNameNow = "1.0.0";
//        try {
//            versionNameNow = mContext.getPackageManager().
//                    getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS).
//                    versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        queryUrlMap.put("ORGVERSION", versionNameNow);//客户端当前版本 1.2.7
//        //YDPAYQ’—源达OEM ‘PATOOL’ – 平安二级机构工具 ‘TPAYY’— 顶汇OEM ‘KJPAYQ’ --柳州钱海OEM  ??
//        loadMapByOEM(queryUrlMap);
//        queryUrlMap.put("PICTYPE", "LOOPPIC");//LOOPPIC-轮播图 ADPIC-广告图
//        NetUtil.getDataNoSession(ToJsonUtil.toJson(queryUrlMap), Constant.NET_QUERY_PICTURE_URL,
//                new RequestCallBack<String>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        String result = StringUtil.dealResponseResult(responseInfo.result);
//                        LogUtils.i("queryPictureUrl() result" + result);
//                        try {
//                            JSONObject object = new JSONObject(result);
//                            String RESP = object.getString(Constant.JonstrResPesc);
//                            if ("000".equals(RESP)) {
//                                String picurlList = object.getString("PICURLLIST");
//                                sendHandlerMessage(Constant.QUERY_PICURL_SUCC, picurlList);
//                            } else {
//                                //请求广告URL失败
//                                sendHandlerMessage(Constant.QUERY_PICURL_F, "");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            sendHandlerMessage(Constant.QUERY_PICURL_F, "");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(HttpException e, String s) {
//                        //TipDialogUtils.showTipdialog(mContext, R.string.net_error);
//                        sendHandlerMessage(Constant.QUERY_PICURL_F, "");
//                    }
//                });
//    }
//
//    /**
//     * 每次有了新OEM就需要变换
//     *
//     * @param map
//     */
//    private void loadMapByOEM(HashMap<String, String> map) {
//        if (Constant.PACKAGE_NAME_YUANDA.equals(mContext.getPackageName())) {
//            map.put("CLIENTTYPE", Constant.CLIENTTYPE_YUANDA);
//        } else if (Constant.PACKAGE_NAME_LIUZHOU.equals(mContext.getPackageName())) {
//            map.put("CLIENTTYPE", Constant.CLIENTTYPE_LIUZHOU);
//        } else if (Constant.PACKAGE_NAME_MJTONG.equals(mContext.getPackageName())) {
//            map.put("CLIENTTYPE", Constant.CLIENTTYPE_MJTONG);
//        } else if (Constant.PACKAGE_NAME_YINHAI.equals(mContext.getPackageName())) {
//            map.put("CLIENTTYPE", Constant.CLIENTTYPE_YINHAI);
//        } else if (Constant.PACKAGE_NAME_BAOKANG.equals(mContext.getPackageName())) {
//            map.put("CLIENTTYPE", Constant.CLIENTTYPE_BAOKANG);
//        }
//    }

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
                            int pos = SlideShowView.this.getCurrentItem();// 获取ViewPager中正在播放的索引
                            pos++;
                            // 设置下一个页面为当前的页面
                            SlideShowView.this.setCurrentItem(pos);
                        }
                        break;

//                    case Constant.QUERY_PICURL_SUCC://下载 网络图片URl成功
//                        loadingImageByObjstr((String) msg.obj);
//                        break;
//                    case Constant.QUERY_PICURL_F://下载 网络图片URL失败 不管
//                        //loadingImageByObjstr((String) msg.obj);//测试造假
//                        break;
                    default:
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
    public void initData(FlowIndicator flowIndicator, List<PictureURL> showGoods) {
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
        mFlowIndicator.setCount(mCount);
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
        resViewAdapter = new SlideViewAdapter(mContext, showGoods);
        this.setAdapter(resViewAdapter);
        if (mFlowIndicator != null) {
            mFlowIndicator.setCount(mCount);
        }
    }


    private static final ViewGroup.LayoutParams M_M = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public interface IOnRedBagClickListener {
        void gotoQuestionPage(String position, PictureURL pictureURL);
    }

    private IOnRedBagClickListener redBagClickListener;

    public void setRedBagClickListener(IOnRedBagClickListener l) {
        this.redBagClickListener = l;
    }

    /**
     * 是否播放网络轮播图
     */
    private void isShowWebImageView() {
        if (imageList == null || imageList.size() == 0) {
            return;//没轮播图片不播放
        }
        mWebImageAdapter = new SlideShowWebImageViewAdapter(mContext, imageList);
        //setOnPagerChangeListener();
        this.setAdapter(mWebImageAdapter);
        mWebImageAdapter.notifyDataSetChanged();

        mCount = imageList.size();
        mFlowIndicator.setCount(mCount);
    }

    /**
     * custom ViewPager adapter
     */
    class SlideShowViewAdapter extends PagerAdapter {

        private Context context;
        private List<PictureURL> data;
        private int currentIndex;

        public SlideShowViewAdapter(Context context, List<PictureURL> data) {
            this.context = context;
            this.data = data;
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            if (data == null || data.isEmpty()) {
                return 1;
            }
            if (data.size() == 1) {
                return 1;
            } else {
                return Integer.MAX_VALUE;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View rootView = null;
            rootView = View.inflate(mContext, R.layout.v_home_image, null);
            ImageView imgView = (ImageView) rootView.findViewById(R.id.item_image);
            final String url;
            final String link;
            final boolean action;
            currentIndex = position % data.size();
            final PictureURL pictureURL = data.get(currentIndex);
            url = pictureURL.getBannerUrl();
            link = pictureURL.getActionUrl();
            action = pictureURL.isAction();
            Glide.with(mContext).load(url).into(imgView);
            if (data.size() != 0) {
                rootView.setTag(position % data.size());
            }
            rootView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Integer i = (Integer) v.getTag();
                    if (redBagClickListener != null) {
                        if (!TextUtils.isEmpty(link) && action)
                            redBagClickListener.gotoQuestionPage(link, pictureURL);
                    }
                }
            });

            ((ViewPager) container).addView(rootView, M_M);
            return rootView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

    }

    /**
     * custom ViewPager adapter
     */
    class SlideShowWebImageViewAdapter extends PagerAdapter {

        private Context context;
        private ArrayList<String> data;
        private int currentIndex;

        public SlideShowWebImageViewAdapter(Context context, ArrayList<String> data) {
            this.context = context;
            this.data = data;
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View rootView = null;
            rootView = View.inflate(mContext, R.layout.v_home_image, null);
            ImageView imgView = (ImageView) rootView.findViewById(R.id.item_image);
            currentIndex = position % data.size();
            String url = data.get(currentIndex);
            Glide.with(mContext).load(url).into(imgView);
            if (data.size() != 0) {
                rootView.setTag(position % data.size());
            }
            rootView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer i = (Integer) v.getTag();
                    if (redBagClickListener != null) {
                    }
                }
            });

            ((ViewPager) container).addView(rootView, M_M);
            return rootView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }


    }


    /**
     * custom ViewPager adapter
     */
    class SlideViewAdapter extends PagerAdapter {

        private Context context;
        private ArrayList<Integer> data;
        private int currentIndex;

        public SlideViewAdapter(Context context, ArrayList<Integer> data) {
            this.context = context;
            this.data = data;
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            if (data == null || data.isEmpty()) {
                return 1;
            }
            if (data.size() == 1) {
                return 1;
            } else {
                return Integer.MAX_VALUE;
            }
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
//            imgView.setBackgroundResource(url);
            Glide.with(mContext).load(url).into(imgView);

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


    private void sendHandlerMessage(int what, Object msgObj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = msgObj;
        mHandler.sendMessage(msg);
    }
}

