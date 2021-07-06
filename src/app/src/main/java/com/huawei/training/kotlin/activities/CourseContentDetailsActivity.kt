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
import com.huawei.training.kotlin.adapters.CourseContentDetailsListAdapter
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.CloudDbUiCallbackListener
import com.huawei.training.kotlin.database.tables.CourseContentTable
import com.huawei.training.kotlin.listeners.CourseContentItemClick
import com.huawei.training.kotlin.models.CourseContentDataModel
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.video.Constants
import com.huawei.training.kotlin.utils.video.entity.PlayEntity
import kotlinx.android.synthetic.main.activity_course_content_details.*

/**
 * @author Huawei DTSE India
 * @since 2020
 */
class CourseContentDetailsActivity : BaseActivity(), CloudDbUiCallbackListener {

    /**
     * The My list data.
     */
    var myListData: List<CourseContentDataModel?>? = null

    /**
     * The Course name.
     */
    private var courseName: String? = ""

    /**
     * The Course id.
     */
    private var courseId = 0

    /**
     * Gets cloud db helper.
     *
     * @return the cloud db helper
     */
    var cloudDbHelper: CloudDbHelper?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_content_details)
        val intent = intent
        courseName = intent.getStringExtra(Constants.COURSE_NAME)
        val st_id = intent.getStringExtra(Constants.COURSE_ID)
        if(st_id!=null){
            courseId = st_id.toInt()
        }
        setToolbar(courseName)

        cloudDbHelper= CloudDbHelper.getInstance(applicationContext)

        cloudDbHelper?.addCallBackListener(this)
        AppUtils.showLoadingDialog(this)
        queryWithCourseId()
        startcourse.setOnClickListener { start: View? ->
            if (!showLoginDialog()) {
                if (userObj != null) {
                    appAnalytics?.startCourseClickEvent(courseName)
                    val playEntity = PlayEntity()
                    playEntity.url = resources.getString(R.string.url)
                    playEntity.setUrlType(Constants.UrlType.URL)
                    val intent1 = Intent(this@CourseContentDetailsActivity, PlayActivity::class.java)
                    intent1.putExtra(Constants.COURSE_NAME, courseName)
                    intent1.putExtra(Constants.COURSE_ID, "" + courseId)
                    startActivity(intent1)
                }
            }
        }
    }

    // set course data
    private fun setCourseData(contentList: List<CourseContentTable>) {
        myListData = AppUtils.getCourseOrCodelabContentData(contentList[0], null)
    }

    /**
     * The Course item click.
     */
    var courseItemClick = object : CourseContentItemClick {
        override fun courseOnClick(model: CourseContentDataModel?) {

        }
    }

    // get data from db using id
    private fun queryWithCourseId() {
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(CourseContentTable::class.java)
        cloudDBZoneQuery.equalTo("courseId", ("" + courseId).toLong())
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryCourseContentTable(
                cloudDBZoneQuery as CloudDBZoneQuery<CourseContentTable>?, CloudDbAction.GET_COURSE_CONTENT)
    }

    override fun onSuccessDbData(cloudDbAction: CloudDbAction?, dataList: List<CloudDBZoneObject?>?) {
        AppUtils.cancelLoading()
        if (cloudDbAction == CloudDbAction.GET_COURSE_CONTENT) {
            setCourseData(dataList as List<CourseContentTable>)
            val adapter = CourseContentDetailsListAdapter(
                    myListData, courseItemClick)
            recyclerview_contentlist.setHasFixedSize(true)
            recyclerview_contentlist.layoutManager = LinearLayoutManager(this)
            recyclerview_contentlist.adapter = adapter
        }
    }

    override fun onSuccessDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        AppUtils.cancelLoading()
    }

    override fun onFailureDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        showToast(message)
        AppUtils.cancelLoading()
    }
}