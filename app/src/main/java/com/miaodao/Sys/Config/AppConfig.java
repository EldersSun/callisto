package com.miaodao.Sys.Config;

import com.miaodao.Application.WheatFinanceApplication;
import com.miaodao.Remote.IRemoteResponse;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.RequestTool;
import com.miaodao.Sys.Utils.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 程序配置文件
 * Created by Home_Pc on 2017/3/10.
 */
public class AppConfig implements IRemoteResponse {

    public final boolean ISDEBUG = true;

    public final String BANK_CARD_PAY = "1001";
    public final String ALIPAY_PAY = "1002";
    public final String WECHAT_PAY = "1003";

    private static AppConfig instance;

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    /**
     * 请求结果正确的情况下
     */
    public final String REQUEST_SUCC_CODE = "000000";//成功码
    public final String REQUEST_SYS_MAINTAIN_CODE = "900001";//系统升级码
    public final String REQUEST_LOGIN_CODE = "010399";//跳转登录页
    public final String REQUEST_ZHIMA_CODE = "010300";//芝麻为授权

    /**
     * 验证身份--拍摄身份证时正反面
     */
    public final String VERIFICATION_ID_FRONT = "0X1011";
    public final String VERIFICATION_ID_BACK = "0X1012";
    /**
     * 验证用户是否登录
     */
    public final String VERIFICATION_USER_ISLOGIN = "0X1013";
    /**
     * 错误时点击页面向前一页面传递的消息
     */
    public final String VERIFICATION_USER_MESSAGE_1 = "0X1014";

    public final String JUMP_MESSAGE_TAG_1 = "0X1013";
    public final String JUMP_MESSAGE_TAG_2 = "0X1014";
    public final String JUMP_MESSAGE_TAG_3 = "0X1015";
    public final String JUMP_MESSAGE_TAG_4 = "0X1016";
    public final String JUMP_MESSAGE_TAG_5 = "0X1017";
    public final String JUMP_MESSAGE_TAG_6 = "0X1018";

    public final String VERIFICATION_VIDEO_MESSAGE = "0X1050";
    public final String VERIFICATION_ID_MESSAGE = "0X1051";
    public final String MESSAGE_GET_COUPON = "0X1052";
    public final String MESSAGE_USE_COUPON = "0X1053";


    public final String VERIFICATION_ID_FRONT_PHONE = "front.jpg";
    public final String VERIFICATION_ID_BACK_PHONE = "back.jpg";
    public final String VERIFICATION_ID_VIDEO_PHONE = "img.jpg";
    public final String VideoAuthenticationFrie = "VideoAuthentication.mp4";

    public final String VERIFICATION_ID_FRONT_PHONE_PATH = SysSDCardCacheDir.getImgDir() + "/" + VERIFICATION_ID_FRONT_PHONE;
    public final String VERIFICATION_ID_BACK_PHONE_PATH = SysSDCardCacheDir.getImgDir() + "/" + VERIFICATION_ID_BACK_PHONE;
    /**
     * 验证--图像验证 最后一步的存放路径
     */
    public final String VERIFICATION_ID_PHONE_PATH = SysSDCardCacheDir.getVideoDir() + "/" + VERIFICATION_ID_VIDEO_PHONE;
    /**
     * 视频
     */
    public final String VERIFICATION_ID_VIDEO_PATH = SysSDCardCacheDir.getVideoDir() + "/" + VideoAuthenticationFrie;

    public final String LOAN_MESSAGE_1 = "0x1001";

    public final String RESULT_CODE = "code";
    public final String RESULT_MSG = "msg";
    public final String RESULT_DATA = "data";
    public final String RESULT_TOKRNID_TAG = "tokenId";
    public final String RESULT_USERID = "userId";
    public final String RESULT_INVITEDCODE = "invitedCode";
    public final String RESULT_COUPON_NUMBER = "ticketCount";
    public final String RESULT_LOGINNAME = "loginName";
    public final String USER_PHONE_NUM = "userPhone";
    public final String USER_ISPAYPWD_EXIST_TAG = "isPayPwdExist";

    /**
     * 手势密码常亮
     */
    public final String GESTURE_LOGIN_FINISH = "gestureLoginFinish";
    public final String GESTURE_PD = "gesturePd";//手势密码key
    public final int POINT_STATE_NORMAL = 0; // 正常状态
    public final int POINT_STATE_SELECTED = 1; // 按下状态
    public final int POINT_STATE_WRONG = 2; // 错误状态
    /**
     * 判断APP是否是第一次启动
     */
    public final String IS_FIRST_LAUNCH = "isFirstLaunch";


    /**
     * 支持的银行
     */
    public final String[] Banks = new String[]{"中国建设银行", "中国农业银行", "中国工商银行",
            "招商银行", "中国银行", "中信银行", "广发银行", "兴业银行",
            "上海银行", "浦发银行", "交通银行"};

    public final String PickerTitleBackColor = "#ffffff";
    public final String white = "#ffffff";
    public final String black = "#000000";

    public final String defauleProvince = "上海市";
    public final String defauleCity = "上海市";
    public final String defauleDistrict = "浦东新区";

    public final String IdCardFrontBitmap = "frontBitmap";
    public final String IdCardBackBitmap = "backBitmap";
    public final String IdAuthenticationBitmap = "IdAuthenticationBitmap";

    /**
     * 微信支付使用的APPID
     */
    public final String WX_APP_ID = "wxd930ea5d5a258f4f";
    /**
     * 支付宝支付使用的APPID
     */
//    public static final String ALI_APP_ID = "alp930ea5d5a258f4f";
    public static final String ALI_APP_ID = "2016080300154098";//沙箱环境

    /**
     * appData
     */
    public Map<String, Object> PerfectInfoMap = new HashMap<>();

    /**
     * 总倒计时 时间 一分钟
     */
    public final int millisInFuture = 1000 * 60;
    /**
     * 倒计时的步进
     */
    public final int countDownInterval = 1000;

    /**
     * 文件上传类型
     */
    public final String upDateType = "application/octet-stream";

    /**
     * 清空数据
     */
    public void clearUserData() {
        SharedPreferencesUtil.removeKey(WheatFinanceApplication.getInstance(), GESTURE_PD);
        clearUserFile();
        SharedPreferencesUtil.removeKey(WheatFinanceApplication.getInstance(), AppConfig.getInstance().RESULT_USERID);
        SharedPreferencesUtil.removeKey(WheatFinanceApplication.getInstance(), AppConfig.getInstance().RESULT_TOKRNID_TAG);
        SharedPreferencesUtil.removeKey(WheatFinanceApplication.getInstance(), AppConfig.getInstance().RESULT_INVITEDCODE);
        SharedPreferencesUtil.removeKey(WheatFinanceApplication.getInstance(), AppConfig.getInstance().USER_ISPAYPWD_EXIST_TAG);
    }


    /**
     * 清空用户磁盘文件
     */
    public void clearUserFile() {
        WheatFinanceApplication.getInstance().clearLruCache();
        File imgFile = new File(SysSDCardCacheDir.getImgDir().getPath());
        File videoFile = new File(SysSDCardCacheDir.getVideoDir().getPath());
        if (imgFile.exists()) {
            File[] files = imgFile.listFiles();
            if (files == null) return;
            for (int i = 0; i < files.length; i++) {
                if (files[i].exists()) {
                    files[i].delete();
                }
            }
            imgFile.delete();
        }
        if (videoFile.exists()) {
            File[] files = videoFile.listFiles();
            if (files == null) return;
            for (int i = 0; i < files.length; i++) {
                if (files[i].exists()) {
                    files[i].delete();
                }
            }
            videoFile.delete();
        }
    }

    private RequestTool requestTool = new RequestTool(this);
    private final int REQUEST_MESSAGE_TAG_1 = 0X9999;

    /************************************* 安全退出 *************************************/
    /**
     * 向后台发送请求告知安全退出
     */
    public void requestClearData() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtil.getString(WheatFinanceApplication.getInstance(), AppConfig.getInstance().RESULT_USERID, ""));
        map.put("tokenId", SharedPreferencesUtil.getString(WheatFinanceApplication.getInstance(), AppConfig.getInstance().RESULT_TOKRNID_TAG, ""));
        requestTool.requestForHttp(WheatFinanceApplication.getInstance(), REQUEST_MESSAGE_TAG_1, logout, map, false);
    }

    /**
     * 判断用户是否登录状态
     */
    public boolean checkUserLoginStaus() {
        String userId = SharedPreferencesUtil.getString(WheatFinanceApplication.getInstance(), AppConfig.getInstance().RESULT_USERID, "");
        String userTokenId = SharedPreferencesUtil.getString(WheatFinanceApplication.getInstance(), AppConfig.getInstance().RESULT_TOKRNID_TAG, "");
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(userTokenId)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case REQUEST_MESSAGE_TAG_1:
                break;
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {

    }

    /************************************* 静态页面URL *************************************/
    public final String DATABASE_URL = "http://120.27.49.79";
    /**
     * 费率计算url
     */
    public final String lation_amount_url = DATABASE_URL + "/appStatic/view/feeExplain.html";
    /**
     * 填写规范url
     */
    public final String input_Standard_url = DATABASE_URL + "/appStatic/view/fillinStandard.html";
    /**
     * 更多URL
     */
    public final String MORE_URL = DATABASE_URL + "/appStatic/view/more.html";
    /**
     * 帮助中心
     */
    public final String HELP_CENTER_URL = DATABASE_URL + "/appStatic/view/helpCenter.html";

    /**
     * 提额页面
     */
    public final String QUOTA_MAIN_URL = DATABASE_URL + "/appStatic/view/amount.html";
    /**
     * 签到页面
     */
    public final String ACCOUNT_SIGN_URL = DATABASE_URL + "/appStatic/view/common.html";

    /**
     * 麦子速贷注册协议
     */
    public final String REGIST_URL = DATABASE_URL + "/appStatic/view/statement.html";

    /**
     * 贷款协议
     */
    public final String LOAN_URL = DATABASE_URL + "/appStatic/view/loanAgreement.html";

    /**
     * 征信协议
     */
    public final String XINYONG_URL = DATABASE_URL + "/appStatic/view/creditAuthorization.html";

    /**
     * 关于
     */
    public final String MIANZE_URL = DATABASE_URL + "/appStatic/view/about.html";

    /**
     * 免责声明
     */
    public final String ABOUT_URL = DATABASE_URL + "/appStatic/view/about.html";

    /**
     * 免息
     */
    public final String ACCOUNT_ACTIVITY_URL = DATABASE_URL + "/appStatic/view/activity.html";
    /**
     * 查询银行卡信息
     */
    public final String bankColorUrl = DATABASE_URL + "/appStatic/data/bankColor.json";

    /**
     * 获取banner图
     */
    public final String GET_BANNER = DATABASE_URL + "/appStatic/data/banner.json";

    /**
     * app banner 图
     */
    public final String BASE_BANNER = DATABASE_URL + "/images/banner";

    /**
     * bank 图
     */
    public final String BASE_BANK = DATABASE_URL + "/images/bank";

    /**
     * wechat alipay
     */
    public final String BASE_ALIPAY = DATABASE_URL + "/images/wechat";


    /************************************* 程序请求用URL *************************************/
    /**
     * 程序url
     */
    public final String appUrl = "http://101.37.99.110/v1";
//    public final String appUrl = "http://192.168.0.34:8099/v1";
    /**
     * 发送短信
     */
    public final String postMessage = "/sendMessage.do";
    /**
     * 获取图形验证码
     */
    public final String getImgCode = "/gainImgCode.do";
    /**
     * 用户注册
     */
    public final String UserReg = "/userinfo/register.do";
    /**
     * 用户登陆
     */
    public final String Userlogin = "/userinfo/loginByPwd.do";
    /**
     * 用户登陆
     */
    public final String UserTokenlogin = "/userinfo/loginByToken.do";
    /**
     * 密码重置
     */
    public final String UserPwdRegSetting = "/userinfo/resetPassword.do";
    /**
     * 根据id查询用户信息
     */
    public final String idInUserInfo = "/userinfo/queryUserInfoById.do";
    /**
     * 上传照片
     */
    public final String uploadIdCard = "/userauth/uploadIdCard.do";

    /**
     * 认证第一步 提交
     */
    public final String commitCustomerAuth = "/userauth/commitCustomerAuth.do";
    /**
     * 绑定银行卡第二步
     */
    public final String bindCardAuth = "/card/bindCardAuth.do";
    /**
     * 第三步
     */
    public final String contactsAuth = "/userauth/contactsAuth.do";
    /**
     * 第四步
     */
    public final String linkManAuth = "/userauth/linkManAuth.do";
    /**
     * 视频认证
     */
    public final String photoAuth = "/userauth/photoAuth.do";
    /**
     * 根据卡信息查询银行
     */
    public final String queryBankByNo = "/card/queryBankByNo.do";
    /**
     * 发送签约银行卡认证短信
     */
    public final String sendBindCardSmsCode = "/card/sendBindCardSmsCode.do";
    /**
     * 总提交
     */
    public final String submitAuthInfo = "/userauth/submitAuthInfo.do";
    /**
     * 安全退出
     */
    public final String logout = "/userinfo/logout.do";
    /**
     * 设置支付密码
     */
    public final String setPayPassword = "/userinfo/setPayPassword.do";
    /**
     * 提现用途
     */
    public final String queryPageData = "/trans/queryPageData.do";
    /**
     * 贷款操作
     */
    public final String applyLoan = "/trans/applyLoan.do";
    /**
     * 贷款成功确认
     */
    public final String applyLoanConfirm = "/trans/userConfirmLoan.do";


    /************************************* 程序查询请求用URL *************************************/
    /**
     * 查询身份证状态
     */
    public final String queryContactsAuth = "/userauth/queryContactsAuth.do";
    /**
     * 查询用户信息进度
     */
    public final String queryUserAuthInfo = "/userauth/queryUserAuthInfo.do";
    /**
     * 判断首页状态
     */
    public final String homeInitState = "/system/homeInitState.do";
    /**
     * 查询产品信息
     */
    public final String queryLoanProduct = "/product/queryLoanProduct.do";

    /**
     * 请求我的银行卡列表
     */
    public final String queryMyCard = "/card/querySignCard.do";
    /**
     * 查询历史账单
     */
    public final String queryUserLoanInfo = "/trans/queryUserLoanInfo.do";
    /**
     * 查询问卷题目
     */
    public final String QUESTION_LIST = "/userinfo/questionnaire.do";
    /**
     * 提交问卷调查答案
     */
    public final String UPLOAD_QUESTION_ANSWER = "/userinfo/submitQuestionnaire.do";


    /**
     * 提交问卷调查答案
     */
    public final String FEEDBACK = "/userinfo/suggestionFeedback.do";

    /**
     * 绑卡支持的银行列表
     */
    public final String BIND_CARD_BANK_LIST = "/card/queryChannelList.do";

    /**
     * 支付渠道
     */
    public final String PAY_CHANNEL = "/payment/queryPayChannel.do";

    /**
     * 查询支付订单信息
     */
    public final String PAY_LOAN_INFO = "/payment/placeRepayOrder.do";

    /**
     * 支付还款
     */
    public final String PAY_REPAY = "/payment/loanRepay.do";

    /**
     * 查询还款信息
     */
    public final String QUERY_PAY = "/payment/queryPayTrans.do";

    /**
     * 获取验证码
     */
    public final String GET_INVITED_CODE = "/userinfo/queryUserInvitedCode.do";

    /**
     * 查询轮播文字
     */
    public final String GET_ROLL_STR = "/userinfo/queryScrollbarMsg.do";

    /**
     * 获取免息券
     */
    public final String GET_COUPON_NUMBER = "/activity/queryCouponCount.do";

    /**
     * 查询免息券
     */
    public final String GET_COUPON = "/activity/queryCouponInfo.do";

    /**
     * 查询还款处理中订单信息
     */
    public final String GET_PENDING_TRADE = "/trans/queryRepayPendingOrderInfo.do";

    /**
     * 芝麻信用授权
     */
    public final String ZHIMAAUTHOR = "/userauth/authInfoAuthorize.do";





}
