package com.ai.genie.ui.home.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.ai.genie.R;
import com.ai.genie.common.ConstantValue;
import com.ai.genie.common.SaveData;

public class VoiceActivity extends AppCompatActivity {

    private RadioButton radio1;
    private RadioButton radio2;
    private RadioButton radio3;
    private RadioButton radio4;
    private RadioButton radio5;
    private SaveData saveData;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        saveData = new SaveData(this);

        ivBack = findViewById(R.id.ivBack);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);
        radio5 = findViewById(R.id.radio5);
        Log.e("abc", "=====Voice_rate==========" + saveData.getFloat(ConstantValue.Voice_rate));
        if (saveData.getFloat(ConstantValue.Voice_rate) == 0.5f) {
            radio1.setChecked(true);
        } else if (saveData.getFloat(ConstantValue.Voice_rate) == 0.75f) {
            radio2.setChecked(true);

        } else if (saveData.getFloat(ConstantValue.Voice_rate) == 1f) {
            radio3.setChecked(true);

        } else if (saveData.getFloat(ConstantValue.Voice_rate) == 1.5f) {
            radio4.setChecked(true);

        } else if (saveData.getFloat(ConstantValue.Voice_rate) == 2f) {
            radio5.setChecked(true);

        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save(ConstantValue.Voice_rate, 0.5f);

            }
        });

        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save(ConstantValue.Voice_rate, 0.75f);

            }
        });

        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save(ConstantValue.Voice_rate, 1f);

            }
        });


        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save(ConstantValue.Voice_rate, 1.5f);

            }
        });

        radio5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save(ConstantValue.Voice_rate, 2f);

            }
        });
       /* radio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveData.save(ConstantValue.Voice_rate,0.5f);
            }
        });

        radio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveData.save(ConstantValue.Voice_rate,0.75f);
            }
        });

        radio3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveData.save(ConstantValue.Voice_rate,1f);
            }
        });

        radio4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveData.save(ConstantValue.Voice_rate,1.5f);
            }
        });
        radio5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveData.save(ConstantValue.Voice_rate,2f);
            }
        });*/

    }
}