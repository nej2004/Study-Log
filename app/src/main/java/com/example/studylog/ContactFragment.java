package com.example.studylog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ContactFragment extends Fragment {
    private TextInputEditText etName, etEmail, etSubject, etMessage;
    private MaterialButton btnSend;
    private ProgressBar progressBar;
    private ContactApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initViews(view);
        setupListeners();

        apiService = ContactApiService.getInstance(getContext());

        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        etSubject = view.findViewById(R.id.et_subject);
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.btn_send);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void setupListeners() {
        btnSend.setOnClickListener(v -> {
            if (validateInputs()) {
                sendContactForm();
            }
        });
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
}
