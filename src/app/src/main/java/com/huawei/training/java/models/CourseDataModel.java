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
public class CourseDataModel {
    private String name;
    /**
     * The Price.
     */
    private String price;
    /**
     * The Course id.
     */
    private String courseId;

    private int imgId;
    /**
     * The Rating.
     */
    private Float rating;

    /**
     * Instantiates a new Course data model.
     *
     * @param courseId the course id
     * @param name     the name
     * @param price    the price
     * @param rating   the rating
     * @param imgId    the img id
     */
    public CourseDataModel(String courseId, String name, String price, Float rating, int imgId) {
        this.courseId = courseId;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.imgId = imgId;
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
     * Gets ratings.
     *
     * @return the ratings
     */
    public Float getRatings() {
        return rating;
    }

    /**
     * Sets ratings.
     *
     * @param ratings the ratings
     */
    public void setRatings(Float ratings) {
        this.rating = ratings;
    }

    /**
     * Gets img id.
     *
     * @return the img id
     */
    public int getImgId() {
        return imgId;
    }

    /**
     * Sets img id.
     *
     * @param imgId the img id
     */
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
