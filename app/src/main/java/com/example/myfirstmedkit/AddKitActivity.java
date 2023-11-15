package com.example.myfirstmedkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.KitData;
import com.example.myfirstmedkit.Validation.ValidationField;

public class AddKitActivity extends AppCompatActivity {

    ChipGroup chipGroup;
    Chip blue1;
    Chip blue2;
    Chip purple;
    Chip red;
    Chip crimson;

    private int colorImage = R.color.blue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kit);

        //chip Group
        chipGroup = findViewById(R.id.chipGroup);
        blue1 = findViewById(R.id.colorBlue1);
        blue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.blue1;
                changeColor();
            }
        });
        blue2 = findViewById(R.id.colorBlue2);
        blue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.blue2;
                changeColor();
            }
        });
        purple = findViewById(R.id.colorPurple);
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.purple;
                changeColor();
            }
        });
        red = findViewById(R.id.colorRed);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.red;
                changeColor();
            }
        });
        crimson = findViewById(R.id.colorCrimson);
        crimson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.crimson;
                changeColor();
            }
        });

    }

    public void addKit(View view){
        TextInputEditText nameKit = findViewById(R.id.textFieldKit);
        String text = String.valueOf(nameKit.getText());

        DBHelper db = new DBHelper(this);
        ArrayList<KitData> kits = db.getAllKit();

        int error = ValidationField.FieldAddKit(text, kits);
        if(error == ValidationField.isGood){
            KitData kitData = new KitData(text, colorImage);
            db.addKit(kitData);

            onBackPressed();
        }
        else{
            nameKit.setError(getResources().getString(error));
        }
    }

    private void changeColor(){
        ImageView imageView = findViewById(R.id.imageKit);
        imageView.setColorFilter(getResources().getColor(colorImage));

        Button button = findViewById(R.id.buttonAddKit);
        button.setBackgroundColor(getResources().getColor(colorImage));

        TextInputLayout textInputLayout = findViewById(R.id.boxTextInputKit);
        textInputLayout.setBoxStrokeColor(getResources().getColor(colorImage));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}