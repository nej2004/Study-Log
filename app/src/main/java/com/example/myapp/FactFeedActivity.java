package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
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
    private List<Fact> facts = new ArrayList<>();
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
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        // Using a public simple API that returns an array of jokes/facts - use Chuck Norris jokes as example
        String url = "https://api.sampleapis.com/futurama/quotes"; // returns an array of quotes

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    parseFacts(response);
                    progressBar.setVisibility(View.GONE);
                }, error -> {
                    progressBar.setVisibility(View.GONE);
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
            // sample image placeholder URL
            String img = "https://via.placeholder.com/150";
            facts.add(new Fact(text, img));
        }
        adapter.notifyDataSetChanged();
    }
}

