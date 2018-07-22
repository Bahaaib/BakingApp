package com.example.bahaa.bakingapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bahaa.bakingapp.Activities.DetailsFragment;
import com.example.bahaa.bakingapp.Activities.MainActivity;
import com.example.bahaa.bakingapp.Models.RecipesModel;
import com.example.bahaa.bakingapp.R;

import java.util.ArrayList;

import static com.example.bahaa.bakingapp.Activities.MainActivity.strList;

/**
 * Created by Bahaa on 3/23/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter {

    //Here we recieve from the calling Fragment :
    // The cards container List & The Parent Activity context
    private Context context;
    private ArrayList<RecipesModel> adapterModel;

    {
        adapterModel = new ArrayList<>();
    }

    public RecipeAdapter(Context context, ArrayList<RecipesModel> adapterModel) {
        this.context = context;
        this.adapterModel = adapterModel;
    }

    //Here We tell the RecyclerView what to show at each element of it..it'd be a cardView!
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    //Here We tell the RecyclerView what to show at each CardView..
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecipeViewHolder) holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return adapterModel.size();
    }

    //Here we bind all the children views of each cardView with their corresponding
    // actions to show & interact with them
    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView recipeName;


        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_name);

        }


        //Here where all the glory being made..!
        public void BindView(final int position) {

            recipeName.setText(adapterModel.get(position).getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mainStr", strList.get(position));

                    DetailsFragment detailsFragment = new DetailsFragment();
                    detailsFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, detailsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


        }


    }
}
