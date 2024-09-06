package com.example.recyclerviewlazyload.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<List<String>> itemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final List<String> currentItems = new ArrayList<>();
    private boolean isLoading = false;
    private int currentPage = 1;
    private static final int PAGE_SIZE = 20;
    // Expose LiveData to the UI
    public LiveData<List<String>> getItemsLiveData() {
        return itemsLiveData;
    }

    // Expose loading state to the UI
    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    // Public getter for isLoading
    public boolean isLoading() {
        return isLoading;
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    // Load more items for lazy loading
    // Load more items for lazy loading
    public void loadMoreItems() {
        if (isLoading) return;

        isLoading = true;
        loadingLiveData.postValue(true);  // Show loader
        // Simulate data fetching with a delay
        new Thread(() -> {
            try {
                Thread.sleep(1500); // Simulate network delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.e("Before: ",""+currentItems.size());
            // Fetch new items and update the list
            List<String> newItems = getDataFromServer(currentPage, PAGE_SIZE);
            Log.e("currentPage: ",""+currentPage);
            currentItems.addAll(newItems);
            Log.e("After: ",""+currentItems.size());
            itemsLiveData.postValue(new ArrayList<>(currentItems)); // Use a new list to trigger LiveData change
            isLoading = false;
            loadingLiveData.postValue(false); // Hide loader
            currentPage++;
        }).start();
    }

    // Simulate fetching data from a server
    private List<String> getDataFromServer(int page, int pageSize) {
        List<String> newItems = new ArrayList<>();
        int start = (page - 1) * pageSize;
        Log.e("start: ",""+start);
        int end = start + pageSize;
        Log.e("end: ",""+end);

        for (int i = start; i < end; i++) {
            Log.e("Item: ",""+(i + 1));
            newItems.add("Item " + (i + 1));
        }
        return newItems;
    }
}
