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
package com.huawei.training.kotlin.adapters

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.huawei.training.R
import com.huawei.training.kotlin.database.tables.CourseContentTable
import com.huawei.training.kotlin.database.tables.CoursesCodelabDetailsTable
import com.huawei.training.kotlin.database.tables.ExamTable
import com.huawei.training.kotlin.fragments.CodeLabsFragment
import com.huawei.training.kotlin.fragments.CourseContentListFragment
import com.huawei.training.kotlin.fragments.ExamFragment
import com.huawei.training.kotlin.models.ExamModel
import com.huawei.training.kotlin.utils.AppUtils

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class TabsPagerAdapter
/**
 * Instantiates a new Tabs pager adapter.
 *
 * @param context      the context
 * @param fm           the fm
 * @param content      the content
 * @param coCodelabDoc the co codelab doc
 * @param exam         the exam
 */(private val mContext: Context, fm: FragmentManager?,
    /**
     * The Co content.
     */
    var coContent: CourseContentTable,
    /**
     * The Co codelab doc.
     */
    var coCodelabDoc: CoursesCodelabDetailsTable,
    /**
     * The Exam.
     */
    var exam: ExamTable) : FragmentPagerAdapter(fm!!) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CourseContentListFragment.newInstance(
                    AppUtils.getCourseOrCodelabContentData(coContent, null))
            1 -> CodeLabsFragment.newInstance(
                    AppUtils.getCourseOrCodelabContentData(null, coCodelabDoc))
            2 -> ExamFragment.newInstance(getExamModel(exam))
            else -> CourseContentListFragment.newInstance(
                    AppUtils.getCourseOrCodelabContentData(coContent, null))
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }

    private fun getExamModel(exam: ExamTable): ExamModel {
        val examModel = ExamModel()
        examModel.cId = exam.courseId
        examModel.name = exam.examName
        examModel.desc = exam.examDescription
        examModel.duration = exam.examDuration
        examModel.noOfQuestions = exam.examNoOfQuestions
        examModel.totalMarks = exam.examTotalMarks
        examModel.attempts = exam.examAttempts
        return examModel
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.documents, R.string.codelab, R.string.exam)
    }

}