package com.example.bahaa.bakingapp.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bahaa.bakingapp.Adapters.RecipeAdapter;
import com.example.bahaa.bakingapp.IdlingResource.BakingIdlingResource;
import com.example.bahaa.bakingapp.Models.RecipesModel;
import com.example.bahaa.bakingapp.R;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    public static ArrayList<RecipesModel> recipesList;
    public static ArrayList<String> strList;


    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    LinearLayoutManager linearLayoutManager;
    private String API_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    @Nullable
    private BakingIdlingResource mIdlingResource;
    @VisibleForTesting
    @NonNull
    public BakingIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new BakingIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(getBaseContext());
        recipesList = new ArrayList<>();
        strList = new ArrayList<>();

        loadRecipes();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recipeAdapter = new RecipeAdapter(this, recipesList);
        recyclerView.setAdapter(recipeAdapter);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        getIdlingResource();
    }

    private void loadRecipes() {

        StringRequest request = new StringRequest(Request.Method.GET, API_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Statuss", "Got Response!");


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

                        Log.i("titles", response);

                        try {
                            JSONArray mainArray = new JSONArray(response);



                            for (int i = 0; i < mainArray.length(); i++) {
                                String mainStr = mainArray.get(i).toString();
                                strList.add(mainStr);
                                RecipesModel model = gson.fromJson(mainStr, RecipesModel.class);


                                Log.i("titles", model.getName());


                                recipesList.add(model);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Notify the RecyclerView with the new data..
                        recipeAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener()


        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        requestQueue.add(request);

    }
}
