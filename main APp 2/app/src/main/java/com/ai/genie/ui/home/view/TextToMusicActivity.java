package com.ai.genie.ui.home.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.ai.genie.ui.home.adapter.MusicAdapter;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.databinding.ActivityTextToMusicBinding;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.ui.home.model.UseMostRecentModel;

import java.util.ArrayList;

public class TextToMusicActivity extends AppCompatActivity {


    ActivityTextToMusicBinding binding;
    private ArrayList<UseMostRecentModel> useMostRecentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityTextToMusicBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
        ProgressManager.notify(this);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.rvMusic.setHasFixedSize(true);
        binding.rvMusic.setLayoutManager(new LinearLayoutManager(this));
        MusicAdapter adapter = new MusicAdapter(this, useMostRecentList, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {

            }

            @Override
            public void onItemClick(int pos, String question) {

            }
        });
        binding.rvMusic.setAdapter(adapter);
    }

}