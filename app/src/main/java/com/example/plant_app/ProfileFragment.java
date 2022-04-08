package com.example.plant_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_app.detail.PlantDetailFragment;
import com.example.plant_app.detail.RecyclerItemClickListener;
import com.example.plant_app.firebase.FirebaseLocal;
import com.example.plant_app.firebase.Plant;
import com.example.plant_app.firebase.PlantListView;
import com.example.plant_app.profile.EditProfileFragment;
import com.example.plant_app.profile.FavouritePageFragment;
import com.example.plant_app.profile.ImageAdapter;
import com.example.plant_app.search.PlantListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ImageView iconList;
    private String userId;
    private TextView helloUser;
    private RecyclerView vegetableRecycle;
    private RecyclerView fruitRecycle;
    private RecyclerView herbRecycle;
    ImageAdapter vegetableAdapter;
    ImageAdapter fruitAdapter;
    ImageAdapter herbAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference, ref;
    private FirebaseFirestore db;

    String[] plantName = new String[] {
            "unknown", "carrot","coriander","cabbage","lettuce","broccoli","madras thorn","bilimbi","santol","pomegranate","salak","pineapple"
            ,"holy basil","roselle","galanga","gotu kola","tamarind","java tea","aloe","andrographis"
    };
    int[] plantImg = new int[]{
            R.drawable.logo, R.drawable.carrot, R.drawable.coriander, R.drawable.cabbage, R.drawable.lettuce, R.drawable.brocoli, R.drawable.madras_thorn, R.drawable.bilimbi,
            R.drawable.santol, R.drawable.pomegranate, R.drawable.salak, R.drawable.pineapple, R.drawable.holy_basil, R.drawable.roselle, R.drawable.galanga,
            R.drawable.gotu_kola, R.drawable.tamarind, R.drawable.java_tea, R.drawable.aloe, R.drawable.andrographis
    };
    List<PlantListView> profileVegetables = new ArrayList<>();
    List<PlantListView> profileFruits = new ArrayList<>();
    List<PlantListView> profileHerbs = new ArrayList<>();
    private ArrayList<Plant> plantsList = new ArrayList<>();
    LinearLayoutManager mLayoutManager, mLayoutManager1, mLayoutManager2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        initElement(v);

        initUser(userId);
        getAllPlant(v);
        getAllPlants();

        iconList.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(), view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup_profile, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.profile_popup_edit:
                        replaceFragment(new EditProfileFragment());
                        return true;
                    case R.id.profile_popup_fav:
                        replaceFragment(new FavouritePageFragment());
                        return true;
                    case R.id.profile_popup_regis:
                        return true;
                    case R.id.profile_popup_logout:
                        FirebaseLocal.logout();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });

        return v;
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

    private void getAllPlant(View v) {
        db.collection(userId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<PlantListView> pVege = new ArrayList<>();
                        List<PlantListView> pFruit = new ArrayList<>();
                        List<PlantListView> pHerb = new ArrayList<>();
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
                            PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index], 0);
                            if (plant.getType().equalsIgnoreCase("FRUIT")) {
                                pFruit.add(plantListView);
                            } else if (plant.getType().equalsIgnoreCase("VEGETABLE")) {
                                pVege.add(plantListView);
                            } else if (plant.getType().equalsIgnoreCase("HERB")) {
                                pHerb.add(plantListView);
                            }
                        }
                        System.out.println("size 3 array list = " + pVege.size() + " " + pFruit.size() + " " + pHerb.size());
                        if (pVege.size() > 0) {
                            this.profileVegetables = new ArrayList<>(pVege);
                        } else {
                            vegetableRecycle.setVisibility(v.GONE);
                        }
                        if (pFruit.size() > 0) {
                            this.profileFruits = new ArrayList<>(pFruit);
                        } else {
                            fruitRecycle.setVisibility(v.GONE);
                        }
                        if (pHerb.size() > 0) {
                            this.profileHerbs = new ArrayList<>(pHerb);
                        } else {
                            herbRecycle.setVisibility(v.GONE);
                        }
                        initAdapter(v);
                    }
                }).addOnFailureListener(e -> Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                        .show());
    }

    private void initElement(View v) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        if (firebaseUser == null) {
            navigateToMain();
        }

        iconList = v.findViewById(R.id.profile_list_icon);
        helloUser = v.findViewById(R.id.profile_user_name);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        vegetableRecycle = v.findViewById(R.id.profile_vegetable_recycle);

        mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        fruitRecycle = v.findViewById(R.id.profile_fruit_recycle);

        mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        herbRecycle = v.findViewById(R.id.profile_herb_recycle);
    }

    private void navigateToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
    }

    private void initAdapter(View v) {

        try {
            if (profileVegetables.size() > 0) {

                vegetableAdapter = new ImageAdapter(profileVegetables);
                vegetableRecycle.setLayoutManager(mLayoutManager);
                vegetableRecycle.setItemAnimator(new DefaultItemAnimator());
                vegetableRecycle.setAdapter(vegetableAdapter);
                vegetableRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), vegetableRecycle, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        viewPlantDetail(profileVegetables.get(position).getName());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            if (profileFruits.size() > 0) {

                fruitAdapter = new ImageAdapter(profileFruits);
                fruitRecycle.setLayoutManager(mLayoutManager1);
                fruitRecycle.setItemAnimator(new DefaultItemAnimator());
                fruitRecycle.setAdapter(fruitAdapter);
                fruitRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), fruitRecycle, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        viewPlantDetail(profileFruits.get(position).getName());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            if (profileHerbs.size() > 0) {
                herbAdapter = new ImageAdapter(profileHerbs);
                herbRecycle.setLayoutManager(mLayoutManager2);
                herbRecycle.setItemAnimator(new DefaultItemAnimator());
                herbRecycle.setAdapter(herbAdapter);
                herbRecycle.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), herbRecycle, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        viewPlantDetail(profileHerbs.get(position).getName());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getAllPlants() {
        db.collection("VEGETABLE").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot plants: queryDocumentSnapshots) {
                            Plant plant = plants.toObject(Plant.class);
                            plantsList.add(plant);
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
                            plantsList.add(plant);
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
                            plantsList.add(plant);
                        }
                    }
                }).addOnFailureListener(e -> Toast
                .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                .show());
    }

    private void initUser(String id) {
        DocumentReference db = FirebaseFirestore.getInstance().collection("User").document(id);
        db.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            String fullname = "Hello,\n" + document.getString("firstname") + " " + document.getString("lastname");
                            helloUser.setText(fullname);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrameLayout, fragment);
        fragmentTransaction.commit();
    }
}