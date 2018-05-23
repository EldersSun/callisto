package com.miaodao.Fragment.Loan.Apply;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fcloud.licai.miaodao.R;
import com.miaodao.Sys.Config.AppConfig;
import com.miaodao.Sys.Config.SysSDCardCacheDir;
import com.miaodao.Sys.Utils.AdrToolkit;
import com.miaodao.Sys.Utils.ImgUtils;

import java.io.File;

import static com.miaodao.Fragment.Loan.Apply.ActIdCamera.ID_BACK;
import static com.miaodao.Fragment.Loan.Apply.ActIdCamera.ID_FRONT;

/**
 * Created by daixinglong on 2017/4/21.
 */

public class ActIdPhoto extends Activity implements View.OnClickListener {

    private ImageView ivPreview;//显示拍摄的照片
    private ImageView ivIdTip;//身份证提示图
    private TextView controlOk;//确定按钮
    private TextView controlRetake;//重拍
    private TextView tvTip;//提示信息

    private String idTag = "";
    private Intent intent;
    private String savePath = "";//用于保存图片的路径
    private File file;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fm_id_camera_preview);

        initParams();
        initActions();
    }


    /**
     * 初始化参数
     */
    private void initParams() {

        ivPreview = (ImageView) findViewById(R.id.iv_preview);
        ivIdTip = (ImageView) findViewById(R.id.iv_id_tip);
        controlOk = (TextView) findViewById(R.id.control_ok);
        controlRetake = (TextView) findViewById(R.id.control_retake);
        tvTip = (TextView) findViewById(R.id.tv_tip);

        intent = getIntent();
        if (intent == null) return;
        savePath = intent.getStringExtra("savePath");
        idTag = intent.getStringExtra(ActIdCamera.ID_TAG);
        if (TextUtils.isEmpty(savePath) || TextUtils.isEmpty(idTag)) return;
        showPhoto();

    }


    private void initActions() {
        controlOk.setOnClickListener(this);
        controlRetake.setOnClickListener(this);
    }


    /**
     * 展示照片
     */
    private void showPhoto() {
        file = new File(SysSDCardCacheDir.getImgDir().getPath(), savePath);
        Glide.with(this).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivPreview);

        switch (idTag) {

            case ID_FRONT:
                tvTip.setText(Html.fromHtml("请确保<font color='#3196FE'>身份证人像面</font>信息清晰、无遮挡"));
                ivIdTip.setImageResource(R.drawable.ic_tips_id_front);
                break;

            case ID_BACK:
                tvTip.setText(Html.fromHtml("请确保<font color='#3196FE'>身份国徽面</font>信息清晰、无遮挡"));
                ivIdTip.setImageResource(R.drawable.ic_tips_id_back);
                break;

            default:
                break;

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.control_ok:

                // TODO: 2017/4/25 点击确定按钮后，如果身份证国徽照片不存在，就直接到拍摄国徽

                cropPhoto();

                if (ID_FRONT.equals(idTag)) {
                    File file = new File(SysSDCardCacheDir.getImgDir().getPath(), AppConfig.getInstance().VERIFICATION_ID_BACK_PHONE);
                    if (!file.exists()) {
                        Intent intent = new Intent(this, ActIdCamera.class);
                        intent.putExtra(ActIdCamera.ID_TAG, ID_BACK);
                        startActivity(intent);
                    }
                }
                finish();
                break;

            case R.id.control_retake:
                file.delete();
                Intent intent = new Intent(this, ActIdCamera.class);
                intent.putExtra(ActIdCamera.ID_TAG, idTag);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }

    }


    /**
     * 裁剪图片
     */
    private void cropPhoto() {

        String path = "";
        String filePath = "";
        File file;
        if (ID_FRONT.equals(idTag)) {
            path = AppConfig.getInstance().VERIFICATION_ID_FRONT_PHONE_PATH;
            filePath = AppConfig.getInstance().VERIFICATION_ID_FRONT_PHONE;
        } else {
            path = AppConfig.getInstance().VERIFICATION_ID_BACK_PHONE_PATH;
            filePath = AppConfig.getInstance().VERIFICATION_ID_BACK_PHONE;
        }
        Bitmap originalBitmap = BitmapFactory.decodeFile(path);
        Bitmap scaleBitmap = ImgUtils.centerSquareScaleBitmap(originalBitmap, (AdrToolkit.getScreenWidth() - AdrToolkit.dip2px(300)), (AdrToolkit.getScreenHeight() - AdrToolkit.dip2px(200)));
        file = new File(SysSDCardCacheDir.getImgDir().getPath(), filePath);
        if (file.exists()) {
            file.delete();
        }
        ImgUtils.saveBitmap(scaleBitmap, file);
    }
}
