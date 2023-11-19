package com.example.myfirstmedkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch darkMode;
    private RadioGroup backgroundWork;
    private RadioButton radioBut1;
    private RadioButton radioBut2;
    private RadioButton radioBut3;
    private RadioButton radioBut4;
    private int backgroundWorkTime1 = 12;
    private int backgroundWorkTime2 = 24;
    private int backgroundWorkTime3 = 48;
    private int backgroundWorkTime4 = 168;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        initSetting();
        updateValueBackgroundWork();

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences setting = getSharedPreferences("dark_mode", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = setting.edit();
                if(isChecked){
                    prefEditor.putInt("dark_mode", AppCompatDelegate.MODE_NIGHT_YES);
                    prefEditor.apply();

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    prefEditor.putInt("dark_mode", AppCompatDelegate.MODE_NIGHT_NO);
                    prefEditor.apply();

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

    }

    private void initView(){
        darkMode = findViewById(R.id.switchDarkMode);
        backgroundWork = findViewById(R.id.background_work_group);
        radioBut1 = findViewById(R.id.rb_but_1);
        radioBut2 = findViewById(R.id.rb_but_2);
        radioBut3 = findViewById(R.id.rb_but_3);
        radioBut4 = findViewById(R.id.rb_but_4);
    }

    private void initSetting(){
        SharedPreferences setting = getSharedPreferences("dark_mode", MODE_PRIVATE);
        int mode = setting.getInt("dark_mode", AppCompatDelegate.MODE_NIGHT_NO);
        if(mode != AppCompatDelegate.MODE_NIGHT_NO)
            darkMode.setChecked(true);

        setting = getSharedPreferences("background_work", MODE_PRIVATE);
        int valueBackgroundWorkTime = setting.getInt("background_work", backgroundWorkTime1);
        if(valueBackgroundWorkTime == backgroundWorkTime1)
            radioBut1.setChecked(true);
        if(valueBackgroundWorkTime == backgroundWorkTime2)
            radioBut2.setChecked(true);
        if(valueBackgroundWorkTime == backgroundWorkTime3)
            radioBut3.setChecked(true);
        if(valueBackgroundWorkTime == backgroundWorkTime4)
            radioBut4.setChecked(true);

    }

    private void updateValueBackgroundWork(){
        radioBut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences setting = getSharedPreferences("background_work", MODE_PRIVATE);
                updateSettingBackground_Work(backgroundWorkTime1);
            }
        });

        radioBut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettingBackground_Work(backgroundWorkTime2);
            }
        });

        radioBut3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettingBackground_Work(backgroundWorkTime3);
            }
        });

        radioBut4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettingBackground_Work(backgroundWorkTime4);
            }
        });
    }

    private void updateSettingBackground_Work(int value){
        SharedPreferences setting = getSharedPreferences("background_work", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = setting.edit();
        prefEditor.putInt("background_work", value);
        prefEditor.apply();

        setting = getSharedPreferences("is_update_work", MODE_PRIVATE);
        prefEditor = setting.edit();
        prefEditor.putBoolean("is_update_work", true);
        prefEditor.apply();
    }

    public void backSetting(View view) {
        onBackPressed();
    }

    public void openNotification(View view) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // для Android 5-7
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);
        // для Android 8-13
        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        startActivity(intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}