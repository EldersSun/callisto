package com.miaodao.Fragment.Loan.Apply;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Config.SysSDCardCacheDir;
import com.miaodao.Sys.Utils.ImgUtils;
import com.miaodao.Sys.Utils.ToastUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by daixinglong on 2017/4/25.
 */

public class ActFaceCamera extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView sfFace;
    private TextView faceTake, tvCancel;
    private SurfaceHolder holder;
    private Camera camera;
    private File file;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_video_face);
        initParams();
        initActions();
    }


    /**
     *
     */
    private void initParams() {
        sfFace = (SurfaceView) findViewById(R.id.sf_face);
        faceTake = (TextView) findViewById(R.id.face_take);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        file = new File(SysSDCardCacheDir.getVideoDir().getPath(), AppConfig.getInstance().VERIFICATION_ID_VIDEO_PHONE);

        camera = Camera.open(1);//默认开启后置



        holder = sfFace.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //保持屏幕的高亮
        sfFace.setKeepScreenOn(true);
        holder.addCallback(this);
    }


    /**
     *
     */
    private void initActions() {
        faceTake.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.face_take:
                if (camera == null) return;
//                camera.autoFocus(autoFocusCallBack);

                camera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {
                        //按下快门的瞬间操作
                    }
                }, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        //是否保存原始图片信息
                    }
                }, pictureCallBack);


                break;


            case R.id.tv_cancel:
                finishThis();
                break;


            default:
                break;


        }


    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化camera
//        initCamera();
        if (camera == null) return;
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {


        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera();
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0x001);
            return;
        }

        try {
//            camera = Camera.open(1);//默认开启后置
            if (camera == null) return;
//            camera.setPreviewDisplay(holder);
//            camera.setDisplayOrientation(90);//摄像头进行旋转90度
            Camera.Parameters parameters = camera.getParameters();
            //设置相机预览照片帧数
//            parameters.setPreviewFpsRange(4, 10);
            //设置图片格式
            parameters.setPictureFormat(ImageFormat.JPEG);
            //设置图片的质量
            parameters.setJpegQuality(100);
            //设置预览照片的大小

//            WindowManager windowManager = getWindowManager();
//            Display display = windowManager.getDefaultDisplay();
//            int screenWidth = display.getWidth();
//            int screenHeight = display.getHeight();
//            Camera.Parameters mParameters = camera.getParameters();
//            mParameters.setPictureSize(screenWidth, screenHeight);
//            camera.setParameters(mParameters);

            parameters.setPictureSize(1080, 1920);
//            parameters.setPictureSize(1024,768);
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(parameters);
            camera.startPreview();


        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    Camera.AutoFocusCallback autoFocusCallBack = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                camera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {
                        //按下快门的瞬间操作
                    }
                }, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        //是否保存原始图片信息
                    }
                }, pictureCallBack);

            }
        }
    };


    Camera.PictureCallback pictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data == null) {
                ToastUtils.shortShow("拍照失败");
                return;
            }
//            final Bitmap resource = ImgUtils.getFitSampleBitmap(data);
            final Bitmap resource = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (resource == null) {
                ToastUtils.shortShow("拍照失败");
                return;
            }
            final Matrix matrix = new Matrix();
            matrix.setRotate(270);
            final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
            if (bitmap != null) {
                ImgUtils.saveBitmap(bitmap, file);
                goPhotoShow();
                finishThis();
            }
        }
    };


    /**
     *
     */
    private void goPhotoShow() {
        Intent intent = new Intent(this, ActFacePreview.class);
        startActivity(intent);
    }


    /**
     * 关闭当前act
     */
    private void finishThis() {
        releaseCamera();
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

}
