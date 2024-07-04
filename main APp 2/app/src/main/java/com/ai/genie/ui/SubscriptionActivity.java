package com.ai.genie.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.ai.genie.R;

import java.util.ArrayList;

public class SubscriptionActivity extends AppCompatActivity {

    private ArrayList<SlideModel>image_list = new ArrayList<>();
    private ImageSlider imageSlider;

    private SwitchCompat switch1;
    private LinearLayout llYearly;
    private LinearLayout llFree;
    private LinearLayout llFeature;
    private ImageView ivClose;

    private TextView ivDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        imageSlider = findViewById(R.id.imageSlider);
        switch1 = findViewById(R.id.switch1);
        llYearly = findViewById(R.id.llYearly);
        llFree = findViewById(R.id.llFree);
        llFeature = findViewById(R.id.llFeature);
        ivClose = findViewById(R.id.ivClose);
        ivDescription = findViewById(R.id.ivDescription);
        image_list.add(new SlideModel( R.drawable.ai1, ScaleTypes.CENTER_CROP));// for one image
        image_list.add(new SlideModel(R.drawable.ai2, ScaleTypes.CENTER_CROP)); // you can with title
        image_list.add(new SlideModel(R.drawable.ai3, ScaleTypes.CENTER_CROP)); // you can with title
        image_list.add(new SlideModel(R.drawable.ai4, ScaleTypes.CENTER_CROP)); // you can with title
        image_list.add(new SlideModel(R.drawable.ai5, ScaleTypes.CENTER_CROP)); // you can with title
        imageSlider.setImageList(image_list, ScaleTypes.CENTER_CROP); // for all images

      /*  Animation rightSwipe = AnimationUtils.loadAnimation(this, R.anim.anim_right);
        llFeature.startAnimation(rightSwipe);*/

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    llFree.setBackground(getResources().getDrawable(R.drawable.bg_selected));
                    llYearly.setBackground(getResources().getDrawable(R.drawable.bg_round_corner1));
                    ivDescription.setText(getResources().getString(R.string.sub_text1));
                }else {
                    llYearly.setBackground(getResources().getDrawable(R.drawable.bg_selected));
                    llFree.setBackground(getResources().getDrawable(R.drawable.bg_round_corner1));
                    ivDescription.setText(getResources().getString(R.string.sub_text2));


                }
            }
        });

        llYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivDescription.setText(getResources().getString(R.string.sub_text2));
                llYearly.setBackground(getResources().getDrawable(R.drawable.bg_selected));
                llFree.setBackground(getResources().getDrawable(R.drawable.bg_round_corner1));
            }
        });

        llFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch1.setChecked(true);
                ivDescription.setText(getResources().getString(R.string.sub_text1));
                llFree.setBackground(getResources().getDrawable(R.drawable.bg_selected));
                llYearly.setBackground(getResources().getDrawable(R.drawable.bg_round_corner1));
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}