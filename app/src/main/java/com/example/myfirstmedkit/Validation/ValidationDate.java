package com.example.myfirstmedkit.Validation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ValidationDate {
    public static boolean isOverMedicament(int count){
        return count <= 0;
    }

    public static boolean isExpired(String date){
        String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        if(Integer.parseInt(cutYear(date)) < Integer.parseInt(cutYear(currentDate))){
            return true;
        }
        else if( (Integer.parseInt(cutYear(date)) == Integer.parseInt(cutYear(currentDate))) &&
                (Integer.parseInt(cutMonth(date)) < Integer.parseInt(cutMonth(currentDate))) ){
            return true;
        }
        else if( ( (Integer.parseInt(cutYear(date)) == Integer.parseInt(cutYear(currentDate))) &&
                (Integer.parseInt(cutMonth(date)) >= Integer.parseInt(cutMonth(currentDate))) )&&
                (Integer.parseInt(cutDay(date)) <= Integer.parseInt(cutDay(currentDate))) ){
            return true;
        }
        else{
            return false;
        }
    }

    private static String cutDay(String date){
        return date.substring(0, 2);
    }
    private static String cutMonth(String date){
        return date.substring(3, 5);
    }

    private static String cutYear(String date){
        return date.substring(6, 10);
    }
}
