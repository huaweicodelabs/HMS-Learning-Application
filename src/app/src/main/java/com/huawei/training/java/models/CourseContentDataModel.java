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
public class CourseContentDataModel implements Parcelable {
    private String courseName;
    /**
     * The Course url.
     */
    private String courseUrl;
    /**
     * The Course id.
     */
    private String courseId;

    /**
     * Instantiates a new Course content data model.
     *
     * @param courseName the course name
     * @param courseUrl  the course url
     * @param courseId   the course id
     */
    public CourseContentDataModel(String courseName, String courseUrl, String courseId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseUrl = courseUrl;
    }

    /**
     * Gets course name.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets course name.
     *
     * @param courseName the course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets course url.
     *
     * @return the course url
     */
    public String getCourseUrl() {
        return courseUrl;
    }

    /**
     * Sets course url.
     *
     * @param courseUrl the course url
     */
    public void setCourseUrl(String courseUrl) {
        this.courseUrl = courseUrl;
    }

    /**
     * Gets course id.
     *
     * @return the course id
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Sets course id.
     *
     * @param courseId the course id
     */
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courseName);
        dest.writeString(this.courseUrl);
        dest.writeString(this.courseId);
    }

    /**
     * Instantiates a new Course content data model.
     *
     * @param in the in
     */
    protected CourseContentDataModel(Parcel in) {
        this.courseName = in.readString();
        this.courseUrl = in.readString();
        this.courseId = in.readString();
    }

    /**
     * The constant CREATOR.
     */
    public static final Parcelable.Creator<CourseContentDataModel> CREATOR =
            new Parcelable.Creator<CourseContentDataModel>() {
                @Override
                public CourseContentDataModel createFromParcel(Parcel source) {
                    return new CourseContentDataModel(source);
                }

                @Override
                public CourseContentDataModel[] newArray(int size) {
                    return new CourseContentDataModel[size];
                }
            };
}
