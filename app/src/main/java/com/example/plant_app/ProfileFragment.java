package com.example.plant_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
    private ImageAdapter vegetableAdapter;
    private ImageAdapter herbAdapter;
    private ImageAdapter fruitAdapter;
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
    List<PlantListView> vegetables;
    List<PlantListView> fruits;
    List<PlantListView> herbs;

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
        getAllPlant();
//        initAdapter(v);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        vegetableRecycle = v.findViewById(R.id.profile_vegetable_recycle);
        vegetableAdapter = new ImageAdapter(vegetables);
        vegetableRecycle.setLayoutManager(mLayoutManager);
        vegetableRecycle.setItemAnimator(new DefaultItemAnimator());
        vegetableRecycle.setAdapter(vegetableAdapter);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        fruitRecycle = v.findViewById(R.id.profile_fruit_recycle);
        fruitAdapter = new ImageAdapter(fruits);
        fruitRecycle.setLayoutManager(mLayoutManager1);
        fruitRecycle.setItemAnimator(new DefaultItemAnimator());
        fruitRecycle.setAdapter(fruitAdapter);

        System.out.println("size = " + vegetables.size());
        System.out.println("size = " + fruits.size());

        iconList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_profile, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
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
                    }
                });
                popupMenu.show();
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
//                                PlantListView plantListView = new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index]);
                                if (plant.getType().equalsIgnoreCase("FRUIT")) {
//                                    System.out.println("Hello fruit " + plantListView);
                                    fruits.add(new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index]));
                                } else if (plant.getType().equalsIgnoreCase("VEGETABLE")) {
//                                    System.out.println("Hello vegetable " + plantListView);
                                    vegetables.add(new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index]));
                                } else if (plant.getType().equalsIgnoreCase("HERB")) {
//                                    System.out.println("Hello herb " + plantListView);
                                    herbs.add(new PlantListView(plant.getName(), plant.getScienceName(), plant.getType(), plantImg[index]));
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
                Toast
                        .makeText(getActivity(), Html.fromHtml("<font color='#FE0000' ><b>Cannot find plant!</b></font>"), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void initElement(View v) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        iconList = v.findViewById(R.id.profile_list_icon);
        helloUser = v.findViewById(R.id.profile_user_name);

        vegetables = new ArrayList<>();
        fruits = new ArrayList<>();
        herbs = new ArrayList<>();
    }

    private void initAdapter(View v) {
        System.out.println("adapter ================");
        for (PlantListView pl : vegetables) {
            System.out.println(pl);
        }
        System.out.println("End ===========");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        vegetableRecycle = v.findViewById(R.id.profile_vegetable_recycle);
        vegetableAdapter = new ImageAdapter(vegetables);
        vegetableRecycle.setLayoutManager(mLayoutManager);
        vegetableRecycle.setItemAnimator(new DefaultItemAnimator());
        vegetableRecycle.setAdapter(vegetableAdapter);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        fruitRecycle = v.findViewById(R.id.profile_fruit_recycle);
        fruitAdapter = new ImageAdapter(fruits);
        fruitRecycle.setLayoutManager(mLayoutManager1);
        fruitRecycle.setItemAnimator(new DefaultItemAnimator());
        fruitRecycle.setAdapter(fruitAdapter);
    }

    private void initUser(String id) {
        DocumentReference db = FirebaseFirestore.getInstance().collection("User").document(id);
        db.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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