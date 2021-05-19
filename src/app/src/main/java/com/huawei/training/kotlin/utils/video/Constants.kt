/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.training.kotlin.utils.video

/**
 * @author Huawei DTSE India
 * @since 2020
 */
object Constants {
    /**
     * The constant PUSH_TOKEN.
     */
    var PUSH_TOKEN = "push_token"

    /**
     * The constant COURSE_NAME.
     */
    const val COURSE_NAME = "course_name"

    /**
     * The constant COURSE_DOCUMENTURL.
     */
    const val COURSE_DOCUMENTURL = "course_documenturl"

    /**
     * The constant COURSE_ID.
     */
    const val COURSE_ID = "course-id"

    /**
     * The constant VIDEO_PLAY_DATA.
     */
    const val VIDEO_PLAY_DATA = "video_play_data"

    /**
     * The constant REQUEST_SIGN_IN_LOGIN_CODE.
     */
    const val REQUEST_SIGN_IN_LOGIN_CODE = 1003

    /**
     * The constant UID.
     */
    const val UID = "UID"
    const val PRICE = "500"
    const val PROGRESS = 100
    const val AD_TIMEOUT = 5000
    const val MSG_AD_TIMEOUT = 1001

    /**
     * The constant DELAY_MILLIS_500.
     */
    // The delay time
    const val DELAY_MILLIS_500: Long = 500

    /**
     * The constant DELAY_MILLIS_3000.
     */
    const val DELAY_MILLIS_3000: Long = 3000
    const val SLIDER_DELAY: Long = 4000
    const val SLIDER_PERIOD: Long = 6000

    /**
     * The constant DELAY_MILLIS_1000.
     */
    const val DELAY_MILLIS_1000: Long = 1000

    /**
     * The constant HEIGHT_DP.
     */
    // Under the vertical screen SurfaceView height
    const val HEIGHT_DP = 300f

    /**
     * The constant PLAYING_WHAT.
     */
    // present Current Position
    const val PLAYING_WHAT = 1

    /**
     * The constant UPDATE_PLAY_STATE.
     */
    // After the completion of the play status update button
    const val UPDATE_PLAY_STATE = 4

    /**
     * The constant PLAY_ERROR_FINISH.
     */
    // Receive the onError news out of the current page
    const val PLAY_ERROR_FINISH = 5

    /**
     * The constant PLAYER_SWITCH_STOP_REQUEST_STREAM.
     */
    // Switch bitrate
    const val PLAYER_SWITCH_STOP_REQUEST_STREAM = 7

    /**
     * The constant MSG_SETTING.
     */
    // Setting
    const val MSG_SETTING = 8

    /**
     * The constant PLAYER_SWITCH_PLAY_SPEED.
     */
    // Switch speed
    const val PLAYER_SWITCH_PLAY_SPEED = 9

    /**
     * The constant PLAYER_SWITCH_BITRATE.
     */
    // Switch bitrate
    const val PLAYER_SWITCH_BITRATE = 10

    /**
     * The constant PLAYER_SWITCH_AUTO_DESIGNATED.
     */
    // Smooth/Designated cutting rate
    const val PLAYER_SWITCH_AUTO_DESIGNATED = 11

    /**
     * The constant PLAYER_SWITCH_BANDWIDTH_MODE.
     */
    // Set the bandwidth adaptive switch
    const val PLAYER_SWITCH_BANDWIDTH_MODE = 12

    /**
     * The constant PLAYER_SWITCH_PLAY_MODE.
     */
    // Switch the audio and video
    const val PLAYER_SWITCH_PLAY_MODE = 13

    /**
     * The constant PLAYER_SWITCH_LOOP_PLAY_MODE.
     */
    // Set the looping
    const val PLAYER_SWITCH_LOOP_PLAY_MODE = 14

    /**
     * The constant PLAYER_SWITCH_VIDEO_MUTE_MODE.
     */
    // Set the mute
    const val PLAYER_SWITCH_VIDEO_MUTE_MODE = 15

    /**
     * The constant PLAYER_SWITCH_VIDEO_MODE.
     */
    // Set the video type
    const val PLAYER_SWITCH_VIDEO_MODE = 0

    /**
     * The constant PLAYER_SWITCH_VIDEO_VIEW.
     */
    // Set the play View
    const val PLAYER_SWITCH_VIDEO_VIEW = 1

    /**
     * The constant PLAYER_SWITCH_VIDEO_MUTE.
     */
    // Set the mute
    const val PLAYER_SWITCH_VIDEO_MUTE = 3

    /**
     * The constant PLAYER_SWITCH_VIDEO_PLAY.
     */
    // Set the play mode
    const val PLAYER_SWITCH_VIDEO_PLAY = 4

    /**
     * The constant PLAYER_SWITCH_BANDWIDTH.
     */
    // Set the bandwidth adaptive
    const val PLAYER_SWITCH_BANDWIDTH = 5

    /**
     * The constant PLAYER_SWITCH_INIT_BANDWIDTH.
     */
    // Set the bitrate
    const val PLAYER_SWITCH_INIT_BANDWIDTH = 6

    /**
     * The constant PLAYER_SWITCH_CLOSE_LOGO.
     */
    // Set off the logo
    const val PLAYER_SWITCH_CLOSE_LOGO = 7

    /**
     * The constant PLAYER_SWITCH_CLOSE_LOGO_EFFECT.
     */
    // Close the logo under the influence of all the sources or sources
    const val PLAYER_SWITCH_CLOSE_LOGO_EFFECT = 8

    /**
     * The constant DIALOG_INDEX_ONE.
     */
    // The first options dialog
    const val DIALOG_INDEX_ONE = 0

    /**
     * The constant DIALOG_INDEX_TWO.
     */
    // The second options dialog
    const val DIALOG_INDEX_TWO = 1

    /**
     * The constant VIDEO_TYPE_LIVE.
     */
    // Set play type live
    const val VIDEO_TYPE_LIVE = 1

    /**
     * The constant VIDEO_TYPE_ON_DEMAND.
     */
    // Set play type demand
    const val VIDEO_TYPE_ON_DEMAND = 0

    /**
     * The constant SCORE_CORRECT.
     */
    const val SCORE_CORRECT = "score_correct"

    /**
     * The constant SCORE_WRONG.
     */
    const val SCORE_WRONG = "score_wrong"

    /**
     * The constant TOTAL_QUESTIONS.
     */
    const val TOTAL_QUESTIONS = "total_questions"

    /**
     * The constant FINISHSTATUS.
     */
    const val FINISHSTATUS = "finishstatus"

    /**
     * The type Url type.
     */
    // Url type
    object UrlType {
        /**
         * The constant URL.
         */
        // A single play address
        const val URL = 0

        /**
         * The constant URL_MULTIPLE.
         */
        // Multiple play address
        const val URL_MULTIPLE = 1

        /**
         * The constant URL_JSON.
         */
        // Huawei managed video address
        const val URL_JSON = 2
    }
}