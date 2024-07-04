package com.ai.genie.ui.authentication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ai.genie.ui.authentication.adapter.PagerAdapter;
import com.ai.genie.common.ConstantValue;
import com.ai.genie.common.SaveData;
import com.ai.genie.databinding.ActivityOnboardingBinding;
import com.ai.genie.databinding.ActivityUserTypeBinding;

public class OnboardingActivity extends AppCompatActivity {

    ActivityOnboardingBinding binding;
    PagerAdapter adapter;
    private SaveData saveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_onboarding);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        saveData = new SaveData(this);

        adapter = new PagerAdapter(getSupportFragmentManager(), getLifecycle());

        // add Fragments in your ViewPagerFragmentAdapter class
        adapter.addFragment(new Page1());
        adapter.addFragment(new Page2());
        adapter.addFragment(new Page3());
        adapter.addFragment(new Page4());


        // set Orientation in your ViewPager2
        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

            /*    if (position==2){
                    binding.ivSkip.setVisibility(View.GONE);
                }else {
                    binding.ivSkip.setVisibility(View.VISIBLE);

                }
*/
                // Optionally, update the path between points or change their appearance.
                // You can use position to determine the selected page and adjust the indicator accordingly.
            }
        });

      /*  binding.ivSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save_int(ConstantValue.ONBOARDING,1);
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // Start the MainActivity and finish the current SplashActivity
                    startActivity(new Intent(OnboardingActivity.this, DashBoardActivity.class));
                    finish();
                } else {
                    // Start the LoginActivity and finish the current SplashActivity
                    startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });*/
    }

    public static class UserTypeActivity extends AppCompatActivity {

        private ActivityUserTypeBinding binding;
        int type = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_user_type);
            binding = ActivityUserTypeBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);

            binding.btnContinue.setOnClickListener(view1 -> {

                if (binding.radio1.isChecked()) {
                    type = 1;
                }
                if (binding.radio2.isChecked()) {
                    type = 2;
                }
                if (binding.radio3.isChecked()) {
                    type = 3;
                }
                Intent intent = new Intent(UserTypeActivity.this, RegisterActivity.class);
                intent.putExtra(ConstantValue.USER_TYPE, type);
                startActivity(intent);
            });

            binding.tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(UserTypeActivity.this, LoginActivity.class));
                }
            });
        }
    }
}