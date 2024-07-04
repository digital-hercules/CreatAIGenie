package com.ai.genie.ui;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.genie.R;
import com.ai.genie.databinding.ActivitySubscription1Binding;
import com.razorpay.Checkout;

public class SubscriptionActivity1 extends AppCompatActivity  {

    private AlertDialog.Builder alertDialogBuilder;

    private ActivitySubscription1Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscription1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        setContentView(R.layout.activity_subscription1);
        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());

        // ...

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });
        // ...
        binding.llProMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llProMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.sub_color)));
                binding.llStarterMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.llWordCreditMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.rlProPlan.setVisibility(View.VISIBLE);
                binding.rlStarter.setVisibility(View.GONE);
                binding.rlWords.setVisibility(View.GONE);
                binding.llIndicator.setVisibility(View.VISIBLE);

            }
        });

        binding.llStarterMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llProMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.llStarterMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.sub_color)));
                binding.llWordCreditMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.rlProPlan.setVisibility(View.GONE);
                binding.rlStarter.setVisibility(View.VISIBLE);
                binding.rlWords.setVisibility(View.GONE);
                binding.llIndicator.setVisibility(View.VISIBLE);

            }
        });

        binding.llWordCreditMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llProMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.llStarterMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                binding.llWordCreditMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.sub_color)));
                binding.rlProPlan.setVisibility(View.GONE);
                binding.rlStarter.setVisibility(View.GONE);
                binding.llIndicator.setVisibility(View.GONE);
                binding.rlWords.setVisibility(View.VISIBLE);
            }
        });
        selectPlan(false);

        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectPlan(b);
            }
        });

        binding.tvProUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        binding.tvStarterUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        binding.tvWordUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
    private void selectPlan(boolean check){

        if (check){
            binding.tvProYearlyPrice.setVisibility(View.VISIBLE);
            binding.tvProSaving.setVisibility(View.VISIBLE);
            binding.tvStarterYearlyPrice.setVisibility(View.VISIBLE);
            binding.tvStarterSaving.setVisibility(View.VISIBLE);
            binding.tvProPrice.setText("$37");
            binding.tvStarterPrice.setText("$15");

        }else {
            binding.tvProYearlyPrice.setVisibility(View.GONE);
            binding.tvProSaving.setVisibility(View.GONE);
            binding.tvStarterYearlyPrice.setVisibility(View.GONE);
            binding.tvStarterSaving.setVisibility(View.GONE);
            binding.tvProPrice.setText("$49");
            binding.tvStarterPrice.setText("$20");

        }

    }



}