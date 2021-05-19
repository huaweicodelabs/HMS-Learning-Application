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

package com.huawei.training.java.database;

import android.app.Activity;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.cloud.database.CloudDBZone;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.training.java.database.tables.CourseContentTable;
import com.huawei.training.java.database.tables.CourseDetailsTable;
import com.huawei.training.java.database.tables.CoursePlatformTable;
import com.huawei.training.java.database.tables.CoursesCodelabDetailsTable;
import com.huawei.training.java.database.tables.CoursesDocTable;
import com.huawei.training.java.database.tables.CoursesMainCategoryTable;
import com.huawei.training.java.database.tables.ExamTable;
import com.huawei.training.java.database.tables.MyCoursesTable;
import com.huawei.training.java.database.tables.QuestionsTable;
import com.huawei.training.java.database.tables.RecentlyViewedCoursesTable;
import com.huawei.training.java.database.tables.UsersInfoTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class CloudDbQueyCalls {
    /**
     * The M cloud db zone.
     */
    CloudDBZone mCloudDBZone = null;
    /**
     * The Cloud db ui callback listener.
     */
    CloudDbUiCallbackListener cloudDbUiCallbackListener;

    /**
     * Sets cloud db zone.
     *
     * @param mCloudDBZone the m cloud db zone
     */
    public void setmCloudDBZone(CloudDBZone mCloudDBZone) {
        this.mCloudDBZone = mCloudDBZone;
    }

    /**
     * Sets cloud db ui callback listener.
     *
     * @param cloudDbUiCallbackListener the cloud db ui callback listener
     */
    public void setCloudDbUiCallbackListener(CloudDbUiCallbackListener cloudDbUiCallbackListener) {
        this.cloudDbUiCallbackListener = cloudDbUiCallbackListener;
    }

    /**
     * Login.
     *
     * @param activity      the activity
     * @param cloudDbAction the cloud db action
     */
    public void login(Activity activity, CloudDbAction cloudDbAction) {
        AGConnectAuth auth = AGConnectAuth.getInstance();
        auth.signInAnonymously()
                .addOnSuccessListener(
                        activity,
                        signInResult -> {
                            cloudDbUiCallbackListener.onSuccessDbQueryMessage(
                                    cloudDbAction, "Successfully Loggedin into DB");
                        })
                .addOnFailureListener(
                        activity,
                        e -> {
                            cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                    cloudDbAction, "Failed to Loggedin into DB");
                        });
    }

    // -------------------------------------------------Inserting User Data to
    // DB----------------------------------------------------

    /**
     * Insterting user information to cloud DB
     *
     * @param userInfo      the user info
     * @param cloudDbAction the cloud db action
     */
    public void upsertUserInfo(UsersInfoTable userInfo, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }

        Task<Integer> upsertTask = mCloudDBZone.executeUpsert(userInfo);
        upsertTask
                .addOnSuccessListener(
                        new OnSuccessListener<Integer>() {
                            @Override
                            public void onSuccess(Integer cloudDBZoneResult) {
                                cloudDbUiCallbackListener.onSuccessDbQueryMessage(
                                        cloudDbAction, null);
                            }
                        })
                .addOnFailureListener(
                        e -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, "Insert UserInfo failed"));
    }

    /**
     * Query recently viewed courses in storage from cloud
     * side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryUser(CloudDBZoneQuery<UsersInfoTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<UsersInfoTable>> userInfoQueryTask =
                mCloudDBZone.executeQuery(query,
                        CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);

        userInfoQueryTask
                .addOnSuccessListener(
                        userInfoCloudDBZoneSnapshot -> processUserInfo(
                                userInfoCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        ex -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, ""));
    }

    private void processUserInfo(
            CloudDBZoneSnapshot<UsersInfoTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<UsersInfoTable> userInfoCursor = snapshot.getSnapshotObjects();
        List<UsersInfoTable> userInfoArrayList = new ArrayList<>();
        try {
            while (userInfoCursor.hasNext()) {
                UsersInfoTable userInfoCourses = userInfoCursor.next();
                userInfoArrayList.add(userInfoCourses);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(cloudDbAction, (List) userInfoArrayList);
    }

    /**
     * Query all main course categories in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param cloudDbAction the cloud db action
     */
    public void queryAllMainCategories(CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<CoursesMainCategoryTable>> coMainCategoryQueryTask =
                mCloudDBZone.executeQuery(
                        CloudDBZoneQuery.where(CoursesMainCategoryTable.class),
                        CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);

        coMainCategoryQueryTask
                .addOnSuccessListener(
                        coMainCategoryCloudDBZoneSnapshot -> processMainCategories
                                (coMainCategoryCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        e -> cloudDbUiCallbackListener.onFailureDbQueryMessage
                                (cloudDbAction, ""));
    }

    private void processMainCategories(CloudDBZoneSnapshot<CoursesMainCategoryTable> snapshot
            , CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<CoursesMainCategoryTable>
                courseMainCategoryCursor = snapshot.getSnapshotObjects();
        List<CoursesMainCategoryTable> coMainCategoryArrayList = new ArrayList<>();
        try {
            while (courseMainCategoryCursor.hasNext()) {
                CoursesMainCategoryTable coMainCategory = courseMainCategoryCursor.next();
                coMainCategoryArrayList.add(coMainCategory);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(cloudDbAction, (List) coMainCategoryArrayList);
    }

    /**
     * Query recently viewed courses in storage from cloud side
     * with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryRecentlyRecentlyViewedCoursesTable(
            CloudDBZoneQuery<RecentlyViewedCoursesTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<RecentlyViewedCoursesTable>>
                recentlyRecentlyViewedCoursesTableQueryTask =
                mCloudDBZone.executeQuery(query,
                        CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);

        recentlyRecentlyViewedCoursesTableQueryTask
                .addOnSuccessListener(
                        coMainCategoryCloudDBZoneSnapshot ->
                                processRecentlyRecentlyViewedCoursesTable(
                                        coMainCategoryCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        ex -> cloudDbUiCallbackListener.onFailureDbQueryMessage(cloudDbAction,
                                ""));
    }

    private void processRecentlyRecentlyViewedCoursesTable(
            CloudDBZoneSnapshot<RecentlyViewedCoursesTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<RecentlyViewedCoursesTable>
                recentlyRecentlyViewedCoursesTableCursor = snapshot.getSnapshotObjects();
        List<RecentlyViewedCoursesTable>
                recentlyRecentlyViewedCoursesTableArrayList = new ArrayList<>();
        try {
            while (recentlyRecentlyViewedCoursesTableCursor.hasNext()) {
                RecentlyViewedCoursesTable
                        recentlyRecentlyViewedCoursesTable =
                        recentlyRecentlyViewedCoursesTableCursor.next();
                recentlyRecentlyViewedCoursesTableArrayList.add(recentlyRecentlyViewedCoursesTable);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(
                cloudDbAction, (List) recentlyRecentlyViewedCoursesTableArrayList);
    }

    /**
     * Query course details courses in storage from cloud
     * side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryCourseDetailsTable(
            CloudDBZoneQuery<CourseDetailsTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<CourseDetailsTable>> coDetailsQueryTask = null;
        coDetailsQueryTask =
                mCloudDBZone.executeQuery(
                        CloudDBZoneQuery.where(CourseDetailsTable.class),
                        CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);

        if (query != null) {
            coDetailsQueryTask =
                    mCloudDBZone.executeQuery(
                            query,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        }


        coDetailsQueryTask
                .addOnSuccessListener(
                        coDetailsCloudDBZoneSnapshot -> processCourseDetailsTable(
                                coDetailsCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        e -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, ""));
    }

    private void processCourseDetailsTable(
            CloudDBZoneSnapshot<CourseDetailsTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<CourseDetailsTable>
                coDetailsCursor = snapshot.getSnapshotObjects();
        List<CourseDetailsTable> coursesArrayList = new ArrayList<>();
        try {
            while (coDetailsCursor.hasNext()) {
                CourseDetailsTable coDetails = coDetailsCursor.next();
                coursesArrayList.add(coDetails);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(cloudDbAction, (List) coursesArrayList);
    }

    /**
     * Query course content courses in storage from cloud
     * side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryCourseContentTable(
            CloudDBZoneQuery<CourseContentTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<CourseContentTable>> coContentQueryTask = null;

        if (query == null) {
            coContentQueryTask =
                    mCloudDBZone.executeQuery(
                            CloudDBZoneQuery.where(CourseContentTable.class),
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        } else {
            coContentQueryTask =
                    mCloudDBZone.executeQuery(
                            query,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        }

        coContentQueryTask
                .addOnSuccessListener(
                        coContentCloudDBZoneSnapshot -> processCourseContentTable(
                                coContentCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        e -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction,""));
    }

    private void processCourseContentTable(
            CloudDBZoneSnapshot<CourseContentTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<CourseContentTable>
                coContentCursor = snapshot.getSnapshotObjects();
        List<CourseContentTable> coContentArrayList = new ArrayList<>();
        try {
            while (coContentCursor.hasNext()) {
                CourseContentTable coContent = coContentCursor.next();
                coContentArrayList.add(coContent);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(cloudDbAction, (List) coContentArrayList);
    }

    /**
     * Query course platforms in storage from cloud side
     * with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryCoursePlatformTable(
            CloudDBZoneQuery<CoursePlatformTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<CoursePlatformTable>> coPlatformQueryTask = null;
        coPlatformQueryTask =
                mCloudDBZone.executeQuery(
                        CloudDBZoneQuery.where(CoursePlatformTable.class),
                        CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        if (query != null) {
            coPlatformQueryTask =
                    mCloudDBZone.executeQuery(
                            query,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        }

        coPlatformQueryTask
                .addOnSuccessListener(
                        coPlatformCloudDBZoneSnapshot -> processCoursePlatformTables(
                                coPlatformCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        e -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction,""));
    }

    private void processCoursePlatformTables(
            CloudDBZoneSnapshot<CoursePlatformTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<CoursePlatformTable>
                coPlatformCursor = snapshot.getSnapshotObjects();
        List<CoursePlatformTable> coPlatformArrayList = new ArrayList<>();
        try {
            while (coPlatformCursor.hasNext()) {
                CoursePlatformTable coContent = coPlatformCursor.next();
                coPlatformArrayList.add(coContent);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(
                cloudDbAction, (List) coPlatformArrayList);
    }

    /**
     * Query course Doc in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryCoursesDocTable(
            CloudDBZoneQuery<CoursesDocTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<CoursesDocTable>> coDocQueryTask = null;
        if (query == null) {
            coDocQueryTask =
                    mCloudDBZone.executeQuery(
                            CloudDBZoneQuery.where(CoursesDocTable.class),
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        } else {
            coDocQueryTask =
                    mCloudDBZone.executeQuery(
                            query,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        }
        coDocQueryTask
                .addOnSuccessListener(
                        coDocCloudDBZoneSnapshot -> processCoursesDocTable(
                                coDocCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        ex -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, ""));
    }

    private void processCoursesDocTable(
            CloudDBZoneSnapshot<CoursesDocTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<CoursesDocTable>
                coDocCursor = snapshot.getSnapshotObjects();
        List<CoursesDocTable> coDocArrayList = new ArrayList<>();
        try {
            while (coDocCursor.hasNext()) {
                CoursesDocTable coDoc = coDocCursor.next();
                coDocArrayList.add(coDoc);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(cloudDbAction, (List) coDocArrayList);
    }

    /**
     * Query com.huawei.training.java.database.tables.ExamTable
     * details in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryExamTableDetails(
            CloudDBZoneQuery<ExamTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<ExamTable>> examDetailsQueryTask = null;

        if (query == null) {
            examDetailsQueryTask =
                    mCloudDBZone.executeQuery(
                            CloudDBZoneQuery.where(ExamTable.class),
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        } else {
            examDetailsQueryTask =
                    mCloudDBZone.executeQuery(
                            query,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        }

        examDetailsQueryTask
                .addOnSuccessListener(
                        examCloudDBZoneSnapshot -> processExamTableDetails(
                                examCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        e -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, ""));
    }

    private void processExamTableDetails(
            CloudDBZoneSnapshot<ExamTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<ExamTable> examCursor = snapshot.getSnapshotObjects();
        List<ExamTable> examArrayList = new ArrayList<>();
        try {
            while (examCursor.hasNext()) {
                ExamTable coDoc = examCursor.next();
                examArrayList.add(coDoc);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(
                cloudDbAction, (List) examArrayList);
    }

    /**
     * Query com.huawei.training.java.database.tables.
     * QuestionsTable in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryQuestionsTable(
            CloudDBZoneQuery<QuestionsTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<QuestionsTable>> questionsQueryTask = null;

        if (query == null) {
            questionsQueryTask =
                    mCloudDBZone.executeQuery(
                            CloudDBZoneQuery.where(QuestionsTable.class),
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        } else {
            questionsQueryTask =
                    mCloudDBZone.executeQuery(
                            query,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        }

        questionsQueryTask
                .addOnSuccessListener(
                        questionsCloudDBZoneSnapshot -> processQuestionsTable(
                                questionsCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        e -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction,""));
    }

    private void processQuestionsTable(
            CloudDBZoneSnapshot<QuestionsTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<QuestionsTable>
                questionsCursor = snapshot.getSnapshotObjects();
        List<QuestionsTable> questionsArrayList = new ArrayList<>();
        try {
            while (questionsCursor.hasNext()) {
                QuestionsTable questions = questionsCursor.next();
                questionsArrayList.add(questions);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(
                cloudDbAction, (List) questionsArrayList);
    }

    /**
     * Query Codelab in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryCodelab(
            CloudDBZoneQuery<CoursesCodelabDetailsTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<CoursesCodelabDetailsTable>> coCodelabDocQueryTask;

        if (query == null) {
            coCodelabDocQueryTask =
                    mCloudDBZone.executeQuery(
                            CloudDBZoneQuery.where(CoursesCodelabDetailsTable.class),
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        } else {
            coCodelabDocQueryTask =
                    mCloudDBZone.executeQuery(
                            query,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        }

        coCodelabDocQueryTask
                .addOnSuccessListener(
                        coCodelabDocCloudDBZoneSnapshot -> processCoursesCodelabDetailsTable(
                                coCodelabDocCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        exp -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, ""));
    }

    private void processCoursesCodelabDetailsTable(
            CloudDBZoneSnapshot<CoursesCodelabDetailsTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<CoursesCodelabDetailsTable>
                coCOdeLabCursor = snapshot.getSnapshotObjects();
        List<CoursesCodelabDetailsTable> coCodelabDocArrayList = new ArrayList<>();
        try {
            while (coCOdeLabCursor.hasNext()) {
                CoursesCodelabDetailsTable coCodelabDoc = coCOdeLabCursor.next();
                coCodelabDocArrayList.add(coCodelabDoc);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(
                cloudDbAction, (List) coCodelabDocArrayList);
    }

    /**
     * Query My courses in storage from cloud side with
     * CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     *
     * @param query         the query
     * @param cloudDbAction the cloud db action
     */
    public void queryMyCoursesTable(
            CloudDBZoneQuery<MyCoursesTable> query, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<CloudDBZoneSnapshot<MyCoursesTable>> myCoursesQueryTask = null;

        if (query == null) {
            myCoursesQueryTask =
                    mCloudDBZone.executeQuery(
                            CloudDBZoneQuery.where(MyCoursesTable.class),
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        } else {
            myCoursesQueryTask =
                    mCloudDBZone.executeQuery(
                            query,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        }

        myCoursesQueryTask
                .addOnSuccessListener(
                        myCoursesCloudDBZoneSnapshot -> processMyCoursesTable(
                                myCoursesCloudDBZoneSnapshot, cloudDbAction))
                .addOnFailureListener(
                        exp -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, ""));
    }

    private void processMyCoursesTable(
            CloudDBZoneSnapshot<MyCoursesTable> snapshot, CloudDbAction cloudDbAction) {
        CloudDBZoneObjectList<MyCoursesTable>
                myCoursesCursor = snapshot.getSnapshotObjects();
        List<MyCoursesTable> myCoursesDocArrayList = new ArrayList<>();
        try {
            while (myCoursesCursor.hasNext()) {
                MyCoursesTable myCourses = myCoursesCursor.next();
                myCoursesDocArrayList.add(myCourses);
            }
        } catch (AGConnectCloudDBException exp) {
        } finally {
            snapshot.release();
        }
        cloudDbUiCallbackListener.onSuccessDbData(cloudDbAction, (List) myCoursesDocArrayList);
    }

    /**
     * Upsert recently viewed course.
     *
     * @param recentlyRecentlyViewedCoursesTable the recently viewed courses
     * @param cloudDbAction                      the cloud db action
     */
    public void upsertRecentlyViewedCourse(
            RecentlyViewedCoursesTable recentlyRecentlyViewedCoursesTable,
            CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }

        Task<Integer> upsertTask = mCloudDBZone.executeUpsert(recentlyRecentlyViewedCoursesTable);
        upsertTask
                .addOnSuccessListener(
                        cloudDBZoneResult -> cloudDbUiCallbackListener.onSuccessDbQueryMessage(
                                cloudDbAction, null))
                .addOnFailureListener(
                        exp -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, ""));
    }

    /**
     * Upsert my course.
     *
     * @param myCourses     the my courses
     * @param cloudDbAction the cloud db action
     */
    public void upsertMyCourse(MyCoursesTable myCourses, CloudDbAction cloudDbAction) {
        if (mCloudDBZone == null) {
            return;
        }
        Task<Integer> upsertTask = mCloudDBZone.executeUpsert(myCourses);
        upsertTask
                .addOnSuccessListener(
                        cloudDBZoneResult -> cloudDbUiCallbackListener.onSuccessDbQueryMessage(
                                cloudDbAction, "Successfully added course to my list"))
                .addOnFailureListener(
                        exp -> cloudDbUiCallbackListener.onFailureDbQueryMessage(
                                cloudDbAction, "Insert My Course failed"));
    }
}
