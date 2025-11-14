/**
 * VOLLEY REQUEST IMPLEMENTATION - COMPLETE EXAMPLE
 * This file demonstrates the exact Volley request pattern specified by the user
 */

// STANDARD VOLLEY REQUEST IMPLEMENTATION
private void makeAPIRequest() {
    // 1. Set up the API URL
    String apiUrl = "https://worldtimeapi.org/api/timezone/Europe/London"; // Use your actual API URL

    // 2. Update UI to show loading state
    apiResultTextView.setText("Loading...");

    // 3. Create Volley RequestQueue
    RequestQueue queue = Volley.newRequestQueue(this);

    // 4. Create StringRequest with proper error handling
    StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // 5. Parse the JSON response
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("datetime"); // Parse the required data from the response

                    // 6. Update the TextView with the data (SUCCESS STATE)
                    apiResultTextView.setText("Data: " + data); // Update the TextView with the data

                    Log.d("API_SUCCESS", "Data fetched successfully: " + data);

                } catch (JSONException e) {
                    e.printStackTrace();

                    // 7. Handle JSON parsing errors (ERROR STATE)
                    apiResultTextView.setText("Error parsing data");
                    Log.e("API_ERROR", "JSON parsing failed: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 8. Handle network/request errors (ERROR STATE)
                apiResultTextView.setText("Error fetching data");

                // Log detailed error information
                Log.e("API_ERROR", "Request failed: " + error.getMessage());
                if (error.networkResponse != null) {
                    Log.e("API_ERROR", "Status Code: " + error.networkResponse.statusCode);
                }
            }
        }) {

        @Override
        public Map<String, String> getHeaders() {
            // 9. Add headers if needed
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");
            headers.put("User-Agent", "YourApp/1.0");
            return headers;
        }
    };

    // 10. Set timeout and retry policy
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
        15000, // 15 seconds timeout
        1,     // 1 retry attempt
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    ));

    // 11. Add request to queue
    queue.add(stringRequest);
}

// UI STATE MANAGEMENT EXAMPLES

// SUCCESS STATE - Show fetched data
private void showSuccessState(String data) {
    apiResultTextView.setText("Current Time: " + data);
    apiResultTextView.setTextColor(getResources().getColor(android.R.color.black));
    apiResultTextView.setBackgroundColor(getResources().getColor(android.R.color.white));
}

// ERROR STATE - Show error message
private void showErrorState(String errorMessage) {
    apiResultTextView.setText(errorMessage);
    apiResultTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
    apiResultTextView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
}

// LOADING STATE - Show loading indicator
private void showLoadingState() {
    apiResultTextView.setText("Loading current time...");
    apiResultTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
    apiResultTextView.setBackgroundColor(getResources().getColor(android.R.color.white));
}

// COMPLETE IMPLEMENTATION WITH UI STATES
private void fetchAPIDataWithUIStates() {
    // Show loading state
    showLoadingState();

    String apiUrl = getString(R.string.world_time_api_url);
    RequestQueue queue = Volley.newRequestQueue(this);

    StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
        response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String datetime = jsonObject.getString("datetime");

                // Show success state
                showSuccessState(datetime);

            } catch (JSONException e) {
                e.printStackTrace();
                // Show error state for parsing errors
                showErrorState("Error parsing server response");
            }
        },
        error -> {
            // Show error state for network errors
            if (error instanceof TimeoutError) {
                showErrorState("Request timed out - check connection");
            } else if (error instanceof NoConnectionError) {
                showErrorState("No internet connection");
            } else {
                showErrorState("Error fetching data from server");
            }
        }
    ) {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("User-Agent", "StudyLog-Android-App");
            return headers;
        }
    };

    stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 2.0f));
    queue.add(stringRequest);
}
