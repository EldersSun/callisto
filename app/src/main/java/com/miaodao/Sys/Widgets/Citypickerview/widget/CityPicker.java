package com.miaodao.Sys.Widgets.Citypickerview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Utils.JsonUtil.XCJsonUtil;
import com.miaodao.Sys.Widgets.Citypickerview.model.Area;
import com.miaodao.Sys.Widgets.Citypickerview.model.City;
import com.miaodao.Sys.Widgets.Citypickerview.model.Province;
import com.miaodao.Sys.Widgets.Citypickerview.widget.wheel.OnWheelChangedListener;
import com.miaodao.Sys.Widgets.Citypickerview.widget.wheel.WheelView;
import com.miaodao.Sys.Widgets.Citypickerview.widget.wheel.adapters.ArrayWheelAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市区三级选择
 */
public class CityPicker implements CanShow, OnWheelChangedListener {

    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mViewProvince;

    private WheelView mViewCity;

    private WheelView mViewDistrict;

    private RelativeLayout mRelativeTitleBg;

    private TextView mTvOK;

    private TextView mTvTitle;

    private TextView mTvCancel;

    /**
     * 所有省
     */
//    protected String[] mProvinceDatas;

    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;

    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    private OnCityItemClickListener listener;

    public interface OnCityItemClickListener {
        void onSelected(String... citySelected);

        void onSelectedCode(String... cityCode);
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;

    private int textSize = DEFAULT_TEXT_SIZE;

    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /**
     * 省滚轮是否循环滚动
     */
    private boolean isProvinceCyclic = true;

    /**
     * 市滚轮是否循环滚动
     */
    private boolean isCityCyclic = true;

    /**
     * 区滚轮是否循环滚动
     */
    private boolean isDistrictCyclic = true;

    /**
     * item间距
     */
    private int padding = 5;


    /**
     * Color.BLACK
     */
    private String cancelTextColorStr = "#000000";


    /**
     * Color.BLUE
     */
    private String confirmTextColorStr = "#0000FF";

    /**
     * 标题背景颜色
     */
    private String titleBackgroundColorStr = "#E9E9E9";
    /**
     * 标题颜色
     */
    private String titleTextColorStr = "#E9E9E9";

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    private String defaultProvinceName = "江苏";

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    private String defaultCityName = "常州";

    /**
     * 第一次默认得显示，一般配合定位，使用
     */
    private String defaultDistrict = "新北区";

    /**
     * 两级联动
     */
    private boolean showProvinceAndCity = false;

    /**
     * 标题
     */
    private String mTitle = "选择地区";

    /**
     * 设置popwindow的背景
     */
    private int backgroundPop = 0xa0000000;

    private CityPicker(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.visibleItems = builder.visibleItems;
        this.isProvinceCyclic = builder.isProvinceCyclic;
        this.isDistrictCyclic = builder.isDistrictCyclic;
        this.isCityCyclic = builder.isCityCyclic;
        this.context = builder.mContext;
        this.padding = builder.padding;
        this.mTitle = builder.mTitle;
        this.titleBackgroundColorStr = builder.titleBackgroundColorStr;
        this.confirmTextColorStr = builder.confirmTextColorStr;
        this.cancelTextColorStr = builder.cancelTextColorStr;

        this.defaultDistrict = builder.defaultDistrict;
        this.defaultCityName = builder.defaultCityName;
        this.defaultProvinceName = builder.defaultProvinceName;

        this.showProvinceAndCity = builder.showProvinceAndCity;
        this.backgroundPop = builder.backgroundPop;
        this.titleTextColorStr = builder.titleTextColorStr;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.v_pop_citypicker, null);

        mViewProvince = (WheelView) popview.findViewById(R.id.id_province);
        mViewCity = (WheelView) popview.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popview.findViewById(R.id.id_district);
        mRelativeTitleBg = (RelativeLayout) popview.findViewById(R.id.rl_title);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);


        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(backgroundPop));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);


        /**
         * 设置标题背景颜色
         */
        if (!TextUtils.isEmpty(this.titleBackgroundColorStr)) {
            mRelativeTitleBg.setBackgroundColor(Color.parseColor(this.titleBackgroundColorStr));
        }

        /**
         * 设置标题
         */
        if (!TextUtils.isEmpty(this.mTitle)) {
            mTvTitle.setText(this.mTitle);
        }


        //设置确认按钮文字颜色
        if (!TextUtils.isEmpty(this.titleTextColorStr)) {
            mTvTitle.setTextColor(Color.parseColor(this.titleTextColorStr));
        }


        //设置确认按钮文字颜色
        if (!TextUtils.isEmpty(this.confirmTextColorStr)) {
            mTvOK.setTextColor(Color.parseColor(this.confirmTextColorStr));
        }

        //设置取消按钮文字颜色
        if (!TextUtils.isEmpty(this.cancelTextColorStr)) {
            mTvCancel.setTextColor(Color.parseColor(this.cancelTextColorStr));
        }


        //只显示省市两级联动
        if (this.showProvinceAndCity) {
            mViewDistrict.setVisibility(View.GONE);
        } else {
            mViewDistrict.setVisibility(View.VISIBLE);
        }

        //初始化城市数据
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    initCities(context);
                }
            });
            thread.join();
            thread.start();
        } catch (InterruptedException e) {
        }

        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showProvinceAndCity) {
                    listener.onSelected(mCurrentProviceName, mCurrentCityName, "", mCurrentZipCode);
                    listener.onSelectedCode(contextProviceCode, contextCityCode);
                } else {
                    listener.onSelected(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName, mCurrentZipCode);
                    listener.onSelectedCode(contextProviceCode, contextCityCode, contextAreaCode);
                }
                hide();
            }
        });

    }

    public static class Builder {
        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

        /**
         * Default text size
         */
        public static final int DEFAULT_TEXT_SIZE = 18;

        // Text settings
        private int textColor = DEFAULT_TEXT_COLOR;

        private int textSize = DEFAULT_TEXT_SIZE;

        /**
         * 滚轮显示的item个数
         */
        private static final int DEF_VISIBLE_ITEMS = 5;

        // Count of visible items
        private int visibleItems = DEF_VISIBLE_ITEMS;

        /**
         * 省滚轮是否循环滚动
         */
        private boolean isProvinceCyclic = true;

        /**
         * 市滚轮是否循环滚动
         */
        private boolean isCityCyclic = true;

        /**
         * 区滚轮是否循环滚动
         */
        private boolean isDistrictCyclic = true;

        private Context mContext;

        /**
         * item间距
         */
        private int padding = 5;


        /**
         * Color.BLACK
         */
        private String cancelTextColorStr = "#000000";


        /**
         * Color.BLUE
         */
        private String confirmTextColorStr = "#0000FF";

        /**
         * 标题背景颜色
         */
        private String titleBackgroundColorStr = "#E9E9E9";

        /**
         * 标题颜色
         */
        private String titleTextColorStr = "#E9E9E9";


        /**
         * 第一次默认的显示省份，一般配合定位，使用
         */
        private String defaultProvinceName = "江苏";

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         */
        private String defaultCityName = "常州";

        /**
         * 第一次默认得显示，一般配合定位，使用
         */
        private String defaultDistrict = "新北区";

        /**
         * 标题
         */
        private String mTitle = "选择地区";

        /**
         * 两级联动
         */
        private boolean showProvinceAndCity = false;

        /**
         * 设置popwindow的背景
         */
        private int backgroundPop = 0xa0000000;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置popwindow的背景
         *
         * @param backgroundPopColor
         * @return
         */
        public Builder backgroundPop(int backgroundPopColor) {
            this.backgroundPop = backgroundPopColor;
            return this;
        }

        /**
         * 设置标题背景颜色
         *
         * @param colorBg
         * @return
         */
        public Builder titleBackgroundColor(String colorBg) {
            this.titleBackgroundColorStr = colorBg;
            return this;
        }

        /**
         * 设置标题背景颜色
         *
         * @param titleTextColorStr
         * @return
         */
        public Builder titleTextColor(String titleTextColorStr) {
            this.titleTextColorStr = titleTextColorStr;
            return this;
        }


        /**
         * 设置标题
         *
         * @param mtitle
         * @return
         */
        public Builder title(String mtitle) {
            this.mTitle = mtitle;
            return this;
        }

        /**
         * 是否只显示省市两级联动
         *
         * @param flag
         * @return
         */
        public Builder onlyShowProvinceAndCity(boolean flag) {
            this.showProvinceAndCity = flag;
            return this;
        }

        /**
         * 第一次默认的显示省份，一般配合定位，使用
         *
         * @param defaultProvinceName
         * @return
         */
        public Builder province(String defaultProvinceName) {
            this.defaultProvinceName = defaultProvinceName;
            return this;
        }

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         *
         * @param defaultCityName
         * @return
         */
        public Builder city(String defaultCityName) {
            this.defaultCityName = defaultCityName;
            return this;
        }

        /**
         * 第一次默认地区显示，一般配合定位，使用
         *
         * @param defaultDistrict
         * @return
         */
        public Builder district(String defaultDistrict) {
            this.defaultDistrict = defaultDistrict;
            return this;
        }

        //        /**
        //         * 确认按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder confirTextColor(int color) {
        //            this.confirmTextColor = color;
        //            return this;
        //        }

        /**
         * 确认按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder confirTextColor(String color) {
            this.confirmTextColorStr = color;
            return this;
        }

        //        /**
        //         * 取消按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder cancelTextColor(int color) {
        //            this.cancelTextColor = color;
        //            return this;
        //        }

        /**
         * 取消按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder cancelTextColor(String color) {
            this.cancelTextColorStr = color;
            return this;
        }

        /**
         * item文字颜色
         *
         * @param textColor
         * @return
         */
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * item文字大小
         *
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 滚轮显示的item个数
         *
         * @param visibleItems
         * @return
         */
        public Builder visibleItemsCount(int visibleItems) {
            this.visibleItems = visibleItems;
            return this;
        }

        /**
         * 省滚轮是否循环滚动
         *
         * @param isProvinceCyclic
         * @return
         */
        public Builder provinceCyclic(boolean isProvinceCyclic) {
            this.isProvinceCyclic = isProvinceCyclic;
            return this;
        }

        /**
         * 市滚轮是否循环滚动
         *
         * @param isCityCyclic
         * @return
         */
        public Builder cityCyclic(boolean isCityCyclic) {
            this.isCityCyclic = isCityCyclic;
            return this;
        }

        /**
         * 区滚轮是否循环滚动
         *
         * @param isDistrictCyclic
         * @return
         */
        public Builder districtCyclic(boolean isDistrictCyclic) {
            this.isDistrictCyclic = isDistrictCyclic;
            return this;
        }

        /**
         * item间距
         *
         * @param itemPadding
         * @return
         */
        public Builder itemPadding(int itemPadding) {
            this.padding = itemPadding;
            return this;
        }

        public CityPicker build() {
            CityPicker cityPicker = new CityPicker(this);
            return cityPicker;
        }

    }

    private void setUpData() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(defaultProvinceName) && allProviceList.size() > 0) {
            for (int i = 0; i < allProviceList.size(); i++) {
                if (allProviceList.get(i).getName().equals(defaultProvinceName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<Province>(context, allProviceList);
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }
        // 设置可见条目数量
        mViewProvince.setVisibleItems(visibleItems);
        mViewCity.setVisibleItems(visibleItems);
        mViewDistrict.setVisibleItems(visibleItems);
        mViewProvince.setCyclic(isProvinceCyclic);
        mViewCity.setCyclic(isCityCyclic);
        mViewDistrict.setCyclic(isDistrictCyclic);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

        updateCities();
        updateAreas();
    }

    /**
     * 所有省
     */
    private List<Province> allProviceList = new ArrayList<>();
    /**
     * 所有城市
     */
    private List<City> allCityList = new ArrayList<>();
    /**
     * 所有地区
     */
    private List<Area> allAreaList = new ArrayList<>();
    /**
     * 当前城市集合
     */
    private List<City> thisCityList = new ArrayList<>();
    /**
     * 当前地区集合
     */
    private List<Area> thisAreaList = new ArrayList<>();
    /**
     * 当前省的code
     */
    private String contextProviceCode = "";
    /**
     * 当前城市的code
     */
    private String contextCityCode = "";
    /**
     * 当前地区的code
     */
    private String contextAreaCode = "";


    protected void initCities(Context context) {
        try {

            InputStreamReader proviceIsr = new InputStreamReader(context.getAssets().open("provice.json"), "UTF-8");
            InputStreamReader cityIsr = new InputStreamReader(context.getAssets().open("cities.json"), "UTF-8");
            InputStreamReader areaIsr = new InputStreamReader(context.getAssets().open("areas.json"), "UTF-8");

            BufferedReader proviceBr = new BufferedReader(proviceIsr);
            BufferedReader CityBr = new BufferedReader(cityIsr);
            BufferedReader areaBr = new BufferedReader(areaIsr);

            StringBuilder proviceBuilder = new StringBuilder();
            StringBuilder cityBuilder = new StringBuilder();
            StringBuilder areaBuilder = new StringBuilder();

            String proviceline;
            String cityline;
            String arealine;

            while ((proviceline = proviceBr.readLine()) != null) {
                proviceBuilder.append(proviceline);
            }
            while ((cityline = CityBr.readLine()) != null) {
                cityBuilder.append(cityline);
            }

            while ((arealine = areaBr.readLine()) != null) {
                areaBuilder.append(arealine);
            }

            proviceBr.close();
            proviceIsr.close();

            CityBr.close();
            cityIsr.close();

            areaBr.close();
            areaIsr.close();

            Map<String, Object> proviceResultMap = XCJsonUtil.json2Map(new JSONObject(proviceBuilder.toString()));
            Map<String, Object> cityResultMap = XCJsonUtil.json2Map(new JSONObject(cityBuilder.toString()));
            Map<String, Object> areaResultMap = XCJsonUtil.json2Map(new JSONObject(areaBuilder.toString()));

            /**
             * 获取全部省
             */
            for (Map.Entry<String, Object> entry : proviceResultMap.entrySet()) {
                List<Map<String, String>> list = (List<Map<String, String>>) entry.getValue();
                Province provinces = new Province();
                provinces.setCode(list.get(0).get("code"));
                provinces.setName(list.get(0).get("name"));
                allProviceList.add(provinces);
            }

            /**
             * 获取全部市
             */
            for (Map.Entry<String, Object> entry : cityResultMap.entrySet()) {
                List<Map<String, String>> list = (List<Map<String, String>>) entry.getValue();
                for (int i = 0; i < list.size(); i++) {
                    City citys = new City();
                    citys.setName(list.get(i).get("name"));
                    citys.setCode(list.get(i).get("code"));
                    citys.setProvinceCode(list.get(i).get("provinceCode"));
                    allCityList.add(citys);
                }
            }

            /**
             * 获取全部区
             */
            for (Map.Entry<String, Object> entry : areaResultMap.entrySet()) {
                List<Map<String, String>> list = (List<Map<String, String>>) entry.getValue();
                for (int i = 0; i < list.size(); i++) {
                    Area area = new Area();
                    area.setName(list.get(i).get("name"));
                    area.setCode(list.get(i).get("code"));
                    area.setCityCode(list.get(i).get("cityCode"));
                    allAreaList.add(area);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        /**
         * 选择当前城市
         */
        if (thisCityList != null && thisCityList.size() != 0) {
            mCurrentCityName = thisCityList.get(pCurrent).getName();
            contextCityCode = thisCityList.get(pCurrent).getCode();
        } else {
            mCurrentCityName = "";
            contextCityCode = "";
        }
        List<Area> areas = new ArrayList<>();

        String thisCityCode = "";

        /**
         * 当前城市code
         */
        if (thisCityList != null && thisCityList.size() != 0) {
            thisCityCode = thisCityList.get(pCurrent).getCode();
        }

        for (int i = 0; i < allAreaList.size(); i++) {
            if (allAreaList.get(i).getCityCode().equals(thisCityCode)) {
                areas.add(allAreaList.get(i));
            }
        }

        if (areas != null && areas.size() != 0) {
            thisAreaList = areas;
        } else {
            thisAreaList.clear();
        }

        int districtDefault = -1;
        if (!TextUtils.isEmpty(defaultDistrict) && areas.size() > 0) {
            for (int i = 0; i < areas.size(); i++) {
                if (areas.get(i).getName().equals(defaultDistrict)) {
                    districtDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter districtWheel = new ArrayWheelAdapter<Area>(context, areas);
        // 设置可见条目数量
        districtWheel.setTextColor(textColor);
        districtWheel.setTextSize(textSize);
        mViewDistrict.setViewAdapter(districtWheel);
        if (-1 != districtDefault) {
            mViewDistrict.setCurrentItem(districtDefault);
            //获取默认设置的区
            mCurrentDistrictName = defaultDistrict;
        } else {
            mViewDistrict.setCurrentItem(0);
            //获取第一个区名称
            if (areas != null && areas.size() != 0) {
                mCurrentDistrictName = areas.get(0).getName();
            } else {
                mCurrentDistrictName = "";
            }
        }
        districtWheel.setPadding(padding);
//        JLogUtils.D("zipcode key: " + mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
        //获取第一个区名称
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        String provinceCode = allProviceList.get(pCurrent).getCode();
        mCurrentProviceName = allProviceList.get(pCurrent).getName();
        contextProviceCode = provinceCode;

        List<City> citys = new ArrayList<>();

        for (int i = 0; i < allCityList.size(); i++) {
            if (allCityList.get(i).getProvinceCode().equals(provinceCode)) {
                citys.add(allCityList.get(i));
            }
        }

        if (citys != null && citys.size() != 0) {
            thisCityList = citys;
        } else {
            citys = new ArrayList<>();
            thisCityList.clear();
        }


        int cityDefault = -1;
        if (!TextUtils.isEmpty(defaultCityName) && citys.size() > 0) {
            for (int i = 0; i < citys.size(); i++) {
                if (citys.get(i).getName().equals(defaultCityName)) {
                    cityDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter<City>(context, citys);
        // 设置可见条目数量
        cityWheel.setTextColor(textColor);
        cityWheel.setTextSize(textSize);
        mViewCity.setViewAdapter(cityWheel);
        if (-1 != cityDefault) {
            mViewCity.setCurrentItem(cityDefault);
        } else {
            mViewCity.setCurrentItem(0);
        }

        cityWheel.setPadding(padding);
        updateAreas();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            if (mViewDistrict.getCurrentItem() != 0) {
                mCurrentDistrictName = thisAreaList.get(mViewDistrict.getCurrentItem()).getName();
                contextAreaCode = thisAreaList.get(mViewDistrict.getCurrentItem()).getCode();
            } else {
                mCurrentDistrictName = "";
                contextAreaCode = "";
            }
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
        }
    }
}
