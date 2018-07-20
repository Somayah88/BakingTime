package com.somayahalharbi.bakingapp.ui;


import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.adapters.IngredientsAdapter;
import com.somayahalharbi.bakingapp.adapters.StepsAdapter;
import com.somayahalharbi.bakingapp.models.Ingredient;
import com.somayahalharbi.bakingapp.models.Recipe;
import com.somayahalharbi.bakingapp.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements StepsAdapter.StepAdapterOnClickHandler {
    private final String INGREDIENTS_LIST_EXTRA = "ingredients_list";
    private final String STEPS_LIST_EXTRA = "steps_list";
    private final String STEPS_LIST = "steps_list";
    private final String CURRENT_STEP_INDEX = "step_index";
    private final String TWO_PANE = "two_pane";
    private final int defaultPosition = 0;
    @BindView(R.id.ingredients_recyclerview)
    RecyclerView mIngredientRecyclerView;
    @BindView(R.id.step_recyclerview)
    RecyclerView mStepsRecyclerView;
    @BindView(R.id.recipe_name_tv)
    TextView mRecipeNameTv;
    private StepsAdapter mStepsAdapter;
    private IngredientsAdapter mIngredientAdapter;
    private ArrayList<Step> mStepsList = new ArrayList<>();
    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    private int currentPosition;
    private boolean twoPane;
    private Fragment mStepDetailFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INGREDIENTS_LIST_EXTRA)) {
                mIngredientList = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST_EXTRA);
                mStepsList = savedInstanceState.getParcelableArrayList(STEPS_LIST_EXTRA);
            }
        }
        mStepDetailFragment = new StepDetailFragment();

        if (isTablet()) {
            twoPane = true;
            Log.v("DetailsActivity", "Device is tablet");
            if (currentPosition >= 0)
                createFragment(currentPosition);
            else
                createFragment(defaultPosition);
        } else
            twoPane = false;
        //********* Get Data from Intent ***********
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            }
            final Bundle recipeData = getIntent().getExtras();
            if (recipeData != null) {
                Recipe recipe = recipeData.getParcelable("recipe");
                if (recipe != null) {
                    mIngredientList = recipe.getIngredients();
                    mStepsList = recipe.getSteps();

                } else {
                    closeOnError();
                }

            } else {
                closeOnError();
            }
        }

        //**********  Populate INGREDIENT RecyclerView with data ************

        mIngredientRecyclerView.setHasFixedSize(true);
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mIngredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        mIngredientAdapter = new IngredientsAdapter();
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        //*********** populate Steps RecyclerView with Data ************

        mStepsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
        mStepsAdapter = new StepsAdapter(this);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mIngredientAdapter.setData(mIngredientList);
        mStepsAdapter.setData(mStepsList);

    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(INGREDIENTS_LIST_EXTRA, mIngredientList);
        outState.putParcelableArrayList(STEPS_LIST_EXTRA, mStepsList);
        outState.putInt(CURRENT_STEP_INDEX, currentPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(int position) {
        //TODO: check for tablet and open fragment
        currentPosition = position;

        if (twoPane) {
            createFragment(currentPosition);
        } else {
            Context context = DetailsActivity.this;
            Class destinationClass = StepsDetailsActivity.class;
            Intent intentToStartStepsActivity = new Intent(context, destinationClass);
            intentToStartStepsActivity.putParcelableArrayListExtra(STEPS_LIST, mStepsList);
            intentToStartStepsActivity.putExtra(CURRENT_STEP_INDEX, position);
            startActivity(intentToStartStepsActivity);
        }

    }

    private void createFragment(int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_LIST_EXTRA, mStepsList);
        bundle.putInt(CURRENT_STEP_INDEX, position);
        bundle.putBoolean(TWO_PANE, twoPane);
        mStepDetailFragment.setArguments(bundle);
        FragmentManager stepsFragmentManager = getSupportFragmentManager();
        stepsFragmentManager.beginTransaction().add(R.id.steps_container, mStepDetailFragment).commit();


    }

    public boolean isTablet() {
        return (DetailsActivity.this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void getFragmentByScreenSize(int index) {

        assert (this.getSystemService(Context.WINDOW_SERVICE)) != null;
        assert this.getSystemService(Context.WINDOW_SERVICE) != null;
        final int rotation = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            //this if not rotated
            case Surface.ROTATION_0:
                //but sure if it is tablet will make mTwoPane true and initializeFragment
                if (isTablet()) {
                    createFragment(index);

                    twoPane = true;
                }
                break;

            //this if rotated 90 make mTwoPane true and initializeFragment
            case Surface.ROTATION_90:
                createFragment(index);

                twoPane = true;
                break;

            //this if rotated 180 make mTwoPane true and initializeFragment
            case Surface.ROTATION_180:
                createFragment(index);

                twoPane = true;
                break;
        }
    }

}
