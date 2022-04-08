package com.example.plant_app.firebase;

public class PlantListView {
    private String name;
    private String sciName;
    private String type;
    private int img, index;

    public PlantListView() {
    }

    public PlantListView(String name, String sciName, String type, int img, int index) {
        this.name = name;
        this.sciName = sciName;
        this.type = type;
        this.img = img;
        this.index = index;
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

