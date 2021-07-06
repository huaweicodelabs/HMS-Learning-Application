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
package com.huawei.training.kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.training.R
import com.huawei.training.kotlin.database.CloudDbHelper
import com.huawei.training.kotlin.adapters.CourseDetailsListAdapter
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.CloudDbUiCallbackListener
import com.huawei.training.kotlin.database.tables.*
import com.huawei.training.kotlin.listeners.CourseTypeItemClick
import com.huawei.training.kotlin.models.CourseDetailsDataModel
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.LearningApplication
import com.huawei.training.kotlin.utils.video.Constants
import kotlinx.android.synthetic.main.activity_course_details.*
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class CourseDetailsActivity : BaseActivity(), CloudDbUiCallbackListener {

    /**
     * The My list data.
     */
    var myListData: ArrayList<CourseDetailsDataModel>? = null

    /**
     * The Coursename.
     */
    private var coursename: String? = ""

    /**
     * The Course id.
     */
    private var courseId = 0

    /**
     * The Device platforms.
     */
    private var devicePlatforms: String? = null

    /**
     * The My course index.
     */
    private var myCourseIndex = 1

    /**
     * The Recently viewed index.
     */
    private var recentlyViewedIndex = -1

    /**
     * Gets cloud db helper.
     *
     * @return the cloud db helper
     */
    var cloudDbHelper: CloudDbHelper?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)
        checkToFinishActivity(this.intent.extras)
        val intent = intent
        coursename = intent.getStringExtra(Constants.COURSE_NAME)
        courseId = intent.getStringExtra(Constants.COURSE_ID)?.toInt()!!
        (application as LearningApplication).isRecentlyViewedCoresRefresh = false
        setToolbar(coursename)
        init()
    }

    private fun init() {
        try {
            cloudDbHelper= CloudDbHelper.getInstance(applicationContext)
            if (cloudDbHelper != null) {
                cloudDbHelper?.addCallBackListener(this)
            }
        } catch (e: NullPointerException) {
        }
        share.setOnClickListener { start: View? ->
            appAnalytics?.shareCourseClickEvent(coursename)
            AppUtils.shareActivity(this@CourseDetailsActivity)
        }
        addtolist.setOnClickListener { start: View? ->
            if (!showLoginDialog()) {
                if (userObj != null) {
                    val myCourses = MyCoursesTable()
                    myCourses.userId = userObj!!.getuId()
                    myCourses.myCourseId = myCourseIndex
                    myCourses.courseId = courseId
                    myCourses.courseName = coursename
                    appAnalytics?.addToMyCourseCourseClickEvent(
                            userObj?.emailId, coursename)
                    cloudDbHelper?.getCloudDbAllQueyCalls()?.upsertMyCourse(
                            myCourses, CloudDbAction.GET_MY_COURSES)
                }
            }
        }
        AppUtils.showLoadingDialog(this)
        queryWithCourseId()
    }

    private fun checkToFinishActivity(bundle: Bundle?) {
        if (bundle != null) {
            val isActivityToBeFinish = Objects.requireNonNull(this.intent.extras)
                    ?.getBoolean(Constants.FINISHSTATUS)
            if (isActivityToBeFinish!!) {
                finish()
            }
        }
    }

    // set course data
    private fun setCourseData(coPlatformList: List<CoursePlatformTable>) {
        myListData = ArrayList()
        for (coPlatform in coPlatformList) {
            if (devicePlatforms!!.contains(coPlatform.platformId.toString())) {
                myListData?.add(
                        CourseDetailsDataModel(coPlatform.platformName.toString(),
                                coPlatform.platformId
                                        .toString(), Constants.PRICE, 3f))
            }
        }
    }

    /**
     * The Course item click.
     */
    var courseItemClick = object : CourseTypeItemClick {
        override fun courseOnClick(model: CourseDetailsDataModel?) {
            if (model != null) {
                navigatetoCourseDetails(model)
            }
        }
    }

    // navigate to course details page
    private fun navigatetoCourseDetails(model: CourseDetailsDataModel) {
        val intent = Intent(this@CourseDetailsActivity,
                CourseContentDetailsActivity::class.java)
        intent.putExtra(Constants.COURSE_NAME, "" + model.name)
        intent.putExtra(Constants.COURSE_ID, "" + courseId)
        startActivity(intent)
    }

    // get data from db
    private fun queryWithCourseId() {
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(CourseDetailsTable::class.java)
        cloudDBZoneQuery.equalTo("courseId", courseId)
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryCourseDetailsTable(cloudDBZoneQuery as CloudDBZoneQuery<CourseDetailsTable>?, CloudDbAction.GET_ALL_COURSES)
    }

    override fun onSuccessDbData(cloudDbAction: CloudDbAction?, dataList: List<CloudDBZoneObject?>?) {
        when (cloudDbAction) {
            CloudDbAction.GET_COURSE_DOC -> loadCourseDoc(dataList?.get(0) as CoursesDocTable)
            CloudDbAction.GET_ALL_COURSES -> loadAllCourses(dataList as List<*>)
            CloudDbAction.GET_COURSE_PLATFORMS -> loadPlatformCourses(dataList as List<CoursePlatformTable>)
            CloudDbAction.GET_MY_COURSES -> loadMyCourses(dataList as List<MyCoursesTable>)
            CloudDbAction.GET_MY_COURSES_QUERY -> {
                loadMyCoursesQuery(dataList as List<MyCoursesTable>)
                queryRecentlyViewed(true)
            }
            CloudDbAction.GET_ALL_RECENTLY_VIEWED_COURSES -> loadRecentCourses(dataList as List<RecentlyViewedCoursesTable>)
            CloudDbAction.GET_RECENTLY_VIEWED_COURSES -> getRecentlyViewedCourses(dataList as List<CloudDBZoneObject>)
            else -> {
            }
        }
    }

    private fun loadCourseDoc(coDoc: CoursesDocTable) {
        description.text = coDoc.docDescription
        devicePlatforms = coDoc.docPlatformsIds
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryCoursePlatformTable(null, CloudDbAction.GET_COURSE_PLATFORMS)
    }

    private fun loadAllCourses(allCoursesList: List<*>) {
        val coDetails = allCoursesList[0] as CourseDetailsTable
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(CoursesDocTable::class.java)
        cloudDBZoneQuery.equalTo("courseName", coDetails.courseDocId)
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryCoursesDocTable(cloudDBZoneQuery as CloudDBZoneQuery<CoursesDocTable>?, CloudDbAction.GET_COURSE_DOC)
    }

    private fun getRecentlyViewedCourses(dataList: List<CloudDBZoneObject>) {
        if ((dataList as List<*>).isEmpty()) insertRecentlyViewedCourse(recentlyViewedIndex)
        AppUtils.cancelLoading()
    }

    private fun loadPlatformCourses(coPlatformList: List<CoursePlatformTable>) {
        setCourseData(coPlatformList)
        val adapter = myListData?.let { CourseDetailsListAdapter(it, courseItemClick) }
        recyclerview_contentlist.setHasFixedSize(true)
        recyclerview_contentlist.layoutManager = LinearLayoutManager(this)
        recyclerview_contentlist.adapter = adapter
        queryMyCourses(-1)
    }

    private fun loadMyCourses(myCoursesList: List<MyCoursesTable>) {
        if (!myCoursesList.isEmpty()) {
            myCourseIndex = myCoursesList[0].myCourseId!! + 1
            queryMyCourses(courseId)
        } else {
            queryRecentlyViewed(true)
        }
    }

    private fun loadMyCoursesQuery(myCoursesQueryList: List<MyCoursesTable>) {
        if (!myCoursesQueryList.isEmpty()) {
            for (myCourses in myCoursesQueryList) {
                if (myCourses.courseId == courseId) {
                    myCourseIndex = myCourses.myCourseId!!
                    addtolist.isEnabled = false
                    break
                }
            }
        }
    }

    private fun loadRecentCourses(viewedCourses: List<RecentlyViewedCoursesTable>) {
        if (!viewedCourses.isEmpty()) {
            recentlyViewedIndex = (viewedCourses[0].recentlyViewedId?.plus(1)!!)
            queryRecentlyViewed(false)
        } else {
            insertRecentlyViewedCourse(1)
        }
    }

    override fun onSuccessDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        AppUtils.cancelLoading()
        message?.let { showToast(it) }
        if (CloudDbAction.GET_MY_COURSES == cloudDbAction) {
            addtolist.isEnabled = false
        }
    }

    override fun onFailureDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        AppUtils.cancelLoading()
        message?.let { showToast(it) }
    }

    private fun insertRecentlyViewedCourse(index: Int) {
        if (userObj != null) {
            if (userObj?.getuId() != null) {
                val recentlyViewedCourses = RecentlyViewedCoursesTable()
                recentlyViewedCourses.recentlyViewedId = index
                recentlyViewedCourses.courseId = "" + courseId
                recentlyViewedCourses.courseName = coursename
                recentlyViewedCourses.userId = userObj!!.getuId()
                (application as LearningApplication).isRecentlyViewedCoresRefresh = true
                cloudDbHelper
                        ?.getCloudDbAllQueyCalls()
                        ?.upsertRecentlyViewedCourse(recentlyViewedCourses,
                                CloudDbAction.GET_RECENTLY_VIEWED_COURSES_MSG)
            }
        }
    }

    // get data from db
    private fun queryMyCourses(courseId: Int) {
        if (userObj != null) {
            if (userObj?.getuId() != null) {
                val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(MyCoursesTable::class.java)
                if (courseId > -1) {
                    cloudDBZoneQuery.orderByAsc("myCourseId")
                    userObj?.getuId()?.let { cloudDBZoneQuery.contains("userId", it).equalTo("courseId", courseId) }
                    Objects.requireNonNull(this).cloudDbHelper
                            ?.getCloudDbAllQueyCalls()
                            ?.queryMyCoursesTable(cloudDBZoneQuery as CloudDBZoneQuery<MyCoursesTable>?, CloudDbAction.GET_MY_COURSES_QUERY)
                } else {
                    cloudDBZoneQuery.orderByDesc("myCourseId")
                    Objects.requireNonNull(this).cloudDbHelper
                            ?.getCloudDbAllQueyCalls()
                            ?.queryMyCoursesTable(cloudDBZoneQuery as CloudDBZoneQuery<MyCoursesTable>?, CloudDbAction.GET_MY_COURSES)
                }
            }
        } else {
            AppUtils.cancelLoading()
        }
    }

    //calling query for recently viewed courses
    private fun queryRecentlyViewed(isAllList: Boolean) {
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(RecentlyViewedCoursesTable::class.java)
        if (isAllList) {
            cloudDBZoneQuery.orderByDesc("recentlyViewedId")
            cloudDbHelper
                    ?.getCloudDbAllQueyCalls()
                    ?.queryRecentlyRecentlyViewedCoursesTable(cloudDBZoneQuery as CloudDBZoneQuery<RecentlyViewedCoursesTable>?, CloudDbAction.GET_ALL_RECENTLY_VIEWED_COURSES)
        } else {
            if (userObj?.getuId() != null) {
                cloudDBZoneQuery.equalTo("userId", userObj?.getuId()).equalTo("courseId", "" + courseId)
                cloudDbHelper?.getCloudDbAllQueyCalls()
                        ?.queryRecentlyRecentlyViewedCoursesTable(cloudDBZoneQuery as CloudDBZoneQuery<RecentlyViewedCoursesTable>?, CloudDbAction.GET_RECENTLY_VIEWED_COURSES)
            }
        }
    }
}