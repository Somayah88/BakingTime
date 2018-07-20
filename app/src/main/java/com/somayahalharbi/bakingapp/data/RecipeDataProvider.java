package com.somayahalharbi.bakingapp.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.somayahalharbi.bakingapp.data.DataContract.RecipeIngredientsEntry.TABLE_NAME;

public class RecipeDataProvider extends ContentProvider {

    public static final int INGREDIENT = 100;
    public static final int INGREDIENT_WITH_ID = 101;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private RecipeDbHelper mRecipeHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_INGREDIENTS, INGREDIENT);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_INGREDIENTS + "/#", INGREDIENT_WITH_ID);
        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mRecipeHelper = new RecipeDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mRecipeHelper.getReadableDatabase();
        int match = mUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match) {
            case INGREDIENT:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mRecipeHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case INGREDIENT:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DataContract.RecipeIngredientsEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mRecipeHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        int ingredientDeleted;


        switch (match) {
            case INGREDIENT_WITH_ID:
                ingredientDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (ingredientDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ingredientDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mRecipeHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        int ingredientUpdated;


        switch (match) {
            case INGREDIENT_WITH_ID:
                ingredientUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (ingredientUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ingredientUpdated;
    }
}
