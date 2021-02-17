package com.frh.frhsearchbar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.frh.frhsearchbar.databinding.ItemCustomSuggestionBinding;
import com.frh.searchlibrary.SuggestionsAdapter;

public class CustomSuggestionsAdapter extends SuggestionsAdapter<String, CustomSuggestionsAdapter.SuggestionHolder> {

    private SuggestionsAdapter.OnItemViewClickListener listener;
    private Context context;

    public CustomSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public void setListener(SuggestionsAdapter.OnItemViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getSingleViewHeight() {
        return 50;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public CustomSuggestionsAdapter.SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemCustomSuggestionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_custom_suggestion, parent, false);
        return new CustomSuggestionsAdapter.SuggestionHolder(binding);
    }

    @Override
    public void onBindSuggestionHolder(String suggestion, SuggestionHolder holder, int position) {
        ((SuggestionHolder) holder).binding.title.setText(getSuggestions().get(position));
        ((SuggestionHolder) holder).binding.description.setText(getSuggestions().get(position));

        Glide.with(context)
                .load(getUrls().get(position))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                .into(holder.binding.imageView);

        if (position == (getItemCount() - 1)) {
            ((SuggestionHolder) holder).binding.textViewMore.setVisibility(View.VISIBLE);
        } else ((SuggestionHolder) holder).binding.textViewMore.setVisibility(View.GONE);


    }

    public interface OnItemViewClickListener {
        void OnItemClickListener(int position, View v);

        void OnItemDeleteListener(int position, View v);
    }

    class SuggestionHolder extends RecyclerView.ViewHolder {
        private ItemCustomSuggestionBinding binding;

        public SuggestionHolder(final ItemCustomSuggestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.linearItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.linearRoot.performClick();
                }
            });
            binding.linearRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(getSuggestions().get(getAdapterPosition()));
                    listener.OnItemClickListener(getAdapterPosition(), v);
                }
            });
            binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position >= 0 && position < getSuggestions().size()) {
                        v.setTag(getSuggestions().get(getAdapterPosition()));
                        listener.OnItemDeleteListener(getAdapterPosition(), v);
                    }
                }
            });
        }
    }


}
