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

import com.example.bahaa.bakingapp.Activities.MainActivity;
import com.example.bahaa.bakingapp.Activities.PlayerFragment;
import com.example.bahaa.bakingapp.Models.StepsModel;
import com.example.bahaa.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by Bahaa on 3/23/2018.
 */

public class DetailsAdapter extends RecyclerView.Adapter {

    //Here we recieve from the calling Fragment :
    // The cards container List & The Parent Activity context
    private Context context;
    private ArrayList<StepsModel> adapterModel;

    {
        adapterModel = new ArrayList<>();
    }

    public DetailsAdapter(Context context, ArrayList<StepsModel> adapterModel) {
        this.context = context;
        this.adapterModel = adapterModel;
    }

    //Here We tell the RecyclerView what to show at each element of it..it'd be a cardView!
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.details_card, parent, false);
        return new DetailsViewHolder(view);
    }

    //Here We tell the RecyclerView what to show at each CardView..
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DetailsViewHolder) holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return adapterModel.size();
    }

    //Here we bind all the children views of each cardView with their corresponding
    // actions to show & interact with them
    public class DetailsViewHolder extends RecyclerView.ViewHolder {

        TextView shortDesc;


        public DetailsViewHolder(View itemView) {
            super(itemView);
            shortDesc = (TextView) itemView.findViewById(R.id.short_desc);

        }


        //Here where all the glory being made..!
        public void BindView(final int position) {


            shortDesc.setText(adapterModel.get(position).getShortDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("stepsObj", adapterModel.get(position));

                    PlayerFragment playerFragment = new PlayerFragment();
                    playerFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, playerFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });



        }


    }
}
