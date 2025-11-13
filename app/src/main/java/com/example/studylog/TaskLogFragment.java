package com.example.studylog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studylog.R;

public class TaskLogFragment extends Fragment {
    private EditText etTaskName;
    private Button btnSaveTask;
    private TaskDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTaskName = view.findViewById(R.id.etTaskName);
        btnSaveTask = view.findViewById(R.id.btnSaveTask);
        dbHelper = new TaskDatabaseHelper(requireContext());

        btnSaveTask.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String taskName = etTaskName.getText().toString().trim();

        if (taskName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a task name", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.insertTask(taskName);

        if (result != -1) {
            Toast.makeText(requireContext(), "Task Saved!", Toast.LENGTH_SHORT).show();
            etTaskName.setText("");
        } else {
            Toast.makeText(requireContext(), "Failed to save task", Toast.LENGTH_SHORT).show();
        }
    }
}
