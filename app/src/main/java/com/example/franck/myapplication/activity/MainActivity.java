package com.example.franck.myapplication.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.franck.myapplication.R;
import com.example.franck.myapplication.adapter.GalleryAdapter;
import com.example.franck.myapplication.app.AppController;
import com.example.franck.myapplication.models.Result;
import com.example.franck.myapplication.network.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "wtf";
    private final String endpoint = "https://rickandmortyapi.com/api/character/?page=";
    private ArrayList<Result> movies;
    private static int count = 0;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        movies = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), movies);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) throws Exception{
                Bundle bundle = new Bundle();

                bundle.putParcelableArrayList("movies", movies);
                bundle.putInt("position", position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft,"efw");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        final int[] images = new int[] {R.drawable.num_page_1,R.drawable.num_page_2,R.drawable.num_page_3,R.drawable.num_page_4,
                R.drawable.num_page_5,R.drawable.num_page_6,R.drawable.num_page_7,R.drawable.num_page_8,R.drawable.num_page_9,
                R.drawable.num_page_10,R.drawable.num_page_11,R.drawable.num_page_12,R.drawable.num_page_13,R.drawable.num_page_14,
                R.drawable.num_page_15,R.drawable.num_page_16,R.drawable.num_page_17,R.drawable.num_page_18,R.drawable.num_page_19,
                R.drawable.num_page_20};
        super.onResume();
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnection.checkConnection(getApplicationContext())) {

                    if (count == 20) {
                        count = 0;
                        Intent i = new Intent( getApplicationContext() , MainActivity.class );
                        finish();
                        getApplicationContext().startActivity(i);
                        return;
                    }
                    count++;
                    fetchImages(endpoint + (count));
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), images[count-1]));
                    Toast.makeText(getApplicationContext(), "Added new images", Toast.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, R.string.internet, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        fab.callOnClick();


    }



    private void fetchImages(String endpoint) {

        if (InternetConnection.checkConnection(getApplicationContext())) {
            JsonObjectRequest req = new JsonObjectRequest(endpoint,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            pDialog.hide();

                            try {


                                JSONArray moviesJson = response.getJSONArray("results");
                                Log.d("wtf", moviesJson.length()+"");
                                for (int i = 0; i < moviesJson.length(); i++) {

                                    JSONObject object = moviesJson.getJSONObject(i);

                                    Log.d(TAG, object.toString());
                                    Result movie = new Result(object.getString("name"),object.getString("image"));
                                Log.d(TAG, object.getString("name"));
                                    movies.add(movie);
                                }
                            } catch (JSONException j) {
                                j.printStackTrace();
                            }





                            mAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    pDialog.hide();
                }
            });

            AppController.getInstance().addToRequestQueue(req);
        }
    }
}