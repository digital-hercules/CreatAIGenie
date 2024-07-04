package com.ai.genie.ui.authentication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ai.genie.databinding.ActivityWelocmeBinding;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelocmeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelocmeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnGetStart.setOnClickListener(view1 -> {
            startActivity(new Intent(WelcomeActivity.this, OnboardingActivity.UserTypeActivity.class));
            finish();
        });

    }
}