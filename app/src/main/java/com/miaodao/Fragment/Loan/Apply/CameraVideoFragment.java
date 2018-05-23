package com.miaodao.Fragment.Loan.Apply;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.miaodao.AIDL.MusicCallback;
import com.miaodao.Base.BaseFragment;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Config.SysSDCardCacheDir;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.AutoFitTextureView;
import com.miaodao.Sys.Widgets.PlayVideoView;
import com.miaodao.Utils.VideoTvShowAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraVideoFragment extends BaseFragment implements View.OnClickListener, TextureView.SurfaceTextureListener {

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    private static final String TAG = "CameraVideoFragment";
    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final String FRAGMENT_DIALOG = "dialog";

//    private static final String[] VIDEO_PERMISSIONS = {
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
//    };

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private AutoFitTextureView mTextureView;


    private GridView video_tvShow;

    /**
     */
    private CameraDevice mCameraDevice;

    /**
     * 预览。
     */
    private CameraCaptureSession mPreviewSession;


    /**
     */
    private Size mPreviewSize;

    /**
     */
    private Size mVideoSize;

    /**
     * MediaRecorder
     */
    private MediaRecorder mMediaRecorder;

    /**
     * 应用程序是否正在录制视频
     */
    private boolean mIsRecordingVideo;

    /**
     * 一个运行的任务，不应该阻止用户界面的一个额外的线程。
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     * 在后台运行任务
     */
    private Handler mBackgroundHandler;

    /**
     * 为了防止应用程序退出之前关闭相机。
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * 改变它的状态
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != mTextureView) {
                configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            mCameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            mCameraDevice.close();
            mCameraDevice = null;
//            Activity activity = getActivity();
//            if (null != activity) {
//                activity.finish();
//            }
            startPreview();
        }

    };
    private Integer mSensorOrientation;
    private String mNextVideoAbsolutePath;
    private CaptureRequest.Builder mPreviewBuilder;
    private Surface mRecorderSurface;
    private PlayVideoView video_playerView;
    private TextView viedo_time;

    /**
     * 在这个例子中，我们选择一个3x4纵横比的视频大小。另外，我们不使用尺寸大于1080p，由于mediarecorder无法处理如此高分辨率的视频。
     *
     * @param choices The list of available sizes
     * @return The video size
     */
    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    /**
     * 在由相机支持的{ @代码大小}的“代码选择”}中，选择最小的宽度和高度至少与所请求的值相匹配的最小的一个，并且其纵横比符合指定的值。
     *
     * @param choices     The list of sizes that the camera supports for the intended output class
     * @param width       The minimum desired width
     * @param height      The minimum desired height
     * @param aspectRatio The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        //收集支持的分辨率至少和预览表面一样大
        List<Size> bigEnough = new ArrayList<Size>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        //选择最小的那些，假设我们发现了
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    @Override
    protected View initWidgetsViews(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fm_cameraauthent_layout, null);
    }

    private Button video_record, viedo_cancel;
    private VideoTvShowAdapter adapter;
    private TvShowTimer tvShowTimer = new TvShowTimer(1000 * 8, 250);
    private int ColorPosition = 0;
    private TextView tvShowTip;

    /**
     * 是否进行重新录像
     */
    private boolean isRestartRecore = false;
    /**
     * 是否完成录制
     */
    private boolean isSuccRecore = false;
    private boolean isTvShow = false;
    private MusicCallback musicCallbak;
    private ControlMusicServiceConnection connection;
    /**
     * 倒计时开始录像的字幕
     */
    private ShowTimeCount showTimeCount = new ShowTimeCount(3200, 1000);
    private boolean isshowTimeCountFlag = false;
    /**
     * 录制视频播放时长tag
     */
    private final int MESSAGE_TIME_RECORD = 01;
    private final int videoTimeLong = 16 * 1000;

    private class ControlMusicServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicCallbak = MusicCallback.Stub.asInterface(service);
            try {
                musicCallbak.setMusicPlayerType(true);
                boolean isPlaying = musicCallbak.getMusicPlayerType();
            } catch (RemoteException e) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void initServicer() {
        connection = new ControlMusicServiceConnection();
        Intent intent = new Intent(getActivity(), CameraVideoPlayerMusicService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        releaseService();

        closeCamera();
        stopBackgroundThread();
        closeMediaPlay();
        timeHandler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }

    private void releaseService() {
        try {
            musicCallbak.setMusicPlayerType(false);
            getActivity().unbindService(connection);
            connection = null;
        } catch (RemoteException e) {
        }
    }

    private boolean checkSelfPermissions(String permission) {
        boolean isPermission = ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
        return isPermission;
    }

    @Override
    protected void initWidgets(View fgView) {
        mTextureView = (AutoFitTextureView) fgView.findViewById(R.id.video_texture);
        video_record = (Button) fgView.findViewById(R.id.video_record);
        viedo_cancel = (Button) fgView.findViewById(R.id.viedo_cancel);
        video_tvShow = (GridView) fgView.findViewById(R.id.video_tvShow);
        video_playerView = (PlayVideoView) fgView.findViewById(R.id.video_playerView);
        viedo_time = (TextView) fgView.findViewById(R.id.viedo_time);
        tvShowTip = (TextView) fgView.findViewById(R.id.tv_show_tip);
        video_record.setOnClickListener(this);
        viedo_cancel.setOnClickListener(this);

        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(this);
        }

    }

    @Override
    protected void initEvent() {
        adapter = new VideoTvShowAdapter(getActivity());
        video_tvShow.setAdapter(adapter);
        initServicer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
//                    ColorPosition++;
                    tvShowTimer.start();
                    isTvShow = true;
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                          int width, int height) {
        openCamera(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                            int width, int height) {
        configureTransform(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * 更新字幕
     */
    private class TvShowTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TvShowTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            adapter.setColorPosition(++ColorPosition);
        }

        @Override
        public void onFinish() {
            isTvShow = false;
//            adapter.setColorPosition(-1);
        }
    }

    /**
     * 计时
     */
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_TIME_RECORD:
                    /**
                     * 完成录制
                     */
                    isSuccRecore = false;
                    mIsRecordingVideo = true;
                    stopRecordingVideo();
                    break;
            }
        }
    };

    @Override
    public void onResponsSuccess(int TAG, Object result) {

    }

    @Override
    public void onResponsFailed(int TAG, String result) {

    }

    @Override
    public void onNetConnectFailed(int TAG, String result) {
        ToastUtils.shortShow(result);
    }

    @Override
    public void onResume() {
        super.onResume();
//        startBackgroundThread();
//        if (mTextureView.isAvailable()) {
//            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
//        } else {
//            mTextureView.setSurfaceTextureListener(this);
//        }
    }

    private void closeMediaPlay() {
        try {
            if (isTvShow) {
                tvShowTimer.cancel();
            }
            if (isshowTimeCountFlag) {
                showTimeCount.cancel();
            }
            musicCallbak.setMusicPlayerType(false);
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onPause() {
//        closeCamera();
//        stopBackgroundThread();
//        closeMediaPlay();
//        timeHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    private int timeTvShow = 3;

    private class ShowTimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public ShowTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            viedo_time.setText(timeTvShow-- + "");
        }

        @Override
        public void onFinish() {
            viedo_time.setVisibility(View.GONE);
            closeMediaPlay();
            ColorPosition = 1;
            adapter.setColorPosition(ColorPosition++);
            tvShowTimer.start();
            startRecordingVideo();
            isshowTimeCountFlag = false;
            timeTvShow = 3;
            timeHandler.sendEmptyMessageDelayed(MESSAGE_TIME_RECORD, videoTimeLong);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_record:
                if (!isSuccRecore) {
                    if (mIsRecordingVideo) {
                        stopRecordingVideo();
                        timeHandler.removeCallbacksAndMessages(null);
                    } else {
                        try {
                            viedo_time.setVisibility(View.VISIBLE);
                            if (video_playerView.getVisibility() == View.VISIBLE) {
                                video_playerView.setVisibility(View.GONE);
                            }
                            isshowTimeCountFlag = true;
                            musicCallbak.setMusicPlayerType(false);
                            showTimeCount.start();
                        } catch (RemoteException e) {
                        }
                    }
                } else {
                    //录制完成
                    /**
                     * 通知前面页面
                     */
//                    sendMessage(AppConfig.getInstance().VERIFICATION_VIDEO_MESSAGE, mNextVideoAbsolutePath);
                    getActivity().finish();
                }

                break;
            case R.id.viedo_cancel:
                if (isRestartRecore) {
                    isRestartRecore = false;
                    video_playerView.setAnimation(setAlphAnimation(video_playerView, 1, 0));
                    video_playerView.setVisibility(View.GONE);
                    video_record.performClick();
                } else {
                    closeMediaPlay();
                    getActivity().finish();
                }
                break;
        }
    }

    /**
     * 开始一个背景线和它的
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * 开始一个背景线和它的
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
        }
    }


//    private boolean hasPermissionsGranted(String[] permissions) {
//        for (String permission : permissions) {
//            if (ActivityCompat.checkSelfPermission(getActivity(), permission)
//                    != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_PHONE:
//                if (!isPermission().containsKey("true")) {
//                    DialogHelp.getInstance(getActivity()).showDialog(R.string.lead_message, R.string.lead_message_2, R.string.cancel,
//                            R.string.lead_message_3, new CustomDialog.OnSureInterface() {
//                                @Override
//                                public void getOnSure() {
//                                    goToAppSetting();
//                                }
//
//                                @Override
//                                public void getOnDesmiss() {
//                                    getActivity().finish();
//                                }
//                            });
//                }
//                break;
//        }
//    }

    private final String PACKAGE = "package";
    private final int jumpResultCode = 0x1001;
    private final int REQUEST_PHONE = 0;

//    private Map<String, Boolean> isPermission() {
//        Map<String, Boolean> map = new HashMap<>();
//        for (int i = 0; i < VIDEO_PERMISSIONS.length; i++) {
//            if (checkSelfPermissions(VIDEO_PERMISSIONS[i])) {
//                map.put("true", true);
//            } else {
//                map.put("false", false);
//            }
//        }
//        return map;
//    }

//    // 跳转到当前应用的设置界面
//    private void goToAppSetting() {
//        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts(PACKAGE, getPackageName(), null);
//        intent.setData(uri);
//        startActivityForResult(intent, jumpResultCode);
//    }

    /**
     */
    private void openCamera(int width, int height) {
//        if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
//            ActivityCompat.requestPermissions(getActivity(), VIDEO_PERMISSIONS, REQUEST_PHONE);
//            return;
//        }
        final Activity activity = getActivity();
        if (null == activity || activity.isFinishing()) {
            return;
        }
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            Log.d(TAG, "tryAcquire");
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            //0后置，1前置
            String cameraId = manager.getCameraIdList()[1];

            //选择相机预览和视频记录的大小
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
//            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
//                    width, height, mVideoSize);
            mPreviewSize = new Size(mTextureView.getWidth(), mTextureView.getHeight());

            int orientation = getResources().getConfiguration().orientation;
            configureTransform(width, height);
            mMediaRecorder = new MediaRecorder();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            Toast.makeText(activity, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            activity.finish();
        } catch (NullPointerException e) {
        } catch (InterruptedException e) {
            //试图锁定相机打开时中断。
//            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
//            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e) {
            //试图锁定相机关闭时中断。
//            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Start the camera preview.
     * 启动相机预览。
     */
    private void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
//            closePreviewSession();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);

            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Activity activity = getActivity();
                    if (null != activity) {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
        }
    }

    /**
     * 更新预览
     */
    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        try {
            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {

        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

    /**
     * 配置必要的{@链接Android。图形。矩阵变换` mtextureview ` }。这种方法不应该是直到相机预览的大小是确定的opencamera称，
     * 直到大小` mtextureview `固定。
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void setUpMediaRecorder() throws IOException {
        final Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
            mNextVideoAbsolutePath = getVideoFilePath();
        }
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
//        mMediaRecorder.setVideoSize(720, 1280);
        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setVideoFrameRate(20);
        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setMaxDuration(videoTimeLong);
        mMediaRecorder.setVideoEncodingBitRate(800000);
        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {

            }
        });

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        mMediaRecorder.prepare();
    }

    private String getVideoFilePath() {
        return SysSDCardCacheDir.getVideoDir() + "/"
                + AppConfig.getInstance().VideoAuthenticationFrie;
    }

    private void startRecordingVideo() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            //设置相机预览的表面
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            //对于mediarecorder设置表
            mRecorderSurface = mMediaRecorder.getSurface();
            surfaces.add(mRecorderSurface);
            mPreviewBuilder.addTarget(mRecorderSurface);

            //开始一个捕获会话,一旦会话开始，我们可以更新用户界面和开始录制
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            video_record.setText(R.string.recordSucc);
                            mIsRecordingVideo = true;
                            //开始录像
                            mMediaRecorder.start();
                        }
                    });
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Activity activity = getActivity();
                    if (null != activity) {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
        } catch (IOException e) {
        }

    }

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            try {
                mPreviewSession.stopRepeating();
                mPreviewSession.abortCaptures();
                mPreviewSession.close();
                mPreviewSession = null;
            } catch (CameraAccessException e) {

            }
        }
    }

    private void stopRecordingVideo() {
        try {
            // UI
            mIsRecordingVideo = false;
            video_record.setText(R.string.record);
            viedo_cancel.setText(R.string.restartRecord);

            mPreviewSession.stopRepeating();
            mPreviewSession.abortCaptures();

            // Stop recording
            //停止
            if (mMediaRecorder != null) {
//                mMediaRecorder.stop();
//                mMediaRecorder.reset();
                mMediaRecorder.release();
            }

            playerVideo();

//        Activity activity = getActivity();
//        if (null != activity) {
//            Toast.makeText(activity, "Video saved: " + mNextVideoAbsolutePath,
//                    Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Video saved: " + mNextVideoAbsolutePath);
//        }
//        mNextVideoAbsolutePath = null;
//        startPreview();
        } catch (CameraAccessException e) {
        }
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            //我们将在这里以确保乘法不会溢出
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }
    }

    /**
     * 开始播放视频
     */
    private void playerVideo() {
        File playerFile = new File(mNextVideoAbsolutePath);
        if (!playerFile.exists()) {
            ToastUtils.shortShow(R.string.recordMessage_1);
            return;
        }
        video_playerView.setVisibility(View.VISIBLE);
        video_playerView.setAnimation(setAlphAnimation(video_playerView, 0, 1));
        video_playerView.setVideoPath(mNextVideoAbsolutePath);
        video_record.setVisibility(View.GONE);
        tvShowTip.setText("正在回放朗读内容,请稍等...");
        viedo_cancel.setVisibility(View.GONE);
        video_playerView.start();
        video_playerView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("通知", "完成");
                video_record.setVisibility(View.VISIBLE);
                viedo_cancel.setVisibility(View.VISIBLE);
                video_record.setText(R.string.ok);
                tvShowTip.setText(R.string.Viedo_message_5);
                /**
                 * 重新录制
                 */
                isRestartRecore = true;
                /**
                 * 将录制flag置为默认
                 */
                mIsRecordingVideo = false;
                /**
                 * 设置为已经录制完成
                 */
                isSuccRecore = true;
            }
        });
    }

    private Animation setAlphAnimation(View view, float fromAlpha, float toAlpha) {
        Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(100);
        return alphaAnimation;
    }
}
