package com.example.myfirstmedkit.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.example.myfirstmedkit.Model.KitData;
import com.example.myfirstmedkit.Model.Medicament;

public class OldDBHelper extends SQLiteOpenHelper {

    private final String nameTableKit = "Kits";
    private final String nameTableMedicament = "Medicaments";
    private final String idKit = "id_kit";
    private final String nameKit = "name_kit";
    private final String idMedicament = "id_medicament";
    private final String nameMedicament = "name_medicament";
    private final String expirationDate = "expiration_date";
    private final String tablets = "tablets_in_pack";

    public OldDBHelper(@Nullable Context context) {
        super(context, "MyFirstKit.db", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + nameTableKit + " ("
                + idKit +" INTEGER PRIMARY KEY AUTOINCREMENT, " + nameKit + " TEXT NOT NULL UNIQUE, " + "idColor INTEGER NOT NULL" + ");");
        sqLiteDatabase.execSQL("CREATE TABLE " + nameTableMedicament + "("
                + idKit + " INTEGER, " + idMedicament + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + nameMedicament + " TEXT NOT NULL, " + expirationDate + " TEXT NOT NULL, "
                + tablets + " INTEGER NOT NULL,"+ " idColor INTEGER NOT NULL,"+" FOREIGN KEY (id_kit) REFERENCES Kits(id_kit) ON DELETE CASCADE );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Kits");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Medicaments");
        //onCreate(sqLiteDatabase);
    }

    public void addKit(KitData kitData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(nameKit, kitData.getName_kit());
        contentValues.put("idColor", kitData.getIdColor());
        db.insert(nameTableKit, null, contentValues);
        db.close();
    }

    public void addMedicament(Medicament medicament){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_kit", medicament.getId_kit());
        //contentValues.put("id_medicament", medicament.getId_medicament());
        contentValues.put("name_medicament", medicament.getName_medicament());
        contentValues.put("expiration_date", medicament.getExpiration_date());
        contentValues.put("tablets_in_pack", medicament.getTablets_in_pack());
        contentValues.put("idColor", medicament.getIdColor());
        db.insert("Medicaments", null, contentValues);
        db.close();
    }

    public ArrayList<KitData> selectKitData(){
        ArrayList<KitData> kitDataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlSelectKit = "SELECT * FROM Kits";
        Cursor cursor = db.rawQuery(sqlSelectKit, null);
        if(cursor.moveToFirst()){
            do{
                KitData kitData = new KitData(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
                kitDataArrayList.add(kitData);
            } while(cursor.moveToNext());
        }

        db.close();
        return kitDataArrayList;
    }

    public ArrayList<Medicament> selectMedicament(int id_kit){
        ArrayList<Medicament> medicamentArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlSelectKit = "SELECT * FROM Medicaments WHERE id_kit = " + id_kit;
        Cursor cursor = db.rawQuery(sqlSelectKit, null);
        if(cursor.moveToFirst()){
            do{
                Medicament medicament = new Medicament(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                medicamentArrayList.add(medicament);
            } while(cursor.moveToNext());
        }

        db.close();
        return medicamentArrayList;
    }

    public void deleteKit(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(nameTableKit, idKit + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteMedicament(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(nameTableMedicament, idMedicament + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateKit(KitData oldKit, KitData updateKit){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        boolean isEntry = false; // проверка на вхождение в if
        if(!oldKit.getName_kit().equals(updateKit.getName_kit())){
            content.put(nameKit, updateKit.getName_kit());
            isEntry = true;
        }
        if(oldKit.getIdColor() != updateKit.getIdColor()){
            content.put("idColor", updateKit.getIdColor());
            isEntry = true;
        }

        if(isEntry)
            db.update(nameTableKit, content, idKit + "=?", new String[]{String.valueOf(oldKit.getId_kit())});
        db.close();
    }

    public void updateMedicament(Medicament oldMedicament, Medicament updateMedicament){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        boolean isEntry = false; // проверка на вхождение в if
        if(!oldMedicament.getName_medicament().equals(updateMedicament.getName_medicament())){
            content.put(nameMedicament, updateMedicament.getName_medicament());
            isEntry = true;
        }
        if(!oldMedicament.getExpiration_date().equals(updateMedicament.getExpiration_date())){
            content.put(expirationDate, updateMedicament.getExpiration_date());
            isEntry = true;
        }
        if(oldMedicament.getTablets_in_pack() != updateMedicament.getTablets_in_pack()){
            content.put(tablets, updateMedicament.getTablets_in_pack());
            isEntry = true;
        }
        if(oldMedicament.getIdColor() != updateMedicament.getIdColor()){
            content.put("idColor", updateMedicament.getIdColor());
            isEntry = true;
        }

        if(isEntry)
            db.update(nameTableMedicament, content, idMedicament + "=?", new String[]{String.valueOf(oldMedicament.getId_medicament())});
        db.close();
    }

    public void updateTabletsInPack(int id, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(tablets, value);
        db.update(nameTableMedicament, content, idMedicament + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void moveMedicament(int idKit, Medicament medicament){
        deleteMedicament(medicament.getId_medicament());
        medicament.setId_kit(idKit);
        addMedicament(medicament);
    }

}
