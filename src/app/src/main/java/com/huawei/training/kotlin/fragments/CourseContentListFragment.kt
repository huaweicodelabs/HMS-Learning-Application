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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huawei.training.kotlin.activities.DocumentViewActivity
import com.huawei.training.kotlin.adapters.CourseContentDetailsListAdapter
import com.huawei.training.databinding.FragmentCourseContentListBinding
import com.huawei.training.kotlin.listeners.CourseContentItemClick
import com.huawei.training.kotlin.models.CourseContentDataModel
import com.huawei.training.kotlin.utils.video.Constants
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class CourseContentListFragment
/**
 * Instantiates a new Course content list fragment.
 */
    : Fragment() {
    /**
     * The Binding.
     */
    var binding: FragmentCourseContentListBinding? = null

    /**
     * The Course content data model list data.
     */
    var CourseContentDataModelListData: ArrayList<CourseContentDataModel?>? = null

    /**
     * The Recycler view.
     */
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            CourseContentDataModelListData = arguments!!.getParcelableArrayList(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCourseContentListBinding.inflate(inflater, container, false)
        recyclerView = binding?.recyclerviewContentlist
        binding?.recyclerviewContentlist?.setHasFixedSize(true)
        binding?.recyclerviewContentlist?.layoutManager = LinearLayoutManager(activity)
        val adapter = CourseContentDetailsListAdapter(
                CourseContentDataModelListData, courseItemClick)
        recyclerView?.adapter = adapter
        return binding?.root
    }

    /**
     * The Course item click.
     */
    var courseItemClick = object : CourseContentItemClick {
        override fun courseOnClick(model: CourseContentDataModel?) {
            val intent = Intent(activity, DocumentViewActivity::class.java)
            intent.putExtra(Constants.COURSE_DOCUMENTURL, "" + model?.courseUrl)
            intent.putExtra(Constants.COURSE_NAME, "" + model?.courseName)
            startActivity(intent)
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"

        /**
         * New instance course content list fragment.
         *
         * @param myListData the my list data
         * @return the course content list fragment
         */
        fun newInstance(
                myListData: ArrayList<CourseContentDataModel>): CourseContentListFragment {
            val fragment = CourseContentListFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_PARAM1, myListData)
            fragment.arguments = args
            return fragment
        }
    }
}