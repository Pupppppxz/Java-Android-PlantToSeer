package com.example.plant_app.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.plant_app.R;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.firebase.OntologySearch;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantListView;
import com.example.plant_app.search.PlantListAdapter1;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OntologyFilteredFragment extends Fragment {

    private FirebaseStorage storage;

    private ListView listViewOfFiltered;
    private OntologySearch ontologySearch;
    private TextView fBotanicalHabit, fFamily, fPlantType, fPlantTypeValue, fSeason, fVitamin, fMineral, fPlanting, fSoil, fSoilPH, fSun, fwater, fTemp, fHumi, fFert, plant_name_view, fTreatment;
    private LinearLayout linearFamily, linearSeason, linearNutrient, linearPlanting, linearPlantCare, linearBotanical, linearType, linearTreatment;
    private List<Plant> plantsList;
    private List<Plant> plantsFiltered = new ArrayList<>();
    ArrayList<PlantListView> plantList = new ArrayList<>();
    PlantListAdapter1 adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ontology_filtered, container, false);

        initElement(v);

        initfield(v);

        System.out.println(ontologySearch);

        return v;
    }

    private void initAdapter() {
        adapter = new PlantListAdapter1(getActivity(), R.layout.list_view_map_item_filtered, plantList);
        listViewOfFiltered.setAdapter(adapter);
        UIUtils.setListViewHeightBasedOnItems(listViewOfFiltered);
        listViewOfFiltered.setOnItemClickListener((adapterView, view, i, l) -> viewPlantDetail(plantList.get(i).getName()));
    }

    private void viewPlantDetail(String name) {

        PlantDetailFragment plantDetailFragment = new PlantDetailFragment();
        for (int i = 0; i < plantsList.size(); i++) {
            if (plantsList.get(i).getName().equalsIgnoreCase(name)) {
                plantDetailFragment.setPlant(plantsList.get(i));
            }
        }
        plantDetailFragment.setAllPlants(plantsList);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.homeFrameLayout, plantDetailFragment);
        fragmentTransaction.commit();
    }

    private void initfield(View v) {
        System.out.println("size 1 = " + plantsList.size());
        if (!ontologySearch.getBotanicalHabit().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            fBotanicalHabit.setText(ontologySearch.getBotanicalHabit());
            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsList.size(); i++) {
                if (plantsList.get(i).getBotanicalHabit().equalsIgnoreCase(ontologySearch.getBotanicalHabit())) {
                    temp.add(plantsList.get(i));
                }
            }
            System.out.println("pass 1");
            plantsFiltered = new ArrayList<>(temp);
        } else {
            linearBotanical.setVisibility(v.GONE);
        }
        System.out.println("size 2 = " + plantsFiltered.size());
        if (!ontologySearch.getFamily().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            fFamily.setText(ontologySearch.getFamily());
            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsFiltered.size(); i++) {
                if (plantsFiltered.get(i).getFamilies().equalsIgnoreCase(ontologySearch.getFamily())) {
                    temp.add(plantsFiltered.get(i));
                }
            }
            System.out.println("pass 2");
            plantsFiltered = new ArrayList<>(temp);
        } else {
            System.out.println("pass 2");
            linearFamily.setVisibility(v.GONE);
        }
        System.out.println("size 3 = " + plantsFiltered.size());
        if (!ontologySearch.getSeason().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            fSeason.setText(ontologySearch.getSeason());
            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsFiltered.size(); i++) {
                if (plantsFiltered.get(i).getSeason().equalsIgnoreCase(ontologySearch.getSeason())) {
                    temp.add(plantsFiltered.get(i));
                }
            }
            System.out.println("pass 3");
            plantsFiltered = new ArrayList<>(temp);
        } else {
            System.out.println("pass 3");
            linearSeason.setVisibility(v.GONE);
        }
        System.out.println("size 4 = " + plantsFiltered.size());
        if (!ontologySearch.getVitamin().equalsIgnoreCase("") && !ontologySearch.getMineral().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            fVitamin.setText(ontologySearch.getVitamin());
            fMineral.setText(ontologySearch.getMineral());
            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsFiltered.size(); i++) {
                if (plantsFiltered.get(i).getVitamin().contains(ontologySearch.getVitamin()) || plantsFiltered.get(i).getMineral().contains(ontologySearch.getMineral())) {
                    temp.add(plantsFiltered.get(i));
                }
            }
            System.out.println("pass 4");
            plantsFiltered = new ArrayList<>(temp);
        } else {
            System.out.println("pass 4");
            linearNutrient.setVisibility(v.GONE);
        }
        System.out.println("size 5 = " + plantsFiltered.size());
        if (!ontologySearch.getVegetableType().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            System.out.println("hello1");
            fPlantType.setText("Vegetable : ");
            fPlantTypeValue.setText(ontologySearch.getVegetableType());
            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsFiltered.size(); i++) {
                if (plantsFiltered.get(i).getType().split(",")[0].equalsIgnoreCase("VEGETABLE")) {
                    if (plantsFiltered.get(i).getClassification().equalsIgnoreCase(ontologySearch.getVegetableType())) {
                        temp.add(plantsFiltered.get(i));
                    }
                }
            }
            System.out.println("pass 5");
            plantsFiltered = new ArrayList<>(temp);
        } else if (!ontologySearch.getFruitType().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            System.out.println("hello2");
            fPlantType.setText("Fruit : ");
            fPlantTypeValue.setText(ontologySearch.getFruitType());
            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsFiltered.size(); i++) {
                if (plantsFiltered.get(i).getType().split(",")[0].equalsIgnoreCase("FRUIT")) {
                    if (plantsFiltered.get(i).getClassification().equalsIgnoreCase(ontologySearch.getFruitType())) {
                        temp.add(plantsFiltered.get(i));
                    }
                }
            }
            System.out.println("pass 6");
            plantsFiltered = new ArrayList<>(temp);
        } else {
            linearType.setVisibility(v.GONE);
            System.out.println("pass 9");
        }
        if (!ontologySearch.getHerbType().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            System.out.println("hello3");
            fTreatment.setText(ontologySearch.getHerbType());
            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsFiltered.size(); i++) {
                if (plantsFiltered.get(i).getTreatments().contains(ontologySearch.getHerbType())) {
                    temp.add(plantsFiltered.get(i));
                }
            }
            System.out.println("pass 7");
            plantsFiltered = new ArrayList<>(temp);
        }
        System.out.println("size 6 = " + plantsFiltered.size());
        if (!ontologySearch.getPlanting().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            fPlanting.setText(ontologySearch.getPlanting());
            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsFiltered.size(); i++) {
                if (plantsFiltered.get(i).getPlanting().contains(ontologySearch.getPlanting())) {
                    temp.add(plantsFiltered.get(i));
                }
            }
            System.out.println("pass 8");
            plantsFiltered = new ArrayList<>(temp);
        } else {
            System.out.println("pass 10");
            linearPlanting.setVisibility(v.GONE);
        }
        System.out.println("size 7 = " + plantsFiltered.size());
        if (!ontologySearch.getSoil().equalsIgnoreCase("") && !ontologySearch.getSoilPH().equalsIgnoreCase("") && !ontologySearch.getSunExposure().equalsIgnoreCase("")
                && !ontologySearch.getWater().equalsIgnoreCase("") && !ontologySearch.getTemp().equalsIgnoreCase("") && !ontologySearch.getHumidity().equalsIgnoreCase("")
                && !ontologySearch.getFert().equalsIgnoreCase("") && plantsFiltered.size() > 0) {
            fSoil.setText(ontologySearch.getSoil());
            fSoilPH.setText(ontologySearch.getSoilPH());
            fSun.setText(ontologySearch.getSunExposure());
            fwater.setText(ontologySearch.getWater());
            fTemp.setText(ontologySearch.getTemp());
            fHumi.setText(ontologySearch.getHumidity());
            fFert.setText(ontologySearch.getFert());

            List<Plant> temp = new ArrayList<>();
            for (int i = 0; i < plantsFiltered.size(); i++) {
                if (plantsFiltered.get(i).getSoil().contains(ontologySearch.getSoil()) && plantsFiltered.get(i).getSoilPH().contains(ontologySearch.getSoilPH()) &&
                        plantsFiltered.get(i).getSunExposure().contains(ontologySearch.getSunExposure()) && plantsFiltered.get(i).getWater().contains(ontologySearch.getWater()) &&
                        plantsFiltered.get(i).getTemperature().contains(ontologySearch.getTemp()) && plantsFiltered.get(i).getHumidity().contains(ontologySearch.getHumidity()) &&
                        plantsFiltered.get(i).getFertilizer().contains(ontologySearch.getFert())) {
                    temp.add(plantsFiltered.get(i));
                }
            }
            System.out.println("pass 9");
            plantsFiltered = new ArrayList<>(temp);
        } else {
            linearPlantCare.setVisibility(v.GONE);
        }
        if (plantsFiltered.size() > 0) {
            for (Plant plant : plantsFiltered) {
                getPlantImage(plant);
            }
        }
        System.out.println("plantlist length = " + plantsFiltered.size());
    }

    private void getPlantImage(Plant plant) {
        StorageReference ref = storage.getReference()
                .child(FirebaseLocal.storagePathForImageUpload + plant.getOwner() + "/" + plant.getType().split(",")[0].toLowerCase() + "s/" + plant.getName());
        try {
            final File localFile = File.createTempFile(plant.getName(), "jpg");
            ref.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        System.out.println("downloaded image");
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), bitmap, 0, plant.getTreatments());
                        plantList.add(plantListView);
                    })
                    .addOnFailureListener(e -> System.out.println(e))
                    .addOnCompleteListener(task -> initAdapter());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void initElement(View v) {
        fBotanicalHabit = v.findViewById(R.id.ontology_filtered_botanical_habit);
        fFamily = v.findViewById(R.id.ontology_filtered_family);
        fPlantType = v.findViewById(R.id.ontology_filtered_plant_type_topic);
        fPlantTypeValue = v.findViewById(R.id.ontology_filtered_plant_type_value);
        fSeason = v.findViewById(R.id.ontology_filtered_season);
        fVitamin = v.findViewById(R.id.ontology_filtered_vitamin);
        fMineral = v.findViewById(R.id.ontology_filtered_mineral);
        fPlanting = v.findViewById(R.id.ontology_filtered_planting);
        fSoil = v.findViewById(R.id.ontology_filtered_soil);
        fSoilPH = v.findViewById(R.id.ontology_filtered_soilPH);
        fSun = v.findViewById(R.id.ontology_filtered_sun);
        fwater = v.findViewById(R.id.ontology_filtered_water);
        fTemp = v.findViewById(R.id.ontology_filtered_temp);
        fHumi = v.findViewById(R.id.ontology_filtered_humi);
        fFert = v.findViewById(R.id.ontology_filtered_fert);
        plant_name_view = v.findViewById(R.id.ontology_filtered_plant_name);
        fTreatment = v.findViewById(R.id.ontology_filtered_treatment);

        plant_name_view.setText(ontologySearch.getName());

        listViewOfFiltered = v.findViewById(R.id.ontology_filtered_list_view);

        linearFamily = v.findViewById(R.id.ontology_filtered_linear_family);
        linearSeason = v.findViewById(R.id.ontology_filtered_linear_season);
        linearNutrient = v.findViewById(R.id.ontology_filtered_linear_nutrient);
        linearPlanting = v.findViewById(R.id.ontology_filtered_linear_planting);
        linearPlantCare = v.findViewById(R.id.ontology_filtered_linear_plant_care);
        linearBotanical = v.findViewById(R.id.ontology_filtered_linear_botanical_habit);
        linearType = v.findViewById(R.id.ontology_filtered_linear_plant_type);
        linearTreatment = v.findViewById(R.id.ontology_filtered_linear_treatment);

        storage = FirebaseStorage.getInstance();
    }

    public void setAllPlants(List<Plant> plantsList) {
        this.plantsList = new ArrayList<>(plantsList);
        this.plantsFiltered = plantsList;
    }

    public void setOntologySearch(OntologySearch ontologySearch) {
        this.ontologySearch = ontologySearch;
    }
}
