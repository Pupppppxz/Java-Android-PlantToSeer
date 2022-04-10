package com.example.plant_app;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.plant_app.detail.PlantDetailFragment;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantLiked;
import com.example.plant_app.firebase.PlantListView;
import com.example.plant_app.firebase.User;
import com.example.plant_app.insert.InitSpinner;
import com.example.plant_app.search.PlantListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    private StorageReference storageReference, ref;
    private FirebaseFirestore db;
    private String userId;

    Spinner searchSpinner;
    ListView searchPlant;
    EditText textSearch;
    PlantListAdapter adapter;
    Button editBtn, deleteBtn;
    String[] items1 = new String[]{
            "All", "Plants", "    Vegetable", "    Fruit", "    Herb", "Symptoms", "Disease"
    };
    String[] plantName = new String[] {
            "unknown", "carrot","coriander","cabbage","lettuce","broccoli","madras thorn","bilimbi","santol","pomegranate","salak","pineapple"
            ,"holy basil","roselle","galanga","gotu kola","tamarind","java tea","aloe","andrographis", "amla"
    };
    int[] plantImg = new int[]{
        R.drawable.logo, R.drawable.carrot, R.drawable.coriander, R.drawable.cabbage, R.drawable.lettuce, R.drawable.brocoli, R.drawable.madras_thorn, R.drawable.bilimbi,
            R.drawable.santol, R.drawable.pomegranate, R.drawable.salak, R.drawable.pineapple, R.drawable.holy_basil, R.drawable.roselle, R.drawable.galanga,
            R.drawable.gotu_kola, R.drawable.tamarind, R.drawable.java_tea, R.drawable.aloe, R.drawable.andrographis, R.drawable.amla
    };
    ArrayList<PlantListView> plantList = new ArrayList<>();
    ArrayList<PlantListView> plantList1 = new ArrayList<>();
    ArrayList<Plant> plantsList = new ArrayList<>();
    int count = 0;
    private User user;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        initElement(v);
        initListView();
        getAllPlant();

        System.out.println("size = " + plantList1.size() + " " + plantList.size() + " " + plantsList.size());

        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    plantList1 = new ArrayList<>();
                    String filter1 = searchSpinner.getSelectedItem().toString().trim();
                    System.out.println("filter = " + filter1);
                    for (int j = 0; j < plantList.size(); j++) {
                        System.out.println(plantList.get(j).getType());
                        if (filter1 == "All") {
                            if (plantList.get(j).getName().toLowerCase().contains(charSequence) ||
                                    plantList.get(j).getSciName().toLowerCase().contains(charSequence) ||
                                    plantList.get(j).getTreatments().toLowerCase().contains(charSequence)) {
                                plantList1.add(plantList.get(j));
                            }
                        } else if(filter1 == "Symptoms" || filter1 == "Disease") {
                            if (plantList.get(j).getTreatments().toLowerCase().contains(charSequence)) {
                                plantList1.add(plantList.get(j));
                            }
                        } else {
                            if ((plantList.get(j).getName().toLowerCase().contains(charSequence) ||
                                    plantList.get(j).getSciName().toLowerCase().contains(charSequence)) &&
                                    plantList.get(j).getType().toLowerCase().contains(filter1.toLowerCase())) {
                                plantList1.add(plantList.get(j));
                            }
                        }
                    }
                    setNewAdapter(plantList1);
                } else {
                    setNewAdapter(plantList);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    plantList1 = new ArrayList<>();
                    String filter1 = searchSpinner.getSelectedItem().toString().trim();
                    System.out.println("filter = " + filter1);
                    for (int j = 0; j < plantList.size(); j++) {
                        System.out.println(plantList.get(j).getType());
                        if (filter1 == "All") {
                            if (plantList.get(j).getName().toLowerCase().contains(charSequence) ||
                                    plantList.get(j).getSciName().toLowerCase().contains(charSequence) ||
                                    plantList.get(j).getTreatments().toLowerCase().contains(charSequence)) {
                                plantList1.add(plantList.get(j));
                            }
                        } else if(filter1 == "Symptoms" || filter1 == "Disease") {
                            if (plantList.get(j).getTreatments().toLowerCase().contains(charSequence)) {
                                plantList1.add(plantList.get(j));
                            }
                        } else {
                            if ((plantList.get(j).getName().toLowerCase().contains(charSequence) ||
                                    plantList.get(j).getSciName().toLowerCase().contains(charSequence)) &&
                            plantList.get(j).getType().toLowerCase().contains(filter1.toLowerCase())) {
                                plantList1.add(plantList.get(j));
                            }
                        }
                    }
                    setNewAdapter(plantList1);
                } else {
                    setNewAdapter(plantList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String inp = textSearch.getText().toString();
                plantList1 = new ArrayList<>();
                String filter1 = searchSpinner.getSelectedItem().toString().trim();
                System.out.println("filter = " + filter1);
                for (int j = 0; j < plantList.size(); j++) {
                    System.out.println(plantList.get(j).getType());
                    if (filter1 == "All") {
                        if (plantList.get(j).getName().toLowerCase().contains(inp) ||
                                plantList.get(j).getSciName().toLowerCase().contains(inp) ||
                                plantList.get(j).getTreatments().toLowerCase().contains(inp)) {
                            plantList1.add(plantList.get(j));
                        }
                    } else if(filter1 == "Symptoms" || filter1 == "Disease") {
                        if (plantList.get(j).getTreatments().toLowerCase().contains(inp)) {
                            plantList1.add(plantList.get(j));
                        }
                    } else {
                        if ((plantList.get(j).getName().toLowerCase().contains(inp) ||
                                plantList.get(j).getSciName().toLowerCase().contains(inp)) &&
                                plantList.get(j).getType().toLowerCase().contains(filter1.toLowerCase())) {
                            plantList1.add(plantList.get(j));
                        }
                    }
                }
                setNewAdapter(plantList1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }

    private void initListView() {
        System.out.println("hello user eiei");
        System.out.println(user);
        if (user.getStatus().equalsIgnoreCase("user")) {
            adapter = new PlantListAdapter(getActivity(), R.layout.list_view_map_item, plantList);
            searchPlant.setAdapter(adapter);
            searchPlant.setOnItemClickListener((adapterView, view, i, l) -> viewPlantDetail(plantList1.get(i).getIndex()));
        } else if (user.getStatus().equalsIgnoreCase("admin")) {
            adapter = new PlantListAdapter(getActivity(), R.layout.list_view_map_item_admin, plantList);
            searchPlant.setAdapter(adapter);
            searchPlant.setOnItemClickListener((adapterView, view, i, l) -> viewPlantDetail(plantList1.get(i).getIndex()));
        }
    }

    private void viewPlantDetail(int i) {

        PlantDetailFragment plantDetailFragment = new PlantDetailFragment();
        plantDetailFragment.setPlant(plantsList.get(i));
        plantDetailFragment.setAllPlants(plantsList);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.homeFrameLayout, plantDetailFragment);
        fragmentTransaction.commit();
    }

    private void getAllPlant() {
        db.collection("VEGETABLE").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            System.out.println(plant);
                            int index = 0;
                            for (int i = 0; i < plantName.length; i++) {
                                if (plant.getName().toLowerCase().equals(plantName[i])) {
                                    index = i;
                                    break;
                                }
                            }
                            plantsList.add(plant);
                            PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index], count, plant.getTreatments());
                            count++;
                            plantList.add(plantListView);
                            plantList1.add(plantListView);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                        .show());

        db.collection("FRUIT").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            System.out.println(plant);
                            int index = 0;
                            for (int i = 0; i < plantName.length; i++) {
                                if (plant.getName().toLowerCase().equals(plantName[i])) {
                                    index = i;
                                    break;
                                }
                            }
                            plantsList.add(plant);
                            PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index], count, plant.getTreatments());
                            count++;
                            plantList.add(plantListView);
                            plantList1.add(plantListView);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                        .show());

        db.collection("HERB").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            System.out.println(plant);
                            int index = 0;
                            for (int i = 0; i < plantName.length; i++) {
                                if (plant.getName().toLowerCase().equals(plantName[i])) {
                                    index = i;
                                    break;
                                }
                            }
                            plantsList.add(plant);
                            PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index], count, plant.getTreatments());
                            count++;
                            plantList.add(plantListView);
                            plantList1.add(plantListView);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                        .show());
    }

    private void initElement(View v) {
        searchPlant = v.findViewById(R.id.search_plant_map);
        searchSpinner = v.findViewById(R.id.spinner_search_page);
        textSearch = v.findViewById(R.id.search_text_field);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        storageReference = storage.getReference();

        if (firebaseUser == null) {
            navigateToMain();
        }

        searchSpinner.setDropDownVerticalOffset(100);
        InitSpinner.setInitSpinner(items1, searchSpinner, getActivity());
    }

    private void setNewAdapter(ArrayList<PlantListView> pl) {
        if (user.getStatus().equalsIgnoreCase("user")) {
            PlantListAdapter adapter1 = new PlantListAdapter(getActivity(), R.layout.list_view_map_item, pl);
            searchPlant.setAdapter(adapter1);
        } else if (user.getStatus().equalsIgnoreCase("admin")) {
            PlantListAdapter adapter1 = new PlantListAdapter(getActivity(), R.layout.list_view_map_item_admin, pl);
            searchPlant.setAdapter(adapter1);
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
    }

    public void setUser(User user) {
        this.user = user;
    }
}