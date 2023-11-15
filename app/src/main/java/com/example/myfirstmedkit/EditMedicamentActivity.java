package com.example.myfirstmedkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.Medicament;
import com.example.myfirstmedkit.Validation.ValidationField;

public class EditMedicamentActivity extends AppCompatActivity {

    private int colorImage;
    private Medicament medicament;
    private String nameKit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicament);

        ValidationField validation = new ValidationField();
        TextView textInfo = findViewById(R.id.textMedicamentInfo);
        textInfo.setText(R.string.edit_medicament);

        Bundle argument = getIntent().getExtras();
        int idKit = (int) argument.get("idKit");
        nameKit = (String) argument.get("nameKit");
        int idMedicament = (int) argument.get("idMedicament");
        String nameMedicament = (String) argument.get("nameMedicament");
        String expirationDate = (String) argument.get("expirationDate");
        int countMedicament = (int) argument.get("countMedicament");
        int idColor = (int) argument.get("idColor");
        medicament = new Medicament(idMedicament, idKit, nameMedicament, expirationDate, countMedicament, idColor);

        colorImage = idColor;
        changeColor();

        // Chip Group
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
        // Set Input Name Medicament
        TextInputEditText textInputEditText = findViewById(R.id.textFieldMedicament);
        textInputEditText.setText(nameMedicament);
        // Set Date Picker
        DatePicker datePicker = findViewById(R.id.datePicker);
        List<Integer> dates = validation.splitByDate(expirationDate);
        datePicker.updateDate(dates.get(0), dates.get(1), dates.get(2));
        //Set Input Count Medicament
        TextInputEditText inputCountMedicament = findViewById(R.id.countMedicament);
        inputCountMedicament.setText(String.valueOf(countMedicament));
        // code DB
        Button edit = findViewById(R.id.buttonAddMedicament);
        edit.setText(R.string.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidationField validation = new ValidationField();
                TextInputEditText updateName = findViewById(R.id.textFieldMedicament);
                TextInputEditText countMedicament = findViewById(R.id.countMedicament);
                DatePicker datePicker = findViewById(R.id.datePicker);
                String count = String.valueOf(countMedicament.getText());
                String date = validation.collectOnDate(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
                Medicament updateMedicament = new Medicament(idMedicament, idKit, updateName.getText().toString(), date, Integer.parseInt(count), colorImage);
                DBHelper db = new DBHelper(getApplicationContext());
                db.updateMedicament(medicament, updateMedicament);
                onBackPressed();
            }
        });
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
        intent.putExtra("id_kit_put", medicament.getId_kit());
        intent.putExtra("name_kit_put", nameKit);
        this.startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}