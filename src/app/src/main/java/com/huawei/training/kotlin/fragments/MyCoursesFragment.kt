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
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.training.R
import com.huawei.training.database.CloudDbHelper
import com.huawei.training.kotlin.activities.BaseActivity
import com.huawei.training.kotlin.activities.PlayActivity
import com.huawei.training.kotlin.adapters.MyCourseListAdapter
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.CloudDbUiCallbackListener
import com.huawei.training.kotlin.database.tables.MyCoursesTable
import com.huawei.training.databinding.FragmentMyCoursesBinding
import com.huawei.training.kotlin.listeners.CourseItemClick
import com.huawei.training.kotlin.models.CourseDataModel
import com.huawei.training.kotlin.models.UserObj
import com.huawei.training.kotlin.utils.LearningApplication
import com.huawei.training.kotlin.utils.eventmanager.AnalyticsConstants
import com.huawei.training.kotlin.utils.eventmanager.AppAnalytics
import com.huawei.training.kotlin.utils.video.Constants
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class MyCoursesFragment
/**
 * Instantiates a new My courses fragment.
 */
    : Fragment(), CloudDbUiCallbackListener {
    /**
     * The Model list.
     */
    var modelList: MutableList<CourseDataModel> = ArrayList()

    /**
     * The Binding.
     */
    var binding: FragmentMyCoursesBinding? = null

    /**
     * The User obj.
     */
    var userObj: UserObj? = null

    /**
     * The Cloud db helper.
     */
    var cloudDbHelper: CloudDbHelper? = null

    /**
     * The App analytics.
     */
    var appAnalytics: AppAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userObj = (Objects.requireNonNull(
                activity)?.application as LearningApplication).userObj
        cloudDbHelper = (activity as BaseActivity?)!!.cloudDbHelper
        appAnalytics = (activity as BaseActivity?)!!.appAnalytics
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyCoursesBinding.inflate(inflater, container, false)
        cloudDbHelper?.addCallBackListener(this)
        queryMyCourses()
        return binding?.root
    }

    // get data from db
    private fun queryMyCourses() {
        if (userObj != null) {
            if (userObj?.getuId() != null) {
                val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(MyCoursesTable::class.java)
                cloudDBZoneQuery.equalTo("userId", userObj?.getuId())
                (Objects.requireNonNull(activity) as BaseActivity).cloudDbHelper
                        ?.getCloudDbAllQueyCalls()
                        ?.queryMyCoursesTable(cloudDBZoneQuery as CloudDBZoneQuery<MyCoursesTable>?, CloudDbAction.GET_MY_COURSES)
            }
        }
    }

    // bind data to adapter
    private fun loadData() {
        val adapter = MyCourseListAdapter(modelList, courseItemClick)
        binding?.recyclerviewMycourseslist?.setHasFixedSize(true)
        binding?.recyclerviewMycourseslist?.layoutManager = LinearLayoutManager(activity)
        binding?.recyclerviewMycourseslist?.adapter = adapter
    }

    /**
     * The Course item click.
     */
    // course item click
    var courseItemClick = object : CourseItemClick {
        override fun courseOnClick(model: CourseDataModel?) {
            appAnalytics?.courseClickEvent(
                    AnalyticsConstants.MY_COURSES_SCREEN, model?.name)
            val intent = Intent(activity, PlayActivity::class.java)
            intent.putExtra(Constants.COURSE_NAME, "" + model?.name)
            intent.putExtra(Constants.COURSE_ID, model?.courseId)
            startActivity(intent)
        }
    }

    override fun onSuccessDbData(cloudDbAction: CloudDbAction?, dataList: List<CloudDBZoneObject?>?) {
        if (cloudDbAction == CloudDbAction.GET_MY_COURSES) {
            var model: CourseDataModel
            val myCoursesList: List<MyCoursesTable> = dataList as List<MyCoursesTable>
            if (myCoursesList.isEmpty()) {
                (Objects.requireNonNull(activity) as BaseActivity)
                        .showToast("Not found any courses. Please add at least one")
            }
            modelList.clear()
            for (myCourses in myCoursesList) {
                model = CourseDataModel(
                        "" + myCourses.courseId,
                        myCourses.courseName!!,
                        resources.getString(R.string.Rs) + "500",
                        3f,
                        android.R.mipmap.sym_def_app_icon)
                modelList.add(model)
            }
            loadData()
        }
    }

    override fun onSuccessDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        if (message != null) (Objects.requireNonNull(activity) as BaseActivity).showToast(message)
    }

    override fun onFailureDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        if (message != null) try {
            (Objects.requireNonNull(activity) as BaseActivity).showToast(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * New instance my courses fragment.
         *
         * @return the my courses fragment
         */
        fun newInstance(): MyCoursesFragment {
            val fragment = MyCoursesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}