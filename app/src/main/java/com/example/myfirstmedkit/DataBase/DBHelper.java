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
import com.example.myfirstmedkit.Model.NameMed;

public class DBHelper extends SQLiteOpenHelper {
    private final String nameTableKits = "KitsTest";
    private final String idKit = "id_kits";
    private final String nameKit = "name_kit";
    private final String idColor = "id_color";
    private final String nameTableNameMed = "Name_medTest";
    private final String idMed = "id_med";
    private final String nameMed = "name_med";
    private final String nameTableMedicaments = "MedicamentsTest";
    private final String id = "id_test";
    private final String expirationDate = "expiration_date";
    private final String count = "countMed";

    public DBHelper(@Nullable Context context) {
        super(context, "MyFirstMedKit.db", null, 14);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + nameTableKits + " ("
               + idKit + " INTEGER PRIMARY KEY AUTOINCREMENT, "
               + nameKit + " TEXT UNIQUE NOT NULL, "
               + idColor + " INTEGER NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + nameTableNameMed + " ("
                + idMed + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + nameMed + " TEXT UNIQUE NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + nameTableMedicaments + " ("
                + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + idKit + " INTEGER, "
                + idMed + " INTEGER, "
                + expirationDate + " TEXT NOT NULL, "
                + count + " INTEGER NOT NULL,"
                + idColor + " INTEGER NOT NULL, "
                + "FOREIGN KEY(id_kits) REFERENCES KitsTest(id_kits) ON DELETE CASCADE, "
                + "FOREIGN KEY(id_med) REFERENCES Name_medTest(id_med) ON DELETE CASCADE );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS KitsTest");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Name_medTest");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MedicamentsTest");
        onCreate(sqLiteDatabase);
    }

    public void addKit(KitData kitData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(nameKit, kitData.getName_kit());
        contentValues.put(idColor, kitData.getIdColor());
        db.insert(nameTableKits, null, contentValues);
        db.close();
    }

    private void addNameMed(NameMed med){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put(nameMed, med.getNameMed());
            db.insert(nameTableNameMed, null, content);
            db.close();
        } catch (Exception e) {
            System.out.println("Test");
            throw new RuntimeException(e);
        }
    }

    private ArrayList<NameMed> getAllNameMed(){
        ArrayList<NameMed> nameMeds = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlSelectKit = "SELECT * FROM " + nameTableNameMed;
        Cursor cursor = db.rawQuery(sqlSelectKit, null);
        if(cursor.moveToFirst()){
            do{
                NameMed nameMed = new NameMed(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1));
                nameMeds.add(nameMed);
            } while(cursor.moveToNext());
        }
        db.close();
        return nameMeds;
    }

    public boolean isEntryNameMed(String name){
        ArrayList<NameMed> nameMeds = getAllNameMed();
        for (NameMed nameMedItem:nameMeds) {
            if(nameMedItem.getNameMed().equals(name))
                return true;
        }
        return false;
    }

    public int getIdNameMed(String name){
        ArrayList<NameMed> nameMeds = getAllNameMed();
        for (NameMed nameMedItem:nameMeds) {
            if(nameMedItem.getNameMed().equals(name))
                return nameMedItem.getIdMed();
        }
        return -1;
    }

    private String getNameMedById(int idMedItem){
        ArrayList<NameMed> nameMeds = getAllNameMed();
        for (NameMed nameMedItem:nameMeds) {
            if(nameMedItem.getIdMed() == idMedItem)
                return nameMedItem.getNameMed();
        }
        return null;
    }

    public void addMedicament(Medicament medicament){
        SQLiteDatabase db;
        ContentValues content;
        if (!isEntryNameMed(medicament.getName_medicament())) {
            NameMed nameMed = new NameMed(medicament.getName_medicament());
            addNameMed(nameMed);
        }
        int id = getIdNameMed(medicament.getName_medicament());
        db = this.getWritableDatabase();
        content = new ContentValues();

        content.put(idMed, id);
        content.put(idKit, (Integer) medicament.getId_kit());
        content.put(expirationDate, (String) medicament.getExpiration_date());
        content.put(count, (Integer) medicament.getTablets_in_pack());
        content.put(idColor, (Integer) medicament.getIdColor());
        db.insert(nameTableMedicaments, null, content);
        db.close();
    }

    public ArrayList<KitData> getAllKit(){
        ArrayList<KitData> kitDataArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlSelectKit = "SELECT * FROM " + nameTableKits+";";
        Cursor cursor = db.rawQuery(sqlSelectKit, null);
        if(cursor.moveToFirst()){
            do{
                KitData kitData = new KitData(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)));
                kitDataArrayList.add(kitData);
            } while(cursor.moveToNext());
        }

        db.close();
        return kitDataArrayList;
    }

    public ArrayList<Medicament> getAllMedicaments(int id_kit){
        ArrayList<Medicament> medicamentArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlSelectKit = "SELECT * FROM " + nameTableMedicaments + " WHERE "  + idKit + "=" + id_kit+";";
        Cursor cursor = db.rawQuery(sqlSelectKit, null);
        if(cursor.moveToFirst()){
            do{
                Medicament medicament = new Medicament(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        getNameMedById(Integer.parseInt(cursor.getString(2))),
                        cursor.getString(3),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                medicamentArrayList.add(medicament);
            } while(cursor.moveToNext());
        }

        db.close();
        return medicamentArrayList;
    }

    private Medicament getMedicamentById(int idMedicament){
        Medicament medicament;
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlSelectKit = "SELECT * FROM " + nameTableMedicaments + " WHERE " + id + "=" + idMedicament+";";
        Cursor cursor = db.rawQuery(sqlSelectKit, null);
        medicament = new Medicament(
                Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)),
                getNameMedById(Integer.parseInt(cursor.getString(2))),
                cursor.getString(3),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)));
        return medicament;
    }

    public void deleteKit(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(nameTableKits, idKit + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteMedicament(int idMedicament){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(nameTableMedicaments, id + "=?", new String[]{String.valueOf(idMedicament)});
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
            content.put(idColor, updateKit.getIdColor());
            isEntry = true;
        }

        if(isEntry)
            db.update(nameTableKits, content, idKit + "=?", new String[]{String.valueOf(oldKit.getId_kit())});
        db.close();
    }

    public void updateMedicament(Medicament oldMedicament, Medicament updateMedicament){
        if(!oldMedicament.getName_medicament().equals(updateMedicament.getName_medicament())){
                deleteMedicament(oldMedicament.getId_medicament());
                addMedicament(updateMedicament);
                return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        boolean isEntry = false; // проверка на вхождение в if
        if(!oldMedicament.getExpiration_date().equals(updateMedicament.getExpiration_date())){
            content.put(expirationDate, updateMedicament.getExpiration_date());
            isEntry = true;
        }
        if(oldMedicament.getTablets_in_pack() != updateMedicament.getTablets_in_pack()){
            content.put(count, updateMedicament.getTablets_in_pack());
            isEntry = true;
        }
        if(oldMedicament.getIdColor() != updateMedicament.getIdColor()){
            content.put(idColor, updateMedicament.getIdColor());
            isEntry = true;
        }

        if(isEntry)
            db.update(nameTableMedicaments, content, id + "=?", new String[]{String.valueOf(oldMedicament.getId_medicament())});
        db.close();
    }

    public void updateTabletsInPack(int idMedicament, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(count, value);
        db.update(nameTableMedicaments, content, id + "=?", new String[]{String.valueOf(idMedicament)});
        db.close();
    }

    public void moveMedicament(int idKit, Medicament medicament){
        deleteMedicament(medicament.getId_medicament());
        medicament.setId_kit(idKit);
        addMedicament(medicament);
    }
}
