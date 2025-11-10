package com.example.studylog;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FactFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FactAdapter adapter;
    private List<FactModel> facts = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact_feed);

        recyclerView = findViewById(R.id.recyclerFacts);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FactAdapter(facts);
        recyclerView.setAdapter(adapter);

        fetchFacts();
    }

    private void fetchFacts() {
        progressBar.setVisibility(android.view.View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.sampleapis.com/futurama/quotes";

        JsonArrayRequest req = new JsonArrayRequest(url,
                response -> {
                    parseFacts(response);
                    progressBar.setVisibility(android.view.View.GONE);
                }, error -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(this, "Failed to fetch facts", Toast.LENGTH_SHORT).show();
                });
        queue.add(req);
    }

    private void parseFacts(JSONArray arr) {
        facts.clear();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) continue;
            String text = o.optString("quote", "No text");
            String img = "https://via.placeholder.com/150";
            facts.add(new FactModel(text, img));
        }
        adapter.notifyDataSetChanged();
    }
}
