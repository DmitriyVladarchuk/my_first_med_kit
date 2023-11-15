package com.example.myfirstmedkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.Medicament;
import com.example.myfirstmedkit.Validation.ValidationField;

public class AddMedicamentActivity extends AppCompatActivity {
    TextInputEditText nameMedicament;
    TextInputEditText countMedicament;
    DatePicker expirationDate;
    ChipGroup chipGroup;
    Chip blue1;
    Chip blue2;
    Chip purple;
    Chip red;
    Chip crimson;

    private int id_kit;
    private String nameKit;
    private int colorImage = R.color.blue1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicament);

        nameMedicament = findViewById(R.id.textFieldMedicament);
        countMedicament = findViewById(R.id.countMedicament);
        expirationDate = findViewById(R.id.datePicker);

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


        Bundle argument = getIntent().getExtras();
        id_kit = (int) argument.get("id_kit_put");
        nameKit = (String) argument.get("name_kit_put");

    }

    public void addMedicament(View view){
        ValidationField validation = new ValidationField();
        TextInputEditText nameMedicament = findViewById(R.id.textFieldMedicament);
        String text = String.valueOf(nameMedicament.getText());
        if(!text.equals("")){
            String count = String.valueOf(countMedicament.getText());
            String date = validation.collectOnDate(expirationDate.getDayOfMonth(), expirationDate.getMonth(), expirationDate.getYear());
            Medicament medicament = new Medicament(id_kit, String.valueOf(nameMedicament.getText()), date, Integer.parseInt(count), colorImage);
            DBHelper db = new DBHelper(this);
            db.addMedicament(medicament);

            Intent intent = new Intent(this, MedicamentActivity.class);
            intent.putExtra("id_kit_put", id_kit);
            intent.putExtra("name_kit_put", nameKit);
            this.startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else{
            nameMedicament.setError("Ошибка Валидации");
        }
    }

    public void changeColor(){
        ImageView imageView = findViewById(R.id.imageTablet);
        imageView.setColorFilter(getResources().getColor(colorImage));

        Button button = findViewById(R.id.buttonAddMedicament);
        button.setBackgroundColor(getResources().getColor(colorImage));

        TextInputLayout textInputLayout = findViewById(R.id.boxTextInputName);
        textInputLayout.setBoxStrokeColor(getResources().getColor(colorImage));

        TextInputLayout textInput = findViewById(R.id.boxTextInputCount);
        textInput.setBoxStrokeColor(getResources().getColor(colorImage));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MedicamentActivity.class);
        intent.putExtra("id_kit_put", id_kit);
        intent.putExtra("name_kit_put", nameKit);
        this.startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}