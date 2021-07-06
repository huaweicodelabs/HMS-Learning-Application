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

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.training.R
import com.huawei.training.kotlin.database.CloudDbHelper
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.CloudDbUiCallbackListener
import com.huawei.training.kotlin.database.tables.QuestionsTable
import com.huawei.training.kotlin.models.QuestionModel
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.video.Constants
import kotlinx.android.synthetic.main.activity_exam.*
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class ExamActivity : BaseActivity(), CloudDbUiCallbackListener {
    /**
     * The Questions list.
     */
    var questions_list: MutableList<QuestionModel> = ArrayList()

    /**
     * The Current question model.
     */
    var currentQuestionModel: QuestionModel? = null

    /**
     * The Score.
     */
    private var score = 0

    /**
     * The Wrong.
     */
    private var wrong = 0

    /**
     * The Quid.
     */
    private var quid = 0

    /**
     * The Coursename.
     */
    private var coursename: String? = ""

    /**
     * The Course id.
     */
    private var courseId = 0

    /**
     * Gets cloud db helper.
     *
     * @return the cloud db helper
     */
    var cloudDbHelper: CloudDbHelper?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)
        coursename = intent.getStringExtra(Constants.COURSE_NAME)
        courseId = intent.getStringExtra(Constants.COURSE_ID)?.toInt()!!

        cloudDbHelper= CloudDbHelper.getInstance(applicationContext)
        cloudDbHelper?.addCallBackListener(this)
        setToolbar(resources.getString(R.string.exam))
        init()
    }

    private fun init() {
        AppUtils.showLoadingDialog(this)
        queryCourseExamQuestions()
        next.setOnClickListener { start: View? ->
            val answer = questionRadioGroup?.checkedRadioButtonId?.let {
                findViewById<RadioButton>(it)
            }
            if (answer == null) {
                showToast(getString(R.string.select_ans))
            }
            if (answer != null) {
                if (currentQuestionModel?.answer == answer.text) {
                    score++
                } else {
                    wrong++
                }
                if (quid < questions_list.size) {
                    currentQuestionModel = questions_list[quid]
                    setQuestionView()
                } else {
                    navigatetoResult()
                }
            }
        }
    }

    // bind questions data to views
    private fun setQuestionView() {
        questionRadioGroup?.clearCheck()
        currentQuestionModel = questions_list[quid]
        if (currentQuestionModel != null) {
            if (currentQuestionModel?.question != null) {
                question.text = currentQuestionModel?.question
            }
            if (currentQuestionModel?.optA != null) {
                radio0.text = currentQuestionModel?.optA
            }
            if (currentQuestionModel?.optB != null) {
                radio1.text = currentQuestionModel?.optB
            }
            if (currentQuestionModel?.optC != null) {
                radio2.text = currentQuestionModel?.optC
            }
            if (currentQuestionModel?.optD != null) {
                radio3.text = currentQuestionModel?.optD
            }
        }
        quid++
    }

    // add data to list
    private fun setCourseData(questionsList: List<QuestionsTable>) {
        for (i in questionsList.indices) {
            questions_list.add(
                    QuestionModel(
                            ""+(i + 1 )+". " + questionsList[i].question,
                            questionsList[i].optionA.toString(),
                            questionsList[i].optionB.toString(),
                            questionsList[i].optionC.toString(),
                            questionsList[i].optionD.toString(),
                            questionsList[i].answer.toString()))
        }
    }

    override fun onSuccessDbData(cloudDbAction: CloudDbAction?, dataList: List<CloudDBZoneObject?>?) {
        AppUtils.cancelLoading()
        if (cloudDbAction == CloudDbAction.GET_QUESTIONS) {
            setCourseData(dataList as List<QuestionsTable>)
            setQuestionView()
        }
    }

    override fun onSuccessDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        AppUtils.cancelLoading()
    }

    override fun onFailureDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        AppUtils.cancelLoading()
        showToast(message)
    }

    private fun queryCourseExamQuestions() {
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(QuestionsTable::class.java)
        cloudDBZoneQuery.equalTo("courseId", courseId)
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryQuestionsTable(cloudDBZoneQuery as CloudDBZoneQuery<QuestionsTable>?, CloudDbAction.GET_QUESTIONS)
    }

    // navigate to result activity
    private fun navigatetoResult() {
        val intent = Intent(this@ExamActivity, ResultActivity::class.java)
        val bundle = Bundle()
        bundle.putInt(Constants.SCORE_CORRECT, score)
        bundle.putInt(Constants.SCORE_WRONG, wrong)
        bundle.putInt(Constants.TOTAL_QUESTIONS, questions_list.size)
        intent.putExtra(Constants.COURSE_NAME, coursename)
        intent.putExtra(Constants.COURSE_ID, "" + courseId)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}