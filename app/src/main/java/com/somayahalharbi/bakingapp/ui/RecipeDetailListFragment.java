package com.somayahalharbi.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.adapters.IngredientsAdapter;
import com.somayahalharbi.bakingapp.adapters.StepsAdapter;
import com.somayahalharbi.bakingapp.models.Ingredient;
import com.somayahalharbi.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailListFragment extends Fragment implements StepsAdapter.StepAdapterOnClickHandler {


    @BindView(R.id.steps_recyclerView)
    RecyclerView stepsRV;
    @BindView(R.id.ingredients_recyclerView)
    RecyclerView ingredientRV;
    private List<Step> stepsList = new ArrayList<>();
    private List<Ingredient> ingredientList = new ArrayList<>();
    private StepsAdapter stepsAdapter;
    private IngredientsAdapter ingredientsAdapter;

    public RecipeDetailListFragment() {

    }


    @Override
    public void onClick(int position) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list_detail, container, false);
        ButterKnife.bind(this, rootView);

        GridLayoutManager ingredientsLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        ingredientRV.setLayoutManager(ingredientsLayoutManager);
        ingredientsAdapter = new IngredientsAdapter();
        ingredientRV.setAdapter(ingredientsAdapter);
        ingredientsAdapter.setData(ingredientList);
        GridLayoutManager stepsLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.VERTICAL, false);
        stepsRV.setLayoutManager(stepsLayoutManager);
        stepsAdapter = new StepsAdapter(this);
        stepsRV.setAdapter(stepsAdapter);
        stepsAdapter.setData(stepsList);

        return rootView;

    }

    public void setStepsList(List<Step> stepsList) {
        this.stepsList = stepsList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
