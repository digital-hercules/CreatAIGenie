package com.ai.genie.ui.sidenav.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ai.genie.common.ProgressManager;
import com.ai.genie.databinding.ActivityBrandVoiceBinding;
import com.ai.genie.ui.sidenav.model.Brand;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BrandVoiceActivity extends AppCompatActivity {

    ActivityBrandVoiceBinding binding;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
    private Brand brand;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBrandVoiceBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
//        setContentView(R.layout.activity_brand_voice);
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child(userId).child("brand").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brand = snapshot.getValue(Brand.class);
                // Use user object as needed
                if (brand!=null) {
                    binding.etBrandName.setText(brand.brand_name);
                    binding.etObjective.setText(brand.objective);
                    binding.etBrandTheme.setText(brand.brand_theme);
                    binding.etOther.setText(brand.other_details);
                    binding.switch1.setChecked(brand.enable);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (brand!=null){
                    if (brand.enable!=b) {
                        brand.enable = b;
                        databaseRef.child(userId).child("brand").setValue(brand).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ProgressManager.dismissDialog();

                                if (task.isSuccessful()) {

                                    Toast.makeText(BrandVoiceActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();

                                } else {
                                    Exception exception = task.getException();
                                    Log.e("abc", "===============exception==============" + exception.getMessage());

                                    Toast.makeText(BrandVoiceActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String etBrandName = binding.etBrandName.getText().toString();
                String etObjective = binding.etObjective.getText().toString();
                String etOther = binding.etOther.getText().toString();
                String etBrandTheme = binding.etBrandTheme.getText().toString();

                if (!etBrandName.isEmpty() && !etObjective.isEmpty()&& !etOther.isEmpty()&& !etBrandTheme.isEmpty()){
//                    progressDialog.show();
                    ProgressManager.showDailog(BrandVoiceActivity.this);
                    Brand brand = new Brand();
                    brand.brand_name = etBrandName;
                    brand.enable = binding.switch1.isChecked();
                    brand.objective = etObjective;
                    brand.brand_theme = etBrandTheme;
                    brand.other_details = etOther;

                    databaseRef.child(userId).child("brand").setValue(brand).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressManager.dismissDialog();

                            if (task.isSuccessful()) {

                                Toast.makeText(BrandVoiceActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();

                            } else {
                                Exception exception = task.getException();
                                Log.e("abc", "===============exception==============" + exception.getMessage());

                                Toast.makeText(BrandVoiceActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(BrandVoiceActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}