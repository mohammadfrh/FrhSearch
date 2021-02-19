package com.frh.frhsearchbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.frh.frhsearchbar.databinding.ActivityMainBinding;
import com.frh.searchlibrary.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String URL = "data:image/jpeg;base64,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";

    private TestModels testModels = new TestModels();

    private CustomSuggestionsAdapter adapter;
    private ActivityMainBinding binding;

    private int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        clickListener();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, getLayoutResourceId());
        adapter = new CustomSuggestionsAdapter(LayoutInflater.from(this));
        binding.searchBar.setCustomSuggestionAdapter(adapter);
        binding.searchBar.setHint("search malls");
        binding.searchBar.setRoundedSearchBarEnabled(true);
        binding.searchBar.setMaxSuggestionCount(4);

    }

    private List<ApiModel> createFakeData() {
        List<ApiModel> fakeDatas = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            fakeDatas.add(
                    new ApiModel("mohammad" + i, "Asghar" + i, URL)
            );
        }

        return fakeDatas;
    }

    private TestModels setModel(List<ApiModel> apiModels) {
        List<String> titles = new ArrayList<>();
        List<String> descs = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (ApiModel model : apiModels) {
            titles.add(model.getTitle());
            descs.add(model.getDesc());
            urls.add(model.getUrl());
        }

        return new TestModels(titles, descs, urls);

    }

    private void clickListener() {

        final List<String> filteredTitles = new ArrayList<>();
        final List<String> filteredDescriptions = new ArrayList<>();
        final List<String> filteredUrls = new ArrayList<>();

        adapter.setListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });

        binding.searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2)
                    binding.searchBar.showLoading(true);
                else
                    binding.searchBar.showLoading(false);

                for (int i = 0; i < createFakeData().size(); i++) {
                    if (createFakeData().get(i).title.contains(s.toString().trim())) {
                       /* if (.getTitle().contains(createFakeData().getTitle().get(i)))
                            return;*/

                        filteredTitles.add(createFakeData().get(i).getTitle());
                        filteredDescriptions.add(createFakeData().get(i).getDesc());
                        filteredUrls.add(createFakeData().get(i).getUrl());

                        testModels.setTitle(filteredTitles);
                        testModels.setDescription(filteredDescriptions);
                        testModels.setUrls(filteredUrls);


                        binding.searchBar.setLastSuggestions(testModels);
                        binding.searchBar.showSuggestionsList();
                    } else {
                        //  resultTitle.remove(model.get(i).getTitle());
                        // resultTitle.remove(model.get(i));
                        // search_bar.hideSuggestionsList();
                    }


                }
            }
        });

    }


    private class ApiModel {
        private String title;
        private String desc;
        private String url;

        public ApiModel(String title, String desc, String url) {
            this.title = title;
            this.desc = desc;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
