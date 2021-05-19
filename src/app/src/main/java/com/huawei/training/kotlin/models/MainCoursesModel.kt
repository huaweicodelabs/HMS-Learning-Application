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

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class MainCoursesModel(
        /**
         * Sets name.
         *
         * @param name the name
         */
        var name: String, courseDataModels: MutableList<CourseDataModel>) {
    /**
     * Gets name.
     *
     * @return the name
     */
    /**
     * Gets course data models.
     *
     * @return the course data models
     */
    /**
     * Sets course data models.
     *
     * @param courseDataModels the course data models
     */
    var courseDataModels: MutableList<CourseDataModel>? = null

    /**
     * Instantiates a new Main courses model.
     *
     * @param name             the name
     * @param courseDataModels the course data models
     */
    init {
        this.courseDataModels = courseDataModels
    }
}