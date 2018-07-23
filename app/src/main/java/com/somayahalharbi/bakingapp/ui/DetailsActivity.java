package com.somayahalharbi.bakingapp.ui;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.adapters.IngredientsAdapter;
import com.somayahalharbi.bakingapp.adapters.StepsAdapter;
import com.somayahalharbi.bakingapp.models.Ingredient;
import com.somayahalharbi.bakingapp.models.Recipe;
import com.somayahalharbi.bakingapp.models.Step;
import com.somayahalharbi.bakingapp.widget.BakingAppWidgetProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements StepsAdapter.StepAdapterOnClickHandler, StepDetailFragment.NavigationButtonListener {
    public static final String EXTRA_STEP_FRAGMENT = "step_fragment";
    public final String EXTRA_ROTATION = "extra_is_rotated";
    private final String INGREDIENTS_LIST_EXTRA = "ingredients_list";
    private final String STEPS_LIST_EXTRA = "steps_list";
    private final String STEPS_LIST = "steps_list";
    private final String CURRENT_STEP_INDEX = "step_index";
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
    private Fragment mStepDetailFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);


        mStepDetailFragment = new StepDetailFragment();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INGREDIENTS_LIST_EXTRA)) {
                mIngredientList = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST_EXTRA);
                mStepsList = savedInstanceState.getParcelableArrayList(STEPS_LIST_EXTRA);
                currentPosition = savedInstanceState.getInt(CURRENT_STEP_INDEX);
                if (isTablet()) {
                    mStepDetailFragment = getSupportFragmentManager().getFragment(savedInstanceState, EXTRA_STEP_FRAGMENT);

                }
            }
        }

        if (isTablet()) {

            Log.v("DetailsActivity", "Device is tablet");
            if (currentPosition >= 0)
                createFragment(currentPosition, false);
        }
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
                    mRecipeNameTv.setText(recipe.getName());

                } else {
                    closeOnError();
                }

            } else {
                closeOnError();
            }
        }

        //**********  Populate INGREDIENT RecyclerView with data ************
        mIngredientRecyclerView.setHasFixedSize(true);
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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

        updateIngredientWidget();
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
        if (isTablet()) {
            getSupportFragmentManager().putFragment(outState, EXTRA_STEP_FRAGMENT, mStepDetailFragment);

        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(int position) {
        currentPosition = position;

        if (isTablet()) {
            mStepDetailFragment = new StepDetailFragment();
            createFragment(currentPosition, false);
        } else {
            Context context = DetailsActivity.this;
            Class destinationClass = StepsDetailsActivity.class;
            Intent intentToStartStepsActivity = new Intent(context, destinationClass);
            intentToStartStepsActivity.putParcelableArrayListExtra(STEPS_LIST, mStepsList);
            intentToStartStepsActivity.putExtra(CURRENT_STEP_INDEX, position);
            startActivity(intentToStartStepsActivity);
        }

    }

    private void createFragment(int position, boolean rotated) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_LIST_EXTRA, mStepsList);
        bundle.putInt(CURRENT_STEP_INDEX, position);
        //bundle.putBoolean(TWO_PANE, twoPane);
        bundle.putBoolean(EXTRA_ROTATION, rotated);

        mStepDetailFragment.setArguments(bundle);
        FragmentManager stepsFragmentManager = getSupportFragmentManager();
        stepsFragmentManager.beginTransaction().replace(R.id.step_container, mStepDetailFragment).commit();


    }

    public boolean isTablet() {
        return (DetailsActivity.this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void updateIngredientWidget() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BakingAppWidgetProvider.sendRefreshBroadcast(DetailsActivity.this);
            }
        });
    }

    @Override
    public void onNextClicked() {
        if (isTablet())
            if (currentPosition < mStepsList.size() - 1) {
                currentPosition++;
                replaceFragment(currentPosition);


            }

    }

    @Override
    public void onPrevClicked() {
        if (isTablet())
            if (currentPosition > 0) {
                currentPosition--;
                replaceFragment(currentPosition);

            }

    }

    private void replaceFragment(int index) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_LIST, mStepsList);
        bundle.putInt(CURRENT_STEP_INDEX, index);
        mStepDetailFragment = new StepDetailFragment();
        mStepDetailFragment.setArguments(bundle);
        FragmentManager stepsFragmentManager = getSupportFragmentManager();
        stepsFragmentManager.beginTransaction().replace(R.id.step_container, mStepDetailFragment).commit();


    }
}
