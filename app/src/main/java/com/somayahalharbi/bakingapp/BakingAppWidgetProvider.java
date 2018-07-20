package com.somayahalharbi.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.somayahalharbi.bakingapp.Utils.SharedPref;
import com.somayahalharbi.bakingapp.models.Recipe;
import com.somayahalharbi.bakingapp.ui.DetailsActivity;
import com.somayahalharbi.bakingapp.ui.MainActivity;


public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, BakingAppWidgetProvider.class));
        context.sendBroadcast(intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPref pref = new SharedPref();
        Recipe recipe = pref.getPrefData(context);
        if (recipe != null) {

            CharSequence widgetText = context.getString(R.string.widget_recipe_name);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            views.setTextViewText(R.id.recipe_name, widgetText);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            Intent titleIntent = new Intent(context, MainActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.recipe_name, titlePendingIntent);
            Intent intent = new Intent(context, BakingAppRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(R.id.widget_listView, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listView);
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, BakingAppWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(cn), R.id.widget_listView);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(),
                    R.layout.baking_app_widget);

            Intent intent = new Intent(context, BakingAppRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(R.id.widget_listView, intent);
            Intent titleIntent = new Intent(context, MainActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            rv.setOnClickPendingIntent(R.id.recipe_name, titlePendingIntent);
            rv.setRemoteAdapter(R.id.widget_listView, intent);
            Intent clickIntentTemplate = new Intent(context, DetailsActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_listView, clickPendingIntentTemplate);
            ComponentName component = new ComponentName(context, BakingAppWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);
            appWidgetManager.updateAppWidget(component, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
//TODO: add action

    }


}

