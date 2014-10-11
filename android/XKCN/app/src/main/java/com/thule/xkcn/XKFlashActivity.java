package com.thule.xkcn;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.thule.xkcn.manager.XKDataManager;
import com.thule.xkcn.util.XKFontUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thule on 10/11/14.
 */
public class XKFlashActivity extends Activity
{
    ImageView _background;
    TextView _appName, _slogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xk_flash_activity_layout);
        _background = (ImageView) findViewById(R.id.blurImageView);
        _background.setScaleType(ImageView.ScaleType.CENTER_CROP);

        _appName = (TextView) findViewById(R.id.appName);
        XKFontUtils.setCustomFont(this, _appName, XKFontUtils.FontType.Candy);
        _slogan = (TextView) findViewById(R.id.subtileText);
        XKFontUtils.setCustomFont(this, _slogan, XKFontUtils.FontType.RobotoBold);
//        Bitmap bitmap = getBlurBitmap();
//
//        Bitmap blur = XKBlurUtils.blurBitmap(this, bitmap, 0.9f);
//        _background.setImageBitmap(blur);

        XKDataManager.shareInstance().loadNewPage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
    startMainActivity();
            }
        }, 2000);

        //Config image loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024) // 50
                        // Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    void startMainActivity()
    {
        Intent intent = new Intent(this, XKMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    Bitmap getBlurBitmap()
    {
        //String imagePath = "dauoidau.jpg";
        String imagePath = "drawable/background.png";
        AssetManager mngr = getAssets();

        // Create an input stream to read from the asset folder
        InputStream is=null;
        try {
            is = mngr.open(imagePath);
        } catch (IOException e1) {  e1.printStackTrace();}

        //Get the texture from the Android resource directory
        //InputStream is = context.getResources().openRawResource(R.drawable.radiocd5);
        Bitmap bitmap = null;
        try {
            //BitmapFactory is an Android graphics utility for images
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeStream(is, new Rect(0,0,0,0), options);
            bitmap = BitmapFactory.decodeStream(is);

        } finally {
            //Always clear and close
            try {
                is.close();
                is = null;
            } catch (IOException e) {
            }
        }
        return bitmap;
    }

    void saveBitmap (Bitmap bitmap)
    {
        try
        {
            String directory = Environment.getExternalStorageDirectory() + File.separator + "XKCN";
            File direct = new File(directory);
            if (!direct.exists())
            {
                if( direct.mkdir())
                {
                    File file = new File(directory + File.separator + "drawable/background.png");

                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                }
            }

            // save screenshot


        } catch (FileNotFoundException e)
        {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e)
        {
            Log.e("GREC", e.getMessage(), e);
        }
    }
}
