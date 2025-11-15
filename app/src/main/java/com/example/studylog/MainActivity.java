package com.example.studylog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.studylog.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean hasTriedBackupAPI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupBottomNavigation();
        // note: Call/Find buttons and API result are now handled in ContactFragment
    }

    private void setupUI() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        View root = findViewById(android.R.id.content);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // apiResultTextView is intentionally not initialized here; it's local to ContactFragment now
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(this::handleNavigationItemSelected);
        bottomNav.setSelectedItemId(R.id.nav_facts);
    }

    private boolean handleNavigationItemSelected(android.view.MenuItem item) {
        Fragment fragment = createFragmentForMenuItem(item.getItemId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        return true;
    }

    private Fragment createFragmentForMenuItem(int itemId) {
        if (itemId == R.id.nav_tasks) {
            return new TaskLogFragment();
        } else if (itemId == R.id.nav_contact) {
            return new ContactFragment();
        } else if (itemId == R.id.nav_profile) {
            return new ProfileFragment();
        } else {
            return new FactFeedFragment(); // default to facts
        }
    }

    /**
     * Standard Volley Request Implementation
     */
    private void makeStandardVolleyRequest() {
        String apiUrl = getString(R.string.world_time_api_url);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
            response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.optString("datetime", jsonObject.optString("dateTime", ""));
                    if (data == null || data.isEmpty()) {
                        // apiResultTextView.setText(getString(R.string.api_result_parse_error));
                    } else {
                        // apiResultTextView.setText(getString(R.string.api_result_current_time, data));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    // apiResultTextView.setText(getString(R.string.api_result_parse_error));
                }
            }, error -> {
                Log.e(TAG, "Standard Volley request failed: " + error.getMessage());
                // apiResultTextView.setText(getString(R.string.api_result_network_error) + " - Tap to retry with diagnostics");
                // apiResultTextView.setOnClickListener(v -> {
                //     apiResultTextView.setOnClickListener(null);
                //     fetchCurrentTimeAdvanced(false);
                // });
            }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("User-Agent", "StudyLog-Android-App");
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
            15000,
            1,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(stringRequest);
    }

    private void handleApiError(VolleyError error, boolean wasBackupAPI) {
        Log.e(TAG, "API request failed", error);

        String errorMessage = getString(R.string.api_result_network_error);

        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            switch (statusCode) {
                case 404:
                    errorMessage = "API endpoint not found (404)";
                    break;
                case 500:
                    errorMessage = "Server error occurred (500)";
                    break;
                case 403:
                    errorMessage = "Access denied to API (403)";
                    break;
                default:
                    errorMessage = "Network error (Code: " + statusCode + ")";
                    break;
            }
        } else if (error instanceof TimeoutError) {
            errorMessage = "Request timeout - check connection";
        } else if (error instanceof NoConnectionError) {
            errorMessage = "No internet connection available";
        } else if (error instanceof NetworkError) {
            errorMessage = "Network error occurred";
        }

        if (wasBackupAPI) {
            errorMessage += " (Both APIs failed)";
        }

        errorMessage += " - Tap to retry";
        final String finalMessage = errorMessage;
        runOnUiThread(() -> {
            // apiResultTextView.setText(finalMessage);
            // apiResultTextView.setOnClickListener(v -> {
            //     hasTriedBackupAPI = false;
            //     apiResultTextView.setOnClickListener(null);
            //     fetchCurrentTimeAdvanced(false);
            // });
        });
    }
}
