package com.example.myfirstmedkit.Model;

public class Medicament {
    private int id_kit;
    private int id_medicament;
    private String name_medicament;
    private String expiration_date;
    private int tablets_in_pack;
    private int idColor;

    public Medicament(int id_medicament, int id_kit, String name_medicament, String expiration_date, int tablets_in_pack, int idColor) {
        this.id_kit = id_kit;
        this.id_medicament = id_medicament;
        this.name_medicament = name_medicament;
        this.expiration_date = expiration_date;
        this.tablets_in_pack = tablets_in_pack;
        this.idColor = idColor;
    }

    public Medicament(int id_kit, String name_medicament, String expiration_date, int tablets_in_pack, int idColor) {
        this.id_kit = id_kit;
        this.name_medicament = name_medicament;
        this.expiration_date = expiration_date;
        this.tablets_in_pack = tablets_in_pack;
        this.idColor = idColor;
    }

    public int getId_kit() {
        return id_kit;
    }

    public int getId_medicament() {
        return id_medicament;
    }

    public String getName_medicament() {
        return name_medicament;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public int getTablets_in_pack() {
        return tablets_in_pack;
    }

    public int getIdColor() {
        return idColor;
    }
    public void setId_kit(int value){
        id_kit = value;
    }

    public void setTablets_in_pack(int tablets_in_pack) {
        this.tablets_in_pack = tablets_in_pack;
    }

}
