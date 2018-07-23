package com.somayahalharbi.bakingapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.somayahalharbi.bakingapp.models.Recipe;


public class SharedPref {

    public static final String PREFERENCE_NAME = "baking_app_pref";
    public static final String RECIPE = "recipe";

    //**************** Store Recipe Info in SharedPreference for Widget*************
    public void setPrefData(Context context, Recipe recipe) {
        Gson gson = new Gson();
        String recipeJson = gson.toJson(recipe);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RECIPE, recipeJson);
        editor.commit();


    }
    //**************** Restore Recipe Info from SharedPreference for Widget*************


    public Recipe getPrefData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String recipeJson = sharedPreferences.getString(RECIPE, "");
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeJson, Recipe.class);
        return recipe;


    }


}
