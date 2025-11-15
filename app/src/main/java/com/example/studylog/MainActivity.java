package com.example.studylog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView apiResultTextView;
    private boolean hasTriedBackupAPI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();

        // Initialize database with sample data
        DatabaseUtils.initializeWithSampleData(this);
        DatabaseUtils.logUserCount(this);

        setupBottomNavigation();
        setupButtons();
        fetchCurrentTime();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        View root = findViewById(android.R.id.content);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiResultTextView = findViewById(R.id.tv_api_result);
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

    private void setupButtons() {
        setupCallTutorButton();
        setupFindLibraryButton();
    }

    private void setupCallTutorButton() {
        Button btnCallTutor = findViewById(R.id.btn_call_tutor);
        btnCallTutor.setOnClickListener(v -> openPhoneDialer());
    }

    private void setupFindLibraryButton() {
        Button btnFindLibrary = findViewById(R.id.btn_find_library);
        btnFindLibrary.setOnClickListener(v -> openLibraryMap());
    }

    private void openPhoneDialer() {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse(getString(R.string.tutor_phone_number)));
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        }
    }

    private void openLibraryMap() {
        Uri gmmIntentUri = Uri.parse(getString(R.string.library_search_query));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(getString(R.string.google_maps_package));
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void fetchCurrentTime() {
        fetchCurrentTimeAdvanced(false);
    }

    private void fetchCurrentTimeAdvanced() {
        fetchCurrentTimeAdvanced(false);
    }

    private void fetchCurrentTimeAdvanced(boolean useBackupAPI) {
        String apiUrl = useBackupAPI ?
            getString(R.string.backup_time_api_url) :
            getString(R.string.world_time_api_url);

        Log.d(TAG, "Fetching time from API: " + apiUrl + " (backup: " + useBackupAPI + ")");

        // Set loading state
        if (!useBackupAPI) {
            apiResultTextView.setText(getString(R.string.api_result_default));
        } else {
            apiResultTextView.setText("Trying backup API...");
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
            Request.Method.GET,
            apiUrl,

            response -> {
                // Enhanced response logging
                Log.d(TAG, "=== API RESPONSE START ===");
                Log.d(TAG, "URL: " + apiUrl);
                Log.d(TAG, "Response Length: " + (response != null ? response.length() : "null"));
                Log.d(TAG, "Raw Response: " + response);
                Log.d(TAG, "=== API RESPONSE END ===");

                // Validate response before processing
                if (response == null || response.trim().isEmpty()) {
                    Log.e(TAG, "Empty or null response received from API");
                    apiResultTextView.setText("Empty response from server");
                    return;
                }

                // Check if response looks like valid JSON
                if (!response.trim().startsWith("{") && !response.trim().startsWith("[")) {
                    Log.e(TAG, "Response doesn't appear to be JSON: " + response.substring(0, Math.min(100, response.length())));
                    apiResultTextView.setText("Invalid JSON response from server");
                    return;
                }

                handleApiResponse(response, apiUrl);
            },
            error -> {
                // Enhanced error logging
                Log.e(TAG, "=== API ERROR START ===");
                Log.e(TAG, "URL: " + apiUrl);
                Log.e(TAG, "Error Type: " + error.getClass().getSimpleName());
                Log.e(TAG, "Error Message: " + error.getMessage());

                // Log network response details if available
                if (error.networkResponse != null) {
                    Log.e(TAG, "HTTP Status Code: " + error.networkResponse.statusCode);
                    Log.e(TAG, "Response Headers: " + error.networkResponse.headers);

                    if (error.networkResponse.data != null) {
                        String errorData = new String(error.networkResponse.data);
                        Log.e(TAG, "Error Response Data: " + errorData);

                        // Try to parse error response as JSON for more details
                        try {
                            JSONObject errorJson = new JSONObject(errorData);
                            Log.e(TAG, "Parsed Error JSON: " + errorJson.toString(2));
                        } catch (JSONException e) {
                            Log.e(TAG, "Error response is not valid JSON");
                        }
                    }
                } else {
                    Log.e(TAG, "No network response available");
                }

                // Log additional error details
                if (error.getCause() != null) {
                    Log.e(TAG, "Error Cause: " + error.getCause().getMessage());
                }

                Log.e(TAG, "=== API ERROR END ===");

                // Try backup API if primary fails and we haven't tried it yet
                if (!useBackupAPI && !hasTriedBackupAPI) {
                    Log.d(TAG, "Primary API failed, attempting backup API");
                    hasTriedBackupAPI = true;
                    fetchCurrentTimeAdvanced(true);
                } else {
                    handleApiError(error, useBackupAPI);
                }
            }
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("User-Agent", "StudyLog-Android-App/1.0 (Android)");
                headers.put("Accept", "application/json, text/plain, */*");
                headers.put("Accept-Encoding", "gzip, deflate");
                headers.put("Connection", "keep-alive");
                headers.put("Cache-Control", "no-cache");

                Log.d(TAG, "Request Headers: " + headers);
                return headers;
            }

            @Override
            protected void deliverResponse(String response) {
                Log.d(TAG, "Delivering response to UI thread");
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                Log.d(TAG, "Delivering error to UI thread");
                super.deliverError(error);
            }
        };

        // Enhanced timeout and retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
            15000, // 15 seconds timeout (increased for slower networks)
            1, // 1 retry attempt
            2.0f // exponential backoff multiplier
        ));

        Log.d(TAG, "Request configured with 15s timeout, 1 retry attempt");

        queue.add(stringRequest);
    }

    private void handleApiResponse(String response, String apiUrl) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String dateTime;

            if (jsonObject.has("datetime")) {
                dateTime = jsonObject.getString("datetime");
            } else if (jsonObject.has("dateTime")) {
                dateTime = jsonObject.getString("dateTime");
            } else {
                apiResultTextView.setText(getString(R.string.api_result_parse_error));
                return;
            }

            String displayText = getString(R.string.api_result_current_time, dateTime);
            apiResultTextView.setText(displayText);
            Log.d(TAG, "Successfully parsed and displayed time: " + dateTime);

        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error: " + e.getMessage());
            apiResultTextView.setText(getString(R.string.api_result_parse_error));
        }
    }

    private void handleApiError(VolleyError error, boolean wasBackupAPI) {
        String errorMessage;
        if (wasBackupAPI) {
            errorMessage = "Both primary and backup APIs failed";
        } else {
            errorMessage = getString(R.string.api_result_network_error);
        }

        apiResultTextView.setText(errorMessage);
        Log.e(TAG, "Final API error: " + errorMessage);
    }
}
