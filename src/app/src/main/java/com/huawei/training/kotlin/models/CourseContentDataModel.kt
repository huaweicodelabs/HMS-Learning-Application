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
class CourseContentDataModel : Parcelable {
    /**
     * Gets course name.
     *
     * @return the course name
     */
    /**
     * Sets course name.
     *
     * @param courseName the course name
     */
    var courseName: String?

    /**
     * Gets course url.
     *
     * @return the course url
     */
    /**
     * Sets course url.
     *
     * @param courseUrl the course url
     */
    /**
     * The Course url.
     */
    var courseUrl: String?

    /**
     * Gets course id.
     *
     * @return the course id
     */
    /**
     * Sets course id.
     *
     * @param courseId the course id
     */
    /**
     * The Course id.
     */
    var courseId: String?

    /**
     * Instantiates a new Course content data model.
     *
     * @param courseName the course name
     * @param courseUrl  the course url
     * @param courseId   the course id
     */
    constructor(courseName: String?, courseUrl: String?, courseId: String?) {
        this.courseId = courseId
        this.courseName = courseName
        this.courseUrl = courseUrl
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(courseName)
        dest.writeString(courseUrl)
        dest.writeString(courseId)
    }

    /**
     * Instantiates a new Course content data model.
     *
     * @param in the in
     */
    protected constructor(`in`: Parcel) {
        courseName = `in`.readString()
        courseUrl = `in`.readString()
        courseId = `in`.readString()
    }

    companion object {
        /**
         * The constant CREATOR.
         */
        val CREATOR: Parcelable.Creator<CourseContentDataModel?> = object : Parcelable.Creator<CourseContentDataModel?> {
            override fun createFromParcel(source: Parcel): CourseContentDataModel? {
                return CourseContentDataModel(source)
            }

            override fun newArray(size: Int): Array<CourseContentDataModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}