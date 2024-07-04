package com.ai.genie.ui.assistant.view;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ai.genie.ui.assistant.adapter.AssistantAdapter;
import com.ai.genie.ui.assistant.adapter.AssistantCatAdapter;
import com.ai.genie.databinding.FragmentAssistantBinding;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.ui.assistant.model.AssistantModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssistantFragment extends Fragment {

    private FragmentAssistantBinding binding;

    private DatabaseReference databaseReference;
    private ArrayList<AssistantModel> sports = new ArrayList<>();
    private ArrayList<String> alType = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAssistantBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        alType.add("All");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("assistant");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AssistantModel marketModel = snapshot.getValue(AssistantModel.class);
                        marketModel.id = snapshot.getKey();

                        if (marketModel.suggestion.size() > 0) {
                            marketModel.suggestion.remove(0);
                            // Check if suggestion is null before accessing its elements
                            Log.e("abc", "==========marketModel.suggestion=========" + marketModel.suggestion);
                        }

                        Log.e("abc", "=========snapshot.getValue()==========" + snapshot.getValue());
                        sports.add(marketModel);
                        if (!alType.contains(marketModel.assistant)) {
                            alType.add(marketModel.assistant);
                        }
                    }

                    Log.e("abc", "============sports.size()============" + sports.size());
                    binding.rvAssistant.setHasFixedSize(true);
                    binding.rvAssistant.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    AssistantAdapter adapterSports = new AssistantAdapter(getActivity(), sports);
                    binding.rvAssistant.setAdapter(adapterSports);
                    binding.rvType.setHasFixedSize(true);
                    binding.rvType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    AssistantCatAdapter adapterType = new AssistantCatAdapter(getActivity(), alType, new ClickListner() {
                        @Override
                        public void onItemClickCopy(int pos, String answer) {
                        }

                        @Override
                        public void onItemClick(int pos, String question) {
                            refreshList(question);
                        }
                    });
                    binding.rvType.setAdapter(adapterType);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("abc", "============onCancelled================" + databaseError.toString());
            }
        });
        return view;
        /*View view = inflater.inflate(R.layout.fragment_document, container, false);
        return view;*/
    }

    private void refreshList(String type) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("assistant");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sports.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AssistantModel marketModel = snapshot.getValue(AssistantModel.class);
                        marketModel.id = snapshot.getKey();
                        marketModel.suggestion.remove(0);
                        // Check if suggestion is null before accessing its elements
                        Log.e("abc", "==========marketModel.suggestion=========" + marketModel.suggestion);
                        Log.e("abc", "=========snapshot.getValue()==========" + snapshot.getValue());
                        if (type.equals("All")) {
                            sports.add(marketModel);

                        } else {
                            if (marketModel.assistant.equals(type)) {
                                sports.add(marketModel);
                            }
                        }
                    }

                    Log.e("abc", "============sports.size()============" + sports.size());
                    binding.rvAssistant.setHasFixedSize(true);
                    binding.rvAssistant.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                    AssistantAdapter adapterSports = new AssistantAdapter(getActivity(), sports);
                    binding.rvAssistant.setAdapter(adapterSports);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("abc", "============onCancelled================" + databaseError.toString());
            }
        });
    }


}
