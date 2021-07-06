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

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.training.R
import com.huawei.training.kotlin.database.CloudDbHelper
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.CloudDbUiCallbackListener
import com.huawei.training.kotlin.database.tables.CourseDetailsTable
import com.huawei.training.kotlin.database.tables.CoursesMainCategoryTable
import com.huawei.training.kotlin.database.tables.RecentlyViewedCoursesTable
import com.huawei.training.kotlin.fragments.HomeFragment
import com.huawei.training.kotlin.fragments.MyCoursesFragment
import com.huawei.training.kotlin.fragments.ProfileFragment
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.LearningApplication
import com.huawei.training.kotlin.utils.eventmanager.AnalyticsConstants
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class HomeActivity : BaseActivity(), CloudDbUiCallbackListener {
    /**
     * The Main category list.
     */
    @JvmField
    var mainCategoryList: MutableList<CoursesMainCategoryTable> = ArrayList()

    /**
     * The Co details array list.
     */
    @JvmField
    var coDetailsArrayList: MutableList<CourseDetailsTable> = ArrayList()

    /**
     * The Recently viewed courses array list.
     */
    @JvmField
    var recentlyViewedCoursesArrayList: List<RecentlyViewedCoursesTable> = ArrayList()

    /**
     * Gets cloud db helper.
     *
     * @return the cloud db helper
     */
    var cloudDbHelper: CloudDbHelper?=null

    /**
     * The Binding.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        openFragment(HomeFragment.newInstance())
        AppUtils.showLoadingDialog(this)
        fetchDataFromDB()
    }

    /**
     * Open fragment.
     *
     * @param fragment the fragment
     */
    fun openFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /**
     * Fetching data from Cloud DB
     */
    private fun fetchDataFromDB() {
        cloudDbHelper= CloudDbHelper.getInstance(applicationContext)
        cloudDbHelper?.addCallBackListener(this)
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryAllMainCategories(
                CloudDbAction.GET_MAIN_CATEGORIES)
    }

    /**
     * The Navigation item selected listener.
     */
    var navigationItemSelectedListener = label@ BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
        when (item.itemId) {
            R.id.navigation_home -> {
                openFragment(HomeFragment.newInstance())
                appAnalytics?.bottomTabClickEvent(AnalyticsConstants.TAB_HOME)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> if (!showLoginDialog()) {
                openFragment(ProfileFragment.newInstance("", ""))
                appAnalytics?.bottomTabClickEvent(AnalyticsConstants.TAB_PROFILE)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_courses -> if (!showLoginDialog()) {
                openFragment(MyCoursesFragment.newInstance())
                appAnalytics?.bottomTabClickEvent(AnalyticsConstants.TAB_MY_COURSE)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //calling query for recently viewed courses
    private fun queryRecentlyViewed(): Boolean {
        if (userObj != null) {
            if (userObj?.getuId() != null) {
                val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(
                        RecentlyViewedCoursesTable::class.java)
                cloudDBZoneQuery.equalTo("userId", userObj?.getuId())
                cloudDbHelper
                        ?.getCloudDbAllQueyCalls()
                        ?.queryRecentlyRecentlyViewedCoursesTable(cloudDBZoneQuery as CloudDBZoneQuery<RecentlyViewedCoursesTable>?, CloudDbAction.GET_RECENTLY_VIEWED_COURSES)
                return true
            }
        }
        return false
    }

    override fun onSuccessDbData(cloudDbAction: CloudDbAction?, dataList: List<CloudDBZoneObject?>?) {
        when (cloudDbAction) {
            CloudDbAction.GET_MAIN_CATEGORIES -> {
                mainCategoryList.addAll(dataList as List<CoursesMainCategoryTable>)
                cloudDbHelper?.cloudDbQueyCalls?.queryCourseDetailsTable(
                        null, CloudDbAction.GET_ALL_COURSES)
            }
            CloudDbAction.GET_RECENTLY_VIEWED_COURSES -> {
                recentlyViewedCoursesArrayList = dataList as List<RecentlyViewedCoursesTable>
                if (Build.VERSION.SDK_INT >= 24) {
                    viewedCourses()
                }
            }
            CloudDbAction.GET_ALL_COURSES -> {
                loadAllCourses(dataList as List<CloudDBZoneObject>)
                AppUtils.cancelLoading()
            }
            else -> {
            }
        }
    }

    private fun viewedCourses() {
        if (supportFragmentManager.fragments[0] is HomeFragment) {
            if (Build.VERSION.SDK_INT >= 24) {
                (supportFragmentManager.fragments[0] as HomeFragment)
                        .setData(mainCategoryList,
                                coDetailsArrayList, recentlyViewedCoursesArrayList)
            }
        }
    }

    private fun loadAllCourses(dataList: List<CloudDBZoneObject>) {
        if (coDetailsArrayList.size == 0) {
            coDetailsArrayList.addAll(dataList as List<CourseDetailsTable>)
        }
        if (!queryRecentlyViewed()) {
            if (supportFragmentManager.fragments[0] is HomeFragment) {
                if (Build.VERSION.SDK_INT >= 24) {
                    (supportFragmentManager.fragments[0] as HomeFragment).setData(mainCategoryList,
                            coDetailsArrayList, recentlyViewedCoursesArrayList)
                }
            }
        }
    }

    override fun onSuccessDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        AppUtils.cancelLoading()
        message?.let { showToast(it) }
    }

    override fun onFailureDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        AppUtils.cancelLoading()
        if (cloudDbAction != CloudDbAction.INSERT_USER_INFO) {
            message?.let { showToast(it) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        if ((application as LearningApplication).isRecentlyViewedCoresRefresh) fetchDataFromDB()
    }
}