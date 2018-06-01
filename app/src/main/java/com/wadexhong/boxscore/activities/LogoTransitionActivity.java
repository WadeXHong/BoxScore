package com.wadexhong.boxscore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import com.wadexhong.boxscore.R;

public class LogoTransitionActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("destroy","destroy");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_transition);
        setStatusBar(this);

//                ChangeBounds bounds = new ChangeBounds();
//        bounds.setDuration(10000);
//        getWindow().setSharedElementExitTransition(bounds);

        getWindow().setWindowAnimations(R.style.main_activity_animation_long_fade);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(500);
                }catch (InterruptedException mE){
                    mE.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(LogoTransitionActivity.this, BoxScoreActivity.class);
//                        android.support.v4.util.Pair<View, String> pair1 = android.support.v4.util.Pair.create(findViewById(R.id.activity_logo_transition_layout), "layout");
                        android.support.v4.util.Pair<View, String> pair2 = android.support.v4.util.Pair.create(findViewById(R.id.activity_logo_transition_imageview), "logo");
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(LogoTransitionActivity.this, pair2);
                        startActivity(intent, optionsCompat.toBundle());
                    }
                });
                try{
                    Thread.sleep(500);
                }catch (InterruptedException mE){
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

    private void setStatusBar(Activity activity) {

        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
