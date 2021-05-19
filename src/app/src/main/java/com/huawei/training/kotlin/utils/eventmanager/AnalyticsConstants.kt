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
package com.huawei.training.kotlin.utils.eventmanager

/**
 * @since 2020
 * @author Huawei DTSE India
 */
interface AnalyticsConstants {
    companion object {
        /**
         * The constant COURSE_CLICKED_EVENT.
         */
        const val COURSE_CLICKED_EVENT = "COURSE_CLICKED_EVENT"

        /**
         * The constant COURSE_NAME_PROP.
         */
        const val COURSE_NAME_PROP = "COURSE_NAME"

        /**
         * The constant COURSE_ID_PROP.
         */
        const val COURSE_ID_PROP = "COURSE_ID"

        /**
         * The constant SOURCE_PROP.
         */
        const val SOURCE_PROP = "SOURCE"

        /**
         * The constant CODE_LAB_CLICKED_EVENT.
         */
        const val CODE_LAB_CLICKED_EVENT = "CODELAB_CLICKED_EVENT"

        /**
         * The constant CODE_LAB_SUB_TOPIC_PROP.
         */
        const val CODE_LAB_SUB_TOPIC_PROP = "CODELAB_SUB_TOPICNAME"

        /**
         * The constant START_EXAM_CLICKED_EVENT.
         */
        const val START_EXAM_CLICKED_EVENT = "START_CLICKED_EVENT"

        /**
         * The constant START_EXAM_CLICKED_PROP.
         */
        const val START_EXAM_CLICKED_PROP = "START_EXAM_CLICKED_PROP"

        /**
         * The constant USER_MMAIL_PROP.
         */
        const val USER_MMAIL_PROP = "USER_MAIL_PROP"

        /**
         * The constant START_COURSE_CLICKED_EVENT.
         */
        const val START_COURSE_CLICKED_EVENT = "START_COURSE_EVENT"

        /**
         * The constant SHARE_COURSE_CLICKED_EVENT.
         */
        const val SHARE_COURSE_CLICKED_EVENT = "SHARE_COURSE_CLICKED_EVENT"

        /**
         * The constant ADD_TO_MY_COURSE_CLICKED_EVENT.
         */
        const val ADD_TO_MY_COURSE_CLICKED_EVENT = "ADD_TO_MY_COURSE_CLICKED_EVENT"

        /**
         * The constant TAB_HOME.
         */
        /* ******** Event Names ****************** */
        const val TAB_HOME = "Home"

        /**
         * The constant TAB_PROFILE.
         */
        const val TAB_PROFILE = "My Profile"

        /**
         * The constant TAB_MY_COURSE.
         */
        const val TAB_MY_COURSE = "My Courses"

        /**
         * The constant HOME_SCREEN.
         */
        const val HOME_SCREEN = "HOME_SCREEN"

        /**
         * The constant MY_COURSES_SCREEN.
         */
        const val MY_COURSES_SCREEN = "MY_COURSES_SCREEN" /* ************************************** */
    }
}