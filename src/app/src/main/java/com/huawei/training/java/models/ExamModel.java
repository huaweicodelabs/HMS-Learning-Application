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

package com.huawei.training.java.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class ExamModel implements Parcelable {
    private Integer id;

    private Integer c_id;

    private String name;

    private String desc;

    private Integer duration;

    private Integer no_of_questions;

    private Integer total_marks;

    private Integer pass_marks;

    private Integer attempts;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.c_id);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeValue(this.duration);
        dest.writeValue(this.no_of_questions);
        dest.writeValue(this.total_marks);
        dest.writeValue(this.pass_marks);
        dest.writeValue(this.attempts);
    }

    /**
     * Instantiates a new Exam model.
     */
    public ExamModel() {
    }

    /**
     * Instantiates a new Exam model.
     *
     * @param in the in
     */
    protected ExamModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.c_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.desc = in.readString();
        this.duration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.no_of_questions = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total_marks = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pass_marks = (Integer) in.readValue(Integer.class.getClassLoader());
        this.attempts = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    /**
     * The constant CREATOR.
     */
    public static final Parcelable.Creator<ExamModel> CREATOR =
            new Parcelable.Creator<ExamModel>() {
                @Override
                public ExamModel createFromParcel(Parcel source) {
                    return new ExamModel(source);
                }

                @Override
                public ExamModel[] newArray(int size) {
                    return new ExamModel[size];
                }
            };

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets c id.
     *
     * @return the c id
     */
    public Integer getCId() {
        return c_id;
    }

    /**
     * Sets c id.
     *
     * @param c_id the c id
     */
    public void setCId(Integer c_id) {
        this.c_id = c_id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Gets no of questions.
     *
     * @return the no of questions
     */
    public Integer getNoOfQuestions() {
        return no_of_questions;
    }

    /**
     * Sets no of questions.
     *
     * @param no_of_questions the no of questions
     */
    public void setNoOfQuestions(Integer no_of_questions) {
        this.no_of_questions = no_of_questions;
    }

    /**
     * Gets total marks.
     *
     * @return the total marks
     */
    public Integer getTotalMarks() {
        return total_marks;
    }

    /**
     * Sets total marks.
     *
     * @param total_marks the total marks
     */
    public void setTotalMarks(Integer total_marks) {
        this.total_marks = total_marks;
    }

    /**
     * Gets pass marks.
     *
     * @return the pass marks
     */
    public Integer getPassMarks() {
        return pass_marks;
    }

    /**
     * Sets pass marks.
     *
     * @param pass_marks the pass marks
     */
    public void setPassMarks(Integer pass_marks) {
        this.pass_marks = pass_marks;
    }

    /**
     * Gets attempts.
     *
     * @return the attempts
     */
    public Integer getAttempts() {
        return attempts;
    }

    /**
     * Sets attempts.
     *
     * @param attempts the attempts
     */
    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }
}
