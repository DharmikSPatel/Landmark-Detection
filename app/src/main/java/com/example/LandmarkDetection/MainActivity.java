package com.example.LandmarkDetection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int ACTIVITY_CODE = 1235;
    static final String TAG = "DHARMIK";

    ImageView myLogo;
    TextView title, tagLine;
    ArrayList<Landmark> landmarks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLogo = findViewById(R.id.id_logo);
        title = findViewById(R.id.id_title);
        tagLine = findViewById(R.id.id_tagline);

        landmarks.add(new Landmark("Big Ben"));
        landmarks.add(new Landmark("Machu Picchu"));
        landmarks.add(new Landmark("Christ the Redeemer"));

        AnimationSet logoAnim = new AnimationSet(true);
        TranslateAnimation anim1 = new TranslateAnimation(0, 0, -1450, 0);
        logoAnim.addAnimation(anim1);
        AlphaAnimation anim2 = new AlphaAnimation(0f, 1f);
        logoAnim.addAnimation(anim2);
        logoAnim.setDuration(1000);
        myLogo.startAnimation(logoAnim);

        AnimationSet textAnim = new AnimationSet(true);
        TranslateAnimation anim3 = new TranslateAnimation(0, 0, 1000, 0);
        textAnim.addAnimation(anim3);
        AlphaAnimation anim4 = new AlphaAnimation(0f, 1f);
        textAnim.addAnimation(anim4);
        textAnim.setDuration(1000);
        title.startAnimation(textAnim);
        tagLine.startAnimation(textAnim);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent lauchDashboard = new Intent(MainActivity.this, Dashboard.class);
                lauchDashboard.putExtra("list", landmarks);
                startActivity(lauchDashboard);
                finish();
            }
        }, 3000);
    }
}

