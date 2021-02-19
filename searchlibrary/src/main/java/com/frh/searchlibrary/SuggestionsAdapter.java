package com.frh.searchlibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class SuggestionsAdapter<S, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> implements Filterable {

    protected List<S> suggestions = new ArrayList<>();
    protected List<S> descriptions = new ArrayList<>();
    protected List<S> urls = new ArrayList<>();
    protected List<S> suggestions_clone = new ArrayList<>();
    private final LayoutInflater inflater;
    protected int maxSuggestionsCount = 5;

    public SuggestionsAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void addSuggestion(S r){
        if (maxSuggestionsCount <= 0)
            return;

        if (r == null)
            return;
        if (!suggestions.contains(r))
        {
            if (suggestions.size() >= maxSuggestionsCount) {
                suggestions.remove(maxSuggestionsCount - 1);
            }
            suggestions.add(0, r);
        }else {
            suggestions.remove(r);
            suggestions.add(0, r);
        }
        suggestions_clone = suggestions;
        notifyDataSetChanged();
    }

    public void setSuggestions(List<S> suggestions) {
        this.suggestions = suggestions;
        if (suggestions.size() > maxSuggestionsCount) {
            suggestions.remove(maxSuggestionsCount - 1);
        }
        suggestions_clone = suggestions;
        notifyDataSetChanged();
    }

    public void setdescriptions(List<S> descriptions) {
        this.descriptions = descriptions;
    }

    public void setUrls(List<S> urls) {
        this.urls = urls;
    }

    public void clearSuggestions() {
        suggestions.clear();
        suggestions_clone = suggestions;
        notifyDataSetChanged();
    }

    public void deleteSuggestion(int position, S r) {
        if(r == null)
            return;
        if(suggestions.contains(r))
        {
            this.notifyItemRemoved(position);
            suggestions.remove(r);
            suggestions_clone = suggestions;
        }
    }

    public List<S> getSuggestions() {
        return suggestions;
    }

    public List<S> getDescriptions() {
        return descriptions;
    }

    public List<S> getUrls() {
        return urls;
    }

    public int getMaxSuggestionsCount() {
        return maxSuggestionsCount;
    }

    public void setMaxSuggestionsCount(int maxSuggestionsCount) {
        this.maxSuggestionsCount = maxSuggestionsCount;
    }

    protected LayoutInflater getLayoutInflater(){
        return this.inflater;
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        onBindSuggestionHolder(suggestions.get(position), holder, position);
    }

    public abstract void onBindSuggestionHolder(S suggestion, V holder, int position);

    public abstract int getSingleViewHeight();

    public int getListHeight(){
        return getItemCount() * getSingleViewHeight() * 2;
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public interface OnItemViewClickListener{
        void OnItemClickListener(int position, View v);
        void OnItemDeleteListener(int position, View v);
    }

}
