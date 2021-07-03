package com.example.dclv_v2126.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.dclv_v2126.SessionManager;
import com.example.dclv_v2126.databinding.FragmentNotificationsBinding;

public class
ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentNotificationsBinding binding;
    private Button btn_logout;
    SessionManager sessionManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btn_logout = binding.button6;

        btn_logout.setOnClickListener((v)-> {sessionManager.logout();});
//        final TextView textView = binding.text_home1;
//        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}