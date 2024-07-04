package com.ai.genie.ui.sidenav.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ai.genie.databinding.ActivityFeedbackSuccessBinding;
import com.ai.genie.ui.home.DashBoardActivity;

public class FeedbackSuccessActivity extends AppCompatActivity {
    ActivityFeedbackSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackSuccessBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
//        setContentView(R.layout.activity_feedback_success);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FeedbackSuccessActivity.this, DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}