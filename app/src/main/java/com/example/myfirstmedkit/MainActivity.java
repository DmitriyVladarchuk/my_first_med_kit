package com.example.myfirstmedkit;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.myfirstmedkit.Adapters.RVKitAdapter;
import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.KitData;

public class MainActivity extends AppCompatActivity {

    private ArrayList<KitData> kitDataArrayList = new ArrayList<>();
    private RecyclerView rvKit;
    private RVKitAdapter rvKitAdapter;
    private int backgroundWorkTime;

    @SuppressLint("BatteryLife")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApp();
        startBackgroundWork();

        rvKit = findViewById(R.id.rv_kit);
        DBHelper dataBaseHelper = new DBHelper(this);
        kitDataArrayList = dataBaseHelper.getAllKit();

        rvKitAdapter = new RVKitAdapter(this, kitDataArrayList);
        rvKit.setLayoutManager(new LinearLayoutManager(this));
        rvKit.setAdapter(rvKitAdapter);

        // разрешение на на игнор энерго сбережения
        String packageName = this.getPackageName();
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + packageName));
            this.startActivity(intent);
        }
    }

    private void initApp(){
        SharedPreferences setting = getSharedPreferences("dark_mode", MODE_PRIVATE);
        int darkMode = setting.getInt("dark_mode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(darkMode);

        setting = getSharedPreferences("background_work", MODE_PRIVATE);
        backgroundWorkTime = setting.getInt("background_work", 12);

        setting = getSharedPreferences("is_update_work", MODE_PRIVATE);
        boolean isUpdateWork = setting.getBoolean("is_update_work", false);
    }

    private void startBackgroundWork(){
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest testWork =
                new PeriodicWorkRequest.Builder(MyWork.class, backgroundWorkTime, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .addTag("tag")
                        .build();

        SharedPreferences setting = getSharedPreferences("is_update_work", MODE_PRIVATE);
        boolean isUpdateWork = setting.getBoolean("is_update_work", false);
        if(isUpdateWork){
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("periodicNotification",
                    ExistingPeriodicWorkPolicy.REPLACE, testWork);
            // REPLACE - остановит текущию и запустит новую
            SharedPreferences.Editor prefEditor = setting.edit();
            prefEditor = setting.edit();
            prefEditor.putBoolean("is_update_work", false);
            prefEditor.apply();
        }
        else{
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("periodicNotification",
                    ExistingPeriodicWorkPolicy.KEEP, testWork);
            //KEEP - оставит в работе текущую работу, а новую проигнорирует
        }
    }

    public void openSetting(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}