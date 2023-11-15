package com.example.myfirstmedkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfirstmedkit.Validation.ValidationField;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.KitData;

import java.util.ArrayList;

public class EditKitActivity extends AppCompatActivity {

    private int colorImage = R.color.blue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kit);

        Bundle argument = getIntent().getExtras();
        int id_kit = (int) argument.get("id_kit_put");
        String nameKit = (String) argument.get("name_kit_put");
        int idColor = (int) argument.get("colorKit");
        KitData kit = new KitData(id_kit, nameKit, idColor);
        colorImage = idColor;
        changeColor();

        TextView info = findViewById(R.id.textInfoAddKit);
        info.setText(R.string.update_kit);
        TextInputEditText textInput = findViewById(R.id.textFieldKit);
        textInput.setText(nameKit);

        //chip Group
        ChipGroup chipGroup = findViewById(R.id.chipGroup);

        Chip blue1 = findViewById(R.id.colorBlue1);
        blue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.blue1;
                changeColor();
            }
        });
        Chip blue2 = findViewById(R.id.colorBlue2);
        blue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.blue2;
                changeColor();
            }
        });
        Chip purple = findViewById(R.id.colorPurple);
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.purple;
                changeColor();
            }
        });
        Chip red = findViewById(R.id.colorRed);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.red;
                changeColor();
            }
        });
        Chip crimson = findViewById(R.id.colorCrimson);
        crimson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorImage = R.color.crimson;
                changeColor();
            }
        });

        Button updateKit = findViewById(R.id.buttonAddKit);
        updateKit.setText(R.string.edit);
        updateKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText editText = findViewById(R.id.textFieldKit);
                String text = String.valueOf(editText.getText());
                DBHelper db = new DBHelper(getApplicationContext());
                ArrayList<KitData> kits = db.getAllKit();
                KitData updateKit = new KitData(id_kit, editText.getText().toString(), colorImage);

                int error = ValidationField.FieldUpdateKit(updateKit, kits);
                if(error == ValidationField.isGood){
                    //KitData updateKit = new KitData(id_kit, editText.getText().toString(), colorImage);
                    db.updateKit(kit, updateKit);
                    onBackPressed();
                }
                else{
                    editText.setError(getResources().getString(error));
                }
            }
        });
    }

    public void changeColor(){
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