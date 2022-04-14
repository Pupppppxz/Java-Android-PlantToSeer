package com.example.plant_app.firebase;

import android.graphics.Bitmap;

public class PlantListView {
    private String name;
    private String sciName;
    private String type;
    private String treatments;
    private int img, index;
    private Bitmap bitmap;

    public PlantListView() {
    }

    public PlantListView(String name, String sciName, String type, Bitmap img, int index, String treatments) {
        this.name = name;
        this.sciName = sciName;
        this.type = type;
        this.bitmap = img;
        this.index = index;
        this.treatments = treatments;
    }

    public PlantListView(String name, String sciName, String type, int img, int index, String treatments) {
        this.name = name;
        this.sciName = sciName;
        this.type = type;
        this.treatments = treatments;
        this.img = img;
        this.index = index;
    }

    public String getTreatments() {
        return treatments;
    }

    public void setTreatments(String treatments) {
        this.treatments = treatments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSciName() {
        return sciName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Plant [name=" + name + ", type=" + type + "]";
    }

}

