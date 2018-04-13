package com.example.franck.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franck.myapplication.models.MovieModel;
import com.example.franck.myapplication.network.ApiService;
import com.example.franck.myapplication.network.InternetConnection;
import com.example.franck.myapplication.network.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.withText1);
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("jsom");
            dialog.setMessage("efw");
            dialog.show();

            ApiService api = RetroClient.getApiService();
            Call<MovieModel> call = api.getMyJSON();
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    dialog.dismiss();

                    if (response.isSuccessful()) {
                        String s = response.body().getMovies().get(0).getDirector();
                        Toast.makeText(getApplicationContext(),  s,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        }

    }
}
