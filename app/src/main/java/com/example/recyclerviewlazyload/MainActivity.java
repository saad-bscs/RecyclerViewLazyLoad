package com.example.recyclerviewlazyload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.recyclerviewlazyload.utils.ItemAdapterWithoutViewModel;

import java.util.ArrayList;
import java.util.List;

//lazy loading without viewmodel class
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapterWithoutViewModel itemAdapter;
    private ProgressBar progressBar;
    private List<String> items = new ArrayList<>();
    private boolean isLoading = false;
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        setupRecyclerView();
        loadMoreItems(); // Load initial data
    }

    private void setupRecyclerView() {
        itemAdapter = new ItemAdapterWithoutViewModel(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);

        // Add scroll listener for lazy loading
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                Log.e("visibleItemCount: ",""+visibleItemCount);
                int totalItemCount = layoutManager.getItemCount();
                Log.e("totalItemCount: ",""+totalItemCount);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                Log.e("firstVisibleItemPosition: ",""+firstVisibleItemPosition);

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                    loadMoreItems();
                }
            }
        });
    }

    private void loadMoreItems() {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        // Simulate a delay for loading data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> newItems = getDataFromServer(currentPage, PAGE_SIZE);
                itemAdapter.addItems(newItems);
                progressBar.setVisibility(View.GONE);
                isLoading = false;
                currentPage++;
            }
        }, 1500);
    }

    // Simulate data fetching from a server
    private List<String> getDataFromServer(int page, int pageSize) {
        List<String> newItems = new ArrayList<>();
        int start = (page - 1) * pageSize;
        int end = start + pageSize;

        for (int i = start; i < end; i++) {
            newItems.add("Item " + (i + 1));
        }
        return newItems;
    }

}