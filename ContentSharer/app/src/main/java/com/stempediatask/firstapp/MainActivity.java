package com.stempediatask.firstapp;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private TileAdapter adapter;
    private List<Tile> tileList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize the list
        tileList = new ArrayList<>();

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        // Use a GridLayoutManager with 3 columns (as it's landscape)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Setup Adapter
        adapter = new TileAdapter(this, tileList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();
    }

    private void fetchDataFromFirestore() {
        db.collection("tiles") // The name of your collection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tileList.clear(); // Clear the list before adding new data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert each document to a Tile object
                            Tile tile = document.toObject(Tile.class);
                            tileList.add(tile);
                        }
                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}