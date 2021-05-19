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
package com.huawei.training.kotlin.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.training.R
import com.huawei.training.kotlin.activities.BaseActivity
import com.huawei.training.kotlin.activities.CourseDetailsActivity
import com.huawei.training.kotlin.activities.DocumentViewActivity
import com.huawei.training.kotlin.activities.HomeActivity
import com.huawei.training.kotlin.adapters.MainCourseListAdapter
import com.huawei.training.kotlin.adapters.SliderAdapter
import com.huawei.training.kotlin.database.tables.CourseDetailsTable
import com.huawei.training.kotlin.database.tables.CoursesMainCategoryTable
import com.huawei.training.kotlin.database.tables.RecentlyViewedCoursesTable
import com.huawei.training.databinding.FragmentHomeBinding
import com.huawei.training.kotlin.listeners.BannerItemClick
import com.huawei.training.kotlin.listeners.CourseItemClick
import com.huawei.training.kotlin.models.BannerModel
import com.huawei.training.kotlin.models.CourseDataModel
import com.huawei.training.kotlin.models.MainCoursesModel
import com.huawei.training.kotlin.utils.eventmanager.AnalyticsConstants
import com.huawei.training.kotlin.utils.video.Constants
import java.util.*

/**
 * @author Huawei DTSE India
 * @since 2020
 */
class HomeFragment
/**
 * Instantiates a new Home fragment.
 */
    : Fragment() {
    /**
     * The Binding.
     */
    var binding: FragmentHomeBinding? = null

    /**
     * The Main courses models.
     */
    var mainCoursesModels: MutableList<MainCoursesModel?> = ArrayList()

    /**
     * The Recently viewed courses array list.
     */
    var recentlyViewedCoursesArrayList: MutableList<RecentlyViewedCoursesTable> = ArrayList()

    /**
     * The Banner model list.
     */
    var bannerModelList: MutableList<BannerModel> = ArrayList()
    var images = intArrayOf(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setSlider()
        setData((Objects.requireNonNull(activity) as HomeActivity).mainCategoryList,
                (activity as HomeActivity?)!!.coDetailsArrayList,
                (activity as HomeActivity?)!!.recentlyViewedCoursesArrayList)
        return binding!!.root
    }

    // set slider
    private fun setSlider() {
        setBannerData()
        binding?.viewPager?.adapter = SliderAdapter(activity!!, bannerModelList, bannerItemClick)
        binding?.indicator?.setupWithViewPager(binding?.viewPager, true)
        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(), Constants.SLIDER_DELAY, Constants.SLIDER_PERIOD)
    }

    private inner class SliderTimer : TimerTask() {
        override fun run() {
            if (activity != null) {
                activity?.runOnUiThread(
                                Runnable {
                                    if (binding?.viewPager?.currentItem!! < bannerModelList.size - 1) {
                                        binding?.viewPager?.currentItem = binding!!.viewPager.currentItem + 1
                                    } else {
                                        binding?.viewPager?.currentItem = 0
                                    }
                                })
            } else {
                if (binding?.viewPager?.currentItem!! < bannerModelList.size - 1) {
                    binding?.viewPager?.currentItem = binding?.viewPager!!.currentItem + 1
                } else {
                    binding?.viewPager?.currentItem = 0
                }
            }
        }
    }

    /**
     * Set the main course and recently viewed courses titles, details
     *
     * @param mainCategories            the main categories
     * @param coDetailsList             the co details list
     * @param recentlyViewedCoursesList the recently viewed courses list
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun setData(
            mainCategories: List<CoursesMainCategoryTable>,
            coDetailsList: List<CourseDetailsTable>,
            recentlyViewedCoursesList: List<RecentlyViewedCoursesTable>) {
        mainCoursesModels.clear()
        recentlyViewedCoursesArrayList.clear()
        if (!mainCategories.isEmpty() && !coDetailsList.isEmpty()) {
            val mainCoursesModel: MainCoursesModel
            var courseDataModel: CourseDataModel
            val courseDataModelList: MutableList<CourseDataModel>
            if (recentlyViewedCoursesList.size > 0) {
                courseDataModelList = ArrayList()
                for (recentlyViewedCourses in recentlyViewedCoursesList) {
                    courseDataModel = CourseDataModel(
                            recentlyViewedCourses.courseId.toString(),
                            recentlyViewedCourses.courseName.toString(),
                            "1",
                            3f,
                            R.mipmap.hms)
                    courseDataModelList.add(courseDataModel)
                }
                if (courseDataModelList.size > 0) {
                    mainCoursesModel = MainCoursesModel(mainCategories[5]
                            .courseMainCategoryName.toString(), courseDataModelList)
                    mainCoursesModels.add(mainCoursesModel)
                }
            }
            arrangingCoursesInList(mainCategories, coDetailsList)
            if (!mainCoursesModels.isEmpty()) {
                val adapter = activity?.let {
                    MainCourseListAdapter(mainCoursesModels,
                            courseItemClick, it)
                }
                binding?.recyclerviewCourselist?.setHasFixedSize(true)
                binding?.recyclerviewCourselist?.layoutManager = LinearLayoutManager(activity)
                binding?.recyclerviewCourselist?.adapter = adapter
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun arrangingCoursesInList(mainCategories: List<CoursesMainCategoryTable>,
                                       coDetailsList: List<CourseDetailsTable>) {
        val hashSetCourseIds = Hashtable<Int, String?>()
        val hashtableMainCoursesModel = Hashtable<Int, MainCoursesModel?>()
        var courseDataModel: CourseDataModel? = null
        for (coMainCategory in mainCategories) {
            hashSetCourseIds[coMainCategory.courseMainCategoryId] = coMainCategory.courseMainCategoryName
        }
        for (coDetails in coDetailsList) {
            if (hashSetCourseIds.containsKey(coDetails.mainCategoryId)) {
                courseDataModel = CourseDataModel(
                        coDetails.courseId.toString(),
                        coDetails.courseNmae.toString(),
                        "",
                        3f,
                        R.mipmap.hms)
            }
            if (hashtableMainCoursesModel.containsKey(coDetails.mainCategoryId)) {
                if (courseDataModel != null) {
                    Objects.requireNonNull(hashtableMainCoursesModel[coDetails.mainCategoryId])?.courseDataModels?.add(courseDataModel)
                }
            } else {
                val courseModelList: MutableList<CourseDataModel> = ArrayList()
                courseModelList.add(courseDataModel!!)
                hashtableMainCoursesModel[coDetails.mainCategoryId] = MainCoursesModel(hashSetCourseIds[coDetails.mainCategoryId]!!, courseModelList)
            }
        }
        hashtableMainCoursesModel.forEach { id: Int?, mMainCoursesModel: MainCoursesModel? -> mainCoursesModels.add(mMainCoursesModel) }
    }

    /**
     * The Course item click.
     */
    // course item click
    var courseItemClick = object : CourseItemClick {
        override fun courseOnClick(model: CourseDataModel?) {
            (Objects.requireNonNull(activity) as BaseActivity).appAnalytics!!.courseClickEvent(
                    AnalyticsConstants.HOME_SCREEN, model?.name)
            val intent = Intent(activity, CourseDetailsActivity::class.java)
            intent.putExtra(Constants.COURSE_NAME, "" + model?.name)
            intent.putExtra(Constants.COURSE_ID, model?.courseId)
            startActivity(intent)
        }
    }

    // set banner data
    private fun setBannerData() {
        bannerModelList.clear()
        val names = resources.getStringArray(R.array.bannernmes)
        val urls = resources.getStringArray(R.array.urls)
        for (i in names.indices) {
            bannerModelList.add(BannerModel(names[i], urls[i], images[i]))
        }
    }

    // banner item click
    var bannerItemClick = object : BannerItemClick {
        override fun bannerClick(data: BannerModel?) {
            val intent = Intent(activity, DocumentViewActivity::class.java)
            intent.putExtra(Constants.COURSE_NAME, data?.bannerUrl)
            intent.putExtra(Constants.COURSE_DOCUMENTURL, data?.bannerName)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * New instance home fragment.
         *
         * @return the home fragment
         */
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}