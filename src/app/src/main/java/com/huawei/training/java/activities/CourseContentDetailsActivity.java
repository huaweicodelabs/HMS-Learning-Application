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
import com.huawei.training.java.adapters.CourseContentDetailsListAdapter;
import com.huawei.training.java.database.CloudDbAction;
import com.huawei.training.java.database.CloudDbUiCallbackListener;
import com.huawei.training.java.database.tables.CourseContentTable;
import com.huawei.training.databinding.ActivityCourseContentDetailsBinding;
import com.huawei.training.java.listeners.CourseContentItemClick;
import com.huawei.training.java.models.CourseContentDataModel;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.video.Constants;
import com.huawei.training.java.utils.video.entity.PlayEntity;

import java.util.List;
import java.util.Objects;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
public class CourseContentDetailsActivity extends BaseActivity
        implements CloudDbUiCallbackListener {
    /**
     * The Binding.
     */
    ActivityCourseContentDetailsBinding binding;
    /**
     * The My list data.
     */
    List<CourseContentDataModel> myListData;
    /**
     * The Course name.
     */
    private String courseName = "";
    /**
     * The Course id.
     */
    private int courseId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_course_content_details);
        Intent intent = getIntent();
        courseName = intent.getStringExtra(Constants.COURSE_NAME);
        courseId = Integer.parseInt(
                Objects.requireNonNull(intent.getStringExtra(Constants.COURSE_ID)));
        setToolbar(courseName);
        cloudDbHelper.addCallBackListener(this);
        AppUtils.showLoadingDialog(this);
        queryWithCourseId();
        binding.startcourse.setOnClickListener(
                start -> {
                    if (!showLoginDialog()) {
                        if (userObj != null) {
                            appAnalytics.startCourseClickEvent(courseName);
                            PlayEntity playEntity = new PlayEntity();
                            playEntity.setUrl(getResources().getString(R.string.url));
                            playEntity.setUrlType(Constants.UrlType.URL);
                            Intent intent1 = new Intent(CourseContentDetailsActivity.this, PlayActivity.class);
                            intent1.putExtra(Constants.COURSE_NAME, courseName);
                            intent1.putExtra(Constants.COURSE_ID, "" + courseId);
                            startActivity(intent1);
                        }
                    }
                });
    }

    // set course data
    private void setCourseData(List<CourseContentTable> contentList) {
        myListData = AppUtils.getCourseOrCodelabContentData(contentList.get(0), null);
    }

    /**
     * The Course item click.
     */
    CourseContentItemClick courseItemClick = model -> {
    };

    // get data from db using id
    private void queryWithCourseId() {
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(CourseContentTable.class);
        cloudDBZoneQuery.equalTo("courseId", Long.parseLong("" + courseId));
        cloudDbHelper.getCloudDbQueyCalls().queryCourseContentTable(
                cloudDBZoneQuery, CloudDbAction.GET_COURSE_CONTENT);
    }

    @Override
    public void onSuccessDbData(CloudDbAction cloudDbAction, List<CloudDBZoneObject> dataList) {
        AppUtils.cancelLoading();
        if (cloudDbAction == CloudDbAction.GET_COURSE_CONTENT) {
            setCourseData((List) dataList);
            CourseContentDetailsListAdapter adapter = new CourseContentDetailsListAdapter(
                    myListData, courseItemClick);
            binding.recyclerviewContentlist.setHasFixedSize(true);
            binding.recyclerviewContentlist.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerviewContentlist.setAdapter(adapter);
        }
    }

    @Override
    public void onSuccessDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        AppUtils.cancelLoading();
    }

    @Override
    public void onFailureDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        showToast(message);
        AppUtils.cancelLoading();
    }
}
