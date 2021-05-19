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
package com.huawei.training.kotlin.database

import android.app.Activity
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.SignInResult
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.training.kotlin.database.tables.*
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class CloudDbQueyCalls {
    /**
     * The M cloud db zone.
     */
    var mCloudDBZone: CloudDBZone? = null

    /**
     * The Cloud db ui callback listener.
     */
    var cloudDbUiCallbackListener: CloudDbUiCallbackListener? = null

    /**
     * Sets cloud db zone.
     *
     * @param mCloudDBZone the m cloud db zone
     */
    fun setmCloudDBZone(mCloudDBZone: CloudDBZone?) {
        this.mCloudDBZone = mCloudDBZone
    }

    /**
     * Sets cloud db ui callback listener.
     *
     * @param cloudDbUiCallbackListener the cloud db ui callback listener
     */
    fun addCloudDbUiCallbackListener(cloudDbUiCallbackListener: CloudDbUiCallbackListener?) {
        this.cloudDbUiCallbackListener = cloudDbUiCallbackListener
    }
    /**
     * Login.
     *
     * @param activity      the activity
     * @param cloudDbAction the cloud db action
     */
    fun login(activity: Activity?, cloudDbAction: CloudDbAction?) {
        val auth = AGConnectAuth.getInstance()
        auth.signInAnonymously()
                .addOnSuccessListener(
                        activity
                ) { signInResult: SignInResult? ->
                    cloudDbUiCallbackListener?.onSuccessDbQueryMessage(
                            cloudDbAction, "Successfully Loggedin into DB")
                }
                .addOnFailureListener(
                        activity
                ) { e: Exception? ->
                    cloudDbUiCallbackListener?.onFailureDbQueryMessage(
                            cloudDbAction, "Failed to Loggedin into DB")
                }
    }
    // -------------------------------------------------Inserting User Data to
    // DB----------------------------------------------------
    /**
     * Insterting user information to cloud DB
     *
     * @param userInfo      the user info
     * @param cloudDbAction the cloud db action
     */
    fun upsertUserInfo(userInfo: UsersInfoTable?, cloudDbAction: CloudDbAction?) {
        if (mCloudDBZone == null) {
            return
        }
        val upsertTask = mCloudDBZone!!.executeUpsert(userInfo!!)
        upsertTask
                .addOnSuccessListener {
                    try {
                        cloudDbUiCallbackListener!!.onSuccessDbQueryMessage(
                                cloudDbAction, null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                .addOnFailureListener { e: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "Insert UserInfo failed")
                }
    }

    /**
     * Query recently viewed courses in storage from cloud
     * side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryUser(query: CloudDBZoneQuery<UsersInfoTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        val userInfoQueryTask = mCloudDBZone!!.executeQuery(query!!,
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        userInfoQueryTask
                .addOnSuccessListener { userInfoCloudDBZoneSnapshot: CloudDBZoneSnapshot<UsersInfoTable> ->
                    processUserInfo(
                            userInfoCloudDBZoneSnapshot, cloudDbAction)
                }
                .addOnFailureListener { ex: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processUserInfo(
            snapshot: CloudDBZoneSnapshot<UsersInfoTable>, cloudDbAction: CloudDbAction) {
        val userInfoCursor = snapshot.snapshotObjects
        val userInfoArrayList: MutableList<UsersInfoTable> = ArrayList()
        try {
            while (userInfoCursor.hasNext()) {
                val userInfoCourses = userInfoCursor.next()
                userInfoArrayList.add(userInfoCourses)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(cloudDbAction, userInfoArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query all main course categories in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param cloudDbAction the cloud db action
     */
    fun queryAllMainCategories(cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        val coMainCategoryQueryTask = mCloudDBZone!!.executeQuery(
                CloudDBZoneQuery.where(CoursesMainCategoryTable::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        coMainCategoryQueryTask
                .addOnSuccessListener { coMainCategoryCloudDBZoneSnapshot: CloudDBZoneSnapshot<CoursesMainCategoryTable> -> processMainCategories(coMainCategoryCloudDBZoneSnapshot, cloudDbAction) }
                .addOnFailureListener { e: Exception? -> cloudDbUiCallbackListener!!.onFailureDbQueryMessage(cloudDbAction, "") }
    }

    private fun processMainCategories(snapshot: CloudDBZoneSnapshot<CoursesMainCategoryTable>
                                      , cloudDbAction: CloudDbAction) {
        val courseMainCategoryCursor = snapshot.snapshotObjects
        val coMainCategoryArrayList: MutableList<CoursesMainCategoryTable> = ArrayList()
        try {
            while (courseMainCategoryCursor.hasNext()) {
                val coMainCategory = courseMainCategoryCursor.next()
                coMainCategoryArrayList.add(coMainCategory)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(cloudDbAction, coMainCategoryArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query recently viewed courses in storage from cloud side
     * with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryRecentlyRecentlyViewedCoursesTable(
            query: CloudDBZoneQuery<RecentlyViewedCoursesTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        val recentlyRecentlyViewedCoursesTableQueryTask = mCloudDBZone!!.executeQuery(query!!,
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        recentlyRecentlyViewedCoursesTableQueryTask
                .addOnSuccessListener { coMainCategoryCloudDBZoneSnapshot: CloudDBZoneSnapshot<RecentlyViewedCoursesTable> ->
                    processRecentlyRecentlyViewedCoursesTable(
                            coMainCategoryCloudDBZoneSnapshot, cloudDbAction)
                }
                .addOnFailureListener { ex: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(cloudDbAction,
                            "")
                }
    }

    private fun processRecentlyRecentlyViewedCoursesTable(
            snapshot: CloudDBZoneSnapshot<RecentlyViewedCoursesTable>, cloudDbAction: CloudDbAction) {
        val recentlyRecentlyViewedCoursesTableCursor = snapshot.snapshotObjects
        val recentlyRecentlyViewedCoursesTableArrayList: MutableList<RecentlyViewedCoursesTable> = ArrayList()
        try {
            while (recentlyRecentlyViewedCoursesTableCursor.hasNext()) {
                val recentlyRecentlyViewedCoursesTable = recentlyRecentlyViewedCoursesTableCursor.next()
                recentlyRecentlyViewedCoursesTableArrayList.add(recentlyRecentlyViewedCoursesTable)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(
                cloudDbAction, recentlyRecentlyViewedCoursesTableArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query course details courses in storage from cloud
     * side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryCourseDetailsTable(
            query: CloudDBZoneQuery<CourseDetailsTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        var coDetailsQueryTask: Task<CloudDBZoneSnapshot<CourseDetailsTable>>? = null
        coDetailsQueryTask = mCloudDBZone!!.executeQuery(
                CloudDBZoneQuery.where(CourseDetailsTable::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        if (query != null) {
            coDetailsQueryTask = mCloudDBZone!!.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        }
        coDetailsQueryTask
                .addOnSuccessListener(
                        OnSuccessListener { coDetailsCloudDBZoneSnapshot: CloudDBZoneSnapshot<CourseDetailsTable> ->
                            processCourseDetailsTable(
                                    coDetailsCloudDBZoneSnapshot, cloudDbAction)
                        })
                .addOnFailureListener { e: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processCourseDetailsTable(
            snapshot: CloudDBZoneSnapshot<CourseDetailsTable>, cloudDbAction: CloudDbAction) {
        val coDetailsCursor = snapshot.snapshotObjects
        val coursesArrayList: MutableList<CourseDetailsTable> = ArrayList()
        try {
            while (coDetailsCursor.hasNext()) {
                val coDetails = coDetailsCursor.next()
                coursesArrayList.add(coDetails)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(cloudDbAction, coursesArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query course content courses in storage from cloud
     * side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryCourseContentTable(
            query: CloudDBZoneQuery<CourseContentTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        var coContentQueryTask: Task<CloudDBZoneSnapshot<CourseContentTable>>? = null
        coContentQueryTask = if (query == null) {
            mCloudDBZone!!.executeQuery(
                    CloudDBZoneQuery.where(CourseContentTable::class.java),
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        } else {
            mCloudDBZone!!.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        }
        coContentQueryTask
                .addOnSuccessListener(
                        OnSuccessListener { coContentCloudDBZoneSnapshot: CloudDBZoneSnapshot<CourseContentTable> ->
                            processCourseContentTable(
                                    coContentCloudDBZoneSnapshot, cloudDbAction)
                        })
                .addOnFailureListener { e: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processCourseContentTable(
            snapshot: CloudDBZoneSnapshot<CourseContentTable>, cloudDbAction: CloudDbAction) {
        val coContentCursor = snapshot.snapshotObjects
        val coContentArrayList: MutableList<CourseContentTable> = ArrayList()
        try {
            while (coContentCursor.hasNext()) {
                val coContent = coContentCursor.next()
                coContentArrayList.add(coContent)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(cloudDbAction, coContentArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query course platforms in storage from cloud side
     * with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryCoursePlatformTable(
            query: CloudDBZoneQuery<CoursePlatformTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        var coPlatformQueryTask: Task<CloudDBZoneSnapshot<CoursePlatformTable>>? = null
        coPlatformQueryTask = mCloudDBZone!!.executeQuery(
                CloudDBZoneQuery.where(CoursePlatformTable::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        if (query != null) {
            coPlatformQueryTask = mCloudDBZone!!.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        }
        coPlatformQueryTask
                .addOnSuccessListener(
                        OnSuccessListener { coPlatformCloudDBZoneSnapshot: CloudDBZoneSnapshot<CoursePlatformTable> ->
                            processCoursePlatformTables(
                                    coPlatformCloudDBZoneSnapshot, cloudDbAction)
                        })
                .addOnFailureListener { e: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processCoursePlatformTables(
            snapshot: CloudDBZoneSnapshot<CoursePlatformTable>, cloudDbAction: CloudDbAction) {
        val coPlatformCursor = snapshot.snapshotObjects
        val coPlatformArrayList: MutableList<CoursePlatformTable> = ArrayList()
        try {
            while (coPlatformCursor.hasNext()) {
                val coContent = coPlatformCursor.next()
                coPlatformArrayList.add(coContent)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(
                cloudDbAction, coPlatformArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query course Doc in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryCoursesDocTable(
            query: CloudDBZoneQuery<CoursesDocTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        var coDocQueryTask: Task<CloudDBZoneSnapshot<CoursesDocTable>>? = null
        coDocQueryTask = if (query == null) {
            mCloudDBZone!!.executeQuery(
                    CloudDBZoneQuery.where(CoursesDocTable::class.java),
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        } else {
            mCloudDBZone!!.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        }
        coDocQueryTask
                .addOnSuccessListener(
                        OnSuccessListener { coDocCloudDBZoneSnapshot: CloudDBZoneSnapshot<CoursesDocTable> ->
                            processCoursesDocTable(
                                    coDocCloudDBZoneSnapshot, cloudDbAction)
                        })
                .addOnFailureListener { ex: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processCoursesDocTable(
            snapshot: CloudDBZoneSnapshot<CoursesDocTable>, cloudDbAction: CloudDbAction) {
        val coDocCursor = snapshot.snapshotObjects
        val coDocArrayList: MutableList<CoursesDocTable> = ArrayList()
        try {
            while (coDocCursor.hasNext()) {
                val coDoc = coDocCursor.next()
                coDocArrayList.add(coDoc)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(cloudDbAction, coDocArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query com.huawei.training.kotlin.database.tables.ExamTable
     * details in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryExamTableDetails(
            query: CloudDBZoneQuery<ExamTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        var examDetailsQueryTask: Task<CloudDBZoneSnapshot<ExamTable>>? = null
        examDetailsQueryTask = if (query == null) {
            mCloudDBZone!!.executeQuery(
                    CloudDBZoneQuery.where(ExamTable::class.java),
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        } else {
            mCloudDBZone!!.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        }
        examDetailsQueryTask
                .addOnSuccessListener(
                        OnSuccessListener { examCloudDBZoneSnapshot: CloudDBZoneSnapshot<ExamTable> ->
                            processExamTableDetails(
                                    examCloudDBZoneSnapshot, cloudDbAction)
                        })
                .addOnFailureListener { e: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processExamTableDetails(
            snapshot: CloudDBZoneSnapshot<ExamTable>, cloudDbAction: CloudDbAction) {
        val examCursor = snapshot.snapshotObjects
        val examArrayList: MutableList<ExamTable> = ArrayList()
        try {
            while (examCursor.hasNext()) {
                val coDoc = examCursor.next()
                examArrayList.add(coDoc)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(
                cloudDbAction, examArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query com.huawei.training.kotlin.database.tables.
     * QuestionsTable in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryQuestionsTable(
            query: CloudDBZoneQuery<QuestionsTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        var questionsQueryTask: Task<CloudDBZoneSnapshot<QuestionsTable>>? = null
        questionsQueryTask = if (query == null) {
            mCloudDBZone!!.executeQuery(
                    CloudDBZoneQuery.where(QuestionsTable::class.java),
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        } else {
            mCloudDBZone!!.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        }
        questionsQueryTask
                .addOnSuccessListener(
                        OnSuccessListener { questionsCloudDBZoneSnapshot: CloudDBZoneSnapshot<QuestionsTable> ->
                            processQuestionsTable(
                                    questionsCloudDBZoneSnapshot, cloudDbAction)
                        })
                .addOnFailureListener { e: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processQuestionsTable(
            snapshot: CloudDBZoneSnapshot<QuestionsTable>, cloudDbAction: CloudDbAction) {
        val questionsCursor = snapshot.snapshotObjects
        val questionsArrayList: MutableList<QuestionsTable> = ArrayList()
        try {
            while (questionsCursor.hasNext()) {
                val questions = questionsCursor.next()
                questionsArrayList.add(questions)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(
                cloudDbAction, questionsArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query Codelab in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryCodelab(
            query: CloudDBZoneQuery<CoursesCodelabDetailsTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        val coCodelabDocQueryTask: Task<CloudDBZoneSnapshot<CoursesCodelabDetailsTable>>
        coCodelabDocQueryTask = if (query == null) {
            mCloudDBZone!!.executeQuery(
                    CloudDBZoneQuery.where(CoursesCodelabDetailsTable::class.java),
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        } else {
            mCloudDBZone!!.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        }
        coCodelabDocQueryTask
                .addOnSuccessListener { coCodelabDocCloudDBZoneSnapshot: CloudDBZoneSnapshot<CoursesCodelabDetailsTable> ->
                    processCoursesCodelabDetailsTable(
                            coCodelabDocCloudDBZoneSnapshot, cloudDbAction)
                }
                .addOnFailureListener { exp: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processCoursesCodelabDetailsTable(
            snapshot: CloudDBZoneSnapshot<CoursesCodelabDetailsTable>, cloudDbAction: CloudDbAction) {
        val coCOdeLabCursor = snapshot.snapshotObjects
        val coCodelabDocArrayList: MutableList<CoursesCodelabDetailsTable> = ArrayList()
        try {
            while (coCOdeLabCursor.hasNext()) {
                val coCodelabDoc = coCOdeLabCursor.next()
                coCodelabDocArrayList.add(coCodelabDoc)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(
                cloudDbAction, coCodelabDocArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Query My courses in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    fun queryMyCoursesTable(
            query: CloudDBZoneQuery<MyCoursesTable>?, cloudDbAction: CloudDbAction) {
        if (mCloudDBZone == null) {
            return
        }
        var myCoursesQueryTask: Task<CloudDBZoneSnapshot<MyCoursesTable>>? = null
        myCoursesQueryTask = if (query == null) {
            mCloudDBZone!!.executeQuery(
                    CloudDBZoneQuery.where(MyCoursesTable::class.java),
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        } else {
            mCloudDBZone!!.executeQuery(
                    query,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)
        }
        myCoursesQueryTask
                .addOnSuccessListener(
                        OnSuccessListener { myCoursesCloudDBZoneSnapshot: CloudDBZoneSnapshot<MyCoursesTable> ->
                            processMyCoursesTable(
                                    myCoursesCloudDBZoneSnapshot, cloudDbAction)
                        })
                .addOnFailureListener { exp: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    private fun processMyCoursesTable(
            snapshot: CloudDBZoneSnapshot<MyCoursesTable>, cloudDbAction: CloudDbAction) {
        val myCoursesCursor = snapshot.snapshotObjects
        val myCoursesDocArrayList: MutableList<MyCoursesTable> = ArrayList()
        try {
            while (myCoursesCursor.hasNext()) {
                val myCourses = myCoursesCursor.next()
                myCoursesDocArrayList.add(myCourses)
            }
        } catch (exp: AGConnectCloudDBException) {
        } finally {
            snapshot.release()
        }
        cloudDbUiCallbackListener!!.onSuccessDbData(cloudDbAction, myCoursesDocArrayList as List<CloudDBZoneObject>)
    }

    /**
     * Upsert recently viewed course.
     *
     * @param recentlyRecentlyViewedCoursesTable the recently viewed courses
     * @param cloudDbAction                      the cloud db action
     */
    fun upsertRecentlyViewedCourse(
            recentlyRecentlyViewedCoursesTable: RecentlyViewedCoursesTable?,
            cloudDbAction: CloudDbAction?) {
        if (mCloudDBZone == null) {
            return
        }
        val upsertTask = mCloudDBZone!!.executeUpsert(recentlyRecentlyViewedCoursesTable!!)
        upsertTask
                .addOnSuccessListener { cloudDBZoneResult: Int? ->
                    cloudDbUiCallbackListener!!.onSuccessDbQueryMessage(
                            cloudDbAction, null)
                }
                .addOnFailureListener { exp: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }

    /**
     * Upsert my course.
     *
     * @param myCourses     the my courses
     * @param cloudDbAction the cloud db action
     */
    fun upsertMyCourse(myCourses: MyCoursesTable?, cloudDbAction: CloudDbAction?) {
        if (mCloudDBZone == null) {
            return
        }
        val upsertTask = mCloudDBZone!!.executeUpsert(myCourses!!)
        upsertTask
                .addOnSuccessListener { cloudDBZoneResult: Int? ->
                    cloudDbUiCallbackListener!!.onSuccessDbQueryMessage(
                            cloudDbAction, "Successfully added course to my list")
                }
                .addOnFailureListener { exp: Exception? ->
                    cloudDbUiCallbackListener!!.onFailureDbQueryMessage(
                            cloudDbAction, "Insert My Course failed")
                }
    }
}