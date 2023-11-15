package com.example.myfirstmedkit.Model;

public class KitData {
    private int id_kit;
    private String name_kit;
    private int idColor;
    public KitData(int id_kit, String name_kit, int idColor){
        this.id_kit = id_kit;
        this.name_kit = name_kit;
        this.idColor = idColor;
    }

    public KitData(String name_kit, int idColor){
        this.name_kit = name_kit;
        this.idColor = idColor;
    }

    public int getId_kit() {
        return id_kit;
    }

    public String getName_kit() {
        return name_kit;
    }

    public int getIdColor() {
        return idColor;
    }
}
