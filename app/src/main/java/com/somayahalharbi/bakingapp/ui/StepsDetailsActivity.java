package com.somayahalharbi.bakingapp.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsDetailsActivity extends AppCompatActivity {
    private final String STEPS_LIST = "steps_list";
    private final String CURRENT_STEP_INDEX = "step_index";
    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    private int currentIndex;
    private ArrayList<Step> stepList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_details);
        ButterKnife.bind(this);
        final Bundle bundle = getIntent().getExtras();
        if (savedInstanceState == null) {
            if (bundle != null && bundle.containsKey(STEPS_LIST) && bundle.containsKey(CURRENT_STEP_INDEX)) {
                currentIndex = bundle.getInt(CURRENT_STEP_INDEX);
                stepList = bundle.getParcelableArrayList(STEPS_LIST);
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_STEP_INDEX) && savedInstanceState.containsKey(STEPS_LIST)) {
                currentIndex = savedInstanceState.getInt(CURRENT_STEP_INDEX);
                stepList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            }
        }
        displayData();
        setSupportActionBar(mToolBar);

    }

    private void displayData() {
        StepDetailFragment stepsFragment = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_LIST, stepList);
        bundle.putInt(CURRENT_STEP_INDEX, currentIndex);
        stepsFragment.setArguments(bundle);
        FragmentManager stepsFragmentManager = getSupportFragmentManager();
        stepsFragmentManager.beginTransaction().replace(R.id.steps_container, stepsFragment).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_INDEX, currentIndex);
        outState.putParcelableArrayList(STEPS_LIST, stepList);
        super.onSaveInstanceState(outState);
    }
}
