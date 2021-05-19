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

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.huawei.training.R
import com.huawei.training.kotlin.utils.video.Constants
import kotlinx.android.synthetic.main.activity_result.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class ResultActivity : BaseActivity() {

    /**
     * The Coursename.
     */
    private var coursename: String? = ""

    /**
     * The Course id.
     */
    private var courseId = 0

    /**
     * The Score correct.
     */
    private var score_correct = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setToolbar(resources.getString(R.string.result))
        val bundle = intent?.extras!!
        score_correct = bundle.getInt(Constants.SCORE_CORRECT)
        val score_wrong = bundle.getInt(Constants.SCORE_WRONG)
        val total = bundle.getInt(Constants.TOTAL_QUESTIONS)
        ratingBar1.rating = score_correct.toFloat()
        total_questions_num.text = "" + total
        correct_answers_num.text = "" + score_correct
        wrong_answers_num.text = "" + score_wrong
        coursename = intent.getStringExtra(Constants.COURSE_NAME)
        courseId = intent.getStringExtra(Constants.COURSE_ID)?.toInt()!!
        finish.setOnClickListener { view: View? ->
            if (finish.text.toString().equals(resources.getString(R.string.finish), ignoreCase = true)) {
                val intent = Intent(
                        this@ResultActivity, CourseDetailsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(Constants.FINISHSTATUS, true)
                intent.putExtra(Constants.COURSE_NAME, coursename)
                intent.putExtra(Constants.COURSE_ID, "" + courseId)
                startActivity(intent)
            }
            finish()
        }
        setData()
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        if (score_correct > 3) {
            finish.text = "" + resources.getString(R.string.finish)
        } else {
            finish.text = "" + resources.getString(R.string.tryagain)
        }
    }
}