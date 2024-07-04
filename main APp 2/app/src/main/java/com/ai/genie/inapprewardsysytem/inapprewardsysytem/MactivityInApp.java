package com.ai.genie.inapprewardsysytem.inapprewardsysytem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ai.genie.R;
import com.ai.genie.ui.home.DashBoardActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MactivityInApp extends AppCompatActivity {
    private LoginRewardRepo loginRewardRepo;
    private FirebaseAuth mAuth;
    //TextView pointsTextView;
    TextView earnIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //pointsTextView = findViewById(R.id.pointsTextMain);
        loginRewardRepo = new LoginRewardRepo();

        setContentView(R.layout.activity_maininappreward);

        mAuth = FirebaseAuth.getInstance();


        TextView daily = findViewById(R.id.Get_daily);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MactivityInApp.this, Daily_Rewards.class);
                startActivity(i);
            }
        });
        earnIt = findViewById(R.id.uniqueone);

        TextView frnd = findViewById(R.id.frnd_invite);
        frnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MactivityInApp.this, Invite_Freiends.class);
                startActivity(i);
            }
        });
        ImageButton bckbtn = (ImageButton) findViewById(R.id.back_button);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MactivityInApp.this, DashBoardActivity.class);
                startActivity(i);
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            displayPoints(user.getUid());


        }
    }

    private void displayPoints(String userId) {
        loginRewardRepo.getPoints(userId, new LoginRewardRepo.PointsListener() {
            @Override
            public void onPointsFetched(int points) {
                earnIt.setText("Points: " + points);
            }
        });
    }



}