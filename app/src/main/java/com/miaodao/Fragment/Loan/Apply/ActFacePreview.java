package com.miaodao.Fragment.Loan.Apply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Config.SysSDCardCacheDir;

import java.io.File;

/**
 * Created by daixinglong on 2017/4/25.
 */

public class ActFacePreview extends Activity implements View.OnClickListener {

    private ImageView facePreview;
    private TextView confirm, reTake;
    private File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_video_face_preview);

        initParams();
        initActions();
    }


    /**
     *
     */
    private void initParams() {
        facePreview = (ImageView) findViewById(R.id.iv_face);
        confirm = (TextView) findViewById(R.id.face_confirm);
        reTake = (TextView) findViewById(R.id.tv_retake);
        file = new File(SysSDCardCacheDir.getVideoDir().getPath(), AppConfig.getInstance().VERIFICATION_ID_VIDEO_PHONE);

        Glide.with(this).load(file).
                skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.NONE).
                into(facePreview);
    }


    /**
     *
     */
    private void initActions() {
        confirm.setOnClickListener(this);
        reTake.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.face_confirm:
                finish();
                break;

            case R.id.tv_retake:
                goFaceCamera();
                break;
        }
    }

    /**
     * 返回拍照
     */
    private void goFaceCamera() {
        if (file.exists())
            file.delete();

        Intent intent = new Intent(this, ActFaceCame.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        goFaceCamera();
    }
}
