package com.frh.frhsearchbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.frh.searchlibrary.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public MaterialSearchBar search_bar;
    ArrayList<String> test;
    ArrayList<String> result;
    LinearLayout linearItem;
    RelativeLayout searchview;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_bar = findViewById(R.id.search_bar);
        linearItem = findViewById(R.id.linear_item);
        searchview = findViewById(R.id.searchview);
        search_bar.setHint("جستجو ...");
        search_bar.setRoundedSearchBarEnabled(true);

        search_bar.mtSearchBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });


        test = new ArrayList<>();
        result = new ArrayList();

        test.add("دفتر پیشخوان دولت");
        test.add("دفتر پیشخوان دولت");
        test.add("دفتر پیشخوان دولت");
        test.add("دفتر پیشخوان دولت");
        test.add("دفتر پیشخوان دولت");
        test.add("دفتر پیشخوان دولت");
        test.add("دفتر پیشخوان دولت");
        test.add("دفتر پیشخوان دولت");


        final ConstraintLayout constraintLayout = (ConstraintLayout) search_bar.findViewById(R.id.root);
        final RecyclerView recyclerView = (RecyclerView) search_bar.findViewById(R.id.mt_recycler);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                
                // constraintLayout.setMaxHeight(600);

            }
        });

        for (int i = 0; i <= 5; i++) {
            result.add(test.get(i));
            search_bar.setLastSuggestions(result);
        }

        linearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search_bar.isSuggestionsVisible()) {
                    search_bar.hideSuggestionsList();
                }
                else
                    search_bar.showSuggestionsList();
            }
        });
    }
}
