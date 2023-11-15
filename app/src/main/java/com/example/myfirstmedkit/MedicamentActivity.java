package com.example.myfirstmedkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.myfirstmedkit.Adapters.RVMedicamentAdapter;
import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.Medicament;

public class MedicamentActivity extends AppCompatActivity {

    private ArrayList<Medicament> medicamentArrayList = new ArrayList<>();
    private int id_kit;
    private String nameKit;
    private RVMedicamentAdapter rvMedicamentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament);

        Bundle argument = getIntent().getExtras();
        id_kit = (int) argument.get("id_kit_put");
        nameKit = (String) argument.get("name_kit_put");
        TextView textInfo = findViewById(R.id.nameKitInfo);
        textInfo.setText(nameKit);

        RecyclerView rv_medicament = findViewById(R.id.rv_medicament);
        DBHelper dataBaseHelper = new DBHelper(this);
        medicamentArrayList = dataBaseHelper.getAllMedicaments(id_kit);

        rvMedicamentAdapter = new RVMedicamentAdapter(this, medicamentArrayList, nameKit);
        rv_medicament.setLayoutManager(new LinearLayoutManager(this));
        rv_medicament.setAdapter(rvMedicamentAdapter);

    }

    public void addMedicamentButton(View view){
        Intent intent = new Intent(this, AddMedicamentActivity.class);
        intent.putExtra("id_kit_put", id_kit);
        intent.putExtra("name_kit_put", nameKit);
        this.startActivity(intent);
        this.finish();
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