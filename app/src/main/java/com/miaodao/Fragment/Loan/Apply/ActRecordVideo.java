package com.miaodao.Fragment.Loan.Apply;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Utils.ToastUtils;
import com.miaodao.Sys.Widgets.CustomDialog;
import com.miaodao.Sys.Widgets.DialogHelp;
import com.miaodao.Sys.Widgets.PlayVideoView;
import com.miaodao.Sys.Widgets.camera.CameraPreview;
import com.miaodao.Utils.VideoTvShowAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by daixinglong on 2017/4/25.
 */

public class ActRecordVideo extends Activity implements View.OnClickListener {

    private final int CAN_START_RECORD = 0x000;//可以开始录音了
    private final int START_ANIMATION = 0x001;//开始动画
    private final int START_PLAY_VIDEO = 0x002;//开始播放视频
    private final int FINISH_RECORD = 0x003;//完成朗读

    private TextView videoTime;//开始录音倒计时

    //顶部录像的朗读文字部分
    private LinearLayout llReadingTip;
    private TextView tvReadingTip;//提示语
    private Button btnStartRecord;//开始录音按钮
    private GridView gvReadingText;//朗读的文字内容

    //顶部录制成功后的布局
    private LinearLayout llVideoSucc;
    private TextView tvRetakeVideo;//点击重新开始录制
    private TextView tvConfirmVideo;//点击确认该录音

    //中间布局
    private SurfaceView sfRecord;//录像的surfaceView
    private PlayVideoView playRecord;
    private ImageView ivVideoMask;//遮罩层
    private TextView tvVideoCancel;//取消录制按钮
    private LinearLayout llRecordTip;//录像的时候提示文字
    private TextView tvKeepFaceCenterTip;//将脸放于中间提示语
    private LinearLayout llRelay;//点击重新播放

    //朗读文字
    private TvShowTimer tvShowTimer;
    private VideoTvShowAdapter readingTextAdapter;
    private int colorPosition;

    private RecordHandler recordHandler;
    private AlphaAnimation alphaAnimation;//开始朗读的按钮动画

    //播放录音
    private AudioManager audioMgr;//音量控制器
    private int curVolume;//音量
    private ExecutorService executorServicePlayMusic;//用来播放音乐线程
    private MediaPlayer mediaPlayer;//播放音量媒体
    private boolean isPlaying;//标识录音是否在录制

    private boolean isRecording;//是否在录像
    private SurfaceHolder recordHolder;//录像的surfaceViewHolder
    private Camera recordCamera;//配合录像
    private MediaRecorder mediaRecorder;//录像

    private CameraPreview preview;
//    /**
//     * 录像的callback
//     */
//    private class RecordCallback implements SurfaceHolder.Callback {
//
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            if (!isRecording) {
//                //初始化camera
//                initCamera();
//            }
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//
//        }
//    }


    private class RecordHandler extends Handler {

        WeakReference<Activity> activityWeakReference;

        public RecordHandler(ActRecordVideo actRecordVideo) {
            activityWeakReference = new WeakReference<Activity>(actRecordVideo);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ActRecordVideo act = (ActRecordVideo) activityWeakReference.get();
            if (act == null) return;

            switch (msg.what) {

                case CAN_START_RECORD:
                    act.btnStartRecord.setEnabled(true);
                    ToastUtils.showWithTime("点击屏幕又上方按钮,开始朗读", Toast.LENGTH_LONG);
                    act.btnStartRecord.setBackgroundResource(R.drawable.btn_shape_backgroup_color);
                    break;

                case START_ANIMATION:
                    act.recordBtnEnable();
                    break;

                case START_PLAY_VIDEO:
//                    releaseCamera();
                    startPlayVideo();
                    break;

                case FINISH_RECORD:
                    act.recordBtnFinish();
//                    btnStartRecord.setText("完成朗读");
//                    btnStartRecord.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }


        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_record_video);


        initParams();
        initActions();
    }

    /**
     *
     */
    private void initParams() {
        videoTime = (TextView) findViewById(R.id.video_time);

        llReadingTip = (LinearLayout) findViewById(R.id.ll_reading_tip);
        tvReadingTip = (TextView) findViewById(R.id.tv_reading_tip);
        btnStartRecord = (Button) findViewById(R.id.btn_start_record);
        gvReadingText = (GridView) findViewById(R.id.gv_reading_text);

        llVideoSucc = (LinearLayout) findViewById(R.id.ll_video_succ);
        tvRetakeVideo = (TextView) findViewById(R.id.tv_retake_video);
        tvConfirmVideo = (TextView) findViewById(R.id.tv_confirm_video);

        sfRecord = (SurfaceView) findViewById(R.id.sf_record);
        playRecord = (PlayVideoView) findViewById(R.id.play_record);
        ivVideoMask = (ImageView) findViewById(R.id.iv_video_mask);
        tvVideoCancel = (TextView) findViewById(R.id.tv_video_cancel);
        llRecordTip = (LinearLayout) findViewById(R.id.ll_record_tip);
        tvKeepFaceCenterTip = (TextView) findViewById(R.id.tv_keep_face_center_tip);
        llRelay = (LinearLayout) findViewById(R.id.ll_relay);

        recordHandler = new RecordHandler(this);

//        preview = new CameraPreview(this, sfRecord);
        readingTextAdapter = new VideoTvShowAdapter(this);
        gvReadingText.setAdapter(readingTextAdapter);
        initReadingText();
//        initPlayMusic();

        new Thread(new Runnable() {
            @Override
            public void run() {
                preview = new CameraPreview(ActRecordVideo.this, sfRecord);
                initPlayMusic();
            }
        }).start();


        recordHolder = sfRecord.getHolder();
        recordHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //保持屏幕的高亮
        sfRecord.setKeepScreenOn(true);
//        recordHolder.addCallback(new RecordCallback());
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0x001);
            return;
        }

        openCamera();

    }


    /**
     *
     */
    private void initActions() {
        btnStartRecord.setOnClickListener(this);
        tvVideoCancel.setOnClickListener(this);
        llRelay.setOnClickListener(this);
        tvRetakeVideo.setOnClickListener(this);
        tvConfirmVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_start_record:
                if (btnStartRecord.getText().toString().equals("完成朗读")) {
//                    releaseCamera();
                    startPlayVideo();
                } else {
                    startRecordCountDown();
                }
//                recordHandler.removeCallbacksAndMessages(null);
                break;

            case R.id.tv_video_cancel:

                if ((tvVideoCancel.getText().toString().equals("取消") && isRecording)
                        || tvVideoCancel.getText().toString().equals("返回")) {
                    showDialog();
                } else {
                    File file = new File(AppConfig.getInstance().VERIFICATION_ID_VIDEO_PATH);
                    if (file.exists()) {
                        file.delete();
                    }
                    finish();
                }


                break;

            case R.id.ll_relay:
                llVideoSucc.setVisibility(View.GONE);
                llReadingTip.setVisibility(View.VISIBLE);
                tvVideoCancel.setVisibility(View.GONE);
                tvVideoCancel.setText("取消");
                llRelay.setVisibility(View.GONE);
                playRecord.setVideoPath(AppConfig.getInstance().VERIFICATION_ID_VIDEO_PATH);
                playRecord.start();
                break;

            case R.id.tv_retake_video:
                finish();
                break;

            case R.id.tv_confirm_video:
                finish();
                break;

            default:
                break;

        }
    }


    /**
     * 显示是否保存视频文件
     */
    private void showDialog() {

        DialogHelp.getInstance(this).showDialog("温馨提示", "是否保存视频", "不保存", "保存", new CustomDialog.OnSureInterface() {
            @Override
            public void getOnSure() {
                finish();
            }

            @Override
            public void getOnDesmiss() {
                File file = new File(AppConfig.getInstance().VERIFICATION_ID_VIDEO_PATH);
                if (file.exists()) {
                    file.delete();
                }
                finish();
            }
        });


    }


    /**
     * 朗读文字初始化
     */
    private void initReadingText() {
        if (tvShowTimer == null) {
            tvShowTimer = new TvShowTimer(1000 * 18, 250);
        }
        colorPosition = 0;
        tvShowTimer.start();
    }


    /**
     * 开始播放录音
     */
    private void initPlayMusic() {

        audioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 初始化音量大概为最大音量的1/2
        curVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume,
                AudioManager.FLAG_PLAY_SOUND);

        //使用单线程是因为录音JIN函数不具备线程安全性
        executorServicePlayMusic = Executors.newSingleThreadExecutor();
        //创建播放录音媒体
        mediaPlayer = MediaPlayer.create(this, R.raw.record);

        playAudioFile();
    }


    /**
     * 播放录音
     */
    private void playAudioFile() {

        //检查播放状态，防止多次播放
        if (!isPlaying) {
            isPlaying = true;
            SystemClock.sleep(1000);
            executorServicePlayMusic.submit(new Runnable() {
                @Override
                public void run() {
                    doPlay();
                }
            });
        }
    }


    /**
     * 播放录音
     */
    private void doPlay() {

        try {
            //设置监听回调
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放结束，释放播放器
                    stopPlay();
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    //提示用户
                    playFail();

                    //释放播放器
                    stopPlay();

                    //错误已经处理，返回true
                    return true;
                }
            });

            //配置音量，是否循环
            mediaPlayer.setVolume(1, 1);
            mediaPlayer.setLooping(false);

            //准备，开始播放
//            mediaPlayer.prepare();
            mediaPlayer.start();

            //异常处理，防止闪退
        } catch (RuntimeException e) {

            e.printStackTrace();

            //提示用户
            playFail();

            //释放播放器
            stopPlay();

        }
    }

    /**
     * 播放失败，提示用户
     */
    private void playFail() {

        recordHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.shortShow("播放失败");
            }
        });

    }

    /**
     * 开始朗读按钮可以使用
     */
    public void recordBtnEnable() {
        btnStartRecord.setEnabled(true);
        //开始动画提示用户可以开始录像了
        startAlphaAnimation();
    }

    public void recordBtnFinish(){
        btnStartRecord.setText("完成朗读");
        btnStartRecord.setVisibility(View.VISIBLE);
    }

    /**
     * 开始动画
     */
    private void startAlphaAnimation() {

        if (btnStartRecord.getVisibility() == View.GONE)
            return;
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(300);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(false);
        //设置动画结束之后的状态是否是动画开始时的状态，true，表示是保持动画开始时的状态
        alphaAnimation.setFillBefore(true);
        //设置动画的重复模式：反转REVERSE和重新开始RESTART
        alphaAnimation.setRepeatMode(AlphaAnimation.REVERSE);
        //设置动画播放次数
        alphaAnimation.setRepeatCount(3);
        //开始动画
        btnStartRecord.startAnimation(alphaAnimation);
    }


    /**
     * 显示开始朗读倒计时3秒
     * 倒计时结束的时候，开始文字变色
     */
    private void startRecordCountDown() {
        readingTextAdapter.setColorPosition(-1);
        isRecording = true;
        videoTime.setVisibility(View.VISIBLE);
//        btnStartRecord.setVisibility(View.GONE);
        RecordTimer recordTimer = new RecordTimer(4000, 1000);
        recordTimer.start();
    }


    /**
     * 更新字幕
     */
    private class TvShowTimer extends CountDownTimer {


        public TvShowTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            readingTextAdapter.setColorPosition(++colorPosition);

            if (isRecording) return;

            //动画提示用户点击
            if (millisUntilFinished > 3000 && millisUntilFinished < 3250) {
                recordHandler.sendEmptyMessage(START_ANIMATION);
            }

            //按钮可以点击
            if (millisUntilFinished > 7000 && millisUntilFinished < 7250) {
                recordHandler.sendEmptyMessage(CAN_START_RECORD);
            }
        }

        @Override
        public void onFinish() {
        }
    }

    /**
     * 3秒倒计时开始录像
     */
    private class RecordTimer extends CountDownTimer {

        public RecordTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            videoTime.setText(millisUntilFinished / 1000 + "");
        }

        @Override
        public void onFinish() {
            videoTime.setVisibility(View.GONE);
            initReadingText();
            tvKeepFaceCenterTip.setVisibility(View.VISIBLE);
            startRecord();
        }
    }


//    /**
//     * 初始化相机
//     */
//    private void initCamera() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0x001);
//            return;
//        }
//
//        openCamera();
//    }


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
     * 打开相机
     */
    private void openCamera() {
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                recordCamera = Camera.open(1);
                recordCamera.startPreview();
                preview.setCamera(recordCamera);
                preview.reAutoFocus();
            } catch (RuntimeException ex) {
            }
        }
    }

    /**
     * 开始录制视频
     */
    private void startRecord() {

        recordCamera.unlock();
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.reset();
        mediaRecorder.setCamera(recordCamera);

        //录制的视频的角度，要自行旋转，否则与预览角度不同
//        mediaRecorder.setOrientationHint(90);
//        if (1 == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            mediaRecorder.setOrientationHint(270);
//        }

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        // 设置录像的质量（分辨率，帧数）
        CamcorderProfile mCamcorderProfile = CamcorderProfile.get(1, CamcorderProfile.QUALITY_480P);
        mediaRecorder.setProfile(mCamcorderProfile);
        mediaRecorder.setMaxDuration(20 * 1000);
//        // 设置视频文件输出的路径
        mediaRecorder.setOutputFile(AppConfig.getInstance().VERIFICATION_ID_VIDEO_PATH);
        // 设置捕获视频图像的预览界面
        mediaRecorder.setPreviewDisplay(recordHolder.getSurface());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            recordHandler.sendEmptyMessageDelayed(START_PLAY_VIDEO, 20 * 1000);
            recordHandler.sendEmptyMessageDelayed(FINISH_RECORD, 8 * 1000);
        } catch (RuntimeException | IOException e) {
            isRecording = false;
            e.printStackTrace();
            recordCamera.lock();
        }
    }


    /**
     * 开始播放视频
     */
    private void startPlayVideo() {

        playShow();
        //播放器如果在播放，不能不能播放
        if (playRecord.isPlaying()) {
            return;
        }
        playRecord.setVideoPath(AppConfig.getInstance().VERIFICATION_ID_VIDEO_PATH);
        playRecord.start();
        playRecord.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                llVideoSucc.setVisibility(View.VISIBLE);
                llReadingTip.setVisibility(View.GONE);
                tvVideoCancel.setVisibility(View.VISIBLE);
                tvVideoCancel.setText("返回");
                llRelay.setVisibility(View.VISIBLE);
            }
        });

        playRecord.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //提示用户
                playFail();

                //错误已经处理，返回true
                return true;
            }
        });
    }


    /**
     * 播放的时候显示的界面
     */
    private void playShow() {
        playRecord.setVisibility(View.VISIBLE);
        sfRecord.setVisibility(View.GONE);
        ivVideoMask.setVisibility(View.GONE);
        tvKeepFaceCenterTip.setVisibility(View.GONE);
        tvReadingTip.setText("正在回放朗读内容,请检查确认...");
        tvVideoCancel.setVisibility(View.GONE);
//        btnStartRecord.setVisibility(View.GONE);
    }

    /**
     * 录音的时候显示的界面
     */
    private void recordShow() {
        playRecord.setVisibility(View.GONE);
        sfRecord.setVisibility(View.VISIBLE);
        ivVideoMask.setVisibility(View.VISIBLE);
        tvKeepFaceCenterTip.setVisibility(View.VISIBLE);
        tvReadingTip.setText(R.string.Viedo_message_5);
        tvVideoCancel.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        clearAnimation();
        cancelTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorServicePlayMusic.shutdownNow();
        stopPlay();
//        releaseCamera();
    }

    /**
     * 清除动画
     */
    private void clearAnimation() {
        btnStartRecord.clearAnimation();
        if (alphaAnimation != null) {
            alphaAnimation.cancel();
            alphaAnimation = null;
        }
    }


    /**
     * 释放timer
     */
    private void cancelTimer() {
        if (tvShowTimer != null) {
            tvShowTimer.cancel();
            tvShowTimer = null;
        }
    }

    /**
     * 停止播放录音
     */
    private void stopPlay() {
        //重置播放状态
        isPlaying = false;

        //释放播放器
        if (mediaPlayer != null) {
            //重置监听器，防止内存泄露
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);

            //释放播放器
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }


//    /**
//     * 释放camera
//     */
//    private void releaseCamera() {
//
//        if (mediaRecorder != null) {
//            if (isRecording) {
//                mediaRecorder.stop();
//                mediaRecorder.reset();
//                mediaRecorder.release();
//                mediaRecorder = null;
//                isRecording = false;
//            }
//        }
//
//        if (recordCamera != null) {
//            //重新设置预览，否则会出现连续拍照崩溃的问题
//            recordCamera.stopPreview();//停掉原来摄像头的预览
//            recordCamera.release();//释放资源
//            recordCamera = null;
//        }
//
//
//    }


}
