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

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huawei.training.kotlin.activities.BaseActivity
import com.huawei.training.kotlin.activities.ExamActivity
import com.huawei.training.databinding.FragmentExamBinding
import com.huawei.training.kotlin.models.ExamModel
import com.huawei.training.kotlin.models.UserObj
import com.huawei.training.kotlin.utils.LearningApplication
import com.huawei.training.kotlin.utils.eventmanager.AppAnalytics
import com.huawei.training.kotlin.utils.video.Constants
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class ExamFragment
/**
 * Instantiates a new Exam fragment.
 */
    : Fragment() {
    /**
     * The Binding.
     */
    var binding: FragmentExamBinding? = null

    /**
     * The Exam model.
     */
    var examModel: ExamModel? = null

    /**
     * The App analytics.
     */
    var appAnalytics: AppAnalytics? = null

    /**
     * The User obj.
     */
    var userObj: UserObj? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            examModel = arguments!!.getParcelable(ARG_PARAM1)
        }
        appAnalytics = (Objects.requireNonNull(activity) as BaseActivity).appAnalytics
        userObj = (activity!!.application as LearningApplication).userObj
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExamBinding.inflate(inflater, container, false)
        binding?.examTitle?.text = examModel!!.name
        binding?.examDesc?.text = examModel!!.desc
        binding?.examDurationMin?.text = "" + examModel!!.duration
        binding?.examTotalQC?.text = "" + examModel!!.noOfQuestions
        binding?.startexam?.setOnClickListener { start: View? ->
            if (!(Objects.requireNonNull(activity) as BaseActivity).showLoginDialog()) {
                if (userObj == null) userObj = (activity
                        ?.getApplication() as LearningApplication).userObj
                if (userObj != null) {
                    navigatetoExam()
                }
            }
        }
        return binding?.root
    }

    // navigate to exam activity
    private fun navigatetoExam() {
        appAnalytics!!.startExamClickEvent(examModel!!.name)
        val intent = Intent(activity, ExamActivity::class.java)
        intent.putExtra(Constants.COURSE_NAME, examModel!!.name)
        intent.putExtra(Constants.COURSE_ID, "" + examModel!!.cId)
        startActivity(intent)
    }

    companion object {
        private const val ARG_PARAM1 = "param1"

        /**
         * New instance exam fragment.
         *
         * @param examModel the exam model
         * @return the exam fragment
         */
        fun newInstance(examModel: ExamModel?): ExamFragment {
            val fragment = ExamFragment()
            val args = Bundle()
            args.putParcelable(ARG_PARAM1, examModel)
            fragment.arguments = args
            return fragment
        }
    }
}