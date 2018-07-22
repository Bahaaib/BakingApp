package com.example.bahaa.bakingapp.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bahaa.bakingapp.Adapters.DetailsAdapter;
import com.example.bahaa.bakingapp.Models.StepsModel;
import com.example.bahaa.bakingapp.R;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetailsFragment extends Fragment {

    private Button ingButton;
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String mainStr;
    private ArrayList<StepsModel> stepsList;

    public DetailsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance() {

        return new DetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        mainStr = getArguments().getString("mainStr");
        Log.i("stepsStr", mainStr);
        stepsList = new ArrayList<>();


        ingButton = (Button) v.findViewById(R.id.ing_btn);

        recyclerView = (RecyclerView) v.findViewById(R.id.details_rv);
        detailsAdapter = new DetailsAdapter(getActivity(), stepsList);
        recyclerView.setAdapter(detailsAdapter);
        //Log.i("stepsCount", String.valueOf(stepsList.size()));

        loadSteps();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        ingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("mainStr", mainStr);

                IngFragment ingFragment = new IngFragment();
                ingFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, ingFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        return v;
    }

    private void loadSteps() {
        // Initialize Gson and start new transaction
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();

        try {
            JSONObject mainObj = new JSONObject(mainStr);
            JSONArray stepsArray = mainObj.getJSONArray("steps");

            for (int k = 0; k < stepsArray.length(); k++) {
                StepsModel stepsModel = gson.fromJson(stepsArray.get(k).toString(), StepsModel.class);

                stepsList.add(stepsModel);

                Log.i("description", stepsModel.getDescription());
            }
            detailsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
