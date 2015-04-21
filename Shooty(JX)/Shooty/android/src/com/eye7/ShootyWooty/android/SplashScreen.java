package com.eye7.ShootyWooty.android;

/**
 * Created by anvitha on 17/04/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.io.InputStream;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH = 2000;
    MediaPlayer mpSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mpSplash = MediaPlayer.create(this,R.raw.splash_sound);
        mpSplash.setLooping(true);
        mpSplash.start();
        setContentView(R.layout.activity_splash);
        try {
            InputStream is = getAssets().open("HomeScreen/splash.png");
            Bitmap splashMap = BitmapFactory.decodeStream(is);
            ImageView imageView = (ImageView) findViewById(R.id.imgLogo);
            imageView.setImageBitmap(splashMap);
        }
        catch(Exception e){

        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, AndroidLauncher.class);
                startActivity(i);
                mpSplash.stop();
                finish();
            }
        }, SPLASH);
    }

}