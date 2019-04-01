package com.bardxhong.boxscore.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bardxhong.boxscore.R;

public class LogoTransitionActivity extends AppCompatActivity {

    private final String TAG = LogoTransitionActivity.class.getSimpleName();

    private final String TRANSITION_NAME = "logo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logo_transition);
        setStatusBar(this);
        getWindow().setWindowAnimations(R.style.main_activity_animation_long_fade);

        startTransitionAnimation();


    }

    private void setStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * 利用Thread在Activity 顯示後延遲0.5秒後再開始intent程序以及動畫
     * 並在intent開始後再延遲0.5秒後配合fadeout再finish。
     */
    private void startTransitionAnimation() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(LogoTransitionActivity.this, BoxScoreActivity.class);
                        android.support.v4.util.Pair<View, String> pair = android.support.v4.util.Pair.create(findViewById(R.id.activity_logo_transition_imageview), TRANSITION_NAME);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(LogoTransitionActivity.this, pair);
                        startActivity(intent, optionsCompat.toBundle());
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });

            }
        }).start();
    }
}
