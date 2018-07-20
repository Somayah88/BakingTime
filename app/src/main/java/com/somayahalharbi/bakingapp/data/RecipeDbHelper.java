package com.somayahalharbi.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by somayahalharbi on 7/19/18.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RECIPES.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "create table " + DataContract.RecipeIngredientsEntry.TABLE_NAME + " ( " +
                DataContract.RecipeIngredientsEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.RecipeIngredientsEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                DataContract.RecipeIngredientsEntry.COLUMN_MEASUREMENT + " TEXT  " +
                DataContract.RecipeIngredientsEntry.COLUMN_MEASUREMENT + " DOUBLE );";
        db.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.RecipeIngredientsEntry.TABLE_NAME);
        onCreate(db);

    }
}
