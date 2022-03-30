package com.example.plant_app.insert;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.example.plant_app.insert.InitSpinner;

import com.example.plant_app.R;

public class InsertVegetableFragment extends Fragment {

    private static final String KEY_User_Documents = "doc1";
    ImageButton btnImageInsertVeg;
    Spinner botanical;
    Spinner vegetable;

    String[] items1 = new String[]{
            "-- Botanical habit type --", "Herbaceous stem", "Perennial plant", "Shrub", "Climber", "Scandent"
    };

    String[] items2 = new String[]{
            "-- Vegetable type --", "Bulb", "Leaf", "Pod", "Flower"
    };

    public InsertVegetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_insert_vegetable, container, false);

        botanical = v.findViewById(R.id.in_veg_bot_habit);
        botanical.setDropDownVerticalOffset(100);
        InitSpinner.setInitSpinner(items1, botanical, getActivity());

        vegetable = v.findViewById(R.id.in_veg_bot_classi);
        vegetable.setDropDownVerticalOffset(100);
        InitSpinner.setInitSpinner(items2, vegetable, getActivity());

        btnImageInsertVeg = v.findViewById(R.id.btnImageInsertVeg);
        btnImageInsertVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }

}