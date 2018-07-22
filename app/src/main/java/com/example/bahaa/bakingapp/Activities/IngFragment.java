package com.example.bahaa.bakingapp.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bahaa.bakingapp.Adapters.IngAdapter;
import com.example.bahaa.bakingapp.Models.IngredientsModel;
import com.example.bahaa.bakingapp.R;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class IngFragment extends Fragment {
    private String mainStr;
    public static ArrayList<IngredientsModel> ingList;
    private RecyclerView recyclerView;
    private IngAdapter ingAdapter;
    private LinearLayoutManager linearLayoutManager;



    public IngFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static IngFragment newInstance(String param1, String param2) {

        return new IngFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_ing, container, false);

       ingList = new ArrayList<>();


        mainStr = getArguments().getString("mainStr");
        Log.i("stepsStr", mainStr);
        recyclerView = (RecyclerView)v.findViewById(R.id.ing_rv);
        ingAdapter = new IngAdapter(getActivity(), ingList);
        recyclerView.setAdapter(ingAdapter);
        //Log.i("stepsCount", String.valueOf(stepsList.size()));

        loadIngredients();


        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);




        return v;
    }

    private void loadIngredients(){
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
            JSONArray stepsArray = mainObj.getJSONArray("ingredients");

            for (int k = 0; k < stepsArray.length(); k++){
                IngredientsModel ingredientsModel = gson.fromJson(stepsArray.get(k).toString(), IngredientsModel.class);

                ingList.add(ingredientsModel);

                Log.i("description", ingredientsModel.getIngredient());
            }
            ingAdapter.notifyDataSetChanged();

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


}
