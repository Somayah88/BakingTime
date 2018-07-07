package com.somayahalharbi.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.somayahalharbi.bakingapp.R;
import com.somayahalharbi.bakingapp.Utils.ApiService;
import com.somayahalharbi.bakingapp.Utils.NetworkUtilities;
import com.somayahalharbi.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<Recipe> recipes = new ArrayList<>();
    private TextView mainTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTv = findViewById(R.id.main_tv);

        Log.v(TAG, "onCreate before api call");

        ApiService api = NetworkUtilities.getRetrofitInstance().create(ApiService.class);


        Log.v(TAG, "calling JSON");
        Call<List<Recipe>> call = api.getMyJSON();
        Log.v(TAG, "JSON called");


        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                //Dismiss Dialog

                Log.v(TAG, "On response executed");
                if (response.isSuccessful()) {

                    Log.v(TAG, "response is successful " + response);

                    recipes = response.body();
                    Log.v(TAG, "recipes size " + recipes.size());
                    if (recipes.size() > 0) {
                        Log.v(TAG, "Recipes is not null  and the number of recipes is " + recipes.size());

                        mainTv.setText(recipes.get(0).getSteps().get(1).getShortDescription());
                    } else {
                        Log.v(TAG, "Recipes is null");

                        mainTv.setText("N/A");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.v(TAG, "call failed" + t.toString());


            }
        });
        Log.v(TAG, "display Results");


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
