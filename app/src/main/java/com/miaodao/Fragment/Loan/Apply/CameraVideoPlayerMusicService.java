package com.miaodao.Fragment.Loan.Apply;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.miaodao.AIDL.MusicCallback;
import com.miaodao.Application.WheatFinanceApplication;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Utils.ToastUtils;

/**
 * 播放背景音效
 * Created by Home_Pc on 2017/3/16.
 */

public class CameraVideoPlayerMusicService extends Service {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isPlayering = false;
    private Handler mainHanlder;


    private MusicCallback.Stub musicCallback = new MusicCallback.Stub() {
        @Override
        public void setMusicPlayerType(boolean isPlayerType) throws RemoteException {
            if (isPlayerType) {
                startPlay();
//                isPlayering = true;
//                mediaPlayer.start();//开始播放
            } else {
                if (isPlayering) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    isPlayering = false;
                    stopPlay();
                }
            }
        }

        @Override
        public boolean getMusicPlayerType() throws RemoteException {
            return isPlayering;
        }
    };


    /**
     * 开始播放逻辑
     */
    private void startPlay() {

        mediaPlayer = MediaPlayer.create(WheatFinanceApplication.getInstance(), R.raw.record);//重新设置要播放的音频
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setLooping(false);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlayering = false;
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

        isPlayering = true;
        try {
//            mediaPlayer.prepare();
            mediaPlayer.start();//开始播放
        } catch (RuntimeException e) {
            e.printStackTrace();
            //提示用户
            playFail();
            //释放播放器
            stopPlay();

        }
    }


    /**
     * 出现异常提示用户
     */
    private void playFail() {
        mainHanlder.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.shortShow("播放失败");
            }
        });
    }


    /**
     * 停止播放
     */
    private void stopPlay() {

        //重置播放状态
        isPlayering = false;

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


    @Override
    public void onCreate() {
        super.onCreate();
        mainHanlder = new Handler(getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicCallback;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlay();
    }
}
