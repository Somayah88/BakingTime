package com.somayahalharbi.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.Utils.ApiService;
import com.somayahalharbi.bakingapp.Utils.DeviceConfig;
import com.somayahalharbi.bakingapp.Utils.NetworkUtilities;
import com.somayahalharbi.bakingapp.Utils.SharedPref;
import com.somayahalharbi.bakingapp.adapters.RecipeAdapter;
import com.somayahalharbi.bakingapp.idling.EspressoIdlingResource;
import com.somayahalharbi.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {
    private static final String TAG = "MainActivity";
    //****************************************
    @BindView(R.id.recipe_recycler_view)
    RecyclerView mRecipeRecyclerView;
    //***************************************
    private List<Recipe> recipes = new ArrayList<>();
    private RecipeAdapter mRecipeAdapter;
    //****************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager;
        fetchData();

        if (DeviceConfig.isTablet(this)) {
            gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        } else {
            gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        }


        mRecipeRecyclerView.setLayoutManager(gridLayoutManager);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);


    }

    private void fetchData() {
        EspressoIdlingResource.increment();

        ApiService api = NetworkUtilities.getRetrofitInstance().create(ApiService.class);
        Call<List<Recipe>> call = api.getMyJSON();


        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (response.isSuccessful()) {


                    recipes = response.body();
                    if (recipes.size() > 0) {
                        mRecipeAdapter.setRecipesData((ArrayList<Recipe>) recipes);
                        EspressoIdlingResource.decrement(); // Tells Espresso test to resume


                    } else {
                        //TODO: show error message

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.v(TAG, "call failed" + t.toString());


            }
        });
    }

    @Override
    public void onClick(Recipe recipe) {
        Context context = this;
        Class destinationClass = DetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("recipe", recipe);
        // ******* Update data in shared preference for the widget**********
        SharedPref pref = new SharedPref();
        pref.setPrefData(context, recipe);
        //*******************************************************************
        startActivity(intentToStartDetailActivity);

    }

}