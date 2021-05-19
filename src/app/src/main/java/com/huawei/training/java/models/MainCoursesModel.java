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

import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class MainCoursesModel {
    private String name;
    private List<CourseDataModel> courseDataModels = null;

    /**
     * Instantiates a new Main courses model.
     *
     * @param name             the name
     * @param courseDataModels the course data models
     */
    public MainCoursesModel(String name, List<CourseDataModel> courseDataModels) {
        this.name = name;
        this.courseDataModels = courseDataModels;
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
     * Gets course data models.
     *
     * @return the course data models
     */
    public List<CourseDataModel> getCourseDataModels() {
        return courseDataModels;
    }

    /**
     * Sets course data models.
     *
     * @param courseDataModels the course data models
     */
    public void setCourseDataModels(List<CourseDataModel> courseDataModels) {
        this.courseDataModels = courseDataModels;
    }
}
