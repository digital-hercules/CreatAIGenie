package com.ai.genie.ui.home.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.ai.genie.R;
import com.ai.genie.ui.home.adapter.NotificationAdapter;
import com.ai.genie.localdatabase.LocalDatabasQueryClass;
import com.ai.genie.ui.home.model.NotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView rvNotification;
    private ArrayList<NotificationModel> otherList = new ArrayList<>();
    private LocalDatabasQueryClass localDatabasQueryClass;

    DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference("notification");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        String userId = mAuth.getCurrentUser().getUid();
        databaseRef1.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e("abc", "==========snapshot.getValue()==========" + snapshot.getValue());
                    NotificationModel notificationModel = snapshot.getValue(NotificationModel.class);
                    otherList.add(notificationModel);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
       /* localDatabasQueryClass= new LocalDatabasQueryClass(this);
        otherList.addAll(localDatabasQueryClass.getAllNotification());*/
        ivBack = findViewById(R.id.ivBack);
        rvNotification = findViewById(R.id.rvNotification);

        ivBack.setOnClickListener(view -> onBackPressed());
        rvNotification.setHasFixedSize(true);
        rvNotification.setLayoutManager(new LinearLayoutManager(this));
        NotificationAdapter adapter = new NotificationAdapter(this, otherList);
        rvNotification.setAdapter(adapter);
    }
}