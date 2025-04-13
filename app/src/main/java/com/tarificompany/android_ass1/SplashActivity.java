package com.tarificompany.android_ass1;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SplashActivity extends AppCompatActivity {

    private ImageView logoImageView;

    private TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        // Clear CartPrefs
//        SharedPreferences cartPrefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor cartEditor = cartPrefs.edit();
//        cartEditor.clear();
//        cartEditor.apply();
//
//        // Clear FavoritesPrefs
//        SharedPreferences favoritesPrefs = getSharedPreferences("FavoritesPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor favoritesEditor = favoritesPrefs.edit();
//        favoritesEditor.clear();
//        favoritesEditor.apply();
//
//        // Clear ItemManager (default SharedPreferences)
//        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor defaultEditor = defaultPrefs.edit();
//        defaultEditor.clear();
//        defaultEditor.apply();
//
//        Toast.makeText(this, "All preferences cleared", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setUpViews();
    }

    /**
     * setUpViews method that will initialise the hooks, and create the animation.
     */
    public void setUpViews() {
        logoImageView = findViewById(R.id.logoImageView);
        txtSlogan = findViewById(R.id.txtSlogan);

        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.text_fade);

        logoImageView.startAnimation(logoAnim);
        txtSlogan.startAnimation(textAnim);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}