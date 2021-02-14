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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.frh.searchlibrary.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public MaterialSearchBar search_bar;
    ArrayList<String> test;
    ArrayList<String> result;
    LinearLayout linearItem;
    RelativeLayout searchview;
    EditText editText;
    String[] test2;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_bar = findViewById(R.id.search_bar);
        linearItem = findViewById(R.id.linear_item);
        searchview = findViewById(R.id.searchview);
        search_bar.setHint("search malls");
        search_bar.setRoundedSearchBarEnabled(true);
        editText = search_bar.findViewById(R.id.mt_editText);
        search_bar.mtSearchBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });

        test = new ArrayList<>();
        result = new ArrayList();
        test2 = new String[]{"Westlake Center", "tehran", "qom", "arak", "esfehan", "soed", "mashhad", "esfehan"};


        test.add("Westlake Center");
        test.add("tehran");
        test.add("qom");
        test.add("qom1");
        test.add("qom2");
        test.add("qom3");
        test.add("arak");
        test.add("esfehan");
        test.add("soed");
        test.add("mashhad");
        test.add("esfehan");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 0; i < test.size(); i++) {
                    if (test.get(i).contains(s.toString().trim())) {
                        result.add(test.get(i));
                        search_bar.setLastSuggestions(result);
                        search_bar.showSuggestionsList();
                    } else {
                        result.remove(test.get(i));
                       // search_bar.hideSuggestionsList();
                    }


                }
            }
        });

    }
}
