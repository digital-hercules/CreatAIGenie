package com.ai.genie.ui.sidenav.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.genie.R;
import com.ai.genie.databinding.ActivityPlanBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class PlanActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    private ActivityPlanBinding binding;

    private TextView registerButton;
    private LinearLayout generalUserBox;
    private LinearLayout creatorBox;
    private LinearLayout enterpriseBox;
    private String selectedPlanAmount;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use View Binding
        binding = ActivityPlanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize views using binding
        registerButton = binding.registerButton;
        generalUserBox = binding.generalUserBox;
        creatorBox = binding.creatorBox;
        enterpriseBox = binding.enterpriseBox;

        // Initialize Razorpay
        Checkout.preload(getApplicationContext());
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });

        // Set click listeners for each box
        generalUserBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setText(R.string.price_general_user);
                selectedPlanAmount = "100"; // Replace with actual amount for General User
            }
        });

        creatorBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setText(R.string.price_creator);
                selectedPlanAmount = "200"; // Replace with actual amount for Creator
            }
        });

        enterpriseBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setText(R.string.price_enterprise);
                selectedPlanAmount = "300"; // Replace with actual amount for Enterprise
            }
        });

        // Set click listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment(selectedPlanAmount);
            }
        });

        // Set click listener for the back button
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void startPayment(String amount) {
        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_XNfjSWN8qIG5Jv"); // Replace with your Razorpay key ID

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Your Company Name");
            options.put("description", "Subscription Charges");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR"); // Change to your currency if needed
            options.put("amount", Integer.parseInt(amount) * 100); // Amount in paise

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(this, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("Payment Failed:\nError Code: " + i + "\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
