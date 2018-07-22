package com.somayahalharbi.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.somayahalharbi.bakingapp.idling.EspressoIdlingResource;
import com.somayahalharbi.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void clickTest() {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (getRVCount() > 0) {
            onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }


    }


    @After
    public void unregisterIdlingResource() {
        if (EspressoIdlingResource.getIdlingResource() != null) {
            IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
        }
    }

    private int getRVCount() {
        RecyclerView recyclerView = mActivityRule.getActivity().findViewById(R.id.recipe_recycler_view);
        return recyclerView.getAdapter().getItemCount();
    }
}

