package com.ai.genie.ui.home.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ai.genie.R;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.databinding.ActivityDashBoardBinding;
import com.ai.genie.databinding.ActivityTextToVideoBinding;

public class TextToVideoActivity extends AppCompatActivity {


    ActivityTextToVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityTextToVideoBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ProgressManager.notify(this);

    }

}