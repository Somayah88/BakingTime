package com.somayahalharbi.bakingapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        final Bundle recipeData = getIntent().getExtras();
        if (recipeData != null) {
            Recipe recipe = recipeData.getParcelable("recipe");
            if (recipe != null) {
                RecipeDetailListFragment recipeListFragment = new RecipeDetailListFragment();
                recipeListFragment.setIngredientList(recipe.getIngredients());
                recipeListFragment.setStepsList(recipe.getSteps());
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.ingredient_steps_container, recipeListFragment)
                        .commit();


            } else {
                closeOnError();
            }

        } else {
            closeOnError();
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error, Toast.LENGTH_SHORT).show();
    }

}
