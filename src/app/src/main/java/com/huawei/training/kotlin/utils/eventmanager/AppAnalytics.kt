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
package com.huawei.training.kotlin.utils.eventmanager

import android.content.Context
import android.os.Bundle
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class AppAnalytics(
        /**
         * The M context.
         */
        var mContext: Context) {

    /**
     * The Bundle.
     */
    var bundle: Bundle? = null

    /**
     * The Hi analytics instance.
     */
    var hiAnalyticsInstance: HiAnalyticsInstance

    /**
     * Adding the event to bundle to forward parameters to sendEvent
     *
     * @param source     the source
     * @param courseName course name retrived when user clicked on course
     */
    fun courseClickEvent(source: String?, courseName: String?) {
        bundle = Bundle()
        bundle?.putString(AnalyticsConstants.COURSE_NAME_PROP, courseName)
        bundle?.putString(AnalyticsConstants.SOURCE_PROP, source)
        sendEvent(AnalyticsConstants.COURSE_CLICKED_EVENT, bundle!!)
    }

    /**
     * Adding the event to bundle to forward parameters to sendEvent
     *
     * @param courseName course name retrived when user clicked on course
     */
    fun startCourseClickEvent(courseName: String?) {
        bundle = Bundle()
        bundle?.putString(AnalyticsConstants.COURSE_NAME_PROP, courseName)
        sendEvent(AnalyticsConstants.START_COURSE_CLICKED_EVENT, bundle!!)
    }

    /**
     * Add to my course course click event.
     *
     * @param userMail   the user mail
     * @param courseName the course name
     */
    fun addToMyCourseCourseClickEvent(userMail: String?, courseName: String?) {
        bundle = Bundle()
        bundle?.putString(AnalyticsConstants.USER_MMAIL_PROP, userMail)
        bundle?.putString(AnalyticsConstants.COURSE_NAME_PROP, courseName)
        sendEvent(AnalyticsConstants.ADD_TO_MY_COURSE_CLICKED_EVENT, bundle!!)
    }

    /**
     * Share course click event.
     *
     * @param courseName the course name
     */
    fun shareCourseClickEvent(courseName: String?) {
        bundle = Bundle()
        bundle?.putString(AnalyticsConstants.COURSE_NAME_PROP, courseName)
        sendEvent(AnalyticsConstants.SHARE_COURSE_CLICKED_EVENT, bundle!!)
    }

    /**
     * Adding the event to bundle to forward parameters to sendEvent
     *
     * @param courseId    the course id
     * @param codeLabName course name retrived when user clicked on course
     */
    fun codeLabClickEvent(courseId: String?, codeLabName: String?) {
        bundle = Bundle()
        bundle?.putString(AnalyticsConstants.COURSE_ID_PROP, courseId)
        bundle?.putString(AnalyticsConstants.CODE_LAB_SUB_TOPIC_PROP, codeLabName)
        sendEvent(AnalyticsConstants.CODE_LAB_CLICKED_EVENT, bundle!!)
    }

    /**
     * Adding the event to bundle to forward parameters to sendEvent
     *
     * @param tabName tab name
     */
    fun bottomTabClickEvent(tabName: String?) {
        bundle = Bundle()
        bundle?.putString(AnalyticsConstants.COURSE_NAME_PROP, tabName)
        sendEvent(AnalyticsConstants.COURSE_CLICKED_EVENT, bundle!!)
    }

    /**
     * Adding the event to bundle to forward parameters to sendEvent
     *
     * @param examName course name retrived when user clicked on course
     */
    fun startExamClickEvent(examName: String?) {
        bundle = Bundle()
        bundle?.putString(AnalyticsConstants.START_EXAM_CLICKED_PROP, examName)
        sendEvent(AnalyticsConstants.START_EXAM_CLICKED_EVENT, bundle!!)
    }

    /**
     * Send the event to HMS Analytics
     *
     * @param eventName Event name
     * @param bundle    Bundle of properties
     */
    private fun sendEvent(eventName: String, bundle: Bundle) {
        hiAnalyticsInstance.onEvent(eventName, bundle)
    }

    /**
     * Instantiates a new App analytics.
     *
     * @param context the context
     */
    init {
        hiAnalyticsInstance = HiAnalytics.getInstance(mContext)
        hiAnalyticsInstance.setAnalyticsEnabled(true)
    }
}