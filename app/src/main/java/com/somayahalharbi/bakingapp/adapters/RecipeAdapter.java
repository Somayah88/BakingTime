package com.somayahalharbi.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private final RecipeAdapterOnClickHandler mClickHandler;
    private ArrayList<Recipe> recipesList = new ArrayList<>();


    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        //String img=recipesList.get(position).getImage();
        String title = recipesList.get(position).getName();
        int serving = recipesList.get(position).getServings();
        holder.recipeTV.setText(title);
        holder.servingTv.setText(String.valueOf(serving));


    }

    @Override
    public int getItemCount() {
        if (recipesList.isEmpty())
            return 0;
        return recipesList.size();
    }

    public void setRecipesData(ArrayList<Recipe> recipes) {
        clear();
        recipesList = recipes;
        notifyDataSetChanged();
    }

    public void clear() {
        final int size = recipesList.size();
        recipesList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);

    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_name)
        TextView recipeTV;
        @BindView(R.id.serving_tv)
        TextView servingTv;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }
    }
}