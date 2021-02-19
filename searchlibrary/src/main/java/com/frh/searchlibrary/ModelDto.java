package com.frh.searchlibrary;

import java.io.Serializable;
import java.util.List;

public class ModelDto implements Serializable {

    private List<String> title;
    private  List<String>  description;
    private  List<String>  urls;
    private List<Integer>  numbers;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

}
