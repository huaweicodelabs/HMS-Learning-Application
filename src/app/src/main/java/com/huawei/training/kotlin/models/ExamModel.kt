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
package com.huawei.training.kotlin.models

import android.os.Parcel
import android.os.Parcelable

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class ExamModel : Parcelable {
    /**
     * Gets id.
     *
     * @return the id
     */
    /**
     * Sets id.
     *
     * @param id the id
     */
    var id: Int? = null
    /**
     * Gets c id.
     *
     * @return the c id
     */
    /**
     * Sets c id.
     *
     * @param c_id the c id
     */
    var cId: Int? = null
    /**
     * Gets name.
     *
     * @return the name
     */
    /**
     * Sets name.
     *
     * @param name the name
     */
    var name: String? = null
    /**
     * Gets desc.
     *
     * @return the desc
     */
    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    var desc: String? = null
    /**
     * Gets duration.
     *
     * @return the duration
     */
    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    var duration: Int? = null
    /**
     * Gets no of questions.
     *
     * @return the no of questions
     */
    /**
     * Sets no of questions.
     *
     * @param no_of_questions the no of questions
     */
    var noOfQuestions: Int? = null
    /**
     * Gets total marks.
     *
     * @return the total marks
     */
    /**
     * Sets total marks.
     *
     * @param total_marks the total marks
     */
    var totalMarks: Int? = null
    /**
     * Gets pass marks.
     *
     * @return the pass marks
     */
    /**
     * Sets pass marks.
     *
     * @param pass_marks the pass marks
     */
    var passMarks: Int? = null
    /**
     * Gets attempts.
     *
     * @return the attempts
     */
    /**
     * Sets attempts.
     *
     * @param attempts the attempts
     */
    var attempts: Int? = null
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(cId)
        dest.writeString(name)
        dest.writeString(desc)
        dest.writeValue(duration)
        dest.writeValue(noOfQuestions)
        dest.writeValue(totalMarks)
        dest.writeValue(passMarks)
        dest.writeValue(attempts)
    }

    /**
     * Instantiates a new Exam model.
     */
    constructor() {}

    /**
     * Instantiates a new Exam model.
     *
     * @param in the in
     */
    protected constructor(`in`: Parcel) {
        id = `in`.readValue(Int::class.java.classLoader) as Int?
        cId = `in`.readValue(Int::class.java.classLoader) as Int?
        name = `in`.readString()
        desc = `in`.readString()
        duration = `in`.readValue(Int::class.java.classLoader) as Int?
        noOfQuestions = `in`.readValue(Int::class.java.classLoader) as Int?
        totalMarks = `in`.readValue(Int::class.java.classLoader) as Int?
        passMarks = `in`.readValue(Int::class.java.classLoader) as Int?
        attempts = `in`.readValue(Int::class.java.classLoader) as Int?
    }

    companion object {
        /**
         * The constant CREATOR.
         */
        val CREATOR: Parcelable.Creator<ExamModel?> = object : Parcelable.Creator<ExamModel?> {
            override fun createFromParcel(source: Parcel): ExamModel? {
                return ExamModel(source)
            }

            override fun newArray(size: Int): Array<ExamModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}