package com.somayahalharbi.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.Utils.SharedPref;
import com.somayahalharbi.bakingapp.models.Ingredient;
import com.somayahalharbi.bakingapp.models.Recipe;

import java.util.ArrayList;


public class BakingAppRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private int appWidgetId;

    public BakingAppRemoteViewFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }


    @Override
    public void onCreate() {
        ingredientList.clear();
        updateData();

    }

    public void updateData() {
        ingredientList.clear();
        SharedPref sharedPref = new SharedPref();
        Recipe recipe = sharedPref.getPrefData(mContext);
        ingredientList = recipe.getIngredients();

    }

    @Override
    public void onDataSetChanged() {
        ingredientList.clear();
        updateData();



    }

    @Override
    public void onDestroy() {
        ingredientList.clear();

    }

    @Override
    public int getCount() {

        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {


        RemoteViews views = new RemoteViews((mContext.getPackageName()), R.layout.widget_list_item);
        views.setTextViewText(R.id.widget_ingredient_item, ingredientList.get(position).getIngredient());
        views.setTextViewText(R.id.widget_ingredient_measurement, ingredientList.get(position).getMeasure());
        views.setTextViewText(R.id.widget_ingredient_quantity, String.valueOf(ingredientList.get(position).getQuantity()));

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return ingredientList.size();
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }
}
