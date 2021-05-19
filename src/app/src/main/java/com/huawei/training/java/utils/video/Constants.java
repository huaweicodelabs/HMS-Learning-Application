/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.training.java.utils.video;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
public class Constants {
    /**
     * The constant PUSH_TOKEN.
     */
    public static String PUSH_TOKEN = "push_token";
    /**
     * The constant COURSE_NAME.
     */
    public static final String COURSE_NAME = "course_name";
    /**
     * The constant COURSE_DOCUMENTURL.
     */
    public static final String COURSE_DOCUMENTURL = "course_documenturl";
    /**
     * The constant COURSE_ID.
     */
    public static final String COURSE_ID = "course-id";
    /**
     * The constant VIDEO_PLAY_DATA.
     */
    public static final String VIDEO_PLAY_DATA = "video_play_data";
    /**
     * The constant REQUEST_SIGN_IN_LOGIN_CODE.
     */
    public static final int REQUEST_SIGN_IN_LOGIN_CODE = 1003;
    /**
     * The constant UID.
     */
    public static final String UID = "UID";

    public static final String PRICE = "500";
    public static final int PROGRESS = 100;
    public static final int AD_TIMEOUT = 5000;
    public static final int MSG_AD_TIMEOUT = 1001;

    /**
     * The constant DELAY_MILLIS_500.
     */
   // The delay time
    public static final long DELAY_MILLIS_500 = 500;

    /**
     * The constant DELAY_MILLIS_3000.
     */
    public static final long DELAY_MILLIS_3000 = 3000;
    public static final long SLIDER_DELAY = 4000;
    public static final long SLIDER_PERIOD = 6000;

    /**
     * The constant DELAY_MILLIS_1000.
     */
    public static final long DELAY_MILLIS_1000 = 1000;

    /**
     * The constant HEIGHT_DP.
     */
// Under the vertical screen SurfaceView height
    public static final float HEIGHT_DP = 300;

    /**
     * The constant PLAYING_WHAT.
     */
// present Current Position
    public static final int PLAYING_WHAT = 1;

    /**
     * The constant UPDATE_PLAY_STATE.
     */
// After the completion of the play status update button
    public static final int UPDATE_PLAY_STATE = 4;

    /**
     * The constant PLAY_ERROR_FINISH.
     */
// Receive the onError news out of the current page
    public static final int PLAY_ERROR_FINISH = 5;

    /**
     * The constant PLAYER_SWITCH_STOP_REQUEST_STREAM.
     */
// Switch bitrate
    public static final int PLAYER_SWITCH_STOP_REQUEST_STREAM = 7;

    /**
     * The constant MSG_SETTING.
     */
// Setting
    public static final int MSG_SETTING = 8;

    /**
     * The constant PLAYER_SWITCH_PLAY_SPEED.
     */
// Switch speed
    public static final int PLAYER_SWITCH_PLAY_SPEED = 9;

    /**
     * The constant PLAYER_SWITCH_BITRATE.
     */
// Switch bitrate
    public static final int PLAYER_SWITCH_BITRATE = 10;

    /**
     * The constant PLAYER_SWITCH_AUTO_DESIGNATED.
     */
// Smooth/Designated cutting rate
    public static final int PLAYER_SWITCH_AUTO_DESIGNATED = 11;

    /**
     * The constant PLAYER_SWITCH_BANDWIDTH_MODE.
     */
// Set the bandwidth adaptive switch
    public static final int PLAYER_SWITCH_BANDWIDTH_MODE = 12;

    /**
     * The constant PLAYER_SWITCH_PLAY_MODE.
     */
// Switch the audio and video
    public static final int PLAYER_SWITCH_PLAY_MODE = 13;

    /**
     * The constant PLAYER_SWITCH_LOOP_PLAY_MODE.
     */
// Set the looping
    public static final int PLAYER_SWITCH_LOOP_PLAY_MODE = 14;

    /**
     * The constant PLAYER_SWITCH_VIDEO_MUTE_MODE.
     */
// Set the mute
    public static final int PLAYER_SWITCH_VIDEO_MUTE_MODE = 15;

    /**
     * The constant PLAYER_SWITCH_VIDEO_MODE.
     */
// Set the video type
    public static final int PLAYER_SWITCH_VIDEO_MODE = 0;

    /**
     * The constant PLAYER_SWITCH_VIDEO_VIEW.
     */
// Set the play View
    public static final int PLAYER_SWITCH_VIDEO_VIEW = 1;

    /**
     * The constant PLAYER_SWITCH_VIDEO_MUTE.
     */
// Set the mute
    public static final int PLAYER_SWITCH_VIDEO_MUTE = 3;

    /**
     * The constant PLAYER_SWITCH_VIDEO_PLAY.
     */
// Set the play mode
    public static final int PLAYER_SWITCH_VIDEO_PLAY = 4;

    /**
     * The constant PLAYER_SWITCH_BANDWIDTH.
     */
// Set the bandwidth adaptive
    public static final int PLAYER_SWITCH_BANDWIDTH = 5;

    /**
     * The constant PLAYER_SWITCH_INIT_BANDWIDTH.
     */
// Set the bitrate
    public static final int PLAYER_SWITCH_INIT_BANDWIDTH = 6;

    /**
     * The constant PLAYER_SWITCH_CLOSE_LOGO.
     */
// Set off the logo
    public static final int PLAYER_SWITCH_CLOSE_LOGO = 7;

    /**
     * The constant PLAYER_SWITCH_CLOSE_LOGO_EFFECT.
     */
// Close the logo under the influence of all the sources or sources
    public static final int PLAYER_SWITCH_CLOSE_LOGO_EFFECT = 8;

    /**
     * The constant DIALOG_INDEX_ONE.
     */
// The first options dialog
    public static final int DIALOG_INDEX_ONE = 0;

    /**
     * The constant DIALOG_INDEX_TWO.
     */
// The second options dialog
    public static final int DIALOG_INDEX_TWO = 1;

    /**
     * The constant VIDEO_TYPE_LIVE.
     */
// Set play type live
    public static final int VIDEO_TYPE_LIVE = 1;

    /**
     * The constant VIDEO_TYPE_ON_DEMAND.
     */
// Set play type demand
    public static final int VIDEO_TYPE_ON_DEMAND = 0;

    /**
     * The type Url type.
     */
// Url type
    public static class UrlType {
        /**
         * The constant URL.
         */
// A single play address
        public static final int URL = 0;

        /**
         * The constant URL_MULTIPLE.
         */
// Multiple play address
        public static final int URL_MULTIPLE = 1;

        /**
         * The constant URL_JSON.
         */
// Huawei managed video address
        public static final int URL_JSON = 2;
    }

    /**
     * The constant SCORE_CORRECT.
     */
    public static final String SCORE_CORRECT = "score_correct";
    /**
     * The constant SCORE_WRONG.
     */
    public static final String SCORE_WRONG = "score_wrong";
    /**
     * The constant TOTAL_QUESTIONS.
     */
    public static final String TOTAL_QUESTIONS = "total_questions";
    /**
     * The constant FINISHSTATUS.
     */
    public static final String FINISHSTATUS = "finishstatus";
}
