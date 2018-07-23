package com.somayahalharbi.bakingapp.Utils;


import android.content.Context;
import android.content.res.Configuration;


public class DeviceConfig {
    //**************** Device is Tablet*************
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    //*************** Device is Rotated********************
    public static boolean isRotated(Context context) {
        return context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;

    }
}
