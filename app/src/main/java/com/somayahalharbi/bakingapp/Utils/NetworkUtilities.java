package com.somayahalharbi.bakingapp.Utils;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtilities {

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    public static final String TAG = "NetworkUitilties";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


}
