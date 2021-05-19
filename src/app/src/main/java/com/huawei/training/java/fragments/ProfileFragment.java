/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.training.java.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.training.R;
import com.huawei.training.java.activities.BaseActivity;
import com.huawei.training.java.activities.LoginActivity;
import com.huawei.training.databinding.FragmentProfileBinding;
import com.huawei.training.java.models.UserObj;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.LearningApplication;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /**
     * The Binding.
     */
    FragmentProfileBinding binding;
    /**
     * The User obj.
     */
    UserObj userObj = null;

    /**
     * Instantiates a new Profile fragment.
     */
    public ProfileFragment() {
    }

    /**
     * New instance profile fragment.
     *
     * @param param1 the param 1
     * @param param2 the param 2
     * @return the profile fragment
     */
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        userObj = ((LearningApplication) Objects.requireNonNull(
                getActivity()).getApplicationContext()).getUserObj();
        if (userObj != null) {
            binding.name.setText(userObj.getFirstName());
            binding.email.setText(userObj.getEmailId());
        }
        // account signout
        binding.signout.setOnClickListener(
                view -> {
                    AppUtils utils = new AppUtils(getActivity());
                    utils.clearAllPref();
                    AGConnectAuth.getInstance().signOut();
                    ((BaseActivity) getActivity()).showToast(getString(R.string.logout_success));
                    ((LearningApplication) getActivity().getApplication()).setUserStatus();
                    ((LearningApplication) getActivity().getApplication()).setUserObj(null);
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                });
        return binding.getRoot();
    }
}
