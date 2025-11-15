package com.example.studylog;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private Button capturePhotoButton;
    private TextView userNameTextView;
    private DatabaseHelper dbHelper;

    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Bundle extras = result.getData().getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profileImageView.setImageBitmap(imageBitmap);
            }
        }
    );

    private ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestPermission(),
        isGranted -> {
            if (isGranted) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    );

    private ActivityResultLauncher<String[]> multiplePermissionsLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestMultiplePermissions(),
        permissions -> {
            boolean cameraGranted = permissions.getOrDefault(Manifest.permission.CAMERA, false);
            boolean storageGranted = permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);

            if (cameraGranted) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }

            if (!storageGranted) {
                Toast.makeText(getContext(), "Storage permission denied - photos won't be saved to gallery", Toast.LENGTH_SHORT).show();
            }
        }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize database helper
        dbHelper = new DatabaseHelper(getContext());

        initializeViews(view);
        setupClickListeners();
        loadUserData();

        return view;
    }

    private void initializeViews(View view) {
        profileImageView = view.findViewById(R.id.profileImageView);
        capturePhotoButton = view.findViewById(R.id.btnCapturePhoto);
        userNameTextView = view.findViewById(R.id.tvUserInfo);
    }

    private void setupClickListeners() {
        capturePhotoButton.setOnClickListener(v -> {
            checkPermissionsAndOpenCamera();
        });
    }

    private void checkPermissionsAndOpenCamera() {
        boolean cameraPermissionGranted = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storagePermissionGranted = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        // For Android 10+ (API 29+), WRITE_EXTERNAL_STORAGE is not needed for app-specific directories
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= 29) {
            storagePermissionGranted = true; // Scoped storage handles this
        }

        if (cameraPermissionGranted && storagePermissionGranted) {
            openCamera();
        } else if (cameraPermissionGranted && !storagePermissionGranted) {
            // Camera permission granted but storage denied - still allow camera usage
            openCamera();
            Toast.makeText(getContext(), "Storage permission denied - photo won't be saved to gallery", Toast.LENGTH_SHORT).show();
        } else {
            // Request missing permissions
            if (!cameraPermissionGranted && !storagePermissionGranted && sdkVersion < 29) {
                multiplePermissionsLauncher.launch(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                });
            } else if (!cameraPermissionGranted) {
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(getContext(), "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserData() {
        // Read user data from SQLite database
        try {
            String userName = dbHelper.getUserName();

            if (userName != null && !userName.isEmpty()) {
                // Display user name as requested in the example
                userNameTextView.setText("User: " + userName);
            } else {
                // Fallback if no user is found
                userNameTextView.setText("User: No user found");
            }

            // Optional: Load complete user data for future use
            User user = dbHelper.getFirstUser();
            if (user != null) {
                // Could be used for additional profile information
                // For now, we just log it for debugging
                android.util.Log.d("ProfileFragment", "Loaded user: " + user.toString());
            }

        } catch (Exception e) {
            // Handle any database errors gracefully
            android.util.Log.e("ProfileFragment", "Error loading user data: " + e.getMessage());
            userNameTextView.setText("User: Error loading data");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close database connection to prevent memory leaks
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

