package com.example.studylog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactFragment extends Fragment {
    private TextInputEditText etName, etEmail, etSubject, etMessage;
    private MaterialButton btnSend;
    private ProgressBar progressBar;
    private ContactApiService apiService;

    // New: local views for contact tab functionality
    private Button btnCallTutor, btnFindLibrary;
    private TextView tvApiResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initViews(view);
        setupListeners();

        apiService = ContactApiService.getInstance(getContext());

        // Immediately fetch time and display (only in Contact tab)
        fetchCurrentTime();

        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        etSubject = view.findViewById(R.id.et_subject);
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.btn_send);
        progressBar = view.findViewById(R.id.progress_bar);

        // New views
        btnCallTutor = view.findViewById(R.id.btn_call_tutor);
        btnFindLibrary = view.findViewById(R.id.btn_find_library);
        tvApiResult = view.findViewById(R.id.tv_api_result);
    }

    private void setupListeners() {
        btnSend.setOnClickListener(v -> {
            if (validateInputs()) {
                sendContactForm();
            }
        });

        btnCallTutor.setOnClickListener(v -> openPhoneDialer());
        btnFindLibrary.setOnClickListener(v -> openLibraryMap());

        // Allow tapping the API result to retry fetch
        tvApiResult.setOnClickListener(v -> fetchCurrentTime());
    }

    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(subject)) {
            etSubject.setError("Subject is required");
            etSubject.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(message)) {
            etMessage.setError("Message is required");
            etMessage.requestFocus();
            return false;
        }

        return true;
    }

    private void sendContactForm() {
        showLoading(true);

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        ContactModel contact = new ContactModel(name, email, subject, message);

        apiService.submitContactForm(contact, new ContactApiService.ContactSubmissionListener() {
            @Override
            public void onSuccess(String response) {
                showLoading(false);
                showToast(response);
                clearForm();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                showToast(error);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnSend.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSend.setEnabled(true);
        }
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etName.setText("");
        etEmail.setText("");
        etSubject.setText("");
        etMessage.setText("");
    }

    // New: phone and map helpers
    private void openPhoneDialer() {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse(getString(R.string.tutor_phone_number)));
        if (dialIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(dialIntent);
        }
    }

    private void openLibraryMap() {
        Uri gmmIntentUri = Uri.parse(getString(R.string.library_search_query));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(getString(R.string.google_maps_package));
        if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    // New: fetch current time (local to contact fragment only)
    private void fetchCurrentTime() {
        if (tvApiResult == null) return;
        tvApiResult.setText(getString(R.string.api_result_default));

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String apiUrl = getString(R.string.world_time_api_url);

        StringRequest request = new StringRequest(Request.Method.GET, apiUrl,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String datetime = json.optString(getString(R.string.json_datetime_key), json.optString(getString(R.string.json_simple_time_key), ""));
                        if (datetime == null || datetime.isEmpty()) {
                            tvApiResult.setText(getString(R.string.api_result_parse_error));
                        } else {
                            tvApiResult.setText(getString(R.string.api_result_current_time, datetime));
                        }
                    } catch (JSONException e) {
                        tvApiResult.setText(getString(R.string.api_result_parse_error));
                    }
                },
                error -> {
                    // Only show network error text inside contact tab
                    tvApiResult.setText(getString(R.string.api_result_network_error));
                }
        );

        queue.add(request);
    }
}
