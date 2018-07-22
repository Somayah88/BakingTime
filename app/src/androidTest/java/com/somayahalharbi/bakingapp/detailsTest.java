package com.somayahalharbi.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.somayahalharbi.bakingapp.models.Ingredient;
import com.somayahalharbi.bakingapp.models.Recipe;
import com.somayahalharbi.bakingapp.models.Step;
import com.somayahalharbi.bakingapp.ui.DetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class detailsTest {
    private final String RECIPE = "recipe";

    private Recipe recipeOpj;


    @Rule
    public ActivityTestRule<DetailsActivity> mActivityRule =
            new ActivityTestRule<DetailsActivity>(DetailsActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, DetailsActivity.class);
                    result.putExtra(RECIPE, getRecipeOpj());
                    return result;
                }
            };

    @Test
    public void scrollToPosition() {

        onView(withId(R.id.step_recyclerview)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withId(R.id.ingredients_recyclerview)).perform(RecyclerViewActions.scrollToPosition(3));


    }

    private Recipe getRecipeOpj() {

        recipeOpj = new Recipe();

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredient("Graham Cracker crumbs");
        ingredient.setMeasure("CUP");
        ingredient.setQuantity(2);
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        ingredientArrayList.add(ingredient);

        Step step = new Step();
        step.setDescription("Recipe Introduction");
        step.setId(1);
        step.setShortDescription("Recipe Introduction");
        step.setThumbnailURL("");
        step.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdae8_-intro-cheesecake/-intro-cheesecake.mp4");

        ArrayList<Step> stepArrayList = new ArrayList<>();
        stepArrayList.add(step);

        recipeOpj.setName("Cheesecake");
        recipeOpj.setId(4);
        recipeOpj.setImage("");
        recipeOpj.setIngredients(ingredientArrayList);
        recipeOpj.setSteps(stepArrayList);
        recipeOpj.setServings(8);


        return recipeOpj;
    }


}