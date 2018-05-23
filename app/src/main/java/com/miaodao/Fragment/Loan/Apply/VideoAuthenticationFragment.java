package com.miaodao.Fragment.Loan.Apply;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miaodao.Base.ContentBaseFragment;
import com.miaodao.Base.ServiceBaseActivity;
import com.miaodao.Fragment.Product.ProductSummaryFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Shared.SharedPreferencesUtil;
import com.miaodao.Sys.Utils.PermissionUtil;
import com.miaodao.Sys.Utils.StringUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;
import com.miaodao.Sys.Widgets.ShapedImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 视频认证
 * Created by Home_Pc on 2017/3/15.
 */

public class VideoAuthenticationFragment extends ContentBaseFragment implements View.OnClickListener {

    private Button viedo_submit;
    private ShapedImageView viedo_img, viedo_Recording;//视频认证拍照
    private ImageView show;//视频认证拍照
    private TextView tvRecordVideo, tvRecordPhoto;

    private final int REQUEST_MESSAGE_TAG = 0X1051;
    private final int REQUEST_MESSAGE_TAG_2 = 0X1052;
    private String tag = "";
    private File imageFile, videoFile;

    @Override
    protected View initViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_viedo_authe_layout, null);
    }

    @Override
    protected void initWidgets(View fgView) {
        title_menu.setBackgroundResource(R.color.transparentColor);
        title_menu.setText(R.string.Viedo_message_2);
        title_tvShow.setText(R.string.Viedo_message_1);

        viedo_Recording = (ShapedImageView) fgView.findViewById(R.id.viedo_Recording);
        viedo_img = (ShapedImageView) fgView.findViewById(R.id.viedo_img);
        tvRecordVideo = (TextView) fgView.findViewById(R.id.tv_record_video);
        tvRecordPhoto = (TextView) fgView.findViewById(R.id.tv_record_photo);

        viedo_submit = (Button) fgView.findViewById(R.id.viedo_submit);
        show = (ImageView) fgView.findViewById(R.id.show);
    }

    @Override
    protected void initEvent() {

        if (!PermissionUtil.hasPermission(getActivity(), Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)) {
            PermissionUtil.requestPermission(getActivity(), 0x000, Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO);
        }
        viedo_Recording.setOnClickListener(this);
        viedo_submit.setOnClickListener(this);
        viedo_img.setOnClickListener(this);
        observeMessage(AppConfig.getInstance().VERIFICATION_ID_MESSAGE);
        observeMessage(AppConfig.getInstance().VERIFICATION_VIDEO_MESSAGE);

        Bundle bundle = getArguments();
        if (bundle.containsKey("tag")) {
            tag = bundle.getString("tag");
        }

        setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelp.getInstance(getActivity()).showDialog(R.string.Prompt, R.string.backMessage,
                        R.string.cancel, R.string.ok, new CustomDialog.OnSureInterface() {
                            @Override
                            public void getOnSure() {
                                if (StringUtils.isBlank(tag)) {
                                    ServiceBaseActivity.startActivity(getActivity(), ProductSummaryFragment.class.getName());
                                }
                                getActivity().finish();
                            }

                            @Override
                            public void getOnDesmiss() {

                            }
                        });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        imageFile = new File(AppConfig.getInstance().VERIFICATION_ID_PHONE_PATH);
        videoFile = new File(AppConfig.getInstance().VERIFICATION_ID_VIDEO_PATH);


        if (imageFile.exists()) {
            Glide.
                    with(this).
                    load(imageFile).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    skipMemoryCache(true).
                    into(viedo_img);
            tvRecordPhoto.setVisibility(View.VISIBLE);
        } else {
            tvRecordPhoto.setVisibility(View.GONE);
            Glide.
                    with(this).
                    load(R.drawable.ic_commit_real_avatar).
                    into(viedo_img);
        }


        if (videoFile.exists()) {
            Glide.
                    with(this).
                    load(videoFile).
                    asBitmap().
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    skipMemoryCache(true).
                    into(viedo_Recording);
            tvRecordVideo.setVisibility(View.VISIBLE);
        } else {
            tvRecordVideo.setVisibility(View.GONE);
            Glide.
                    with(this).
                    load(R.drawable.ic_commit_video).
                    into(viedo_Recording);
        }


    }

    @Override
    public void onResponsSuccess(int TAG, Object result) {
        switch (TAG) {
            case REQUEST_MESSAGE_TAG:
                Map<String, Object> submitMap = new HashMap<>();
                String userId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, "");
                String tokenId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, "");
                submitMap.put("userId", userId);
                submitMap.put("tokenId", tokenId);
                requestForHttp(REQUEST_MESSAGE_TAG_2, AppConfig.getInstance().submitAuthInfo, submitMap);
                break;

            case REQUEST_MESSAGE_TAG_2:
                dismissProressDialog();
                ToastUtils.shortShow(R.string.submitSucc);
                if (StringUtils.isBlank(tag)) {
//                    ServiceBaseActivity.startActivity(getActivity(), "com.wheatfinance.Fragment.Loan.Apply.BindCardFragment");
                } else {
                    EventBus.getDefault().post(AppConfig.getInstance().VERIFICATION_USER_MESSAGE_1);
                }
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onResponsFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
        if ("认证申请处理中".equals(result)) {
            sendMessage(AppConfig.getInstance().JUMP_MESSAGE_TAG_1, "");
            getActivity().finish();
        }
    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        dismissProressDialog();
        ToastUtils.shortShow(result);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.viedo_Recording:
                showProgressDailog();
                intent = new Intent(getActivity(), ActRecordVide.class);
//                intent = new Intent(getActivity(), ActRecordVideo.class);
                startActivity(intent);
                break;
            case R.id.viedo_submit:

                if (!videoFile.exists()) {
                    ToastUtils.shortShow("请先录制视频");
                    return;
                }
                if (!imageFile.exists()) {
                    ToastUtils.shortShow("请先拍摄照片");
                    return;
                }

                Map<String, File> fileMap = new HashMap<>();
                Map<String, String> map = new HashMap<>();
                String userId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_USERID, "");
                String tokenId = SharedPreferencesUtil.getString(getActivity(), AppConfig.getInstance().RESULT_TOKRNID_TAG, "");
                map.put("userId", userId);
                map.put("tokenId", tokenId);
                fileMap.put("video", videoFile);
                fileMap.put("pic", imageFile);
                requestForHttpFile(REQUEST_MESSAGE_TAG, AppConfig.getInstance().photoAuth, fileMap, map);
                showProgressDailog();
                break;
            case R.id.viedo_img:


                if (!PermissionUtil.hasPermission(getActivity(), Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)) {

                    PermissionUtil.requestPermission(getActivity(), 0x000, Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO);

                    return;
                }

                intent = new Intent(getActivity(), ActFaceCame.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onReceiveMessage(String msgkey, Object msgObject) {
        super.onReceiveMessage(msgkey, msgObject);
//        if (msgkey.equals(AppConfig.getInstance().VERIFICATION_ID_MESSAGE)) {
//            String filePath = (String) msgObject;
//            Bitmap bitmap = ImgUtils.fileToBitmap(filePath);
////            Bitmap backBitmap = ImgUtils.getBitmapProportionScale(bitmap);
////            ImgUtils.saveBitmap(filePath, backBitmap);
//            WheatFinanceApplication.getInstance().setLruCache(AppConfig.getInstance().IdAuthenticationBitmap, bitmap);
//            viedo_img.setBackground(ImgUtils.bitmapToDrawable(bitmap));
//            viedo_img.setImageResource(R.drawable.btn_idcarimgshow_selector);
//        } else if (msgkey.equals(AppConfig.getInstance().VERIFICATION_VIDEO_MESSAGE)) {
//            Bitmap bitmap = VideoUtils.getVideoThumb(AppConfig.getInstance().VERIFICATION_ID_VIDEO_PATH);
//            viedo_Recording.setBackground(ImgUtils.bitmapToDrawable(bitmap));
//        }
    }


    @Override
    protected boolean OnkeyDownListener(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogHelp.getInstance(getActivity()).showDialog(R.string.Prompt, R.string.backMessage,
                    R.string.cancel, R.string.ok, new CustomDialog.OnSureInterface() {
                        @Override
                        public void getOnSure() {
                            if (StringUtils.isBlank(tag)) {
                                ServiceBaseActivity.startActivity(getActivity(), ProductSummaryFragment.class.getName());
                            }
                            getActivity().finish();
                        }

                        @Override
                        public void getOnDesmiss() {

                        }
                    });
            return true;
        }
        return super.OnkeyDownListener(keyCode, event);
    }


    @Override
    public void onStop() {
        super.onStop();
        dismissProressDialog();
    }
}
