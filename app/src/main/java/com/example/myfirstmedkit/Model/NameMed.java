package com.example.myfirstmedkit.Model;

public class NameMed {
    private int idMed;
    private String nameMed;

    public NameMed(int idMed, String nameMed){
        this.idMed = idMed;
        this.nameMed = nameMed;
    }
    public NameMed(String nameMed){
        this.nameMed = nameMed;
    }

    public int getIdMed() {
        return idMed;
    }

    public String getNameMed() {
        return nameMed;
    }
}
