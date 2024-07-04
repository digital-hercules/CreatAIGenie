package com.ai.genie.ui.fragement;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ai.genie.adapter.BlogAdapter;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.databinding.FragmentDocumentBinding;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.models.BlogModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DocumentFragment extends Fragment {

    private FragmentDocumentBinding binding;

    private ArrayList<BlogModel> list = new ArrayList<>();
    private DatabaseReference databaseReference;
    BlogAdapter blogAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDocumentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvBlog.setLayoutManager(linearLayoutManager);
        binding.rvBlog.setHasFixedSize(true);
        ProgressManager.showDailog(getActivity());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("blogs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BlogModel blogModel = snapshot.getValue(BlogModel.class);
                        blogModel.id = snapshot.getKey();
                        list.add(blogModel);
                    }
                    blogAdapter = new BlogAdapter(getActivity(), list, new ClickListner() {
                        @Override
                        public void onItemClickCopy(int pos, String answer) {

                        }

                        @Override
                        public void onItemClick(int pos, String question) {
                        }
                    });
                    binding.rvBlog.setAdapter(blogAdapter);
                    ProgressManager.dismissDialog();

                } else {
                    ProgressManager.dismissDialog();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("abc", "============onCancelled================" + databaseError.toString());
                ProgressManager.dismissDialog();
            }
        });

        return view;
    }

}
