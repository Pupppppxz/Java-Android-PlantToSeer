package com.example.plant_app.firebase;

public class OntologySearch {
    private String botanicalHabit, family, season, vitamin, mineral, vegetableType, fruitType, herbType,
            planting, soil, soilPH, sunExposure, water, temp, humidity, fert, name;

    public OntologySearch(){}

    public OntologySearch(String botanicalHabit, String family, String season, String vitamin, String mineral,
                          String vegetableType, String fruitType, String herbType, String planting, String soil,
                          String soilPH, String sunExposure, String water, String temp, String humidity, String fert, String name) {
        this.botanicalHabit = botanicalHabit;
        this.family = family;
        this.season = season;
        this.vitamin = vitamin;
        this.mineral = mineral;
        this.vegetableType = vegetableType;
        this.fruitType = fruitType;
        this.herbType = herbType;
        this.planting = planting;
        this.soil = soil;
        this.soilPH = soilPH;
        this.sunExposure = sunExposure;
        this.water = water;
        this.temp = temp;
        this.humidity = humidity;
        this.fert = fert;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBotanicalHabit() {
        return botanicalHabit;
    }

    public void setBotanicalHabit(String botanicalHabit) {
        this.botanicalHabit = botanicalHabit;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getVitamin() {
        return vitamin;
    }

    public void setVitamin(String vitamin) {
        this.vitamin = vitamin;
    }

    public String getMineral() {
        return mineral;
    }

    public void setMineral(String mineral) {
        this.mineral = mineral;
    }

    public String getVegetableType() {
        return vegetableType;
    }

    public void setVegetableType(String vegetableType) {
        this.vegetableType = vegetableType;
    }

    public String getFruitType() {
        return fruitType;
    }

    public void setFruitType(String fruitType) {
        this.fruitType = fruitType;
    }

    public String getHerbType() {
        return herbType;
    }

    public void setHerbType(String herbType) {
        this.herbType = herbType;
    }

    public String getPlanting() {
        return planting;
    }

    public void setPlanting(String planting) {
        this.planting = planting;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getSoilPH() {
        return soilPH;
    }

    public void setSoilPH(String soilPH) {
        this.soilPH = soilPH;
    }

    public String getSunExposure() {
        return sunExposure;
    }

    public void setSunExposure(String sunExposure) {
        this.sunExposure = sunExposure;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getFert() {
        return fert;
    }

    @Override
    public String toString() {
        return "OntologySearch{" +
                "botanicalHabit='" + botanicalHabit + '\'' +
                ", family='" + family + '\'' +
                ", season='" + season + '\'' +
                ", vitamin='" + vitamin + '\'' +
                ", mineral='" + mineral + '\'' +
                ", vegetableType='" + vegetableType + '\'' +
                ", fruitType='" + fruitType + '\'' +
                ", herbType='" + herbType + '\'' +
                ", planting='" + planting + '\'' +
                ", soil='" + soil + '\'' +
                ", soilPH='" + soilPH + '\'' +
                ", sunExposure='" + sunExposure + '\'' +
                ", water='" + water + '\'' +
                ", temp='" + temp + '\'' +
                ", humidity='" + humidity + '\'' +
                ", fert='" + fert + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void setFert(String fert) {
        this.fert = fert;
    }
}
