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
import android.widget.RadioButton;

import androidx.databinding.DataBindingUtil;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.training.R;
import com.huawei.training.java.database.CloudDbAction;
import com.huawei.training.java.database.CloudDbUiCallbackListener;
import com.huawei.training.java.database.tables.QuestionsTable;
import com.huawei.training.databinding.ActivityExamBinding;
import com.huawei.training.java.models.QuestionModel;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.video.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class ExamActivity extends BaseActivity implements CloudDbUiCallbackListener {
    /**
     * The Binding.
     */
    ActivityExamBinding binding;
    /**
     * The Questions list.
     */
    List<QuestionModel> questions_list = new ArrayList<>();
    /**
     * The Current question model.
     */
    QuestionModel currentQuestionModel;
    /**
     * The Score.
     */
    private int score = 0;
    /**
     * The Wrong.
     */
    private int wrong = 0;

    /**
     * The Quid.
     */
    private int quid = 0;
    /**
     * The Coursename.
     */
    private String coursename = "";
    /**
     * The Course id.
     */
    private int courseId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam);
        coursename = getIntent().getStringExtra(Constants.COURSE_NAME);
        courseId = Integer.parseInt(Objects.requireNonNull(
                getIntent().getStringExtra(Constants.COURSE_ID)));
        cloudDbHelper.addCallBackListener(this);
        setToolbar(getResources().getString(R.string.exam));
        init();
    }

    private void init() {
        AppUtils.showLoadingDialog(this);
        queryCourseExamQuestions();
        binding.next.setOnClickListener(
                start -> {
                    RadioButton answer = findViewById(
                            binding.questionRadioGroup.getCheckedRadioButtonId());
                    if (answer == null) {
                        showToast(getString(R.string.select_ans));
                    }
                    if (answer != null) {
                        if (currentQuestionModel.getAnswer().equals(answer.getText())) {
                            score++;
                        } else {
                            wrong++;
                        }
                        if (quid < questions_list.size()) {
                            currentQuestionModel = questions_list.get(quid);
                            setQuestionView();
                        } else {
                            navigatetoResult();
                        }
                    }
                });
    }

    // bind questions data to views
    private void setQuestionView() {
        binding.questionRadioGroup.clearCheck();
        currentQuestionModel = questions_list.get(quid);
        if(currentQuestionModel!=null) {
            if(currentQuestionModel.getQuestion()!=null) {
                binding.question.setText(currentQuestionModel.getQuestion());
            }
            if(currentQuestionModel.getOptA()!=null) {
                binding.radio0.setText(currentQuestionModel.getOptA());
            }
            if(currentQuestionModel.getOptB()!=null) {
                binding.radio1.setText(currentQuestionModel.getOptB());
            }
            if(currentQuestionModel.getOptC()!=null) {
                binding.radio2.setText(currentQuestionModel.getOptC());
            }
            if(currentQuestionModel.getOptD()!=null) {
                binding.radio3.setText(currentQuestionModel.getOptD());
            }
        }

        quid++;
    }

    // add data to list
    private void setCourseData(@NotNull List<QuestionsTable> questionsList) {
        for (int i = 0; i < questionsList.size(); i++) {
            questions_list.add(
                    new QuestionModel(
                            i + 1 + ". " + questionsList.get(i).getQuestion(),
                            questionsList.get(i).getOptionA(),
                            questionsList.get(i).getOptionB(),
                            questionsList.get(i).getOptionC(),
                            questionsList.get(i).getOptionD(),
                            questionsList.get(i).getAnswer()));
        }
    }

    @Override
    public void onSuccessDbData(CloudDbAction cloudDbAction, List<CloudDBZoneObject> dataList) {
        AppUtils.cancelLoading();
        if (cloudDbAction == CloudDbAction.GET_QUESTIONS) {
            setCourseData((List) dataList);
            setQuestionView();
        }
    }

    @Override
    public void onSuccessDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        AppUtils.cancelLoading();
    }

    @Override
    public void onFailureDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        AppUtils.cancelLoading();
        showToast(message);
    }

    private void queryCourseExamQuestions() {
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(QuestionsTable.class);
        cloudDBZoneQuery.equalTo("courseId", courseId);
        cloudDbHelper.getCloudDbQueyCalls().queryQuestionsTable(cloudDBZoneQuery, CloudDbAction.GET_QUESTIONS);
    }

    // navigate to result activity
    private void navigatetoResult() {
        Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.SCORE_CORRECT, score);
        bundle.putInt(Constants.SCORE_WRONG, wrong);
        bundle.putInt(Constants.TOTAL_QUESTIONS, questions_list.size());
        intent.putExtra(Constants.COURSE_NAME, coursename);
        intent.putExtra(Constants.COURSE_ID, "" + courseId);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
