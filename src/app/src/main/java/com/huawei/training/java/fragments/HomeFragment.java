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
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.huawei.training.R;
import com.huawei.training.java.activities.BaseActivity;
import com.huawei.training.java.activities.CourseDetailsActivity;
import com.huawei.training.java.activities.DocumentViewActivity;
import com.huawei.training.java.activities.HomeActivity;
import com.huawei.training.java.adapters.MainCourseListAdapter;
import com.huawei.training.java.adapters.SliderAdapter;
import com.huawei.training.java.database.tables.CourseDetailsTable;
import com.huawei.training.java.database.tables.CoursesMainCategoryTable;
import com.huawei.training.java.database.tables.RecentlyViewedCoursesTable;
import com.huawei.training.databinding.FragmentHomeBinding;
import com.huawei.training.java.listeners.BannerItemClick;
import com.huawei.training.java.listeners.CourseItemClick;
import com.huawei.training.java.models.BannerModel;
import com.huawei.training.java.models.CourseDataModel;
import com.huawei.training.java.models.MainCoursesModel;
import com.huawei.training.java.utils.eventmanager.AnalyticsConstants;
import com.huawei.training.java.utils.video.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.huawei.training.java.utils.video.Constants.SLIDER_DELAY;
import static com.huawei.training.java.utils.video.Constants.SLIDER_PERIOD;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
public class HomeFragment extends Fragment {
    /**
     * The Binding.
     */
    FragmentHomeBinding binding;
    /**
     * The Main courses models.
     */
    List<MainCoursesModel> mainCoursesModels = new ArrayList<>();
    /**
     * The Recently viewed courses array list.
     */
    List<RecentlyViewedCoursesTable> recentlyViewedCoursesArrayList = new ArrayList<>();
    /**
     * The Banner model list.
     */
    List<BannerModel> bannerModelList = new ArrayList<>();

    int[] images = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4};

    /**
     * Instantiates a new Home fragment.
     */
    public HomeFragment() {
    }

    /**
     * New instance home fragment.
     *
     * @return the home fragment
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setSlider();
        setData(((HomeActivity) Objects.requireNonNull(getActivity())).mainCategoryList,
                ((HomeActivity) getActivity()).coDetailsArrayList,
                ((HomeActivity) getActivity()).recentlyViewedCoursesArrayList);
        return binding.getRoot();
    }

    // set slider
    private void setSlider() {
        setBannerData();
        binding.viewPager.setAdapter(new SliderAdapter(getActivity(),
                bannerModelList, bannerItemClick));
        binding.indicator.setupWithViewPager(binding.viewPager, true);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), SLIDER_DELAY, SLIDER_PERIOD);
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity()
                        .runOnUiThread(
                                () -> {
                                    if (binding.viewPager.getCurrentItem() <
                                            bannerModelList.size() - 1) {
                                        binding.viewPager.setCurrentItem(
                                                binding.viewPager.getCurrentItem() + 1);
                                    } else {
                                        binding.viewPager.setCurrentItem(0);
                                    }
                                });
            } else {
                if (binding.viewPager.getCurrentItem() < bannerModelList.size() - 1) {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                } else {
                    binding.viewPager.setCurrentItem(0);
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
    public void setData(
            List<CoursesMainCategoryTable> mainCategories,
            List<CourseDetailsTable> coDetailsList,
            List<RecentlyViewedCoursesTable> recentlyViewedCoursesList) {
        mainCoursesModels.clear();
        recentlyViewedCoursesArrayList.clear();

        if (!mainCategories.isEmpty() && !coDetailsList.isEmpty()) {
            MainCoursesModel mainCoursesModel;
            CourseDataModel courseDataModel;
            List<CourseDataModel> courseDataModelList;

            if (recentlyViewedCoursesList.size() > 0) {
                courseDataModelList = new ArrayList<>();
                for (RecentlyViewedCoursesTable recentlyViewedCourses : recentlyViewedCoursesList) {
                    courseDataModel =
                            new CourseDataModel(
                                    recentlyViewedCourses.getCourseId(),
                                    recentlyViewedCourses.getCourseName(),
                                    "1",
                                    3f,
                                    R.mipmap.hms);
                    courseDataModelList.add(courseDataModel);
                }
                if (courseDataModelList.size() > 0) {
                    mainCoursesModel = new MainCoursesModel(mainCategories.get(5)
                            .getCourseMainCategoryName(), courseDataModelList);
                    mainCoursesModels.add(mainCoursesModel);
                }
            }
            arrangingCoursesInList(mainCategories, coDetailsList);
            if (!mainCoursesModels.isEmpty()) {
                MainCourseListAdapter adapter =
                        new MainCourseListAdapter(mainCoursesModels,
                                courseItemClick, getActivity());
                binding.recyclerviewCourselist.setHasFixedSize(true);
                binding.recyclerviewCourselist.setLayoutManager(
                        new LinearLayoutManager(getActivity()));
                binding.recyclerviewCourselist.setAdapter(adapter);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void arrangingCoursesInList(List<CoursesMainCategoryTable> mainCategories,
                                        List<CourseDetailsTable> coDetailsList) {
        Hashtable<Integer, String> hashSetCourseIds = new Hashtable<>();
        Hashtable<Integer, MainCoursesModel> hashtableMainCoursesModel = new Hashtable<>();
        CourseDataModel courseDataModel = null;
        for (CoursesMainCategoryTable coMainCategory : mainCategories) {
            hashSetCourseIds.put(coMainCategory.getCourseMainCategoryId(),
                    coMainCategory.getCourseMainCategoryName());
        }
        for (CourseDetailsTable coDetails : coDetailsList) {
            if (hashSetCourseIds.containsKey(coDetails.getMainCategoryId())) {
                courseDataModel =
                        new CourseDataModel(
                                coDetails.getCourseId().toString(),
                                coDetails.getCourseNmae(),
                                "",
                                3f,
                                R.mipmap.hms);
            }
            if (hashtableMainCoursesModel.containsKey(coDetails.getMainCategoryId())) {
                Objects.requireNonNull(hashtableMainCoursesModel.get(
                        coDetails.getMainCategoryId())).getCourseDataModels().add(courseDataModel);
            } else {
                List<CourseDataModel> courseModelList = new ArrayList<>();
                courseModelList.add(courseDataModel);
                hashtableMainCoursesModel.put(coDetails.getMainCategoryId(),
                        new MainCoursesModel(hashSetCourseIds.get(
                                coDetails.getMainCategoryId()), courseModelList));
            }
        }
        hashtableMainCoursesModel.forEach((id, mMainCoursesModel) ->
                mainCoursesModels.add(mMainCoursesModel));
    }

    /**
     * The Course item click.
     */
// course item click
    CourseItemClick courseItemClick =
            model -> {
                ((BaseActivity) Objects.requireNonNull(getActivity()))
                        .appAnalytics.courseClickEvent(
                        AnalyticsConstants.HOME_SCREEN, model.getName());
                Intent intent = new Intent(getActivity(), CourseDetailsActivity.class);
                intent.putExtra(Constants.COURSE_NAME, "" + model.getName());
                intent.putExtra(Constants.COURSE_ID, model.getCourseId());
                startActivity(intent);
            };

    // set banner data
    private void setBannerData() {
        bannerModelList.clear();

        String[] names= getResources().getStringArray(R.array.bannernmes);
        String[] urls= getResources().getStringArray(R.array.urls);
        for(int i=0 ;i<names.length;i++){
            bannerModelList.add(new BannerModel(names[i], urls[i],images[i]));
        }
    }

    // banner item click
    BannerItemClick bannerItemClick =
            data -> {
                Intent intent = new Intent(getActivity(), DocumentViewActivity.class);
                intent.putExtra(Constants.COURSE_NAME, data.getBannerName());
                intent.putExtra(Constants.COURSE_DOCUMENTURL, data.getBannerUrl());
                startActivity(intent);
            };
}
