package com.example.franck.myapplication.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import com.example.franck.myapplication.models.Image;
import com.example.franck.myapplication.network.InternetConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private final static String URL = "https://rickandmortyapi.com/api/character/?page=";
    public final static int AMOUNT_PAGES = 20;
    private final static int START_PAGE = 0;
    //Api has only 20 pages
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private IntroFragment fragment;
    private FloatingActionButton fab;
    private static int count = 0;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private final int[] imagesDrawable = new int[] {R.drawable.num_page_1, R.drawable.num_page_2, R.drawable.num_page_3,
            R.drawable.num_page_4, R.drawable.num_page_5, R.drawable.num_page_6, R.drawable.num_page_7, R.drawable.num_page_8,
            R.drawable.num_page_9, R.drawable.num_page_10, R.drawable.num_page_11, R.drawable.num_page_12, R.drawable.num_page_13,
            R.drawable.num_page_14, R.drawable.num_page_15, R.drawable.num_page_16, R.drawable.num_page_17, R.drawable.num_page_18,
            R.drawable.num_page_19, R.drawable.num_page_20};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragment = new IntroFragment();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.placeHolder, fragment);
        fragmentTransaction.commit();


        //init block
        recyclerView = findViewById(R.id.recycler_view);
        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        fab = findViewById(R.id.fab);
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) throws Exception {
                //transport data to fragment
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "newFragment");
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchImagesAndUpdateFabImage(view, true);
            }
        });
    }



    private void fetchImagesAndUpdateFabImage(View view, boolean fabClick) {
        if (InternetConnection.checkConnection(getApplicationContext())) {
            if (count == AMOUNT_PAGES) {
                count = START_PAGE;
            }

            //Count increment if user click on fab
            if (fabClick) {
                count++;
            }

            //If we have init activity, we return, else we remove fragment
            if(count == START_PAGE) {
                return;
            } else {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment).commit();
            }

            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), imagesDrawable[count - 1]));
            fetchImages(URL + count);
            Toast.makeText(getApplicationContext(), "New images", Toast.LENGTH_LONG).show();
        } else {


            Snackbar.make(view, R.string.internet, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = savedInstanceState.getInt("count");
//        images = savedInstanceState.getParcelableArrayList("images");
        fetchImagesAndUpdateFabImage(getCurrentFocus(), false);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
        outState.putParcelableArrayList("images", images);
    }

    protected void onPause(){
        super.onPause();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
    }


    private void fetchImages(String endpoint) {
        JsonObjectRequest req = new JsonObjectRequest(endpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //clear images to load new images(on new page)
                        images.clear();

                        pDialog.hide();
                        try {
                            JSONArray moviesJson = response.getJSONArray("results");

                            for (int i = 0; i < moviesJson.length(); i++) {
                                JSONObject object = moviesJson.getJSONObject(i);
                                Image movie = new Image(object.getString("name"), object.getString("image"));
                                images.add(movie);
                            }
                        } catch (JSONException j) {
                            Toast.makeText(getApplicationContext(), "Error with server", Toast.LENGTH_LONG).show();
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error with server", Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }
}