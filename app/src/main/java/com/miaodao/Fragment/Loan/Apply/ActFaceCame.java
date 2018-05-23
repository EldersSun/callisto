package com.miaodao.Fragment.Loan.Apply;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Config.SysSDCardCacheDir;
import com.miaodao.Sys.Utils.ImgUtils;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.camera.CameraPreview;

import java.io.File;

/**
 * Created by daixinglong on 2017/4/25.
 */

public class ActFaceCame extends Activity implements View.OnClickListener, View.OnTouchListener {

    private SurfaceView sfFace;
    private TextView faceTake, tvCancel;
    private SurfaceHolder holder;
    private Camera camera;
    private File file;
    private View focusIndex;
    private Handler handler = new Handler();
    private CameraPreview preview;

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
        focusIndex = (View) findViewById(R.id.focus_index);
        sfFace = (SurfaceView) findViewById(R.id.sf_face);
        faceTake = (TextView) findViewById(R.id.face_take);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        file = new File(SysSDCardCacheDir.getVideoDir().getPath(), AppConfig.getInstance().VERIFICATION_ID_VIDEO_PHONE);

        holder = sfFace.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //保持屏幕的高亮
        sfFace.setKeepScreenOn(true);

        preview = new CameraPreview(this, sfFace);
        sfFace.setOnTouchListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x001);
            return;
        }

        openCamera();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                finish();
                return;
            }
        }
        openCamera();
    }


    /**
     * 开启照相机
     */
    private void openCamera() {
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                camera = Camera.open(1);
                camera.startPreview();
                preview.setCamera(camera);
                preview.reAutoFocus();
            } catch (RuntimeException ex) {
            }
        }
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
                takePhoto();
                break;


            case R.id.tv_cancel:
                finishThis();
                break;


            default:
                break;


        }
    }


    /**
     * 拍摄照片
     */
    private void takePhoto() {
        try {
            camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        } catch (Throwable t) {
            t.printStackTrace();
            Toast.makeText(this, "拍照失败", Toast.LENGTH_LONG).show();
            try {
                camera.startPreview();
            } catch (Throwable e) {

            }
        }
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
        }
    };


    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
        }
    };


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
        final Matrix matrix = new Matrix();
        matrix.setRotate(270);
        final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
        if (bitmap != null) {
            ImgUtils.saveBitmap(bitmap, file);
            goPhotoShow();
            finishThis();
        }
    }


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
        destroyCamera();
        finish();
    }


    /**
     * 释放相机
     */
    private void destroyCamera() {

        if (camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
            preview.setNull();
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                preview.pointFocus(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                focusIndex.getLayoutParams());
        layout.setMargins((int) event.getX() - 60, (int) event.getY() - 60, 0, 0);

        focusIndex.setLayoutParams(layout);
        focusIndex.setVisibility(View.VISIBLE);

        ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(800);
        focusIndex.startAnimation(sa);
        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                focusIndex.setVisibility(View.INVISIBLE);
            }
        }, 800);
        return false;
    }
}
