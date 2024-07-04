package com.ai.genie.inapprewardsysytem.inapprewardsysytem;


import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.ai.genie.R;
import com.ai.genie.ui.authentication.model.User;
import com.ai.genie.ui.sidenav.view.EditProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class LoginRewardRepo {
    private FirebaseDatabase database;

    public interface PointsListener {
        void onPointsFetched(int points);
    }

    public LoginRewardRepo() {
        database = FirebaseDatabase.getInstance();
    }

    public interface OnRewardStatusListener {
        void onRewardStatus(RewardStatus rewardStatus);
    }

    public void checkRewardStatus(String userId, final OnRewardStatusListener listener) {
        DatabaseReference userRewardsRef = database.getReference("users").child(userId);

        userRewardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               User user = snapshot.getValue(User.class);
                // Use user object as needed
                if (user!=null) {
                    Log.e("abc","============user.points=================="+user.points);
                    Log.e("abc","============user.points=================="+user.lastClaimTimestamp);
                    Log.e("abc","============user.points=================="+user.streak);

                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    long timeSinceLastClaim = currentTime - user.lastClaimTimestamp;

                    if (timeSinceLastClaim >= 24 * 60 * 60 * 1000) { // 24 hours in milliseconds
                        listener.onRewardStatus(new RewardStatus(true, user.streak, null,user.points));
                    } else {
                        long remainingTime = 24 * 60 * 60 * 1000 - timeSinceLastClaim;
                        listener.onRewardStatus(new RewardStatus(false, user.streak, remainingTime,user.points));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors appropriately
            }
        });
    }

    public interface OnRewardClaimedListener {
        void onRewardClaimed(boolean success);
    }

    public void claimReward(String userId, final OnRewardClaimedListener listener) {
        DatabaseReference userRewardsRef = database.getReference("users").child(userId);

        userRewardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long lastClaimTimestamp = snapshot.child("lastClaimTimestamp").getValue(Long.class) != null
                        ? snapshot.child("lastClaimTimestamp").getValue(Long.class) : 0L;
                int streak = snapshot.child("streak").getValue(Integer.class) != null
                        ? snapshot.child("streak").getValue(Integer.class) : 0;
                int points = snapshot.child("points").getValue(Integer.class) != null
                        ? snapshot.child("points").getValue(Integer.class) : 0;

                long currentTime = Calendar.getInstance().getTimeInMillis();
                long timeSinceLastClaim = currentTime - lastClaimTimestamp;

                if (timeSinceLastClaim >= 24 * 60 * 60 * 1000) {
                    userRewardsRef.child("lastClaimTimestamp").setValue(currentTime);
                    userRewardsRef.child("streak").setValue(streak + 1);
                    userRewardsRef.child("points").setValue(points + 10);
                    listener.onRewardClaimed(true);
                } else {
                    listener.onRewardClaimed(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onRewardClaimed(false);
            }
        });
    }

    public void getPoints(String userId, final PointsListener pointsListener) {
        final DatabaseReference userRewardsRef = database.getReference("users").child(userId);
        userRewardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int points = snapshot.child("points").getValue(Integer.class) != null ? snapshot.child("points").getValue(Integer.class) : 0;
                pointsListener.onPointsFetched(points);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors appropriately (e.g., log the error, show a message to the user)
            }
        });
    }
}