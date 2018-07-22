package com.example.bahaa.bakingapp.Adapters;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bahaa.bakingapp.Models.IngredientsModel;
import com.example.bahaa.bakingapp.R;
import com.example.bahaa.bakingapp.Widgets.UpdateIngService;

import java.util.ArrayList;

/**
 * Created by Bahaa on 3/23/2018.
 */

public class IngAdapter extends RecyclerView.Adapter {

    //Here we recieve from the calling Fragment :
    // The cards container List & The Parent Activity context
    private Context context;
    private ArrayList<IngredientsModel> adapterModel;
    private ArrayList<String> ingStrArray, measureStrArray;

    {
        adapterModel = new ArrayList<>();
        ingStrArray = new ArrayList<>();
        measureStrArray = new ArrayList<>();
    }

    public IngAdapter(Context context, ArrayList<IngredientsModel> adapterModel) {
        this.context = context;
        this.adapterModel = adapterModel;
    }

    //Here We tell the RecyclerView what to show at each element of it..it'd be a cardView!
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.ingredients_card, parent, false);
        return new IngViewHolder(view);
    }

    //Here We tell the RecyclerView what to show at each CardView..
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((IngViewHolder) holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return adapterModel.size();
    }

    //Here we bind all the children views of each cardView with their corresponding
    // actions to show & interact with them
    public class IngViewHolder extends RecyclerView.ViewHolder {

        TextView quantity, measure, ingredient;


        public IngViewHolder(View itemView) {
            super(itemView);
            quantity = (TextView)itemView.findViewById(R.id.quantity);
            measure = (TextView)itemView.findViewById(R.id.measure);
            ingredient = (TextView)itemView.findViewById(R.id.ingredient);

        }


        //Here where all the glory being made..!
        public void BindView(final int position) {

            quantity.setText(String.valueOf(adapterModel.get(position).getQuantity()));
            measure.setText(adapterModel.get(position).getMeasure());
            ingredient.setText(adapterModel.get(position).getIngredient());

            ingStrArray.add(adapterModel.get(position).getIngredient());
            measureStrArray.add(adapterModel.get(position).getMeasure());

            Log.i("ingStrArray", String.valueOf(getItemCount()));
            //Log.i("measureStrArray", adapterModel.get(position).getMeasure());


            UpdateIngService.startBakingService(context, ingStrArray, measureStrArray);
        }



    }
}
