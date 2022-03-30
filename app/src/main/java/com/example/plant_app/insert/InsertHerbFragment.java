package com.example.plant_app.insert;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.plant_app.R;

public class InsertHerbFragment extends Fragment {

    Spinner botanical;
    Spinner vegetable;

    String[] items1 = new String[]{
            "-- Botanical habit type --", "Herbaceous stem", "Perennial plant", "Shrub", "Climber", "Scandent"
    };

    public InsertHerbFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_insert_herb, container, false);

        botanical = v.findViewById(R.id.in_fruit_bot_habit);
        botanical.setDropDownVerticalOffset(100);
        InitSpinner.setInitSpinner(items1, botanical, getActivity());

        return v;
    }
}