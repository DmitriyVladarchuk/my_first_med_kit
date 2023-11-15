package com.example.myfirstmedkit.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstmedkit.EditMedicamentActivity;
import com.example.myfirstmedkit.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.KitData;
import com.example.myfirstmedkit.Model.Medicament;
import com.example.myfirstmedkit.Validation.ValidationDate;

public class RVMedicamentAdapter extends RecyclerView.Adapter<RVMedicamentAdapter.MedicamentHolder>{
    private Context context;
    private ArrayList<Medicament> medicamentArrayList;
    private String nameKit;

    public RVMedicamentAdapter(Context context, ArrayList<Medicament> medicamentArrayList, String nameKit){
        this.context = context;
        this.medicamentArrayList = medicamentArrayList;
        this.nameKit = nameKit;
    }

    @NonNull
    @Override
    public MedicamentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicament_item, parent, false);
        return new MedicamentHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MedicamentHolder holder, int position) {
        holder.nameMedicament.setText(medicamentArrayList.get(position).getName_medicament());
        holder.expirationDate.setText(context.getResources().getText(R.string.valid) + " "
                + medicamentArrayList.get(position).getExpiration_date());
        holder.totalMedicament.setText(context.getResources().getText(R.string.count) + " "
                + medicamentArrayList.get(position).getTablets_in_pack());
        holder.imageView.setColorFilter(context.getResources().getColor(medicamentArrayList.get(position).getIdColor()));

        validationItem(holder, position);

        int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottoms_sheet_setting_medicament);

                //code click listener
                TextView nameBottomSheet = dialog.findViewById(R.id.bottomSheetNameMedicament);
                nameBottomSheet.setText(medicamentArrayList.get(pos).getName_medicament());

                Button accept = dialog.findViewById(R.id.acceptMedicamentButton);
                Button edit = dialog.findViewById(R.id.editMedicamentButton);
                Button move = dialog.findViewById(R.id.moveMedicamentButton);
                Button delete = dialog.findViewById(R.id.deleteMedicamentButton);

                // event accept
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(!ValidationDate.isExpired(medicamentArrayList.get(pos).getExpiration_date()) &&
                                    medicamentArrayList.get(pos).getTablets_in_pack() > 0){
                                dialog.cancel();
                                Dialog dialogAccept = new Dialog(context);
                                dialogAccept.setContentView(R.layout.accept_medicament_dialog);
                                dialogAccept.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView textInfo = dialogAccept.findViewById(R.id.textAcceptInfo);
                                textInfo.setText(medicamentArrayList.get(pos).getName_medicament());
                                // event input text
                                TextView remainedText = dialogAccept.findViewById(R.id.remainedText);
                                TextInputEditText acceptField = dialogAccept.findViewById(R.id.acceptField);
                                acceptField.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        if (charSequence.length() > 0)
                                        {
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        if (editable.length() > 0) {
                                            int remained = Integer.parseInt(String.valueOf(acceptField.getText()));
                                            if (medicamentArrayList.get(pos).getTablets_in_pack() - remained >= 0)
                                                remainedText.setText(context.getResources().getString(R.string.remained) + ": " + (medicamentArrayList.get(pos).getTablets_in_pack() - remained));
                                            else
                                                acceptField.setError(context.getResources().getString(R.string.error_remained));
                                        }
                                    }
                                });

                                // button
                                Button acceptButton = dialogAccept.findViewById(R.id.acceptDialogButton);
                                acceptButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int value = medicamentArrayList.get(pos).getTablets_in_pack() - Integer.parseInt(String.valueOf(acceptField.getText()));
                                        //DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                                        //dataBaseHelper.updateTabletsInPack(medicamentArrayList.get(pos).getId_medicament(), value);
                                        DBHelper db = new DBHelper(context);
                                        db.updateTabletsInPack(medicamentArrayList.get(pos).getId_medicament(), value);
                                        dialogAccept.cancel();
                                        Medicament medicamentUpdate = medicamentArrayList.get(pos);
                                        medicamentUpdate.setTablets_in_pack(value);
                                        updateItem(medicamentUpdate, pos);
                                    }
                                });

                                dialogAccept.show();
                                dialogAccept.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            }
                            else{
                                dialog.cancel();
                                Dialog dialogMessege = new Dialog(context);
                                dialogMessege.setContentView(R.layout.dialog_messege);
                                dialogMessege.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView textInfo = dialogMessege.findViewById(R.id.textWarning);
                                if(ValidationDate.isExpired(medicamentArrayList.get(pos).getExpiration_date()))
                                    textInfo.setText(context.getResources().getText(R.string.warning_expiration_date));
                                else
                                    textInfo.setText(context.getResources().getText(R.string.warning_count));

                                dialogMessege.show();
                                dialogMessege.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            }

                        }
                    });

                // event edit
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int idKit = medicamentArrayList.get(pos).getId_kit();
                        int idMedicament = medicamentArrayList.get(pos).getId_medicament();
                        String nameMedicament = medicamentArrayList.get(pos).getName_medicament();
                        String expirationDate = medicamentArrayList.get(pos).getExpiration_date();
                        int countMedicament = medicamentArrayList.get(pos).getTablets_in_pack();
                        int idColor = medicamentArrayList.get(pos).getIdColor();

                        Intent intent = new Intent(context, EditMedicamentActivity.class);
                        intent.putExtra("idKit", idKit);
                        intent.putExtra("nameKit", nameKit);
                        intent.putExtra("idMedicament", idMedicament);
                        intent.putExtra("nameMedicament", nameMedicament);
                        intent.putExtra("expirationDate", expirationDate);
                        intent.putExtra("countMedicament", countMedicament);
                        intent.putExtra("idColor", idColor);
                        dialog.closeOptionsMenu();
                        ((Activity) context).startActivity(intent);
                        ((Activity) context).finish();
                    }
                });
                // event move
                move.setOnClickListener(new View.OnClickListener() {
                    int idKit;
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        Dialog dialogMove = new Dialog(context);
                        dialogMove.setContentView(R.layout.move_medicament_dialog);
                        dialogMove.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView textInfo = dialogMove.findViewById(R.id.textMoveInfo);
                        textInfo.setText(medicamentArrayList.get(pos).getName_medicament());

                        // Spinner
                        //DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        //ArrayList<KitData> kits = dataBaseHelper.selectKitData();
                        DBHelper db = new DBHelper(context);
                        ArrayList<KitData> kits = db.getAllKit();
                        Spinner spinner = dialogMove.findViewById(R.id.spinnerMove);
                        String[] nameKitList = new String[kits.size()];
                        for(int i = 0; i < kits.size(); i++)
                            nameKitList[i] = kits.get(i).getName_kit();

                        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, nameKitList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Получаем выбранный объект
                                idKit = kits.get(position).getId_kit();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        };
                        spinner.setOnItemSelectedListener(itemSelectedListener);
                        // button event
                        Button moveButton = dialogMove.findViewById(R.id.moveDialogButton);
                        moveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                                //dataBaseHelper.moveMedicament(idKit, medicamentArrayList.get(pos));
                                DBHelper db = new DBHelper(context);
                                db.moveMedicament(idKit, medicamentArrayList.get(pos));

                                removeItem(pos);
                                dialogMove.cancel();
                            }
                        });

                        dialogMove.show();
                        dialogMove.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    }
                });

                // event delete
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        //dataBaseHelper.deleteMedicament(medicamentArrayList.get(pos).getId_medicament());
                        DBHelper db = new DBHelper(context);
                        db.deleteMedicament(medicamentArrayList.get(pos).getId_medicament());

                        removeItem(pos);
                        validationItem(holder, pos);
                        dialog.cancel();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicamentArrayList.size();
    }


    public void updateItem(Medicament medicament, int position){
        medicamentArrayList.set(position, medicament);
        notifyItemChanged(position);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void removeItem(int position){
        //Test db = new Test(context);
        //medicamentArrayList = db.getAllMedicaments()
        medicamentArrayList.remove(position);
        //notifyItemRemoved(position);
        notifyDataSetChanged();
        //notifyItemChanged(position);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void validationItem(MedicamentHolder holder, int position){
        if(position >= getItemCount())
            return;
        Medicament medicament = medicamentArrayList.get(position);
        boolean isEntry = false;
        if(ValidationDate.isExpired(medicament.getExpiration_date())){
            holder.expirationDate.setText(context.getResources().getText(R.string.is_expired));
            isEntry = true;
        }
        if (ValidationDate.isOverMedicament(medicament.getTablets_in_pack())){
            isEntry = true;
        }

        if(isEntry)
            holder.cardView.setForeground(context.getResources().getDrawable(R.drawable.red_border));
        else
            holder.cardView.setForeground(null);

    }

    static class MedicamentHolder extends RecyclerView.ViewHolder {
        TextView nameMedicament, expirationDate, totalMedicament;
        ImageView imageView;
        CardView cardView;

        public MedicamentHolder(@NonNull View itemView) {
            super(itemView);

            nameMedicament = itemView.findViewById(R.id.nameMedicament);
            expirationDate = itemView.findViewById(R.id.expirationDate);
            totalMedicament = itemView.findViewById(R.id.totalMedicament);
            imageView = itemView.findViewById(R.id.image_medicament);
            cardView = itemView.findViewById(R.id.medicamentItemView);
        }
    }
}

