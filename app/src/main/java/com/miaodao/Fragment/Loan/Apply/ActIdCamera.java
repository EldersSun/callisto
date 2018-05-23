package com.miaodao.Fragment.Loan.Apply;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Config.SysSDCardCacheDir;
import com.miaodao.Sys.Utils.ImgUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.camera.CameraUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by daixinglong on 2017/4/21.
 */

public class ActIdCamera extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    public static final String ID_TAG = "ID_TAG";
    public static final String ID_FRONT = "F";
    public static final String ID_BACK = "B";

    private SurfaceView surPreview;//拍着预览
    private CheckBox controlFlash;//闪光灯控制
    private TextView controlTake;//点击拍照
    private TextView controlCancel;//取消拍照
    private TextView tvTip1;//拍照顶部提示文字
    //    private ImageView ivIdShowImg;//身份证遮罩层
    private ImageView ivIdTip;//身份证展示图
    private LinearLayout llIdCardMask;//身份证遮罩层

    private SurfaceHolder surfaceHolder;//
    private Camera camera;
    private Intent intent;
    private String idTag = "";
    private String savePath = "";//用于保存图片的路径
    private File file;
    private int camera_id = 0;
    private IOrientationEventListener iOriListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fm_id_camera);

        initParams();
        initActions();
    }


    /**
     * 初始化参数
     */
    private void initParams() {

        surPreview = (SurfaceView) findViewById(R.id.sur_preview);
        controlFlash = (CheckBox) findViewById(R.id.control_flash);
//        ivIdShowImg = (ImageView) findViewById(R.id.iv_id_show_img);
        ivIdTip = (ImageView) findViewById(R.id.iv_id_tip);
        controlTake = (TextView) findViewById(R.id.control_take);
        controlCancel = (TextView) findViewById(R.id.control_cancel);
        tvTip1 = (TextView) findViewById(R.id.tv_tip1);
        llIdCardMask = (LinearLayout) findViewById(R.id.ll_id_card_mask);

        intent = getIntent();
        if (intent == null) return;
        idTag = intent.getStringExtra(ID_TAG);
        showPreview();
        file = new File(SysSDCardCacheDir.getImgDir().getPath(), savePath);
        surfaceHolder = surPreview.getHolder();
        //surfaceView不需要自己的缓冲区,直接输出
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //保持屏幕的高亮
        surPreview.setKeepScreenOn(true);
        surfaceHolder.addCallback(this);

        iOriListener = new IOrientationEventListener(this);
    }


    /**
     * 显示不同的界面
     */
    private void showPreview() {

        switch (idTag) {

            case ID_FRONT:
//                ivIdShowImg.setImageResource(R.drawable.ic_idcard_front_skeleton);
                llIdCardMask.setBackgroundResource(R.drawable.id_front_mask);
                ivIdTip.setImageResource(R.drawable.ic_tips_id_front);
                tvTip1.setText(R.string.id_camera_tip_front);
                savePath = AppConfig.getInstance().VERIFICATION_ID_FRONT_PHONE;
                break;

            case ID_BACK:
//                ivIdShowImg.setImageResource(R.drawable.ic_idcard_back_skeleton);
                llIdCardMask.setBackgroundResource(R.drawable.id_back_mask);
                ivIdTip.setImageResource(R.drawable.ic_tips_id_back);
                tvTip1.setText(R.string.id_camera_tip_back);
                savePath = AppConfig.getInstance().VERIFICATION_ID_BACK_PHONE;
                break;

            default:
                break;

        }


    }


    /**
     * 设置事件监听
     */
    private void initActions() {
        controlTake.setOnClickListener(this);
        controlCancel.setOnClickListener(this);

        controlFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Camera.Parameters parameters = camera.getParameters();
                if (isChecked) {
                    controlFlash.setChecked(true);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                } else {
                    controlFlash.setChecked(false);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭
                }

                camera.setParameters(parameters);
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.control_take:
                if (camera == null) return;
                takePhoto();
                break;

            case R.id.control_cancel:
                finish();
//                if (file.exists()) {
//                    file.delete();
//                }
                break;

            default:
                break;


        }


    }


    private void takePhoto() {

        try {
            camera.takePicture(null, null, jpegCallback);
        } catch (Throwable t) {
            t.printStackTrace();
            Toast.makeText(this, "拍照失败", Toast.LENGTH_LONG).show();
            try {
                camera.startPreview();
            } catch (Throwable e) {

            }
        }


    }


    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            saveImage(data);
        }
    };


    private void saveImage(byte[] data) {

        if (data == null) {
            ToastUtils.shortShow("拍照失败");
            return;
        }
        final Bitmap resource = ImgUtils.getFitSampleBitmap(data);
        if (resource == null) {
            ToastUtils.shortShow("拍照失败");
            return;
        }
//            final Matrix matrix = new Matrix();
//            matrix.setRotate(90);
//            final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
        if (resource != null) {
            ImgUtils.saveBitmap(resource, file);
            goPhotoShow();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化camera
        initCamera();
        showImgDialog();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setCameraAndDisplay(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 释放摄像头资源
        releaseCamera();
    }

    /**
     * 初始化相机
     */
    private void initCamera() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0x001);
            finish();
            return;
        }
        openCamera();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        openCamera();

    }

    /**
     * 打开照相机
     */
    private void openCamera() {

        int mNumberOfCameras = Camera.getNumberOfCameras();
        // Find the ID of the default camera
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                camera_id = i;
            }
        }
        camera = Camera.open(camera_id);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview(); // 开始预览
            iOriListener.enable();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * 显示身份证反面对话框
     */
    private void showImgDialog() {
        final Dialog customViewDialog = new Dialog(this, R.style.dialog);
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.v_dialog_message_back_layout, null);//获取自定义布局
        ImageView ivDialog = (ImageView) layout.findViewById(R.id.iv_dialog);
        TextView tvDialog = (TextView) layout.findViewById(R.id.tv_dialog);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        customViewDialog.addContentView(layout, layoutParams);
        customViewDialog.setCanceledOnTouchOutside(false);
        final Button CameraDialogBack_picture = (Button) layout.findViewById(R.id.CameraDialogBack_picture);
        if (idTag.equals(ID_BACK)) {
            ivDialog.setImageResource(R.drawable.messgae_default_back);
            tvDialog.setText(R.string.personal_message_11);
            CameraDialogBack_picture.setText(R.string.personal_message_12);
        } else {
            ivDialog.setImageResource(R.drawable.messgae_default_front);
            tvDialog.setText(R.string.personal_message_10);
            CameraDialogBack_picture.setText(R.string.personal_message_9);
        }

        CameraDialogBack_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customViewDialog.dismiss();
            }
        });
        customViewDialog.show();
    }


    Camera.PictureCallback pictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data == null) {
                ToastUtils.shortShow("拍照失败");
                return;
            }
            final Bitmap resource = ImgUtils.getFitSampleBitmap(data);
            if (resource == null) {
                ToastUtils.shortShow("拍照失败");
                return;
            }
//            final Matrix matrix = new Matrix();
//            matrix.setRotate(90);
//            final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
            if (resource != null) {
                ImgUtils.saveBitmap(resource, file);
                goPhotoShow();
            }
        }
    };


    /**
     * 展示拍摄的照片
     */
    private void goPhotoShow() {
        releaseCamera();
        Intent intent = new Intent(this, ActIdPhoto.class);
        intent.putExtra("savePath", savePath);
        intent.putExtra(ID_TAG, idTag);
        startActivity(intent);
        finish();
    }


    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
        finish();
    }


    public class IOrientationEventListener extends OrientationEventListener {

        public IOrientationEventListener(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }


        @Override
        public void onOrientationChanged(int orientation) {
            // TODO Auto-generated method stub
            if (ORIENTATION_UNKNOWN == orientation) {
                return;
            }
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(camera_id, info);
            orientation = (orientation + 45) / 90 * 90;
            int rotation = 0;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else {
                rotation = (info.orientation + orientation) % 360;
            }
            if (null != camera) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setRotation(rotation);
                camera.setParameters(parameters);
            }

        }

    }

    public void setCameraAndDisplay(int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        /*获取摄像头支持的PictureSize列表*/
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        /*从列表中选取合适的分辨率*/
        Camera.Size picSize = CameraUtils.getProperSize(pictureSizeList, ((float) width) / height);
        if (null != picSize) {
            parameters.setPictureSize(picSize.width, picSize.height);
        } else {
            picSize = parameters.getPictureSize();
        }
        /*获取摄像头支持的PreviewSize列表*/
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size preSize = CameraUtils.getProperSize(previewSizeList, ((float) width) / height);
        if (null != preSize) {
            Log.v("TestCameraActivityTag", preSize.width + "," + preSize.height);
            parameters.setPreviewSize(preSize.width, preSize.height);
        }

        /*根据选出的PictureSize重新设置SurfaceView大小*/
        float w = picSize.width;
        float h = picSize.height;
        surPreview.setLayoutParams(new RelativeLayout.LayoutParams((int) (height * (w / h)), height));
//        surPreview.setLayoutParams(new RelativeLayout.LayoutParams(height, width));

        parameters.setJpegQuality(100); // 设置照片质量

        //先判断是否支持，否则会报错
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
        camera.setDisplayOrientation(0);
        camera.setParameters(parameters);


    }


}
