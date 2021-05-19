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

package com.huawei.training.java.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.huawei.training.R;
import com.huawei.training.java.database.tables.CourseContentTable;
import com.huawei.training.java.database.tables.CoursesCodelabDetailsTable;
import com.huawei.training.java.database.tables.ExamTable;
import com.huawei.training.java.fragments.CodeLabsFragment;
import com.huawei.training.java.fragments.CourseContentListFragment;
import com.huawei.training.java.fragments.ExamFragment;
import com.huawei.training.java.models.ExamModel;
import com.huawei.training.java.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.documents, R.string.codelab, R.string.exam};
    /**
     * The Co content.
     */
    CourseContentTable coContent;
    /**
     * The Co codelab doc.
     */
    CoursesCodelabDetailsTable coCodelabDoc;
    /**
     * The Exam.
     */
    ExamTable exam;
    private final Context mContext;
    /**
     * Instantiates a new Tabs pager adapter.
     *
     * @param context      the context
     * @param fm           the fm
     * @param content      the content
     * @param coCodelabDoc the co codelab doc
     * @param exam         the exam
     */
    public TabsPagerAdapter(Context context, FragmentManager fm, CourseContentTable content,
                            CoursesCodelabDetailsTable coCodelabDoc, ExamTable exam) {
        super(fm);
        mContext = context;
        this.coContent = content;
        this.coCodelabDoc = coCodelabDoc;
        this.exam = exam;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CourseContentListFragment.newInstance(
                        AppUtils.getCourseOrCodelabContentData(coContent, null));
            case 1:
                return CodeLabsFragment.newInstance(
                        AppUtils.getCourseOrCodelabContentData(null, coCodelabDoc));
            case 2:
                return ExamFragment.newInstance(getExamModel(exam));
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    private ExamModel getExamModel(ExamTable exam) {
        ExamModel examModel = new ExamModel();
        examModel.setCId(exam.getCourseId());
        examModel.setName(exam.getExamName());
        examModel.setDesc(exam.getExamDescription());
        examModel.setDuration(exam.getExamDuration());
        examModel.setNoOfQuestions(exam.getExamNoOfQuestions());
        examModel.setTotalMarks(exam.getExamTotalMarks());
        examModel.setAttempts(exam.getExamAttempts());
        return examModel;
    }
}
