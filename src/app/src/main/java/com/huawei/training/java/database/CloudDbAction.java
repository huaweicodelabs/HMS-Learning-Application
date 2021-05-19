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

package com.huawei.training.java.database;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public enum CloudDbAction {
    /**
     * Db login cloud db action.
     */
    DB_LOGIN,
    /**
     * Open db cloud db action.
     */
    OPEN_DB,
    /**
     * Insert user info cloud db action.
     */
    INSERT_USER_INFO,
    /**
     * Get user cloud db action.
     */
    GET_USER,
    /**
     * Get all courses cloud db action.
     */
    GET_ALL_COURSES,
    /**
     * Get main categories cloud db action.
     */
    GET_MAIN_CATEGORIES,
    /**
     * Get recently viewed courses cloud db action.
     */
    GET_RECENTLY_VIEWED_COURSES,
    /**
     * Get all recently viewed courses cloud db action.
     */
    GET_ALL_RECENTLY_VIEWED_COURSES,
    /**
     * Get recently viewed courses msg cloud db action.
     */
    GET_RECENTLY_VIEWED_COURSES_MSG,
    /**
     * Get course content cloud db action.
     */
    GET_COURSE_CONTENT,
    /**
     * Get course platforms cloud db action.
     */
    GET_COURSE_PLATFORMS,
    /**
     * Get course doc cloud db action.
     */
    GET_COURSE_DOC,
    /**
     * Get exam details cloud db action.
     */
    GET_EXAM_DETAILS,
    /**
     * Get questions cloud db action.
     */
    GET_QUESTIONS,
    /**
     * Get code lab cloud db action.
     */
    GET_CODE_LAB,
    /**
     * Get my courses cloud db action.
     */
    GET_MY_COURSES,
    /**
     * Get my courses query cloud db action.
     */
    GET_MY_COURSES_QUERY
}
