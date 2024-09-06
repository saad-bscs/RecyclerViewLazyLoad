package com.example.recyclerviewlazyload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.recyclerviewlazyload.utils.ItemAdapterWithViewModel;
import com.example.recyclerviewlazyload.viewModels.ItemViewModel;

import java.util.ArrayList;

//lazy loading with viewmodel class
public class SecondActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapterWithViewModel itemAdapter;
    private ProgressBar progressBar;
    private ItemViewModel itemViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // Initialize ViewModel
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        setupRecyclerView();
        observeViewModel();

        // Load initial data
        itemViewModel.loadMoreItems();
    }

    private void setupRecyclerView() {
        itemAdapter = new ItemAdapterWithViewModel(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);

        // Add scroll listener for lazy loading
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                // Use the getter method isLoading() and PAGE_SIZE
                if (!itemViewModel.isLoading() &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= itemViewModel.getPageSize()) {
                    itemViewModel.loadMoreItems();
                }
            }
        });
    }

    private void observeViewModel() {
        // Observe LiveData for items
        itemViewModel.getItemsLiveData().observe(this, items -> {
            if (items != null) {
                itemAdapter.addItems(items);  // Update the adapter with new items
            } else {
                Toast.makeText(SecondActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe LiveData for loading state
        itemViewModel.getLoadingLiveData().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

}