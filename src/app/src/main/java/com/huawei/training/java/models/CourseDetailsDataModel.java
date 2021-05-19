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

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class CourseDetailsDataModel {
    private String name;
    /**
     * The Course id.
     */
    private String courseId;
    /**
     * The Price.
     */
    private String price;

    /**
     * The Rating.
     */
    private Float rating;

    /**
     * Instantiates a new Course details data model.
     *
     * @param name     the name
     * @param courseId the course id
     * @param price    the price
     * @param rating   the rating
     */
    public CourseDetailsDataModel(String name, String courseId, String price, Float rating) {
        this.courseId = courseId;
        this.name = name;
        this.price = price;
        this.rating = rating;
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

    /**
     * Gets price.
     *
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Gets rating.
     *
     * @return the rating
     */
    public Float getRating() {
        return rating;
    }

    /**
     * Sets rating.
     *
     * @param rating the rating
     */
    public void setRating(Float rating) {
        this.rating = rating;
    }
}
