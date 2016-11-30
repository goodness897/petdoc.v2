package com.compet.petdoc.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import com.compet.petdoc.R;
import com.compet.petdoc.util.NetworkUtil;

import static com.compet.petdoc.R.id.imageView;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private ImageView animImageView;
    private AnimationDrawable anim;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkPermission(this);
        animImageView = (ImageView) findViewById(imageView);

        anim = (AnimationDrawable) animImageView.getDrawable();
        anim.setOneShot(true);
        anim.start();
        if (anim.isRunning()) {
            if(NetworkUtil.getConnectivityStatus(this) == 0) {
                moveMainActivity();
                Toast.makeText(this, "Wifi 혹은 데이터를 켜주세요", Toast.LENGTH_LONG).show();
            } else {
            }
        }
    }
    private void moveMainActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
