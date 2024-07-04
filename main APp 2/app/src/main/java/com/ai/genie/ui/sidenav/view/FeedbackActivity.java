package com.ai.genie.ui.sidenav.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ai.genie.databinding.ActivityFeedbackBinding;
import com.ai.genie.ui.sidenav.model.Feedback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding binding;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("feedbacks");
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feedback);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
        String userId = mAuth.getCurrentUser().getUid();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        binding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etMessage.getText().toString().length()>0){
                    saveFeedback(userId,binding.etMessage.getText().toString(),binding.rating.getNumStars());
                }

            }
        });

    }

    private void saveFeedback(String userId, String message,int rating) {
        Feedback feedback = new Feedback();
        feedback.message = message;
        feedback.rating= rating;
        progressDialog.show();


        databaseRef.child(userId).setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss(); // Dismiss progress dialog

                    Intent intent = new Intent(FeedbackActivity.this, FeedbackSuccessActivity.class);
                    startActivity(intent);
                } else {
                    Exception exception = task.getException();
                    Log.e("abc", "===============exception==============" + exception.getMessage());

                    Toast.makeText(FeedbackActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss(); // Dismiss progress dialog
                }
            }
        });
    }
}