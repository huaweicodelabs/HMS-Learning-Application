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

package com.huawei.training.java.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.training.R;
import com.huawei.training.java.activities.BaseActivity;
import com.huawei.training.java.activities.PlayActivity;
import com.huawei.training.java.adapters.MyCourseListAdapter;
import com.huawei.training.java.database.CloudDbAction;
import com.huawei.training.java.database.CloudDbHelper;
import com.huawei.training.java.database.CloudDbUiCallbackListener;
import com.huawei.training.java.database.tables.MyCoursesTable;
import com.huawei.training.databinding.FragmentMyCoursesBinding;
import com.huawei.training.java.listeners.CourseItemClick;
import com.huawei.training.java.models.CourseDataModel;
import com.huawei.training.java.models.UserObj;
import com.huawei.training.java.utils.LearningApplication;
import com.huawei.training.java.utils.eventmanager.AnalyticsConstants;
import com.huawei.training.java.utils.eventmanager.AppAnalytics;
import com.huawei.training.java.utils.video.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class MyCoursesFragment extends Fragment implements CloudDbUiCallbackListener {
    /**
     * The Model list.
     */
    List<CourseDataModel> modelList = new ArrayList<>();
    /**
     * The Binding.
     */
    FragmentMyCoursesBinding binding = null;
    /**
     * The User obj.
     */
    UserObj userObj = null;
    /**
     * The Cloud db helper.
     */
    CloudDbHelper cloudDbHelper = null;
    /**
     * The App analytics.
     */
    AppAnalytics appAnalytics = null;

    /**
     * Instantiates a new My courses fragment.
     */
    public MyCoursesFragment() {
    }

    /**
     * New instance my courses fragment.
     *
     * @return the my courses fragment
     */
    public static MyCoursesFragment newInstance() {
        MyCoursesFragment fragment = new MyCoursesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userObj = ((LearningApplication) Objects.requireNonNull(
                getActivity()).getApplication()).getUserObj();
        cloudDbHelper = CloudDbHelper.getInstance(getActivity().getApplicationContext());
        appAnalytics = ((BaseActivity) getActivity()).appAnalytics;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyCoursesBinding.inflate(inflater, container, false);
        cloudDbHelper.addCallBackListener(this);
        queryMyCourses();
        return binding.getRoot();
    }

    // get data from db
    private void queryMyCourses() {
        if (userObj != null) {
            if (userObj.getuId() != null) {
                CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(MyCoursesTable.class);
                cloudDBZoneQuery.equalTo("userId", userObj.getuId());
                if(cloudDbHelper != null){
                    cloudDbHelper.getCloudDbQueyCalls()
                            .queryMyCoursesTable(cloudDBZoneQuery, CloudDbAction.GET_MY_COURSES);
                }
            }
        }
    }

    // bind data to adapter
    private void loadData() {
        MyCourseListAdapter adapter = new MyCourseListAdapter(modelList, courseItemClick);
        binding.recyclerviewMycourseslist.setHasFixedSize(true);
        binding.recyclerviewMycourseslist.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewMycourseslist.setAdapter(adapter);
    }

    /**
     * The Course item click.
     */
// course item click
    CourseItemClick courseItemClick =
            model -> {
                appAnalytics.courseClickEvent(
                        AnalyticsConstants.MY_COURSES_SCREEN, model.getName());
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra(Constants.COURSE_NAME, "" + model.getName());
                intent.putExtra(Constants.COURSE_ID, model.getCourseId());
                startActivity(intent);
            };

    @Override
    public void onSuccessDbData(CloudDbAction cloudDbAction, List<CloudDBZoneObject> dataList) {
        if (cloudDbAction == CloudDbAction.GET_MY_COURSES) {
            CourseDataModel model;
            List<MyCoursesTable> myCoursesList = (List) dataList;
            if (myCoursesList.isEmpty()) {
                ((BaseActivity) Objects.requireNonNull(getActivity()))
                        .showToast("Not found any courses. Please add at least one");
            }
            modelList.clear();
            for (MyCoursesTable myCourses : myCoursesList) {
                model =
                        new CourseDataModel(
                                "" + myCourses.getCourseId(),
                                myCourses.getCourseName(),
                                getResources().getString(R.string.Rs) + "500",
                                3f,
                                android.R.mipmap.sym_def_app_icon);
                modelList.add(model);
            }
            loadData();
        }
    }

    @Override
    public void onSuccessDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        if (message != null)
            ((BaseActivity) Objects.requireNonNull(getActivity())).showToast(message);
    }

    @Override
    public void onFailureDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        if (message != null)
            ((BaseActivity) Objects.requireNonNull(getActivity())).showToast(message);
    }
}
