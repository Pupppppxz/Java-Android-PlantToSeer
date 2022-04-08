package com.example.plant_app.detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.plant_app.R;
import com.example.plant_app.firebase.OntologySearch;
import com.example.plant_app.firebase.Plant;

import java.util.ArrayList;
import java.util.List;

public class OntologyFragment extends Fragment {

    private Plant plantArg;
    private CheckBox herbaceous, perennial, shrub, climber, scandent, family, season, nutrient, bulb, pod, leaf,
                flower, simpleFruit, multipleFruit, aggregateFruit, treatment, planting, plantCare;
    private Button btnNext;
    private String oBotanicalHabit = "", oFamily = "", oSeason = "", oVitamin = "", oMineral = "", oVegetableType = "",
            oFruitType = "", oHerbType = "", oPlanting = "", oPlantCare = "", oSoil = "", oSoilPH = "", oSunExposure = "",
            oWater = "", oTemp = "", oHumidity = "", oFert = "";
    private List<Plant> plantsList;

    public OntologyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ontology, container, false);

        initElement(v);

        initCheckBox();

        btnNext.setOnClickListener(view -> initParameterAntSwitchFragment());

        otherEvent();

        botanicalListener();

        typeOfPlantListener();

        return v;
    }

    private void typeOfPlantListener() {
        bulb.setOnClickListener(view -> {
            oVegetableType = "Bulb";
            oFruitType = "";
            oHerbType = "";

            pod.setChecked(false);
            leaf.setChecked(false);
            flower.setChecked(false);
            simpleFruit.setChecked(false);
            multipleFruit.setChecked(false);
            aggregateFruit.setChecked(false);
            treatment.setChecked(false);
        });

        pod.setOnClickListener(view -> {
            oVegetableType = "Pod";
            oFruitType = "";
            oHerbType = "";

            bulb.setChecked(false);
            leaf.setChecked(false);
            flower.setChecked(false);
            simpleFruit.setChecked(false);
            multipleFruit.setChecked(false);
            aggregateFruit.setChecked(false);
            treatment.setChecked(false);
        });

        leaf.setOnClickListener(view -> {
            oVegetableType = "Leaf";
            oFruitType = "";
            oHerbType = "";

            pod.setChecked(false);
            bulb.setChecked(false);
            flower.setChecked(false);
            simpleFruit.setChecked(false);
            multipleFruit.setChecked(false);
            aggregateFruit.setChecked(false);
            treatment.setChecked(false);
        });

        flower.setOnClickListener(view -> {
            oVegetableType = "Flower";
            oFruitType = "";
            oHerbType = "";

            pod.setChecked(false);
            leaf.setChecked(false);
            bulb.setChecked(false);
            simpleFruit.setChecked(false);
            multipleFruit.setChecked(false);
            aggregateFruit.setChecked(false);
            treatment.setChecked(false);
        });

        simpleFruit.setOnClickListener(view -> {
            oVegetableType = "";
            oFruitType = "Simple Fruit";
            oHerbType = "";

            pod.setChecked(false);
            leaf.setChecked(false);
            flower.setChecked(false);
            bulb.setChecked(false);
            multipleFruit.setChecked(false);
            aggregateFruit.setChecked(false);
            treatment.setChecked(false);
        });

        multipleFruit.setOnClickListener(view -> {
            oVegetableType = "";
            oFruitType = "Multiple Fruit";
            oHerbType = "";

            pod.setChecked(false);
            leaf.setChecked(false);
            flower.setChecked(false);
            simpleFruit.setChecked(false);
            bulb.setChecked(false);
            aggregateFruit.setChecked(false);
            treatment.setChecked(false);
        });

        aggregateFruit.setOnClickListener(view -> {
            oVegetableType = "";
            oFruitType = "Aggregate Fruit";
            oHerbType = "";

            pod.setChecked(false);
            leaf.setChecked(false);
            flower.setChecked(false);
            simpleFruit.setChecked(false);
            multipleFruit.setChecked(false);
            bulb.setChecked(false);
            treatment.setChecked(false);
        });

        treatment.setOnClickListener(view -> {
            oVegetableType = "";
            oFruitType = "";
            oHerbType = "Treatment";

            pod.setChecked(false);
            leaf.setChecked(false);
            flower.setChecked(false);
            simpleFruit.setChecked(false);
            multipleFruit.setChecked(false);
            aggregateFruit.setChecked(false);
            bulb.setChecked(false);
        });
    }

    private void botanicalListener() {
        herbaceous.setOnClickListener(view -> {
            oBotanicalHabit = "Herbaceous Stem";
            perennial.setChecked(false);
            shrub.setChecked(false);
            climber.setChecked(false);
            scandent.setChecked(false);
        });

        perennial.setOnClickListener(view -> {
            oBotanicalHabit = "Perennial Plant";
            herbaceous.setChecked(false);
            shrub.setChecked(false);
            climber.setChecked(false);
            scandent.setChecked(false);
        });

        shrub.setOnClickListener(view -> {
            oBotanicalHabit = "Shrub";
            perennial.setChecked(false);
            herbaceous.setChecked(false);
            climber.setChecked(false);
            scandent.setChecked(false);
        });

        climber.setOnClickListener(view -> {
            oBotanicalHabit = "Climber";
            perennial.setChecked(false);
            shrub.setChecked(false);
            herbaceous.setChecked(false);
            scandent.setChecked(false);
        });

        scandent.setOnClickListener(view -> {
            oBotanicalHabit = "Scandent";
            perennial.setChecked(false);
            shrub.setChecked(false);
            climber.setChecked(false);
            herbaceous.setChecked(false);
        });
    }

    private void otherEvent() {
        family.setOnClickListener(view -> {
            if (family.isChecked()) {
                oFamily = plantArg.getFamilies();
            } else {
                oFamily = "";
            }
        });

        season.setOnClickListener(view -> {
            if (season.isChecked()) {
                oSeason = plantArg.getSeason();
            } else {
                oSeason = "";
            }
        });

        nutrient.setOnClickListener(view -> {
            if (nutrient.isChecked()) {
                oMineral = plantArg.getMineral();
                oVitamin = plantArg.getVitamin();
            } else {
                oMineral = "";
                oVitamin = "";
            }
        });

        planting.setOnClickListener(view -> {
            if (planting.isChecked()) {
                oPlanting = plantArg.getPlanting();
            } else {
                oPlanting = "";
            }
        });

        plantCare.setOnClickListener(view -> {
            if (plantCare.isChecked()) {
                oSoil = plantArg.getSoil();
                oSoilPH = plantArg.getSoilPH();
                oSunExposure = plantArg.getSunExposure();
                oWater = plantArg.getWater();
                oTemp = plantArg.getTemperature();
                oHumidity = plantArg.getHumidity();
                oFert = plantArg.getFertilizer();
            } else {
                oSoil = "";
                oSoilPH = "";
                oSunExposure = "";
                oWater = "";
                oTemp = "";
                oHumidity = "";
                oFert = "";
            }
        });

        treatment.setOnClickListener(viewß -> {
            if (treatment.isChecked()) {
                oHerbType = plantArg.getTreatments();
            } else {
                oHerbType = "";
            }
        });
    }

    private void initParameterAntSwitchFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        OntologySearch ontologySearch = new OntologySearch(oBotanicalHabit, oFamily, oSeason, oVitamin, oMineral,
                oVegetableType, oFruitType, oHerbType, oPlanting, oSoil, oSoilPH, oSunExposure, oWater, oTemp, oHumidity, oFert);
        OntologyFilteredFragment ontologyFilteredFragment = new OntologyFilteredFragment();
        ontologyFilteredFragment.setOntologySearch(ontologySearch);
        ontologyFilteredFragment.setAllPlants(plantsList);

        fragmentTransaction.replace(R.id.homeFrameLayout, ontologyFilteredFragment);
        fragmentTransaction.commit();
    }

    private void initCheckBox() {
        switch (plantArg.getBotanicalHabit()) {
            case "Perennial Plant":
                perennial.setChecked(true);
                oBotanicalHabit = plantArg.getBotanicalHabit();
                break;
            case "Shrub":
                shrub.setChecked(true);
                oBotanicalHabit = plantArg.getBotanicalHabit();
                break;
            case "Climber":
                climber.setChecked(true);
                oBotanicalHabit = plantArg.getBotanicalHabit();
                break;
            case "Scandent":
                scandent.setChecked(true);
                oBotanicalHabit = plantArg.getBotanicalHabit();
                break;
            default:
                herbaceous.setChecked(true);
                oBotanicalHabit = plantArg.getBotanicalHabit();
        }

        if (plantArg.getType().equalsIgnoreCase("FRUIT")) {
            switch (plantArg.getClassification()) {
                case "Multiple Fruit":
                    multipleFruit.setChecked(true);
                    oFruitType = plantArg.getClassification();
                    break;
                case "Aggregate Fruit":
                    aggregateFruit.setChecked(true);
                    oFruitType = plantArg.getClassification();
                    break;
                default:
                    simpleFruit.setChecked(true);
                    oFruitType = plantArg.getClassification();
            }
        } else if (plantArg.getType().equalsIgnoreCase("VEGETABLE")) {
            switch (plantArg.getClassification()) {
                case "Bulb":
                    bulb.setChecked(true);
                    oVegetableType = plantArg.getClassification();
                    break;
                case "Pod":
                    pod.setChecked(true);
                    oVegetableType = plantArg.getClassification();
                    break;
                case "Leaf":
                    leaf.setChecked(true);
                    oVegetableType = plantArg.getClassification();
                    break;
                default:
                    flower.setChecked(true);
                    oVegetableType = plantArg.getClassification();
            }
        } else {
            treatment.setChecked(true);
            oHerbType = plantArg.getTreatments();
        }
    }

    private void initElement(View v) {
        herbaceous = v.findViewById(R.id.ontology_herbaceous_stem);
        perennial = v.findViewById(R.id.ontology_perennial_plant);
        shrub = v.findViewById(R.id.ontology_shrub);
        climber = v.findViewById(R.id.ontology_climber);
        scandent = v.findViewById(R.id.ontology_scandent);
        family = v.findViewById(R.id.ontology_family);
        season = v.findViewById(R.id.ontology_season);
        nutrient = v.findViewById(R.id.ontology_nutrient);
        bulb = v.findViewById(R.id.ontology_bulb);
        pod = v.findViewById(R.id.ontology_pod);
        leaf = v.findViewById(R.id.ontology_leaf);
        flower = v.findViewById(R.id.ontology_flower);
        simpleFruit = v.findViewById(R.id.ontology_simple_fruit);
        multipleFruit = v.findViewById(R.id.ontology_multiple_fruit);
        aggregateFruit = v.findViewById(R.id.ontology_aggregate_fruit);
        planting = v.findViewById(R.id.ontology_planting);
        treatment = v.findViewById(R.id.ontology_treatment);
        plantCare = v.findViewById(R.id.ontology_plant_care);
        btnNext = v.findViewById(R.id.plant_ontology_button);
    }

    public void setAllPlants(List<Plant> plantsList) {
        this.plantsList = new ArrayList<>(plantsList);
    }

    public void setPlant(Plant plant) {
        this.plantArg = plant;
    }
}