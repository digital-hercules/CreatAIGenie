package com.ai.genie.ui.generate_ai.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.ai.genie.ui.generate_ai.adapter.MarketNewAdapter;
import com.ai.genie.ui.generate_ai.adapter.MarketPlaceAdapter;
import com.ai.genie.databinding.ActivityMarketPlaceBinding;
import com.ai.genie.ui.generate_ai.model.MarketModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MarketPlaceActivity extends AppCompatActivity {

    private ArrayList<MarketModel> sports = new ArrayList<>();
    private DatabaseReference databaseReference;
    ActivityMarketPlaceBinding binding;

    MarketPlaceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_market_place);
        binding = ActivityMarketPlaceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*databaseReference = FirebaseDatabase.getInstance().getReference().child("marketplace");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        MarketModel marketModel = snapshot.getValue(MarketModel.class);
                        marketModel.id = snapshot.getKey();
                        sports.add(marketModel);
                    }
                    binding.rvMarketPlace.setHasFixedSize(true);
                    binding.rvMarketPlace.setLayoutManager(new GridLayoutManager(MarketPlaceActivity.this,2));
                    MarketPlaceAdapter adapterSports = new MarketPlaceAdapter(MarketPlaceActivity.this,sports);
                    binding.rvMarketPlace.setAdapter(adapterSports);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("abc","============onCancelled================"+databaseError.toString());

            }
        });*/

        binding.rvMarketPlace.setHasFixedSize(true);
        binding.rvMarketPlace.setLayoutManager(new GridLayoutManager(MarketPlaceActivity.this, 1));
        MarketNewAdapter adapterSports = new MarketNewAdapter(MarketPlaceActivity.this, sports);
        binding.rvMarketPlace.setAdapter(adapterSports);
    }
}