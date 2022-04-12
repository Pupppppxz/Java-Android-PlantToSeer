package com.example.plant_app.firebase;

public class Plant {
    private String name;
    private String scienceName;
    private String families;
    private String familyDescription;
    private String description;
    private String season;
    private String vitamin;
    private String mineral;
    private String harvestTime;
    private String treatments;
    private String planting;
    private String soil;
    private String soilPH;
    private String sunExposure;
    private String water;
    private String temperature;
    private String humidity;
    private String fertilizer;
    private String botanicalHabit;
    private String classification;
    private String type;
    private String owner;
    public Plant() {}
    public Plant(String name, String scienceName, String families, String familyDescription, String description,
                 String season, String vitamin, String mineral, String harvestTime, String treatments, String planting,
                 String soil, String soilPH, String sunExposure, String water, String temperature, String humidity,
                 String fertilizer, String botanicalHabit, String type) {
        this.name = name;
        this.scienceName = scienceName;
        this.families = families;
        this.familyDescription = familyDescription;
        this.description = description;
        this.season = season;
        this.vitamin = vitamin;
        this.mineral = mineral;
        this.harvestTime = harvestTime;
        this.treatments = treatments;
        this.planting = planting;
        this.soil = soil;
        this.soilPH = soilPH;
        this.sunExposure = sunExposure;
        this.water = water;
        this.temperature = temperature;
        this.humidity = humidity;
        this.fertilizer = fertilizer;
        this.botanicalHabit = botanicalHabit;
        this.type = type;
    }

    public Plant(String name, String scienceName, String families, String familyDescription, String description, String season, String vitamin, String mineral, String harvestTime, String treatments, String planting, String soil, String soilPH, String sunExposure, String water, String temperature, String humidity, String fertilizer, String botanicalHabit, String classification, String type, String owner) {
        this.name = name;
        this.scienceName = scienceName;
        this.families = families;
        this.familyDescription = familyDescription;
        this.description = description;
        this.season = season;
        this.vitamin = vitamin;
        this.mineral = mineral;
        this.harvestTime = harvestTime;
        this.treatments = treatments;
        this.planting = planting;
        this.soil = soil;
        this.soilPH = soilPH;
        this.sunExposure = sunExposure;
        this.water = water;
        this.temperature = temperature;
        this.humidity = humidity;
        this.fertilizer = fertilizer;
        this.botanicalHabit = botanicalHabit;
        this.classification = classification;
        this.type = type;
        this.owner = owner;
    }

    public Plant(String name, String scienceName, String families, String familyDescription, String description, String season, String vitamin, String mineral, String harvestTime, String treatments, String planting, String soil, String soilPH, String sunExposure, String water, String temperature, String humidity, String fertilizer, String botanicalHabit, String type, String owner) {
        this.name = name;
        this.scienceName = scienceName;
        this.families = families;
        this.familyDescription = familyDescription;
        this.description = description;
        this.season = season;
        this.vitamin = vitamin;
        this.mineral = mineral;
        this.harvestTime = harvestTime;
        this.treatments = treatments;
        this.planting = planting;
        this.soil = soil;
        this.soilPH = soilPH;
        this.sunExposure = sunExposure;
        this.water = water;
        this.temperature = temperature;
        this.humidity = humidity;
        this.fertilizer = fertilizer;
        this.botanicalHabit = botanicalHabit;
        this.type = type;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getScienceName() {
        return scienceName;
    }
    public void setScienceName(String scienceName) {
        this.scienceName = scienceName;
    }
    public String getFamilies() {
        return families;
    }
    public void setFamilies(String families) {
        this.families = families;
    }
    public String getFamilyDescription() {
        return familyDescription;
    }
    public void setFamilyDescription(String familyDescription) {
        this.familyDescription = familyDescription;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public String getHarvestTime() {
        return harvestTime;
    }
    public void setHarvestTime(String harvestTime) {
        this.harvestTime = harvestTime;
    }
    public String getTreatments() {
        return treatments;
    }
    public void setTreatments(String treatments) {
        this.treatments = treatments;
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
    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public String getHumidity() {
        return humidity;
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public String getFertilizer() {
        return fertilizer;
    }
    public void setFertilizer(String fertilizer) {
        this.fertilizer = fertilizer;
    }
    public String getBotanicalHabit() {
        return botanicalHabit;
    }
    public void setBotanicalHabit(String botanicalHabit) {
        this.botanicalHabit = botanicalHabit;
    }
    public String getClassification() {
        return classification;
    }
    public void setClassification(String classification) {
        this.classification = classification;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Plant [botanicalHabit=" + botanicalHabit + ", classification=" + classification + ", description="
                + description + ", families=" + families + ", familyDescription=" + familyDescription + ", fertilizer="
                + fertilizer + ", harvestTime=" + harvestTime + ", humidity=" + humidity + ", mineral=" + mineral
                + ", name=" + name + ", planting=" + planting + ", scienceName=" + scienceName + ", season=" + season
                + ", soil=" + soil + ", soilPH=" + soilPH + ", sunExposure=" + sunExposure + ", temperature="
                + temperature + ", treatments=" + treatments + ", type=" + type + ", vitamin=" + vitamin + ", water="
                + water + "]";
    }
}
