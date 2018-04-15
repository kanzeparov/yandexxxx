package com.example.franck.myapplication.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.franck.myapplication.R;
import com.example.franck.myapplication.adapter.GalleryAdapter;
import com.example.franck.myapplication.models.Image;

import java.util.ArrayList;

public class IntroFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle save){
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        return view;
    }

}
