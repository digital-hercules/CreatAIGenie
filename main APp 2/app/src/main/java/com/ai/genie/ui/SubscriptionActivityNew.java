package com.ai.genie.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.ai.genie.adapter.ViewPagerAdapter;
import com.ai.genie.databinding.ActivitySubscriptionNewBinding;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.models.ViewpagerModel;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubscriptionActivityNew extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {

    ActivitySubscriptionNewBinding binding;
    private ArrayList<ViewpagerModel> list = new ArrayList<>();
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionNewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //        setContentView(R.layout.activity_chat_new);
        Checkout.preload(getApplicationContext());
        alertDialogBuilder = new AlertDialog.Builder(SubscriptionActivityNew.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });
        ViewpagerModel model = new ViewpagerModel();
        model.displayIndicator = true;
        model.plan = 1;
        ViewpagerModel model1 = new ViewpagerModel();
        model1.displayIndicator = true;
        model1.plan = 2;
        ViewpagerModel model2 = new ViewpagerModel();
        model2.displayIndicator = true;
        model2.plan = 3;

        list.add(model);
        list.add(model1);
        list.add(model2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, list, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {

            }

            @Override
            public void onItemClick(int pos, String question) {
                startPayment(question);
            }
        });
        binding.viewPager.setClipToPadding(false);
        binding.viewPager.setClipChildren(false);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.viewPager.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(8));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1 - Math.abs(position);

                page.setScaleY(0.8f + v * 0.2f);
            }
        });
        binding.viewPager.setPageTransformer(transformer);

        binding.tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse("https://razorpay.com/sample-application/"));
                startActivity(httpIntent);
            }
        });


    }

    public void startPayment(String amount) {
        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_XNfjSWN8qIG5Jv");


        try {
            JSONObject options = new JSONObject("MVHJzXHiEby3m1QYmLXSE5Xw");
            co.open(this, options);
        } catch (JSONException e) {
         /*   Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();*/
            e.printStackTrace();
        }

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "USD");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(this, options);
        } catch (Exception e) {
           /* Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();*/
            e.printStackTrace();
        }
        /* }*/


    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */


    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
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
            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}