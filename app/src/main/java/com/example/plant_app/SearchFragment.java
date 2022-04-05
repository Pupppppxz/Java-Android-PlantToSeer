package com.example.plant_app;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantListView;
import com.example.plant_app.insert.InitSpinner;
import com.example.plant_app.search.PlantListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    String[] items1 = new String[]{
            "All", "Plants", "    Vegetable", "    Fruit", "    Herb", "Symptoms", "Disease"
    };
    String[] plantName = new String[] {
            "unknown", "carrot","coriander","cabbage","lettuce","broccoli","madras thorn","bilimbi","santol","pomegranate","salak","pineapple"
            ,"holy basil","roselle","galanga","gotu kola","tamarind","java tea","aloe","andrographis"
    };
    int[] plantImg = new int[]{
        R.drawable.logo, R.drawable.carrot, R.drawable.coriander, R.drawable.cabbage, R.drawable.lettuce, R.drawable.brocoli, R.drawable.madras_thorn, R.drawable.bilimbi,
            R.drawable.santol, R.drawable.pomegranate, R.drawable.salak, R.drawable.pineapple, R.drawable.holy_basil, R.drawable.roselle, R.drawable.galanga,
            R.drawable.gotu_kola, R.drawable.tamarind, R.drawable.java_tea, R.drawable.aloe, R.drawable.andrographis
    };
    ArrayList<PlantListView> plantList = new ArrayList<>();
    ArrayList<PlantListView> plantList1;

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
        getAllPlant();

        adapter = new PlantListAdapter(getActivity(), R.layout.list_view_map_item, plantList);
        searchPlant.setAdapter(adapter);

        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    ArrayList<PlantListView> pl = new ArrayList<>();
                    String filter1 = searchSpinner.getSelectedItem().toString().trim();
                    System.out.println("filter = " + filter1);
                    for (int j = 0; j < plantList.size(); j++) {
                        System.out.println(plantList.get(j).getType());
                        if (filter1 == "All" || filter1 == "Symptoms" || filter1 == "Disease") {
                            if (plantList.get(j).getName().toLowerCase().contains(charSequence) ||
                                    plantList.get(j).getSciName().toLowerCase().contains(charSequence)) {
                                pl.add(plantList.get(j));
                            }
                        } else {
                            if ((plantList.get(j).getName().toLowerCase().contains(charSequence) ||
                                    plantList.get(j).getSciName().toLowerCase().contains(charSequence)) &&
                            plantList.get(j).getType().toLowerCase().contains(filter1.toLowerCase())) {
                                pl.add(plantList.get(j));
                            }
                        }
                    }
                    setNewAdapter(pl);
                } else {
                    setNewAdapter(plantList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    private void getAllPlant() {
        db.collection(userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
                                PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index]);
                                plantList.add(plantListView);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast
                                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                                .show();
                    }
        });
    }

    private void downloadFile(Context context, String fileName, String fileExtension, File destination, String url) {
        System.out.println("url = " + url);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, "" + destination, fileName + fileExtension);

        downloadManager.enqueue(request);
    }

    private void initElement(View v) {
        searchPlant = v.findViewById(R.id.search_plant_map);
        searchSpinner = v.findViewById(R.id.spinner_search_page);
        textSearch = v.findViewById(R.id.search_text_field);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        plantList1 = new ArrayList<>(plantList);

        searchSpinner.setDropDownVerticalOffset(100);
        InitSpinner.setInitSpinner(items1, searchSpinner, getActivity());
    }

    private void setNewAdapter(ArrayList<PlantListView> pl) {
        PlantListAdapter adapter1 = new PlantListAdapter(getActivity(), R.layout.list_view_map_item, pl);
        searchPlant.setAdapter(adapter1);
    }
}