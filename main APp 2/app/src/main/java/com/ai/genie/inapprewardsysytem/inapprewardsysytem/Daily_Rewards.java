package com.ai.genie.inapprewardsysytem.inapprewardsysytem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import com.ai.genie.databinding.ActivityDailyRewardsBinding;
import com.ai.genie.databinding.ActivityOnboardingBinding;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.TextView;



import com.ai.genie.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Daily_Rewards extends AppCompatActivity {

    private LoginRewardRepo loginRewardRepo;
    private FirebaseAuth auth;


    private
    ActivityDailyRewardsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_rewards);
        binding = ActivityDailyRewardsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        loginRewardRepo = new LoginRewardRepo();

        FirebaseUser currentUser = auth.getCurrentUser();


        binding.redeemnowbuttondailyreward6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    claimReward(currentUser.getUid());
                }
            }
        });
        binding.closedailyrewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Daily_Rewards.this, MactivityInApp.class);
                startActivity(i);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    private void checkRewardStatus(String userId) {
        loginRewardRepo.checkRewardStatus(userId, new LoginRewardRepo.OnRewardStatusListener() {
            @Override
            public void onRewardStatus(RewardStatus rewardStatus) {
                if (rewardStatus.available) {
                    //rewardStatusText.setText("Reward available! Streak: " + rewardStatus.streak);
                    binding.redeemnowbuttondailyreward6.setEnabled(true);
                } else {
                    //rewardStatusText.setText("Next reward available in: " + rewardStatus.remainingTime / 1000 + " seconds");
                    binding.redeemnowbuttondailyreward6.setEnabled(false);
                }

            }
        });
    }
    private void claimReward(String userId) {
        loginRewardRepo.claimReward(userId, new LoginRewardRepo.OnRewardClaimedListener() {
            @Override
            public void onRewardClaimed(boolean success) {
                if (success) {
                    Toast.makeText(Daily_Rewards.this, "Reward claimed!", Toast.LENGTH_SHORT).show();
                    checkRewardStatus(userId);
                } else {
                    Toast.makeText(Daily_Rewards.this, "Reward not yet available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}