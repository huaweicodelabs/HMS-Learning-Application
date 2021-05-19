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

package com.huawei.training.java.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.training.R;
import com.huawei.training.java.adapters.CourseDetailsListAdapter;
import com.huawei.training.java.database.CloudDbAction;
import com.huawei.training.java.database.CloudDbUiCallbackListener;
import com.huawei.training.java.database.tables.CourseDetailsTable;
import com.huawei.training.java.database.tables.CoursePlatformTable;
import com.huawei.training.java.database.tables.CoursesDocTable;
import com.huawei.training.java.database.tables.MyCoursesTable;
import com.huawei.training.java.database.tables.RecentlyViewedCoursesTable;
import com.huawei.training.databinding.ActivityCourseDetailsBinding;
import com.huawei.training.java.listeners.CourseTypeItemClick;
import com.huawei.training.java.models.CourseDetailsDataModel;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.LearningApplication;
import com.huawei.training.java.utils.video.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class CourseDetailsActivity extends BaseActivity implements CloudDbUiCallbackListener {
    /**
     * The Binding.
     */
    ActivityCourseDetailsBinding binding;
    /**
     * The My list data.
     */
    ArrayList<CourseDetailsDataModel> myListData;
    /**
     * The Coursename.
     */
    private String coursename = "";
    /**
     * The Course id.
     */
    private int courseId = 0;
    /**
     * The Device platforms.
     */
    private String devicePlatforms = null;
    /**
     * The My course index.
     */
    private int myCourseIndex = 1;
    /**
     * The Recently viewed index.
     */
    private int recentlyViewedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_details);
        checkToFinishActivity(this.getIntent().getExtras());
        Intent intent = getIntent();
        coursename = intent.getStringExtra(Constants.COURSE_NAME);
        courseId = Integer.parseInt(Objects.requireNonNull(
                intent.getStringExtra(Constants.COURSE_ID)));
        ((LearningApplication) getApplication()).setRecentlyViewedCoresRefresh(false);
        setToolbar(coursename);
        init();
    }

    private void init() {

            try {
                if(cloudDbHelper!=null) {
                    cloudDbHelper.addCallBackListener(this);
                }
            } catch (NullPointerException e) {
        }
        binding.share.setOnClickListener(
                start -> {
                    appAnalytics.shareCourseClickEvent(coursename);
                    AppUtils.shareActivity(CourseDetailsActivity.this);
                });
        binding.addtolist.setOnClickListener(
                start -> {
                    if (!showLoginDialog()) {
                        if (userObj != null) {
                            MyCoursesTable myCourses = new MyCoursesTable();
                            myCourses.setUserId(userObj.getuId());
                            myCourses.setMyCourseId(myCourseIndex);
                            myCourses.setCourseId(courseId);
                            myCourses.setCourseName(coursename);
                            appAnalytics.addToMyCourseCourseClickEvent(
                                    userObj.getEmailId(), coursename);
                            cloudDbHelper.getCloudDbQueyCalls().upsertMyCourse(
                                    myCourses, CloudDbAction.GET_MY_COURSES);
                        }
                    }
                });

        AppUtils.showLoadingDialog(this);
        queryWithCourseId();
    }

    private void checkToFinishActivity(Bundle bundle) {
        if (bundle != null) {
            boolean isActivityToBeFinish = Objects.requireNonNull(this.getIntent().getExtras())
                    .getBoolean(Constants.FINISHSTATUS);
            if (isActivityToBeFinish) {
                finish();
            }
        }
    }

    // set course data
    private void setCourseData(List<CoursePlatformTable> coPlatformList) {
        myListData = new ArrayList<>();
        for (CoursePlatformTable coPlatform : coPlatformList) {
            if (devicePlatforms.contains(coPlatform.getPlatformId().toString())) {
                myListData.add(
                        new CourseDetailsDataModel(coPlatform.getPlatformName(),
                                coPlatform.getPlatformId()
                                        .toString(), Constants.PRICE, 3f));
            }
        }
    }

    /**
     * The Course item click.
     */
    CourseTypeItemClick courseItemClick = this::navigatetoCourseDetails;

    // navigate to course details page
    private void navigatetoCourseDetails(CourseDetailsDataModel model) {
        Intent intent = new Intent(CourseDetailsActivity.this,
                CourseContentDetailsActivity.class);
        intent.putExtra(Constants.COURSE_NAME, "" + model.getName());
        intent.putExtra(Constants.COURSE_ID, "" + courseId);
        startActivity(intent);
    }

    // get data from db
    private void queryWithCourseId() {
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(CourseDetailsTable.class);
        cloudDBZoneQuery.equalTo("courseId", courseId);
        cloudDbHelper.getCloudDbQueyCalls().queryCourseDetailsTable
                (cloudDBZoneQuery, CloudDbAction.GET_ALL_COURSES);
    }

    @Override
    public void onSuccessDbData(CloudDbAction cloudDbAction, List<CloudDBZoneObject> dataList) {
        switch (cloudDbAction) {
            case GET_COURSE_DOC:
                loadCourseDoc((CoursesDocTable) ((List) dataList).get(0));
                break;
            case GET_ALL_COURSES:
                loadAllCourses((List) dataList);
                break;
            case GET_COURSE_PLATFORMS:
                loadPlatformCourses((List) dataList);
                break;
            case GET_MY_COURSES:
                loadMyCourses((List) dataList);
                break;
            case GET_MY_COURSES_QUERY:
                loadMyCoursesQuery((List) dataList);
                queryRecentlyViewed(true);
                break;
            case GET_ALL_RECENTLY_VIEWED_COURSES:
                loadRecentCourses((List) dataList);
                break;
            case GET_RECENTLY_VIEWED_COURSES:
                getRecentlyViewedCourses(dataList);
                break;
            default:
                break;
        }
    }

    private void loadCourseDoc(CoursesDocTable coDoc) {
        binding.description.setText(coDoc.getDocDescription());
        devicePlatforms = coDoc.getDocPlatformsIds();
        cloudDbHelper.getCloudDbQueyCalls().queryCoursePlatformTable(null, CloudDbAction.GET_COURSE_PLATFORMS);
    }

    private void loadAllCourses(List allCoursesList) {
        CourseDetailsTable coDetails = (CourseDetailsTable) allCoursesList.get(0);
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(CoursesDocTable.class);
        cloudDBZoneQuery.equalTo("courseName", coDetails.getCourseDocId());
        cloudDbHelper.getCloudDbQueyCalls().queryCoursesDocTable(cloudDBZoneQuery, CloudDbAction.GET_COURSE_DOC);
    }

    private void getRecentlyViewedCourses(List<CloudDBZoneObject> dataList) {
        if (((List) dataList).isEmpty()) insertRecentlyViewedCourse(recentlyViewedIndex);
        AppUtils.cancelLoading();
    }

    private void loadPlatformCourses(List<CoursePlatformTable> coPlatformList) {
        setCourseData(coPlatformList);
        CourseDetailsListAdapter adapter = new CourseDetailsListAdapter(myListData, courseItemClick);
        binding.recyclerviewContentlist.setHasFixedSize(true);
        binding.recyclerviewContentlist.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewContentlist.setAdapter(adapter);
        queryMyCourses(-1);
    }

    private void loadMyCourses(List<MyCoursesTable> myCoursesList) {
        if (!myCoursesList.isEmpty()) {
            myCourseIndex = myCoursesList.get(0).getMyCourseId() + 1;
            queryMyCourses(courseId);
        } else {
            queryRecentlyViewed(true);
        }
    }

    private void loadMyCoursesQuery(List<MyCoursesTable> myCoursesQueryList) {
        if (!myCoursesQueryList.isEmpty()) {
            for (MyCoursesTable myCourses : myCoursesQueryList) {
                if (myCourses.getCourseId() == courseId) {
                    myCourseIndex = myCourses.getMyCourseId();
                    binding.addtolist.setEnabled(false);
                    break;
                }
            }
        }
    }

    private void loadRecentCourses(List<RecentlyViewedCoursesTable> viewedCourses) {
        if (!viewedCourses.isEmpty()) {
            recentlyViewedIndex = viewedCourses.get(0).getRecentlyViewedId() + 1;
            queryRecentlyViewed(false);
        } else {
            insertRecentlyViewedCourse(1);
        }
    }

    @Override
    public void onSuccessDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        AppUtils.cancelLoading();
        if (message != null) {
            showToast(message);
        }
        if (CloudDbAction.GET_MY_COURSES == cloudDbAction) {
            binding.addtolist.setEnabled(false);
        }
    }

    @Override
    public void onFailureDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        AppUtils.cancelLoading();
        if (message != null) {
            showToast(message);
        }
    }

    private void insertRecentlyViewedCourse(int index) {
        if (userObj != null) {
            if (userObj.getuId() != null) {
                RecentlyViewedCoursesTable recentlyViewedCourses = new RecentlyViewedCoursesTable();
                recentlyViewedCourses.setRecentlyViewedId(index);
                recentlyViewedCourses.setCourseId("" + courseId);
                recentlyViewedCourses.setCourseName(coursename);
                recentlyViewedCourses.setUserId(userObj.getuId());
                ((LearningApplication) getApplication()).setRecentlyViewedCoresRefresh(true);
                cloudDbHelper
                        .getCloudDbQueyCalls()
                        .upsertRecentlyViewedCourse(recentlyViewedCourses,
                                CloudDbAction.GET_RECENTLY_VIEWED_COURSES_MSG);
            }
        }
    }

    // get data from db
    private void queryMyCourses(int courseId) {
        if (userObj != null) {
            if (userObj.getuId() != null) {
                CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(MyCoursesTable.class);
                if (courseId > -1) {
                    cloudDBZoneQuery.orderByAsc("myCourseId");
                    cloudDBZoneQuery.contains("userId", userObj.getuId()).equalTo("courseId", courseId);
                    Objects.requireNonNull(this)
                            .cloudDbHelper
                            .getCloudDbQueyCalls()
                            .queryMyCoursesTable(cloudDBZoneQuery, CloudDbAction.GET_MY_COURSES_QUERY);
                } else {
                    cloudDBZoneQuery.orderByDesc("myCourseId");
                    Objects.requireNonNull(this)
                            .cloudDbHelper
                            .getCloudDbQueyCalls()
                            .queryMyCoursesTable(cloudDBZoneQuery, CloudDbAction.GET_MY_COURSES);
                }
            }
        } else {
            AppUtils.cancelLoading();
        }
    }

    //calling query for recently viewed courses
    private void queryRecentlyViewed(Boolean isAllList) {
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(RecentlyViewedCoursesTable.class);
        if (isAllList) {
            cloudDBZoneQuery.orderByDesc("recentlyViewedId");
            cloudDbHelper
                    .getCloudDbQueyCalls()
                    .queryRecentlyRecentlyViewedCoursesTable
                            (cloudDBZoneQuery, CloudDbAction.GET_ALL_RECENTLY_VIEWED_COURSES);
        } else {
            if (userObj.getuId() != null) {
                cloudDBZoneQuery.equalTo("userId", userObj.getuId()).equalTo("courseId", "" + courseId);
                cloudDbHelper
                        .getCloudDbQueyCalls()
                        .queryRecentlyRecentlyViewedCoursesTable
                                (cloudDBZoneQuery, CloudDbAction.GET_RECENTLY_VIEWED_COURSES);
            }
        }
    }
}
