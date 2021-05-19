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

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.training.R;
import com.huawei.training.java.database.CloudDbAction;
import com.huawei.training.java.database.CloudDbUiCallbackListener;
import com.huawei.training.java.database.tables.CourseDetailsTable;
import com.huawei.training.java.database.tables.CoursesMainCategoryTable;
import com.huawei.training.java.database.tables.RecentlyViewedCoursesTable;
import com.huawei.training.databinding.ActivityHomeBinding;
import com.huawei.training.java.fragments.HomeFragment;
import com.huawei.training.java.fragments.MyCoursesFragment;
import com.huawei.training.java.fragments.ProfileFragment;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.LearningApplication;
import com.huawei.training.java.utils.eventmanager.AnalyticsConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class HomeActivity extends BaseActivity implements CloudDbUiCallbackListener {
    /**
     * The Main category list.
     */
    public List<CoursesMainCategoryTable> mainCategoryList = new ArrayList<>();
    /**
     * The Co details array list.
     */
    public List<CourseDetailsTable> coDetailsArrayList = new ArrayList<>();
    /**
     * The Recently viewed courses array list.
     */
    public List<RecentlyViewedCoursesTable> recentlyViewedCoursesArrayList = new ArrayList<>();
    /**
     * The Binding.
     */
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(HomeFragment.newInstance());
        mainCategoryList = new ArrayList<>();
        AppUtils.showLoadingDialog(this);
        fetchDataFromDB();
    }

    /**
     * Open fragment.
     *
     * @param fragment the fragment
     */
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Fetching data from Cloud DB
     */
    private void fetchDataFromDB() {
        cloudDbHelper.addCallBackListener(this);
        cloudDbHelper.getCloudDbQueyCalls().queryAllMainCategories(
                CloudDbAction.GET_MAIN_CATEGORIES);
    }

    /**
     * The Navigation item selected listener.
     */
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        openFragment(HomeFragment.newInstance());
                        appAnalytics.bottomTabClickEvent(AnalyticsConstants.TAB_HOME);
                        return true;
                    case R.id.navigation_profile:
                        if (!showLoginDialog()) {
                            openFragment(ProfileFragment.newInstance("", ""));
                            appAnalytics.bottomTabClickEvent(AnalyticsConstants.TAB_PROFILE);
                            return true;
                        }
                        break;
                    case R.id.navigation_courses:
                        if (!showLoginDialog()) {
                            openFragment(MyCoursesFragment.newInstance());
                            appAnalytics.bottomTabClickEvent(AnalyticsConstants.TAB_MY_COURSE);
                            return true;
                        }
                        break;
                }
                return false;
            };

    //calling query for recently viewed courses
    private Boolean queryRecentlyViewed() {
        if (userObj != null) {
            if (userObj.getuId() != null) {
                CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(
                        RecentlyViewedCoursesTable.class);
                cloudDBZoneQuery.equalTo("userId", userObj.getuId());
                cloudDbHelper
                        .getCloudDbQueyCalls()
                        .queryRecentlyRecentlyViewedCoursesTable
                                (cloudDBZoneQuery, CloudDbAction.GET_RECENTLY_VIEWED_COURSES);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSuccessDbData(CloudDbAction cloudDbAction, List<CloudDBZoneObject> dataList) {
        switch (cloudDbAction) {
            case GET_MAIN_CATEGORIES:
                this.mainCategoryList.addAll((List) dataList);
                cloudDbHelper.getCloudDbQueyCalls().queryCourseDetailsTable(
                        null, CloudDbAction.GET_ALL_COURSES);
                break;
            case GET_RECENTLY_VIEWED_COURSES:
                this.recentlyViewedCoursesArrayList = (List) dataList;
                if (Build.VERSION.SDK_INT >= 24) {
                    viewedCourses();
                }
                break;
            case GET_ALL_COURSES:
                loadAllCourses(dataList);
                AppUtils.cancelLoading();
                break;
            default:
                break;
        }
    }

    private void viewedCourses() {
        if ((getSupportFragmentManager().getFragments().get(0) instanceof HomeFragment)) {
            if (Build.VERSION.SDK_INT >= 24) {
                ((HomeFragment) (getSupportFragmentManager().getFragments().get(0)))
                        .setData(mainCategoryList,
                                coDetailsArrayList, recentlyViewedCoursesArrayList);
            }
        }
    }

    private void loadAllCourses(List<CloudDBZoneObject> dataList) {
        if (coDetailsArrayList.size() == 0) {
            this.coDetailsArrayList.addAll((List) dataList);
        }
        if (!queryRecentlyViewed()) {
            if ((getSupportFragmentManager().getFragments().get(0) instanceof HomeFragment)) {
                if (Build.VERSION.SDK_INT >= 24) {
                    ((HomeFragment) (getSupportFragmentManager().getFragments().get(0))).
                            setData(mainCategoryList,
                                    coDetailsArrayList, recentlyViewedCoursesArrayList);
                }
            }
        }
    }

    @Override
    public void onSuccessDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        AppUtils.cancelLoading();
        if (message != null) showToast(message);
    }

    @Override
    public void onFailureDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        AppUtils.cancelLoading();
        if (cloudDbAction != CloudDbAction.INSERT_USER_INFO) {
            if (message != null) showToast(message);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (((LearningApplication) getApplication()).isRecentlyViewedCoresRefresh())
            fetchDataFromDB();
    }
}
