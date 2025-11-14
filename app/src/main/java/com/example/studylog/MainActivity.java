package com.example.studylog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.studylog.R;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView apiResultTextView;
    private boolean hasTriedBackupAPI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupBottomNavigation();
        setupButtons();
        fetchCurrentTime();
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
        // Use the standard Volley request implementation
        makeStandardVolleyRequest();
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
            Log.d(TAG, "=== PARSING API RESPONSE ===");
            Log.d(TAG, "API URL: " + apiUrl);
            Log.d(TAG, "Response length: " + response.length() + " characters");

            JSONObject jsonObject = new JSONObject(response);
            Log.d(TAG, "JSON parsed successfully");
            Log.d(TAG, "Available JSON keys: " + jsonObject.keys());

            String datetime = null;
            String source = "Unknown";

            // Handle WorldTime API format (primary)
            if (jsonObject.has("datetime")) {
                datetime = jsonObject.getString("datetime");
                source = "WorldTime API";
                Log.d(TAG, "Found 'datetime' field: " + datetime);
            }
            // Handle TimeAPI.io format (alternative)
            else if (jsonObject.has("dateTime")) {
                datetime = jsonObject.getString("dateTime");
                source = "TimeAPI.io";
                Log.d(TAG, "Found 'dateTime' field: " + datetime);
            }
            // Handle other possible formats
            else if (jsonObject.has("time")) {
                datetime = jsonObject.getString("time");
                source = "Generic API";
                Log.d(TAG, "Found 'time' field: " + datetime);
            }
            // Handle Unix timestamp format
            else if (jsonObject.has("timestamp")) {
                long timestamp = jsonObject.getLong("timestamp");
                datetime = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US)
                    .format(new java.util.Date(timestamp * 1000));
                source = "Timestamp API";
                Log.d(TAG, "Found 'timestamp' field, converted to: " + datetime);
            }

            if (datetime != null && !datetime.trim().isEmpty()) {
                // Log successful extraction
                Log.d(TAG, "DateTime successfully extracted: " + datetime);
                Log.d(TAG, "Source: " + source);

                // Format and display the result
                String formattedResult = getString(R.string.api_result_current_time, datetime);
                Log.d(TAG, "Formatted result: " + formattedResult);

                // Update UI on main thread
                runOnUiThread(() -> {
                    apiResultTextView.setText(formattedResult);
                    // Remove any click listeners since we succeeded
                    apiResultTextView.setOnClickListener(null);
                });

                Log.d(TAG, "Successfully updated UI with time from " + source);

                // Log additional useful information if available
                logAdditionalApiInfo(jsonObject);

            } else {
                // No datetime field found
                Log.e(TAG, "No datetime field found in API response");
                Log.e(TAG, "Available fields: " + jsonObject.keys());
                Log.e(TAG, "Full JSON response for debugging: " + jsonObject.toString(2));

                runOnUiThread(() -> {
                    apiResultTextView.setText("No time data found in API response");
                });
            }

        } catch (JSONException e) {
            Log.e(TAG, "=== JSON PARSING ERROR ===");
            Log.e(TAG, "Error message: " + e.getMessage());
            Log.e(TAG, "Error location: " + e.getClass().getSimpleName());
            Log.e(TAG, "Response that failed to parse (first 500 chars): " +
                response.substring(0, Math.min(500, response.length())));

            // Try to identify the issue
            if (!response.trim().startsWith("{")) {
                Log.e(TAG, "Response doesn't start with '{' - not valid JSON object");
                runOnUiThread(() -> apiResultTextView.setText("Invalid JSON format received"));
            } else {
                Log.e(TAG, "Response appears to be JSON but parsing failed");
                runOnUiThread(() -> apiResultTextView.setText(getString(R.string.api_result_parse_error)));
            }

            // Log stack trace for debugging
            Log.e(TAG, "Full stack trace:", e);
        }
    }

    private void logAdditionalApiInfo(JSONObject jsonObject) {
        try {
            // Log timezone info if available
            if (jsonObject.has("timezone")) {
                Log.d(TAG, "Timezone: " + jsonObject.getString("timezone"));
            }
            if (jsonObject.has("utc_offset")) {
                Log.d(TAG, "UTC Offset: " + jsonObject.getString("utc_offset"));
            }
            if (jsonObject.has("day_of_week")) {
                Log.d(TAG, "Day of week: " + jsonObject.getInt("day_of_week"));
            }
            if (jsonObject.has("day_of_year")) {
                Log.d(TAG, "Day of year: " + jsonObject.getInt("day_of_year"));
            }
        } catch (JSONException e) {
            Log.d(TAG, "Additional API info logging failed: " + e.getMessage());
        }
    }

    /**
     * Standard Volley Request Implementation
     * This method follows the exact pattern specified by the user requirements
     */
    private void makeStandardVolleyRequest() {
        // Use your actual API URL
        String apiUrl = getString(R.string.world_time_api_url);

        // Set loading state in UI
        apiResultTextView.setText("Loading current time...");

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        // Parse the required data from the response (datetime field for WorldTime API)
                        String data = jsonObject.getString("datetime");
                        // Update the TextView with the data
                        apiResultTextView.setText("Data: " + data);
                        Log.d(TAG, "Successfully fetched and displayed data: " + data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        apiResultTextView.setText("Error parsing data");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle the error response
                    Log.e(TAG, "Standard Volley request failed: " + error.getMessage());
                    if (error.networkResponse != null) {
                        Log.e(TAG, "HTTP Status Code: " + error.networkResponse.statusCode);
                    }

                    // Show basic error message first
                    apiResultTextView.setText("Error fetching data - Tap to retry with diagnostics");

                    // Add tap-to-retry with advanced diagnostics
                    apiResultTextView.setOnClickListener(v -> {
                        Log.d(TAG, "User tapped error message, starting advanced diagnostics");
                        apiResultTextView.setOnClickListener(null); // Remove click listener
                        fetchCurrentTimeAdvanced(); // Use advanced method with backup APIs
                    });
                }
            }) {

            @Override
            public java.util.Map<String, String> getHeaders() {
                // Add proper headers for the API request
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("User-Agent", "StudyLog-Android-App");
                return headers;
            }
        };

        // Add timeout and retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
            15000, // 15 seconds timeout
            1, // 1 retry
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(stringRequest);
    }

    private void handleApiError(VolleyError error, boolean wasBackupAPI) {
        Log.e(TAG, "API request failed", error);

        String errorMessage = getString(R.string.api_result_network_error);

        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            Log.e(TAG, "HTTP Status Code: " + statusCode);

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

        // Add tap to retry functionality
        errorMessage += " - Tap to retry";
        apiResultTextView.setText(errorMessage);
        apiResultTextView.setOnClickListener(v -> {
            hasTriedBackupAPI = false; // Reset backup API flag
            apiResultTextView.setOnClickListener(null); // Remove click listener

            // Test all APIs to see which one works
            testAllAPIs();
        });

        Log.e(TAG, "Final error message: " + errorMessage);
    }

    private void testAllAPIs() {
        apiResultTextView.setText("Testing APIs...");
        Log.d(TAG, "Starting comprehensive API test");

        // Test primary WorldTime API
        testSingleAPI("https://worldtimeapi.org/api/timezone/Europe/London", "datetime", "Primary WorldTime API");
    }

    private void testSingleAPI(String url, String jsonKey, String apiName) {
        Log.d(TAG, "Testing " + apiName + ": " + url);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
            Request.Method.GET,
            url,
            response -> {
                Log.d(TAG, apiName + " SUCCESS: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has(jsonKey)) {
                        String datetime = jsonObject.getString(jsonKey);
                        String result = "✅ " + apiName + " works!\nTime: " + datetime;
                        apiResultTextView.setText(result);
                        Log.d(TAG, apiName + " parsed successfully: " + datetime);
                    } else {
                        Log.e(TAG, apiName + " missing " + jsonKey + " field");
                        apiResultTextView.setText("❌ " + apiName + " missing " + jsonKey + " field");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, apiName + " JSON parse error", e);
                    apiResultTextView.setText("❌ " + apiName + " JSON error: " + e.getMessage());
                }
            },
            error -> {
                Log.e(TAG, apiName + " FAILED", error);
                String errorMsg = "❌ " + apiName + " failed: ";

                if (error.networkResponse != null) {
                    errorMsg += "HTTP " + error.networkResponse.statusCode;
                } else if (error instanceof TimeoutError) {
                    errorMsg += "Timeout";
                } else if (error instanceof NoConnectionError) {
                    errorMsg += "No connection";
                } else {
                    errorMsg += "Network error";
                }

                // Try alternative APIs
                if (url.contains("worldtimeapi.org") && url.startsWith("https")) {
                    Log.d(TAG, "HTTPS failed, trying HTTP");
                    testSingleAPI("http://worldtimeapi.org/api/timezone/Europe/London", "datetime", "HTTP WorldTime API");
                } else if (url.contains("worldtimeapi.org")) {
                    Log.d(TAG, "WorldTime API failed, trying TimeAPI.io");
                    testSingleAPI("https://timeapi.io/api/Time/current/zone?timeZone=Europe/London", "dateTime", "TimeAPI.io");
                } else {
                    // All APIs failed
                    apiResultTextView.setText(errorMsg + "\n\nAll APIs failed. Check internet connection.");
                }
            }
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("User-Agent", "StudyLog-Android-App/1.0");
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Set timeout
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
            15000, // 15 seconds
            0, // no retries
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(stringRequest);
    }
}
