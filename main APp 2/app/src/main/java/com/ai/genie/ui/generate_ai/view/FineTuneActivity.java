package com.ai.genie.ui.generate_ai.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.genie.R;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.databinding.ActivityFineTuneBinding;

public class FineTuneActivity extends AppCompatActivity {

    ActivityFineTuneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFineTuneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize Spinner
        Spinner spinnerYourRole = findViewById(R.id.spinnerYourRole);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bot_model_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerYourRole.setAdapter(adapter);

        // Set title based on intent extra
        int type = getIntent().getIntExtra("type", -1);
        if (type == 1) {
            binding.tvTitle.setText(getResources().getString(R.string.fine_tune_model));
            // Additional logic for creating fine-tune model can be added here
        } else if (type == 2) {
            binding.tvTitle.setText(getResources().getString(R.string.using_template));
            // Additional logic for using template can be added here
        }

        // Set up back button listener
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Set up upload file button listener
        binding.llUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressManager.notify(FineTuneActivity.this);
            }
        });

        // Set up create button listener
        binding.tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressManager.notify(FineTuneActivity.this);
            }
        });
    }
}
