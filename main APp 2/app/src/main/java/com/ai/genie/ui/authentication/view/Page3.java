package com.ai.genie.ui.authentication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ai.genie.common.ConstantValue;
import com.ai.genie.common.SaveData;
import com.ai.genie.databinding.FragmentPage3Binding;
import com.ai.genie.ui.home.DashBoardActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Page3 extends Fragment {
    private FragmentPage3Binding binding;

    private SaveData saveData;
    public Page3(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentPage3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        saveData = new SaveData(getActivity());

        binding.llSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save_int(ConstantValue.ONBOARDING,1);
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // Start the MainActivity and finish the current SplashActivity
                    startActivity(new Intent(getActivity(), DashBoardActivity.class));
                    getActivity().finish();
                } else {
                    // Start the LoginActivity and finish the current SplashActivity
                    startActivity(new Intent(getActivity(), WelcomeActivity.class));
                    getActivity().finish();

                }
            }
        });

        return view;

    }
}

