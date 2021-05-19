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
package com.huawei.training.kotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.training.R
import com.huawei.training.kotlin.activities.BaseActivity
import com.huawei.training.kotlin.activities.LoginActivity
import com.huawei.training.databinding.FragmentProfileBinding
import com.huawei.training.kotlin.models.UserObj
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.LearningApplication
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class ProfileFragment
/**
 * Instantiates a new Profile fragment.
 */
    : Fragment() {
    /**
     * The Binding.
     */
    var binding: FragmentProfileBinding? = null

    /**
     * The User obj.
     */
    var userObj: UserObj? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userObj = (Objects.requireNonNull(activity)?.applicationContext as LearningApplication).userObj
        if (userObj != null) {
            binding?.name?.text = userObj?.firstName
            binding?.email?.text = userObj?.emailId
        }
        // account signout
        binding?.signout?.setOnClickListener { view: View? ->
            val utils = activity?.let { AppUtils(it) }
            utils?.clearAllPref()
            AGConnectAuth.getInstance().signOut()
            (activity as BaseActivity?)!!.showToast(getString(R.string.logout_success))
            (activity?.application as LearningApplication).setUserStatus()
            (activity?.application as LearningApplication).userObj = null
            activity?.startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * New instance profile fragment.
         *
         * @param param1 the param 1
         * @param param2 the param 2
         * @return the profile fragment
         */
        fun newInstance(param1: String?, param2: String?): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}