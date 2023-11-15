package com.example.myfirstmedkit.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstmedkit.AddKitActivity;
import com.example.myfirstmedkit.EditKitActivity;
import com.example.myfirstmedkit.MedicamentActivity;
import com.example.myfirstmedkit.R;

import java.util.ArrayList;

import com.example.myfirstmedkit.DataBase.DBHelper;
import com.example.myfirstmedkit.Model.KitData;

public class RVKitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<KitData> kitDataArrayList;
    public RVKitAdapter(Context context, ArrayList<KitData> kitDataArrayList){
        this.context = context;
        this.kitDataArrayList = kitDataArrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kit_item_add, parent, false);
            return new KitHolderAdd(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kit_item, parent, false);
        return new KitHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int pos = position;
        if(position != kitDataArrayList.size()){
            KitHolder kitHolder = (KitHolder) holder;
            kitHolder.nameKit.setText(kitDataArrayList.get(position).getName_kit());
            kitHolder.imageView.setColorFilter(context.getResources().getColor(kitDataArrayList.get(position).getIdColor()));

            // click event
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pos != RecyclerView.NO_POSITION){
                        int id = kitDataArrayList.get(pos).getId_kit();
                        String nameKit = kitDataArrayList.get(pos).getName_kit();
                        Intent intent = new Intent(context, MedicamentActivity.class);
                        intent.putExtra("id_kit_put", id);
                        intent.putExtra("name_kit_put", nameKit);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.bottoms_sheet_setting_kit);

                    TextView textView = dialog.findViewById(R.id.bottomSheetNameKit);
                    textView.setText(kitHolder.nameKit.getText());
                    Button buttonEdit = dialog.findViewById(R.id.editKitButton);
                    Button buttonDelete = dialog.findViewById(R.id.deleteKitButton);
                    //code click listener
                    buttonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(pos != RecyclerView.NO_POSITION){
                                int id = kitDataArrayList.get(pos).getId_kit();
                                String nameKit = kitDataArrayList.get(pos).getName_kit();
                                int color = kitDataArrayList.get(pos).getIdColor();
                                Intent intent = new Intent(context, EditKitActivity.class);
                                intent.putExtra("id_kit_put", id);
                                intent.putExtra("name_kit_put", nameKit);
                                intent.putExtra("colorKit", color);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        }
                    });

                    buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                            //dataBaseHelper.deleteKit(kitDataArrayList.get(pos).getId_kit());
                            DBHelper db = new DBHelper(context);
                            db.deleteKit(kitDataArrayList.get(pos).getId_kit());

                            dialog.cancel();
                            removeItem(pos);
                        }
                    });

                    dialog.show();
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    return false;
                }
            });
        }
        // кнопка добавления набора
        else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddKitActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(kitDataArrayList.size() == 0){
            return 1;
        }
        return kitDataArrayList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == kitDataArrayList.size())
            return 1;
        return 0;

    }

    public void removeItem(int position){
        //Test db = new Test(context);
        //kitDataArrayList = db.getAllKit();
        if (position >= kitDataArrayList.size()) return;
        kitDataArrayList.remove(position);
        //notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}

class KitHolder extends RecyclerView.ViewHolder{
    TextView nameKit;
    CardView cardView;
    ImageView imageView;
    public KitHolder(@NonNull View itemView) {
        super(itemView);

        nameKit = itemView.findViewById(R.id.nameKit);
        cardView = itemView.findViewById(R.id.cart_kit);
        imageView = itemView.findViewById(R.id.image_kit);
    }
}

class KitHolderAdd extends RecyclerView.ViewHolder {

    CardView cardView;

    public KitHolderAdd(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.cart_kit_add);
    }
}
