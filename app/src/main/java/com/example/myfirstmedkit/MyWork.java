package com.example.myfirstmedkit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;

import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.KitData;
import com.example.myfirstmedkit.Model.Medicament;
import com.example.myfirstmedkit.Validation.ValidationDate;

public class MyWork extends Worker {
    private Context context;
    private static final int NOTIFY_ID = 101;

    public MyWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        DBHelper db = new DBHelper(context);
        ArrayList<KitData> data = db.getAllKit();

        for(int i = 0; i < data.size(); i++){
            KitData kitData = data.get(i);
            ArrayList<Medicament> medicamentIsExpired = formingMedicamentsIsExpired(db, kitData.getId_kit());

            if(medicamentIsExpired.size() > 0){
                String title = context.getString(R.string.notification_is_expired_title);
                String mainText = getMainTextNotification(medicamentIsExpired, kitData);
                startNotification(i, title, mainText, kitData.getId_kit(), kitData.getName_kit());
            }
        }

        return Result.success();
    }

    private void startNotification(int idNotification, String title, String mainText, int idKit, String nameKit){
        createNotificationChannel();
        Intent intent = new Intent(context, MedicamentActivity.class);
        intent.putExtra("id_kit_put", idKit);
        intent.putExtra("name_kit_put", nameKit);
        PendingIntent clickNotification = PendingIntent.getActivity(context, idNotification, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(context, "TestWork")
                .setContentTitle(title)
                .setContentText(mainText)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(mainText))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(clickNotification)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(idNotification, notification);
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("TestWork", "Aboba",
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private ArrayList<Medicament> formingMedicamentsIsExpired(DBHelper db, int idKit){
        ArrayList<Medicament> medicaments = db.getAllMedicaments(idKit);
        ArrayList<Medicament> medicamentIsExpired = new ArrayList<>();
        for(int j = 0; j < medicaments.size(); j++){
            Medicament medicament = medicaments.get(j);
            if(ValidationDate.isExpired(medicament.getExpiration_date())){
                medicamentIsExpired.add(medicament);
            }
        }
        return medicamentIsExpired;
    }

    private String getMainTextNotification(ArrayList<Medicament> medicamentIsExpired, KitData kit){
        String mainText;
        if(medicamentIsExpired.size() == 1){
            String nameMedicament = medicamentIsExpired.get(0).getName_medicament();
            mainText = context.getString(R.string.notification_is_expired_one, kit.getName_kit() ,nameMedicament);
        }
        else{
            StringBuilder nameMedicaments = new StringBuilder();
            for(int j = 1; j <= medicamentIsExpired.size(); j++){
                if(j == medicamentIsExpired.size())
                    nameMedicaments.append(medicamentIsExpired.get(j - 1).getName_medicament());
                else
                    nameMedicaments.append(medicamentIsExpired.get(j - 1).getName_medicament()).append(", ");
            }
            mainText = context.getString(R.string.notification_is_expired_two, kit.getName_kit(), nameMedicaments);
        }
        return mainText;
    }
}
