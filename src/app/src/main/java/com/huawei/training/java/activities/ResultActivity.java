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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.huawei.training.R;
import com.huawei.training.databinding.ActivityResultBinding;
import com.huawei.training.java.utils.video.Constants;

import java.util.Objects;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class ResultActivity extends BaseActivity {
    /**
     * The Binding.
     */
    ActivityResultBinding binding;
    /**
     * The Coursename.
     */
    private String coursename = "";
    /**
     * The Course id.
     */
    private int courseId = 0;
    /**
     * The Score correct.
     */
    private int score_correct = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result);
        setToolbar(getResources().getString(R.string.result));
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        score_correct = bundle.getInt(Constants.SCORE_CORRECT);
        int score_wrong = bundle.getInt(Constants.SCORE_WRONG);
        int total = bundle.getInt(Constants.TOTAL_QUESTIONS);
        binding.ratingBar1.setRating(score_correct);
        binding.totalQuestionsNum.setText("" + total);
        binding.correctAnswersNum.setText("" + score_correct);
        binding.wrongAnswersNum.setText("" + score_wrong);
        coursename = getIntent().getStringExtra(Constants.COURSE_NAME);
        courseId = Integer.parseInt(Objects.requireNonNull(
                getIntent().getStringExtra(Constants.COURSE_ID)));
        binding.finish.setOnClickListener(view -> {
            if (binding.finish.getText().toString().equalsIgnoreCase(getResources().getString(R.string.finish))) {
                Intent intent = new Intent(
                        ResultActivity.this, CourseDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.FINISHSTATUS, true);
                intent.putExtra(Constants.COURSE_NAME, coursename);
                intent.putExtra(Constants.COURSE_ID, "" + courseId);
                startActivity(intent);
            }
            finish();
        });
        setData();
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        if (score_correct > 3) {
            binding.finish.setText("" + getResources().getString(R.string.finish));
        } else {
            binding.finish.setText("" + getResources().getString(R.string.tryagain));
        }
    }

}
