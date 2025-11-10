package com.example.studylog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.studylog.R;

public class FactFeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private FactAdapter adapter;
    private List<FactModel> facts = new ArrayList<>();
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fact_feed, container, false);
        recyclerView = v.findViewById(R.id.recycler_facts);
        progressBar = v.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FactAdapter(facts);
        recyclerView.setAdapter(adapter);

        fetchFacts();
        return v;
    }

    private void fetchFacts() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        // Use uselessfacts API which returns a single object; we'll fetch it multiple times to populate a small list
        String url = "https://uselessfacts.jsph.pl/random.json?language=en";

        // For demo, fetch 8 facts using StringRequest sequentially
        for (int i = 0; i < 8; i++) {
            StringRequest req = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            JSONObject o = new JSONObject(response);
                            String text = o.optString("text", "No fact");
                            String img = "https://placehold.co/100x100/A0D9EF/000000?text=FACT";
                            facts.add(new FactModel(text, img));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }, error -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed to fetch fact: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            queue.add(req);
        }
    }
}
