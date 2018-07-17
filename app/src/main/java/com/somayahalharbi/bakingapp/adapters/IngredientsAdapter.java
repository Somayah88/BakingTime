package com.somayahalharbi.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private List<Ingredient> ingredientList = new ArrayList<>();

    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);


        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        String measurement = ingredientList.get(position).getMeasure();
        String name = ingredientList.get(position).getIngredient();
        double quantity = ingredientList.get(position).getQuantity();
        holder.ingrediantName.setText(name);
        holder.measurement.setText(measurement);
        holder.quantity.setText(String.valueOf(quantity));


    }

    @Override
    public int getItemCount() {

        if (ingredientList.size() == 0)
            return 0;
        return ingredientList.size();
    }

    public void setData(List<Ingredient> ingredients) {
        clear();
        ingredientList = ingredients;
        notifyDataSetChanged();

    }

    public void clear() {

        final int size = ingredientList.size();
        ingredientList.clear();
        notifyItemRangeRemoved(0, size);

    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_name)
        TextView ingrediantName;
        @BindView(R.id.measurement)
        TextView measurement;
        @BindView(R.id.quantity)
        TextView quantity;


        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
