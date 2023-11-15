package com.example.myfirstmedkit.Validation;


import com.example.myfirstmedkit.R;

import java.util.ArrayList;
import java.util.List;

import com.example.myfirstmedkit.Model.KitData;

public class ValidationField {
    public static int isGood = -1;
    public ValidationField(){}
    public String collectOnDate(int day, int mouth, int year){
        String date = "";
        mouth += 1;
        if(day < 10){
            date += String.valueOf(0) + day + ".";
        }
        else{
            date += day + ".";
        }
        if(mouth < 10){
            date += String.valueOf(0) + mouth + ".";
        }
        else{
            date += mouth + ".";
        }
        date += year;
        return date;
    }

    public List<Integer> splitByDate(String date){
        ArrayList<Integer> dates = new ArrayList<>();
        dates.add(Integer.parseInt(date.substring(6)));
        // Для коректной записи в DatePicker
        dates.add(Integer.parseInt(date.substring(3, 5))-1);
        dates.add(Integer.parseInt(date.substring(0, 2)));
        return dates;
    }

    public static int FieldUpdateKit(KitData updateKit, ArrayList<KitData> kits){
        if(updateKit.getName_kit().equals(""))
            return R.string.warning_is_null;

        for (KitData kit:kits)
            if(kit.getName_kit().equals(updateKit.getName_kit())
                    && updateKit.getId_kit() != kit.getId_kit())
                return R.string.warning_coincidence;

        return isGood;
    }

    public static int FieldAddKit(String nameKit, ArrayList<KitData> kits){
        if(nameKit.equals(""))
            return R.string.warning_is_null;

        for (KitData kit:kits)
            if(kit.getName_kit().equals(nameKit))
                return R.string.warning_coincidence;

        return isGood;
    }

}
